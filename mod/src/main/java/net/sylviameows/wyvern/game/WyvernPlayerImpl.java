package net.sylviameows.wyvern.game;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.game.WyvernPlayer;
import net.sylviameows.wyvern.api.mood.MoodHandler;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.task.Task;
import net.sylviameows.wyvern.mixin.components.PlayerMoodComponentAccessor;
import org.jetbrains.annotations.Nullable;

public class WyvernPlayerImpl implements WyvernPlayer {

    private final PlayerShopComponent shop;
    private final PlayerMoodComponent mood;

    private final PlayerEntity player;
    private Role role;

    public WyvernPlayerImpl(PlayerEntity player) {
        this.player = player;

        this.shop = PlayerShopComponent.KEY.get(player);
        this.mood = PlayerMoodComponent.KEY.get(player);
    }

    @Override
    public PlayerEntity entity() {
        return player;
    }

    @Override
    public Role role() {
        return role;
    }

    @Override
    public void role(Role role) {
        GameWorldComponent.KEY.get(player.getWorld()).addRole(player, role.getWathe());
        this.role = role;
    }

    @Override
    public int balance() {
        return shop.balance;
    }

    @Override
    public void balance(int balance) {
        shop.setBalance(balance);
    }

    @Override
    public float mood() {
        return mood.getMood();
    }

    @Override
    public @Nullable Task task() {
        MoodHandler handler = ((PlayerMoodComponentAccessor) mood).wyvern$getMoodHandler();
        if (handler == null) return null;
        return handler.getTask();
    }

    @Override
    public void kill(Identifier reason, @Nullable WyvernPlayer killer) {

    }
}
