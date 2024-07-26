package dev.gether.getgrouptp;

import dev.gether.getconfig.selector.SelectorAddon;
import dev.gether.getgrouptp.bstats.Metrics;
import dev.gether.getgrouptp.cmd.GroupTPCommand;
import dev.gether.getgrouptp.cmd.RandomTPCommand;
import dev.gether.getgrouptp.cmd.handler.InvalidUsageCommandHandler;
import dev.gether.getgrouptp.cmd.handler.PermissionHandler;
import dev.gether.getgrouptp.core.TeleportManager;
import dev.gether.getgrouptp.file.FileManager;
import dev.gether.getgrouptp.listener.PlayerInteractListener;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class GetGroupTP extends JavaPlugin {

    private static GetGroupTP instance;
    private SelectorAddon selectorAddon;
    private LiteCommands liteCommands;
    private FileManager fileManager;
    private TeleportManager teleportManager;

    @Override
    public void onEnable() {
        instance = this;

        this.fileManager = new FileManager(this);

        // selector
        this.selectorAddon = new SelectorAddon();
        this.selectorAddon.enable(this);

        teleportManager = new TeleportManager(fileManager);

        getServer().getPluginManager().registerEvents(new PlayerInteractListener(fileManager, teleportManager), this);

        // register cmd
        registerCommand();

        Metrics metrics = new Metrics(this, 22789);
    }

    @Override
    public void onDisable() {

        if(selectorAddon != null) {
            selectorAddon.disable();
        }
        if(liteCommands != null) {
            liteCommands.unregister();
        }

        HandlerList.unregisterAll(this);
    }

    private void registerCommand() {
        this.liteCommands = LiteBukkitFactory.builder("get", this)
                .commands(
                        new GroupTPCommand(selectorAddon, fileManager),
                        new RandomTPCommand(teleportManager)
                )
                .invalidUsage(new InvalidUsageCommandHandler(fileManager))
                .missingPermission(new PermissionHandler(fileManager))
                //
                .build();

    }

}
