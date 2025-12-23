package net.sylviameows.wyvern.registry;

import dev.doctor4t.wathe.api.WatheRoles;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.registry.RoleRegistrar;
import net.sylviameows.wyvern.api.role.Role;

import java.util.*;

public final class RoleRegistry implements RoleRegistrar {

    private final Map<Identifier, Role> roles = new HashMap<>();

    /**
     * Gets a role from an identifier.
     * @param id the id of the role to get.
     * @return the role
     */
    public Role get(Identifier id) {
        return roles.get(id);
    }

    public dev.doctor4t.wathe.api.Role getWathe(Identifier id) {
        return roles.get(id).getWathe();
    }

    public Set<Identifier> getIdentifiers() {
        return roles.keySet();
    }

    public Collection<Role> getRoles() {
        return roles.values();
    }

    @Override
    public boolean register(Role role) {
        roles.put(role.id(), role);
        WatheRoles.registerRole(role.getWathe());
        return true;
    }

    @Override
    public boolean unregister(Identifier id) {
        Role removed = roles.remove(id);
        if (removed != null) {
            WatheRoles.ROLES.remove(removed.getWathe());
            return true;
        }
        return false;
    }

}
