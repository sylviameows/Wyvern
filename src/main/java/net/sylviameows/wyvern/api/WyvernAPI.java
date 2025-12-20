package net.sylviameows.wyvern.api;

import net.sylviameows.wyvern.registry.ResultRegistry;
import net.sylviameows.wyvern.registry.RoleRegistry;
import net.sylviameows.wyvern.registry.TaskRegistry;

public interface WyvernAPI {

    static TaskRegistry tasks() {
        return WyvernAPI.getInstance().getTaskRegistry();
    }

    static RoleRegistry roles() {
        return WyvernAPI.getInstance().getRoleRegistry();
    }

    static ResultRegistry results() {
        return WyvernAPI.getInstance().getResultRegistry();
    }

    RoleRegistry getRoleRegistry();

    TaskRegistry getTaskRegistry();

    ResultRegistry getResultRegistry();

    static WyvernAPI getInstance() {
        return Holder.INSTANCE;
    }

    final class Holder {
        private static WyvernAPI INSTANCE;

        private Holder() {}

        public static void setInstance(WyvernAPI api) {
            INSTANCE = api;
        }
    }

}
