package net.sylviameows.wyvern.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import dev.doctor4t.wathe.item.CocktailItem;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.sylviameows.wyvern.api.mood.MoodHandler;
import net.sylviameows.wyvern.api.task.Task;
import net.sylviameows.wyvern.components.NicknameComponent;
import net.sylviameows.wyvern.mixin.components.PlayerMoodComponentAccessor;
import net.sylviameows.wyvern.game.task.DrinkTask;
import net.sylviameows.wyvern.game.task.EatTask;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "eatFood", at = @At("HEAD"))
    private void wyvern$consume(World world, ItemStack stack, FoodComponent foodComponent, @NotNull CallbackInfoReturnable<ItemStack> cir) {
        MoodHandler handler = ((PlayerMoodComponentAccessor) PlayerMoodComponent.KEY.get(this)).wyvern$getMoodHandler();
        if (handler == null) return;

        Task task = handler.getTask();
        if (task == null) return;

        if (stack.getItem() instanceof CocktailItem && task instanceof DrinkTask drinkTask) {
            drinkTask.fulfill();
        } else if (task instanceof EatTask eatTask) {
            eatTask.fulfill();
        }
    }

    @ModifyReturnValue(method = "getDisplayName", at = @At("RETURN"))
    private Text wyvern$scoreboardNickname(Text original) {
        NicknameComponent component = NicknameComponent.KEY.get(this);
        if (component.isUnset()) return original;

        MutableText nickname = component.get().copy();
        Text real = Text.literal("*").styled(style -> style.withColor(0xAAAAAA).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, original)));

        return nickname.append(real);
    }

}