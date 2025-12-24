package net.sylviameows.wyvern.api.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Hands {

    static boolean insertStackInFreeSlot(@NotNull PlayerEntity player, ItemStack stackToInsert) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isEmpty()) {
                player.getInventory().setStack(i, stackToInsert);
                return true;
            }
        }
        return false;
    }

}
