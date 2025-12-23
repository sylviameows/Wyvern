package net.sylviameows.wyvern.api.registry;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.task.Task;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("UnusedReturnValue")
public interface TaskRegistrar extends Registrar<Task>{

    /**
     * Register a task into the Wyvern mod.
     * @param id the identifier for the task.
     * @param supplier a function to create the task using default values.
     * @return if the task was registered.
     */
    default boolean register(Identifier id, Supplier<Task> supplier) {
        return register(id, nbt -> supplier.get());
    }

    /**
     * Register a task into the Wyvern mod.
     * @param id the identifier for the task.
     * @param generator a function to create the task using nbt data.
     * @return if the task was registered.
     */
    default boolean register(Identifier id, Function<NbtCompound, Task> generator) {
        return register(id, generator, () -> generator.apply(new NbtCompound()));
    }

    /**
     * Register a task into the Wyvern mod.
     * @param id the identifier for the task.
     * @param generator a function to create the task using nbt data.
     * @param def a function to create the task using default values.
     * @return if the task was registered.
     */
    default boolean register(Identifier id, Function<NbtCompound, Task> generator, Supplier<Task> def) {
        return register(id, generator, def, role -> true);
    }

    /**
     * Register a task into the Wyvern mod.
     * @param id the identifier for the task.
     * @param generator a function to create the task using nbt data.
     * @param def a function to create the task using default values.
     * @param predicate a function to determine if a role can receive the task.
     * @return if the task was registered.
     */
    boolean register(Identifier id, Function<NbtCompound, Task> generator, Supplier<Task> def, Predicate<Role> predicate);

    /**
     * Loads a task from nbt data.
     * @param nbt the nbt information of the task.
     * @return the task that matches the given state.
     */
    Task load(NbtCompound nbt);

    Set<Identifier> available(Role role);

}
