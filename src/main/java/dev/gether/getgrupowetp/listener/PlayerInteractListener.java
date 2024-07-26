package dev.gether.getgrupowetp.listener;

import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getgrupowetp.core.GroupTeleport;
import dev.gether.getgrupowetp.core.TeleportManager;
import dev.gether.getgrupowetp.file.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Map;
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
