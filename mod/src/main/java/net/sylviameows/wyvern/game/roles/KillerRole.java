package net.sylviameows.wyvern.game.roles;

import dev.doctor4t.wathe.cca.PlayerShopComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.WyvernColors;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.role.options.DynamicOptions;
import net.sylviameows.wyvern.api.role.options.RoleOptions;
import net.sylviameows.wyvern.api.shop.Shop;
import net.sylviameows.wyvern.api.util.Time;
import net.sylviameows.wyvern.game.roles.instinct.KillerInstinct;

public final class KillerRole extends Role {
    public static Identifier IDENTIFIER = Wyvern.id("killer");
    public static int COLOR = WyvernColors.KILLER;

    public KillerRole() {
        super(IDENTIFIER, Alignment.KILLER, COLOR);

        settings.setInstinct(new KillerInstinct());
        settings.setShop(Shop.getDefault());
    }

    @Override
    public void assign(PlayerEntity player) {
        PlayerShopComponent.KEY.get(player).setBalance(100);
    }

    @Override
    public void tick(PlayerEntity player) {
        World world = player.getWorld();

        if (world.getTime() % Time.getInTicks(0, 10) == 0) {
            PlayerShopComponent.KEY.get(player).addToBalance(5);
        }
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
