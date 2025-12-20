package net.sylviameows.wyvern.api.task;

import net.minecraft.entity.player.PlayerEntity;

public abstract class FulfilledTask implements Task {
    protected boolean fulfilled = false;

    @Override
    public boolean isFulfilled(PlayerEntity player) {
        return fulfilled;
    }

    public void fulfill() {
        fulfilled = true;
    }
}
