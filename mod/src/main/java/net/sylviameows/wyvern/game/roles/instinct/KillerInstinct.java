package net.sylviameows.wyvern.game.roles.instinct;

import dev.doctor4t.wathe.entity.FirecrackerEntity;
import dev.doctor4t.wathe.entity.NoteEntity;
import dev.doctor4t.wathe.entity.PlayerBodyEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.sylviameows.wyvern.api.WyvernColors;
import net.sylviameows.wyvern.api.instinct.Instinct;
import net.sylviameows.wyvern.api.instinct.InstinctResult;
import net.sylviameows.wyvern.api.instinct.Instincts;
import org.jetbrains.annotations.Nullable;

public class KillerInstinct implements Instinct {

    @Override
    public @Nullable InstinctResult resolve(Entity entity) {

        if (entity instanceof ItemEntity || entity instanceof NoteEntity || entity instanceof FirecrackerEntity) {
            return new InstinctResult(WyvernColors.ITEM);
        }

        if (entity instanceof PlayerEntity player) {
            return Instincts.player(player, false);
        }

        if (entity instanceof PlayerBodyEntity body) {
            return Instincts.body(body, true, false);
        }

        return null;
    }

}
