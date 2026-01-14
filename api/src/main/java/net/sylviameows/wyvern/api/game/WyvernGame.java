package net.sylviameows.wyvern.api.game;

import net.minecraft.entity.player.PlayerEntity;
import net.sylviameows.wyvern.api.result.WinResult;

import java.util.UUID;

public interface WyvernGame {

    default WyvernPlayer getPlayer(PlayerEntity player) {
        return getPlayer(player.getUuid());
    }
    WyvernPlayer getPlayer(UUID uuid);

    void end(WinResult result);

}
