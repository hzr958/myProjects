package com.smate.center.batch.service.pdwh.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.cnipr.CniprPubAddrDao;
import com.smate.center.batch.dao.pdwh.pub.cnipr.CniprPubAssignDao;
import com.smate.center.batch.dao.pdwh.pub.cnipr.CniprPubMaddrDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprInsName;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprPubAddr;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprPubAssign;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprPubMaddr;
import com.smate.center.batch.service.pdwh.pubmatch.CniprInsNameMatchService;

/**
 * cnipr成果基准库批量抓取临时库匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("cniprPubInsMatchService")
@Transactional(rollbackFor = Exception.class)
public class CniprPubInsMatchServiceImpl implements CniprPubInsMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = -7734958294679544733L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CniprInsNameMatchService cniprInsNameMatchService;
  @Autowired
  private CniprPubMaddrDao cniprPubMaddrDao;
  @Autowired
  private CniprPubAddrDao cniprPubAddrDao;
  @Autowired
  private CniprPubAssignDao cniprPubAssignDao;

  @Override
  public Integer matchPubCache(Long insId, Long pubId) throws ServiceException {

    List<CniprPubAddr> pubAddrs = cniprPubAddrDao.getCniprPubAddr(pubId);
    return this.matchPubCache(insId, pubId, pubAddrs);
  }

  @Override
  public Integer matchPubCache(Long insId, Long pubId, List<CniprPubAddr> pubAddrs) throws ServiceException {

    try {
      // 匹配成果地址.
      int matched = matchPubAddrs(insId, pubId, pubAddrs);

      // 保存匹配结果
      CniprPubAssign assign = cniprPubAssignDao.getCniprPubAssign(pubId, insId);
      if (assign == null) {
        assign = new CniprPubAssign(pubId, insId, 1, matched, 0);
      } else {
        // 一直都是匹配上的状态
        if (assign.getResult() == 1 && matched == 1) {
          assign.setStatus(1);
          cniprPubAssignDao.save(assign);
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
      cniprPubAssignDao.save(assign);
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
  private int matchPubAddrs(Long insId, Long pubId, List<CniprPubAddr> pubAddrs) throws ServiceException {

    if (CollectionUtils.isEmpty(pubAddrs)) {
      return 4;
    }
    List<CniprPubMaddr> qmaddrs = cniprPubMaddrDao.getCniprPubMaddrs(pubId, insId);
    List<CniprPubMaddr> maddrs = new ArrayList<CniprPubMaddr>();
    for (CniprPubAddr pubAddr : pubAddrs) {
      CniprPubMaddr maddr = null;
      for (CniprPubMaddr tmaddr : qmaddrs) {
        if (pubAddr.getAddrId().equals(tmaddr.getAddrId())) {
          maddr = tmaddr;
          maddr.setProtoAddr(pubAddr.getAddr());
          // 还原为未匹配上
          maddr.setMatched(0);
          maddrs.add(maddr);
        }
      }
      if (maddr == null) {
        maddr = new CniprPubMaddr(pubAddr.getAddrId(), insId, pubId, pubAddr.getAddr());
        // 设置默认未匹配上
        maddr.setMatched(0);
        maddrs.add(maddr);
      }
    }
    // 判断是否能匹配上本机构
    int matched = this.matchInsName(insId, maddrs);
    // 保存地址匹配结果.
    for (CniprPubMaddr pubMaddr : maddrs) {
      this.cniprPubMaddrDao.save(pubMaddr);
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
  private int matchInsName(Long insId, List<CniprPubMaddr> pubMaddrs) throws ServiceException {

    int matched = 0;
    for (CniprPubMaddr pubMaddr : pubMaddrs) {
      CniprInsName matchInsName = cniprInsNameMatchService.cniprNameMatch(pubMaddr.getProtoAddr(), insId);
      if (matchInsName != null) {
        pubMaddr.setMatched(1);
        pubMaddr.setCniprNameId(matchInsName.getId());
        pubMaddr.setMinsId(insId);
        matched = 1;
      } else {
        pubMaddr.setMatched(0);
      }
    }
    return matched;
  }

  @Override
  public List<CniprPubAssign> getRematchMatchPub(Long startId) throws ServiceException {
    try {

      return this.cniprPubAssignDao.getRematchMatchPub(startId);
    } catch (Exception e) {
      logger.error("获取需要重新匹配的数据列表", e);
      throw new ServiceException("获取需要重新匹配的数据列表", e);
    }
  }

  @Override
  public void rematchInsPub(CniprPubAssign pubAssign) throws ServiceException {
    try {
      List<CniprPubAddr> pubAddrs = this.cniprPubAddrDao.getCniprPubAddr(pubAssign.getPubId());
      if (CollectionUtils.isEmpty(pubAddrs)) {
        pubAssign.setStatus(1);
        this.cniprPubAssignDao.save(pubAssign);
        return;
      }
      this.matchPubCache(pubAssign.getInsId(), pubAssign.getPubId(), pubAddrs);
    } catch (Exception e) {
      logger.error("CNIPR重新匹配成果到机构", e);
      throw new ServiceException(e);
    }
  }

}
