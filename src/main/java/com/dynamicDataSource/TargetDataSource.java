package com.dynamicDataSource;

import java.lang.annotation.*;

/**
 *  在方法上使用，用于指定使用那个数据源
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource{
    /**
     * 数据库源的名字
     * @return
     */
    String name();
}
