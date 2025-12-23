package net.sylviameows.wyvern.game;

import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.result.KillerWin;
import net.sylviameows.wyvern.api.result.PassengerWin;

public interface GameResults {
    PassengerWin passengerByTime = new PassengerWin(Wyvern.MOD_ID, "time");
    PassengerWin passengerByEliminations = new PassengerWin(Wyvern.MOD_ID, "eliminated");

    KillerWin killerByEliminations = new KillerWin(Wyvern.MOD_ID, "eliminated");
}
