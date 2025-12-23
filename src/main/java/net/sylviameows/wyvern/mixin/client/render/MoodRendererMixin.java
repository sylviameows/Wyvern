package net.sylviameows.wyvern.mixin.client.render;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import dev.doctor4t.wathe.cca.PlayerPsychoComponent;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.client.gui.MoodRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.WyvernGamemode;
import net.sylviameows.wyvern.api.mood.MoodHandler;
import net.sylviameows.wyvern.api.mood.render.CustomMoodRenderer;
import net.sylviameows.wyvern.api.mood.render.MoodRenderOptions;
import net.sylviameows.wyvern.api.mood.render.SimpleMoodRenderer;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.task.Task;
import net.sylviameows.wyvern.client.render.DefaultMoodRenderOptions;
import net.sylviameows.wyvern.client.render.WyvernTaskRenderer;
import net.sylviameows.wyvern.mixin.components.PlayerMoodComponentAccessor;
import net.sylviameows.wyvern.util.migration.WatheMigrator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MoodRenderer.class)
public abstract class MoodRendererMixin {

    @Shadow
    private static void renderPsycho(@NotNull PlayerEntity player, @NotNull TextRenderer renderer, @NotNull DrawContext context, PlayerPsychoComponent component, @NotNull RenderTickCounter tickCounter) {
        throw new AssertionError();
    }

    @Shadow
    public static float moodRender;

    @Shadow
    public static float moodOffset;

    @Shadow
    public static float moodTextWidth;

    @Shadow
    public static float moodAlpha;

    @Unique
    private static @Nullable WyvernTaskRenderer taskRenderer = null;

    @Unique
    private static @Nullable Task task = null;

    @Inject(method = "renderHud", at = @At(value = "HEAD"), cancellable = true)
    private static void wyvern$renderTask(PlayerEntity player, TextRenderer textRenderer, DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        GameWorldComponent game = GameWorldComponent.KEY.get(player.getWorld());
        if (game.isRunning() && WatheClient.isPlayerAliveAndInSurvival()) {
            if (!(game.getGameMode() instanceof WyvernGamemode)) return;

            PlayerMoodComponent mood = PlayerMoodComponent.KEY.get(player);
            MoodHandler handler = ((PlayerMoodComponentAccessor) mood).wyvern$getMoodHandler();

            if (handler instanceof CustomMoodRenderer renderer) {
                renderer.render(player, mood, textRenderer, context, tickCounter);
                return;
            }

//            float prev = moodRender;

            if (game.getRole(player) == null) return;
            Role role = WatheMigrator.migrateRole(game.getRole(player));
            if (role == null) return;

            MoodRenderOptions options = new DefaultMoodRenderOptions(role, handler, player);

            if (handler instanceof SimpleMoodRenderer renderer) {
                renderer.apply(options);
            }

            moodRender = MathHelper.lerp(tickCounter.getTickDelta(true) / 8, moodRender, options.getMood());
            moodAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 16, moodAlpha, taskRenderer == null ? 0f : 1f);

            PlayerPsychoComponent psycho = PlayerPsychoComponent.KEY.get(player);
            if (psycho.getPsychoTicks() > 0) {
                renderPsycho(player, textRenderer, context, psycho, tickCounter);
                return;
            }

            if (options.getText() != null) {
                if (taskRenderer == null) {
                    taskRenderer = new WyvernTaskRenderer();
                } else {
                    if (taskRenderer.tick(options.getText(), tickCounter.getTickDelta(true))) {
                        taskRenderer = null;
                    } else {
                        Wyvern.LOGGER.info("alpha: {}, offset: {}", taskRenderer.alpha, taskRenderer.offset);
                        context.getMatrices().push();
                        context.getMatrices().translate(0, 10 * taskRenderer.offset, 0);
                        context.drawTextWithShadow(textRenderer, taskRenderer.text, 22, 6, MathHelper.packRgb(1f, 1f, 1f) | ((int) (taskRenderer.alpha * 255) << 24));
                        context.getMatrices().pop();

                        moodOffset = MathHelper.lerp(tickCounter.getTickDelta(true) / 8, moodOffset, taskRenderer.offset);
                        moodTextWidth = MathHelper.lerp(tickCounter.getTickDelta(true) / 32, moodTextWidth, textRenderer.getWidth(taskRenderer.text));
                    }
                }
            } else if (taskRenderer != null) {
                Wyvern.LOGGER.info("c");
                taskRenderer = null;
            }

            context.getMatrices().push();
            context.getMatrices().translate(0, 3 * moodOffset, 0);

            Identifier texture = options.getTexture();
            context.drawGuiTexture(texture, 5, 6, options.getWidth(), options.getHeight());

            context.getMatrices().pop();
            context.getMatrices().push();

            context.getMatrices().translate(0, 10 * moodOffset, 0);
            context.getMatrices().translate(12 + options.getWidth(), 8 + textRenderer.fontHeight, 0);
            context.getMatrices().scale((moodTextWidth - 8) * moodRender, 1, 1);

            Integer color = options.getColor();
            if (color == null) color = MathHelper.hsvToRgb(moodRender / 3.0F, 1.0F, 1.0F) | ((int) (moodAlpha * 255) << 24);
            context.fill(0, 0, 1, 1, color);

            context.getMatrices().pop();;

        } else {
            task = null;
            taskRenderer = null;
        }

        ci.cancel();
    }

}
