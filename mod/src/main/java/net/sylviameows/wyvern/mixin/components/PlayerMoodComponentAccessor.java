package net.sylviameows.wyvern.mixin.components;

import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import net.sylviameows.wyvern.api.mood.MoodHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerMoodComponent.class)

public interface PlayerMoodComponentAccessor {

    @SuppressWarnings("MixinAnnotationTarget")
    @Invoker("wyvern$setMoodHandler")
    void wyvern$setMoodHandler(MoodHandler handler);

    @SuppressWarnings("MixinAnnotationTarget")
    @Invoker("wyvern$getMoodHandler")
    MoodHandler wyvern$getMoodHandler();

}
