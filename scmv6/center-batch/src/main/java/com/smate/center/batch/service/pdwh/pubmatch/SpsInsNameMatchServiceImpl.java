package com.smate.center.batch.service.pdwh.pubmatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.sps.SpsInsNameDao;
import com.smate.center.batch.dao.pdwh.pub.sps.SpsPubAddrExcDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.sps.SpsInsName;
import com.smate.center.batch.model.pdwh.pub.sps.SpsPubAddrExc;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * 单位scopus名称匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("spsInsNameMatchService")
@Transactional(rollbackFor = Exception.class)
public class SpsInsNameMatchServiceImpl implements SpsInsNameMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = -1556548614925659983L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SpsInsNameDao spsInsNameDao;
  @Autowired
  private SpsPubAddrExcDao spsPubAddrExcDao;
  @Autowired
  private IsiInsNameMatchService isiInsNameMatchService;
  // 缓存
  private Ehcache cache;

  @Override
  public SpsInsName spsNameMatchAll(String pubAddr, Long currentInsId) throws ServiceException {

    // 判断是否能匹配上本机构
    SpsInsName matchInsName = this.spsNameMatch(pubAddr, currentInsId, true);
    if (matchInsName != null) {
      return matchInsName;
    }
    matchInsName = this.spsNameMatchOther(pubAddr, currentInsId);

    return matchInsName;
  }

  @Override
  public boolean spsNameMatchProtoAddr(String pubAddrs, Long insId) throws ServiceException {
    // 获取机构名称列表，构造SpsPubCacheOrgs对象
    Set<String> pubAddrSet = XmlUtil.parseScopusPubAddrs(pubAddrs);
    for (String pubAddr : pubAddrSet) {
      SpsInsName insName = this.spsNameMatch(pubAddr, insId, false);
      if (insName != null) {
        return true;
      }
    }
    return false;
  }

  @Override
  public SpsInsName spsNameMatch(String pubAddr, Long insId) throws ServiceException {

    return this.spsNameMatch(pubAddr, insId, true);
  }

  private SpsInsName spsNameMatch(String pubAddr, Long insId, boolean updateFreq) throws ServiceException {
    try {

      SpsPubAddrExc addrExc = this.matchExcAddr(insId, pubAddr);
      if (addrExc != null) {
        return null;
      }
      // 整理成果地址
      pubAddr = clearMatchPubAddr(pubAddr);

      // 匹配是否是国外成果，如果是，直接排除
      if (this.isiInsNameMatchService.matchedExcCtyName(pubAddr, insId)) {
        return null;
      }

      // 匹配机构别名
      List<SpsInsName> spsInsNameList = this.getSpsInsName(insId);
      outerLoop2: for (SpsInsName spsInsName : spsInsNameList) {
        String[] spsNames = StringUtils.split(spsInsName.getSpsName(), ",");
        // 匹配机构名，如果全部匹配上，直接返回匹配上的机构别名
        for (String spsName : spsNames) {
          // 前后拼接逗号
          spsName = "," + spsName + ",";
          if (pubAddr.indexOf(spsName) == -1) {
            continue outerLoop2;
          }
        }
        // if (updateFreq) {
        // // 更新匹配上的次数
        // this.spsInsNameDao.increateFreq(spsInsName.getId());
        // }
        return spsInsName;
      }
      // 未匹配上
      return null;
    } catch (Exception e) {
      logger.error("传入成果地址，进行单位别名匹配", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<String, Object> spsNameMatchPart(String pubAddr, Long insId) throws ServiceException {
    try {

      Map<String, Object> map = new HashMap<String, Object>();
      // 匹配机构别名
      List<SpsInsName> spsInsNameList = this.getSpsInsName(insId);
      outerLoop2: for (SpsInsName spsInsName : spsInsNameList) {
        List<String> showNameList = new ArrayList<String>();
        // 拆分机构名，如果全部部分匹配上，直接返回匹配上的机构别名
        String[] spsNames = StringUtils.split(spsInsName.getSpsName(), ",");
        for (String spsName : spsNames) {
          if (pubAddr.indexOf(spsName) < 0) {
            continue outerLoop2;
          }
          String showMatchSpsName = pubAddr.replace(spsName, "<b>" + spsName + "</b>");
          showNameList.add(showMatchSpsName);
        }
        map.put("spsInsName", spsInsName);
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
  public SpsInsName spsNameMatchOther(String pubAddr, Long excInsId) throws ServiceException {

    List<Long> insIdList = this.getSpsInsId();
    for (Long insId : insIdList) {
      if (excInsId.equals(insId)) {
        continue;
      } else {
        SpsInsName spsInsName = this.spsNameMatch(pubAddr, insId, false);
        if (spsInsName != null) {
          return spsInsName;
        }
      }
    }
    return null;
  }

  @Override
  public SpsPubAddrExc matchExcAddr(Long insId, String addr) throws ServiceException {
    try {
      List<SpsPubAddrExc> list = this.getSpsExcAddr(insId);
      if (list == null || list.size() == 0) {
        return null;
      }
      Integer hash = addr.hashCode();
      for (SpsPubAddrExc exc : list) {
        if (hash.equals(exc.getAddrHash())) {
          return exc;
        }
      }
      return null;
    } catch (Exception e) {
      logger.error("判断成果地址是否是排除地址", e);
      throw new ServiceException(e);
    }
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
  @Deprecated
  public List<SpsInsName> getAllSpsInsName() throws ServiceException {
    try {
      String cacheKey = "SPS_KEY_ALL";
      List<SpsInsName> allSpsInsName = (List<SpsInsName>) this.getCache(cacheKey);
      if (allSpsInsName == null) {
        allSpsInsName = spsInsNameDao.getAll();
        this.putCache(cacheKey, allSpsInsName);
      }
      return allSpsInsName;
    } catch (Exception e) {

      logger.error("获取所有的单位SPS别名数据", e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<SpsInsName> getSpsInsName(Long insId) throws ServiceException {
    try {
      String cacheKey = "SPS_KEY_" + insId;
      List<SpsInsName> spsInsNameList = (List<SpsInsName>) this.getCache(cacheKey);
      if (spsInsNameList == null) {
        spsInsNameList = spsInsNameDao.getSpsInsName(insId);
        for (SpsInsName spsInsName : spsInsNameList) {
          // 处理scopus别名用于匹配
          spsInsName.setSpsName(XmlUtil.clearEnPubAddrMatchSpecChar(spsInsName.getSpsName()));
        }
        this.putCache(cacheKey, spsInsNameList);
      }
      return spsInsNameList;
    } catch (Exception e) {

      logger.error("获取单位SPS别名数据", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取排除的成果地址列表.
   * 
   * @param insId
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  private List<SpsPubAddrExc> getSpsExcAddr(Long insId) throws ServiceException {
    try {
      String cacheKey = "SPS_EXC_KEY_" + insId;

      List<SpsPubAddrExc> excAddrList = (List<SpsPubAddrExc>) this.getCache(cacheKey);
      if (excAddrList == null) {
        excAddrList = spsPubAddrExcDao.getExcAddrHash(insId);
        this.putCache(cacheKey, excAddrList);
      }
      return excAddrList;
    } catch (Exception e) {
      logger.error("获取排除的成果地址列表", e);
      throw new ServiceException(e);
    }
  }

  public List<Long> getSpsInsId() throws ServiceException {
    try {
      String cacheKey = "SPS_INSID_KEY";
      @SuppressWarnings("unchecked")
      List<Long> insIdList = (List<Long>) this.getCache(cacheKey);
      if (insIdList == null) {
        insIdList = spsInsNameDao.getAllInsId();
        this.putCache(cacheKey, insIdList);
      }
      return insIdList;
    } catch (Exception e) {

      logger.error("获取获取所有机构ID", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 清除SPS别名缓存.
   * 
   * @param insId
   * @throws ServiceException
   */
  private void clearIsInsNameCache(Long insId) throws ServiceException {
    try {
      String cacheKey = "SPS_KEY_" + insId;
      cache.remove(cacheKey);
      cacheKey = "SPS_EXC_KEY_" + insId;
      cache.remove(cacheKey);
    } catch (Exception e) {

      logger.error("清除SPS别名缓存", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public SpsInsName saveSpsInsName(Long insId, String insName) throws ServiceException {

    try {
      insName = XmlUtil.removeCommaBlank(StringUtils.trim(insName)).toLowerCase();
      // SCOPUS全部点去除
      insName = insName.replace(".", " ");
      // 多个空格换成一个空格
      insName = insName.replaceAll("\\s+", " ");
      insName = StringUtils.substring(insName, 0, 200);
      SpsInsName spsInsName = this.spsInsNameDao.saveSpsInsName(insId, insName);
      if (spsInsName != null)
        this.clearIsInsNameCache(insId);
      return spsInsName;
    } catch (Exception e) {
      logger.error("保存SPS机构别名,BPO调用", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void syncSpsInsName(SpsInsName insName) throws ServiceException {

    try {
      this.spsInsNameDao.save(insName);
      this.clearIsInsNameCache(insName.getInsId());
    } catch (Exception e) {
      logger.error("接收同步SPS机构别名，BPO->ROL", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void initSpsInsName() throws ServiceException {
    List<Long> insIdList = this.getSpsInsId();
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
          List<SpsInsName> spsInsNameList = spsInsNameDao.getSpsInsName(subInsIds);
          Map<Long, List<SpsInsName>> mapInsName = new HashMap<Long, List<SpsInsName>>();
          for (SpsInsName insName : spsInsNameList) {
            Long insId = insName.getInsId();
            List<SpsInsName> insNameList =
                mapInsName.get(insId) == null ? new ArrayList<SpsInsName>() : mapInsName.get(insId);
            // 处理scopus别名用于匹配
            insName.setSpsName(XmlUtil.clearEnPubAddrMatchSpecChar(insName.getSpsName()));
            insNameList.add(insName);
            mapInsName.put(insId, insNameList);
          }

          // 排除地址
          List<SpsPubAddrExc> excAddrList = spsPubAddrExcDao.getExcAddrHash(subInsIds);
          Map<Long, List<SpsPubAddrExc>> mapExcAddr = new HashMap<Long, List<SpsPubAddrExc>>();
          for (SpsPubAddrExc excAddr : excAddrList) {
            Long insId = excAddr.getInsId();
            List<SpsPubAddrExc> addrList =
                mapExcAddr.get(insId) == null ? new ArrayList<SpsPubAddrExc>() : mapExcAddr.get(insId);
            addrList.add(excAddr);
            mapExcAddr.put(insId, addrList);
          }

          // 放入缓存
          for (Long insId : subInsIds) {
            mapInsName.keySet().iterator();
            String cacheKey = "SPS_KEY_" + insId;
            this.putCache(cacheKey, mapInsName.get(insId));
            cacheKey = "SPS_EXC_KEY_" + insId;
            this.putCache(cacheKey, mapExcAddr.get(insId));
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
