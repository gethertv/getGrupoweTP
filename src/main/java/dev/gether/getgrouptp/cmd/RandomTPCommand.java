package dev.gether.getgrouptp.cmd;

import dev.gether.getgrouptp.core.TeleportManager;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

@Command(name = "randomtp", aliases = "rtp")
@Permission("randomtp.use")
public class RandomTPCommand {

    private final TeleportManager teleportManager;

    public RandomTPCommand(TeleportManager teleportManager) {
        this.teleportManager = teleportManager;
    }

    @Execute
    public void executeRandomTeleport(@Context Player player) {
        teleportManager.randomTeleport(player, player.getWorld());
    }
}