package net.sylviameows.wyvern.api.result;

import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.Alignment;

public final class PassengerWin extends WinResult {

    public PassengerWin(String modId, String reason) {
        super(Identifier.of(modId, "passenger_%s".formatted(reason)), Alignment.INNOCENT, reason);
    }

}
