package me.noci.quickutilities.qcommand.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandPermission {

    /**
     * If set to true, every given permission is needed otherwise only one of the permissions is needed
     *
     * @return if permissions are strict
     */
    boolean strict() default false;

    /**
     * Empty or null Strings will be ignored
     *
     * @return an array of permissions
     */
    String[] value();

}
