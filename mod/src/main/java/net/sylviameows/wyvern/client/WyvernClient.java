package net.sylviameows.wyvern.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.client.gui.NicknameScreen;
import net.sylviameows.wyvern.client.gui.WyvernScreen;
import net.sylviameows.wyvern.payloads.BoardPayload;

public final class WyvernClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Wyvern.LOGGER.info("Setting up Wyvern client modifications.");

        // receive payloads.
        ClientPlayNetworking.registerGlobalReceiver(BoardPayload.ID, new BoardPayload.Receiver());

        WyvernKeybinds.init();

        ClientTickEvents.START_WORLD_TICK.register(world -> {
            if (WyvernKeybinds.NICKNAME.isPressed()) {
                MinecraftClient client = MinecraftClient.getInstance();
                client.setScreen(new NicknameScreen(client.player));
            }
        });

    }
}
