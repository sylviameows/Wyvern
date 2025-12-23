package net.sylviameows.wyvern.game.task;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.WyvernConstants;
import net.sylviameows.wyvern.api.task.TimedTask;
import org.jetbrains.annotations.NotNull;

public final class OutsideTask extends TimedTask {

    public static Identifier IDENTIFIER = Wyvern.id("outside");

    @Override
    public Identifier id() {
        return IDENTIFIER;
    }

    public OutsideTask() {
        this(WyvernConstants.TIMED_TASK_DURATION);
    }

    private OutsideTask(int time) {
        super(time);
    }

    @Override
    public void tick(@NotNull PlayerEntity player) {
        if (Wathe.isSkyVisibleAdjacent(player)) {
            decrement();
        }
    }

    public static OutsideTask fromNbt(NbtCompound nbt) {
        return new OutsideTask(nbt.getInt("time"));
    }

}
