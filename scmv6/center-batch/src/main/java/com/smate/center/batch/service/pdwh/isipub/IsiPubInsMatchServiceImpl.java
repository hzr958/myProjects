package com.smate.center.batch.service.pdwh.isipub;

import com.smate.center.batch.dao.pdwh.pub.isi.*;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.isi.*;
import com.smate.center.batch.service.pdwh.pubmatch.IsiInsNameMatchService;
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
@Service("isiPubInsMatchService")
@Transactional(rollbackFor = Exception.class)
public class IsiPubInsMatchServiceImpl implements IsiPubInsMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = 6182414626692320403L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private IsiInsNameMatchService isiInsNameMatchService;
  @Autowired
  private IsiPubMaddrDao isiPubMaddrDao;
  @Autowired
  private IsiPubAddrDao isiPubAddrDao;
  @Autowired
  private IsiPubAssignDao isiPubAssignDao;
  @Autowired
  private IsiPubAddrExcDao isiPubAddrExcDao;
  @Autowired
  private IsiPublicationDao isiPublicationDao;

  @Override
  public Integer matchPubCache(Long insId, Long pubId) throws ServiceException {

    List<IsiPubAddr> pubAddrs = isiPubAddrDao.getIsiPubAddr(pubId);
    return this.matchPubCache(insId, pubId, pubAddrs);
  }

  @Override
  public Integer matchPubCache(Long insId, Long pubId, List<IsiPubAddr> pubAddrs) throws ServiceException {

    try {
      // 匹配成果地址.
      int matched = matchPubAddrs(insId, pubId, pubAddrs);

      // 保存匹配结果
      IsiPubAssign assign = isiPubAssignDao.getIsiPubAssign(pubId, insId);
      if (assign == null) {
        assign = new IsiPubAssign(pubId, insId, 1, matched, 0);
      } else {
        // 一直都是匹配上的状态
        if (assign.getResult() == 1 && matched == 1) {
          assign.setStatus(1);
          isiPubAssignDao.save(assign);
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
      isiPubAssignDao.save(assign);
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
  private int matchPubAddrs(Long insId, Long pubId, List<IsiPubAddr> pubAddrs) throws ServiceException {

    if (CollectionUtils.isEmpty(pubAddrs)) {
      return 4;
    }
    List<IsiPubMaddr> qmaddrs = isiPubMaddrDao.getIsiPubMaddrs(pubId, insId);
    List<IsiPubMaddr> maddrs = new ArrayList<IsiPubMaddr>();
    for (IsiPubAddr pubAddr : pubAddrs) {
      IsiPubMaddr maddr = null;
      for (IsiPubMaddr tmaddr : qmaddrs) {
        if (pubAddr.getAddrId().equals(tmaddr.getAddrId())) {
          maddr = tmaddr;
          maddr.setProtoAddr(pubAddr.getAddr());
          // 还原为未确认
          maddr.setMatched(3);
          maddrs.add(maddr);
        }
      }
      if (maddr == null) {
        maddr = new IsiPubMaddr(pubAddr.getAddrId(), insId, pubId, pubAddr.getAddr(), pubAddr.getAddr());
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
    for (IsiPubMaddr pubMaddr : maddrs) {
      this.isiPubMaddrDao.save(pubMaddr);
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
  private int matchInsName(Long insId, List<IsiPubMaddr> pubMaddrs) throws ServiceException {

    int matched = 3;
    for (IsiPubMaddr pubMaddr : pubMaddrs) {
      IsiInsName matchInsName = isiInsNameMatchService.isiNameMatch(pubMaddr.getProtoAddr(), insId);
      if (matchInsName != null) {
        pubMaddr.setMatched(1);
        pubMaddr.setIsiNameId(matchInsName.getId());
        pubMaddr.setMinsId(insId);
        pubMaddr.setAddr(
            pubMaddr.getProtoAddr().replace(matchInsName.getIsiName(), "<b>" + matchInsName.getIsiName() + "</b>"));
        matched = 1;
      }
    }
    // 如果有一个地址匹配上，其他地址的状态设置为不需要匹配0的状态
    if (matched == 1) {
      for (IsiPubMaddr pubMaddr : pubMaddrs) {
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
  private int matchOtherInsName(Long insId, List<IsiPubMaddr> pubMaddrs) throws ServiceException {
    int matched = 4;
    for (IsiPubMaddr pubMaddr : pubMaddrs) {
      if (pubMaddr.getMatched() != null && pubMaddr.getMatched() != 3) {
        continue;
      }
      // 先匹配排除的成果地址
      IsiPubAddrExc exc = this.isiInsNameMatchService.matchExcAddr(insId, pubMaddr.getProtoAddr());
      if (exc != null) {
        pubMaddr.setMatched(4);
        continue;
      }
      // 判断是否匹配国外机构
      if (isiInsNameMatchService.matchedExcCtyName(pubMaddr.getProtoAddr(), insId)) {
        pubMaddr.setMatched(4);
        pubMaddr.setMinsId(null);
      }

      // 匹配其他机构别名
      IsiInsName matchInsName = isiInsNameMatchService.isiNameMatchOther(pubMaddr.getProtoAddr(), insId);
      if (matchInsName != null) {
        pubMaddr.setMatched(4);
        pubMaddr.setIsiNameId(matchInsName.getId());
        pubMaddr.setMinsId(matchInsName.getInsId());
        pubMaddr.setAddr(
            pubMaddr.getProtoAddr().replace(matchInsName.getIsiName(), "<b>" + matchInsName.getIsiName() + "</b>"));
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
  private int matchPartInsName(Long insId, List<IsiPubMaddr> pubMaddrs) throws ServiceException {
    // 默认状态：不确定是否是机构成果.
    int matched = 3;
    for (IsiPubMaddr org : pubMaddrs) {
      if (org.getMatched() != null && org.getMatched() != 3) {
        continue;
      }
      // 匹配其他机构别名
      Map<String, Object> map = this.isiInsNameMatchService.isiNameMatchPart(org.getProtoAddr(), insId);
      if (map != null) {
        IsiInsName matchInsName = (IsiInsName) map.get("isiInsName");
        String showName = (String) map.get("showName");
        org.setMatched(2);
        org.setIsiNameId(matchInsName.getId());
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
      this.isiPubAssignDao.pubStatistics(insIds, page);
      return page;
    } catch (Exception e) {
      logger.error("查询待确认机构别名列表", e);
      throw new ServiceException("查询待确认机构别名列表", e);
    }
  }

  @Override
  public Page<Object> loadInsPubAssign(Integer matched, Long insId, Page<Object> page) throws ServiceException {
    try {
      this.isiPubAssignDao.loadInsPubAssign(matched, insId, page);
      return page;
    } catch (Exception e) {
      logger.error("加载匹配成果结果XML_ID列表", e);
      throw new ServiceException("加载匹配成果结果XML_ID列表", e);
    }
  }

  @Override
  public List<IsiPubMaddr> loadOrgs(Integer matched, Set<Long> pubIds, Long insId) throws ServiceException {
    try {

      return this.isiPubMaddrDao.loadOrgs(matched, pubIds, insId);
    } catch (Exception e) {
      logger.error("查询待确认机构别名列表", e);
      throw new ServiceException("查询待确认机构别名列表", e);
    }
  }

  @Override
  public List<IsiPubMaddr> loadOrgs(List<Integer> matched, Set<Long> pubIds, Long insId) throws ServiceException {
    try {

      return this.isiPubMaddrDao.loadOrgs(matched, pubIds, insId);
    } catch (Exception e) {
      logger.error("查询待确认机构别名列表", e);
      throw new ServiceException("查询待确认机构别名列表", e);
    }
  }

  @Override
  public Map<Long, Map<String, Object>> loadPubTitleAuthor(List<Object> pubIds) throws ServiceException {
    try {

      return this.isiPublicationDao.loadPubTitleAuthor(pubIds);
    } catch (Exception e) {
      logger.error("加载标题以及作者", e);
      throw new ServiceException("加载标题以及作者", e);
    }
  }

  @Override
  public IsiPubMaddr getIsiPubMaddr(Long maddrId) throws ServiceException {
    try {

      return this.isiPubMaddrDao.getIsiPubMaddr(maddrId);
    } catch (Exception e) {
      logger.error("获取地址匹配信息", e);
      throw new ServiceException("获取地址匹配信息", e);
    }
  }

  @Override
  public void saveResetIsiPubMaddr(IsiPubMaddr maddr) throws ServiceException {
    try {

      this.isiPubMaddrDao.saveResetIsiPubMaddr(maddr);
    } catch (Exception e) {
      logger.error("bpo重新设置匹配结果", e);
      throw new ServiceException("bpo重新设置匹配结果", e);
    }
  }

  @Override
  public IsiPubAddrExc saveIsiPubAddrExc(Long insId, String addr) throws ServiceException {
    try {

      Long hash = PubHashUtils.cleanPubAddrHash(addr);
      if (hash == null) {
        return null;
      }
      return this.isiPubAddrExcDao.saveIsiPubAddrExc(insId, StringUtils.substring(addr, 0, 500), hash);
    } catch (Exception e) {
      logger.error("保存排除单位地址", e);
      throw new ServiceException("保存排除单位地址", e);
    }
  }

  @Override
  public void updatePubAssignResult(Long insId, Long pubId, Integer matched) throws ServiceException {
    try {

      IsiPubAssign assign = this.isiPubAssignDao.getIsiPubAssign(pubId, insId);
      if (assign == null) {
        return;
      }
      assign.setResult(matched);
      this.isiPubAssignDao.save(assign);
    } catch (Exception e) {
      logger.error("修改匹配结果", e);
      throw new ServiceException("修改匹配结果", e);
    }
  }

  @Override
  public List<IsiPubAssign> getNeedMatchPub(Long insId) throws ServiceException {
    try {

      return this.isiPubAssignDao.getNeedMatchPub(insId);
    } catch (Exception e) {
      logger.error("获取需要重新匹配的数据列表", e);
      throw new ServiceException("获取需要重新匹配的数据列表", e);
    }
  }

  @Override
  public void rematchInsPub(IsiPubAssign pubAssign) throws ServiceException {
    try {
      List<IsiPubAddr> pubAddrs = this.isiPubAddrDao.getIsiPubAddr(pubAssign.getPubId());
      if (CollectionUtils.isEmpty(pubAddrs)) {
        pubAssign.setStatus(1);
        this.isiPubAssignDao.save(pubAssign);
        return;
      }
      this.matchPubCache(pubAssign.getInsId(), pubAssign.getPubId(), pubAddrs);
    } catch (Exception e) {
      logger.error("ISI重新匹配成果到机构", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<IsiPubAssign> getRematchMatchPub(Long startId) throws ServiceException {
    try {

      return this.isiPubAssignDao.getRematchMatchPub(startId);
    } catch (Exception e) {
      logger.error("获取需要重新匹配的数据列表", e);
      throw new ServiceException("获取需要重新匹配的数据列表", e);
    }
  }

}
