package com.smate.core.base.utils.service.consts;

import java.util.List;

import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.exception.SmateException;

public interface SieConstDicManage {

  List<ConstDictionary> getSieConstByGategory(String gategory) throws SmateException;

  /**
   * 获取单位性质Id
   * 
   * @param name
   * @return
   * @throws ServiceException
   */
  public Long getNatureByName(String name) throws SmateException;
}
