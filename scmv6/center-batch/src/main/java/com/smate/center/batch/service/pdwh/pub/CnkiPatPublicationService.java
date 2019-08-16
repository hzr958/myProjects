package com.smate.center.batch.service.pdwh.pub;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubAssign;

/**
 * CnkiPat专利基准库服务类.
 * 
 * @author liqinghua
 * 
 */
public interface CnkiPatPublicationService extends Serializable {

  /**
   * 查询重复记者库成果.
   * 
   * @param sourceIdHash
   * @param titleHash
   * @param unitHash
   * @return
   * @throws ServiceException
   */
  public Long getDupPub(Long titleHash, Long patentHash) throws ServiceException;

  /**
   * 查询重复记者库成果.
   * 
   * @param sourceIdHash
   * @param titleHash
   * @param unitHash
   * @return
   * @throws ServiceException
   */
  public Long getDupPub(Long titleHash, Long patentHash, Long patentOpenHash) throws ServiceException;

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

  /**
   * 获取需要发送到机构的指派信息.
   * 
   * @param startId
   * @param size
   * @return
   * @throws ServiceException
   */
  public List<CnkiPatPubAssign> getNeedSendPub(Long startId, int size) throws ServiceException;

  /**
   * 发送单位成果.
   * 
   * @param pubassign
   * @throws ServiceException
   */
  public void sendInsPub(CnkiPatPubAssign pubassign) throws ServiceException;

  public CnkiPatPubAssign getCnkiPatPubAssign(Long pubId, Long insId) throws ServiceException;
}
