package net.sylviameows.wyvern.api.result;

import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.Alignment;

public final class KillerWin extends WinResult {

    public static final KillerWin ELIMINATED = new KillerWin(Wyvern.MOD_ID, "eliminated");

    public KillerWin(String modId, String reason) {
        super(Identifier.of(modId, "killer_%s".formatted(reason)), Alignment.KILLER, reason);
    }

}
