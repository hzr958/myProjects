package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.AcPrjScheme;
import com.smate.center.batch.model.sns.pub.PrjScheme;

/**
 * 项目资助类别.
 * 
 * @author liqinghua
 * 
 */
public interface PrjSchemeService extends Serializable {

  /**
   * 查询指定智能匹配前 N 条数据.
   * 
   * @param startStr
   * @param agencyId
   * @param size
   * @return
   * @throws ServiceException
   */
  List<AcPrjScheme> getAcPrjScheme(String startStr, Long agencyId, int size) throws ServiceException;

  /**
   * 通过名字查找项目资助类别.
   * 
   * @param name
   * @param agencyId
   * @return
   * @throws ServiceException
   */
  PrjScheme findByName(String name, Long agencyId) throws ServiceException;

  PrjScheme findByName(String name) throws ServiceException;
}
