package net.sylviameows.wyvern.api.task;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public interface TaskType {

    default Text text(Task task) {
        Identifier id = task.id();

        Text description = Text.translatable("%s.task.%s".formatted(id.getNamespace(), id.getPath()));
        if (alias() == null) {
            return description;
        }

        return Text.translatable("wyvern.tasks.%s".formatted(alias()), description);
    }

    String alias();

}
