package org.example.intellibookmallapi.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解：标记需要Token验证的接口
 * 使用此注解的Controller方法参数会自动注入当前用户信息
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenToUser {
    /**
     * 是否必须登录，默认为true
     * 如果设置为false，则在未登录时返回null而不是抛出异常
     */
    boolean required() default true;
}
