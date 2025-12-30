package net.sylviameows.wyvern.api.role.settings;

import dev.doctor4t.wathe.api.Role;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.instinct.Instinct;
import net.sylviameows.wyvern.api.shop.Shop;
import net.sylviameows.wyvern.api.util.Time;
import org.jetbrains.annotations.Nullable;

/**
 * Role settings determine how the role interacts with other features from wathe and wyvern.
 */
public final class RoleSettings {

    private int color;
    private Alignment alignment;

    private int stamina;
    private boolean consideredEvil;
    private boolean canSeeTime;

    // @TODO("Replace with a custom Instinct setting and Shop API.")
    private boolean canUseKiller;

    private @Nullable Shop shop;
    private @Nullable Instinct instinct;

    public RoleSettings(int color, Alignment alignment) {
        this.color = color;
        this.alignment = alignment;

        // defaults from alignment:
        this.consideredEvil = alignment != Alignment.INNOCENT;
        this.canUseKiller = alignment == Alignment.KILLER;
        this.canSeeTime = canUseKiller;
        this.stamina = canUseKiller ? -1 : Time.getInTicks(0, 10);
    }

    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }

    public Alignment getAlignment() {
        return alignment;
    }
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    /**
     * Get how long a player can sprint in ticks. Is set to {@code -1} if infinite stamina. <br>
     * Equivalent of {@link Role#getMaxSprintTime()}
     */
    public int getMaxStamina() {
        return stamina;
    }
    /**
     * Set how long a player can sprint in ticks. Set to {@code -1} for infinite stamina. <br>
     * Equivalent of setting {@link Role#getMaxSprintTime()}
     */
    public void setMaxStamina(int stamina) {
        this.stamina = stamina;
    }

    /**
     * Get if killing this player drops the revolver/tries to backfire. <br>
     * Equivalent of {@link dev.doctor4t.wathe.api.Role#isInnocent()}
     */
    public boolean isInnocent() {
        return !consideredEvil;
    }
    /**
     * Set if killing this player drops the revolver/tries to backfire. <br>
     * Equivalent of setting {@link dev.doctor4t.wathe.api.Role#isInnocent()}
     */
    public void setInnocent(boolean innocence) {
        this.consideredEvil = !innocence;
    }

    /**
     * Get if killing this player drops the revolver/tries to backfire. <br>
     * Equivalent of {@link dev.doctor4t.wathe.api.Role#canUseKiller()}
     */
    public boolean canUseKiller() {
        return canUseKiller;
    }
    /**
     * Set if killing this player drops the revolver/tries to backfire. <br>
     * Equivalent of setting {@link dev.doctor4t.wathe.api.Role#canUseKiller()}
     */
    public void setCanUseKiller(boolean canUse) {
        this.canUseKiller = canUse;
    }

    /**
     * Get if this player can see the match timer. <br>
     * Equivalent of {@link Role#canSeeTime()}
     */
    public boolean canSeeTime() {
        return canSeeTime;
    }
    /**
     * Set if this player can see the match timer. <br>
     * Equivalent of setting {@link Role#canSeeTime()}
     */
    public void setCanSeeTime(boolean showTime) {
        this.canSeeTime = showTime;
    }

    public boolean hasInstinct() {
        return instinct != null;
    }

    public void setInstinct(@Nullable Instinct instinct) {
        this.instinct = instinct;
    }

    public @Nullable Instinct getInstinct() {
        return instinct;
    }

    public boolean hasShop() {
        return shop != null;
    }

    public void setShop(@Nullable Shop shop) {
        this.shop = shop;
    }

    public @Nullable Shop getShop() {
        return shop;
    }

}
