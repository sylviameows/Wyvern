package net.sylviameows.wyvern.mixin.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.client.gui.RoundTextRenderer;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.WyvernGamemode;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.components.ResultComponent;
import net.sylviameows.wyvern.game.configuration.WyvernRoleOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(RoundTextRenderer.class)
public class RoundTextRendererMixin {

    @Unique
    private static final int HEAD_WIDTH = 8;
    @Unique
    private static final int HEAD_HORIZONTAL_MARGIN = 4;
    @Unique
    private static final int HEAD_VERTICAL_MARGIN = 6;

    @Unique
    private static final int HEAD_HORIZONTAL_INTERVAL = HEAD_WIDTH + HEAD_HORIZONTAL_MARGIN;
    @Unique
    private static final int HEAD_VERTICAL_INTERVAL = HEAD_WIDTH + HEAD_VERTICAL_MARGIN;






    @Shadow
    private static int endTime;

    @Shadow
    @Final
    private static int END_DURATION;

    @Inject(method = "renderHud", at = @At("HEAD"), cancellable = true)
    private static void wyvern$renderHud(TextRenderer renderer, ClientPlayerEntity player, DrawContext context, CallbackInfo ci) {
        GameWorldComponent game = GameWorldComponent.KEY.get(player.getWorld());

        if (game.getGameMode() instanceof WyvernGamemode) {
            if (endTime > 0 && endTime < END_DURATION - (GameConstants.FADE_TIME * 2) && !game.isRunning()) {
                ci.cancel();
                wyvern$renderDefault(renderer, player, context);
            }
        }
    }

    @Unique
    private static void wyvern$renderDefault(TextRenderer renderer, ClientPlayerEntity player, DrawContext context) {
        ResultComponent component = ResultComponent.KEY.get(player.getWorld());

        if (component.getResult() == null) return;

        Text title = component.getTitleText(player);
        Text reason = component.getReasonText();

        context.getMatrices().push();
        context.getMatrices().translate(context.getScaledWindowWidth() / 2f, context.getScaledWindowHeight() / 2f - 40, 0);

        context.getMatrices().push();
        context.getMatrices().scale(2.6f, 2.6f, 1f);
        context.drawTextWithShadow(renderer, title, -renderer.getWidth(title) / 2, -12, 0xFFFFFF);
        context.getMatrices().pop();

        context.getMatrices().push();
        context.getMatrices().scale(1.2f, 1.2f, 1f);
        context.drawTextWithShadow(renderer, reason, -renderer.getWidth(reason) / 2, -4, 0xFFFFFF);
        context.getMatrices().pop();

        context.drawTextWithShadow(renderer, Alignment.INNOCENT.getAlignmentName(), -renderer.getWidth(Alignment.INNOCENT.getAlignmentName()) / 2 - (HEAD_HORIZONTAL_INTERVAL * 2), 14, 0xFFFFFF);
        context.drawTextWithShadow(renderer, Alignment.KILLER.getAlignmentName(), -renderer.getWidth(Alignment.KILLER.getAlignmentName()) / 2 + HEAD_HORIZONTAL_MARGIN + (HEAD_HORIZONTAL_INTERVAL * 5), 14, 0xFFFFFF);

        List<ResultComponent.PlayerEntry> passengers = new ArrayList<>();
        List<ResultComponent.PlayerEntry> killers = new ArrayList<>();
        List<ResultComponent.PlayerEntry> other = new ArrayList<>();

        for (var entry : component.getPlayers()) {
            context.getMatrices().push();
            context.getMatrices().scale(2f, 2f, 1f);

            switch (entry.role().alignment()) {
                case INNOCENT -> {
                    int mod = passengers.size();
                    context.getMatrices().translate(-48 + (mod % 5) * HEAD_HORIZONTAL_INTERVAL, 14 + (Math.floorDiv(mod, 5) * HEAD_VERTICAL_INTERVAL), 0);
                    passengers.add(entry);
                }
                case KILLER ->  {
                    int mod = killers.size();
                    context.getMatrices().translate(20, 14 + (mod * HEAD_VERTICAL_INTERVAL), 0);
                    killers.add(entry);
                }
                case OTHER -> {
                    context.getMatrices().pop();
                    other.add(entry);
                    continue;
                }
            }

            wyvern$drawPlayer(entry, renderer, context);
            context.getMatrices().pop();
        }

        if (!other.isEmpty()) {
            int vertical = Math.max(Math.floorDiv(passengers.size(), 5), killers.size());

            context.drawTextWithShadow(renderer, Alignment.OTHER.getAlignmentName(), -renderer.getWidth(Alignment.OTHER.getAlignmentName()) / 2, 14 + 16 + (vertical * (HEAD_VERTICAL_INTERVAL*2)), 0xFFFFFF);

            var count = 0;
            var width = (other.size() * HEAD_HORIZONTAL_INTERVAL) - 4;
            for (var entry : other) {
                context.getMatrices().push();
                context.getMatrices().scale(2f, 2f, 1f);

                context.getMatrices().translate(0, (vertical * HEAD_VERTICAL_INTERVAL) + 8, 0);
                context.getMatrices().translate(-8 - Math.floorDiv(width, 2) + (count * 12), 14, 0);

                wyvern$drawPlayer(entry, renderer, context);
                context.getMatrices().pop();

                count++;
            }
        }

        context.getMatrices().pop();

    }

    @Unique
    private static void wyvern$drawPlayer(ResultComponent.PlayerEntry entry, TextRenderer renderer, DrawContext context) {
        PlayerListEntry playerListEntry = TMMClient.PLAYER_ENTRIES_CACHE.get(entry.profile().getId());
        if (playerListEntry != null) {
            Identifier texture = playerListEntry.getSkinTextures().texture();
            if (texture != null) {
                RenderSystem.enableBlend();

                context.getMatrices().push();

                context.getMatrices().translate(8, 0 ,0);
                float saturation = entry.dead() ? 0.4f : 1f;
                ((DrawContextAccessor) context).wyvern$drawTexturedQuad(texture, 0, 8, 0, 8, 0, 8 / 64f, 16 / 64f, 8 / 64f, 16 / 64f, 1f, saturation, saturation, 1f);
                context.getMatrices().translate(-0.5, -0.5, 0);
                context.getMatrices().scale(1.125f, 1.125f, 1f);
                ((DrawContextAccessor) context).wyvern$drawTexturedQuad(texture, 0, 8, 0, 8, 0, 40 / 64f, 48 / 64f, 8 / 64f, 16 / 64f, 1f, saturation, saturation, 1f);

                context.getMatrices().pop();
            }


            if (entry.role().unique()) {
                Text roleText = entry.role().announcement().roleText;
                context.getMatrices().push();
                context.getMatrices().translate(12, 7, 0);
                context.getMatrices().scale(0.25f, 0.25f, 1f);
                context.drawTextWithShadow(renderer, roleText, -renderer.getWidth(roleText) / 2, 8, 0xFFFFFF);
                context.getMatrices().pop();
            }

            if (entry.dead()) {
                context.getMatrices().translate(13, 0, 0);
                context.getMatrices().scale(2f,1f, 1f);
                context.drawText(renderer, "x", -renderer.getWidth("x") / 2, 0, 0xE10000, false);
                context.drawText(renderer, "x", -renderer.getWidth("x") / 2, 1, 0x550000, false);
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private static void wyvern$tick(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null && GameWorldComponent.KEY.get(client.world).getGameMode() instanceof WyvernGamemode) {
            PlayerEntity player = client.player;
            if (endTime > 0) {
                if (endTime == END_DURATION - (GameConstants.FADE_TIME * 2) && player != null) {
                    ResultComponent component = ResultComponent.KEY.get(player.getWorld());

                    player.getWorld().playSound(
                            player, player.getX(), player.getY(), player.getZ(),
                            component.isWinner(player) ? TMMSounds.UI_PIANO_WIN : TMMSounds.UI_PIANO_LOSE,
                            SoundCategory.MASTER, 10f, 1f, player.getRandom().nextLong()
                    );
                }

                endTime--;

                // since we cancel the regular function, this needs to be reimplemented.
                var options = MinecraftClient.getInstance().options;
                if (options != null && options.playerListKey.isPressed()) endTime = Math.max(2, endTime);

                ci.cancel();
            }
        }

    }

}
