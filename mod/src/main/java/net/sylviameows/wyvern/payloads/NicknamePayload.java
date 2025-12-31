package net.sylviameows.wyvern.payloads;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.sylviameows.wyvern.Wyvern;
import net.sylviameows.wyvern.commands.NicknameCommand;

public record NicknamePayload(String nickname) implements CustomPayload {

    public static final CustomPayload.Id<NicknamePayload> ID = new CustomPayload.Id<>(Wyvern.id("nickname"));
    public static final PacketCodec<PacketByteBuf, NicknamePayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, NicknamePayload::nickname, NicknamePayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<NicknamePayload> {
        @Override
        public void receive(NicknamePayload payload, ServerPlayNetworking.Context context) {
            NicknameCommand.setNickname(context.player(), payload.nickname.isEmpty() ? null : payload.nickname);
        }
    }

}
