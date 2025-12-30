package net.sylviameows.wyvern;

import dev.doctor4t.wathe.cca.*;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.game.gamemode.MurderGameMode;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.WyvernAPI;
import net.sylviameows.wyvern.api.result.WinResult;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.shop.Shop;
import net.sylviameows.wyvern.components.ConfigurationComponent;
import net.sylviameows.wyvern.components.ResultComponent;
import net.sylviameows.wyvern.components.WeightsComponent;
import net.sylviameows.wyvern.game.GameResults;
import net.sylviameows.wyvern.mixin.components.PlayerMoodComponentAccessor;
import net.sylviameows.wyvern.payloads.BoardPayload;
import net.sylviameows.wyvern.game.roles.CivilianRole;
import net.sylviameows.wyvern.api.util.WatheMigrator;

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
                component.addRole(player, WyvernAPI.roles().getWathe(CivilianRole.IDENTIFIER));
            }
            var role = WatheMigrator.migrateRole(component.getRole(player));
            if (role == null) continue;

            role.assign(player);
            Shop shop = role.settings().getShop();
            if (shop != null) PlayerShopComponent.KEY.get(player).setBalance(shop.getStartingBalance());

            ((PlayerMoodComponentAccessor) PlayerMoodComponent.KEY.get(player)).wyvern$setMoodHandler(role.getMoodHandler());
            ServerPlayNetworking.send(player, new BoardPayload(role.id(), killerCount));
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

}
