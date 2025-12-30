package net.sylviameows.wyvern.game.roles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.WyvernColors;
import net.sylviameows.wyvern.api.instinct.Instinct;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.role.options.DynamicOptions;
import net.sylviameows.wyvern.api.role.options.RoleOptions;
import net.sylviameows.wyvern.api.shop.Shop;

public final class KillerRole extends Role {
    public static Identifier IDENTIFIER = Wyvern.id("killer");
    public static int COLOR = WyvernColors.KILLER;

    public KillerRole() {
        super(IDENTIFIER, Alignment.KILLER, COLOR);

        settings.setInstinct(Instinct.getKiller());
        settings.setShop(Shop.getDefault());
    }

    @Override
    public void assign(PlayerEntity player) {

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
