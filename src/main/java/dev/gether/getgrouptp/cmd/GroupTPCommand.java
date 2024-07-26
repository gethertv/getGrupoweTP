package dev.gether.getgrouptp.cmd;

import dev.gether.getconfig.domain.Cuboid;
import dev.gether.getconfig.selector.SelectorAddon;
import dev.gether.getconfig.selector.SelectorPlayer;
import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getgrouptp.core.GroupTeleport;
import dev.gether.getgrouptp.file.FileManager;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;

@Command(name = "grouptp", aliases = "gtp")
@Permission("grouptp.admin")
public class GroupTPCommand {

    private final SelectorAddon selectorAddon;
    private final FileManager fileManager;

    public GroupTPCommand(SelectorAddon selectorAddon, FileManager fileManager) {
        this.selectorAddon = selectorAddon;
        this.fileManager = fileManager;
    }

    @Execute(name = "reload")
    private void reload(@Context CommandSender commandSender) {
        fileManager.reload();
        MessageUtil.sendMessage(commandSender, "&aConfig successfully reloaded!");
    }

    private boolean isValidSelection(Player player) {
        Optional<SelectorPlayer> selectorPlayerOpt = selectorAddon.getSelectorManager().findSelectorPlayerByUUID(player.getUniqueId());
        return selectorPlayerOpt.isPresent()
                && selectorPlayerOpt.get().getFirstLocation() != null
                && selectorPlayerOpt.get().getSecondLocation() != null;
    }

    @Execute(name = "create")
    private void createGroupTP(@Context Player player, @Arg("name") String name, @Arg("amount") int amount) {
        if (!isValidSelection(player)) {
            MessageUtil.sendMessage(player, "&cYou must select both locations!");
            return;
        }
        Optional<SelectorPlayer> selectorPlayerOpt = selectorAddon.getSelectorManager().findSelectorPlayerByUUID(player.getUniqueId());
        if(selectorPlayerOpt.isEmpty()) return;

        Optional<GroupTeleport> first = fileManager.getConfig().getTeleports().stream().filter(groupTeleport -> groupTeleport.getKey().equalsIgnoreCase(name)).findFirst();
        if(first.isPresent()) {
            MessageUtil.sendMessage(player, "&cThis name is already in use!");
            return;
        }
        SelectorPlayer selectorPlayer = selectorPlayerOpt.get();
        GroupTeleport newTeleport = GroupTeleport.builder()
                .key(name)
                .minRadius(500)
                .maxRadius(1000)
                .buttons(new HashSet<>())
                .cuboid(new Cuboid(selectorPlayer.getFirstLocation(), selectorPlayer.getSecondLocation()))
                .membersAmount(amount)
                .build();

        fileManager.getConfig().getTeleports().add(newTeleport);
        fileManager.getConfig().save();

        MessageUtil.sendMessage(player, "&aGroup TP successfully created");
    }

    @Execute(name = "setbutton gtp")
    private void setButton(@Context Player player, @Arg("name") String key) {
        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock == null) {
            MessageUtil.sendMessage(player, "&cYou must be looking at a block!");
            return;
        }

        Optional<GroupTeleport> teleportOpt = fileManager.getConfig().getTeleports().stream()
                .filter(groupTeleport -> groupTeleport.getKey().equalsIgnoreCase(key))
                .findFirst();

        if (teleportOpt.isEmpty()) {
            MessageUtil.sendMessage(player, "&cTeleport with the given ID does not exist!");
            return;
        }

        GroupTeleport groupTeleport = teleportOpt.get();
        groupTeleport.getButtons().add(targetBlock.getLocation());
        fileManager.getConfig().save();

        MessageUtil.sendMessage(player, "&aButton successfully added!");
    }

    @Execute(name = "removebutton gtp")
    private void removeGTPButton(@Context Player player, @Arg("name") String key) {
        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock == null) {
            MessageUtil.sendMessage(player, "&cYou must be looking at a block!");
            return;
        }

        Optional<GroupTeleport> teleportOpt = fileManager.getConfig().getTeleports().stream()
                .filter(groupTeleport -> groupTeleport.getKey().equalsIgnoreCase(key))
                .findFirst();

        if (teleportOpt.isEmpty()) {
            MessageUtil.sendMessage(player, "&cTeleport with the given ID does not exist!");
            return;
        }

        GroupTeleport groupTeleport = teleportOpt.get();
        Location buttonLocation = targetBlock.getLocation();
        boolean removed = groupTeleport.getButtons().remove(buttonLocation);

        if (removed) {
            fileManager.getConfig().save();
            MessageUtil.sendMessage(player, "&aGTP button successfully removed!");
        } else {
            MessageUtil.sendMessage(player, "&cNo GTP button found at this location!");
        }
    }

    @Execute(name = "removebutton rtp")
    private void removeRTPButton(@Context Player player) {
        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock == null) {
            MessageUtil.sendMessage(player, "&cYou must be looking at a block!");
            return;
        }

        Location buttonLocation = targetBlock.getLocation();
        boolean removed = fileManager.getConfig().getRtpLocations().remove(buttonLocation);

        if (removed) {
            fileManager.getConfig().save();
            MessageUtil.sendMessage(player, "&aRTP button successfully removed!");
        } else {
            MessageUtil.sendMessage(player, "&cNo RTP button found at this location!");
        }
    }

    @Execute(name = "setbutton rtp")
    private void addRTPButton(@Context Player player) {
        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock == null) {
            MessageUtil.sendMessage(player, "&cYou must be looking at a block!");
            return;
        }

        fileManager.getConfig().getRtpLocations().add(targetBlock.getLocation());
        fileManager.getConfig().save();

        MessageUtil.sendMessage(player, "&aRTP button successfully added!");
    }

    @Execute(name = "item")
    public void getItem(@Context Player player) {
        player.getInventory().addItem(selectorAddon.getSelectorItem().clone());
        MessageUtil.sendMessage(player, "&aSelector item successfully given!");
    }
}