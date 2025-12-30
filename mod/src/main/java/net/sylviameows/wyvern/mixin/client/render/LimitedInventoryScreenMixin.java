package net.sylviameows.wyvern.mixin.client.render;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.client.gui.screen.ingame.LimitedHandledScreen;
import dev.doctor4t.wathe.client.gui.screen.ingame.LimitedInventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.WyvernGamemode;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.shop.Shop;
import net.sylviameows.wyvern.api.shop.ShopItem;
import net.sylviameows.wyvern.client.gui.ShopItemWidget;
import net.sylviameows.wyvern.api.util.WatheMigrator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LimitedInventoryScreen.class)
public abstract class LimitedInventoryScreenMixin extends LimitedHandledScreen<PlayerScreenHandler> {
    @Shadow
    @Final
    public ClientPlayerEntity player;

    public LimitedInventoryScreenMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(
            method = "init",
            at = @At(value = "INVOKE", target = "Ldev/doctor4t/wathe/client/gui/screen/ingame/LimitedHandledScreen;init()V", shift = At.Shift.AFTER),
            cancellable = true
    )
    private void wyvern$renderShop(CallbackInfo ci) {
        GameWorldComponent game = GameWorldComponent.KEY.get(player.getWorld());
        if (game.getGameMode() instanceof WyvernGamemode) {
            Role role = WatheMigrator.migrateRole(game.getRole(player));
            if (role == null) return;
            ci.cancel();

            Shop shop = role.settings().getShop();
            if (shop == null) return;

            List<ShopItem> items = shop.getItems();
            int distance = 38;

            int x = this.width / 2 - items.size() * distance / 2 + 9;
            int y = this.y - 46;

            for (int i = 0; i < items.size(); i++) {
                this.addDrawableChild(new ShopItemWidget((LimitedInventoryScreen) (Object) this, x + distance * i, y, items.get(i), i));
            }
        }
    }

}
