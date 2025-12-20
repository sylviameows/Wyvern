package net.sylviameows.wyvern.api.result;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.role.Role;

/**
 * A win result where only one role is the victor, usually "other" aligned roles.
 */
@SuppressWarnings("unused")
public final class RoleWin extends WinResult {
    private final Role role;

    public RoleWin(Role role, String reason) {
        super(Identifier.of(
                role.id().getNamespace(),
                "role_%s".formatted(role.id().getPath())),
                role.alignment(),
                reason
        );

        this.role = role;
    }

    @Override
    public Text getDefaultTitle() {
        return role.announcement().winText;
    }

    @Override
    public boolean isWinner(Role role, PlayerEntity player) {
        return role == this.role;
    }

    @Override
    public Text getReasonText() {
        return Text.translatable("%s.result.%s.%s".formatted(identifier.getNamespace(), role.id().getPath(), reason));
    }

}
