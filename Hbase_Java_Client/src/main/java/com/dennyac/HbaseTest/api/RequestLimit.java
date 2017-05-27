package com.dennyac.HbaseTest.api;

import java.lang.annotation.*;

/**
 * request请求次数限制
 * 示例:@RequestLimit(ipCount=2,ipTime=60000,uriCount=2,uriTime=60000)
 * @date 2016-03-23 13:28:06
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequestLimit {

    /**
     * ip允许访问的次数，默认值1000
     */
    int ipCount() default 1000;

    /**
     * ip时间段，单位为毫秒，默认值一分钟
     */
    long ipTime() default 60000;

    /**
     * uri允许访问的次数，默认值600
     */
    int uriCount() default 600;

    /**
     * uri时间段，单位为毫秒，默认值一分钟
     */
    long uriTime() default 60000;

}
