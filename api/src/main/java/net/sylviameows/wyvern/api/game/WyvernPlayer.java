package net.sylviameows.wyvern.api.game;

import dev.doctor4t.wathe.game.GameFunctions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.task.Task;
import org.jetbrains.annotations.Nullable;

public interface WyvernPlayer {

    PlayerEntity entity();

    Role role();
    void role(Role role);

    default Alignment alignment() {
        return role().alignment();
    }

    int balance();
    void balance(int balance);

    float mood();
    @Nullable Task task();

    default boolean alive() {
        return GameFunctions.isPlayerAliveAndSurvival(entity());
    }

    void kill(Identifier reason, @Nullable WyvernPlayer killer);

}
