package net.sylviameows.wyvern.api.registry;

import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.role.Role;

import java.util.Collection;
import java.util.Set;

@SuppressWarnings("UnusedReturnValue")
public interface RoleRegistrar extends Registrar<Role>{

    boolean register(Role role);

    default boolean unregister(Role role) {
        return unregister(role.id());
    }

    boolean unregister(Identifier id);

    Set<Identifier> getIdentifiers();

    Collection<Role> getRoles();

    /**
     * Get the harpy version of the role from an identifier.
     * @param id the id of the role to get.
     * @return the harpy role.
     */
    dev.doctor4t.wathe.api.Role getWathe(Identifier id);


}
