package net.sylviameows.wyvern.client.gui;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.game.GameFunctions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.client.gui.widgets.WyvernButtonWidget;
import net.sylviameows.wyvern.client.gui.widgets.NicknameTextField;
import net.sylviameows.wyvern.components.NicknameComponent;
import net.sylviameows.wyvern.payloads.NicknamePayload;

public class NicknameScreen extends WyvernScreen {

    private final int GOLD_COLOR = 0xC09E42;
    private final PlayerEntity player;

    private WyvernButtonWidget button;

    public NicknameScreen(PlayerEntity player) {
        super(Text.translatable("screen.wyvern.nickname"), 160, 100);
        this.player = player;
    }

    @Override
    protected void init() {
        this.sizeY = this.textRenderer.fontHeight * 3 + 18 + 2;

        super.init();

        int unit = (sizeX - 18) / 3;

        NicknameComponent nickname = NicknameComponent.KEY.get(player);
        NicknameTextField field = new NicknameTextField(this.textRenderer, left() + 9, top() + this.textRenderer.fontHeight + 13, (unit * 2) - 2, (int) (this.textRenderer.fontHeight * 1.8), nickname);
        this.addDrawableChild(field);
        this.button = new WyvernButtonWidget(left() + 9 + (unit * 2) + 2, top() + this.textRenderer.fontHeight + 13 - 1, unit - 2, (int) (this.textRenderer.fontHeight * 1.8) + 2, Text.translatable("wyvern.gui.set"), (a) -> ClientPlayNetworking.send(new NicknamePayload(field.getText())));
        this.addDrawableChild(this.button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        button.active = !GameWorldComponent.KEY.get(player.getWorld()).isRunning() || !GameFunctions.isPlayerAliveAndSurvival(player);
        super.render(context, mouseX, mouseY, delta);
    }
}
