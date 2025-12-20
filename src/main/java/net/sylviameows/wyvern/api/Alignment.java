package net.sylviameows.wyvern.api;

import net.minecraft.text.Text;
import net.sylviameows.wyvern.api.mood.FakeMoodHandler;
import net.sylviameows.wyvern.api.mood.MoodHandler;
import net.sylviameows.wyvern.api.mood.RealMoodHandler;
import net.sylviameows.wyvern.api.role.Role;

import java.util.function.Function;

/**
 * An alignment is another word for a team. Usually a role is aligned with the innocents or killers,
 * however sometimes a role isn't aligned with either. Usually this means they have their own custom win condition or objective!
 * <i>e.g. A "Jester" role may win by getting killed by an innocent.</i>
 */
public enum Alignment {
    INNOCENT(RealMoodHandler::new, "passengers", 0x36E51B),
    KILLER(FakeMoodHandler::new, "killers", 0xC13838),
    OTHER(RealMoodHandler::new, "other", 0xFFFFFF);

    private final Function<Role, MoodHandler> generator;
    private final int color;
    private final String alias;

    /**
     * Create an alignment.
     * @param generator a function to create a mood handler for the role.
     * @param name the name of the alignment, e.g. "passengers", "killers", or "other"
     * @param color the color to represent this alignment.
     */
    Alignment(Function<Role, MoodHandler> generator, String name, int color) {
        this.generator = generator;
        this.color = color;
        this.alias = name;
    }

    public String alias() {
        return alias;
    }

    public Text getAlignmentName() {
        return Text.translatable("wyvern.alignment.%s.name".formatted(alias)).withColor(color);
    }

    public int color() {
        return color;
    }

    public MoodHandler mood(Role role) {
        return generator.apply(role);
    }
}
