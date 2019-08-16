package com.smate.core.base.utils.dynamicds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * 
 * @author zk
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

  @Override
  protected Object determineCurrentLookupKey() {
    return CustomerContextHolder.getDataSource();
  }

}
