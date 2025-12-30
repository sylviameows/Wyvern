package net.sylviameows.wyvern.api.instinct;

import net.minecraft.entity.Entity;
import net.sylviameows.wyvern.api.WyvernAPI;
import org.jetbrains.annotations.Nullable;

public interface Instinct {

    static Instinct getKiller() {
        return WyvernAPI.getInstance().getKillerInstinct();
    }

    @Nullable InstinctResult resolve(Entity entity);

}
