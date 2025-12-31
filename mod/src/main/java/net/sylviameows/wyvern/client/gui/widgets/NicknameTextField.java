package net.sylviameows.wyvern.client.gui.widgets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.components.NicknameComponent;

public class NicknameTextField extends TextFieldWidget {

    private final NicknameComponent component;

    public NicknameTextField(TextRenderer renderer, int x, int y, int width, int height, NicknameComponent nickname) {
        super(renderer, x, y, width, height, Text.empty());
        this.setText(nickname.get().getString());

        this.component = nickname;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.getText().isEmpty()) {
            setSuggestion(component.player().getName().getString());
        } else {
            setSuggestion(null);
        }
        super.renderWidget(context, mouseX, mouseY, delta);
    }
}
