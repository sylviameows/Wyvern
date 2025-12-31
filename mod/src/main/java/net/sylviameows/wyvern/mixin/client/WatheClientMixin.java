package net.sylviameows.wyvern.mixin.client;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.entity.FirecrackerEntity;
import dev.doctor4t.wathe.entity.NoteEntity;
import dev.doctor4t.wathe.entity.PlayerBodyEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.WyvernGamemode;
import net.sylviameows.wyvern.api.WyvernColors;
import net.sylviameows.wyvern.api.instinct.Instinct;
import net.sylviameows.wyvern.api.instinct.InstinctResult;
import net.sylviameows.wyvern.api.instinct.Instincts;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.client.gui.NicknameScreen;
import net.sylviameows.wyvern.api.util.WatheMigrator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WatheClient.class)
public abstract class WatheClientMixin {

    @Shadow
    public static GameWorldComponent gameComponent;

    @Shadow
    public static boolean isPlayerSpectatingOrCreative() {
        throw new AssertionError();
    }

    @Shadow
    public static KeyBinding instinctKeybind;

    @Inject(method = "getInstinctHighlight", at = @At(value = "HEAD"), cancellable = true)
    private static void getInstinctHighlight(Entity target, CallbackInfoReturnable<Integer> cir) {
        if (!instinctKeybind.isPressed()) {
            cir.setReturnValue(-1);
            return;
        }

        // spectator instinct/qol
        if (isPlayerSpectatingOrCreative()) {
            InstinctResult result = null;
            if (target instanceof PlayerEntity player)
                result = Instincts.player(player, true);
            else if (target instanceof ItemEntity || target instanceof NoteEntity || target instanceof FirecrackerEntity)
                result = new InstinctResult(WyvernColors.ITEM);
            else if (target instanceof PlayerBodyEntity body) {
                result = Instincts.body(body, true, true);
            }

            if (result != null) {
                cir.setReturnValue(result.color());
            } else {
                cir.setReturnValue(-1);
            }

            return;
        }

        // role instinct
        if (gameComponent.getGameMode() instanceof WyvernGamemode) {
            PlayerEntity self = MinecraftClient.getInstance().player;
            var wathe = gameComponent.getRole(self);
            if (wathe == null) {
                cir.setReturnValue(-1);
                return;
            }
            Role role = WatheMigrator.migrateRole(wathe);
            if (role == null) {
                cir.setReturnValue(-1);
                return;
            }

            Instinct instinct = role.settings().getInstinct();
            InstinctResult result = instinct.resolve(target);

            if (result != null) {
                cir.setReturnValue(result.color());
            } else {
                cir.setReturnValue(-1);
            }
        }



    }


}
