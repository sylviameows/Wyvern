package net.sylviameows.wyvern.components;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.WyvernAPI;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.role.options.DynamicOptions;
import net.sylviameows.wyvern.api.role.options.RoleOptions;
import net.sylviameows.wyvern.game.configuration.WyvernRoleOptions;
import net.sylviameows.wyvern.game.roles.CivilianRole;
import net.sylviameows.wyvern.game.roles.KillerRole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.*;

public class WeightsComponent implements AutoSyncedComponent {
    public static final ComponentKey<WeightsComponent> KEY = ComponentRegistry.getOrCreate(Wyvern.id("weights"), WeightsComponent.class);
    public final Scoreboard scoreboard;
    public final @Nullable MinecraftServer server;

    private final HashMap<UUID, RoleWeights> map = new HashMap<>();

    public WeightsComponent(Scoreboard scoreboard, @Nullable MinecraftServer server) {
        this.scoreboard = scoreboard;
        this.server = server;
    }

    public RoleWeights getWeights(PlayerEntity player) {
        if (map.containsKey(player.getUuid())) {
            return map.get(player.getUuid());
        } else {
            RoleWeights weights = new RoleWeights();
            map.put(player.getUuid(), weights);
            return weights;
        }
    }

    public int assignRoles(List<ServerPlayerEntity> players, GameWorldComponent game, ConfigurationComponent config) {
        List<PlayerEntity> candidates = new ArrayList<>(players);

        int killerCount = assignKillers(candidates, game, config);
        assignOthers(candidates, game, config, killerCount);
        assignCivilians(candidates, game, config, killerCount);

        return killerCount;
    }

    public void assignCivilians(List<PlayerEntity> players, GameWorldComponent game, ConfigurationComponent config, int killerCount) {
        List<Role> roles = WyvernAPI.roles().getRoles().stream().filter(role -> role.alignment() == Alignment.INNOCENT).toList();

        List<Role> selectedRoles = new ArrayList<>();
        Role defaultRole = WyvernAPI.roles().get(CivilianRole.IDENTIFIER);

        for (Role role : roles) {
            RoleOptions options = config.getOptions(role);

            int count = options.count();
            if (options instanceof WyvernRoleOptions.DynamicRoleOptions(DynamicOptions setting)) {
                if (setting == DynamicOptions.MATCH_KILLERS) {
                    count = killerCount;
                }

                if (setting == DynamicOptions.ALL_UNASSIGNED) {
                    defaultRole = role;
                    continue;
                }
            } else if (!options.enabled() || options.ignored()) {
                continue;
            }

            for (var i = 0; i < count; i++) {
                if (options.odds() == 1f) {
                    selectedRoles.add(role);
                } else if (server.getOverworld().getRandom().nextFloat() <= options.odds()) {
                    selectedRoles.add(role);
                }
            }
        }

        if (players.size() < selectedRoles.size()) {
            Collections.shuffle(selectedRoles);
            while (selectedRoles.size() > players.size()) {
                selectedRoles.removeLast();
            }
        }

        assignSpecial(players, game, selectedRoles, defaultRole);

    }

    public void assignOthers(List<PlayerEntity> players, GameWorldComponent game, ConfigurationComponent config, int killerCount) {
        List<Role> roles = WyvernAPI.roles().getRoles().stream().filter(role -> role.alignment() == Alignment.OTHER).toList();

        List<Role> selectedRoles = new ArrayList<>();

        for (Role role : roles) {
            RoleOptions options = config.getOptions(role);

            int count = options.count();
            if (options instanceof WyvernRoleOptions.DynamicRoleOptions(DynamicOptions setting)) {
                if (setting == DynamicOptions.MATCH_KILLERS) {
                    count = killerCount;
                }

                if (setting == DynamicOptions.ALL_UNASSIGNED) {
                    throw new IllegalStateException("ALL_UNASSIGNED options are not supported for OTHER type roles.");
                }
            } else if (!options.enabled() || options.ignored()) {
                continue;
            }

            for (var i = 0; i < count; i++) {
                if (server.getOverworld().getRandom().nextFloat() <= options.odds()) {
                    selectedRoles.add(role);
                }
            }
        }

        int count = selectedRoles.size();

        // others selection
        int total = 0;
        Map<PlayerEntity, Integer> candidates = new HashMap<>();
        for (var player : players) {
            int weight = getWeights(player).other();
            total += weight;
            candidates.put(player, weight);
        }

        List<PlayerEntity> others = new ArrayList<>();
        assignAlignment(players, count, total, candidates, others);

        if (players.size() < selectedRoles.size()) {
            Collections.shuffle(selectedRoles);
            while (selectedRoles.size() > players.size()) {
                selectedRoles.removeLast();
            }
        }

        for (Role role : selectedRoles) {
            PlayerEntity player = assignWeighted(role, players);
            if (player == null) continue;
            getWeights(player).assign(role);
            game.addRole(player, WyvernAPI.roles().getWathe(role.id()));
        }
    }

    public int assignKillers(List<PlayerEntity> players, GameWorldComponent game, ConfigurationComponent config) {
        double calc = players.size() * config.killerRatio;
        if (calc < 1) calc = 1;
        else calc = (int) Math.floor(calc);
        int count = (int) calc;

        // killer selection
        int total = 0;
        Map<PlayerEntity, Integer> candidates = new HashMap<>();
        for (var player : players) {
            int weight = getWeights(player).killer();
            total += weight;
            candidates.put(player, weight);
        }

        List<PlayerEntity> killers = new ArrayList<>();
        assignAlignment(players, count, total, candidates, killers);

        // role rolling
        List<Role> killerRoles = WyvernAPI.roles().getRoles().stream().filter(role -> role.alignment() == Alignment.KILLER).toList();

        List<Role> selectedRoles = new ArrayList<>();
        Role defaultRole = WyvernAPI.roles().get(KillerRole.IDENTIFIER);

        for (Role killerRole : killerRoles) {
            RoleOptions options = config.getOptions(killerRole);

            if (options instanceof WyvernRoleOptions.DynamicRoleOptions(DynamicOptions setting)) {
                if (setting == DynamicOptions.MATCH_KILLERS) {
                    throw new IllegalStateException("MATCH_KILLERS options are not supported for KILLER type roles.");
                } else if (setting == DynamicOptions.ALL_UNASSIGNED) {
                    defaultRole = killerRole;
                }
                continue;
            } else if (!options.enabled() || options.ignored()) {
                continue;
            }

            for (var i = 0; i < options.count(); i++) {
                if (server.getOverworld().getRandom().nextFloat() <= options.odds()) {
                    selectedRoles.add(killerRole);
                }
            }
        }

        if (count < selectedRoles.size()) {
            Collections.shuffle(selectedRoles);
            while (selectedRoles.size() > count) {
                selectedRoles.removeLast();
            }
        }

        assignSpecial(killers, game, selectedRoles, defaultRole);

        return count;
    }

    private void assignAlignment(List<PlayerEntity> candidates, int alignedCount, int totalWeights, Map<PlayerEntity, Integer> weights, List<PlayerEntity> aligned) {
        List<PlayerEntity> toRemove = new ArrayList<>();
        for (var i = 0; i < alignedCount; i++) {
            int selection = server.getOverworld().getRandom().nextInt(totalWeights);
            for (var entry : weights.entrySet()) {
                selection -= entry.getValue();
                if (selection <= 0) {
                    aligned.add(entry.getKey());
                    totalWeights -= entry.getValue();
                    toRemove.add(entry.getKey());
                    break;
                }
            }
        }

        toRemove.forEach(player -> {
            weights.remove(player);
            candidates.remove(player);
        });
    }

    private void assignSpecial(List<PlayerEntity> candidates, GameWorldComponent game, List<Role> availableRoles, Role defaultRole) {
        for (Role role : availableRoles) {
            PlayerEntity player = assignWeighted(role, candidates);
            if (player == null) continue;
            getWeights(player).assign(role);
            game.addRole(player, WyvernAPI.roles().getWathe(role.id()));
        }

        while (!candidates.isEmpty()) {
            if (defaultRole == null) break;
            PlayerEntity player = candidates.removeFirst();
            getWeights(player).assign(defaultRole);
            game.addRole(player, WyvernAPI.roles().getWathe(defaultRole.id()));
        }
    }

    private PlayerEntity assignWeighted(Role role, List<PlayerEntity> players) {
        Wyvern.LOGGER.info("Assigning {} to a player...", role.id());
        int total = 0;
        Map<PlayerEntity, Integer> candidates = new HashMap<>();
        for (var player : players) {
            int weight = getWeights(player).role(role);
            total += weight;
            candidates.put(player, weight);
        }

        int selection = server.getOverworld().getRandom().nextInt(total);
        for (var entry : candidates.entrySet()) {
            selection -= entry.getValue();
            if (selection <= 0) {
                PlayerEntity candidate = entry.getKey();
                players.remove(candidate);
                Wyvern.LOGGER.info("Selected {} as the player.", candidate.getName().toString());
                return candidate;
            }
        }

        Wyvern.LOGGER.info("Selected no player???");
        return null;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {

    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {

    }

    public static class RoleWeights {
        // team weights
        private int killer = 1;
        private int other = 1;

        // role weights
        private final Map<Role, Integer> roles = new HashMap<>();

        public RoleWeights() {
            for (Role role : WyvernAPI.roles().getRoles()) {
                if (!role.unique()) continue;
                roles.putIfAbsent(role, 1);
            }
        }

        public RoleWeights(NbtCompound tag) {
            killer = tag.getInt("killer");
            other = tag.getInt("other");

            NbtCompound roles = tag.getCompound("roles");
            for (String key : roles.getKeys()) {
                Identifier id = Identifier.of(key);
                Role role = WyvernAPI.roles().get(id);
                if (role != null) {
                    this.roles.put(role, roles.getInt(key));
                }
            }
        }

        public void assign(Role role) {
            handleRoleWeights(role);

            if (role.alignment() == Alignment.INNOCENT) {
                killer++;
                other++;
            } else if (role.alignment() == Alignment.KILLER) {
                killer = 1;
                other++;
            } else {
                killer++;
                other = 1;
            }
        }

        private void handleRoleWeights(Role assignedRole) {
            roles.forEach((role,weight) -> roles.compute(role, (_role, _weight) -> {
                if (role == assignedRole) return 1; // reset role weight
                if (role.alignment() == assignedRole.alignment()) {
                    return weight + 1; // increment matching alignment roles.
                }
                return weight; // dont increment not matching alignments.
            }));
        }

        public NbtCompound toNbt() {
            NbtCompound tag = new NbtCompound();

            tag.putInt("killer", killer);
            tag.putInt("other", other);

            NbtCompound roles = new NbtCompound();
            for (String key : roles.getKeys()) {
                Identifier id = Identifier.of(key);
                Role role = WyvernAPI.roles().get(id);
                if (role != null && role.unique()) {
                    this.roles.put(role, roles.getInt(key));
                }
            }

            tag.put("roles", roles);

            return tag;
        }

        public int killer() {
            return killer;
        }

        public int other() {
            return other;
        }

        public int role(Role role) {
            return roles.get(role);
        }
    }

}
