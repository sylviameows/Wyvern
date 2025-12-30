package net.sylviameows.wyvern.game.roles;

import dev.doctor4t.wathe.index.WatheItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.WyvernColors;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.role.options.DynamicOptions;
import net.sylviameows.wyvern.api.role.options.RoleOptions;

public final class VigilanteRole extends Role {
    public static Identifier IDENTIFIER = Wyvern.id("vigilante");
    public static int COLOR = WyvernColors.VIGILANTE;

    public VigilanteRole() {
        super(IDENTIFIER, Alignment.INNOCENT, COLOR);
    }

    @Override
    public void assign(PlayerEntity player) {
        player.giveItemStack(new ItemStack(WatheItems.REVOLVER));
    }

    @Override
    public RoleOptions defaults(RoleOptions.Builder builder) {
        return builder.dynamic(DynamicOptions.MATCH_KILLERS);
    }
}
