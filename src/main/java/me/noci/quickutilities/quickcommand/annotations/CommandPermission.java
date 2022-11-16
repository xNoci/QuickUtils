package me.noci.quickutilities.quickcommand.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CommandPermission {

    /**
     * If set to true, every given permission is needed otherwise only one of the permissions is needed
     *
     * @return
     */
    boolean strict() default false;

    /**
     * Empty or null Strings will be ignored
     *
     * @return
     */
    String[] value();

}
