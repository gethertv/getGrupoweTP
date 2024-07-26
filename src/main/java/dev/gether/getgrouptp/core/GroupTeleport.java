package dev.gether.getgrouptp.core;

import dev.gether.getconfig.domain.Cuboid;
import lombok.*;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupTeleport {

    private String key;
    private int minRadius;
    private int maxRadius;
    private int membersAmount;
    private Set<Location> buttons = new HashSet<>();
    private Cuboid cuboid;
}
