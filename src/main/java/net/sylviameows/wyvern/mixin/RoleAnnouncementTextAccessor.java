package net.sylviameows.wyvern.mixin;

import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Function;

@Mixin(RoleAnnouncementTexts.RoleAnnouncementText.class)
public interface RoleAnnouncementTextAccessor {
    @Mutable
    @Accessor
    void setRoleText(Text roleText);

    @Mutable
    @Accessor
    void setWinText(Text winText);

    @Mutable
    @Accessor
    void setGoalText(Function<Integer, Text> goalText);

    @Mutable
    @Accessor
    void setPremiseText(Function<Integer, Text> premiseText);

    @Mutable
    @Accessor
    void setWelcomeText(Text welcomeText);

    @Mutable
    @Accessor
    void setTitleText(Text titleText);
}
