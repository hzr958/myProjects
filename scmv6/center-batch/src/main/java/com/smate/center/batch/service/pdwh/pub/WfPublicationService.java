package com.smate.center.batch.service.pdwh.pub;

import java.io.Serializable;
import java.util.Date;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * wanfang基准库服务类.
 * 
 * @author liqinghua
 * 
 */
public interface WfPublicationService extends Serializable {

  /**
   * 查询重复记者库成果.
   * 
   * @param titleHash
   * @param unitHash
   * @return
   * @throws ServiceException
   */
  public Long getDupPub(Long titleHash, Long unitHash) throws ServiceException;

  /**
   * 添加批量抓取成果Xml.
   * 
   * @param insId
   * @param dataXml
   * @throws ServiceException
   */
  public void addDbCachePub(Long insId, String dataXml) throws ServiceException;

  /**
   * 增加用户在线抓取成果XML.
   * 
   * @param psn
   * @param fetchTime
   * @param dataXml
   * @throws ServiceException
   */
  public void addFetchPub(Long psnId, Long insId, Date fetchTime, String dataXml) throws ServiceException;
}
