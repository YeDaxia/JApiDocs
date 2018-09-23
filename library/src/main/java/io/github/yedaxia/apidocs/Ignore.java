package io.github.yedaxia.apidocs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ignore Field
 * @author yeguozhong yedaxia.github.com
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
public @interface Ignore {
}
