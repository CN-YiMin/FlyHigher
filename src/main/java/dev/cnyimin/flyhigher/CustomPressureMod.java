package dev.cnyimin.flyhigher;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(CustomPressureMod.MODID)
public class CustomPressureMod {
    public static final String MODID = "flyhigher";

    public CustomPressureMod(IEventBus modEventBus, ModContainer modContainer) {
        FlyHigherConfig.load();
        FlyHigherConfig.generateDatapack();

        modContainer.registerConfig(ModConfig.Type.COMMON, (IConfigSpec) FlyHigherForgeConfig.SPEC);
        modEventBus.addListener(this::onConfigLoaded);
        modEventBus.addListener(this::onConfigReloaded);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            registerConfigScreen(modContainer);
        }
    }

    private void onConfigLoaded(ModConfigEvent.Loading event) {
        syncForgeToJson(event);
    }

    private void onConfigReloaded(ModConfigEvent.Reloading event) {
        syncForgeToJson(event);
    }

    private void syncForgeToJson(ModConfigEvent event) {
        if (MODID.equals(event.getConfig().getModId())) {
            FlyHigherConfig.setPressureMultiplier(FlyHigherForgeConfig.HEIGHT_MULTIPLIER.get());
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void registerConfigScreen(ModContainer modContainer) {
        FlyHigherConfigScreen.register(modContainer);
    }
}
