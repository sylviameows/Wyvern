package net.sylviameows.wyvern.api.registry;

import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.role.Role;

@SuppressWarnings("UnusedReturnValue")
public interface RoleRegistrar extends Registrar<Role>{

    boolean register(Role role);

    default boolean unregister(Role role) {
        return unregister(role.id());
    }

    boolean unregister(Identifier id);

    /**
     * Get the harpy version of the role from an identifier.
     * @param id the id of the role to get.
     * @return the harpy role.
     */
    dev.doctor4t.trainmurdermystery.api.Role getHarpy(Identifier id);


}
