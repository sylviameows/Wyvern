package net.sylviameows.wyvern.api.mood.render;

import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;

/**
 * For if you want to get <i>really</i> custom with it.
 * Simply extend this interface in your mood handler, and it will use this to render your mood!
 */
public interface CustomMoodRenderer {

    void render(PlayerEntity player, PlayerMoodComponent mood, TextRenderer textRenderer, DrawContext context, RenderTickCounter tickCounter);

}
