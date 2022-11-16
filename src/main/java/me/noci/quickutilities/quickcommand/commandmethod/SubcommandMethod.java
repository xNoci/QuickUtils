package me.noci.quickutilities.quickcommand.commandmethod;

import me.noci.quickutilities.quickcommand.annotations.CommandPermission;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.Arrays;

public class SubcommandMethod extends CommandMethod<SubcommandMethod> {

    private final String[] subcommandNames;

    protected SubcommandMethod(Method method, CommandPermission permission, String[] subcommandNames, int commandArgs, boolean requiresPlayer) {
        super(method, permission, commandArgs, requiresPlayer);
        this.subcommandNames = subcommandNames;
    }

    @Override
    protected boolean doesCommandMatch(CommandSender sender, String[] args) {
        String[] trimmedArgs = trimSubcommandArgs(args);
        if (hasFixedArgCount() && commandArgs != trimmedArgs.length) return false;

        if (args.length < subcommandNames.length) return false;
        for (int i = 0; i < subcommandNames.length; i++) {
            if (subcommandNames[i].equalsIgnoreCase("*")) continue;
            if (!subcommandNames[i].equalsIgnoreCase(args[i])) return false;
        }

        return true;
    }


    @Override
    protected MatchPriority calculatePriority(SubcommandMethod other, CommandSender sender, String[] args) {
        if (this.subcommandNames.length < args.length && other.subcommandNames.length < args.length)
            return MatchPriority.EQUAL;
        if (this.subcommandNames.length < args.length) return MatchPriority.OTHER;
        if (other.subcommandNames.length < args.length) return MatchPriority.THIS;

        int minArgs = Math.min(this.subcommandNames.length, other.subcommandNames.length);

        for (int i = 0; i < minArgs; i++) {
            String arg = args[i];
            String thisArg = this.subcommandNames[i];
            String otherArg = other.subcommandNames[i];

            if ((thisArg.equalsIgnoreCase("*") && otherArg.equalsIgnoreCase("*")) || ((thisArg.equalsIgnoreCase(arg) && otherArg.equalsIgnoreCase(arg))))
                continue;
            if (thisArg.equalsIgnoreCase(arg)) return MatchPriority.THIS;
            if (otherArg.equalsIgnoreCase(arg)) return MatchPriority.OTHER;
        }

        return MatchPriority.EQUAL;
    }

    private String[] trimSubcommandArgs(String[] args) {
        int argsToRemove = subcommandNames.length;
        int newArrayLength = args.length - argsToRemove;
        if (newArrayLength <= 0) return new String[0];
        return Arrays.copyOfRange(args, argsToRemove, args.length);
    }
}
