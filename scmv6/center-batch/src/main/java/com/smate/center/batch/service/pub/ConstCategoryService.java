package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConstCategory;

/**
 * 常量类别.
 * 
 * @author liqinghua
 * 
 */
public interface ConstCategoryService extends Serializable {

  /**
   * 判断常量类别是否存在.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  Boolean isConstCategoryExist(String id) throws ServiceException;

  /**
   * 保存常量类别.
   * 
   * @param constCategory
   * @throws ServiceException
   */
  ConstCategory saveConstCategory(ConstCategory constCategory);

  /**
   * 获取所有常量类别列表.
   * 
   * @return
   */
  List<ConstCategory> getAllConstCategory();

  /**
   * 获取单个常量类别.
   * 
   * @param id
   * @return
   */
  ConstCategory getConstCategory(String id);

  /**
   * 移除常量.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  Integer removeConstCategory(String id) throws ServiceException;
}
