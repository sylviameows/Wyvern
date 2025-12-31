package net.sylviameows.wyvern.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.texture.Scaling;
import net.minecraft.client.texture.Sprite;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.client.gui.widgets.CloseMenuWidget;
import net.sylviameows.wyvern.mixin.client.ScreenAccessor;
import net.sylviameows.wyvern.mixin.client.render.DrawContextAccessor;

public class WyvernScreen extends Screen {

    private final Scaling.NineSlice SLICE = new Scaling.NineSlice(21, 21, new Scaling.NineSlice.Border(7, 7, 7, 7));
    private final Sprite PANEL_SPRITE = MinecraftClient.getInstance().getGuiAtlasManager().getSprite(Wyvern.id("panel"));

    protected int sizeX;
    protected int sizeY;

    protected WyvernScreen(Text title, int sizeX, int sizeY) {
        super(title);

        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    @Override
    protected void init() {
        this.addDrawableChild(new CloseMenuWidget((left() + sizeX) - 9 - this.textRenderer.getWidth(Text.literal("x")), top() + 7, this));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        this.renderBackground(context, mouseX, mouseY, delta);

        context.getMatrices().push();
        context.drawTextWithShadow(this.textRenderer, this.title, left() + 9, top() + 9, 16777215);

        for(Drawable drawable : ((ScreenAccessor) this).wyvern$getDrawables()) {
            drawable.render(context, mouseX, mouseY, delta);
        }

        context.getMatrices().pop();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.applyBlur(delta);
        this.renderInGameBackground(context);

        ((DrawContextAccessor) context).wyvern$drawSprite(PANEL_SPRITE, SLICE, left(), top(), 0, sizeX, sizeY);
    }

    public int top() {
        return (this.height / 2) - (this.sizeY / 2);
    }

    public int left() {
        return (this.width / 2) - (this.sizeX / 2);
    }

}
