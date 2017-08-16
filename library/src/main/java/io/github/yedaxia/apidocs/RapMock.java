package io.github.yedaxia.apidocs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * rap mock support annotation
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
public @interface RapMock {

    /**
     * the limit of the parameter, for example: parameter of 'string|1-10' is '1-10'
     * @return
     */
    String limit() default "";

    /**
     * mock value
     * @return
     */
    String value() default "";
}
