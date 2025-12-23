package net.sylviameows.wyvern.mixin.client.render;

import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.client.gui.RoleNameRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.components.NicknameComponent;
import net.sylviameows.wyvern.util.WatheMigrator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RoleNameRenderer.class)
public class RoleNameRendererMixin {

    @Shadow
    private static float nametagAlpha;

    @Unique
    private static Text roleText = Text.empty();

    @Redirect(method = "renderHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getDisplayName()Lnet/minecraft/text/Text;"))
    private static Text wyvern$getNickname(PlayerEntity instance) {
        NicknameComponent nickname = NicknameComponent.KEY.get(instance);
        return nickname.get();
    }

    @Inject(method = "renderHud", at = @At(value = "INVOKE", target = "Ldev/doctor4t/wathe/cca/GameWorldComponent;canUseKillerFeatures(Lnet/minecraft/entity/player/PlayerEntity;)Z", ordinal = 0))
    private static void wyvern$getRoleText(TextRenderer renderer, ClientPlayerEntity player, DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci, @Local(name = "target") PlayerEntity target, @Local(name = "component") GameWorldComponent game) {
        if (WatheClient.isPlayerSpectatingOrCreative()) {
            var harpy = game.getRole(target);
            if (harpy == null) {
                roleText = null;
                return;
            }

            Role role = WatheMigrator.migrateRole(harpy);
            if (role == null) {
                roleText = Text.translatable("announcement.title.%s".formatted(harpy.identifier().getPath())).withColor(harpy.color());
            } else {
                roleText = role.announcement().getName();
            }
        } else {
            roleText = null;
        }
    }

    @Inject(method = "renderHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 0, shift = At.Shift.AFTER))
    private static void wyvern$displayRoleText(TextRenderer renderer, ClientPlayerEntity player, DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (roleText != null) {
            int nameWidth = renderer.getWidth(roleText);
            context.drawTextWithShadow(renderer, roleText, -nameWidth / 2, 32, MathHelper.packRgb(1f, 1f, 1f) | ((int) (nametagAlpha * 255) << 24));
        }
    }

}
