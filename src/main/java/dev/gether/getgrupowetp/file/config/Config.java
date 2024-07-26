package dev.gether.getgrupowetp.file.config;

import dev.gether.getconfig.GetConfig;
import dev.gether.getconfig.domain.config.TitleMessage;
import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getgrupowetp.core.GroupTeleport;
import dev.gether.getgrupowetp.core.model.TeleportData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.*;

@Getter
@Setter
public class Config extends GetConfig {

    private int cooldown = 5;
    private String cooldownMessage = "&cMusisz odczekac {sec} sek!";
    private String notRequiredMembers = "&cNie mozna sie tp. Wymagane {members-size} osob/y";
    private String cannotUseRandomTP = "&cNie mozesz uzywac /rtp na tym swiecie";
    private String notFoundSafeLoc = "&cNie udalo znalezc bezpiecznej lokalizacji";
    private Map<String, TeleportData> teleportData = new HashMap<>(Map.of(
            "world", new TeleportData(500, 1000),
            "world_the_end", new TeleportData(100, 300)
    ));

    private List<Material> disableMaterial = new ArrayList<>(List.of(
            Material.LAVA,
            Material.WATER
    ));

    private Set<GroupTeleport> teleports = new HashSet<>();
}
