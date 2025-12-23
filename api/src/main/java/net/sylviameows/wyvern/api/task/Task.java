package net.sylviameows.wyvern.api.task;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public interface Task {

    Identifier id();

    default void tick(@NotNull PlayerEntity player) {

    }

    boolean isFulfilled(PlayerEntity player);

    default NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("id", id().toString());
        return nbt;
    }

}
