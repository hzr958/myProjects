package com.smate.center.batch.service.pdwh.pubimport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.PubRegionExcludeDao;
import com.smate.center.batch.dao.pdwh.pub.PubRegionIncludeDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhInsNameDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubAddrExcDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubAssignDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubMaddrDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PubRegionExclude;
import com.smate.center.batch.model.pdwh.pub.PubRegionInclude;
import com.smate.center.batch.model.pdwh.pubimport.PdwhInsName;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubAddr;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubAddrExc;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubAssign;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubMaddr;
import com.smate.core.base.utils.data.XmlUtil;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;



/**
 * 成果地址匹配实现
 * 
 * @author zll
 *
 */
@Service("matchPubAddrsService")
@Transactional(rollbackFor = Exception.class)
public class MatchPubAddrsServiceImpl implements MatchPubAddrsService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPubAssignDao pdwhPubAssignDao;
  @Autowired
  private PdwhPubMaddrDao pdwhPubMaddrDao;
  @Autowired
  private PdwhInsNameDao pdwhInsNameDao;
  @Autowired
  private PubRegionExcludeDao pubRegionExcludeDao;
  @Autowired
  private PubRegionIncludeDao pubRegionIncludeDao;
  @Autowired
  private PdwhPubAddrExcDao pdwhPubAddrExcDao;
  // 缓存
  private Ehcache cache;


  @Override
  public int pubAddrMatchCache(Long insId, Long pubId, List<PdwhPubAddr> pubAddrs, Integer dbid) {
    try {
      // 匹配成果地址.
      int matched = this.matchPubAddrs(insId, pubId, pubAddrs, dbid);

      // 保存匹配结果
      PdwhPubAssign assign = pdwhPubAssignDao.getPdwhPubAssign(pubId, insId);
      if (assign == null) {
        assign = new PdwhPubAssign(pubId, insId, 1, matched, 0);
      } else {
        // 一直都是匹配上的状态
        if (assign.getResult() == 1 && matched == 1) {
          assign.setStatus(1);
          pdwhPubAssignDao.save(assign);
          return matched;
          // 之前匹配上，现在排除，需要删除单位成果
        } else if (assign.getResult() == 1 && matched != 1) {
          assign.setResult(matched);
          assign.setIsSend(9);
          // 之前未匹配上，现在匹配上
        } else {
          assign.setResult(matched);
          assign.setIsSend(0);
        }

      }
      assign.setStatus(1);
      pdwhPubAssignDao.save(assign);
      return matched;
    } catch (Exception e) {
      logger.error("匹配成果地址", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public int matchPubAddrs(Long insId, Long pubId, List<PdwhPubAddr> pubAddrs, Integer dbid) {
    if (CollectionUtils.isEmpty(pubAddrs)) {
      return 4;
    }
    List<PdwhPubMaddr> qmaddrs = pdwhPubMaddrDao.getPdwhPubMaddrs(pubId, insId);
    List<PdwhPubMaddr> maddrs = new ArrayList<PdwhPubMaddr>();
    for (PdwhPubAddr pubAddr : pubAddrs) {
      PdwhPubMaddr maddr = null;
      for (PdwhPubMaddr tmaddr : qmaddrs) {
        if (pubAddr.getAddrId().equals(tmaddr.getAddrId())) {
          maddr = tmaddr;
          maddr.setProtoAddr(pubAddr.getAddress().toLowerCase());
          if (dbid == 4 || dbid == 21) {
            // 还原为未匹配上
            maddr.setMatched(4);
          } else {
            // 还原为未确认
            maddr.setMatched(3);
          }
          maddrs.add(maddr);
        }
      }
      if (maddr == null) {
        maddr = new PdwhPubMaddr(pubAddr.getAddrId(), insId, pubId, pubAddr.getAddress(),
            pubAddr.getAddress().toLowerCase());
        if (dbid == 4 || dbid == 21) {
          // 还原为未匹配上
          maddr.setMatched(4);
        } else {
          // 还原为未确认
          maddr.setMatched(3);
        }
        maddrs.add(maddr);
      }
    }
    // 判断是否能匹配上本机构
    int matched = this.matchInsName(insId, maddrs, dbid);
    if (matched != 1 && dbid != 4 && dbid != 21) {
      // 跟FZQ讨论一下，暂时不跟其他机构对比，提高效率
      // 判断是否全部是其他机构地址
      // matched = this.matchOtherInsName(insId, maddrs);
      // 判断是否部分匹配上本机构
      if (matched == 3) {
        matched = this.matchPartInsName(insId, maddrs);
      } else {
        matched = 0;
      }
    }
    // 保存地址匹配结果.
    for (PdwhPubMaddr pdwhPubMaddr : maddrs) {
      this.pdwhPubMaddrDao.save(pdwhPubMaddr);
    }
    return matched;
  }

  @Override
  public int matchInsName(Long insId, List<PdwhPubMaddr> maddrs, Integer dbid) {
    int matched = 3;
    if (dbid == 4 || dbid == 21) {
      matched = 4;
    }
    PdwhInsName matchInsName = null;
    for (PdwhPubMaddr pubMaddr : maddrs) {
      if (dbid == 4 || dbid == 21) {
        matchInsName = this.PdwhCnkiInsNameMatch(pubMaddr.getProtoAddr(), insId, dbid);
      } else {
        matchInsName = this.PdwhInsNameMatch(pubMaddr.getProtoAddr(), insId, dbid);
      }
      if (matchInsName != null) {
        pubMaddr.setMatched(1);
        pubMaddr.setPdwhInsNameId(matchInsName.getId());
        pubMaddr.setMatchedInsId(insId);
        if (dbid != 4 && dbid != 21) {
          pubMaddr.setAddr(
              pubMaddr.getProtoAddr().replace(matchInsName.getInsName(), "<b>" + matchInsName.getInsName() + "</b>"));
        } else {
          pubMaddr.setAddr(pubMaddr.getProtoAddr());
        }
        matched = 1;
      }
    }

    // 如果有一个地址匹配上，其他地址的状态设置为不需要匹配0的状态
    if (matched == 1 && dbid != 4 && dbid != 21) {
      for (PdwhPubMaddr pubMaddr : maddrs) {
        if (pubMaddr.getMatched() == null || pubMaddr.getMatched() != 1) {
          pubMaddr.setMatched(0);
        }
      }
    }
    return matched;
  }

  @Override
  public PdwhInsName PdwhCnkiInsNameMatch(String ProtoAddr, Long insId, Integer dbid) {
    List<PdwhInsName> PdwhInsNameList = this.getPdwhInsName(insId, dbid);
    for (PdwhInsName pdwhInsName : PdwhInsNameList) {
      if (ProtoAddr.indexOf(pdwhInsName.getInsName()) > -1) {
        return pdwhInsName;
      }
    }
    return null;
  }

  @Override
  public PdwhInsName PdwhInsNameMatch(String protoAddr, Long insId, Integer dbid) {

    try {
      /// 匹配排除的机构别名.
      PdwhPubAddrExc addrExc = this.matchExcAddr(insId, protoAddr);
      if (addrExc != null) {
        return null;
      }
      // 整理成果地址
      protoAddr = this.clearMatchPubAddr(protoAddr);

      // 匹配是否是国外成果，如果是，直接排除
      if (this.matchedExcCtyName(protoAddr, insId)) {
        return null;
      }
      // 匹配机构别名
      List<PdwhInsName> pdwhInsNameList = this.getPdwhInsName(insId, dbid);
      outerLoop2: for (PdwhInsName pdwhInsName : pdwhInsNameList) {
        String[] insNames = StringUtils.split(pdwhInsName.getInsName(), ",");
        // 匹配机构名，如果全部匹配上，直接返回匹配上的机构别名
        for (String insName : insNames) {
          // 前后拼接逗号
          insName = "," + insName + ",";
          if (protoAddr.indexOf(insName) == -1) {
            continue outerLoop2;
          }
        }
        return pdwhInsName;
      }
      // 未匹配上
      return null;
    } catch (Exception e) {
      logger.error("传入成果地址，进行单位别名匹配", e);
      throw new ServiceException(e);
    }
  }


  @Override
  public boolean matchedExcCtyName(String protoAddr, Long insId) {
    List<PubRegionExclude> nameList = getRegionExclude();
    if (nameList == null || nameList.size() == 0) {
      return false;
    }
    // 去除结尾的字符
    while ((protoAddr.endsWith(",") || protoAddr.endsWith(".")) && protoAddr.length() > 2) {
      protoAddr = protoAddr.substring(0, protoAddr.length() - 1);
    }
    // 先判断是否是单位所在国家
    List<String> regionList = this.getRegionInclude(insId);
    if (CollectionUtils.isNotEmpty(regionList)) {
      for (String region : regionList) {
        // 以空格+国家名结尾
        if (protoAddr.endsWith(" " + region) || protoAddr.endsWith("," + region)) {
          return false;
        }
      }
    }
    // 判断是否国外成果
    for (PubRegionExclude name : nameList) {
      // 以空格+国家名结尾
      if (protoAddr.endsWith(" " + name.getName()) || protoAddr.endsWith("," + name.getName())) {
        return true;
      }
    }
    return false;
  }


  @Override
  public List<String> getRegionInclude(Long insId) {
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


  @Override
  public List<PubRegionExclude> getRegionExclude() {
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
  public PdwhPubAddrExc matchExcAddr(Long insId, String protoAddr) {
    try {
      List<PdwhPubAddrExc> list = this.getPdwhExcAddr(insId);
      if (list == null || list.size() == 0) {
        return null;
      }
      Integer hash = protoAddr.hashCode();
      for (PdwhPubAddrExc exc : list) {
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


  @SuppressWarnings("unchecked")
  private List<PdwhPubAddrExc> getPdwhExcAddr(Long insId) {
    try {
      String cacheKey = "PDWH_EXC_KEY_" + insId;
      List<PdwhPubAddrExc> excAddrList = (List<PdwhPubAddrExc>) this.getCache(cacheKey);
      if (excAddrList == null) {
        excAddrList = pdwhPubAddrExcDao.getExcAddrHash(insId);
        this.putCache(cacheKey, excAddrList);
      }
      return excAddrList;
    } catch (Exception e) {

      logger.error("获取排除的成果地址列表", e);
      throw new ServiceException(e);
    }
  }


  private String clearMatchPubAddr(String protoAddr) {
    // 成果地址匹配预处理
    protoAddr = XmlUtil.clearEnPubAddrMatchSpecChar(protoAddr);
    // 前后拼接逗号
    protoAddr = "," + protoAddr + ",";
    return protoAddr;
  }


  @SuppressWarnings("unchecked")
  private List<PdwhInsName> PdwhInsName(Long insId) {
    try {
      String cacheKey = "CNKI_KEY_" + insId;
      List<PdwhInsName> pdwhInsNameList = (List<PdwhInsName>) this.getCache(cacheKey);
      if (pdwhInsNameList == null) {
        pdwhInsNameList = pdwhInsNameDao.getPdwhInsName(insId);
        this.putCache(cacheKey, pdwhInsNameList);
      }
      return pdwhInsNameList;
    } catch (Exception e) {
      logger.error("获取单位名称数据出错", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 判断成果是否是部分匹配机构别名,进入条件：不确定是否是机构成果.
   * 
   * @param insId
   * @param maddrs
   * @return
   * @throws ServiceException
   */
  private int matchPartInsName(Long insId, List<PdwhPubMaddr> maddrs) throws ServiceException {
    // 默认状态：不确定是否是机构成果.
    int matched = 3;
    for (PdwhPubMaddr org : maddrs) {
      if (org.getMatched() != null && org.getMatched() != 3) {
        continue;
      }
      // 匹配其他机构别名
      Map<String, Object> map = this.pdwhInsNameMatchPart(org.getProtoAddr(), insId);
      if (map != null) {
        PdwhInsName pdwhInsName = (PdwhInsName) map.get("pdwhInsName");
        String showName = (String) map.get("showName");
        org.setMatched(2);
        org.setPdwhInsNameId(pdwhInsName.getId());
        org.setMatchedInsId(null);
        org.setAddr(showName);
        // 部分匹配上机构成果地址
        matched = 2;
      }

    }
    return matched;
  }

  private Map<String, Object> pdwhInsNameMatchPart(String protoAddr, Long insId) {
    try {

      Map<String, Object> map = new HashMap<String, Object>();
      // 匹配机构别名
      List<PdwhInsName> pdwhInsNameList = this.getPdwhInsName(insId, 0);
      outerLoop2: for (PdwhInsName pdwhInsName : pdwhInsNameList) {
        List<String> showNameList = new ArrayList<String>();
        // 拆分机构名，如果全部部分匹配上，直接返回匹配上的机构别名
        String[] insNames = StringUtils.split(pdwhInsName.getInsName(), ",");
        for (String insName : insNames) {
          if (protoAddr.indexOf(insName) < 0) {
            continue outerLoop2;
          }
          String showMatchInsName = protoAddr.replace(insName, "<b>" + insName + "</b>");
          showNameList.add(showMatchInsName);
        }
        map.put("pdwhInsName", pdwhInsName);
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
  @SuppressWarnings("unchecked")
  public List<PdwhInsName> getPdwhInsName(Long insId, Integer dbid) {
    try {
      String cacheKey = "pdwh_KEY_" + insId;
      List<PdwhInsName> pdwhInsNameList = (List<PdwhInsName>) this.getCache(cacheKey);
      if (pdwhInsNameList == null) {
        pdwhInsNameList = pdwhInsNameDao.getPdwhInsName(insId);
        // 别名进行预处理一下
        if (dbid != 4 && dbid != 21) {
          for (PdwhInsName pdwhInsName : pdwhInsNameList) {
            pdwhInsName.setInsName(XmlUtil.clearEnPubAddrMatchSpecChar(pdwhInsName.getInsName()));
          }
        }
        this.putCache(cacheKey, pdwhInsNameList);
      }
      return pdwhInsNameList;
    } catch (Exception e) {

      logger.error("获取单位别名数据", e);
      throw new ServiceException(e);
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
