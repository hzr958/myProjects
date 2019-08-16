package com.smate.center.batch.service.pdwh.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.DbCache;
import com.smate.center.batch.model.pdwh.pub.DbCachePfetch;

/**
 * 临时文献库服务.
 * 
 * @author liqinghua
 * 
 */
public interface DbCacheService extends Serializable {

  /**
   * 批量获取DbCache.
   * 
   * @param startId
   * @param size
   * @return
   */
  public List<DbCache> getDbCacheBatch(Long startId, int size) throws ServiceException;

  /**
   * 删除临时Dbcache.
   * 
   * @param xmlIds
   */
  public void removeDbCache(List<Long> xmlIds) throws ServiceException;

  /**
   * 保存错误信息.
   * 
   * @param e
   * @param xmlId
   * @throws ServiceException
   */
  public void saveDbCacheError(Exception e, Long xmlId);

  /**
   * 批量获取个人抓取成果XML临时存储表.
   * 
   * @param startId
   * @param size
   * @return
   */
  public List<DbCachePfetch> getDbCachePfetchBatch(Long startId, int size) throws ServiceException;

  /**
   * 删除临时个人抓取成果XML临时存储表.
   * 
   * @param xmlIds
   */
  public void removeDbCachePfetch(List<Long> xmlIds) throws ServiceException;

  /**
   * 标记未校验通过的xmlid.
   * 
   * @param xmlIds
   * @throws ServiceException
   */
  public void remarkUnValidate(List<Long> xmlIds) throws ServiceException;

  /**
   * 保存错误信息.
   * 
   * @param error
   */
  public void saveDbCachePfetchError(Exception e, Long xmlId);

}
