package net.sylviameows.wyvern.registry;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.registry.TaskRegistrar;
import net.sylviameows.wyvern.api.role.Role;
import net.sylviameows.wyvern.api.task.Task;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class TaskRegistry implements TaskRegistrar {

    private final Map<Identifier, Entry> tasks = new HashMap<>();

    public Task get(Identifier id) {
        return tasks.get(id).def.get();
    }

    public Task load(NbtCompound nbt) {
        String unparsedId = nbt.getString("id");
        Identifier id = Identifier.of(unparsedId);

        return tasks.get(id).generator.apply(nbt);
    }

    public Set<Identifier> available(Role role) {
        Set<Identifier> available = new HashSet<>();
        tasks.forEach((identifier, entry) -> {
            if (entry.predicate.test(role)) {
                available.add(identifier);
            }
        });
        return available;
    }

    @Override
    public boolean register(Identifier id, Function<NbtCompound, Task> generator, Supplier<Task> def, Predicate<Role> predicate) {
        tasks.put(id, new Entry(generator, def, predicate));
        return true;
    }

    @Override
    public boolean unregister(Identifier id) {
        Entry removed = tasks.remove(id);
        return removed != null;
    }


    @ApiStatus.Internal
    private record Entry(Function<NbtCompound, Task> generator, Supplier<Task> def, Predicate<Role> predicate) {}

}
