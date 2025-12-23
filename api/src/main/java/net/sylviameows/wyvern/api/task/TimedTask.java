package net.sylviameows.wyvern.api.task;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public abstract class TimedTask implements Task {

    protected int timeLeft;

    public TimedTask(int time) {
        this.timeLeft = time;
    }

    int getTimeLeft() {
        return timeLeft;
    }

    protected void decrement() {
        if (timeLeft > 0) timeLeft--;
    }

    public boolean isFulfilled(PlayerEntity player) {
        return getTimeLeft() <= 0;
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = Task.super.toNbt();
        nbt.putInt("time", getTimeLeft());
        return nbt;
    }

}
