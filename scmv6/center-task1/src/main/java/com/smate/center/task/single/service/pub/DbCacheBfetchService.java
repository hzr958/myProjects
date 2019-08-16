package com.smate.center.task.single.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.pub.DbCacheBfetch;
import com.smate.center.task.single.oldXml.pub.ImportPubXmlDocument;

/**
 * 批量抓取成果XML处理接口.
 * 
 * @author LJ 2017-2-28
 * 
 */
public interface DbCacheBfetchService extends Serializable {

  /**
   * 获取待处理的成果XML
   * 
   * @param size
   * @return
   * @throws ServiceException
   */

  public List<DbCacheBfetch> getTohandleList(int size) throws ServiceException;

  /**
   * 保存错误信息.
   * 
   * @param id
   * @param message
   * @throws ServiceException
   */
  public void saveError(Long id, String message) throws ServiceException;

  /**
   * 保存成功
   * 
   * @param id
   * @throws ServiceException
   */
  public void saveSuccess(Long id) throws ServiceException;

  /**
   * 
   * @param doc
   * @return
   * @throws ServiceException
   */

  public boolean validateXml(ImportPubXmlDocument doc) throws ServiceException;


  /**
   * 保存原始xml到dbcache_bfetch表
   * 
   * @param insId
   * @param pubYear
   * @param fileName
   * @param xml
   */
  public void saveXml(Long insId, Integer pubYear, String fileName, String xml);

  public Long saveOriginalPdwhPubRelation(Long xmlId, String seqNo, Long insId, long psnId, int recordFrom);

}
