package net.sylviameows.wyvern.game.task;

import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.task.FulfilledTask;

public final class DrinkTask extends FulfilledTask {

    public static Identifier IDENTIFIER = Wyvern.id("drink");

    @Override
    public Identifier id() {
        return IDENTIFIER;
    }

}
