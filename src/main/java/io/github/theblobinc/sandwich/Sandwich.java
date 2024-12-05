package io.github.theblobinc.sandwich;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Sandwich extends JavaPlugin implements Listener {

    private FileConfiguration config;
    private Map<String, String> topLinks;
    private Map<String, String> bottomLinks;
    private Map<String, Integer> worldTops;
    private Map<String, Integer> worldBottoms;
    private Map<String, Integer> worldPaddings;
    private Logger log;

    @Override
    public void onEnable() {
        log = getLogger();
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        config = this.getConfig();

        topLinks = new HashMap<>();
        bottomLinks = new HashMap<>();
        worldTops = new HashMap<>();
        worldBottoms = new HashMap<>();
        worldPaddings = new HashMap<>();

        int defaultPadding = config.getInt("defaultPadding", 10);

        if (config.getConfigurationSection("worlds") == null) {
            config.createSection("worlds");
        }
        Set<String> worldKeys = config.getConfigurationSection("worlds").getKeys(false);
        if (worldKeys != null) {
            for (String worldString : worldKeys) {
                String path = "worlds." + worldString;
                String topLink = config.getString(path + ".topLink", null);
                String bottomLink = config.getString(path + ".bottomLink", null);
                int worldTop = config.getInt(path + ".worldTop", 319);
                int worldBottom = config.getInt(path + ".worldBottom", -64);
                int worldPadding = config.getInt(path + ".worldPadding", defaultPadding);

                if (topLink != null) {
                    topLinks.put(worldString, topLink);
                }
                if (bottomLink != null) {
                    bottomLinks.put(worldString, bottomLink);
                }

                worldTops.put(worldString, worldTop);
                worldBottoms.put(worldString, worldBottom);
                worldPaddings.put(worldString, worldPadding);
            }
        }
        log.info("Loaded world configurations:");
        for (String worldName : worldKeys) {
            log.info("World: " + worldName);
            log.info("  Top Link: " + topLinks.get(worldName));
            log.info("  Bottom Link: " + bottomLinks.get(worldName));
            log.info("  World Top Y: " + worldTops.get(worldName));
            log.info("  World Bottom Y: " + worldBottoms.get(worldName));
            log.info("  World Padding: " + worldPaddings.get(worldName));
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Check if the player has moved to a new block vertically
        if (event.getFrom().getBlockY() == event.getTo().getBlockY()) {
            return;
        }

        Location toLocation = event.getTo();
        World currentWorld = toLocation.getWorld();
        String currentWorldName = currentWorld.getName();

        // Get per-world configurations
        Integer worldTop = worldTops.get(currentWorldName);
        Integer worldBottom = worldBottoms.get(currentWorldName);
        Integer worldPadding = worldPaddings.get(currentWorldName);

        if (worldTop == null || worldBottom == null || worldPadding == null) {
            // World configuration is missing, ignore
            return;
        }

        int currentY = toLocation.getBlockY();
        if (currentY < worldBottom - worldPadding || currentY > worldTop + worldPadding) {
            World targetWorld = null;
            String targetWorldName = null;
            int targetY = 0;

            if (currentY < worldBottom - worldPadding) {
                // Going down
                if (bottomLinks.containsKey(currentWorldName)) {
                    targetWorldName = bottomLinks.get(currentWorldName);
                    targetWorld = Bukkit.getWorld(targetWorldName);

                    // Get target world's configurations
                    Integer targetWorldTop = worldTops.get(targetWorldName);
                    Integer targetWorldBottom = worldBottoms.get(targetWorldName);
                    Integer targetWorldPadding = worldPaddings.get(targetWorldName);

                    if (targetWorld != null && targetWorldTop != null && targetWorldBottom != null && targetWorldPadding != null) {
                        targetY = targetWorldTop + targetWorldPadding - (worldBottom - currentY);
                    } else {
                        return; // Missing target world configuration
                    }
                }
            } else {
                // Going up
                if (topLinks.containsKey(currentWorldName)) {
                    targetWorldName = topLinks.get(currentWorldName);
                    targetWorld = Bukkit.getWorld(targetWorldName);

                    // Get target world's configurations
                    Integer targetWorldTop = worldTops.get(targetWorldName);
                    Integer targetWorldBottom = worldBottoms.get(targetWorldName);
                    Integer targetWorldPadding = worldPaddings.get(targetWorldName);

                    if (targetWorld != null && targetWorldTop != null && targetWorldBottom != null && targetWorldPadding != null) {
                        targetY = targetWorldBottom - targetWorldPadding + (currentY - worldTop);
                    } else {
                        return; // Missing target world configuration
                    }
                }
            }

            if (targetWorld != null) {
                Player player = event.getPlayer();
                double x = toLocation.getX();
                double z = toLocation.getZ();
                float yaw = toLocation.getYaw();
                float pitch = toLocation.getPitch();
                Vector velocity = player.getVelocity();
                Location targetLocation = new Location(targetWorld, x, targetY, z, yaw, pitch);
                player.teleport(targetLocation);
                player.setVelocity(velocity);
            }
        }
    }
}
