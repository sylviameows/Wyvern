package net.sylviameows.wyvern.util;

import dev.doctor4t.wathe.client.gui.RoleAnnouncementTexts;
import net.sylviameows.wyvern.api.role.RoleAnnouncements;
import net.sylviameows.wyvern.mixin.RoleAnnouncementTextAccessor;

public interface WyvernMigrator {

    static RoleAnnouncementTexts.RoleAnnouncementText migrateAnnouncement(RoleAnnouncements from) {
        RoleAnnouncementTexts.RoleAnnouncementText to = new RoleAnnouncementTexts.RoleAnnouncementText(from.getRole().id().getPath(), from.getRole().color());
        RoleAnnouncementTextAccessor accessor = (RoleAnnouncementTextAccessor) to;

        accessor.setRoleText(from.getName());
        accessor.setTitleText(from.getTeam());

        accessor.setWelcomeText(from.getWelcome());
        accessor.setGoalText(from.getGoal());

        accessor.setWinText(from.getWinText());

        return to;
    }

}
