package com.smate.center.task.single.service.pub;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.constant.ConstDictionary;

/**
 * @author Administrator
 * 
 */
public interface ConstDictionaryManageCb {

  /**
   * 取得常量.
   * 
   * @param gategory
   * @return List<ConstDictionary>
   * @throws ServiceException
   */
  List<ConstDictionary> getConstByGategory(String gategory) throws ServiceException;

  /**
   * 取得人员管理模块常量.
   * 
   * @param gategory
   * @return List<ConstDictionary>
   * @throws ServiceException
   */
  Map<Long, String> getConstGategory(String gategory) throws ServiceException;

  /**
   * 取得人员管理模块常量. Map供freemarker使用，只能用Map<String,String>
   * 
   * @param gategory
   * @param codes
   * 
   * @return List<ConstDictionary>
   * @throws ServiceException
   */
  Map<String, String> getConstGategoryForFM(String gategory, String codes) throws ServiceException;

  /**
   * 
   * @param category
   * @param code
   * @return
   * @throws ServiceException
   */
  ConstDictionary findConstByCategoryAndCode(String category, String code) throws ServiceException;

  /**
   * 获取常量名称.
   * 
   * @param category
   * @param code
   * @param locale
   * @return
   * @throws ServiceException
   */
  String findConstName(String category, String code, Locale locale) throws ServiceException;

  /**
   * 
   * @param category
   * @param name
   * @return
   * @throws ServiceException
   */
  ConstDictionary findConstByCategoryAndName(String category, String name) throws ServiceException;

  /**
   * 保存常量.
   * 
   * @param constDictionary
   */
  ConstDictionary saveConstDictionary(ConstDictionary constDictionary);

  /**
   * 移除常量.
   * 
   * @param dictionary
   * @throws ServiceException
   */
  void removeConstDictionary(String category, String code) throws ServiceException;

  /**
   * 接收常量数据同步.
   * 
   * @param list
   * @throws ServiceException
   */
  void pullConstDictionarySyn(List<ConstDictionary> list) throws ServiceException;

  /**
   * 查找code通过名称.
   * 
   * @param category
   * @param name
   * @return
   * @throws ServiceException
   */
  String findCodeByName(String category, String name) throws ServiceException;

  /**
   * 通过类别和code查找名称，codes包含多个code 用逗号隔开
   * 
   * @param category
   * @param codes
   * @return
   * @throws ServiceException
   */
  Map<String, ConstDictionary> findConstByCategoryAndCodes(String category, String codes) throws ServiceException;

}
