package net.sylviameows.wyvern.mixin.client.render;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.client.gui.StoreRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.sylviameows.wyvern.WyvernGamemode;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.util.WatheMigrator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StoreRenderer.class)
public class StoreRendererMixin {

    @Redirect(method = "renderHud", at = @At(value = "INVOKE", target = "Ldev/doctor4t/wathe/cca/GameWorldComponent;canUseKillerFeatures(Lnet/minecraft/entity/player/PlayerEntity;)Z"))
    private static boolean wyvern$hasShop(GameWorldComponent instance, PlayerEntity player) {
        if (instance.getGameMode() instanceof WyvernGamemode) {
            Role role = WatheMigrator.migrateRole(instance.getRole(player));
            if (role == null) return false;

            return role.settings().hasShop();
        }
        return instance.canUseKillerFeatures(player);
    }

}
