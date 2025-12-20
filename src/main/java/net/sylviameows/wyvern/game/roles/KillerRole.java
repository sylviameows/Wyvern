package net.sylviameows.wyvern.game.roles;

import dev.doctor4t.trainmurdermystery.cca.PlayerShopComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.role.options.DynamicOptions;
import net.sylviameows.wyvern.api.role.options.RoleOptions;

public final class KillerRole extends Role {
    public static Identifier IDENTIFIER = Wyvern.id("killer");
    public static int COLOR = 0xC13838;

    public KillerRole() {
        super(IDENTIFIER, Alignment.KILLER, COLOR);
    }

    @Override
    public void assign(PlayerEntity player) {
        PlayerShopComponent.KEY.get(player).setBalance(100);
    }

    @Override
    public RoleOptions defaults(RoleOptions.Builder builder) {
        return builder.dynamic(DynamicOptions.ALL_UNASSIGNED);
    }

    @Override
    public boolean unique() {
        return false;
    }
}
