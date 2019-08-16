package com.smate.center.batch.service.pdwh.pub;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.DbCacheDao;
import com.smate.center.batch.dao.pdwh.pub.DbCachePfetchDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.DbCache;
import com.smate.center.batch.model.pdwh.pub.DbCacheError;
import com.smate.center.batch.model.pdwh.pub.DbCachePfetch;
import com.smate.center.batch.model.pdwh.pub.DbCachePfetchError;

/**
 * 临时文献库服务.
 * 
 * @author liqinghua
 * 
 */
@Service("dbCacheService")
@Transactional(rollbackFor = Exception.class)
public class DbCacheServiceImpl implements DbCacheService {

  /**
   * 
   */
  private static final long serialVersionUID = 4024741010256948523L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DbCacheDao dbCacheDao;
  @Autowired
  private DbCachePfetchDao dbCachePfetchDao;

  @Override
  public List<DbCache> getDbCacheBatch(Long startId, int size) throws ServiceException {

    try {
      return dbCacheDao.getDbCacheBatch(startId, size);
    } catch (Exception e) {
      logger.error("批量获取DbCache", e);
      throw new ServiceException("批量获取DbCache", e);
    }
  }

  @Override
  public void removeDbCache(List<Long> xmlIds) throws ServiceException {
    try {
      dbCacheDao.removeDbCache(xmlIds);
    } catch (Exception e) {
      logger.error("删除临时Dbcache", e);
      throw new ServiceException("删除临时Dbcache", e);
    }
  }

  @Override
  public void saveDbCacheError(Exception e, Long xmlId) {
    try {
      // 获取错误信息
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      String errorMsg = StringUtils.substring(sw.toString(), 0, 2000);
      DbCacheError error = new DbCacheError(xmlId, errorMsg);
      this.dbCacheDao.saveErrorMsg(error);
    } catch (Exception e1) {
      logger.error("保存DbCache错误信息.", e);
    }
  }

  @Override
  public List<DbCachePfetch> getDbCachePfetchBatch(Long startId, int size) throws ServiceException {
    try {
      return dbCachePfetchDao.getDbCachePfetchBatch(startId, size);
    } catch (Exception e) {
      logger.error("批量获取个人抓取成果XML临时存储表", e);
      throw new ServiceException("批量获取个人抓取成果XML临时存储表", e);
    }
  }

  @Override
  public void removeDbCachePfetch(List<Long> xmlIds) throws ServiceException {
    try {
      dbCachePfetchDao.removeDbCachePfetch(xmlIds);
    } catch (Exception e) {
      logger.error("删除临时个人抓取成果XML临时存储表", e);
      throw new ServiceException("删除临时个人抓取成果XML临时存储表", e);
    }
  }

  @Override
  public void remarkUnValidate(List<Long> xmlIds) throws ServiceException {

    try {
      dbCachePfetchDao.remarkUnValidate(xmlIds);
    } catch (Exception e) {
      logger.error("标记未校验通过的xmlid", e);
      throw new ServiceException("标记未校验通过的xmlid", e);
    }

  }

  @Override
  public void saveDbCachePfetchError(Exception e, Long xmlId) {
    try {
      // 获取错误信息
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      String errorMsg = StringUtils.substring(sw.toString(), 0, 2000);
      DbCachePfetchError error = new DbCachePfetchError(xmlId, errorMsg);
      this.dbCachePfetchDao.saveErrorMsg(error);
    } catch (Exception e1) {
      logger.error("保存DbCachePfetch错误信息.", e);
    }
  }

}
