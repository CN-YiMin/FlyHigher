# Fly Higher

A NeoForge mod that provides advanced air pressure adjustment features for Create Aeronautics.

This mod allows you to customize the air pressure multiplier and pressure values at each altitude node, giving you control over the altitude behavior of airships in Create Aeronautics.

## Features

- Configurable air pressure multiplier
- Per-node altitude and pressure value editing
- Graphical configuration UI (Cloth Config)
- Automatic datapack injection — no manual setup required
- **Works in every dimension**: dimensions without their own Sable `dimension_physics` entry get the same curve, with the 1.0 atm anchor placed at that dimension's sea level (`Level.getSeaLevel()`; for example Overworld 63 and Nether 32)
- **Altitude Sensor compatible**: the pressure readout (and any other Sable pressure consumer) uses the adjusted curve in every dimension; the height readout remains the sensor's true world Y

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
- **Dimension adaptation**: `config/flyhigher-common.toml` → `[dimensions] adaptOtherDimensions`

The mod automatically generates a datapack at `config/flyhigher_datapack/` for the Overworld and loads it into every world. Other dimensions are filled in on the server's datapack sync, so they need no extra files. Run `/reload` (or rejoin) to apply changes without re-entering the world.

`adaptOtherDimensions = false` disables the automatic fill and leaves non-Overworld dimensions on Sable's own defaults.

## Building

Place the runtime jars in `libs/` first (they are not committed):

- `sable-neoforge-1.21.1-2.0.6.jar`
- `cloth-config-15.0.140-neoforge.jar`

Then run:

```powershell
.\gradlew.bat build
```

## Changelog

### 1.1.0

- Apply the pressure curve to dimensions without their own Sable `dimension_physics` entry.
- Place the 1.0 atm anchor at each dimension's sea level (`Level.getSeaLevel()`).
- The Altitude Sensor's air-pressure readout now follows the same per-dimension curve; height remains true world Y.
- Add `adaptOtherDimensions` to the common config.

## License

MIT License

## Credits

Created by CN-YiMin
