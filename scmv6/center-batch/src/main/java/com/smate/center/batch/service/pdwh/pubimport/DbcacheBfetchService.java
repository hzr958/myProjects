package com.smate.center.batch.service.pdwh.pubimport;

import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubXmlToHandle;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;

public interface DbcacheBfetchService {

  /**
   * 获取拆分出的临时xml
   * 
   * @param xmlId Long
   * @return PdwhPubXmlToHandle
   *
   */
  public PdwhPubXmlToHandle getXmlFileById(Long xmlId);

  /**
   * 更新已导入的pdwhpub文件
   * 
   * @param PdwhPublication pdwhPub
   * @param Long dupPubId
   * @param Map <String, Object> pdwhPubInfoMap
   * @return
   *
   */
  public void updatePubInfo(PdwhPublication pdwhPub, Map<String, Object> pdwhPubInfoMap, Long dupPubId)
      throws Exception;

  /**
   * 导入新的基准库成果
   * 
   * @param PdwhPublication pdwhPub
   * @param Map <String, Object> pdwhPubInfoMap
   * @return
   *
   */
  public void saveNewPubInfo(PdwhPublication pdwhPub, Map<String, Object> pdwhPubInfoMap) throws Exception;

  /**
   * 导入新的基准库成果成功，删除临时库xml文件
   * 
   * @param Long xmlId
   * @return
   *
   */
  public void pdwhPubSaveSuccess(Long xmlId);

  /**
   * 导入新的基准库成果失败，更新临时库xml文件状态
   * 
   * @param PdwhPubXmlToHandle pdwhPubXmlToHandle
   * 
   * @return
   *
   */
  public void pdwhPubSaveError(PdwhPubXmlToHandle pdwhPubXmlToHandle);

  /**
   * 为PdwhPublication填充xml信息
   * 
   * @param PdwhPublication pdwhPub
   * @return
   *
   */
  public PdwhPublication wrapPdwhPubXmlInfo(String xmlString) throws ServiceException;

  /**
   * 根据相关成果信息查重获取重复成果Id
   * 
   * @param Long dupPubId
   * @return
   *
   */
  public Long getDupPub(PdwhPublication pdwPub);

  /**
   * 构造v_batch_jobs
   * 
   * @param XmlId
   * @throws ServiceException
   */

  public void constructBatchJobs(Long XmlId) throws ServiceException;

  /**
   * 导入新的基准库成果关键信息缺失，更新临时库xml文件状态
   * 
   * @param PdwhPubXmlToHandle pdwhPubXmlToHandle
   * 
   * @return
   *
   */
  public void pdwhPubSaveInfoMissing(PdwhPubXmlToHandle tmpPubXml);

  /**
   * 更新引用次数时保存新的引用成果
   * 
   * @param pdwhPub
   * @param pdwhPubInfoMap
   * @throws Exception
   */

  public void saveNewPdwhPubInfo(PdwhPublication pdwhPub, Map<String, Object> pdwhPubInfoMap) throws Exception;
}
