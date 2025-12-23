package net.sylviameows.wyvern.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.payloads.BoardPayload;

public class WyvernClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Wyvern.LOGGER.info("Setting up Wyvern client modifications.");

        // receive payloads.
        ClientPlayNetworking.registerGlobalReceiver(BoardPayload.ID, new BoardPayload.Receiver());

    }
}
