package net.sylviameows.wyvern.api.mood;

import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.WyvernConstants;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.WyvernAPI;
import net.sylviameows.wyvern.api.task.Task;
import net.sylviameows.wyvern.api.task.TaskType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MoodHandler {

    private final Role role;

    protected @Nullable Task task = null;
    protected final Map<Identifier, Integer> taskWeights = new HashMap<>();
    protected final TaskType taskType;

    protected int taskTimer;

    protected MoodHandler(Role role, TaskType taskType) {
        this.role = role;
        this.taskTimer = WyvernConstants.FIRST_TASK_COOLDOWN;
        this.taskType = taskType;
    }

    public static MoodHandler fromNbt(NbtCompound nbt) {
        Role role = WyvernAPI.roles().get(Identifier.of(nbt.getString("role")));
        MoodHandler handler = role.getMoodHandler();

        handler.taskTimer = nbt.getInt("timer");
        NbtCompound taskTag = nbt.getCompound("task");
        if (!taskTag.isEmpty()) {
            Task task = WyvernAPI.tasks().load(taskTag);
            handler.assignTask(task);
        }

        return handler;
    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putString("role", role.id().toString());
        nbt.putInt("timer", taskTimer);
        if (task != null) {
            nbt.put("task", task.toNbt());
        }
    }

    /**
     * Updates handler, returns true if successful.
     * @param nbt the nbt to update.
     * @return true if updated, false if nbt doesn't match.
     */
    public boolean update(NbtCompound nbt) {
        if (nbt.getString("role").equals(role.id().toString())) {
            this.taskTimer = nbt.getInt("timer");
            NbtCompound task = nbt.getCompound("task");
            if (!task.isEmpty()) {
                this.task = WyvernAPI.tasks().load(task);
            } else {
                this.task = null;
            }
            return true;
        }

        return false;
    }

    /**
     * Updates a player's mood every tick.
     * @param component the mood component.
     * @param player the player.
     * @return if the server needs to sync to client.
     */
    public boolean tick(PlayerMoodComponent component, PlayerEntity player) {
        boolean sync = false;

        if (!hasTask()) taskTimer--;
        else if (task != null) task.tick(player);

        if (isServerSide(player)) {
            if (taskTimer <= 0) {
                assignTask(player);
                sync = true;
            }

            if (task != null && task.isFulfilled(player)) {
                completeTask(component, player);
                sync = true;
            }
        }

        return sync;
    }

    private boolean isServerSide(PlayerEntity player) {
        return player instanceof ServerPlayerEntity;
    }

    public void completeTask(PlayerMoodComponent component, PlayerEntity player) {
        this.task = null;
    }

    /**
     * Assign a random task, weighted to prefer tasks not received before.
     */
    public void assignTask(PlayerEntity player) {
        Map<Identifier, Float> map = new HashMap<>();
        float total = 0f;

        for (Identifier id : WyvernAPI.tasks().available(role)) {
            float weight = 1f / taskWeights.getOrDefault(id, 1);
            map.put(id, weight);
            total += weight;
        }

        float random = player.getRandom().nextFloat() * total;
        for (var entry : map.entrySet()) {
            random -= entry.getValue();
            if (random <= 0) {
                taskTimer = WyvernConstants.MIN_TASK_COOLDOWN + (int) ((WyvernConstants.MAX_TASK_COOLDOWN - WyvernConstants.MIN_TASK_COOLDOWN) * player.getRandom().nextFloat());
                assignTask(WyvernAPI.tasks().get(entry.getKey()));
                break;
            }
        }

        if (this.task != null) {
            taskWeights.putIfAbsent(this.task.id(), 1);
            taskWeights.put(this.task.id(), taskWeights.get(this.task.id()) + 1);
        }
    }

    /**
     * Assign a specific task. Does not impact weights.
     * @param task the task to assign.
     */
    public void assignTask(Task task) {
        taskTimer = Math.max(taskTimer, 2);
        this.task = task;
    }

    public boolean hasTask() {
        return task != null;
    }

    public @Nullable Task getTask() {
        return task;
    }

    public TaskType type() {
        return taskType;
    }
}
