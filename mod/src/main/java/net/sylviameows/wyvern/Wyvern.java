package net.sylviameows.wyvern;

import dev.doctor4t.wathe.api.WatheGameModes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.game.GameResults;
import net.sylviameows.wyvern.payloads.BoardPayload;
import net.sylviameows.wyvern.registry.ResultRegistry;
import net.sylviameows.wyvern.api.WyvernAPI;
import net.sylviameows.wyvern.registry.RoleRegistry;
import net.sylviameows.wyvern.registry.TaskRegistry;
import net.sylviameows.wyvern.commands.NicknameCommand;
import net.sylviameows.wyvern.payloads.PurchasePayload;
import net.sylviameows.wyvern.game.roles.CivilianRole;
import net.sylviameows.wyvern.game.roles.KillerRole;
import net.sylviameows.wyvern.game.roles.VigilanteRole;
import net.sylviameows.wyvern.game.task.DrinkTask;
import net.sylviameows.wyvern.game.task.EatTask;
import net.sylviameows.wyvern.game.task.OutsideTask;
import net.sylviameows.wyvern.game.task.SleepTask;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Wyvern implements ModInitializer, WyvernAPI {

    public static final String MOD_ID = "wyvern";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static @NotNull Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }

    private final TaskRegistry taskRegistry = new TaskRegistry();
    private final RoleRegistry roleRegistry = new RoleRegistry();
    private final ResultRegistry resultRegistry = new ResultRegistry();

    @Override
    public void onInitialize() {
        LOGGER.info("Setting up Wyvern");
        Holder.setInstance(this);

        WatheGameModes.registerGameMode(WyvernGamemode.IDENTIFIER, new WyvernGamemode());

        // payloads
        PayloadTypeRegistry.playS2C().register(BoardPayload.ID, BoardPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(PurchasePayload.ID, PurchasePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(PurchasePayload.ID, new PurchasePayload.Receiver());

        // commands
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            NicknameCommand.register(dispatcher);
        }));

        // replicate default Wathe mode.
        RoleRegistry roles = getRoleRegistry();
        roles.register(new KillerRole());
        roles.register(new VigilanteRole());
        roles.register(new CivilianRole());
//        roles.register(new JesterRole());

        TaskRegistry tasks = getTaskRegistry();
        tasks.register(DrinkTask.IDENTIFIER, DrinkTask::new);
        tasks.register(EatTask.IDENTIFIER, EatTask::new);
        tasks.register(OutsideTask.IDENTIFIER, OutsideTask::fromNbt, OutsideTask::new);
        tasks.register(SleepTask.IDENTIFIER, SleepTask::fromNbt, SleepTask::new);

        ResultRegistry results = getResultRegistry();
        results.register(GameResults.killerByEliminations);
        results.register(GameResults.passengerByEliminations);
        results.register(GameResults.passengerByTime);

    }

    // api methods

    public RoleRegistry getRoleRegistry() {
        return roleRegistry;
    }

    public TaskRegistry getTaskRegistry() {
        return taskRegistry;
    }

    public ResultRegistry getResultRegistry() {
        return resultRegistry;
    }

}
