package net.sylviameows.wyvern.api.mood;

import dev.doctor4t.trainmurdermystery.cca.PlayerMoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.task.TaskTypes;

public class FakeMoodHandler extends MoodHandler {

    public FakeMoodHandler(Role role) {
        super(role, TaskTypes.FAKE);
    }

    @Override
    public boolean tick(PlayerMoodComponent component, PlayerEntity player) {
        component.setMood(1f); // fake mood should always be full.
        return super.tick(component, player);
    }

}
