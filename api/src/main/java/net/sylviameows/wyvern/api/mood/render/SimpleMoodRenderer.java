package net.sylviameows.wyvern.api.mood.render;

/**
 * Extend this interface for simple mood rendering options like custom text, banners, and colors.
 */
public interface SimpleMoodRenderer {

    /**
     * Modify how the mood bar displays with an options interface.
     * @param options the options object.
     */
    void apply(MoodRenderOptions options);

}
