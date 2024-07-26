package dev.gether.getgrouptp.file.config;

import dev.gether.getconfig.GetConfig;
import dev.gether.getgrouptp.core.GroupTeleport;
import dev.gether.getgrouptp.core.model.TeleportData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

@Getter
@Setter
public class Config extends GetConfig {

    private String usageCommand  = "&7Usage: #5eff96{usage}";
    private String noPermission  = "&cNo permission! #5eff96{permission}";
    private String usageFormat = "&6* &e{scheme}";

    private int cooldown = 5;
    private String cooldownMessage = "&cYou must wait {sec} seconds!";
    private String notEnoughMembersMessage = "&cCannot teleport. Required {members-size} member(s)";
    private String cannotUseRandomTP = "&cYou cannot use /rtp in this world";
    private String safeLocationNotFound = "&cFailed to find a safe location";

    private Map<String, TeleportData> teleportData = new HashMap<>(Map.of(
            "world", new TeleportData(500, 1000),
            "world_the_end", new TeleportData(100, 300)
    ));

    private List<Material> disabledMaterials = new ArrayList<>(List.of(
            Material.LAVA,
            Material.WATER
    ));

    private Set<Location> rtpLocations = new HashSet<>();
    private Set<GroupTeleport> teleports = new HashSet<>();
}