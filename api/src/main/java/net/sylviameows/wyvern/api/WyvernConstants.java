package net.sylviameows.wyvern.api;

import net.sylviameows.wyvern.api.util.Time;

public interface WyvernConstants {

    int FIRST_TASK_COOLDOWN = Time.getInTicks(0, 30);
    int MIN_TASK_COOLDOWN = Time.getInTicks(0, 30);
    int MAX_TASK_COOLDOWN = Time.getInTicks(1, 0);

    int TIMED_TASK_DURATION = Time.getInTicks(0, 8);

    float MOOD_GAIN = 1f / 2;
    float MOOD_DRAIN = 1f / Time.getInTicks(4, 0);

    int ITEM_PSYCHOSIS_REROLL = 200;
    float ITEM_PSYCHOSIS_CHANCE = 1f / 2;

}
