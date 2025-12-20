package net.sylviameows.wyvern.api.result;

import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.Alignment;

public final class PassengerWin extends WinResult {

    public static final PassengerWin TIME = new PassengerWin(Wyvern.MOD_ID, "time");
    public static final PassengerWin ELIMINATED = new PassengerWin(Wyvern.MOD_ID, "eliminated");

    public PassengerWin(String modId, String reason) {
        super(Identifier.of(modId, "passenger_%s".formatted(reason)), Alignment.INNOCENT, reason);
    }

}
