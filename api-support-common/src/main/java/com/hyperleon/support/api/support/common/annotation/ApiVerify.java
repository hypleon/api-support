package com.hyperleon.support.api.support.common.annotation;

import java.lang.annotation.*;

/**
 * @author leon
 * @date  2020-08-12 13:45
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiVerify {

    boolean verify() default true;
}
