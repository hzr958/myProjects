package com.smate.center.batch.service.pdwh.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.pubmed.PubMedPubAddrDao;
import com.smate.center.batch.dao.pdwh.pub.pubmed.PubMedPubAssignDao;
import com.smate.center.batch.dao.pdwh.pub.pubmed.PubMedPubMaddrDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.pubmed.PubMedPubAddr;
import com.smate.center.batch.model.pdwh.pub.pubmed.PubMedPubAssign;
import com.smate.center.batch.model.pdwh.pub.pubmed.PubMedPubMaddr;
import com.smate.center.batch.model.pdwh.pub.pubmed.PubmedInsName;
import com.smate.center.batch.service.pdwh.pubmatch.IsiInsNameMatchService;
import com.smate.center.batch.service.pdwh.pubmatch.PubMedInsNameMatchService;

/**
 * PubMed成果基准库批量抓取临时库匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("pubMedPubInsMatchService")
@Transactional(rollbackFor = Exception.class)
public class PubMedPubInsMatchServiceImpl implements PubMedPubInsMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = 8855306785594082203L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private IsiInsNameMatchService isiInsNameMatchService;
  @Autowired
  private PubMedInsNameMatchService pubMedInsNameMatchService;
  @Autowired
  private PubMedPubMaddrDao pubMedPubMaddrDao;
  @Autowired
  private PubMedPubAddrDao pubMedPubAddrDao;
  @Autowired
  private PubMedPubAssignDao pubMedPubAssignDao;

  @Override
  public Integer matchPubCache(Long insId, Long pubId) throws ServiceException {

    List<PubMedPubAddr> pubAddrs = pubMedPubAddrDao.getPubMedPubAddr(pubId);
    return this.matchPubCache(insId, pubId, pubAddrs);
  }

  @Override
  public Integer matchPubCache(Long insId, Long pubId, List<PubMedPubAddr> pubAddrs) throws ServiceException {

    try {
      // 匹配成果地址.
      int matched = matchPubAddrs(insId, pubId, pubAddrs);

      // 保存匹配结果
      PubMedPubAssign assign = pubMedPubAssignDao.getPubMedPubAssign(pubId, insId);
      if (assign == null) {
        assign = new PubMedPubAssign(pubId, insId, 1, matched, 0);
      } else {
        // 一直都是匹配上的状态
        if (assign.getResult() == 1 && matched == 1) {
          assign.setStatus(1);
          pubMedPubAssignDao.save(assign);
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
      pubMedPubAssignDao.save(assign);

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
  private int matchPubAddrs(Long insId, Long pubId, List<PubMedPubAddr> pubAddrs) throws ServiceException {

    if (CollectionUtils.isEmpty(pubAddrs)) {
      return 4;
    }
    List<PubMedPubMaddr> qmaddrs = pubMedPubMaddrDao.getPubMedPubMaddrs(pubId, insId);
    List<PubMedPubMaddr> maddrs = new ArrayList<PubMedPubMaddr>();
    for (PubMedPubAddr pubAddr : pubAddrs) {
      PubMedPubMaddr maddr = null;
      for (PubMedPubMaddr tmaddr : qmaddrs) {
        if (pubAddr.getAddrId().equals(tmaddr.getAddrId())) {
          maddr = tmaddr;
          maddr.setProtoAddr(pubAddr.getAddr());
          // 还原为未确认
          maddr.setMatched(3);
          maddrs.add(maddr);
        }
      }
      if (maddr == null) {
        maddr = new PubMedPubMaddr(pubAddr.getAddrId(), insId, pubId, pubAddr.getAddr(), pubAddr.getAddr());
        // 还原为未确认
        maddr.setMatched(3);
        maddrs.add(maddr);
      }
    }
    // 判断是否能匹配上本机构
    int matched = this.matchInsName(insId, maddrs);
    if (matched != 1) {
      // 判断是否全部是其他机构地址
      matched = this.matchOtherInsName(insId, maddrs);
      // 判断是否部分匹配上本机构
      if (matched == 3) {
        matched = this.matchPartInsName(insId, maddrs);
      }
    }
    // 保存地址匹配结果.
    for (PubMedPubMaddr pubMaddr : maddrs) {
      this.pubMedPubMaddrDao.save(pubMaddr);
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
  private int matchInsName(Long insId, List<PubMedPubMaddr> pubMaddrs) throws ServiceException {

    int matched = 3;
    for (PubMedPubMaddr pubMaddr : pubMaddrs) {
      PubmedInsName matchInsName = pubMedInsNameMatchService.pubMedNameMatch(pubMaddr.getProtoAddr(), insId);
      if (matchInsName != null) {
        pubMaddr.setMatched(1);
        pubMaddr.setPmNameId(matchInsName.getId());
        pubMaddr.setMinsId(insId);
        pubMaddr.setAddr(pubMaddr.getProtoAddr().replace(matchInsName.getPubmedName(),
            "<b>" + matchInsName.getPubmedName() + "</b>"));
        matched = 1;
      }
    }
    // 如果有一个地址匹配上，其他地址的状态设置为不需要匹配0的状态
    if (matched == 1) {
      for (PubMedPubMaddr pubMaddr : pubMaddrs) {
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
  private int matchOtherInsName(Long insId, List<PubMedPubMaddr> pubMaddrs) throws ServiceException {
    int matched = 4;
    for (PubMedPubMaddr pubMaddr : pubMaddrs) {
      if (pubMaddr.getMatched() != null && pubMaddr.getMatched() != 3) {
        continue;
      }

      // 判断是否匹配国外机构
      if (isiInsNameMatchService.matchedExcCtyName(pubMaddr.getProtoAddr(), insId)) {
        pubMaddr.setMatched(4);
        pubMaddr.setMinsId(null);
      }

      // 匹配其他机构别名
      PubmedInsName matchInsName = pubMedInsNameMatchService.pubMedNameMatchOther(pubMaddr.getProtoAddr(), insId);
      if (matchInsName != null) {
        pubMaddr.setMatched(4);
        pubMaddr.setPmNameId(matchInsName.getId());
        pubMaddr.setMinsId(matchInsName.getInsId());
        pubMaddr.setAddr(pubMaddr.getProtoAddr().replace(matchInsName.getPubmedName(),
            "<b>" + matchInsName.getPubmedName() + "</b>"));
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
  private int matchPartInsName(Long insId, List<PubMedPubMaddr> pubMaddrs) throws ServiceException {
    // 默认状态：不确定是否是机构成果.
    int matched = 3;
    for (PubMedPubMaddr org : pubMaddrs) {
      if (org.getMatched() != null && org.getMatched() != 3) {
        continue;
      }
      // 匹配其他机构别名
      Map<String, Object> map = this.pubMedInsNameMatchService.pubMedNameMatchPart(org.getProtoAddr(), insId);
      if (map != null) {
        PubmedInsName matchInsName = (PubmedInsName) map.get("pubMedInsName");
        String showName = (String) map.get("showName");
        org.setMatched(2);
        org.setPmNameId(matchInsName.getId());
        org.setMinsId(null);
        org.setAddr(showName);
        // 部分匹配上机构成果地址
        matched = 2;
      }

    }
    return matched;
  }

  @Override
  public void rematchInsPub(PubMedPubAssign pubAssign) throws ServiceException {
    try {
      List<PubMedPubAddr> pubAddrs = this.pubMedPubAddrDao.getPubMedPubAddr(pubAssign.getPubId());
      if (CollectionUtils.isEmpty(pubAddrs)) {
        pubAssign.setStatus(1);
        this.pubMedPubAssignDao.save(pubAssign);
        return;
      }
      this.matchPubCache(pubAssign.getInsId(), pubAssign.getPubId(), pubAddrs);
    } catch (Exception e) {
      logger.error("pubmed重新匹配成果到机构", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PubMedPubAssign> getRematchMatchPub(Long startId) throws ServiceException {
    try {

      return this.pubMedPubAssignDao.getRematchMatchPub(startId);
    } catch (Exception e) {
      logger.error("获取需要重新匹配的数据列表", e);
      throw new ServiceException("获取需要重新匹配的数据列表", e);
    }
  }
}
