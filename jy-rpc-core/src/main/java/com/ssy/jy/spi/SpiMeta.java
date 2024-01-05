package com.ssy.jy.spi;

import java.lang.annotation.*;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-12-07
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SpiMeta {
    String DEFAULT = "default";

    String alias() default DEFAULT;
}
