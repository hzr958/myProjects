package com.smate.center.batch.service.pdwh.pub;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubAssign;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubExtend;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubAssign;

/**
 * cnki基准库服务类.
 * 
 * @author liqinghua
 * 
 */
public interface CnkiPublicationService extends Serializable {

  /**
   * 查询重复记者库成果.
   * 
   * @param sourceIdHash
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

  /**
   * 获取需要发送到机构的指派信息.
   * 
   * @param startId
   * @param size
   * @return
   * @throws ServiceException
   */
  public List<CnkiPubAssign> getNeedSendPub(Long startId, int size) throws ServiceException;

  /**
   * 发送单位成果.
   * 
   * @param pubassign
   * @throws ServiceException
   */
  public void sendInsPub(CnkiPubAssign pubassign) throws ServiceException;

  /**
   * 保存关键词拆分.
   * 
   * @param pubId
   * @param keywords
   * @param type
   * @throws ServiceException
   */
  public void saveKeywordsSplit(Long pubId, String keywords, Integer type) throws ServiceException;

  /**
   * 获取基准库XML.
   * 
   * @param pubId
   * @return
   */
  public CnkiPubExtend getCnkiPubExtend(Long pubId);

  /**
   * 获取待解析的成果列表.
   * 
   * @param pubIdList 成果ID列表.
   * @return
   */
  public List<CnkiPubExtend> getCnkiPubExtendList(List<Long> pubIdList);

  /**
   * 获取待解析的成果ID列表.
   * 
   * @param minPubId 获取成果ID的最小值.
   * @param size 一次获取成果记录数.
   * @return
   */
  List<Long> getCnkiPubIdList(Long minPubId, int size);

  /**
   * 获取遗漏未拆分的成果.
   * 
   * @param maxSize
   * @return
   */
  List<Long> getMissPubIdTask(Integer maxSize);

  public CnkiPubAssign getCnkiPubAssign(Long pubId, Long insId) throws ServiceException;

  public List<Long> getCnkiPubId(Long insId);

  public void savePubAssignStatus(CnkiPubAssign pubAssign, Integer status);

  public void dealCnkiXml(String xml, Long pubid, Long insId) throws ServiceException;
}
