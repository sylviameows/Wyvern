package net.sylviameows.wyvern.api.role;

import dev.doctor4t.wathe.client.gui.RoleAnnouncementTexts;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.result.WinResult;
import net.sylviameows.wyvern.mixin.RoleAnnouncementTextAccessor;

public class RoleAnnouncement extends RoleAnnouncementTexts.RoleAnnouncementText {
    private final Role role;
    // todo: win reason?

    public RoleAnnouncement(Role role, Text winText) {
        super(role.id().getPath(), role.color());

        RoleAnnouncementTextAccessor accessor = (RoleAnnouncementTextAccessor) this;

        Identifier id = role.id();
        String prefix = "%s.role.%s.".formatted(id.getNamespace(), id.getPath());

        accessor.setRoleText(Text.translatable(prefix+"name").withColor(role.color()));
        accessor.setTitleText(Text.translatable(prefix+"team").withColor(role.color()));

        accessor.setWelcomeText(Text.translatable("wyvern.roles.welcome", roleText));
        accessor.setGoalText(count -> Text.translatable(prefix+"goal").withColor(role.color()));

        accessor.setWinText(winText);

        this.role = role;
    }

    public RoleAnnouncement(Role role) {
        this(role, Text.translatable("wyvern.alignment.%s.win".formatted(role.alignment().alias())).withColor(role.color()));

        if (role.alignment() == Alignment.OTHER) {
            ((RoleAnnouncementTextAccessor) this).setWinText(Text.translatable("wyvern.alignment.other.win", titleText).withColor(role.color()));
        }
    }

    public Text title(PlayerEntity player, WinResult result) {
        return result.isWinner(role, player) ? win() : loss(result);
    }

    protected Text win() {
        return winText;
    }

    protected Text loss(WinResult winResult) {
        return winResult.getDefaultTitle();
    }

}