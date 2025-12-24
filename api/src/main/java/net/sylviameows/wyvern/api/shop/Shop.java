package net.sylviameows.wyvern.api.shop;

import dev.doctor4t.wathe.cca.PlayerShopComponent;
import dev.doctor4t.wathe.index.WatheItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Shop {

    public static final Shop DEFAULT = new Shop(
            new ShopItem(WatheItems.KNIFE.getDefaultStack(), 100, ShopItem.Types.WEAPON),
            new ShopItem(WatheItems.REVOLVER.getDefaultStack(), 300, ShopItem.Types.WEAPON),
            new ShopItem(WatheItems.GRENADE.getDefaultStack(), 350, ShopItem.Types.WEAPON),
            new ShopItem(WatheItems.PSYCHO_MODE.getDefaultStack(), 300, ShopItem.Types.WEAPON) {
                @Override public boolean purchase(PlayerEntity player) { return PlayerShopComponent.usePsychoMode(player);}
            },
            new ShopItem(WatheItems.POISON_VIAL.getDefaultStack(), 100, ShopItem.Types.POISON),
            new ShopItem(WatheItems.SCORPION.getDefaultStack(), 50, ShopItem.Types.POISON),
            new ShopItem(WatheItems.FIRECRACKER.getDefaultStack(), 10, ShopItem.Types.TOOL),
            new ShopItem(WatheItems.LOCKPICK.getDefaultStack(), 50, ShopItem.Types.TOOL),
            new ShopItem(WatheItems.CROWBAR.getDefaultStack(), 25, ShopItem.Types.TOOL),
            new ShopItem(WatheItems.BODY_BAG.getDefaultStack(), 200, ShopItem.Types.TOOL),
            new ShopItem(WatheItems.BLACKOUT.getDefaultStack(), 200, ShopItem.Types.TOOL) {
                @Override public boolean purchase(PlayerEntity player) { return PlayerShopComponent.useBlackout(player);}
            },
            new ShopItem(new ItemStack(WatheItems.NOTE, 4), 10, ShopItem.Types.TOOL),
            new ShopItem(WatheItems.MARTINI.getDefaultStack(), 5, ShopItem.Types.NONE)
    );

    private final List<ShopItem> items = new ArrayList<>();

    public Shop() {}

    public Shop(ShopItem... items) {
        this.items.addAll(Arrays.asList(items));
    }

    public boolean append(ItemStack stack, int price, ShopItem.Type type) {
        return this.append(new ShopItem(stack, price, type));
    }

    public boolean append(@NotNull ShopItem item) {
        return items.add(item);
    }

    public boolean remove(ItemStack stack) {
        return items.removeIf(item -> item.getItem() == stack);
    }

    public List<ShopItem> getItems() {
        return items;
    }

}
