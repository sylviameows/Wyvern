package net.sylviameows.wyvern.api.mood;

import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.sylviameows.wyvern.api.WyvernConstants;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.task.TaskTypes;

public class RealMoodHandler extends MoodHandler {

    public RealMoodHandler(Role role) {
        super(role, TaskTypes.REAL);
    }

    @Override
    public boolean tick(PlayerMoodComponent component, PlayerEntity player) {
        if (hasTask()) component.setMood(component.getMood() - WyvernConstants.MOOD_DRAIN);

        return super.tick(component, player);
    }

    @Override
    public void completeTask(PlayerMoodComponent component, PlayerEntity player) {
        component.setMood(component.getMood() + WyvernConstants.MOOD_GAIN);
        super.completeTask(component, player);
    }

}
