package com.smate.core.base.utils.datasource.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.smate.core.base.utils.dynamicds.DataSourceEnum;

/**
 * 自定义切换数据源注解
 * 
 * @author tsz
 *
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface NextDataSourceAnnotation {
  // 规定参数类型
  DataSourceEnum dataSource();
}
