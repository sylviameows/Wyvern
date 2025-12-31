package net.sylviameows.wyvern.client.gui.widgets;

import dev.doctor4t.wathe.client.gui.screen.ingame.LimitedInventoryScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.api.shop.ShopItem;
import net.sylviameows.wyvern.mixin.client.ScreenAccessor;
import net.sylviameows.wyvern.payloads.PurchasePayload;

public class ShopItemWidget extends ButtonWidget {

    private final LimitedInventoryScreen screen;
    private final ShopItem item;

    public ShopItemWidget(LimitedInventoryScreen screen, int x, int y, ShopItem item, int index) {
        super(x, y, 16, 16, item.getItem().getName(), (a) -> ClientPlayNetworking.send(new PurchasePayload(index)), DEFAULT_NARRATION_SUPPLIER);

        this.screen = screen;
        this.item = item;
    }


    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        context.drawGuiTexture(item.getType().getTexture(), this.getX() - 7, this.getY() - 7, 30, 30);
        context.drawItem(this.item.getItem(), this.getX(), this.getY());
        if (this.isHovered()) {
            this.screen.renderLimitedInventoryTooltip(context, this.item.getItem());
            drawShopSlotHighlight(context, this.getX(), this.getY(), 0);
        }
        MutableText price = Text.literal(this.item.getPrice() + "\uE781");

        TextRenderer textRenderer = ((ScreenAccessor) screen).wyvern$getTextRenderer();
        context.drawTooltip(textRenderer, price, this.getX() - 4 - textRenderer.getWidth(price) / 2, this.getY() - 9);
    }

    private void drawShopSlotHighlight(DrawContext context, int x, int y, int z) {
        int color = 0x90FFBF49;
        context.fillGradient(RenderLayer.getGuiOverlay(), x, y, x + 16, y + 14, color, color, z);
        context.fillGradient(RenderLayer.getGuiOverlay(), x, y + 14, x + 15, y + 15, color, color, z);
        context.fillGradient(RenderLayer.getGuiOverlay(), x, y + 15, x + 14, y + 16, color, color, z);
    }

    @Override
    public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
    }
}
