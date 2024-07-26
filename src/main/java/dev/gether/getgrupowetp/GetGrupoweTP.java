package dev.gether.getgrupowetp;

import dev.gether.getconfig.selector.SelectorAddon;
import dev.gether.getgrupowetp.cmd.GroupTPCommand;
import dev.gether.getgrupowetp.cmd.RTPCommand;
import dev.gether.getgrupowetp.core.TeleportManager;
import dev.gether.getgrupowetp.file.FileManager;
import dev.gether.getgrupowetp.listener.PlayerInteractListener;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class GetGrupoweTP extends JavaPlugin {

    private static GetGrupoweTP instance;
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
        this.liteCommands = LiteBukkitFactory.builder("grupowetp", this)
                .commands(
                        new GroupTPCommand(selectorAddon, fileManager),
                        new RTPCommand(teleportManager)
                )
                //
                .build();

    }

}
