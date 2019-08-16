package com.smate.center.open.service.nsfc;

import java.util.List;
import java.util.Map;

import com.smate.center.open.model.nsfc.ConstSurName;

public interface ConstSurNameService {

  /**
   * 中文姓名转为拼音(包括复姓的转换)
   * 
   * @param cname
   * @return
   */
  public Map<String, String> parsePinYin(String cname);

  /**
   * 得到所有复姓列表
   * 
   * @return
   * @throws ServiceException
   */
  public List<ConstSurName> findAllSurName() throws Exception;

}
