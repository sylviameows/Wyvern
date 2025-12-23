package net.sylviameows.wyvern.api.role.options;

/**
 * Role options are settings that can be modified by players to customize their experience.
 * Custom role options are to be added.
 */
public interface RoleOptions {

    /**
     * @return The chance for a roll to pass.
     */
    float odds();

    /**
     * @return The amount of times to roll for this role.
     */
    int count();

    /**
     * Check if the role is enabled. (is there a chance to be selected, and is the count high enough?)
     * @return can this role appear in matches?
     */
    default boolean enabled() {
        return odds() > 0f && count() > 0;
    }

    /**
     * If wyvern ignores assigning this role.
     * @return if its ignored.
     */
    default boolean ignored() {
        return count() == -1;
    }

    interface Builder {

        /**
         * Set the chance for a roll to pass.
         * @param odds the odds 0f-1f
         */
        Builder odds(float odds);

        /**
         * Set the amount of times to roll.
         * @param count the roll count.
         */
        Builder count(int count);

        /**
         * Make wyvern ignore assigning this role.
         * @return a settings instance made to be ignored.
         */
        RoleOptions ignored();

        RoleOptions build();

        /**
         * Ignores settings and creates a dynamic count role.
         * @param setting the dynamic type to use.
         * @return the dynamic settings.
         */
        RoleOptions dynamic(DynamicOptions setting);

    }

}
