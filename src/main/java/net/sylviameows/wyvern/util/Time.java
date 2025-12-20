package net.sylviameows.wyvern.util;

public interface Time {
    int TICKS_PER_SECOND = 20;
    int TICKS_PER_MINUTE = 60 * TICKS_PER_SECOND;

    static int getInTicks(int minute, int second) {
        return (minute * TICKS_PER_MINUTE) + (second * TICKS_PER_SECOND);
    }
}
