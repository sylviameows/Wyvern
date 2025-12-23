package net.sylviameows.wyvern;

import dev.doctor4t.wathe.cca.*;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.game.gamemode.MurderGameMode;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.WyvernAPI;
import net.sylviameows.wyvern.api.result.KillerWin;
import net.sylviameows.wyvern.api.result.PassengerWin;
import net.sylviameows.wyvern.api.result.WinResult;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.components.ConfigurationComponent;
import net.sylviameows.wyvern.components.ResultComponent;
import net.sylviameows.wyvern.components.WeightsComponent;
import net.sylviameows.wyvern.mixin.components.PlayerMoodComponentAccessor;
import net.sylviameows.wyvern.payloads.BoardPayload;
import net.sylviameows.wyvern.game.roles.CivilianRole;
import net.sylviameows.wyvern.util.migration.WatheMigrator;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class WyvernGamemode extends MurderGameMode {

    public static final Identifier IDENTIFIER = Wyvern.id("murder");

    public WyvernGamemode() {
        super(IDENTIFIER);
    }

    @Override
    public void initializeGame(ServerWorld world, GameWorldComponent component, List<ServerPlayerEntity> players) {
        TrainWorldComponent.KEY.get(world).setTimeOfDay(TrainWorldComponent.TimeOfDay.NIGHT);
        WeightsComponent weights = WeightsComponent.KEY.get(world.getScoreboard());

        int killerCount = weights.assignRoles(players, component, ConfigurationComponent.KEY.get(world));

        for (ServerPlayerEntity player : players) {
            if (component.getRole(player) == null) {
                Wyvern.LOGGER.warn("{} was not assigned a role!", player.getName());
                component.addRole(player, WyvernAPI.roles().getHarpy(CivilianRole.IDENTIFIER));
            }
            var role = WatheMigrator.migrateRole(component.getRole(player));
            if (role == null) continue;

            role.assign(player);
            ((PlayerMoodComponentAccessor) PlayerMoodComponent.KEY.get(player)).wyvern$setMoodHandler(role.getMoodHandler());
            ServerPlayNetworking.send(player, new BoardPayload(role.id(), killerCount));
        }
    }

    public int assignRoles(@NotNull ServerWorld world, @NotNull List<ServerPlayerEntity> players, GameWorldComponent component) {
        ConfigurationComponent config = ConfigurationComponent.KEY.get(world);
        double calc = players.size() * config.killerRatio;
        if (calc < 1) calc = 1;
        else calc = (int) Math.floor(calc);
        int count = (int) calc;

        WeightsComponent weights = WeightsComponent.KEY.get(world.getScoreboard());

        List<PlayerEntity> killers = new ArrayList<>();

        {
            // killer selection
            int total = 0;
            Map<PlayerEntity, Integer> candidates = new HashMap<>();
            for (var player : players) {
                int weight = weights.getWeights(player).killer();
                total += weight;
                candidates.put(player, weight);
            }

            for (var i = 0; i < count; i++) {
                int selection = world.getRandom().nextInt(total);
                for (var entry : candidates.entrySet()) {
                    selection -= entry.getValue();
                    if (selection <= 0) {
                        killers.add(entry.getKey());
                        total -= entry.getValue();
                        candidates.remove(entry.getKey());
                    }
                }
            }
        }
        {

        }

        List<WeightsComponent.RoleWeights> list = players.stream().map(weights::getWeights).toList();

        return killers.size();
    }

    @Override
    public void tickServerGameLoop(ServerWorld world, GameWorldComponent game) {
        WinResult result = null;

        // check if out of time
        if (!GameTimeComponent.KEY.get(world).hasTime())
            result = PassengerWin.TIME;

        boolean civilianAlive = false;
        boolean killerAlive = false;
        for (ServerPlayerEntity player : world.getPlayers()) {
            var harpy = game.getRole(player);
            Role role = WatheMigrator.migrateRole(harpy);
            if (role == null) continue;

            if (role.alignment() != Alignment.KILLER) {
                if (!GameFunctions.isPlayerEliminated(player)) civilianAlive = true;
            } else {
                // passive income
                Integer balanceToAdd = GameConstants.PASSIVE_MONEY_TICKER.apply(world.getTime());
                if (balanceToAdd > 0) PlayerShopComponent.KEY.get(player).addToBalance(balanceToAdd);

                if (!GameFunctions.isPlayerEliminated(player)) killerAlive = true;
            }
        }

        // check killer win condition (killed all civilians)
        if (!civilianAlive) {
            result = KillerWin.ELIMINATED;
        }

        // check passenger win condition (all killers are dead)
        if (result == null && !killerAlive) {
            result = PassengerWin.ELIMINATED;
        }

        // set result and stop game
        if (result != null && game.getGameStatus() == GameWorldComponent.GameStatus.ACTIVE) {
            ResultComponent.KEY.get(world).setResult(world.getPlayers(), result);
            GameFunctions.stopGame(world);
        }
    }

}
