package dev.gether.getgrouptp.cmd.handler;

import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getgrouptp.file.FileManager;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import org.bukkit.command.CommandSender;

public class PermissionHandler implements MissingPermissionsHandler<CommandSender> {

    private final FileManager fileManager;

    public PermissionHandler(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    @Override
    public void handle(Invocation<CommandSender> invocation, MissingPermissions missingPermissions, ResultHandlerChain<CommandSender> chain) {
        String permissions = missingPermissions.asJoinedText();
        CommandSender sender = invocation.sender();

        MessageUtil.sendMessage(sender, fileManager.getConfig().getNoPermission()
                .replace("{permission}", String.join(", ", permissions))
        );
    }
}