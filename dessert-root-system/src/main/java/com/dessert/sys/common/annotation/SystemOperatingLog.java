package com.dessert.sys.common.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解-操作日志
 *
 */

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemOperatingLog {

    String module()  default "";  //模块名称
    String methods()  default "";  //操作
    String description()  default "";  //描述
}
