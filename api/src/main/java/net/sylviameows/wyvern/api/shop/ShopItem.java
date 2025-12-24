package net.sylviameows.wyvern.api.shop;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.util.Hands;

public class ShopItem {

    private final ItemStack stack;
    private int price;
    private ShopItem.Type type;

    public ShopItem(ItemStack stack, int price, ShopItem.Type type) {
        this.stack = stack;
        this.price = price;
        this.type = type;
    }

    public ItemStack getItem() {
        return stack;
    }
    public int getPrice() {
        return price;
    }
    public Type getType() {
        return type;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public void setType(Type type) {
        this.type = type;
    }

    public boolean purchase(PlayerEntity player) {
        return Hands.insertStackInFreeSlot(player, getItem().copy());
    }

    public interface Type {
        Identifier getTexture();
    }

    public enum Types implements Type {
        WEAPON(Wathe.id("gui/shop_slot_weapon")),
        POISON(Wathe.id("gui/shop_slot_poison")),
        TOOL(Wathe.id("gui/shop_slot_tool")),
        NONE(Wathe.id("gui/shop_slot"));

        private final Identifier identifier;

        Types(final Identifier identifier) {
            this.identifier = identifier;
        }

        @Override
        public Identifier getTexture() {
            return identifier;
        }
    }

}
