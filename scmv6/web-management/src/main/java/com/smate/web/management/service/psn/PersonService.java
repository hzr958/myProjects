package com.smate.web.management.service.psn;

import java.util.List;

import com.smate.core.base.utils.model.security.Person;
import com.smate.web.management.model.psn.PsnInfoForm;

/**
 * 人员服务接口
 * 
 * @author zll
 *
 */
public interface PersonService {
  /**
   * 或者所有人员
   * 
   * @return
   */

  public List<Person> getAllPsn(PsnInfoForm form);


}
