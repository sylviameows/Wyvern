package net.sylviameows.wyvern.api.registry;

import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.result.WinResult;

@SuppressWarnings("UnusedReturnValue")
public interface ResultRegistrar extends Registrar<WinResult> {

    boolean register(WinResult status);

}
