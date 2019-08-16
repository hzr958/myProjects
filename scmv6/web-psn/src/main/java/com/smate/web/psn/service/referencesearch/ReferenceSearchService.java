package com.smate.web.psn.service.referencesearch;

import java.util.List;

import com.smate.web.psn.model.pub.ConstRefDbFrom;

/**
 * 更新引用用-------其他成果相关的请去pub项目
 * 
 * @author Administrator
 *
 */
public interface ReferenceSearchService {
  /**
   * 获取可查找的数据库列表.
   * 
   * @param dbType
   * @return
   */
  public List<ConstRefDbFrom> getDbList(String dbType, List<Long> insIdList);

  /**
   * 根据数据库列表，获取.
   * 
   * @param dbList
   * @return
   */
  public String getDbUrl(List<ConstRefDbFrom> dbList);
}
