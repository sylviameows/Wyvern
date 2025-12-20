package net.sylviameows.wyvern.api.role;

import net.minecraft.text.Text;

import java.util.function.Function;

public interface RoleAnnouncementTextAccessor {
    void setRoleText(Text roleText);

    void setWinText(Text winText);

    void setGoalText(Function<Integer, Text> goalText);

    void setPremiseText(Function<Integer, Text> premiseText);

    void setWelcomeText(Text welcomeText);

    void setTitleText(Text titleText);
}
