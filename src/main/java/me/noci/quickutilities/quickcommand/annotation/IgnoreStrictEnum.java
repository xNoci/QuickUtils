package me.noci.quickutilities.quickcommand.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If this annotation is added to an enum parameter.
 * The given value of the enum can be null if the command sender uses a wrong enum name.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface IgnoreStrictEnum {
}
