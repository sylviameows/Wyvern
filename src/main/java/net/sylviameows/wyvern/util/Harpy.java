package net.sylviameows.wyvern.util;

import net.sylviameows.wyvern.api.WyvernAPI;
import net.sylviameows.wyvern.api.role.Role;

public interface Harpy {

    static Role convertRole(dev.doctor4t.trainmurdermystery.api.Role harpy) {
        return WyvernAPI.roles().get(harpy.identifier());
    }

}
