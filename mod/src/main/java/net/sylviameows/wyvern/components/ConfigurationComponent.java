package net.sylviameows.wyvern.components;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.role.options.RoleOptions;
import net.sylviameows.wyvern.game.configuration.WyvernRoleOptions;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationComponent implements AutoSyncedComponent {

    public static final ComponentKey<ConfigurationComponent> KEY = ComponentRegistry.getOrCreate(Wyvern.id("configuration"), ConfigurationComponent.class);
    private final World world;

    private boolean nicknames = true;

    public float killerRatio = 1f / 6f;
    public Map<Role, RoleOptions> settings = new HashMap<>();

    public ConfigurationComponent(World world) {
        this.world = world;
    }

    public void sync() {
        KEY.sync(this.world);
    }

    public RoleOptions getOptions(Role role) {
        if (settings.containsKey(role)) {
            return settings.get(role);
        } else {
            RoleOptions defaults = role.defaults(new WyvernRoleOptions.Builder());
            settings.put(role, defaults);
            return defaults;
        }
    }

    public boolean areNicknamesEnabled() {
        return nicknames;
    }

    public void setNicknamesEnabled(boolean enabled) {
        this.nicknames = enabled;
        this.sync();
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.getPlayers().forEach(player -> {
                serverWorld.getServer().getPlayerManager().sendToAll(
                        new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, player)
                );
            });
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.nicknames = tag.getBoolean("nicknames");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putBoolean("nicknames", nicknames);
    }
}
