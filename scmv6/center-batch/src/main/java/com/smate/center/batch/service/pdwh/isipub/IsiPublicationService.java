package com.smate.center.batch.service.pdwh.isipub;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubAssign;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubExtend;

/**
 * isi基准库服务类.
 * 
 * @author liqinghua
 * 
 */
public interface IsiPublicationService extends Serializable {

  /**
   * 根据sourceId查找成果基准库id.
   * 
   * @param sourceId
   * @return
   * @throws ServiceException
   */
  public Long getPubPdwhIdBySourceId(String sourceId) throws ServiceException;

  /**
   * 查询重复记者库成果.
   * 
   * @param sourceIdHash
   * @param titleHash
   * @param unitHash
   * @return
   * @throws ServiceException
   */
  public Long getDupPub(Long sourceIdHash, Long titleHash, Long unitHash) throws ServiceException;

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
  public List<IsiPubAssign> getNeedSendPub(Long startId, int size) throws ServiceException;

  /**
   * 发送单位成果.
   * 
   * @param pubassign
   * @throws ServiceException
   */
  public void sendInsPub(IsiPubAssign pubassign) throws ServiceException;

  /**
   * 根据成果ID获取基准库ISI成果表中的成果记录.
   * 
   * @param pubId
   * @return
   */
  public IsiPubExtend getIsiPubExtend(Long pubId);

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
   * 获取待解析的成果列表.
   * 
   * @param minPubId 获取成果ID的最小值.
   * @param size 一次获取成果记录数.
   * @return
   */
  public List<IsiPubExtend> getIsiPubExtendList(List<Long> pubIdList);

  /**
   * 获取待处理的成果列表.
   * 
   * @param minPubId
   * @param size
   * @return
   */
  public List<Long> getIsiPubIdList(Long minPubId, int size);

  /**
   * 获取遗漏未拆分的成果任务列表.
   * 
   * @param maxSize
   * @return
   */
  public List<Long> getMissPubIdTask(Integer maxSize);

  /**
   * 查询需要同步到pubftsrv的isi成果.
   * 
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  public List<Long> getNeedSyncIsiPub(int maxSize) throws ServiceException;

  /**
   * 开始同步冗余数据.
   * 
   * @param pubIdList
   * @throws ServiceException
   */
  public void syncIsiPub(List<Long> pubIdList) throws ServiceException;

  /**
   * 
   * 查询pubCacheAssign所需数据,包含xml
   * 
   * 
   */
  public IsiPubAssign getIsiPubAssign(Long pubId, Long insId) throws ServiceException;

  public List<Long> getIsiPubId(Long insId);

  public void savePubAssignStatus(IsiPubAssign pubAssign, Integer status);

}
