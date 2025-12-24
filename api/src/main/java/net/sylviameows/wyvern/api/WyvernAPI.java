package net.sylviameows.wyvern.api;

import net.sylviameows.wyvern.api.registry.ResultRegistrar;
import net.sylviameows.wyvern.api.registry.RoleRegistrar;
import net.sylviameows.wyvern.api.registry.TaskRegistrar;
import net.sylviameows.wyvern.api.shop.DefaultShop;

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

    static DefaultShop defaultShop() {
        return WyvernAPI.getInstance().getDefaultShop();
    }

    RoleRegistrar getRoleRegistry();

    TaskRegistrar getTaskRegistry();

    ResultRegistrar getResultRegistry();

    DefaultShop getDefaultShop();

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
