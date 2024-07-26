package dev.gether.getgrouptp.cmd.handler;

import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getgrouptp.file.FileManager;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class InvalidUsageCommandHandler implements InvalidUsageHandler<CommandSender> {

    private final FileManager fileManager;

    public InvalidUsageCommandHandler(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        CommandSender sender = invocation.sender();
        Schematic schematic = result.getSchematic();

        if (schematic.all().size() == 1) {
            String first = schematic.first();
            String[] split = first.split("\\|");
            if(split.length<=1) {
                MessageUtil.sendMessage(sender, fileManager.getConfig().getUsageCommand().replace("{usage}", first));
                return;
            }
        }
        List<String> usage = new ArrayList<>();
        for (String scheme : schematic.all()) {
            String usageFormat = fileManager.getConfig().getUsageFormat();
            usageFormat = usageFormat.replace("{scheme}", scheme);
            usage.add(usageFormat);
        }
        MessageUtil.sendMessage(sender, String.join("\n", usage));

    }
}
