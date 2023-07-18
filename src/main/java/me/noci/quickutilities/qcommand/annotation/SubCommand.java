package me.noci.quickutilities.qcommand.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommand {

    /**
     * The subcommand path for example:
     * <br>
     * /command path[0] path[1] ...
     * @return subcommand path as array
     */
    String[] path();

}
