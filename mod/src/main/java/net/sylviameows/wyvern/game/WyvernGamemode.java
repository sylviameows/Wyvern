package net.sylviameows.wyvern.game;

import dev.doctor4t.wathe.cca.*;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.game.gamemode.MurderGameMode;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.game.WyvernGame;
import net.sylviameows.wyvern.api.game.WyvernPlayer;
import net.sylviameows.wyvern.api.result.WinResult;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.shop.Shop;
import net.sylviameows.wyvern.components.ConfigurationComponent;
import net.sylviameows.wyvern.components.ResultComponent;
import net.sylviameows.wyvern.components.WeightsComponent;
import net.sylviameows.wyvern.mixin.components.PlayerMoodComponentAccessor;
import net.sylviameows.wyvern.payloads.BoardPayload;
import net.sylviameows.wyvern.game.roles.CivilianRole;
import net.sylviameows.wyvern.api.util.WatheMigrator;

import java.util.*;

public final class WyvernGamemode extends MurderGameMode implements WyvernGame {

    public static final Identifier IDENTIFIER = Wyvern.id("murder");

    private final Map<UUID, WyvernPlayer> players = new HashMap<>();

    public WyvernGamemode() {
        super(IDENTIFIER);
    }

    @Override
    public void initializeGame(ServerWorld world, GameWorldComponent component, List<ServerPlayerEntity> players) {
        this.players.clear();

        players.forEach(player -> this.players.put(player.getUuid(), new WyvernPlayerImpl(player)));

        WeightsComponent weights = WeightsComponent.KEY.get(world.getScoreboard());
        int killerCount = weights.assignRoles(this.players.values(), ConfigurationComponent.KEY.get(world));

        for (WyvernPlayer player : this.players.values()) {
            if (player.role() == null) {
                player.role(new CivilianRole());
            }
            Role role = player.role();

            role.assign(player.entity());
            Shop shop = role.settings().getShop();

            if (shop != null) player.balance(shop.getStartingBalance());

            ((PlayerMoodComponentAccessor) PlayerMoodComponent.KEY.get(player.entity())).wyvern$setMoodHandler(role.getMoodHandler());
            ServerPlayNetworking.send((ServerPlayerEntity) player.entity(), new BoardPayload(role.id(), killerCount));
        }
    }

    @Override
    public void tickServerGameLoop(ServerWorld world, GameWorldComponent game) {
        WinResult result = null;

        // check if out of time
        if (!GameTimeComponent.KEY.get(world).hasTime())
            result = GameResults.passengerByTime;

        boolean civilianAlive = false;
        boolean killerAlive = false;
        for (ServerPlayerEntity player : world.getPlayers()) {
            var harpy = game.getRole(player);
            Role role = WatheMigrator.migrateRole(harpy);
            if (role == null) continue;

            role.tick(player);

            if (role.alignment() != Alignment.KILLER) {
                if (!GameFunctions.isPlayerEliminated(player)) civilianAlive = true;
            } else {
                if (!GameFunctions.isPlayerEliminated(player)) killerAlive = true;
            }
        }

        // check killer win condition (killed all civilians)
        if (!civilianAlive) {
            result = GameResults.killerByEliminations;
        }

        // check passenger win condition (all killers are dead)
        if (result == null && !killerAlive) {
            result = GameResults.passengerByEliminations;
        }

        // set result and stop game
        if (result != null && game.getGameStatus() == GameWorldComponent.GameStatus.ACTIVE) {
            ResultComponent.KEY.get(world).setResult(world.getPlayers(), result);
            GameFunctions.stopGame(world);
        }
    }

    @Override
    public WyvernPlayer getPlayer(UUID uuid) {
        return this.players.get(uuid);
    }

    @Override
    public void end(WinResult result) {

    }
}
