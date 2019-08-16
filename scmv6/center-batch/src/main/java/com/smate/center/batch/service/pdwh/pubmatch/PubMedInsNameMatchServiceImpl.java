package com.smate.center.batch.service.pdwh.pubmatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.pubmed.PubmedInsNameDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.pubmed.PubmedInsName;
import com.smate.core.base.utils.data.XmlUtil;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * 单位pubmed名称匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("pubMedInsNameMatchService")
@Transactional(rollbackFor = Exception.class)
public class PubMedInsNameMatchServiceImpl implements PubMedInsNameMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = -1556548614925659983L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubmedInsNameDao pubMedInsNameDao;
  @Autowired
  private IsiInsNameMatchService isiInsNameMatchService;
  // 缓存
  private Ehcache cache;

  @Override
  public PubmedInsName pubMedNameMatchAll(String pubAddr, Long currentInsId) throws ServiceException {

    // 判断是否能匹配上本机构
    PubmedInsName matchInsName = this.pubMedNameMatch(pubAddr, currentInsId, true);
    if (matchInsName != null) {
      return matchInsName;
    }
    return matchInsName;
  }

  @Override
  public boolean pubMedNameMatchProtoAddr(String pubAddrs, Long insId) throws ServiceException {
    // 获取机构名称列表，构造PubMedPubCacheOrgs对象
    Set<String> pubAddrSet = XmlUtil.parseScopusPubAddrs(pubAddrs);
    for (String pubAddr : pubAddrSet) {
      PubmedInsName insName = this.pubMedNameMatch(pubAddr, insId, false);
      if (insName != null) {
        return true;
      }
    }
    return false;
  }

  @Override
  public PubmedInsName pubMedNameMatch(String pubAddr, Long insId) throws ServiceException {

    return this.pubMedNameMatch(pubAddr, insId, true);
  }

  private PubmedInsName pubMedNameMatch(String pubAddr, Long insId, boolean updateFreq) throws ServiceException {
    try {

      // 整理成果地址
      pubAddr = clearMatchPubAddr(pubAddr);

      // 匹配是否是国外成果，如果是，直接排除
      if (this.isiInsNameMatchService.matchedExcCtyName(pubAddr, insId)) {
        return null;
      }

      // 匹配机构别名
      List<PubmedInsName> pubMedInsNameList = this.getPubmedInsName(insId);
      outerLoop2: for (PubmedInsName pubMedInsName : pubMedInsNameList) {
        String[] pubMedNames = StringUtils.split(pubMedInsName.getPubmedName(), ",");
        // 匹配机构名，如果全部匹配上，直接返回匹配上的机构别名
        for (String pubMedName : pubMedNames) {
          // 前后拼接逗号
          pubMedName = "," + pubMedName + ",";
          if (pubAddr.indexOf(pubMedName) == -1) {
            continue outerLoop2;
          }
        }
        // if (updateFreq) {
        // // 更新匹配上的次数
        // this.pubMedInsNameDao.increateFreq(pubMedInsName.getId());
        // }
        return pubMedInsName;
      }
      // 未匹配上
      return null;
    } catch (Exception e) {
      logger.error("传入成果地址，进行单位别名匹配", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<String, Object> pubMedNameMatchPart(String pubAddr, Long insId) throws ServiceException {
    try {

      Map<String, Object> map = new HashMap<String, Object>();
      // 匹配机构别名
      List<PubmedInsName> pubMedInsNameList = this.getPubmedInsName(insId);
      outerLoop2: for (PubmedInsName pubMedInsName : pubMedInsNameList) {
        List<String> showNameList = new ArrayList<String>();
        // 拆分机构名，如果全部部分匹配上，直接返回匹配上的机构别名
        String[] pubMedNames = StringUtils.split(pubMedInsName.getPubmedName(), ",");
        for (String pubMedName : pubMedNames) {
          if (pubAddr.indexOf(pubMedName) < 0) {
            continue outerLoop2;
          }
          String showMatchPubMedName = pubAddr.replace(pubMedName, "<b>" + pubMedName + "</b>");
          showNameList.add(showMatchPubMedName);
        }
        map.put("pubMedInsName", pubMedInsName);
        map.put("showName", StringUtils.join(showNameList, ","));
        return map;
      }
      // 未匹配上
      return null;
    } catch (Exception e) {
      logger.error("传入成果地址，进行单位别名匹配", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubmedInsName pubMedNameMatchOther(String pubAddr, Long excInsId) throws ServiceException {

    List<Long> insIdList = this.getPubMedInsId();
    for (Long insId : insIdList) {
      if (excInsId.equals(insId)) {
        continue;
      } else {
        PubmedInsName pubMedInsName = this.pubMedNameMatch(pubAddr, insId, false);
        if (pubMedInsName != null) {
          return pubMedInsName;
        }
      }
    }
    return null;
  }

  private String clearMatchPubAddr(String pubAddr) {
    // 成果地址匹配预处理
    pubAddr = XmlUtil.clearEnPubAddrMatchSpecChar(pubAddr);
    // 前后拼接逗号
    pubAddr = "," + pubAddr + ",";
    return pubAddr;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<PubmedInsName> getPubmedInsName(Long insId) throws ServiceException {
    try {
      String cacheKey = "PUBMED_KEY_" + insId;
      List<PubmedInsName> pubMedInsNameList = (List<PubmedInsName>) this.getCache(cacheKey);
      if (pubMedInsNameList == null) {
        pubMedInsNameList = pubMedInsNameDao.getPubmedInsName(insId);
        for (PubmedInsName pubmedInsName : pubMedInsNameList) {
          // 名称进行预处理
          pubmedInsName.setPubmedName(XmlUtil.clearEnPubAddrMatchSpecChar(pubmedInsName.getPubmedName()));
        }
        this.putCache(cacheKey, pubMedInsNameList);
      }
      return pubMedInsNameList;
    } catch (Exception e) {

      logger.error("获取单位PUBMED别名数据", e);
      throw new ServiceException(e);
    }
  }

  public List<Long> getPubMedInsId() throws ServiceException {
    try {
      String cacheKey = "PUBMEDINSID_KEY";
      @SuppressWarnings("unchecked")
      List<Long> insIdList = (List<Long>) this.getCache(cacheKey);
      if (insIdList == null) {
        insIdList = pubMedInsNameDao.getAllInsId();
        this.putCache(cacheKey, insIdList);
      }
      return insIdList;
    } catch (Exception e) {

      logger.error("获取获取所有机构ID", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 清除PUBMED别名缓存.
   * 
   * @param insId
   * @throws ServiceException
   */
  private void clearIsInsNameCache(Long insId) throws ServiceException {
    try {
      String cacheKey = "PUBMEDKEY_" + insId;
      cache.remove(cacheKey);
      cacheKey = "PUBMEDEXC_KEY_" + insId;
      cache.remove(cacheKey);
    } catch (Exception e) {

      logger.error("清除PUBMED别名缓存", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubmedInsName savePubmedInsName(Long insId, String insName) throws ServiceException {

    try {
      insName = XmlUtil.removeCommaBlank(StringUtils.trim(insName)).toLowerCase();
      // SCOPUS全部点去除
      insName = insName.replace(".", " ");
      // 多个空格换成一个空格
      insName = insName.replaceAll("\\s+", " ");
      insName = StringUtils.substring(insName, 0, 200);
      PubmedInsName pubMedInsName = this.pubMedInsNameDao.savePubmedInsName(insId, insName);
      if (pubMedInsName != null)
        this.clearIsInsNameCache(insId);
      return pubMedInsName;
    } catch (Exception e) {
      logger.error("保存PUBMED机构别名,BPO调用", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void syncPubmedInsName(PubmedInsName insName) throws ServiceException {

    try {
      this.pubMedInsNameDao.save(insName);
      this.clearIsInsNameCache(insName.getInsId());
    } catch (Exception e) {
      logger.error("接收同步PUBMED机构别名，BPO->ROL", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void initPubmedInsName() throws ServiceException {
    List<Long> insIdList = this.getPubMedInsId();
    List<Long> subInsIds = new ArrayList<Long>();

    int fetchSize = 0;
    for (int i = 0; i <= insIdList.size(); i++) {

      if (i < insIdList.size()) {
        subInsIds.add(insIdList.get(i));
        fetchSize++;
      }

      if (fetchSize == 100 || i == insIdList.size()) {
        fetchSize = 0;
        try {

          // 别名
          List<PubmedInsName> pubMedInsNameList = pubMedInsNameDao.getPubmedInsName(subInsIds);
          Map<Long, List<PubmedInsName>> mapInsName = new HashMap<Long, List<PubmedInsName>>();
          for (PubmedInsName insName : pubMedInsNameList) {
            Long insId = insName.getInsId();
            List<PubmedInsName> insNameList =
                mapInsName.get(insId) == null ? new ArrayList<PubmedInsName>() : mapInsName.get(insId);
            // 名称进行预处理
            insName.setPubmedName(XmlUtil.clearEnPubAddrMatchSpecChar(insName.getPubmedName()));
            insNameList.add(insName);
            mapInsName.put(insId, insNameList);
          }
          // 放入缓存
          for (Long insId : subInsIds) {
            mapInsName.keySet().iterator();
            String cacheKey = "PUBMED_KEY_" + insId;
            this.putCache(cacheKey, mapInsName.get(insId));
          }
          // 清空之前的单位ID
          subInsIds.clear();
        } catch (Exception e) {
          logger.error("初始化单位scopus别名数据错误", e);
        }
      }
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
