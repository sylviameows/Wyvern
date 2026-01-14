package net.sylviameows.wyvern.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.components.ConfigurationComponent;
import net.sylviameows.wyvern.components.NicknameComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Shadow
    public abstract ServerWorld getServerWorld();

    @ModifyReturnValue(method = "getPlayerListName", at = @At("RETURN"))
    private Text wyvern$nickname(Text original) {
        ConfigurationComponent config = ConfigurationComponent.KEY.get(this.getServerWorld());
        if (!config.areNicknamesEnabled()) return original;

        NicknameComponent component = NicknameComponent.KEY.get(this);
        if (component.isUnset()) return original;

        return component.get().copy().append(Text.literal("*").withColor(0xAAAAAA));
    }

}
