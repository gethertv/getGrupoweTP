package dev.gether.getgrouptp.listener;

import dev.gether.getgrouptp.core.GroupTeleport;
import dev.gether.getgrouptp.core.TeleportManager;
import dev.gether.getgrouptp.file.FileManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Optional;

public class PlayerInteractListener implements Listener {

    private final FileManager fileManager;
    private final TeleportManager teleportManager;
    public PlayerInteractListener(FileManager fileManager, TeleportManager teleportManager) {
        this.fileManager = fileManager;
        this.teleportManager = teleportManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Block clickedBlock = event.getClickedBlock();
        if(clickedBlock==null)
            return;

        Location location = clickedBlock.getLocation();

        Optional<Location> rtpOptional = fileManager.getConfig().getRtpLocations().stream().filter(loc -> loc.equals(location)).findFirst();
        if(rtpOptional.isPresent()) {
            teleportManager.randomTeleport(player, player.getWorld());
            return;
        }

        Optional<GroupTeleport> teleportOpt = fileManager.getConfig().getTeleports().stream().filter(groupTeleport -> {
            Optional<Location> first = groupTeleport.getButtons().stream().filter(loc -> loc.equals(location)).findFirst();
            return first.isPresent();
        }).findFirst();

        if(teleportOpt.isEmpty())
            return;

        GroupTeleport groupTeleport = teleportOpt.get();
        List<Player> playersInside = groupTeleport.getCuboid().getPlayersInside();
        if(playersInside.isEmpty())
            return;

        int membersAmount = groupTeleport.getMembersAmount();
        if(playersInside.size() < membersAmount) {
            teleportManager.notRequiredMeembers(player, membersAmount);
            return;
        }
        List<Player> players = playersInside.subList(0, membersAmount);
        teleportManager.teleportPlayersToSafeLocation(players, groupTeleport);


    }
}
