package dev.gether.getgrupowetp.cmd;


import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getgrupowetp.core.TeleportManager;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

import java.util.List;

@Command(name = "getrandomtp", aliases = "rtp")
@Permission("getrtp.use")
public class RTPCommand {

    private final TeleportManager teleportManager;

    public RTPCommand(TeleportManager teleportManager) {
        this.teleportManager = teleportManager;
    }

    @Execute()
    public void randomTP(@Context Player player) {
        teleportManager.randomTeleport(player, player.getWorld());
        MessageUtil.sendMessage(player, "&aPomyslnie nadano przedmiot!");
    }

}
