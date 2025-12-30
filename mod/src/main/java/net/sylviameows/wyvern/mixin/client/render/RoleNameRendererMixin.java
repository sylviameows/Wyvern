package net.sylviameows.wyvern.mixin.client.render;

import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.client.gui.RoleNameRenderer;
import dev.doctor4t.wathe.game.GameFunctions;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.sylviameows.wyvern.WyvernGamemode;
import net.sylviameows.wyvern.api.instinct.Instinct;
import net.sylviameows.wyvern.api.instinct.InstinctResult;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.components.NicknameComponent;
import net.sylviameows.wyvern.api.util.WatheMigrator;
import net.sylviameows.wyvern.game.roles.instinct.SpectatorInstinct;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(RoleNameRenderer.class)
public class RoleNameRendererMixin {

    @Shadow
    private static float nametagAlpha;

    @Shadow
    private static Text nametag;

    @Unique
    private static float customNametagAlpha = 0f;

    @Unique
    private static Text customNametag;

    @Unique
    private static float detailsAlpha = 0f;

    @Unique
    private static List<Text> details = new ArrayList<>();

    @Redirect(method = "renderHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getDisplayName()Lnet/minecraft/text/Text;"))
    private static Text wyvern$getNickname(PlayerEntity instance) {
        NicknameComponent nickname = NicknameComponent.KEY.get(instance);
        return nickname.get();
    }

    @Inject(method = "renderHud", at = @At(value = "INVOKE", target = "Ldev/doctor4t/wathe/game/GameFunctions;isPlayerSpectatingOrCreative(Lnet/minecraft/entity/player/PlayerEntity;)Z", ordinal = 0, shift = At.Shift.AFTER))
    private static void wyvern$getDetailsText(TextRenderer renderer, ClientPlayerEntity player, DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci, @Local(name = "component") GameWorldComponent game) {

        float range = GameFunctions.isPlayerSpectatingOrCreative(player) ? 8f : 2f;
        HitResult result = ProjectileUtil.getCollision(player, entity -> true, range);
        if (result instanceof EntityHitResult entityResult) {
            Entity entity = entityResult.getEntity();

            if (WatheClient.isPlayerSpectatingOrCreative()) {
                InstinctResult instinctResult = SpectatorInstinct.INSTANCE.resolve(entity);
                wyvern$handleResult(instinctResult, tickCounter);
                return;
            } else if (game.getGameMode() instanceof WyvernGamemode) {
                Role role = WatheMigrator.migrateRole(game.getRole(player));
                if (role != null) {
                    Instinct instinct = role.settings().getInstinct();
                    if (instinct != null) {
                        wyvern$handleResult(instinct.resolve(entity), tickCounter);
                        return;
                    }
                }
            }
        }

        wyvern$handleResult(null, tickCounter);

    }

    @Unique
    private static void wyvern$handleResult(InstinctResult result, RenderTickCounter tickCounter) {
        if (result == null) {
            detailsAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 4, detailsAlpha, 0f);
            customNametagAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 4, customNametagAlpha, 0f);
        } else {
            details = result.details();
            customNametag = result.nametag();

            if (!details.isEmpty())
                detailsAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 4, detailsAlpha, 1f);
            else
                detailsAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 4, detailsAlpha, 0f);
            if (customNametag != null)
                customNametagAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 4, customNametagAlpha, 1f);
            else
                customNametagAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 4, customNametagAlpha, 0f);
        }
    }

    @Redirect(method = "renderHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I", ordinal = 0))
    private static int wyvern$swapNametagText(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color) {
        if (customNametagAlpha < nametagAlpha) return instance.drawTextWithShadow(textRenderer, customNametag == null ? text : customNametag, x, y, color);
        return -1;
    }

    @Inject(method = "renderHud", at = @At(value = "TAIL"))
    private static void wyvern$displayInstinctText(TextRenderer renderer, ClientPlayerEntity player, DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (customNametagAlpha >= nametagAlpha && customNametagAlpha >= 0.05f && customNametag != null) {
            context.getMatrices().push();
            context.getMatrices().translate(context.getScaledWindowWidth() / 2f, context.getScaledWindowHeight() / 2f + 6, 0);
            context.getMatrices().scale(0.6f, 0.6f, 1f);

            int nameWidth = renderer.getWidth(customNametag);
            context.drawTextWithShadow(renderer, customNametag, -nameWidth / 2, 16, MathHelper.packRgb(1f, 1f, 1f) | ((int) (customNametagAlpha * 255) << 24));

            context.getMatrices().pop();
        } else if (customNametag != null) {
            customNametag = null;
        }

        if (!details.isEmpty()) {
            if (detailsAlpha <= 0.05f) {
                details.clear();
                return;
            }
            context.getMatrices().push();
            context.getMatrices().translate(context.getScaledWindowWidth() / 2f, context.getScaledWindowHeight() / 2f + 6, 0);
            context.getMatrices().scale(0.6f, 0.6f, 1f);
            for (int i = 0; i < details.size(); i++) {
                Text line = details.get(i);
                int width = renderer.getWidth(line);
                context.drawTextWithShadow(renderer, line, -width / 2, ((nametagAlpha > 0.05f || customNametagAlpha > 0.05f) ? 32 : 16) + (i * (renderer.fontHeight + 2)), MathHelper.packRgb(1f, 1f, 1f) | ((int) (detailsAlpha * 255) << 24));
            }
            context.getMatrices().pop();
        }
    }

    @Redirect(method = "renderHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I", ordinal = 1))
    private static int wyvern$cancel(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color) {
        if (details.isEmpty()) {
            return instance.drawTextWithShadow(textRenderer, text, x, y, color);
        }
        return -1;
    }

}
