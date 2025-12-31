package net.sylviameows.wyvern.client.gui.widgets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.mixin.client.ScreenAccessor;

public class CloseMenuWidget extends ButtonWidget {

    private static final Text TEXT = Text.literal("x");

    private final Screen parent;
    private final TextRenderer textRenderer;

    public CloseMenuWidget(int x, int y, Screen screen) {
        super(x, y, 0, 0, TEXT, CloseMenuWidget::onPress, DEFAULT_NARRATION_SUPPLIER);
        this.parent = screen;
        this.textRenderer = ((ScreenAccessor) screen).wyvern$getTextRenderer();

        this.width = textRenderer.getWidth(TEXT);
        this.height = textRenderer.fontHeight;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTextWithShadow(textRenderer, getMessage(), getX(), getY(), isHovered() ? 0xFFFFFF : 0xC09E42);
    }

    public Screen parent() {
        return parent;
    }

    private static void onPress(ButtonWidget button) {
        if (button instanceof CloseMenuWidget widget) {
            widget.parent.close();
        }
    }


}
