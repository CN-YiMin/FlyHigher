package dev.cnyimin.flyhigher;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class FlyHigherConfigScreen {
    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.flyhigher.title"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory category = builder.getOrCreateCategory(Component.translatable("config.flyhigher.category.all"));

        // 全局倍率
        category.addEntry(entryBuilder.startDoubleField(
                        Component.translatable("config.flyhigher.height_multiplier"),
                        FlyHigherConfig.getPressureMultiplier())
                .setDefaultValue(1.0)
                .setMin(0.1)
                .setMax(5.0)
                .setTooltip(Component.translatable("config.flyhigher.height_multiplier.tooltip"))
                .setSaveConsumer(FlyHigherConfig::setPressureMultiplier)
                .build());

        // 节点编辑
        List<FlyHigherConfig.PressureNode> nodes = FlyHigherConfig.getNodes();
        List<FlyHigherConfig.PressureNode> editedNodes = new ArrayList<>();
        for (FlyHigherConfig.PressureNode node : nodes) {
            editedNodes.add(new FlyHigherConfig.PressureNode(node.altitude, node.basePressure));
        }

        for (int i = 0; i < editedNodes.size(); i++) {
            final int idx = i;
            FlyHigherConfig.PressureNode node = editedNodes.get(i);
            double scaledAltitude = FlyHigherConfig.getScaledAltitude(i);

            category.addEntry(entryBuilder.startDoubleField(
                            Component.translatable("config.flyhigher.node.altitude", i + 1, String.format("%.1f", scaledAltitude)),
                            node.altitude)
                    .setDefaultValue(node.altitude)
                    .setMin(-64)
                    .setMax(2048)
                    .setSaveConsumer(v -> editedNodes.get(idx).altitude = v)
                    .build());

            category.addEntry(entryBuilder.startDoubleField(
                            Component.translatable("config.flyhigher.node.pressure", i + 1),
                            node.basePressure)
                    .setDefaultValue(node.basePressure)
                    .setMin(0.0)
                    .setMax(10.0)
                    .setSaveConsumer(v -> editedNodes.get(idx).basePressure = v)
                    .build());
        }

        builder.setSavingRunnable(() -> FlyHigherConfig.setNodes(editedNodes));

        return builder.build();
    }
}
