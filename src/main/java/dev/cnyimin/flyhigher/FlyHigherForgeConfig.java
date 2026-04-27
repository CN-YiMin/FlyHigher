package dev.cnyimin.flyhigher;

import net.neoforged.neoforge.common.ModConfigSpec;

public class FlyHigherForgeConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.DoubleValue HEIGHT_MULTIPLIER;

    static {
        BUILDER.comment("Fly Higher - Air Pressure Configuration");
        BUILDER.push("pressure");
        HEIGHT_MULTIPLIER = BUILDER
                .comment("Height scale multiplier. Scales altitude distances from the 1.0 atm anchor point.",
                         "1.0 = default, 2.0 = double height range, 0.5 = half height range")
                .defineInRange("heightMultiplier", 1.0, 0.1, 5.0);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
