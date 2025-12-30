package net.sylviameows.wyvern.api.util;

import net.sylviameows.wyvern.api.WyvernAPI;
import net.sylviameows.wyvern.api.role.Role;

public interface WatheMigrator {

    static Role migrateRole(dev.doctor4t.wathe.api.Role wathe) {
        if (wathe == null) return null;
        return WyvernAPI.roles().get(wathe.identifier());
    }

}
