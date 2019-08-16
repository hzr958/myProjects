package com.smate.center.batch.service.pdwh.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PublicationRolPdwh;
import com.smate.center.batch.model.sns.pub.PublicationPdwh;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.model.Page;

/**
 * 成果基准库id.
 * 
 * @author liqinghua
 * 
 */
public interface PublicationPdwhService extends Serializable {

  /**
   * 获取sns成果基准库ID.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public PublicationPdwh getPubPdwh(Long pubId) throws ServiceException;

  /**
   * 获取rol成果基准库ID.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public PublicationRolPdwh getPubRolPdwh(Long pubId) throws ServiceException;

  /**
   * 保存sns成果基准库ID.
   * 
   * @param pdwh
   * @throws ServiceException
   */
  public void savePublicationPdwh(PublicationPdwh pdwh) throws ServiceException;

  /**
   * 保存rol成果基准库ID.
   * 
   * @param pdwh
   * @throws ServiceException
   */
  public void savePublicationRolPdwh(PublicationRolPdwh pdwh) throws ServiceException;


  /**
   * 保存成果基准库ID.
   * 
   * @param dbId
   * @param queryId
   * @throws ServiceException
   */
  public PublicationRolPdwh savePublicationRolPdwh(Long pubId, Integer dbId, Long queryId) throws ServiceException;

  /**
   * 将publicationpdwh表中的数据同步到XML中.
   * 
   * @param pubRolPdwh
   */
  public void refreshPubPdwhToXml(PublicationRolPdwh pubRolPdwh) throws ServiceException;

  /**
   * 将Xml数据同步到表publicationpdwh中.
   * 
   * @param pubPdwh
   */
  public void refreshPubPdwhToDB(Long pubId, PubXmlDocument doc) throws ServiceException;

  /**
   * 批量获取成果基准库ID信息.
   * 
   * @param lastId
   * @return
   * @throws ServiceException
   */
  public List<PublicationPdwh> loadPubPdwhBatch(Long lastId) throws ServiceException;

  /**
   * 根据基准库ID和DBID查找出sns的成果ID
   * 
   * @param id
   * @param dbId
   * @return
   * @throws ServiceException
   */
  public List<Long> findSnsIds(Long id, Integer dbId) throws ServiceException;

  /**
   * 
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  public List<PublicationPdwh> getPubPdwhListByPubIds(List<Long> pubIds) throws ServiceException;

  /**
   * 获取psnId好友的论文数
   * 
   * @param psnId
   * @param pdwhPubId
   * @param dbid
   * @return
   * @throws ServiceException
   */
  int getPubPdwhIdByPsnFriend(Long psnId, Long pdwhPubId, int dbid) throws ServiceException;

  /**
   * 人员合作者导入过的论文数
   * 
   * @param psnId
   * @param pdwhPubId
   * @param dbid
   * @return
   * @throws ServiceException
   */
  int getPubPdwhIdByPsnCoop(Long psnId, Long pdwhPubId, int dbid) throws ServiceException;

  /**
   * @param snsPubId
   * @param dbid
   * @return
   * @throws ServiceException
   */
  Long getPdwhPubId(Long snsPubId, int dbid) throws ServiceException;

  /**
   * 根据基准库ID和DBID查找出sns的成果的psnIds
   * 
   * @param page
   * @param id
   * @param dbId
   * @return
   * @throws ServiceException
   */
  public List<Long> findSnsPubOwnerPsnIds(Page page, Long id, Integer dbId) throws ServiceException;

  /**
   * 根据pubId查找sns的成果的psnIds
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public List<Long> findSnsPubOwnerPsnIdsByPubId(Page page, Long pubId) throws ServiceException;

  /**
   * 根据pubId查找其它相关成果的pubIds
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public List<Long> findRelatedPubIdsByPubId(Page page, Long pubId) throws ServiceException;

  /**
   * 根据pubId查找其它相关成果的pubId
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public List<Long> findOtherRelatedPubIdByPubId(Long pubId) throws ServiceException;

  /**
   * 根据基准库ID和DBID查找出sns的成果的psnIds、pubIds
   * 
   * @param id
   * @param dbId
   * @return
   * @throws ServiceException
   */
  public List<Map<String, Object>> findSnsPubPsnIdsAndPubIds(Long id, Integer dbId) throws ServiceException;

  /**
   * 根据pubId查找相关成果的pubIds
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public List<Long> findSnsPubIdsByPubId(Long pubId) throws ServiceException;

  public List<PublicationPdwh> getPubPdwhByPdwhId(Map<String, Long> pdwhPubMap) throws ServiceException;

  public List<Map<String, String>> findSnsPubOwnersByPubId(Page page, Long pubId) throws ServiceException;

  /**
   * 保存指派任务pubAssign至v_batch_jobs
   * 
   * @param pubId, insId,dbSource
   * 
   */
  public void saveToBatchJobs(Long pubId, Long insId, String dbSource) throws ServiceException;

  public void refreshRolPubPdwhToDB(Long pubId, PubXmlDocument doc);
}
