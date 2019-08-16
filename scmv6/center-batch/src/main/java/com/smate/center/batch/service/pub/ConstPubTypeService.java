package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Locale;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConstPubType;

/**
 * @author yamingd
 * 
 */
public interface ConstPubTypeService {

  /**
   * 读取所有Enabled=1的类别.
   * 
   * @return
   */
  List<ConstPubType> getAll() throws ServiceException;

  List<ConstPubType> getTypes(int articleType) throws ServiceException;

  /**
   * 按ID读取类别.
   * 
   * @param id
   * @return
   */
  ConstPubType get(int id) throws ServiceException;

  /**
   * 按ID读取类别.
   * 
   * @param id
   * @param locale
   * @return
   * @throws ServiceException
   */
  ConstPubType get(int id, Locale locale) throws ServiceException;

  /**
   * 按ID读取类别(endNote用，只取英文名).
   * 
   * @param id
   * @return
   */
  ConstPubType endnoteGet(int id) throws ServiceException;

  /**
   * 接收成果类别同步.
   * 
   * @param list
   * @throws ServiceException
   */
  void pullConstPubTypeSyn(List<ConstPubType> list) throws ServiceException;

  String queryResultTypeName(Integer typeId) throws ServiceException;
}
