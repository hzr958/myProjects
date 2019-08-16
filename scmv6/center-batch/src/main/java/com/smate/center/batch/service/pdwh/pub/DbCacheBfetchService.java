package com.smate.center.batch.service.pdwh.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.DbCacheBfetch;

/**
 * 批量抓取成果XML临时存储表.
 * 
 * @author liqinghua
 * 
 */
public interface DbCacheBfetchService extends Serializable {

  /**
   * 存储文件内容到XML中.
   * 
   * @param fileName
   * @param xml
   * @throws ServiceException
   */
  public void saveXml(Long insId, Integer pubYear, String fileName, String xml) throws ServiceException;

  /**
   * 保存错误信息.
   * 
   * @param id
   * @param message
   * @throws ServiceException
   */
  public void saveError(Long id, String message) throws ServiceException;

  /**
   * 保存成功信息.
   * 
   * @param id
   * @throws ServiceException
   */
  public void saveSuccess(Long id) throws ServiceException;

  /**
   * 获取待展开成果XML列表.
   * 
   * @param size
   * @return
   * @throws ServiceException
   */
  public List<DbCacheBfetch> loadExpandBatch(int size) throws ServiceException;

  /**
   * 保存展开成果XML到数据库.
   * 
   * @param insId
   * @param xmlData
   * @param pubYear
   * @throws ServiceException
   */
  public void saveExpandXml(Long insId, String xmlData, Integer pubYear) throws ServiceException;
}
