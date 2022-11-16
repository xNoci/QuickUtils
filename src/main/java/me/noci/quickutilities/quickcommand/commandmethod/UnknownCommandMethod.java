package me.noci.quickutilities.quickcommand.commandmethod;

import me.noci.quickutilities.quickcommand.annotations.CommandPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class UnknownCommandMethod extends CommandMethod<UnknownCommandMethod> {

    protected UnknownCommandMethod(Method method, CommandPermission permission, int commandArgs, boolean requiresPlayer) {
        super(method, permission, commandArgs, requiresPlayer);
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return true;
    }

    @Override
    protected boolean doesCommandMatch(CommandSender sender, String[] args) {
        return true;
    }

    @Override
    protected MatchPriority calculatePriority(UnknownCommandMethod other, CommandSender sender, String[] args) {
        boolean needsPlayerAsSender = sender instanceof Player;
        boolean sameCommandSenderType = (this.requiresPlayer && other.requiresPlayer) || (!this.requiresPlayer && !other.requiresPlayer);

        if (sameCommandSenderType) {
            return MatchPriority.EQUAL;
        }

        if (needsPlayerAsSender) {
            return this.requiresPlayer ? MatchPriority.THIS : MatchPriority.OTHER;
        } else {
            return !this.requiresPlayer ? MatchPriority.THIS : MatchPriority.OTHER;
        }
    }

}
