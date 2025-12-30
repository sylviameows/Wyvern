package net.sylviameows.wyvern.api.shop;

import dev.doctor4t.wathe.cca.PlayerShopComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.sylviameows.wyvern.api.WyvernAPI;
import net.sylviameows.wyvern.api.util.Time;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public final class Shop {

    public static Shop getDefault() {
        return WyvernAPI.defaultShop().use();
    }

    private final List<ShopItem> items = new ArrayList<>();

    private int balance = 0;
    private int increment = 0;
    private int interval = -1;

    public Shop() {}

    public Shop(Shop from) {
        this.items.addAll(from.items);
        this.balance = from.balance;
        this.increment = from.increment;
        this.interval = from.interval;
    }

    public Shop(ShopItem... items) {
        this.items.addAll(Arrays.asList(items));
    }

    public Shop copy() {
        return new Shop(this);
    }

    public void setStartingBalance(int balance) {
        this.balance = balance;
    }

    public void setTicking(int increment, int interval) {
        this.increment = increment;
        this.interval = interval;
    }

    public boolean append(ItemStack stack, int price, ShopItem.Type type) {
        return this.append(new ShopItem(stack, price, type));
    }

    public boolean append(@NotNull ShopItem item) {
        return items.add(item);
    }

    public boolean change(ItemStack stack, ShopItem changeTo) {
        for (int i = 0; i < this.items.size(); i++) {
            ShopItem item = this.items.get(i);
            if (item.getItem().getItem() != stack.getItem()) continue;
            this.items.set(i, changeTo);
            return true;
        }
        return false;
    }

    public void modifyAll(@NotNull Consumer<ShopItem> consumer) {
        for (ShopItem item : this.items) {
            consumer.accept(item);
        }
    }

    public void modify(ItemStack stack, Consumer<ShopItem> modifier) {
        for (ShopItem item : items) {
            if (item.getItem().getItem() != stack.getItem()) continue;
            modifier.accept(item);
        }
    }

    public boolean remove(ItemStack stack) {
        return items.removeIf(item -> item.getItem() == stack);
    }

    public List<ShopItem> getItems() {
        return items;
    }
    public int getStartingBalance() {
        return balance;
    }

    public void tick(PlayerEntity player) {
        if (interval == -1) return;

        World world = player.getWorld();

        if (world.getTime() % interval == 0) {
            PlayerShopComponent.KEY.get(player).addToBalance(increment);
        }
    }

}
