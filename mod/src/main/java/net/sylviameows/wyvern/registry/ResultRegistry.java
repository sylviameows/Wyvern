package net.sylviameows.wyvern.registry;

import net.minecraft.util.Identifier;
import net.sylviameows.wyvern.api.registry.ResultRegistrar;
import net.sylviameows.wyvern.api.result.WinResult;

import java.util.HashMap;
import java.util.Map;

public final class ResultRegistry implements ResultRegistrar {

    private final Map<Identifier, WinResult> results = new HashMap<>();

    public boolean register(WinResult status) {
        this.results.put(status.getIdentifier(), status);
        return true;
    }

    public WinResult get(Identifier identifier) {
        return this.results.get(identifier);
    }

    public boolean unregister(Identifier id) {
        return results.remove(id) != null;
    }

}
