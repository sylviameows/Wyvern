package net.sylviameows.wyvern.api.mood.render;

import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.Alignment;
import org.jetbrains.annotations.Nullable;

/**
 * Modify the way the players mood renders with simple options.
 */
public interface MoodRenderOptions {

    /**
     * Set the banner that gets rendered for this handler.
     * <br><b>When using this method textures must be 14 pixels wide and 17 pixels tall.</b>
     * @param id an identifier that leads to the banner texture (e.g. {@code wyvern:mood/killer})
     */
    default void setBanner(Identifier id) {
        setBanner(id, 14, 17);
    }

    /**
     * Set the banner that gets rendered for this handler with a custom width and height.
     * <br><b>For best results use a texture resolution divisible by the defaults.</b>
     * @param id an identifier that leads to the banner texture (e.g. {@code wyvern:mood/killer})
     * @param width the width (default {@code 14px})
     * @param height the height (default {@code 17px})
     */
    void setBanner(Identifier id, int width, int height);

    /**
     * Set the percent of mood the player renders. Could be useful for displaying ability durations?
     * <br><i>Changing this does not change the players actual mood value.</i>
     * @param mood 0f-1f mood value to display (default: {@link PlayerMoodComponent#getMood()})
     */
    void setMood(float mood);

    /**
     * Set the color the bar renders as.
     * @param color The color value to display. (default: {@link Alignment#color()})
     */
    void setColor(int color);

    /**
     * Set the text displayed in the mood bar. Setting this means the bar will render,
     * otherwise set to null if you don't want the bar to render.
     * @param text The text to display. (defaults to the tasks text)
     */
    void setText(@Nullable Text text);

    int getWidth();
    int getHeight();

    Identifier getTexture();

    float getMood();

    @Nullable Integer getColor();

    @Nullable Text getText();

}
