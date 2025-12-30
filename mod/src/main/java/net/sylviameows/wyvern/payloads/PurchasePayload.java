package net.sylviameows.wyvern.payloads;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import dev.doctor4t.wathe.index.WatheSounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.shop.Shop;
import net.sylviameows.wyvern.api.shop.ShopItem;
import net.sylviameows.wyvern.api.util.WatheMigrator;

/**
 * Equivalent of the {@link dev.doctor4t.wathe.util.StoreBuyPayload} from Wathe, but for Wyvern.
 * @param index the index of the shop item.
 */
public record PurchasePayload(int index) implements CustomPayload {
    public static final Id<PurchasePayload> ID = new Id<>(Wyvern.id("purchase"));
    public static final PacketCodec<PacketByteBuf, PurchasePayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, PurchasePayload::index, PurchasePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<PurchasePayload> {
        @Override
        public void receive(PurchasePayload payload, ServerPlayNetworking.Context context) {
            PlayerShopComponent component = PlayerShopComponent.KEY.get(context.player());
            GameWorldComponent game = GameWorldComponent.KEY.get(context.player().getWorld());

            Role role = WatheMigrator.migrateRole(game.getRole(context.player()));
            if (role == null) return;
            Shop shop = role.settings().getShop();
            if (shop == null || shop.getItems().isEmpty()) return;

            ShopItem item = shop.getItems().get(payload.index);

            if (component.balance >= item.getPrice() && !context.player().getItemCooldownManager().isCoolingDown(item.getItem().getItem()) && item.purchase(context.player())) {
                component.setBalance(component.balance - item.getPrice());
                context.player().networkHandler.sendPacket(new PlaySoundS2CPacket(Registries.SOUND_EVENT.getEntry(WatheSounds.UI_SHOP_BUY), SoundCategory.PLAYERS, context.player().getX(), context.player().getY(), context.player().getZ(), 1.0f, 0.9f + context.player().getRandom().nextFloat() * 0.2f, context.player().getRandom().nextLong()));
            } else {
                context.player().sendMessage(Text.translatable("wyvern.shop.purchase.failed").formatted(Formatting.DARK_RED), true);
                context.player().networkHandler.sendPacket(new PlaySoundS2CPacket(Registries.SOUND_EVENT.getEntry(WatheSounds.UI_SHOP_BUY_FAIL), SoundCategory.PLAYERS, context.player().getX(), context.player().getY(), context.player().getZ(), 1.0f, 0.9f + context.player().getRandom().nextFloat() * 0.2f, context.player().getRandom().nextLong()));
            }
        }
    }

}
