package me.noci.quickutilities.qcommand.executor;

import me.noci.quickutilities.qcommand.annotation.CommandPermission;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;

public class SubCommandCommandExecutor extends BaseCommandExecutor<SubCommandCommandExecutor> {

    public SubCommandCommandExecutor(Method method, CommandPermission permission) {
        super(method, permission);
    }

    @Override
    public boolean matches(CommandSender sender, String[] args) {
        return true;
    }

    @Override
    public MatchPriority compareMatch(SubCommandCommandExecutor toCompare, CommandSender sender, String[] args) {
        return MatchPriority.THIS;
    }

}
