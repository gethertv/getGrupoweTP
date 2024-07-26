package dev.gether.getgrupowetp.core;

import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getgrupowetp.core.model.TeleportData;
import dev.gether.getgrupowetp.file.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class TeleportManager {

    private final FileManager fileManager;
    private final Random random = new Random();
    private Map<UUID, Long> cooldown = new HashMap<>();
    private Map<UUID, Long> cooldownMessage = new HashMap<>();

    public TeleportManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void teleportPlayersToSafeLocation(List<Player> players, GroupTeleport groupTeleport) {
        World world = Bukkit.getWorld(groupTeleport.getCuboid().getWorldName());
        if (world == null) {
            throw new IllegalStateException("World '" + groupTeleport.getCuboid().getWorldName() + "' does not exist.");
        }
        Location spawnLocation = world.getSpawnLocation();
        Location randomLocation = getRandomSafeLocation(world, spawnLocation, groupTeleport.getMinRadius(), groupTeleport.getMaxRadius());

        if (randomLocation != null) {
            for (Player player : players) {
                player.teleport(randomLocation);
            }
        } else {
            for (Player player : players) {
                MessageUtil.sendMessage(player, fileManager.getConfig().getNotFoundSafeLoc());
            }
        }
    }

    public void randomTeleport(Player player, World world) {
        if (world == null) {
            throw new IllegalStateException("World does not exist.");
        }
        Long ctime = cooldown.get(player.getUniqueId());
        long now = System.currentTimeMillis();
        if (ctime != null) {
            int sec = (int) ((ctime - now) / 1000);
            if (sec > 0) {
                String cooldownMessage = fileManager.getConfig().getCooldownMessage();
                MessageUtil.sendMessage(player, cooldownMessage.replace("{sec}", String.valueOf(sec + 1)));
                return;
            }
        }
        cooldown.put(player.getUniqueId(), now + fileManager.getConfig().getCooldown());
        TeleportData teleportData = fileManager.getConfig().getTeleportData().get(world.getName());
        if (teleportData == null) {
            MessageUtil.sendMessage(player, fileManager.getConfig().getCannotUseRandomTP());
            return;
        }
        Location spawnLocation = world.getSpawnLocation();
        Location randomLocation = getRandomSafeLocation(world, spawnLocation, teleportData.getMinRadius(), teleportData.getMaxRadius());

        if (randomLocation != null) {
            player.teleport(randomLocation);
        } else {
            MessageUtil.sendMessage(player, fileManager.getConfig().getNotFoundSafeLoc());
        }
    }

    private Location getRandomSafeLocation(World world, Location spawnLocation, int minRadius, int maxRadius) {
        final int attempts = 5;

        for (int i = 0; i < attempts; i++) {
            int x = getRandomCoordinate(minRadius, maxRadius);
            int z = getRandomCoordinate(minRadius, maxRadius);

            Location randomLocation = new Location(world, spawnLocation.getX() + x, 0, spawnLocation.getZ() + z);
            randomLocation.setY(world.getHighestBlockYAt(randomLocation) + 1.0);

            if (isSafeLocation(randomLocation)) {
                return randomLocation;
            }
        }
        return null;
    }

    private int getRandomCoordinate(int minRadius, int maxRadius) {
        int coordinate = random.nextInt(maxRadius - minRadius + 1) + minRadius;
        return random.nextBoolean() ? coordinate : -coordinate;
    }

    private boolean isSafeLocation(Location location) {
        Material blockType = location.getBlock().getType();
        Material belowBlockType = location.clone().subtract(0, 1, 0).getBlock().getType();
        return !fileManager.getConfig().getDisableMaterial().contains(blockType) &&
                !fileManager.getConfig().getDisableMaterial().contains(belowBlockType);
    }

    public void notRequiredMeembers(Player player, int membersAmount) {
        Long ctime = cooldownMessage.get(player.getUniqueId());
        long now = System.currentTimeMillis();
        if (ctime != null && ctime > now) {
            return;
        }
        cooldownMessage.put(player.getUniqueId(), now + 500L);
        String notRequiredMembers = fileManager.getConfig().getNotRequiredMembers();
        MessageUtil.sendMessage(player, notRequiredMembers.replace("{members-size}", String.valueOf(membersAmount)));
        return;
    }
}
