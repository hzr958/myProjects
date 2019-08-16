package com.smate.sie.center.task.pdwh.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.exception.PubException;
import com.smate.sie.center.task.dao.SieConstRefDbDao;
import com.smate.sie.center.task.model.SieConstRefDb;

/**
 * 第三方数据库表服务
 * 
 * @author jszhou
 *
 */
@Service("sieConstRefDbService")
@Transactional(rollbackFor = Exception.class)
public class SieConstRefDbServiceImpl implements SieConstRefDbService, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2897603666376766676L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieConstRefDbDao sieConstRefDbDao;

  /**
   * 获取所有数据列表.
   * 
   * @return
   * @throws PubException
   */
  public List<SieConstRefDb> getAllConstRefDb() throws PubException {

    try {
      return sieConstRefDbDao.getAllConstRefDb(LocaleContextHolder.getLocale());
    } catch (Exception e) {
      logger.error("sieConstRefDbDao获取所有数据列表错误：", e);
      throw new PubException(e);
    }
  }

  /**
   * 获取所有数据列表.
   * 
   * @return
   * @throws PubException
   */
  public List<SieConstRefDb> getAllLocaleConstRefDb() throws PubException {

    try {
      Locale locale = LocaleContextHolder.getLocale();
      List<SieConstRefDb> dbList = this.getAllConstRefDb();
      for (SieConstRefDb db : dbList) {
        if (Locale.US.equals(locale)) {
          db.setDbName(db.getEnUsName());
        } else {
          db.setDbName(db.getZhCnName());
        }
      }
      return dbList;
    } catch (Exception e) {
      logger.error("sieConstRefDbDao获取所有数据列表错误：", e);
      throw new PubException(e);
    }
  }

  /**
   * 获取单个指定ID的数据.
   * 
   * @param id
   * @return
   * @throws PubException
   */
  public SieConstRefDb getConstRefDbById(Long id) throws PubException {

    try {
      return sieConstRefDbDao.getConstRefDbById(id);
    } catch (DataException e) {
      logger.error("getConstRefDbById 获取单个指定ID的数据错误id：" + id, e);
      throw new PubException(e);
    }
  }

  /**
   * 根据类型获取对应可用文献库.
   * 
   * @author fanzhiqiang
   * @param dbType 需要查找的文献库类型
   * @return 文献库列表
   */
  public List<SieConstRefDb> getConstRefDbByType(String dbType) throws PubException {
    try {
      return sieConstRefDbDao.getConstRefDbByType(dbType, LocaleContextHolder.getLocale());

    } catch (DataException e) {
      logger.error("getConstRefDbByType 获取指定查找类型的文献库时发生错误，dbType:" + dbType, e);
      throw new PubException(e);
    }
  }

  @Override
  public SieConstRefDb getConstRefDbByCode(String sourceDbCode) throws PubException {
    try {
      if (StringUtils.isBlank(sourceDbCode)) {
        return null;
      }
      return this.sieConstRefDbDao.getConstRefDbByCode(sourceDbCode);
    } catch (DataException e) {
      logger.error("getConstRefDbByCode 获取指定查找类型的文献库时发生错误，sourceDbCode:" + sourceDbCode, e);
      throw new PubException(e);
    }
  }

  @Override
  public SieConstRefDb getConstImportRefDbByCode(String sourceDbCode) throws PubException {
    if (StringUtils.isNotBlank(sourceDbCode)) {
      sourceDbCode = sourceDbCode.trim();
      sourceDbCode = "wf".equals(sourceDbCode) ? "WanFang" : sourceDbCode;
      SieConstRefDb sourceDb = this.getConstRefDbByCode(sourceDbCode);
      return sourceDb;
    }
    return null;
  }

  @Override
  public void pullConstRefDbSyn(List<SieConstRefDb> list) throws PubException {
    logger.info("=======================同步ConstRefDb===============");
    try {
      this.sieConstRefDbDao.removeAll();
      for (SieConstRefDb constRefDb : list) {
        this.sieConstRefDbDao.save(constRefDb);
      }
    } catch (DataException e) {
      logger.error("pullConstRefDbSyn接收文献数据库定义同步错误.size:{}", list.size(), e);
      throw new PubException("pullConstRefDbSyn接收文献数据库定义同步错误.", e);
    }
  }

  @Override
  public Map<Long, String> findAllDBCode() throws PubException {
    List<SieConstRefDb> result = this.sieConstRefDbDao.getAll();
    Map<Long, String> resMap = new HashMap<Long, String>();
    for (SieConstRefDb constRefDb : result) {
      resMap.put(constRefDb.getId(), constRefDb.getCode());
    }
    return resMap;
  }

}
