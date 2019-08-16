package com.smate.core.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Struts2 Action RequestMethod注解，用于限定Action只接收指定的请求。 <br>
 * 可限定的请求类型有：GET, POST, HEAD, OPTIONS, PUT, PATCH, DELETE, TRACE
 * 
 * @author ChuanjieHou
 * @date 2017年9月14日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMethod {
  String value() default DEFAULT;

  String DEFAULT = "default";
  String GET = "GET";
  String HEAD = "HEAD";
  String POST = "POST";
  String PUT = "PUT";
  String DELETE = "DELETE";
  String OPTIONS = "OPTIONS";
  String PATCH = "PATCH";
  String TRACE = "TRACE";
}
