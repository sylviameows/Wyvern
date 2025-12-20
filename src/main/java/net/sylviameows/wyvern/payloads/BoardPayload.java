package net.sylviameows.wyvern.payloads;

import dev.doctor4t.trainmurdermystery.client.gui.RoundTextRenderer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.api.WyvernAPI;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.role.RoleAnnouncement;

/**
 * Equivalent of the {@link dev.doctor4t.trainmurdermystery.util.AnnounceWelcomePayload} from TMM, but for Wyvern. Using role ID's instead of registered "announcement texts"
 * @param role the player's role.
 * @param killers how many killers are aboard.
 */
public record BoardPayload(String role, int killers) implements CustomPayload {
    public static final Id<BoardPayload> ID = new Id<>(Wyvern.id("board"));
    public static final PacketCodec<PacketByteBuf, BoardPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, BoardPayload::role, PacketCodecs.INTEGER, BoardPayload::killers, BoardPayload::new);

    public BoardPayload(Identifier role, int killers) {
        this(role.toString(), killers);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public Role getRole() {
        Identifier identifier = Identifier.of(role);
        return WyvernAPI.roles().get(identifier);
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<BoardPayload> {
        @Override
        public void receive(BoardPayload payload, ClientPlayNetworking.Context context) {
            RoleAnnouncement announcement = payload.getRole().announcement();
            RoundTextRenderer.startWelcome(announcement, payload.killers(), 0);
        }
    }

}
