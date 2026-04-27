package dev.cnyimin.flyhigher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FlyHigherConfig {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Paths.get("config", "flyhigher.json");

    private static ConfigData data = new ConfigData();

    public static class PressureNode {
        public double altitude;
        public double basePressure; // 基础气压值（不含倍率）

        public PressureNode(double altitude, double basePressure) {
            this.altitude = altitude;
            this.basePressure = basePressure;
        }
    }

    public static class ConfigData {
        public double pressureMultiplier = 1.0;
        public List<PressureNode> nodes = defaultNodes();
    }

    private static List<PressureNode> defaultNodes() {
        List<PressureNode> nodes = new ArrayList<>();
        nodes.add(new PressureNode(-64, 1.25));
        nodes.add(new PressureNode(63,  1.00));
        nodes.add(new PressureNode(720, 0.50));
        nodes.add(new PressureNode(1024, 0.27));
        nodes.add(new PressureNode(1364, 0.01));
        return nodes;
    }

    public static void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                data = GSON.fromJson(json, ConfigData.class);
                if (data.nodes == null || data.nodes.isEmpty()) data.nodes = defaultNodes();
                LOGGER.info("Fly Higher config loaded: multiplier={}, nodes={}", data.pressureMultiplier, data.nodes.size());
            } else {
                save();
                LOGGER.info("Fly Higher config created with default values");
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load Fly Higher config", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(data));
        } catch (IOException e) {
            LOGGER.error("Failed to save Fly Higher config", e);
        }
    }

    public static double getPressureMultiplier() { return data.pressureMultiplier; }
    public static void setPressureMultiplier(double value) {
        data.pressureMultiplier = value;
        save();
        generateDatapack();
    }

    public static List<PressureNode> getNodes() { return data.nodes; }
    public static void setNodes(List<PressureNode> nodes) {
        data.nodes = nodes;
        save();
        generateDatapack();
    }

    // 生成 datapack JSON 文件
    public static void generateDatapack() {
        try {
            Path datapackPath = Paths.get("config", "flyhigher_datapack", "data", "flyhigher", "dimension_physics");
            Files.createDirectories(datapackPath);

            // 构建 JSON
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"dimension\": \"minecraft:overworld\",\n");
            json.append("  \"priority\": 2000,\n");
            json.append("  \"base_pressure\": 1.0,\n");
            json.append("  \"pressure_function\": [\n");

            for (int i = 0; i < data.nodes.size(); i++) {
                PressureNode node = data.nodes.get(i);
                double scaledAlt = getScaledAltitude(i);
                double slope = 0.0;
                if (i < data.nodes.size() - 1) {
                    PressureNode next = data.nodes.get(i + 1);
                    double nextScaledAlt = getScaledAltitude(i + 1);
                    slope = (next.basePressure - node.basePressure) / (nextScaledAlt - scaledAlt);
                }

                json.append("    {\n");
                json.append(String.format("      \"altitude\": %.1f,\n", scaledAlt));
                json.append(String.format("      \"value\": %.2f,\n", node.basePressure));
                json.append(String.format("      \"slope\": %.6f\n", slope));
                json.append("    }");
                if (i < data.nodes.size() - 1) json.append(",");
                json.append("\n");
            }

            json.append("  ]\n");
            json.append("}\n");

            Files.writeString(datapackPath.resolve("overworld.json"), json.toString());
            LOGGER.info("Generated datapack at {}", datapackPath);
        } catch (IOException e) {
            LOGGER.error("Failed to generate datapack", e);
        }
    }

    // 获取气压为 1.0 的节点高度（锚点）
    private static double getAnchorAltitude() {
        for (PressureNode node : data.nodes) {
            if (Math.abs(node.basePressure - 1.0) < 0.001) {
                return node.altitude;
            }
        }
        return 63.0; // 默认海平面
    }

    // 获取某节点应用高度缩放后的实际高度
    public static double getScaledAltitude(int nodeIndex) {
        PressureNode node = data.nodes.get(nodeIndex);
        double anchor = getAnchorAltitude();
        double distance = node.altitude - anchor;
        return anchor + distance * data.pressureMultiplier;
    }

    // 获取某节点的最终气压（基础值不变）
    public static double getFinalPressure(int nodeIndex) {
        return data.nodes.get(nodeIndex).basePressure;
    }
}
