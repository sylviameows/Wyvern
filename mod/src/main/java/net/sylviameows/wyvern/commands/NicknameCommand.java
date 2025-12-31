package net.sylviameows.wyvern.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.sylviameows.wyvern.components.NicknameComponent;
import org.jetbrains.annotations.NotNull;

public class NicknameCommand {

    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("wyvern:nickname")
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(context -> setNickname(
                                context.getArgument("player", EntitySelector.class).getPlayer(context.getSource()),
                                null,
                                context.getSource()
                        ))
                        .then(CommandManager.argument("nickname", StringArgumentType.greedyString())
                                .executes(context -> setNickname(
                                        context.getArgument("player", EntitySelector.class).getPlayer(context.getSource()),
                                        context.getArgument("nickname", String.class),
                                        context.getSource()
                                )))
                )
                .executes(context -> unfinishedCommand(context.getSource())));
    }

    private static int unfinishedCommand(ServerCommandSource source) {
        source.sendMessage(Text.translatable("command.wyvern.incomplete").withColor(0xFFAAAA));
        return 0;
    }

    private static int setNickname(PlayerEntity player, String nickname, ServerCommandSource source) {
        boolean other = source.getPlayer() == null || !source.getPlayer().equals(player);

        if (other && !source.hasPermissionLevel(2)) {
            source.sendMessage(Text.translatable("command.wyvern.permission"));
            return 0;
        }

        Text text = setNickname(player, nickname);

        if (other) {
            source.sendMessage(Text.translatable("command.wyvern.nickname.other", player.getName().copy().withColor(0xAAAAFF), text.copy().withColor(0xAAAAFF)));
        }

        return 1;
    }

    public static Text setNickname(PlayerEntity player, String nickname) {
        NicknameComponent component = NicknameComponent.KEY.get(player);
        component.setNickname(nickname);

        player.sendMessage(Text.translatable("command.wyvern.nickname.update", component.get().copy().withColor(0xAAAAFF)));

        if (player instanceof ServerPlayerEntity serverPlayer) {
            if (player.getServer() != null) {
                player.getServer().getPlayerManager().sendToAll(
                        new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, serverPlayer)
                );
            }
        }

        return component.get();
    }

}
