package net.sylviameows.wyvern.api.instinct;

import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class InstinctResult {

    private final int color;
    private final List<Text> details = new ArrayList<>();
    private @Nullable Text nametag = null;

    public InstinctResult(int color) {
        this.color = color;
    }

    public InstinctResult addLine(Text text) {
        details.add(text);
        return this;
    }

    public InstinctResult setNametag(Text nametag) {
        this.nametag = nametag;
        return this;
    }

    public @Nullable Text nametag() {
        return this.nametag;
    }

    public List<Text> details() {
        return this.details;
    }

    public int color() {
        return color;
    }
}
