package com.smate.center.task.service.search;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.search.UserSearchDataForm;
import com.smate.center.task.model.sns.quartz.Institution;
import com.smate.core.base.utils.model.security.Person;


/**
 * 
 * @author liqinghua
 * 
 */
public interface UserSearchService extends Serializable {


  /**
   * 首页-页脚链接检索人员.
   * 
   * @param params 请求参数
   * @return
   * @throws ServcieException
   */
  @SuppressWarnings("rawtypes")
  public List<UserSearchDataForm> findIndexUserList(String userInfo, String locale, int maxSize)
      throws ServiceException;


  public void saveUserSearch(Person psn, String zhName, String enName, Integer nodeId, Integer isPrivate,
      Institution ins, int scoreNum) throws ServiceException;



}
