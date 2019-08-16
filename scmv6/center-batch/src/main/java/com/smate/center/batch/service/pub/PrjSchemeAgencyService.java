package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.AcPrjSchemeAgency;
import com.smate.center.batch.model.sns.pub.PrjSchemeAgency;

/**
 * 项目资助机构.
 * 
 * @author liqinghua
 * 
 */
public interface PrjSchemeAgencyService extends Serializable {

  /**
   * 查询指定智能匹配前 N条数据.
   * 
   * @param startStr
   * @param size
   * @return
   * @throws ServiceException
   */
  List<AcPrjSchemeAgency> getAcPrjSchemeAgency(String startStr, int size) throws ServiceException;

  /**
   * 通过名字查找项目资助机构.
   * 
   * @param name
   * @return
   * @throws ServiceException
   */
  PrjSchemeAgency findByName(String name) throws ServiceException;
}
