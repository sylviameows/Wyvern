package net.sylviameows.wyvern.components;

import com.mojang.authlib.GameProfile;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.game.GameFunctions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.WyvernAPI;
import net.sylviameows.wyvern.api.result.WinResult;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.game.roles.CivilianRole;
import net.sylviameows.wyvern.util.WatheMigrator;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Equivalent of the {@link dev.doctor4t.wathe.cca.GameRoundEndComponent} from Wathe, but to support Wyvern's custom roles and give more control.
 */
public class ResultComponent implements AutoSyncedComponent {

    public static final ComponentKey<ResultComponent> KEY = ComponentRegistry.getOrCreate(Wyvern.id("result"), ResultComponent.class);
    private final World world;

    private WinResult result;
    private final List<PlayerEntry> players = new ArrayList<>();

    public ResultComponent(World world) {
        this.world = world;
    }

    public void sync() {
        KEY.sync(this.world);
    }

    public WinResult getResult() {
        return this.result;
    }

    public List<PlayerEntry> getPlayers() {
        return this.players;
    }

    private PlayerEntry getEntry(PlayerEntity player) {
        for (PlayerEntry entry : this.players) {
            if (!entry.profile.getId().equals(player.getGameProfile().getId())) continue;
            return entry;
        }
        return null;
    }

    public Text getTitleText(PlayerEntity player) {
        if (result == null) return Text.empty();

        PlayerEntry entry = getEntry(player);
        if (entry == null) return result.getDefaultTitle();

        return entry.role().announcement().title(player, result);
    }

    public Text getReasonText() {
        if (result == null) return Text.empty();
        return result.getReasonText();
    }

    public boolean isWinner(PlayerEntity player) {
        PlayerEntry entry = getEntry(player);
        if (entry == null || result == null) return false;

        return result.isWinner(entry.role, player);
    }

    public void setResult(@NotNull List<ServerPlayerEntity> players, WinResult result) {
        GameWorldComponent game = GameWorldComponent.KEY.get(this.world);

        this.players.clear();
        for (ServerPlayerEntity player : players) {
            Role role = WyvernAPI.roles().get(CivilianRole.IDENTIFIER);
            boolean dead = GameFunctions.isPlayerEliminated(player);

            var harpy = game.getRole(player);
            if (harpy != null) {
                role = WatheMigrator.migrateRole(harpy);
            } else {
                dead = true;
            }

            this.players.add(new PlayerEntry(player.getGameProfile(), role, dead));
        }

        this.result = result;

        this.sync();
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
        NbtCompound resultNbt = tag.getCompound("result");
        if (!tag.isEmpty()) {
            this.result = WyvernAPI.results().get(Identifier.of(resultNbt.getString("id")));

            NbtCompound resultDataNbt = resultNbt.getCompound("data");
            if (!resultDataNbt.isEmpty()) result.readNbt(resultDataNbt);
        }

        this.players.clear();
        for (var element : tag.getList("players", 10)) this.players.add(new PlayerEntry((NbtCompound) element));
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
        if (result != null) {
            NbtCompound resultNbt = new NbtCompound();
            resultNbt.putString("id", this.result.getIdentifier().toString());

            NbtCompound resultDataNbt = new NbtCompound();
            this.result.writeNbt(resultDataNbt);

            if (!resultDataNbt.isEmpty()) {
                resultNbt.put("data", resultDataNbt);
            }

            tag.put("result", resultNbt);
        }

        NbtList players = new NbtList();
        this.players.forEach(player -> players.add(player.writeToNbt()));
        tag.put("players", players);
    }

    public record PlayerEntry(GameProfile profile, Role role, boolean dead) {
        private PlayerEntry(@NotNull NbtCompound tag) {
            this(
                    new GameProfile(tag.getUuid("uuid"), tag.getString("name")),
                    WyvernAPI.roles().get(Identifier.of(tag.getString("role"))),
                    tag.getBoolean("dead")
            );
        }

        public @NotNull NbtCompound writeToNbt() {
            var tag = new NbtCompound();
            tag.putUuid("uuid", this.profile.getId());
            tag.putString("name", this.profile.getName());
            tag.putString("role", this.role.id().toString());
            tag.putBoolean("dead", this.dead);
            return tag;
        }
    }

}
