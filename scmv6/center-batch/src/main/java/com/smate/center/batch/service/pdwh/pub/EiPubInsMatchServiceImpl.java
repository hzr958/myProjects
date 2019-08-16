package com.smate.center.batch.service.pdwh.pub;

import com.smate.center.batch.dao.pdwh.pub.ei.*;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.ei.*;
import com.smate.center.batch.service.pdwh.pubmatch.EiInsNameMatchService;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * isi成果基准库批量抓取临时库匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("eiPubInsMatchService")
@Transactional(rollbackFor = Exception.class)
public class EiPubInsMatchServiceImpl implements EiPubInsMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = 6182414626692320403L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private EiInsNameMatchService eiInsNameMatchService;
  @Autowired
  private EiPubMaddrDao eiPubMaddrDao;
  @Autowired
  private EiPubAddrDao eiPubAddrDao;
  @Autowired
  private EiPubAssignDao eiPubAssignDao;
  @Autowired
  private EiPubAddrExcDao eiPubAddrExcDao;
  @Autowired
  private EiPublicationDao eiPublicationDao;

  @Override
  public Integer matchPubCache(Long insId, Long pubId) throws ServiceException {

    List<EiPubAddr> pubAddrs = eiPubAddrDao.getEiPubAddr(pubId);
    return this.matchPubCache(insId, pubId, pubAddrs);
  }

  @Override
  public Integer matchPubCache(Long insId, Long pubId, List<EiPubAddr> pubAddrs) throws ServiceException {

    try {
      // 匹配成果地址.
      int matched = matchPubAddrs(insId, pubId, pubAddrs);

      // 保存匹配结果
      EiPubAssign assign = eiPubAssignDao.getEiPubAssign(pubId, insId);
      if (assign == null) {
        assign = new EiPubAssign(pubId, insId, 1, matched, 0);
      } else {
        // 一直都是匹配上的状态
        if (assign.getResult() == 1 && matched == 1) {
          assign.setStatus(1);
          eiPubAssignDao.save(assign);
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
      eiPubAssignDao.save(assign);
      return matched;
    } catch (Exception e) {
      logger.error("匹配成果地址", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 匹配成果地址.
   * 
   * @param insId
   * @param pubId
   * @param pubAddrs
   * @return
   * @throws ServiceException
   */
  private int matchPubAddrs(Long insId, Long pubId, List<EiPubAddr> pubAddrs) throws ServiceException {

    if (CollectionUtils.isEmpty(pubAddrs)) {
      return 4;
    }
    List<EiPubMaddr> qmaddrs = eiPubMaddrDao.getEiPubMaddrs(pubId, insId);
    List<EiPubMaddr> maddrs = new ArrayList<EiPubMaddr>();
    for (EiPubAddr pubAddr : pubAddrs) {
      EiPubMaddr maddr = null;
      for (EiPubMaddr tmaddr : qmaddrs) {
        if (pubAddr.getAddrId().equals(tmaddr.getAddrId())) {
          maddr = tmaddr;
          maddr.setProtoAddr(pubAddr.getAddr());
          // 还原为未确认
          maddr.setMatched(3);
          maddrs.add(maddr);
        }
      }
      if (maddr == null) {
        maddr = new EiPubMaddr(pubAddr.getAddrId(), insId, pubId, pubAddr.getAddr(), pubAddr.getAddr());
        // 还原为未确认
        maddr.setMatched(3);
        maddrs.add(maddr);
      }
    }
    // 判断是否能匹配上本机构
    int matched = this.matchInsName(insId, maddrs);
    if (matched != 1) {
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
    for (EiPubMaddr pubMaddr : maddrs) {
      this.eiPubMaddrDao.save(pubMaddr);
    }
    return matched;
  }

  /**
   * 获取成果地址匹配上本机构的别名列表.
   * 
   * @param insId
   * @param pubMaddrs
   * @return
   * @throws ServiceException
   */
  private int matchInsName(Long insId, List<EiPubMaddr> pubMaddrs) throws ServiceException {

    int matched = 3;
    for (EiPubMaddr pubMaddr : pubMaddrs) {
      EiInsName matchInsName = eiInsNameMatchService.eiNameMatch(pubMaddr.getProtoAddr(), insId);
      if (matchInsName != null) {
        pubMaddr.setMatched(1);
        pubMaddr.setEiNameId(matchInsName.getId());
        pubMaddr.setMinsId(insId);
        pubMaddr.setAddr(
            pubMaddr.getProtoAddr().replace(matchInsName.getEiName(), "<b>" + matchInsName.getEiName() + "</b>"));
        matched = 1;
      }
    }
    // 如果有一个地址匹配上，其他地址的状态设置为不需要匹配0的状态
    if (matched == 1) {
      for (EiPubMaddr pubMaddr : pubMaddrs) {
        if (pubMaddr.getMatched() == null || pubMaddr.getMatched() != 1) {
          pubMaddr.setMatched(0);
        }
      }
    }
    return matched;
  }

  /**
   * 判断成果是否是其他机构成果（所有地址匹配上其他机构）,进入条件：未匹配上本机构别名.
   * 
   * @param insId
   * @param pubMaddrs
   * @return 3不确定，4所有地址匹配上其他机构
   * @throws ServiceException
   */
  private int matchOtherInsName(Long insId, List<EiPubMaddr> pubMaddrs) throws ServiceException {
    int matched = 4;
    for (EiPubMaddr pubMaddr : pubMaddrs) {
      if (pubMaddr.getMatched() != null && pubMaddr.getMatched() != 3) {
        continue;
      }
      // 先匹配排除的成果地址
      EiPubAddrExc exc = this.eiInsNameMatchService.matchExcAddr(insId, pubMaddr.getProtoAddr());
      if (exc != null) {
        pubMaddr.setMatched(4);
        continue;
      }
      // 判断是否匹配国外机构
      if (eiInsNameMatchService.matchedExcCtyName(pubMaddr.getProtoAddr(), insId)) {
        pubMaddr.setMatched(4);
        pubMaddr.setMinsId(null);
      }

      // 匹配其他机构别名
      EiInsName matchInsName = eiInsNameMatchService.eiNameMatchOther(pubMaddr.getProtoAddr(), insId);
      if (matchInsName != null) {
        pubMaddr.setMatched(4);
        pubMaddr.setEiNameId(matchInsName.getId());
        pubMaddr.setMinsId(matchInsName.getInsId());
        pubMaddr.setAddr(
            pubMaddr.getProtoAddr().replace(matchInsName.getEiName(), "<b>" + matchInsName.getEiName() + "</b>"));
      } else {
        // 未匹配上排除别名或者其他机构别名，则设置为未确认
        pubMaddr.setMatched(3);
        pubMaddr.setMinsId(null);
        matched = 3;
      }
    }
    return matched;
  }

  /**
   * 判断成果是否是部分匹配机构别名,进入条件：不确定是否是机构成果.
   * 
   * @param insId
   * @param pubMaddrs
   * @return
   * @throws ServiceException
   */
  private int matchPartInsName(Long insId, List<EiPubMaddr> pubMaddrs) throws ServiceException {
    // 默认状态：不确定是否是机构成果.
    int matched = 3;
    for (EiPubMaddr org : pubMaddrs) {
      if (org.getMatched() != null && org.getMatched() != 3) {
        continue;
      }
      // 匹配其他机构别名
      Map<String, Object> map = this.eiInsNameMatchService.eiNameMatchPart(org.getProtoAddr(), insId);
      if (map != null) {
        EiInsName matchInsName = (EiInsName) map.get("eiInsName");
        String showName = (String) map.get("showName");
        org.setMatched(2);
        org.setEiNameId(matchInsName.getId());
        org.setMinsId(null);
        org.setAddr(showName);
        // 部分匹配上机构成果地址
        matched = 2;
      }

    }
    return matched;
  }

  @Override
  public Page<Object> pubStatistics(List<Long> insIds, Page<Object> page) throws ServiceException {

    try {
      this.eiPubAssignDao.pubStatistics(insIds, page);
      return page;
    } catch (Exception e) {
      logger.error("查询待确认机构别名列表", e);
      throw new ServiceException("查询待确认机构别名列表", e);
    }
  }

  @Override
  public Page<Object> loadInsPubAssign(Integer matched, Long insId, Page<Object> page) throws ServiceException {
    try {
      this.eiPubAssignDao.loadInsPubAssign(matched, insId, page);
      return page;
    } catch (Exception e) {
      logger.error("加载匹配成果结果XML_ID列表", e);
      throw new ServiceException("加载匹配成果结果XML_ID列表", e);
    }
  }

  @Override
  public List<EiPubMaddr> loadOrgs(Integer matched, Set<Long> pubIds, Long insId) throws ServiceException {
    try {

      return this.eiPubMaddrDao.loadOrgs(matched, pubIds, insId);
    } catch (Exception e) {
      logger.error("查询待确认机构别名列表", e);
      throw new ServiceException("查询待确认机构别名列表", e);
    }
  }

  @Override
  public List<EiPubMaddr> loadOrgs(List<Integer> matched, Set<Long> pubIds, Long insId) throws ServiceException {
    try {

      return this.eiPubMaddrDao.loadOrgs(matched, pubIds, insId);
    } catch (Exception e) {
      logger.error("查询待确认机构别名列表", e);
      throw new ServiceException("查询待确认机构别名列表", e);
    }
  }

  @Override
  public Map<Long, Map<String, Object>> loadPubTitleAuthor(List<Object> pubIds) throws ServiceException {
    try {

      return this.eiPublicationDao.loadPubTitleAuthor(pubIds);
    } catch (Exception e) {
      logger.error("加载标题以及作者", e);
      throw new ServiceException("加载标题以及作者", e);
    }
  }

  @Override
  public EiPubMaddr getEiPubMaddr(Long maddrId) throws ServiceException {
    try {

      return this.eiPubMaddrDao.getEiPubMaddr(maddrId);
    } catch (Exception e) {
      logger.error("获取地址匹配信息", e);
      throw new ServiceException("获取地址匹配信息", e);
    }
  }

  @Override
  public void saveResetEiPubMaddr(EiPubMaddr maddr) throws ServiceException {
    try {

      this.eiPubMaddrDao.saveResetEiPubMaddr(maddr);
    } catch (Exception e) {
      logger.error("bpo重新设置匹配结果", e);
      throw new ServiceException("bpo重新设置匹配结果", e);
    }
  }

  @Override
  public EiPubAddrExc saveEiPubAddrExc(Long insId, String addr) throws ServiceException {
    try {

      Long hash = PubHashUtils.cleanPubAddrHash(addr);
      if (hash == null) {
        return null;
      }
      return this.eiPubAddrExcDao.saveEiPubAddrExc(insId, StringUtils.substring(addr, 0, 500), hash);
    } catch (Exception e) {
      logger.error("保存排除单位地址", e);
      throw new ServiceException("保存排除单位地址", e);
    }
  }

  @Override
  public void updatePubAssignResult(Long insId, Long pubId, Integer matched) throws ServiceException {
    try {

      EiPubAssign assign = this.eiPubAssignDao.getEiPubAssign(pubId, insId);
      if (assign == null) {
        return;
      }
      assign.setResult(matched);
      this.eiPubAssignDao.save(assign);
    } catch (Exception e) {
      logger.error("修改匹配结果", e);
      throw new ServiceException("修改匹配结果", e);
    }
  }

  @Override
  public List<EiPubAssign> getNeedMatchPub(Long insId) throws ServiceException {
    try {

      return this.eiPubAssignDao.getNeedMatchPub(insId);
    } catch (Exception e) {
      logger.error("获取需要重新匹配的数据列表", e);
      throw new ServiceException("获取需要重新匹配的数据列表", e);
    }
  }

  @Override
  public void rematchInsPub(EiPubAssign pubAssign) throws ServiceException {
    try {
      List<EiPubAddr> pubAddrs = this.eiPubAddrDao.getEiPubAddr(pubAssign.getPubId());
      if (CollectionUtils.isEmpty(pubAddrs)) {
        pubAssign.setStatus(1);
        this.eiPubAssignDao.save(pubAssign);
        return;
      }
      this.matchPubCache(pubAssign.getInsId(), pubAssign.getPubId(), pubAddrs);
    } catch (Exception e) {
      logger.error("Ei重新匹配成果到机构", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<EiPubAssign> getRematchMatchPub(Long startId) throws ServiceException {
    try {

      return this.eiPubAssignDao.getRematchMatchPub(startId);
    } catch (Exception e) {
      logger.error("获取需要重新匹配的数据列表", e);
      throw new ServiceException("获取需要重新匹配的数据列表", e);
    }
  }

}
