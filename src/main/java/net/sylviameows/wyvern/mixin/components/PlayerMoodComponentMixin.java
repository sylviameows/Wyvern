package net.sylviameows.wyvern.mixin.components;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import dev.doctor4t.wathe.game.GameFunctions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import net.sylviameows.wyvern.api.WyvernConstants;
import net.sylviameows.wyvern.WyvernGamemode;
import net.sylviameows.wyvern.api.mood.MoodHandler;
import net.sylviameows.wyvern.util.migration.WatheMigrator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(PlayerMoodComponent.class)
public abstract class PlayerMoodComponentMixin {

    @Unique
    private MoodHandler handler;

    @Final
    @Shadow
    private PlayerEntity player;

    @Shadow
    @Final
    public Map<PlayerMoodComponent.Task, PlayerMoodComponent.TrainTask> tasks;

    @Shadow
    public abstract void sync();

    @Shadow
    public abstract boolean isLowerThanMid();

    @Shadow
    protected abstract List<Item> getPsychosisItemPool();

    @Shadow
    @Final
    private HashMap<UUID, ItemStack> psychosisItems;

    @Shadow
    private float mood;

    @Unique
    public void wyvern$setMoodHandler(MoodHandler handler) {
        this.handler = handler;
        this.sync();
    }

    @Unique
    public MoodHandler wyvern$getMoodHandler() {
        return this.handler;
    }

    @Inject(method = "reset", at = @At("HEAD"))
    private void wyvern$reset(CallbackInfo ci) {
        handler = null;
    }

    @Inject(method = "setMood", at = @At("HEAD"), cancellable = true)
    private void wyvern$setMood(float mood, CallbackInfo ci) {
        var harpy = GameWorldComponent.KEY.get(this.player.getWorld()).getRole(player);
        if (harpy != null && WatheMigrator.migrateRole(harpy) != null) {
            // this role will use custom handler, thus the "MoodType" is irrelevant.
            this.mood = Math.clamp(mood, 0f, 1f);

            this.sync();
            ci.cancel();
        }
    }

    @Inject(method = "getMood", at = @At("HEAD"), cancellable = true)
    private void wyvern$getMood(CallbackInfoReturnable<Float> cir) {
        GameWorldComponent game =  GameWorldComponent.KEY.get(this.player.getWorld());
        var harpy = game.getRole(player);
        if (harpy != null && WatheMigrator.migrateRole(harpy) != null) {
            // this role will use custom handler, thus the "MoodType" is irrelevant.
            if (!game.isRunning()) return;
            cir.setReturnValue(this.mood);
        }
    }

    @Inject(method = "clientTick", at = @At("HEAD"), cancellable = true)
    private void wyvern$clientTick(CallbackInfo ci) {
        GameWorldComponent game = GameWorldComponent.KEY.get(this.player.getWorld());
        if (game.isRunning() && game.getGameMode() instanceof WyvernGamemode) {
            if (handler == null || !GameFunctions.isPlayerAliveAndSurvival(player)) return;
            if (handler.tick((PlayerMoodComponent) (Object) this, player)) this.sync();

            if (this.isLowerThanMid()) {
                for (PlayerEntity player : this.player.getWorld().getPlayers()) {
                    if (player.equals(this.player) || this.player.getWorld().getRandom().nextInt(WyvernConstants.ITEM_PSYCHOSIS_REROLL) != 0) continue;

                    ItemStack hallucination;
                    List<Item> tagged = getPsychosisItemPool();

                    Random random = this.player.getRandom();
                    if (!tagged.isEmpty() && random.nextFloat() < WyvernConstants.ITEM_PSYCHOSIS_CHANCE) {
                        Item item = Util.getRandom(tagged, random);
                        hallucination = new ItemStack(item);
                    } else {
                        hallucination = player.getMainHandStack();
                    }

                    this.psychosisItems.put(player.getUuid(), hallucination);
                }
            } else if (!this.psychosisItems.isEmpty()) {
                this.psychosisItems.clear();
            }

            ci.cancel();
        }
    }

    @Inject(method = "serverTick", at = @At("HEAD"), cancellable = true)
    private void wyvern$serverTick(CallbackInfo ci) {
        GameWorldComponent game = GameWorldComponent.KEY.get(this.player.getWorld());
        if (game.isRunning() && game.getGameMode() instanceof WyvernGamemode) {
            if (handler == null || !GameFunctions.isPlayerAliveAndSurvival(player)) return;
            if (handler.tick((PlayerMoodComponent) (Object) this, player)) this.sync();
            ci.cancel();
        }
    }

    @Inject(method = "writeToNbt", at = @At("HEAD"))
    private void wyvern$writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        if (handler != null) {
            NbtCompound handlerNbt = new NbtCompound();
            handler.writeNbt(handlerNbt);
            nbt.put("handler", handlerNbt);
        }
    }

    @Inject(method = "readFromNbt", at = @At("HEAD"))
    private void wyvern$readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        NbtCompound handlerNbt = tag.getCompound("handler");
        if (!handlerNbt.isEmpty()) {
            if (handler == null || !handler.update(handlerNbt)) {
                // handler source doesnt match, get new matching handler.
                handler = MoodHandler.fromNbt(handlerNbt);
            }
        } else if (handler != null) {
            wyvern$setMoodHandler(null);
        }
    }

}
