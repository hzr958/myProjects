package com.smate.center.batch.service.pdwh.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.cnkipat.CnkiPatPubAddrDao;
import com.smate.center.batch.dao.pdwh.pub.cnkipat.CnkiPatPubAssignDao;
import com.smate.center.batch.dao.pdwh.pub.cnkipat.CnkiPatPubMaddrDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprInsName;
import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubAddr;
import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubAssign;
import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubMaddr;
import com.smate.center.batch.service.pdwh.pubmatch.CniprInsNameMatchService;

/**
 * cnkipat成果基准库批量抓取临时库匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("cnkipatPubInsMatchService")
@Transactional(rollbackFor = Exception.class)
public class CnkiPatPubInsMatchServiceImpl implements CnkiPatPubInsMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = -5685317758923313973L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CniprInsNameMatchService cniprInsNameMatchService;
  @Autowired
  private CnkiPatPubMaddrDao cnkiPatPubMaddrDao;
  @Autowired
  private CnkiPatPubAddrDao cnkiPatPubAddrDao;
  @Autowired
  private CnkiPatPubAssignDao cnkiPatPubAssignDao;

  @Override
  public Integer matchPubCache(Long insId, Long pubId) throws ServiceException {

    List<CnkiPatPubAddr> pubAddrs = cnkiPatPubAddrDao.getCnkiPatPubAddr(pubId);
    return this.matchPubCache(insId, pubId, pubAddrs);
  }

  @Override
  public Integer matchPubCache(Long insId, Long pubId, List<CnkiPatPubAddr> pubAddrs) throws ServiceException {

    try {
      // 匹配成果地址.
      int matched = matchPubAddrs(insId, pubId, pubAddrs);

      // 保存匹配结果
      CnkiPatPubAssign assign = cnkiPatPubAssignDao.getCnkiPatPubAssign(pubId, insId);
      if (assign == null) {
        assign = new CnkiPatPubAssign(pubId, insId, 1, matched, 0);
      } else {
        // 一直都是匹配上的状态
        if (assign.getResult() == 1 && matched == 1) {
          assign.setStatus(1);
          cnkiPatPubAssignDao.save(assign);
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
      cnkiPatPubAssignDao.save(assign);
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
  private int matchPubAddrs(Long insId, Long pubId, List<CnkiPatPubAddr> pubAddrs) throws ServiceException {

    if (CollectionUtils.isEmpty(pubAddrs)) {
      return 4;
    }
    List<CnkiPatPubMaddr> qmaddrs = cnkiPatPubMaddrDao.getCnkiPatPubMaddrs(pubId, insId);
    List<CnkiPatPubMaddr> maddrs = new ArrayList<CnkiPatPubMaddr>();
    for (CnkiPatPubAddr pubAddr : pubAddrs) {
      CnkiPatPubMaddr maddr = null;
      for (CnkiPatPubMaddr tmaddr : qmaddrs) {
        if (pubAddr.getAddrId().equals(tmaddr.getAddrId())) {
          maddr = tmaddr;
          maddr.setProtoAddr(pubAddr.getAddr());
          // 还原为未匹配上
          maddr.setMatched(0);
          maddrs.add(maddr);
        }
      }
      if (maddr == null) {
        maddr = new CnkiPatPubMaddr(pubAddr.getAddrId(), insId, pubId, pubAddr.getAddr());
        // 设置默认未匹配上
        maddr.setMatched(0);
        maddrs.add(maddr);
      }
    }
    // 判断是否能匹配上本机构
    int matched = this.matchInsName(insId, maddrs);
    // 保存地址匹配结果.
    for (CnkiPatPubMaddr pubMaddr : maddrs) {
      this.cnkiPatPubMaddrDao.save(pubMaddr);
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
  private int matchInsName(Long insId, List<CnkiPatPubMaddr> pubMaddrs) throws ServiceException {

    int matched = 0;
    for (CnkiPatPubMaddr pubMaddr : pubMaddrs) {
      CniprInsName matchInsName = cniprInsNameMatchService.cniprNameMatch(pubMaddr.getProtoAddr(), insId);
      if (matchInsName != null) {
        pubMaddr.setMatched(1);
        pubMaddr.setCnkiPatNameId(matchInsName.getId());
        pubMaddr.setMinsId(insId);
        matched = 1;
      } else {
        pubMaddr.setMatched(0);
      }
    }
    return matched;
  }

  @Override
  public List<CnkiPatPubAssign> getRematchMatchPub(Long startId) throws ServiceException {
    try {

      return this.cnkiPatPubAssignDao.getRematchMatchPub(startId);
    } catch (Exception e) {
      logger.error("获取需要重新匹配的数据列表", e);
      throw new ServiceException("获取需要重新匹配的数据列表", e);
    }
  }

  @Override
  public void rematchInsPub(CnkiPatPubAssign pubAssign) throws ServiceException {
    try {
      List<CnkiPatPubAddr> pubAddrs = this.cnkiPatPubAddrDao.getCnkiPatPubAddr(pubAssign.getPubId());
      if (CollectionUtils.isEmpty(pubAddrs)) {
        pubAssign.setStatus(1);
        this.cnkiPatPubAssignDao.save(pubAssign);
        return;
      }
      this.matchPubCache(pubAssign.getInsId(), pubAssign.getPubId(), pubAddrs);
    } catch (Exception e) {
      logger.error("CNKIPAT重新匹配成果到机构", e);
      throw new ServiceException(e);
    }
  }

}
