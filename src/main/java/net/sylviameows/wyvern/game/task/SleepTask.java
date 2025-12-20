package net.sylviameows.wyvern.game.task;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.WyvernConstants;
import net.sylviameows.wyvern.api.task.TimedTask;
import org.jetbrains.annotations.NotNull;

public final class SleepTask extends TimedTask {

    public static Identifier IDENTIFIER = Wyvern.id("sleep");

    @Override
    public Identifier id() {
        return IDENTIFIER;
    }

    public SleepTask() {
        this(WyvernConstants.TIMED_TASK_DURATION);
    }

    private SleepTask(int time) {
        super(time);
    }

    @Override
    public void tick(@NotNull PlayerEntity player) {
        if (player.isSleeping()) {
            decrement();
        }
    }

    public static SleepTask fromNbt(NbtCompound nbtCompound) {
        return new SleepTask(nbtCompound.getInt("time"));
    }

}
