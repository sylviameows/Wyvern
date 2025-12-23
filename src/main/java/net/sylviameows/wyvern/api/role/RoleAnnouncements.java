package net.sylviameows.wyvern.api.role;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.result.WinResult;
import net.sylviameows.wyvern.mixin.RoleAnnouncementTextAccessor;

import java.util.function.Function;

public class RoleAnnouncements {
    private final Role role;
    // todo: win reason?

    private final Text name;
    private final Text team;
    private final Text welcome;
    private final Function<Integer, Text> goal;

    private final Text winText;

    public RoleAnnouncements(Role role, Text winText) {
        Identifier id = role.id();
        String prefix = "%s.role.%s.".formatted(id.getNamespace(), id.getPath());

        name = Text.translatable(prefix+"name").withColor(role.color());
        team = Text.translatable(prefix+"team").withColor(role.color());

        welcome = Text.translatable("wyvern.roles.welcome", name);
        goal = count -> Text.translatable(prefix+"goal").withColor(role.color());
        this.winText = winText;

        this.role = role;
    }

    public RoleAnnouncements(Role role) {
        this(role, Text.translatable("wyvern.alignment.%s.win".formatted(role.alignment().alias())).withColor(role.color()));

        if (role.alignment() == Alignment.OTHER) {
            ((RoleAnnouncementTextAccessor) this).setWinText(Text.translatable("wyvern.alignment.other.win", team).withColor(role.color()));
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

    public Role getRole() {
        return role;
    }

    public Text getName() {
        return name;
    }

    public Text getTeam() {
        return team;
    }

    public Text getWelcome() {
        return welcome;
    }

    public Function<Integer, Text> getGoal() {
        return goal;
    }

    public Text getWinText() {
        return winText;
    }

}