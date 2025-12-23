package net.sylviameows.wyvern.game.task;

import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.task.FulfilledTask;

public final class EatTask extends FulfilledTask {

    public static Identifier IDENTIFIER = Wyvern.id("eat");

    @Override
    public Identifier id() {
        return IDENTIFIER;
    }

}
