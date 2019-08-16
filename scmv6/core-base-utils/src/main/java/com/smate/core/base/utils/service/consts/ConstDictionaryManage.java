package com.smate.core.base.utils.service.consts;

import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.constant.ConstDictionary;

public interface ConstDictionaryManage {

  /**
   * 通过类别和code查找名称，codes包含多个code 用逗号隔开
   * 
   * @param category
   * @param codes
   * @return
   * @throws ServiceException
   */
  Map<String, ConstDictionary> findConstByCategoryAndCodes(String category, String codes) throws Exception;

  /**
   * 
   * @param category
   * @param code
   * @return
   * @throws ServiceException
   */
  ConstDictionary findConstByCategoryAndCode(String category, String code) throws Exception;

  /**
   * 
   * @param category
   * @return
   * @throws Exception
   */
  List<ConstDictionary> getConstByCategory(String category) throws Exception;

  /**
   * 
   * @param category
   * @param name
   * @return
   * @throws ServiceException
   */
  ConstDictionary findConstByCategoryAndName(String category, String name) throws Exception;

  /**
   * 取得常量.
   * 
   * @param gategory
   * @return List<ConstDictionary>
   * @throws ServiceException
   */
  List<ConstDictionary> getConstByGategory(String category);

  Map<String, String> getConstGategoryForFM(String category, String codes) throws Exception;
}
