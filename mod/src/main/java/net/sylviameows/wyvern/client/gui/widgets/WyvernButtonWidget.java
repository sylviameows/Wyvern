package net.sylviameows.wyvern.client.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.texture.Scaling;
import net.minecraft.client.texture.Sprite;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.mixin.client.render.DrawContextAccessor;

public class WyvernButtonWidget extends ButtonWidget {

    private final Scaling.NineSlice SLICE = new Scaling.NineSlice(9, 9, new Scaling.NineSlice.Border(3, 3, 3, 3));
    private final Sprite ACTIVE_SPRITE = MinecraftClient.getInstance().getGuiAtlasManager().getSprite(Wyvern.id("button"));
    private final Sprite HOVERED_SPRITE = MinecraftClient.getInstance().getGuiAtlasManager().getSprite(Wyvern.id("button_hovered"));
    private final Sprite PRESSED_SPRITE = MinecraftClient.getInstance().getGuiAtlasManager().getSprite(Wyvern.id("button_pressed"));
    private final Sprite DISABLED_SPRITE = MinecraftClient.getInstance().getGuiAtlasManager().getSprite(Wyvern.id("button_disabled"));

    private boolean pressed = false;

    public WyvernButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        Sprite sprite = isHovered() ? HOVERED_SPRITE : ACTIVE_SPRITE;
        if (!active) sprite = DISABLED_SPRITE;
        if (pressed) sprite = PRESSED_SPRITE;

        ((DrawContextAccessor) context).wyvern$drawSprite(sprite, SLICE, getX(), getY(), 0, getWidth(), getHeight());

        drawMessage(context, MinecraftClient.getInstance().textRenderer, 0xFFFFFF);
    }

    @Override
    public void onPress() {
        this.pressed = true;
        super.onPress();
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        this.pressed = false;
        super.onRelease(mouseX, mouseY);
    }
}
