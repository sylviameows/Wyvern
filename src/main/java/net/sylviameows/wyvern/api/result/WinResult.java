package net.sylviameows.wyvern.api.result;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.Alignment;
import net.sylviameows.wyvern.api.role.Role;

public abstract class WinResult {

    protected final Identifier identifier;

    protected final Alignment alignment;
    protected final String reason;

    protected WinResult(Identifier identifier, Alignment alignment, String reason) {
        this.identifier = identifier;

        this.alignment = alignment;
        this.reason = reason;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public boolean isWinner(Role role, PlayerEntity player) {
        return role.alignment() == alignment;
    }

    public Text getDefaultTitle() {
        return Text.translatable("wyvern.alignment.%s.win".formatted(alignment.alias())).withColor(alignment.color());
    }

    public Text getReasonText() {
        return Text.translatable("%s.result.%s.%s".formatted(identifier.getNamespace(), alignment.alias(), reason));
    }

    /**
     * If the result requires special data (a player, or some other statistic) write it to the NBT.
     * You likely don't need to implement this method.
     * @param nbt the nbt to append the data to.
     */
    public void writeNbt(NbtCompound nbt) {

    }

    /**
     * If the result requires special data (a player, or some other statistic) read it from the NBT.
     * You likely don't need to implement this method.
     * @param nbt the nbt to read the data from.
     */
    public void readNbt(NbtCompound nbt) {

    }

}
