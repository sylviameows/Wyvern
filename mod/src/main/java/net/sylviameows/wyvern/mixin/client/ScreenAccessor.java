package net.sylviameows.wyvern.mixin.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenAccessor {
    @Accessor("textRenderer")
    TextRenderer wyvern$getTextRenderer();

    @Accessor("drawables")
    List<Drawable> wyvern$getDrawables();
}
