package com.smate.web.group.service.group.psn;

import com.smate.core.base.utils.model.security.Person;
import com.smate.web.group.exception.GroupException;
import com.smate.web.group.model.group.psn.PsnInfo;

/**
 * 人员信息服务接口
 * 
 * @author zk
 *
 */
public interface PsnInfoService {

  /**
   * 根据人员id和语言获取人员中(英)文名
   * 
   * @param person
   * @param locale
   * @return
   * @throws PsnException
   */
  String getPsnName(Person person, String locale) throws GroupException;

  /**
   * 根据人员对象和语言获取人员中(英)文名
   * 
   * @param psnId
   * @param locale
   * @return
   * @throws GroupException
   */
  String getPsnName(Long psnId, String locale) throws GroupException;

  /**
   * 获取群组成员项目、成果统计数以及工作经历
   * 
   * @param psnId
   * @throws GroupException
   */
  void packageGroupMemberInfo(PsnInfo psnInfo) throws GroupException;

}
