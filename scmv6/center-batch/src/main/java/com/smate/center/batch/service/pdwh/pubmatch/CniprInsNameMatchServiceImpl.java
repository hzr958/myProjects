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
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.cnipr.CniprInsNameDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprInsName;


/**
 * 单位cnipr名称匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("cniprInsNameMatchService")
@Transactional(rollbackFor = Exception.class)
public class CniprInsNameMatchServiceImpl implements CniprInsNameMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = -8825733484306157708L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CniprInsNameDao cniprInsNameDao;
  // 缓存
  private Ehcache cache;

  @Override
  public List<CniprInsName> cniprNameMatch(String pubAddr) throws ServiceException {

    List<CniprInsName> matchResult = new ArrayList<CniprInsName>();
    List<CniprInsName> allCniprInsNameList = this.getAllCniprInsName();
    for (CniprInsName cniprInsName : allCniprInsNameList) {
      // 正则匹配
      if (pubAddr != null && pubAddr.indexOf(cniprInsName.getLowerName()) > -1) {
        // 第一次匹配上
        if (matchResult.size() == 0) {
          matchResult.add(cniprInsName);
        } else {// 已经存在
          int beforeLength = matchResult.get(0).getCniprnLength();
          // 长度相同，添加
          if (beforeLength == cniprInsName.getCniprnLength()) {
            // 判断是否存在
            boolean flag = true;
            for (CniprInsName before : matchResult) {
              if (cniprInsName.getInsId().equals(before.getInsId())) {
                flag = false;
                break;
              }
            }
            if (flag) {
              matchResult.add(cniprInsName);
            }
            // 替换之前匹配上的
          } else if (beforeLength < cniprInsName.getCniprnLength()) {
            matchResult.clear();
            matchResult.add(cniprInsName);
          }
        }
      }
    }
    return matchResult;
  }

  @Override
  public boolean cniprNameMatchProtoAddr(String pubAddrs, Long insId) throws ServiceException {

    CniprInsName insName = this.cniprNameMatch(pubAddrs, insId);
    if (insName != null) {
      return true;
    }
    return false;
  }

  @Override
  public CniprInsName cniprNameMatchAll(String pubAddr, Long currentInsId) throws ServiceException {
    CniprInsName matchCniprName = this.cniprNameMatch(pubAddr, currentInsId);
    if (matchCniprName != null) {
      return matchCniprName;
    }
    matchCniprName = this.cniprNameMatchOther(pubAddr, currentInsId);
    return matchCniprName;
  }

  @Override
  public CniprInsName cniprNameMatchOther(String pubAddr, Long excInsId) throws ServiceException {

    List<Long> insIdList = this.getCniprInsId();
    for (Long insId : insIdList) {
      if (excInsId.equals(insId)) {
        continue;
      } else {
        CniprInsName cniprInsName = this.cniprNameMatch(pubAddr, insId);
        if (cniprInsName != null) {
          return cniprInsName;
        }
      }
    }
    return null;
  }

  @Override
  public CniprInsName cniprNameMatch(String pubAddr, Long insId) throws ServiceException {

    List<CniprInsName> cniprInsNameList = this.getCniprInsName(insId);
    for (CniprInsName cniprInsName : cniprInsNameList) {
      if (pubAddr.indexOf(cniprInsName.getLowerName()) > -1) {
        // 增加匹配上的次数
        // this.cniprInsNameDao.increateFreq(cniprInsName.getId());
        return cniprInsName;
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CniprInsName> getAllCniprInsName() throws ServiceException {
    try {
      String cacheKey = "CNIPR_KEY_ALL";
      List<CniprInsName> allInsNameList = (List<CniprInsName>) this.getCache(cacheKey);
      if (allInsNameList == null) {
        allInsNameList = cniprInsNameDao.getAll();
        this.putCache(cacheKey, allInsNameList);
      }
      return allInsNameList;
    } catch (Exception e) {
      logger.error("获取所有的单位CNIPR名称数据", e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CniprInsName> getCniprInsName(Long insId) throws ServiceException {
    try {
      String cacheKey = "CNIPR_KEY_" + insId;
      List<CniprInsName> insNameList = (List<CniprInsName>) this.getCache(cacheKey);
      if (insNameList == null) {
        insNameList = cniprInsNameDao.getCniprInsName(insId);
        this.putCache(cacheKey, insNameList);
      }
      return insNameList;
    } catch (Exception e) {

      logger.error("获取单位CNIPR名称数据", e);
      throw new ServiceException(e);
    }
  }

  public List<Long> getCniprInsId() throws ServiceException {
    try {
      String cacheKey = "CNIPR_INSID_KEY";
      @SuppressWarnings("unchecked")
      List<Long> insIdList = (List<Long>) this.getCache(cacheKey);
      if (insIdList == null) {
        insIdList = cniprInsNameDao.getAllInsId();
        this.putCache(cacheKey, insIdList);
      }
      return insIdList;
    } catch (Exception e) {

      logger.error("获取获取所有机构ID", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public CniprInsName saveCniprInsName(Long insId, String insName) throws ServiceException {
    try {
      insName = StringUtils.trimToNull(insName);
      if (insName == null) {
        return null;
      }
      insName = StringUtils.substring(insName, 0, 200);
      CniprInsName cniprInsName = this.cniprInsNameDao.saveCniprInsName(insId, insName);
      if (cniprInsName != null)
        this.clearCniprInsNameCache(insId);
      return cniprInsName;
    } catch (Exception e) {
      logger.error("保存ISI机构别名,BPO调用", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void syncCniprInsName(CniprInsName insName) throws ServiceException {
    try {
      this.cniprInsNameDao.save(insName);
      this.clearCniprInsNameCache(insName.getInsId());
    } catch (Exception e) {
      logger.error("接收同步cnipr机构别名，BPO->ROL", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 清除ISI别名缓存.
   * 
   * @param insId
   * @throws ServiceException
   */
  private void clearCniprInsNameCache(Long insId) throws ServiceException {
    try {
      String cacheKey = "CNIPR_KEY_" + insId;
      cache.remove(cacheKey);
    } catch (Exception e) {

      logger.error("清除CNIPR别名缓存", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void initCniprInsName() throws ServiceException {
    List<Long> insIdList = this.getCniprInsId();
    for (Long insId : insIdList) {
      this.getCniprInsName(insId);
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
