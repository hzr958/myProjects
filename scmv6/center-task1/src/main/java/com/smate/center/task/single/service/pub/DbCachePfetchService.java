package com.smate.center.task.single.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.pub.DbCachePfetch;
import com.smate.center.task.model.pdwh.pub.PdwhPubXmlToHandle;
import com.smate.center.task.single.oldXml.pub.ImportPubXmlDocument;

/**
 * 批量抓取成果XML处理接口.
 * 
 * @author LJ 2017-2-28
 * 
 */
public interface DbCachePfetchService extends Serializable {

  /**
   * 获取待处理的成果XML
   * 
   * @param size
   * @return
   * @throws ServiceException
   */

  public List<DbCachePfetch> getTohandleList(int size) throws ServiceException;

  /**
   * 保存错误
   * 
   * @param id
   * @param string
   * @throws ServiceException
   */
  public void saveError(Long id) throws ServiceException;

  /**
   * 保存成功
   * 
   * @param id
   * @throws ServiceException
   */
  public void saveSuccess(Long id) throws ServiceException;

  /* *//**
        * 构造v_batch_jobs
        * 
        * @param XmlId
        * @throws ServiceException
        *//*
           * 
           * public void constructBatchJobs(Long XmlId) throws ServiceException;
           */

  /**
   * 
   * @param doc
   * @return
   * @throws ServiceException
   */

  public boolean validateXml(ImportPubXmlDocument doc) throws ServiceException;

  /**
   * 
   * @param pdwhPubXmlToHandle
   * @return
   */

  public void savePdwhPubXml(PdwhPubXmlToHandle pdwhPubXmlToHandle) throws ServiceException;

  /* public void constructBatchJobs(Long tmpId, int status); */

}
