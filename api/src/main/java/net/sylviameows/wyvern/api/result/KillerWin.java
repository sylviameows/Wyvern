package net.sylviameows.wyvern.api.result;

import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.Alignment;

public final class KillerWin extends WinResult {

    public KillerWin(String modId, String reason) {
        super(Identifier.of(modId, "killer_%s".formatted(reason)), Alignment.KILLER, reason);
    }

}
