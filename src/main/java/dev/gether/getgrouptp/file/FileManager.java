package dev.gether.getgrouptp.file;

import dev.gether.getconfig.ConfigManager;
import dev.gether.getgrouptp.GetGroupTP;
import dev.gether.getgrouptp.file.config.Config;

import java.io.File;

public class FileManager {

    private final Config config;

    public FileManager(GetGroupTP plugin) {
        config = ConfigManager.create(Config.class, it-> {
            it.file(new File(plugin.getDataFolder(), "config.yml"));
            it.load();
        });
    }

    public Config getConfig() {
        return config;
    }

    public void reload() {
        config.load();
    }
}
