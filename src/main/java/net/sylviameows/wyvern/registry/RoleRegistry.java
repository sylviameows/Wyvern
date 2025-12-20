package net.sylviameows.wyvern.registry;

import dev.doctor4t.trainmurdermystery.api.TMMRoles;
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

    public dev.doctor4t.trainmurdermystery.api.Role getHarpy(Identifier id) {
        return roles.get(id).getHarpy();
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
        TMMRoles.registerRole(role.getHarpy());
        return true;
    }

    @Override
    public boolean unregister(Identifier id) {
        Role removed = roles.remove(id);
        if (removed != null) {
            TMMRoles.ROLES.remove(removed.getHarpy());
            return true;
        }
        return false;
    }

}
