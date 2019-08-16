package com.smate.web.psn.service.search;

import java.util.Map;
import java.util.Set;

import com.smate.web.psn.action.search.PersonSearchForm;
import com.smate.web.psn.exception.PsnException;

/**
 * 人员检索服务接口
 * 
 * @author zk
 *
 */
public interface PersonSearchService {

  /**
   * 检索人员入口
   * 
   * @param form
   * @throws PsnException
   */
  public void searchPerson(PersonSearchForm form) throws PsnException;

  /**
   * 提取检索字符串中的人名与机构名, 提高准确性
   * 
   * @param string
   * @throws Exception
   */
  public Map<String, Set<String>> getExtractUserAndInsName(String str) throws Exception;
}
