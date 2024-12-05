Sandwich Plugin
===============

Introduction
------------

The **Sandwich** plugin allows Minecraft server administrators to link multiple worlds vertically. Players can seamlessly transition between worlds by moving beyond the vertical boundaries of a world. This creates a layered world experience, stacking different dimensions or environments on top of or below each other.

Originally based off the MIT Licensed MC 1.13 plugin:
[Downloads](https://github.com/jonthesquirrel/Sandwich/releases)
[SpigotMC](https://www.spigotmc.org/resources/sandwich.59401)

Features
--------

-   **Per-World Height Configuration**: Define individual top and bottom Y-coordinate limits for each world, accommodating worlds with different heights (e.g., 128 blocks, 256 blocks).
-   **Per-World Padding**: Customize the padding for each world to prevent immediate re-triggering of teleportation upon crossing the boundary.
-   **Flexible World Linking**: Specify top and bottom links for worlds to control where players are teleported when they move beyond a world's vertical boundaries.
-   **Automatic Height Adjustment**: The plugin calculates the correct Y-coordinate in the target world, accounting for differences in world heights.
-   **Supports Minecraft 1.21.3 and PaperMC**: Updated to be compatible with the latest versions of Minecraft and PaperMC.

Installation
------------

1.  **Download the Plugin**: Obtain the latest release of the Sandwich plugin from the [Releases](https://github.com/theblobinc/Sandwich/releases/) page.

2.  **Place in Plugins Folder**: Copy the `Sandwich.jar` file into your server's `plugins` directory.

3.  **Start the Server**: Launch your Minecraft server. The plugin will generate a default `config.yml` file in the `plugins/Sandwich` directory.

4.  **Configure the Plugin**:

    -   Stop the server.
    -   Edit the `config.yml` file to suit your world's configurations.
    -   Save the file.
5.  **Restart the Server**: Start the server again to load the new configurations.

Configuration
-------------

### `config.yml`

The `config.yml` file is where you configure the behavior of the Sandwich plugin. Below is an explanation of the configuration options and how to set them up.

```
# Default padding value used if a world doesn't specify its own padding
defaultPadding: 10

# Worlds configuration
worlds:
  world:
    topLink: skylands
    worldTop: 319
    worldBottom: -64
    worldPadding: 10
  skylands:
    topLink: world_the_end
    bottomLink: world
    worldTop: 255
    worldBottom: 0
    worldPadding: 5
  world_the_end:
    bottomLink: skylands
    worldTop: 255
    worldBottom: 0
    worldPadding: 10
```

#### Configuration Options

-   **`defaultPadding`**: *(Integer)* The default padding value applied to worlds that do not specify their own `worldPadding`. Padding prevents players from immediately re-triggering the teleport event after being teleported.

-   **`worlds`**: *(Section)* Contains the configuration for each individual world.

    -   **`[world name]`**: The name of the world as it appears in your server's `worlds` directory.

        -   **`topLink`**: *(String, Optional)* The name of the world that the player should be teleported to when they move above `worldTop + worldPadding`.

        -   **`bottomLink`**: *(String, Optional)* The name of the world that the player should be teleported to when they move below `worldBottom - worldPadding`.

        -   **`worldTop`**: *(Integer)* The highest Y-coordinate of the world. Typically `319` for worlds generated in Minecraft 1.18 and later.

        -   **`worldBottom`**: *(Integer)* The lowest Y-coordinate of the world. Typically `-64` for worlds generated in Minecraft 1.18 and later.

        -   **`worldPadding`**: *(Integer, Optional)* The padding value specific to this world. Overrides `defaultPadding` if specified.

#### Example Configuration

In the example configuration above:

-   **`world`**:

    -   When a player moves above `Y = 319 + 10`, they are teleported to the `skylands` world.
    -   The world's vertical boundaries are from `Y = -64` to `Y = 319` with a padding of `10`.
-   **`skylands`**:

    -   When a player moves above `Y = 255 + 5`, they are teleported to `world_the_end`.
    -   When a player moves below `Y = 0 - 5`, they are teleported to `world`.
    -   The world's vertical boundaries are from `Y = 0` to `Y = 255` with a padding of `5`.
-   **`world_the_end`**:

    -   When a player moves below `Y = 0 - 10`, they are teleported to `skylands`.
    -   The world's vertical boundaries are from `Y = 0` to `Y = 255` with a padding of `10`.

### How It Works

The plugin listens to the `PlayerMoveEvent` and checks if a player moves beyond the defined vertical boundaries of the world they are currently in. If they do, and a link is defined in the configuration, the plugin teleports the player to the corresponding world at a calculated Y-coordinate that maintains their relative position.

#### Teleportation Logic

-   **Going Up**:

    -   If a player moves above `worldTop + worldPadding`, and a `topLink` is defined, they are teleported to the `topLink` world.
    -   The target Y-coordinate is calculated to place the player just above the `worldBottom` of the target world, adjusted by any padding.
-   **Going Down**:

    -   If a player moves below `worldBottom - worldPadding`, and a `bottomLink` is defined, they are teleported to the `bottomLink` world.
    -   The target Y-coordinate is calculated to place the player just below the `worldTop` of the target world, adjusted by any padding.
-   **Height Adjustment**:

    -   The plugin accounts for differences in world heights to ensure smooth transitions between worlds of different sizes.

Usage
-----

Once the plugin is installed and configured, it requires no further interaction. Players will automatically be teleported between linked worlds when they move beyond the vertical boundaries defined in the configuration.

Permissions
-----------

Currently, the Sandwich plugin does not implement any commands or permissions. All configurations are handled through the `config.yml` file.

Commands
--------

The plugin does not provide any commands at this time.

Changelog
---------

### Recent Changes

-   **Per-World Configurations**: Implemented the ability to set individual `worldTop`, `worldBottom`, and `worldPadding` values for each world.
-   **Configuration File Refactoring**: The `config.yml` file structure has been updated to accommodate per-world settings.
-   **Dynamic World Height Support**: The plugin now correctly handles worlds with different heights, such as those limited to 128 blocks.
-   **Teleportation Logic Improvements**: Enhanced the calculation of target Y-coordinates during teleportation to account for differing world heights and paddings.
-   **Updated to Minecraft 1.21.3**: Ensured compatibility with PaperMC 1.21.3 and the latest Minecraft versions.

### Migration from Previous Versions

If you are updating from a previous version of the Sandwich plugin, please note the following:

-   **Configuration Changes**: The `config.yml` structure has changed significantly. You will need to update your configuration file to match the new format. Be sure to back up your existing `config.yml` before updating.
-   **Compatibility**: The updated plugin is compatible with PaperMC 1.21.3 and above.

License
-------

This project is licensed under the MIT License - see the [LICENSE](https://github.com/theblobinc/Sandwich/blob/main/LICENSE) file for details.

Support
-------

If you encounter any issues or have questions about using the Sandwich plugin, please open an issue on the [GitHub Issues]([#](https://github.com/theblobinc/Sandwich/issues)) page.
