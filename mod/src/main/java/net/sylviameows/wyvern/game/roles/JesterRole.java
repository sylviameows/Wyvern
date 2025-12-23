package net.sylviameows.wyvern.game.roles;

import net.minecraft.entity.player.PlayerEntity;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.role.options.RoleOptions;

/**
 * This is a dummy role, and does nothing in its current state.
 */
public class JesterRole extends Role {
    public JesterRole() {
        super(Wyvern.id("jester"), Alignment.OTHER, 0xFF66AA);
    }

    @Override
    public void assign(PlayerEntity player) {

    }

    @Override
    public RoleOptions defaults(RoleOptions.Builder builder) {
        return builder.count(1).odds(0.7f).build();
    }
}
