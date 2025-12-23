package net.sylviameows.wyvern.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import dev.doctor4t.wathe.client.WatheClient;
import net.minecraft.entity.player.PlayerEntity;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.util.migration.WatheMigrator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(WatheClient.class)
public abstract class WatheClientMixin {

    @Shadow
    public static GameWorldComponent gameComponent;

    @Shadow
    public static boolean isPlayerSpectatingOrCreative() {
        throw new AssertionError();
    }

    @Inject(method = "getInstinctHighlight", at = @At(value = "INVOKE", target = "Ldev/doctor4t/wathe/client/WatheClient;isKiller()Z", shift = At.Shift.AFTER), cancellable = true)
    private static void getInstinctHighlight(CallbackInfoReturnable<Integer> cir, @Local(name = "player") PlayerEntity target) {
        if (isPlayerSpectatingOrCreative()) {
            var harpy = gameComponent.getRole(target);
            if (harpy == null) return;

            int color;

            Role role = WatheMigrator.migrateRole(harpy);
            if (role == null) {
                color = harpy.color();
            } else {
                color = role.color();
            }

            PlayerMoodComponent mood = PlayerMoodComponent.KEY.get(target);
            float[] hsb = Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, (color & 0xFF), null);
            if (mood.isLowerThanDepressed()) {
                hsb[2] = Math.min(hsb[2] * 0.3f, 1f);
            } else if (mood.isLowerThanMid()) {
                hsb[2] = Math.min(hsb[2] * 0.6f, 1f);
            }

            cir.setReturnValue(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
        }
    }


}
