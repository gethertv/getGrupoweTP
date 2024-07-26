package dev.gether.getgrupowetp.cmd;

import dev.gether.getconfig.domain.Cuboid;
import dev.gether.getconfig.selector.SelectorAddon;
import dev.gether.getconfig.selector.SelectorPlayer;
import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getgrupowetp.core.GroupTeleport;
import dev.gether.getgrupowetp.file.FileManager;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;

@Command(name = "grupowetp", aliases = "gtp")
@Permission("grupowetp.admin")
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
        MessageUtil.sendMessage(commandSender, "&aPomyslnie przeladowano config!");
    }


    @Execute(name = "create")
    private void createGroupTP(@Context Player player, @Arg("nazwa") String name, @Arg("ilosc") int amount) {
        Optional<SelectorPlayer> selectorPlayerByUUID = selectorAddon.getSelectorManager().findSelectorPlayerByUUID(player.getUniqueId());
        if(selectorPlayerByUUID.isEmpty()) {
            MessageUtil.sendMessage(player, "&cMusisz zaznaczyc obie lokalizacje!");
            return;
        }
        SelectorPlayer selectorPlayer = selectorPlayerByUUID.get();
        fileManager.getConfig().getTeleports().add(GroupTeleport.builder()
                        .key(name)
                        .minRadius(500)
                        .maxRadius(1000)
                        .buttons(new HashSet<>())
                        .cuboid(new Cuboid(selectorPlayer.getFirstLocation(), selectorPlayer.getSecondLocation()))
                        .membersAmount(amount)
                .build());
        fileManager.getConfig().save();

        MessageUtil.sendMessage(player, "&aPomyslnie stworzono grupowe TP");

    }

    @Execute(name = "setbutton")
    private void createGroupTP(@Context Player player, @Arg("nazwa") String key) {
        Block targetBlock = player.getTargetBlockExact(5);
        if(targetBlock==null) {
            MessageUtil.sendMessage(player, "&cMusisz patrzec sie na blok!");
            return;
        }
        Optional<GroupTeleport> first = fileManager.getConfig().getTeleports().stream().filter(groupTeleport -> groupTeleport.getKey().equalsIgnoreCase(key)).findFirst();
        if(first.isEmpty()) {
            MessageUtil.sendMessage(player, "&cTeleport o danym ID nie istnieje!");
            return;
        }
        GroupTeleport groupTeleport = first.get();
        groupTeleport.getButtons().add(targetBlock.getLocation());
        fileManager.getConfig().save();

        MessageUtil.sendMessage(player, "&aPomyslnie dodano przycisk!");

    }

    @Execute(name = "item")
    public void getItem(@Context Player player) {
        player.getInventory().addItem(selectorAddon.getSelectorItem().clone());
        MessageUtil.sendMessage(player, "&aPomyslnie nadano przedmiot!");
    }
}
