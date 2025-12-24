package net.sylviameows.wyvern.game;

import dev.doctor4t.wathe.cca.PlayerShopComponent;
import dev.doctor4t.wathe.index.WatheItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.sylviameows.wyvern.api.shop.DefaultShop;
import net.sylviameows.wyvern.api.shop.Shop;
import net.sylviameows.wyvern.api.shop.ShopItem;

import java.util.function.Consumer;

public class WyvernShop implements DefaultShop {

    private final Shop shop = new Shop(
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
            new ShopItem(new ItemStack(WatheItems.NOTE, 4), 10, ShopItem.Types.TOOL)
    );

    public WyvernShop() {

    }

    @Override
    public Shop use() {
        return shop.copy();
    }

    @Override
    public void modify(Consumer<Shop> consumer) {
        consumer.accept(shop);
    }
}
