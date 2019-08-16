package com.smate.web.v8pub.service.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 成果处理器注解,如果是成果处理器 必须使用该注解
 * 
 * @author tsz
 *
 * @date 2018年6月6日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PubHandlerMapping {
  String pubHandlerName();
}
