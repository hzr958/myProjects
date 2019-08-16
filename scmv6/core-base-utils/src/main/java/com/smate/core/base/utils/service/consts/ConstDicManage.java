package com.smate.core.base.utils.service.consts;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.smate.core.base.utils.exception.SmateException;
import com.smate.core.base.utils.model.consts.ConstDictionary2;

public interface ConstDicManage {


  /**
   * 取得常量.
   * 
   * @param gategory
   * @return List<ConstDictionary>
   * @throws SmateException
   */
  List<ConstDictionary2> getConstByGategory(String gategory) throws SmateException;

}
