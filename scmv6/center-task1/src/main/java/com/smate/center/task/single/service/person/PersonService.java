package com.smate.center.task.single.service.person;

import java.util.List;

import com.smate.center.task.exception.PersonTaskException;
import com.smate.core.base.utils.model.security.Person;

/**
 * 人员后台任务服务接口.
 * 
 * @author lj
 *
 */
public interface PersonService {
  /**
   * 通过PsnId获取人员id,姓名，邮箱语言、性别.
   * 
   * @return Person
   */
  public Person getPeronsForEmail(long psnId) throws PersonTaskException;

  /**
   * 获取跑任务的人员.
   * 
   * @param size not null
   */
  List<Long> getPersonList(Long startPsnId, int size);
}
