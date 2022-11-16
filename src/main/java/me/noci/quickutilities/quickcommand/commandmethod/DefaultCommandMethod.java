package me.noci.quickutilities.quickcommand.commandmethod;

import me.noci.quickutilities.quickcommand.annotations.CommandPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class DefaultCommandMethod extends CommandMethod<DefaultCommandMethod> {

    protected DefaultCommandMethod(Method method, CommandPermission permission, int commandArgs, boolean requiresPlayer) {
        super(method, permission, commandArgs, requiresPlayer);
    }

    @Override
    protected boolean doesCommandMatch(CommandSender sender, String[] args) {
        if (!hasFixedArgCount()) return true;
        return commandArgs == args.length;
    }

    @Override
    protected MatchPriority calculatePriority(DefaultCommandMethod other, CommandSender sender, String[] args) {
        boolean playerProvided = sender instanceof Player;
        boolean sameCommandSenderType = (this.requiresPlayer && other.requiresPlayer) || (!this.requiresPlayer && !other.requiresPlayer);

        if (sameCommandSenderType) {
            return MatchPriority.EQUAL;
        }

        if (!playerProvided) {
            return this.requiresPlayer ? MatchPriority.THIS : MatchPriority.OTHER;
        } else {
            return !this.requiresPlayer ? MatchPriority.THIS : MatchPriority.OTHER;
        }
    }
}
