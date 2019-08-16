package com.smate.center.batch.service.pdwh.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.DbCacheBfetchDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.DbCacheBfetch;

/**
 * 批量抓取成果XML临时存储表.
 * 
 * @author liqinghua
 * 
 */
@Service("dbCacheBfetchService")
@Transactional(rollbackFor = Exception.class)
public class DbCacheBfetchServiceImpl implements DbCacheBfetchService {

  /**
   * 
   */
  private static final long serialVersionUID = 922900327912416415L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DbCacheBfetchDao dbCacheBfetchDao;

  @Override
  public void saveXml(Long insId, Integer pubYear, String fileName, String xml) throws ServiceException {
    try {
      DbCacheBfetch dbcf = new DbCacheBfetch(insId, xml, fileName, pubYear);
      dbCacheBfetchDao.save(dbcf);
    } catch (Exception e) {
      logger.error("保存XML", e);
      throw new ServiceException("保存XML", e);
    }
  }

  @Override
  public void saveError(Long id, String message) throws ServiceException {
    try {

      dbCacheBfetchDao.saveError(id, message);
    } catch (Exception e) {
      logger.error("保存错误信息", e);
      throw new ServiceException("保存错误信息", e);
    }
  }

  @Override
  public void saveSuccess(Long id) throws ServiceException {
    try {

      dbCacheBfetchDao.saveSuccess(id);
    } catch (Exception e) {
      logger.error("保存成功信息", e);
      throw new ServiceException("保存成功信息", e);
    }
  }

  @Override
  public List<DbCacheBfetch> loadExpandBatch(int size) throws ServiceException {
    try {

      return dbCacheBfetchDao.loadExpandBatch(size);
    } catch (Exception e) {
      logger.error("获取待展开成果XML列表", e);
      throw new ServiceException("获取待展开成果XML列表", e);
    }
  }

  @Override
  public void saveExpandXml(Long insId, String xmlData, Integer pubYear) throws ServiceException {
    // TODO Auto-generated method stub

  }

}
