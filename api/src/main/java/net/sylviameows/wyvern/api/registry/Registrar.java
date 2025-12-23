package net.sylviameows.wyvern.api.registry;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface Registrar<T> {

    /**
     * Fetches the matching value for the given identifier.
     * @param id The registered values identifier. (e.g. wyvern:killer)
     * @return The matched value, or null if not found.
     */
    @Nullable T get(Identifier id);

    /**
     * Finds the matching value and removes it from the registry.
     * @param id The registered values identifier. (e.g. wyvern:killer)
     * @return If the value was removed from the registry.
     */
    boolean unregister(Identifier id);

}
