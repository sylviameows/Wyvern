package net.sylviameows.wyvern.api.instinct;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.entity.PlayerBodyEntity;
import dev.doctor4t.wathe.game.GameFunctions;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.WyvernColors;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.util.Time;
import net.sylviameows.wyvern.api.util.WatheMigrator;

import java.awt.*;

public final class Instincts {

    private Instincts() {}

    /**
     * Handles instinct for player entities.
     * @param player the target player entity.
     * @param spectator how the result will be displayed. <br>
     *                - if set to <code>false</code>: killer cohort will be visible alongside their red outlines. <br>
     *                - if set to <code>true</code> role names will be visible alongside role colors. <br>
     * @return the instinct result.
     */
    public static InstinctResult player(PlayerEntity player, boolean spectator) {
        if (GameFunctions.isPlayerSpectatingOrCreative(player)) return null;
        GameWorldComponent game = GameWorldComponent.KEY.get(player.getWorld());

        var wathe = game.getRole(player);
        if (wathe == null) return null;

        Role role = WatheMigrator.migrateRole(wathe);

        int roleColor;
        Text roleName;

        if (role != null) {
            roleColor = role.color();
            roleName = role.announcement().getName();

            if (!spectator && role.alignment() == Alignment.KILLER) {
                return new InstinctResult(WyvernColors.KILLER).addLine(Text.translatable("game.tip.cohort").withColor(WyvernColors.KILLER));
            }
        } else {
            // fallbacks
            roleColor = wathe.color();
            roleName = Text.translatable("announcement.title.%s".formatted(wathe.identifier().getPath())).withColor(wathe.color());

            if (!spectator && wathe.canUseKiller()) {
                return new InstinctResult(WyvernColors.KILLER).addLine(Text.translatable("game.tip.cohort").withColor(WyvernColors.KILLER));
            }
        }


        PlayerMoodComponent mood = PlayerMoodComponent.KEY.get(player);
        int color = spectator ? roleColor : WyvernColors.CIVILIAN;
        float[] hsb = Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, (color & 0xFF), null);
        if (mood.isLowerThanDepressed()) {
            hsb[2] = Math.min(hsb[2] * 0.3f, 1f);
        } else if (mood.isLowerThanMid()) {
            hsb[2] = Math.min(hsb[2] * 0.6f, 1f);
        }

        InstinctResult result = new InstinctResult(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
        if (spectator) {
            return result.addLine(roleName);
        }
        return result;
    }

    public static InstinctResult body(PlayerBodyEntity body, boolean showAge, boolean showRole) {
        InstinctResult result = new InstinctResult(WyvernColors.BODY);

        PlayerListEntry playerListEntry = WatheClient.PLAYER_ENTRIES_CACHE.get(body.getPlayerUuid());
        if (playerListEntry != null) {
            Text name = playerListEntry.getDisplayName();
            if (name == null) name = Text.literal(playerListEntry.getProfile().getName());
            result.setNametag(name);
        }

        if (showRole) {
            GameWorldComponent game = GameWorldComponent.KEY.get(body.getWorld());
            var wathe = game.getRole(body.getPlayerUuid());
            if (wathe != null) {
                Role role = WatheMigrator.migrateRole(wathe);
                if (role != null) {
                    result.addLine(role.announcement().getName());
                } else {
                    result.addLine(Text.translatable("announcement.title.%s".formatted(wathe.identifier().getPath())).withColor(wathe.color()));
                }
            }
        }

        if (showAge) {
            String age = Time.ticksToString(body.age);
            result.addLine(Text.literal(age).withColor(WyvernColors.RED));
        }


        return result;
    }

}
