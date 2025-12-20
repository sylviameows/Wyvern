package net.sylviameows.wyvern.components;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.Wyvern;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class NicknameComponent implements AutoSyncedComponent {

    public static final ComponentKey<NicknameComponent> KEY = ComponentRegistry.getOrCreate(Wyvern.id("nickname"), NicknameComponent.class);
    private final PlayerEntity player;

    private @Nullable String nickname = null;
    private int color = 0xFFFFFF;

    public NicknameComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(this.player);
    }

    public void set(@Nullable String nickname, int color) {
        this.nickname = nickname;
        this.color = color;
        this.sync();
    }

    public void setNickname(@Nullable String nickname) {
        set(nickname, color);
    }

    public void setColor(int color) {
        set(nickname, color);
    }

    public boolean isUnset() {
        return nickname == null;
    }

    public Text get() {
        MutableText name = player.getName().copy();
        if (nickname == null) return name.withColor(color);
        return Text.literal(nickname).withColor(color);
    }


    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (tag.contains("nickname")) nickname = tag.getString("nickname");
        else nickname = null;
        color = tag.getInt("color");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (nickname != null) tag.putString("nickname", nickname);
        tag.putInt("color", color);
    }

}
