package net.sylviameows.wyvern.game.roles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.WyvernColors;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.role.options.DynamicOptions;
import net.sylviameows.wyvern.api.role.options.RoleOptions;

public final class CivilianRole extends Role {
    public static Identifier IDENTIFIER = Wyvern.id("civilian");
    public static int COLOR = WyvernColors.CIVILIAN;

    public CivilianRole() {
        super(IDENTIFIER, Alignment.INNOCENT, COLOR);
    }

    @Override
    public void assign(PlayerEntity player) {
        // do nothing!
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
