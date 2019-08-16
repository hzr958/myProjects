package com.smate.web.psn.service.profile;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.profile.ConstSurName;


/**
 * 复姓service.
 * 
 * @author lj
 * 
 */
public interface ConstSurNameService extends Serializable {

  /**
   * 得到所有复姓列表
   * 
   * @return
   * @throws ServiceException
   */
  public List<ConstSurName> findAllSurName() throws PsnException;

  /**
   * 中文姓名转为拼音(包括复姓的转换)
   * 
   * @param cname
   * @return
   */
  public Map<String, String> parsePinYin(String cname);
}
