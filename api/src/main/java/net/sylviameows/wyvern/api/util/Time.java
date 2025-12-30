package net.sylviameows.wyvern.api.util;

public interface Time {

    int TICKS_PER_SECOND = 20;
    int TICKS_PER_MINUTE = 60 * TICKS_PER_SECOND;

    static int getInTicks(int minute, int second) {
        return (minute * TICKS_PER_MINUTE) + (second * TICKS_PER_SECOND);
    }

    static String ticksToString(int ticks) {
        final int minutes = ticks / TICKS_PER_MINUTE;
        final int remaining = ticks % TICKS_PER_MINUTE;
        final int seconds = remaining / TICKS_PER_SECOND;

        return String.format("%dm %ds", minutes, seconds);
    }

}
