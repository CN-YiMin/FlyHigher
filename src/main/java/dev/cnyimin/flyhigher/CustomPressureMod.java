package dev.cnyimin.flyhigher;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(CustomPressureMod.MODID)
public class CustomPressureMod {
    public static final String MODID = "flyhigher";

    public CustomPressureMod(IEventBus modEventBus, ModContainer modContainer) {
        FlyHigherConfig.load();
        FlyHigherConfig.generateDatapack(); // 生成 datapack

        // 注册 Forge 配置（让机械动力识别）
        modContainer.registerConfig(ModConfig.Type.COMMON, FlyHigherForgeConfig.SPEC);

        // 同步 Forge 配置到 JSON 并重新生成 datapack
        modEventBus.addListener((ModConfigEvent.Loading event) -> {
            if (event.getConfig().getModId().equals(MODID)) {
                syncForgeToJson();
            }
        });

        modEventBus.addListener((ModConfigEvent.Reloading event) -> {
            if (event.getConfig().getModId().equals(MODID)) {
                syncForgeToJson();
            }
        });

        // 注册 Cloth Config UI
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, screen) -> FlyHigherConfigScreen.createConfigScreen(screen));
    }

    private void syncForgeToJson() {
        double forgeValue = FlyHigherForgeConfig.HEIGHT_MULTIPLIER.get();
        FlyHigherConfig.setPressureMultiplier(forgeValue);
        FlyHigherConfig.generateDatapack(); // 重新生成
    }
}
