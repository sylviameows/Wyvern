package net.sylviameows.wyvern.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.sylviameows.wyvern.Wyvern;
import org.lwjgl.glfw.GLFW;

public final class WyvernKeybinds {

    private WyvernKeybinds() {}

    public static KeyBinding ABILITY;
    public static KeyBinding SETTINGS;
    public static KeyBinding NICKNAME;

    public static void init() {
        ABILITY = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key."+ Wyvern.MOD_ID+ ".ability",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_Z,
                        "category."+ Wyvern.MOD_ID +".keybinds")
        );
        SETTINGS = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key."+ Wyvern.MOD_ID+ ".settings",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_I,
                        "category."+ Wyvern.MOD_ID +".keybinds")
        );
        NICKNAME = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key."+ Wyvern.MOD_ID+ ".nickname",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_J,
                        "category."+ Wyvern.MOD_ID +".keybinds")
        );
    }
}
