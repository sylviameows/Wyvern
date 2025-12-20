package net.sylviameows.wyvern.api.role;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.mood.MoodHandler;
import net.sylviameows.wyvern.api.role.options.RoleOptions;

public abstract class Role {
    private final Identifier id;
    private final Alignment alignment;
    private final int color;

    protected Role(Identifier id, Alignment alignment, int color) {
        this.id = id;
        this.alignment = alignment;
        this.color = color;
    }

    public MoodHandler getMoodHandler() {
        return alignment.mood(this);
    }

    public abstract void assign(PlayerEntity player);

    public Identifier id() {
        return id;
    }

    public Alignment alignment() {
        return alignment;
    }

    public int color() {
        return color;
    }

    /**
     * @return if the role is unique, or displays in the end screen.
     */
    public boolean unique() {
        return true;
    }

    abstract public RoleOptions defaults(RoleOptions.Builder builder);

    public RoleAnnouncement announcement() {
        return new RoleAnnouncement(this);
    }

    // harpy compat:

    private dev.doctor4t.trainmurdermystery.api.Role harpy;
    public final dev.doctor4t.trainmurdermystery.api.Role getHarpy() {
        if (harpy == null) {
            this.harpy = new dev.doctor4t.trainmurdermystery.api.Role(
                    id,
                    color,
                    alignment == Alignment.INNOCENT,
                    alignment == Alignment.KILLER,
                    dev.doctor4t.trainmurdermystery.api.Role.MoodType.NONE,
                    -1,
                    alignment == Alignment.KILLER
            );
        }

        return harpy;
    }


}
