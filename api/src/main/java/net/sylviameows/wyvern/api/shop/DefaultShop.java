package net.sylviameows.wyvern.api.shop;

import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public interface DefaultShop {

    /**
     * Creates a clone of the default shop for a role to use and modify.
     * @return new shop instance copying default.
     */
    Shop use();

    /**
     * Provides the default shop instance to be modified.
     * @param consumer the modifier function.
     */
    void modify(Consumer<Shop> consumer);

}
