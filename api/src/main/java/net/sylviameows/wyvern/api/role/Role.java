package net.sylviameows.wyvern.api.role;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.mood.MoodHandler;
import net.sylviameows.wyvern.api.role.options.RoleOptions;
import net.sylviameows.wyvern.api.role.settings.RoleSettings;

public abstract class Role {
    private final Identifier id;
    protected final RoleSettings settings;

    protected Role(Identifier id, Alignment alignment, int color) {
        this.id = id;
        this.settings = new RoleSettings(color, alignment);
    }

    public MoodHandler getMoodHandler() {
        return settings.getAlignment().mood(this);
    }

    public abstract void assign(PlayerEntity player);

    public Identifier id() {
        return id;
    }

    /**
     * Gets the role settings instance;
     */
    public RoleSettings settings() {
        return settings;
    }

    public Alignment alignment() {
        return settings.getAlignment();
    }

    public int color() {
        return settings.getColor();
    }

    /**
     * @return if the role is unique, or displays in the end screen.
     */
    public boolean unique() {
        return true;
    }

    abstract public RoleOptions defaults(RoleOptions.Builder builder);

    public RoleAnnouncements announcement() {
        return new RoleAnnouncements(this);
    }

    // harpy compat:

    private dev.doctor4t.wathe.api.Role wathe;
    public final dev.doctor4t.wathe.api.Role getWathe() {
        if (wathe == null) {
            this.wathe = new dev.doctor4t.wathe.api.Role(
                    id,
                    settings.getColor(),
                    settings.isInnocent(),
                    settings.canUseKiller(),
                    dev.doctor4t.wathe.api.Role.MoodType.NONE, // Wyvern addons use "MoodHandler" so mood type should be ignored.
                    settings.getMaxStamina(),
                    settings.canSeeTime()
            );
        }

        return wathe;
    }


}
