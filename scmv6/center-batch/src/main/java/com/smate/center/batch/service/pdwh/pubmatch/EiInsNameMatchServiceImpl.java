package com.smate.center.batch.service.pdwh.pubmatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.PubRegionExcludeDao;
import com.smate.center.batch.dao.pdwh.pub.PubRegionIncludeDao;
import com.smate.center.batch.dao.pdwh.pub.ei.EiInsNameDao;
import com.smate.center.batch.dao.pdwh.pub.ei.EiPubAddrExcDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PubRegionExclude;
import com.smate.center.batch.model.pdwh.pub.PubRegionInclude;
import com.smate.center.batch.model.pdwh.pub.ei.EiInsName;
import com.smate.center.batch.model.pdwh.pub.ei.EiPubAddrExc;
import com.smate.core.base.utils.data.XmlUtil;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * 单位Ei名称匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("eiInsNameMatchService")
@Transactional(rollbackFor = Exception.class)
public class EiInsNameMatchServiceImpl implements EiInsNameMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = -4755296745491935193L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private EiInsNameDao eiInsNameDao;
  @Autowired
  private PubRegionExcludeDao pubRegionExcludeDao;
  @Autowired
  private PubRegionIncludeDao pubRegionIncludeDao;
  @Autowired
  private EiPubAddrExcDao eiPubAddrExcDao;
  // 缓存
  private Ehcache cache;

  @Override
  public EiInsName eiNameMatchAll(String pubAddr, Long currentInsId) throws ServiceException {

    // 判断是否能匹配上本机构
    EiInsName matchInsName = this.eiNameMatch(pubAddr, currentInsId, true);
    if (matchInsName != null) {
      return matchInsName;
    }
    return matchInsName;
  }

  @Override
  public boolean eiNameMatchProtoAddr(String pubAddrs, Long insId) throws ServiceException {

    // 获取机构名称列表，构造eiPubCacheOrgs对象
    Set<String> pubAddrSet = XmlUtil.parseEiPubAddrs(pubAddrs);
    for (String pubAddr : pubAddrSet) {
      EiInsName eiInsName = this.eiNameMatch(pubAddr, insId, false);
      if (eiInsName != null) {
        return true;
      }
    }
    return false;
  }

  @Override
  public EiInsName eiNameMatch(String pubAddr, Long insId) throws ServiceException {

    return this.eiNameMatch(pubAddr, insId, true);
  }

  private EiInsName eiNameMatch(String pubAddr, Long insId, boolean updateFreq) throws ServiceException {
    try {

      EiPubAddrExc addrExc = this.matchExcAddr(insId, pubAddr);
      if (addrExc != null) {
        return null;
      }
      // 整理成果地址
      pubAddr = clearMatchPubAddr(pubAddr);

      // 匹配是否是国外成果，如果是，直接排除
      if (this.matchedExcCtyName(pubAddr, insId)) {
        return null;
      }
      // 匹配机构别名
      List<EiInsName> eiInsNameList = this.getEiInsName(insId);
      outerLoop2: for (EiInsName eiInsName : eiInsNameList) {
        String[] eiNames = StringUtils.split(eiInsName.getEiName(), ",");
        // 匹配机构名，如果全部匹配上，直接返回匹配上的机构别名
        for (String eiName : eiNames) {
          // 前后拼接逗号
          eiName = "," + eiName + ",";
          if (pubAddr.indexOf(eiName) == -1) {
            continue outerLoop2;
          }
        }
        // if (updateFreq) {
        // // 更新匹配上的次数
        // this.eiInsNameDao.increateFreq(EiInsName.getId());
        // }
        return eiInsName;
      }
      // 未匹配上
      return null;
    } catch (Exception e) {
      logger.error("传入成果地址，进行单位别名匹配", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<String, Object> eiNameMatchPart(String pubAddr, Long insId) throws ServiceException {
    try {

      Map<String, Object> map = new HashMap<String, Object>();
      // 匹配机构别名
      List<EiInsName> eiInsNameList = this.getEiInsName(insId);
      outerLoop2: for (EiInsName eiInsName : eiInsNameList) {
        List<String> showNameList = new ArrayList<String>();
        // 拆分机构名，如果全部部分匹配上，直接返回匹配上的机构别名
        String[] eiNames = StringUtils.split(eiInsName.getEiName(), ",");
        for (String eiName : eiNames) {
          if (pubAddr.indexOf(eiName) < 0) {
            continue outerLoop2;
          }
          String showMatchEiName = pubAddr.replace(eiName, "<b>" + eiName + "</b>");
          showNameList.add(showMatchEiName);
        }
        map.put("eiInsName", eiInsName);
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
  public EiInsName eiNameMatchOther(String pubAddr, Long excInsId) throws ServiceException {

    List<Long> insIdList = this.getEiInsId();
    for (Long insId : insIdList) {
      if (excInsId.equals(insId)) {
        continue;
      } else {
        EiInsName eiInsName = this.eiNameMatch(pubAddr, insId, false);
        if (eiInsName != null) {
          return eiInsName;
        }
      }
    }
    return null;
  }

  @Override
  public EiPubAddrExc matchExcAddr(Long insId, String addr) throws ServiceException {
    try {
      List<EiPubAddrExc> list = this.getEiExcAddr(insId);
      if (list == null || list.size() == 0) {
        return null;
      }
      Integer hash = addr.hashCode();
      for (EiPubAddrExc exc : list) {
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

  /**
   * 获取排除的成果地址列表.
   * 
   * @param insId
   * @return
   * @throws ServiceException
   */
  private List<EiPubAddrExc> getEiExcAddr(Long insId) throws ServiceException {
    try {
      String cacheKey = "EI_EXC_KEY_" + insId;
      @SuppressWarnings("unchecked")
      List<EiPubAddrExc> excAddrList = (List<EiPubAddrExc>) this.getCache(cacheKey);
      if (excAddrList == null) {
        excAddrList = eiPubAddrExcDao.getExcAddrHash(insId);
        this.putCache(cacheKey, excAddrList);
      }
      return excAddrList;
    } catch (Exception e) {

      logger.error("获取排除的成果地址列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean matchedExcCtyName(String pubAddr, Long insId) throws ServiceException {

    List<PubRegionExclude> nameList = getRegionExclude();
    if (nameList == null || nameList.size() == 0) {
      return false;
    }
    // 去除结尾的字符
    while ((pubAddr.endsWith(",") || pubAddr.endsWith(".")) && pubAddr.length() > 2) {
      pubAddr = pubAddr.substring(0, pubAddr.length() - 1);
    }
    // 先判断是否是单位所在国家
    List<String> regionList = this.getRegionInclude(insId);
    if (CollectionUtils.isNotEmpty(regionList)) {
      for (String region : regionList) {
        // 以空格+国家名结尾
        if (pubAddr.endsWith(" " + region) || pubAddr.endsWith("," + region)) {
          return false;
        }
      }
    }
    // 判断是否国外成果
    for (PubRegionExclude name : nameList) {
      // 以空格+国家名结尾
      if (pubAddr.endsWith(" " + name.getName()) || pubAddr.endsWith("," + name.getName())) {
        return true;
      }
    }
    return false;
  }

  @Deprecated
  @Override
  public Map<String, Object> getNeedComfirmEiName(String pubAddr, Long insId) throws ServiceException {

    try {
      // 返回结果,key:result=0未找到，1找到需要确认别名
      // key:name需要确认的机构别名，key:showName用于显示的加粗机构别名
      Map<String, Object> result = new HashMap<String, Object>();
      result.put("result", 0);
      // 整理成果地址
      pubAddr = clearMatchPubAddr(pubAddr);

      // 挖取匹配部分的机构别名
      List<EiInsName> eiInsNameList = this.getEiInsName(insId);
      outerLoop: for (EiInsName eiInsName : eiInsNameList) {
        List<String> nameList = new ArrayList<String>();
        List<String> showNameList = new ArrayList<String>();
        String[] eiNames = StringUtils.split(eiInsName.getEiName(), ",");
        // 匹配机构名，如果全部匹配上，直接返回挖取的机构别名
        for (String eiName : eiNames) {
          String reg = ",[.[^,]]*" + eiName + "[.[^,]]*,";
          Pattern pt = Pattern.compile(reg);
          Matcher mt = pt.matcher(pubAddr);
          if (!mt.find()) {
            continue outerLoop;
          }
          // 去除前后逗号
          String matchEiName = mt.group().replaceAll("^,", "").replaceAll(",$", "");
          if (!nameList.contains(matchEiName)) {
            nameList.add(matchEiName);
            String showMatchEiName = matchEiName.replace(eiName, "<b>" + eiName + "</b>");
            showNameList.add(showMatchEiName);
          }
        }
        result.put("result", 1);
        result.put("name", StringUtils.join(nameList, ","));
        result.put("showName", StringUtils.join(showNameList, ","));
        return result;
      }
      // 未匹配上
      return result;
    } catch (Exception e) {
      logger.error("获取需要BPO确认的机构别名", e);
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
  public List<EiInsName> getEiInsName(Long insId) throws ServiceException {
    try {
      String cacheKey = "EI_KEY_" + insId;
      List<EiInsName> eiInsNameList = (List<EiInsName>) this.getCache(cacheKey);
      if (CollectionUtils.isEmpty(eiInsNameList)) {
        eiInsNameList = eiInsNameDao.getEiInsName(insId);
        // 别名进行预处理一下
        for (EiInsName eiInsName : eiInsNameList) {
          eiInsName.setEiName(XmlUtil.clearEnPubAddrMatchSpecChar(eiInsName.getEiName()));
        }
        this.putCache(cacheKey, eiInsNameList);
      }
      return eiInsNameList;
    } catch (Exception e) {

      logger.error("获取单位Ei别名数据", e);
      throw new ServiceException(e);
    }
  }

  public List<Long> getEiInsId() throws ServiceException {
    try {
      String cacheKey = "EI_INSID_KEY";
      @SuppressWarnings("unchecked")
      List<Long> insIdList = (List<Long>) this.getCache(cacheKey);
      if (insIdList == null) {
        insIdList = eiInsNameDao.getAllInsId();
        this.putCache(cacheKey, insIdList);
      }
      return insIdList;
    } catch (Exception e) {

      logger.error("获取获取所有机构ID", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PubRegionExclude> getRegionExclude() throws ServiceException {
    try {
      String cacheKey = "REGION_EXCLUDE";
      @SuppressWarnings("unchecked")
      List<PubRegionExclude> nameList = (List<PubRegionExclude>) this.getCache(cacheKey);
      if (nameList == null) {
        nameList = pubRegionExcludeDao.getAll();
        this.putCache(cacheKey, nameList);
      }
      return nameList;
    } catch (Exception e) {

      logger.error("获取匹配排除国外名称", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<String> getRegionInclude(Long insId) throws ServiceException {

    try {

      String cacheKey = "REGION_INCLUDE";
      @SuppressWarnings("unchecked")
      List<PubRegionInclude> nameList = (List<PubRegionInclude>) this.getCache(cacheKey);
      if (nameList == null) {
        nameList = pubRegionIncludeDao.getAll();
        this.putCache(cacheKey, nameList);
      }
      List<String> regionList = new ArrayList<String>();
      for (PubRegionInclude name : nameList) {
        if (insId.equals(name.getInsId())) {
          regionList.add(name.getName());
        }
      }
      return regionList;
    } catch (Exception e) {

      logger.error("获取匹配单位所在国家名称", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 清除Ei别名缓存.
   * 
   * @param insId
   * @throws ServiceException
   */
  private void clearEiInsNameCache(Long insId) throws ServiceException {
    try {
      String cacheKey = "EI_KEY_" + insId;
      cache.remove(cacheKey);
      cacheKey = "EI_EXC_KEY_" + insId;
      cache.remove(cacheKey);
    } catch (Exception e) {
      logger.error("清除Ei别名缓存", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public EiInsName saveEiInsName(Long insId, String insName) throws ServiceException {

    try {
      insName = XmlUtil.removeCommaBlank(StringUtils.trim(insName)).toLowerCase();
      insName = StringUtils.substring(insName, 0, 200);
      EiInsName eiInsName = this.eiInsNameDao.saveEiInsName(insId, insName);
      if (eiInsName != null) {
        this.clearEiInsNameCache(insId);
      }
      return eiInsName;
    } catch (Exception e) {
      logger.error("保存Ei机构别名,BPO调用", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void syncEiInsName(EiInsName insName) throws ServiceException {

    try {
      this.eiInsNameDao.save(insName);
      this.clearEiInsNameCache(insName.getInsId());
    } catch (Exception e) {
      logger.error("接收同步Ei机构别名，BPO->ROL", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void initEiInsName() throws ServiceException {
    List<Long> insIdList = this.getEiInsId();
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
          // Ei别名
          List<EiInsName> eiInsNameList = eiInsNameDao.getEiInsName(subInsIds);
          Map<Long, List<EiInsName>> mapInsName = new HashMap<Long, List<EiInsName>>();
          for (EiInsName insName : eiInsNameList) {
            Long insId = insName.getInsId();
            List<EiInsName> insNameList =
                mapInsName.get(insId) == null ? new ArrayList<EiInsName>() : mapInsName.get(insId);
            // 单位名进行预处理
            insName.setEiName(XmlUtil.clearEnPubAddrMatchSpecChar(insName.getEiName()));
            insNameList.add(insName);
            mapInsName.put(insId, insNameList);
          }
          // Ei排除地址
          List<EiPubAddrExc> excAddrList = eiPubAddrExcDao.getExcAddrHash(subInsIds);
          Map<Long, List<EiPubAddrExc>> mapExcAddr = new HashMap<Long, List<EiPubAddrExc>>();
          for (EiPubAddrExc excAddr : excAddrList) {
            Long insId = excAddr.getInsId();
            List<EiPubAddrExc> addrList =
                mapExcAddr.get(insId) == null ? new ArrayList<EiPubAddrExc>() : mapExcAddr.get(insId);
            addrList.add(excAddr);
            mapExcAddr.put(insId, addrList);
          }
          // 放入缓存
          for (Long insId : subInsIds) {
            mapInsName.keySet().iterator();
            String cacheKey = "EI_KEY_" + insId;
            this.putCache(cacheKey, mapInsName.get(insId));
            cacheKey = "EI_EXC_KEY_" + insId;
            this.putCache(cacheKey, mapExcAddr.get(insId));
          }
          // 清空之前的单位ID
          subInsIds.clear();
        } catch (Exception e) {
          logger.error("初始化单位Ei别名数据错误", e);
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
