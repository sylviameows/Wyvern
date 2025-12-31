package net.sylviameows.wyvern.mixin.client.render;

import net.minecraft.client.texture.Scaling;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(net.minecraft.client.gui.DrawContext.class)
public interface DrawContextAccessor {
    @Invoker("drawTexturedQuad")
    void wyvern$drawTexturedQuad(Identifier texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2, float red, float green, float blue, float alpha);

    @Invoker("drawSprite")
    void wyvern$drawSprite(Sprite sprite, Scaling.NineSlice nineSlice, int x, int y, int z, int width, int height);
}
