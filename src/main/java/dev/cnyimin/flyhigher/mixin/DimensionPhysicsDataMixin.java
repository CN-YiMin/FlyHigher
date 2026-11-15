package dev.cnyimin.flyhigher.mixin;

import dev.cnyimin.flyhigher.FlyHigherConfig;
import dev.ryanhcode.sable.physics.config.dimension_physics.DimensionPhysicsData;
import net.minecraft.world.level.Level;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Weather is evaluated when pressure is read, not when the dimension physics
 * entry is created. That makes rain/thunder transitions instant and avoids
 * any datapack reload or packet round-trip.
 */
@Mixin(value = DimensionPhysicsData.class, remap = false)
public abstract class DimensionPhysicsDataMixin {
    @Inject(method = "getAirPressure", at = @At("RETURN"), cancellable = true)
    private static void flyhigher$applyWeatherPressure(Level level, Vector3dc pos,
                                                       CallbackInfoReturnable<Double> cir) {
        if (!FlyHigherConfig.isWeatherPressureEnabled()) {
            return;
        }

        double multiplier = 1.0;
        if (level.isThundering()) {
            multiplier = FlyHigherConfig.getThunderPressureMultiplier();
        } else if (level.isRaining()) {
            multiplier = FlyHigherConfig.getRainPressureMultiplier();
        }

        if (multiplier < 1.0) {
            cir.setReturnValue(cir.getReturnValue() * multiplier);
        }
    }
}
