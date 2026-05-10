# Fly Higher

A NeoForge mod that generates custom air pressure configurations for Create Aeronautics.

This mod allows you to customize the air pressure multiplier and pressure values at each altitude node. The configuration is exported as a datapack that can be loaded by Create Aeronautics to adjust airship altitude behavior.

## Features

- Configurable air pressure multiplier
- Per-node altitude and pressure value editing
- Graphical configuration UI (Cloth Config)
- Datapack generation based on your settings

## Requirements

- **Minecraft**: 1.21.1
- **NeoForge**: 21.1+
- **Create Aeronautics** (Sable 1.1+)
- **Cloth Config**: 15.0+ (for the config UI)

## Installation

1. Download the latest JAR file from [Releases](https://github.com/CN-YiMin/FlyHigher/releases)
2. Place it in your `mods` folder along with the required dependencies
3. Launch Minecraft with NeoForge

## Configuration

You can configure the mod through:
- **In-game**: Mod Menu → Fly Higher → Configure
- **Config File**: `config/flyhigher.json`

## ⚠️ Important — Manual Setup Required

After editing your configuration, the mod generates a datapack file at:

```
.minecraft/config/flyhigher_datapack/
```

For your settings to take effect in-game, you must:

1. Locate the generated folder at `.minecraft/config/flyhigher_datapack/`
2. Package the folder contents into a `.zip` file (or copy the folder directly)
3. Place it into your world's `datapacks/` folder:
   ```
   saves/<your_world>/datapacks/
   ```
4. Run `/reload` in-game, or re-enter the world

**Configuration changes will NOT take effect automatically** — the datapack must be manually installed into each world where you want to use it.

## License

MIT License

## Credits

Created by CN-YiMin
