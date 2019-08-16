package com.smate.center.batch.service.pdwh.pubmatch;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiInsNameDao;
import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiInsNameRegDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiInsName;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiInsNameReg;

/**
 * 单位cnki名称匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("cnkiInsNameMatchService")
@Transactional(rollbackFor = Exception.class)
public class CnkiInsNameMatchServiceImpl implements CnkiInsNameMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = -6837252706038001153L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CnkiInsNameRegDao cnkiInsNameRegDao;
  @Autowired
  private CnkiInsNameDao cnkiInsNameDao;
  // 缓存
  private Ehcache cache;

  @Override
  public List<CnkiInsName> cnkiNameMatch(String pubAddr) throws ServiceException {

    List<CnkiInsName> matchResult = new ArrayList<CnkiInsName>();
    List<CnkiInsName> allCnkiInsNameList = this.getAllCnkiInsName();
    for (CnkiInsName cnkiInsName : allCnkiInsNameList) {
      // 正则匹配
      if (pubAddr != null && pubAddr.indexOf(cnkiInsName.getLowerName()) > -1) {
        // 第一次匹配上
        if (matchResult.size() == 0) {
          matchResult.add(cnkiInsName);
        } else {// 已经存在
          int beforeLength = matchResult.get(0).getCnkinLength();
          // 长度相同，添加
          if (beforeLength == cnkiInsName.getCnkinLength()) {
            // 判断是否存在
            boolean flag = true;
            for (CnkiInsName before : matchResult) {
              if (cnkiInsName.getInsId().equals(before.getInsId())) {
                flag = false;
                break;
              }
            }
            if (flag) {
              matchResult.add(cnkiInsName);
            }
            // 替换之前匹配上的
          } else if (beforeLength < cnkiInsName.getCnkinLength()) {
            matchResult.clear();
            matchResult.add(cnkiInsName);
          }
        }
      }
    }
    return matchResult;
  }

  @Override
  public boolean cnkiNameMatchProtoAddr(String pubAddrs, Long insId) throws ServiceException {

    CnkiInsName insName = this.cnkiNameMatch(pubAddrs, insId);
    if (insName != null) {
      return true;
    }
    return false;
  }

  @Override
  public CnkiInsName cnkiNameMatchAll(String pubAddr, Long currentInsId) throws ServiceException {
    CnkiInsName matchCnkiName = this.cnkiNameMatch(pubAddr, currentInsId);
    if (matchCnkiName != null) {
      return matchCnkiName;
    }
    matchCnkiName = this.cnkiNameMatchOther(pubAddr, currentInsId);
    return matchCnkiName;
  }

  @Override
  public CnkiInsName cnkiNameMatchOther(String pubAddr, Long excInsId) throws ServiceException {

    List<Long> insIdList = this.getCnkiInsId();
    for (Long insId : insIdList) {
      if (excInsId.equals(insId)) {
        continue;
      } else {
        CnkiInsName cnkiInsName = this.cnkiNameMatch(pubAddr, insId);
        if (cnkiInsName != null) {
          return cnkiInsName;
        }
      }
    }
    return null;
  }

  @Override
  public CnkiInsName cnkiNameMatch(String pubAddr, Long insId) throws ServiceException {

    List<CnkiInsName> cnkiInsNameList = this.getCnkiInsName(insId);
    for (CnkiInsName cnkiInsName : cnkiInsNameList) {
      if (pubAddr.indexOf(cnkiInsName.getLowerName()) > -1) {
        // 增加匹配上的次数
        // this.cnkiInsNameDao.increateFreq(cnkiInsName.getId());
        return cnkiInsName;
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CnkiInsNameReg> getAllCnkiInsNameReg() throws ServiceException {
    try {
      String cacheKey = "CNKI_INSNAME_REG_CACHE_KEY";
      List<CnkiInsNameReg> allInsNameReg = (List<CnkiInsNameReg>) this.getCache(cacheKey);
      if (allInsNameReg == null) {
        allInsNameReg = cnkiInsNameRegDao.getAll();
        this.putCache(cacheKey, allInsNameReg);
      }
      return allInsNameReg;
    } catch (Exception e) {
      logger.error("获取所有的单位CNKI名称正则数据", e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CnkiInsName> getAllCnkiInsName() throws ServiceException {
    try {
      String cacheKey = "CNKI_KEY_ALL";
      List<CnkiInsName> allInsNameList = (List<CnkiInsName>) this.getCache(cacheKey);
      if (allInsNameList == null) {
        allInsNameList = cnkiInsNameDao.getAll();
        this.putCache(cacheKey, allInsNameList);
      }
      return allInsNameList;
    } catch (Exception e) {
      logger.error("获取所有的单位CNKI名称数据", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<CnkiInsNameReg> getCnkiInsNameReg(Long insId) throws ServiceException {
    try {
      return cnkiInsNameRegDao.getCnkiInsNameReg(insId);
    } catch (Exception e) {

      logger.error("获取单位CNKI名称正则数据", e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CnkiInsName> getCnkiInsName(Long insId) throws ServiceException {
    try {
      String cacheKey = "CNKI_KEY_" + insId;
      List<CnkiInsName> insNameList = (List<CnkiInsName>) this.getCache(cacheKey);
      if (insNameList == null) {
        insNameList = cnkiInsNameDao.getCnkiInsName(insId);
        this.putCache(cacheKey, insNameList);
      }
      return insNameList;
    } catch (Exception e) {

      logger.error("获取单位CNKI名称数据", e);
      throw new ServiceException(e);
    }
  }

  public List<Long> getCnkiInsId() throws ServiceException {
    try {
      String cacheKey = "CNKI_INSID_KEY";
      @SuppressWarnings("unchecked")
      List<Long> insIdList = (List<Long>) this.getCache(cacheKey);
      if (insIdList == null) {
        insIdList = cnkiInsNameDao.getAllInsId();
        this.putCache(cacheKey, insIdList);
      }
      return insIdList;
    } catch (Exception e) {

      logger.error("获取获取所有机构ID", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public CnkiInsName saveCnkiInsName(Long insId, String insName) throws ServiceException {
    try {
      insName = StringUtils.trimToNull(insName);
      if (insName == null) {
        return null;
      }
      insName = StringUtils.substring(insName, 0, 200);
      CnkiInsName cnkiInsName = this.cnkiInsNameDao.saveCnkiInsName(insId, insName);
      if (cnkiInsName != null)
        this.clearCnkiInsNameCache(insId);
      return cnkiInsName;
    } catch (Exception e) {
      logger.error("保存ISI机构别名,BPO调用", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void syncCnkiInsName(CnkiInsName insName) throws ServiceException {
    try {
      this.cnkiInsNameDao.save(insName);
      this.clearCnkiInsNameCache(insName.getInsId());
    } catch (Exception e) {
      logger.error("接收同步cnki机构别名，BPO->ROL", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 清除ISI别名缓存.
   * 
   * @param insId
   * @throws ServiceException
   */
  private void clearCnkiInsNameCache(Long insId) throws ServiceException {
    try {
      String cacheKey = "CNKI_KEY_" + insId;
      cache.remove(cacheKey);
    } catch (Exception e) {

      logger.error("清除CNKI别名缓存", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void initCnkiInsName() throws ServiceException {
    List<Long> insIdList = this.getCnkiInsId();
    for (Long insId : insIdList) {
      this.getCnkiInsName(insId);
    }
  }

  /**
   * 将数据加入缓存.
   * 
   * @param psnId
   * @param folderId
   * @param count
   */
  private void putCache(String cacheKey, Object obj) {
    Element element = new Element(cacheKey, obj);
    this.cache.put(element);
  }

  /**
   * 获取缓存数据.
   * 
   * @param psnId
   * @param folderId
   * @return
   */
  private Object getCache(String cacheKey) throws ServiceException {

    Element element = cache.get(cacheKey);
    if (element != null) {
      return element.getValue();
    }
    return null;
  }

  public void setCache(Ehcache cache) {
    this.cache = cache;
  }

}
