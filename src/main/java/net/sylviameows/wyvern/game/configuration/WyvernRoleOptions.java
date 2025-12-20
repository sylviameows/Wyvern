package net.sylviameows.wyvern.game.configuration;

import net.sylviameows.wyvern.api.role.options.DynamicOptions;
import net.sylviameows.wyvern.api.role.options.RoleOptions;

import java.util.function.Function;

public final class WyvernRoleOptions implements RoleOptions {

    private final float odds;
    private final int count;

    public WyvernRoleOptions(float odds, int count) {
        this.odds = odds;
        this.count = count;
    }

    @Override
    public float odds() {
        return odds;
    }

    @Override
    public int count() {
        return count;
    }

    public RoleOptions update(Function<RoleOptions.Builder, RoleOptions> function) {
        Builder builder = new Builder(this);
        return function.apply(builder);
    }

    public record DynamicRoleOptions(DynamicOptions setting) implements RoleOptions {

        @Override
        public float odds() {
            return 1f;
        }

        @Override
        public int count() {
            return 0;
        }

    }

    static final public class Builder implements RoleOptions.Builder {

        private float odds = 1f;
        private int count = 1;

        public Builder() {

        }

        public Builder(RoleOptions settings) {
            this.odds = settings.odds();
            this.count = settings.count();
        }

        @Override
        public Builder odds(float odds) {
            this.odds = odds;
            return this;
        }

        @Override
        public Builder count(int count) {
            this.count = count;
            return this;
        }

        @Override
        public RoleOptions ignored() {
            return new WyvernRoleOptions(0f, -1);
        }

        @Override
        public RoleOptions build() {
            return new WyvernRoleOptions(odds, count);
        }

        @Override
        public RoleOptions dynamic(DynamicOptions setting) {
            return new DynamicRoleOptions(setting);
        }

    }

}
