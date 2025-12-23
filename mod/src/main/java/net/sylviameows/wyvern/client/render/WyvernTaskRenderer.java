package net.sylviameows.wyvern.client.render;

import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.sylviameows.wyvern.api.task.Task;
import net.sylviameows.wyvern.api.task.TaskType;

public class WyvernTaskRenderer {
    public int index = 0;
    public float offset = -1f;
    public float alpha = 0.075f;
    public Text text = Text.empty();

    public boolean tick(Text text, float delta) {
        this.text = text;
        this.alpha = MathHelper.lerp(delta / 16, this.alpha, this.present() ? 1f : 0f);
        this.offset = MathHelper.lerp(delta / 32, this.offset, this.index);
        return this.alpha < 0.075f || (((int) (this.alpha * 255.0f) << 24) & -67108864) == 0;
    }

    public boolean present() {
        return this.text != null;
    }

    public boolean tick(Task task, TaskType type, float delta) {
        if (task != null) {
            return tick(type.text(task), delta);
        }
        return tick(null, delta);
    }
}
