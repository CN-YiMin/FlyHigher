package dev.cnyimin.flyhigher;

import dev.ryanhcode.sable.physics.config.dimension_physics.DimensionPhysics;
import dev.ryanhcode.sable.physics.config.dimension_physics.DimensionPhysicsData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

/**
 * Applies Fly Higher's curve to dimensions that have no explicit Sable
 * {@code dimension_physics} entry.
 *
 * <p>This runs before Sable's own {@link OnDatapackSyncEvent} handler
 * ({@link EventPriority#HIGHEST}), so Sable's {@code compilePacket()} already
 * contains the adjusted entries and the client's Altitude Sensor / pressure
 * display receives them in the same sync.
 */
@EventBusSubscriber(modid = CustomPressureMod.MODID)
public final class FlyHigherDimensionSync {
    private FlyHigherDimensionSync() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (!FlyHigherForgeConfig.ADAPT_OTHER_DIMENSIONS.get()) {
            return;
        }

        MinecraftServer server = event.getPlayerList().getServer();
        if (server == null) {
            return;
        }

        int applied = 0;
        for (ServerLevel level : server.getAllLevels()) {
            DimensionPhysics current = DimensionPhysicsData.of(level);
            int priority = current.priority();

            // 0 = Sable's generated default; DIMENSION_PRIORITY = our previous
            // sync, rebuild it so config changes take effect. Anything else is
            // an explicit datapack override and must win.
            if (priority != 0 && priority != FlyHigherConfig.DIMENSION_PRIORITY) {
                continue;
            }

            DimensionPhysicsData.putPhysics(
                    level.dimension(),
                    FlyHigherConfig.createDimensionPhysics(level, current)
            );
            applied++;
        }

        FlyHigherConfig.logger().debug("Applied Fly Higher pressure curve to {} dimension(s)", applied);
    }
}
