package net.sylviameows.wyvern.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.components.NicknameComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @ModifyReturnValue(method = "getPlayerListName", at = @At("RETURN"))
    private Text wyvern$nickname(Text original) {
        NicknameComponent component = NicknameComponent.KEY.get(this);
        if (component.isUnset()) return original;

        return component.get().copy().append(Text.literal("*").withColor(0xAAAAAA));
    }

}
