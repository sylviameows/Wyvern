package net.sylviameows.wyvern.api;

import net.sylviameows.wyvern.api.registry.ResultRegistrar;
import net.sylviameows.wyvern.api.registry.RoleRegistrar;
import net.sylviameows.wyvern.api.registry.TaskRegistrar;

public interface WyvernAPI {

    static TaskRegistrar tasks() {
        return WyvernAPI.getInstance().getTaskRegistry();
    }

    static RoleRegistrar roles() {
        return WyvernAPI.getInstance().getRoleRegistry();
    }

    static ResultRegistrar results() {
        return WyvernAPI.getInstance().getResultRegistry();
    }

    RoleRegistrar getRoleRegistry();

    TaskRegistrar getTaskRegistry();

    ResultRegistrar getResultRegistry();

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
