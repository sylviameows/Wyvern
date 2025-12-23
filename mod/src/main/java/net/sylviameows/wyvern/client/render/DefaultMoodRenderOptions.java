package net.sylviameows.wyvern.client.render;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import dev.doctor4t.wathe.cca.PlayerPsychoComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.mood.MoodHandler;
import net.sylviameows.wyvern.api.mood.render.MoodRenderOptions;
import net.sylviameows.wyvern.api.role.Role;
import org.jetbrains.annotations.Nullable;

public class DefaultMoodRenderOptions implements MoodRenderOptions {

    private Identifier banner;
    private int width = 14; // texture width
    private int height = 17; // texture height

    private float mood;
    private @Nullable Integer color;
    private @Nullable Text text;

    public DefaultMoodRenderOptions(Role role, MoodHandler handler, PlayerEntity player) {
        PlayerMoodComponent mood = PlayerMoodComponent.KEY.get(player);

        this.mood = mood.getMood();
        this.color = role.alignment().color();

        if (handler.getTask() != null) {
            this.text = handler.type().text(handler.getTask());
        }

        switch (role.alignment()) {
            case KILLER -> {
                PlayerPsychoComponent psycho = PlayerPsychoComponent.KEY.get(player);
                if (psycho.getPsychoTicks() > 0) {
                    // psycho rendering is handled by Wathe, so this is only here as a backup.
                    banner = Wathe.id("hud/mood_psycho");
                }
                banner = Wathe.id("hud/mood_killer");

            }
            case INNOCENT -> {
                color = null; // handle
                if (mood.isLowerThanDepressed()) {
                    banner = Wathe.id("hud/mood_depressed");
                } else if (mood.isLowerThanMid()) {
                    banner = Wathe.id("hud/mood_mid");
                } else {
                    banner = Wathe.id("hud/mood_happy");
                }
            }
            case OTHER ->  {
                // default banner for "other" roles.
                banner = Wyvern.id("hud/mood/neutral");
            }
        }
    }

    @Override
    public void setBanner(Identifier id, int width, int height) {
        this.banner = id;
        this.width = width;
        this.height = height;
    }

    @Override
    public void setMood(float mood) {
        this.mood = mood;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Identifier getTexture() {
        return banner;
    }

    @Override
    public float getMood() {
        return mood;
    }

    @Override
    public @Nullable Integer getColor() {
        return color;
    }

    @Override
    public @Nullable Text getText() {
        return text;
    }

}
