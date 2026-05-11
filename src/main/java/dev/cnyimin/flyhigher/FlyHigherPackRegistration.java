package dev.cnyimin.flyhigher;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.nio.file.Files;
import java.nio.file.Path;

@EventBusSubscriber(modid = CustomPressureMod.MODID)
public final class FlyHigherPackRegistration {
    private FlyHigherPackRegistration() {}

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.SERVER_DATA) {
            return;
        }

        Path packRoot = FlyHigherConfig.DATAPACK_ROOT;
        if (!Files.exists(packRoot.resolve("pack.mcmeta"))) {
            FlyHigherConfig.generateDatapack();
        }

        event.addRepositorySource(consumer -> {
            PackLocationInfo location = new PackLocationInfo(
                    "flyhigher_generated",
                    Component.literal("Fly Higher Generated"),
                    PackSource.BUILT_IN,
                    java.util.Optional.empty()
            );

            PackSelectionConfig selection = new PackSelectionConfig(
                    true,
                    Pack.Position.TOP,
                    false
            );

            Pack pack = Pack.readMetaAndCreate(
                    location,
                    new PathPackResources.PathResourcesSupplier(packRoot) {
                        @Override
                        public PackResources openPrimary(PackLocationInfo info) {
                            return new PathPackResources(info, packRoot);
                        }
                    },
                    PackType.SERVER_DATA,
                    selection
            );

            if (pack != null) {
                consumer.accept(pack);
            }
        });
    }
}
