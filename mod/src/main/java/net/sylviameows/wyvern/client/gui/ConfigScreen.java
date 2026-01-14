package net.sylviameows.wyvern.client.gui;

import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.client.gui.widgets.WyvernButtonWidget;
import net.sylviameows.wyvern.components.ConfigurationComponent;

public class ConfigScreen extends WyvernScreen {
    ConfigurationComponent config;

    public ConfigScreen(ConfigurationComponent config) {
        super(Text.translatable("screen.wyvern.config"), 160, 300);

        this.config = config;
    }

    @Override
    protected void init() {
        this.sizeY = this.textRenderer.fontHeight * 3 + 18 + 2;
        super.init();
        int unit = (sizeX - 18) / 2;

        this.addDrawableChild(new TextWidget(left() + 9, top() + this.textRenderer.fontHeight + 13, (unit) - 2, (int) (this.textRenderer.fontHeight * 1.8), Text.translatable("wyvern.gui.nicknames"), textRenderer).alignLeft());
        this.addDrawableChild(new WyvernButtonWidget(left() + 9 + (unit) + 2, top() + this.textRenderer.fontHeight + 13 - 1, (unit) - 2, (int) (this.textRenderer.fontHeight * 1.8) + 2, config.areNicknamesEnabled() ? Text.translatable("wyvern.gui.enabled") : Text.translatable("wyvern.gui.disabled"), (a) -> {
            config.setNicknamesEnabled(!config.areNicknamesEnabled());
            a.setMessage(config.areNicknamesEnabled() ? Text.translatable("wyvern.gui.enabled") : Text.translatable("wyvern.gui.disabled"));
        }));
    }
}
