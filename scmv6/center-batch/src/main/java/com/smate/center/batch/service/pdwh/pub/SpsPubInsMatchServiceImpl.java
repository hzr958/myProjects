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

import com.smate.center.batch.dao.pdwh.pub.sps.SpsPubAddrDao;
import com.smate.center.batch.dao.pdwh.pub.sps.SpsPubAssignDao;
import com.smate.center.batch.dao.pdwh.pub.sps.SpsPubMaddrDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.sps.SpsInsName;
import com.smate.center.batch.model.pdwh.pub.sps.SpsPubAddr;
import com.smate.center.batch.model.pdwh.pub.sps.SpsPubAddrExc;
import com.smate.center.batch.model.pdwh.pub.sps.SpsPubAssign;
import com.smate.center.batch.model.pdwh.pub.sps.SpsPubMaddr;
import com.smate.center.batch.service.pdwh.pubmatch.IsiInsNameMatchService;
import com.smate.center.batch.service.pdwh.pubmatch.SpsInsNameMatchService;

/**
 * scopus成果基准库批量抓取临时库匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("spsPubInsMatchService")
@Transactional(rollbackFor = Exception.class)
public class SpsPubInsMatchServiceImpl implements SpsPubInsMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = 6182414626692320403L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private IsiInsNameMatchService isiInsNameMatchService;
  @Autowired
  private SpsInsNameMatchService spsInsNameMatchService;
  @Autowired
  private SpsPubMaddrDao spsPubMaddrDao;
  @Autowired
  private SpsPubAddrDao spsPubAddrDao;
  @Autowired
  private SpsPubAssignDao spsPubAssignDao;

  @Override
  public Integer matchPubCache(Long insId, Long pubId) throws ServiceException {

    List<SpsPubAddr> pubAddrs = spsPubAddrDao.getSpsPubAddr(pubId);
    return this.matchPubCache(insId, pubId, pubAddrs);
  }

  @Override
  public Integer matchPubCache(Long insId, Long pubId, List<SpsPubAddr> pubAddrs) throws ServiceException {

    try {
      // 匹配成果地址.
      int matched = matchPubAddrs(insId, pubId, pubAddrs);

      // 保存匹配结果
      SpsPubAssign assign = spsPubAssignDao.getSpsPubAssign(pubId, insId);
      if (assign == null) {
        assign = new SpsPubAssign(pubId, insId, 1, matched, 0);
      } else {
        // 一直都是匹配上的状态
        if (assign.getResult() == 1 && matched == 1) {
          assign.setStatus(1);
          spsPubAssignDao.save(assign);
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
      spsPubAssignDao.save(assign);

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
  private int matchPubAddrs(Long insId, Long pubId, List<SpsPubAddr> pubAddrs) throws ServiceException {

    if (CollectionUtils.isEmpty(pubAddrs)) {
      return 4;
    }
    List<SpsPubMaddr> qmaddrs = spsPubMaddrDao.getSpsPubMaddrs(pubId, insId);
    List<SpsPubMaddr> maddrs = new ArrayList<SpsPubMaddr>();
    for (SpsPubAddr pubAddr : pubAddrs) {
      SpsPubMaddr maddr = null;
      for (SpsPubMaddr tmaddr : qmaddrs) {
        if (pubAddr.getAddrId().equals(tmaddr.getAddrId())) {
          maddr = tmaddr;
          maddr.setProtoAddr(pubAddr.getAddr());
          // 还原为未确认
          maddr.setMatched(3);
          maddrs.add(maddr);
        }
      }
      if (maddr == null) {
        maddr = new SpsPubMaddr(pubAddr.getAddrId(), insId, pubId, pubAddr.getAddr(), pubAddr.getAddr());
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
    for (SpsPubMaddr pubMaddr : maddrs) {
      this.spsPubMaddrDao.save(pubMaddr);
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
  private int matchInsName(Long insId, List<SpsPubMaddr> pubMaddrs) throws ServiceException {

    int matched = 3;
    for (SpsPubMaddr pubMaddr : pubMaddrs) {
      SpsInsName matchInsName = spsInsNameMatchService.spsNameMatch(pubMaddr.getProtoAddr(), insId);
      if (matchInsName != null) {
        pubMaddr.setMatched(1);
        pubMaddr.setSpsNameId(matchInsName.getId());
        pubMaddr.setMinsId(insId);
        pubMaddr.setAddr(
            pubMaddr.getProtoAddr().replace(matchInsName.getSpsName(), "<b>" + matchInsName.getSpsName() + "</b>"));
        matched = 1;
      }
    }
    // 如果有一个地址匹配上，其他地址的状态设置为不需要匹配0的状态
    if (matched == 1) {
      for (SpsPubMaddr pubMaddr : pubMaddrs) {
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
  private int matchOtherInsName(Long insId, List<SpsPubMaddr> pubMaddrs) throws ServiceException {
    int matched = 4;
    for (SpsPubMaddr pubMaddr : pubMaddrs) {
      if (pubMaddr.getMatched() != null && pubMaddr.getMatched() != 3) {
        continue;
      }
      // 先匹配排除的成果地址
      SpsPubAddrExc exc = this.spsInsNameMatchService.matchExcAddr(insId, pubMaddr.getProtoAddr());
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
      SpsInsName matchInsName = spsInsNameMatchService.spsNameMatchOther(pubMaddr.getProtoAddr(), insId);
      if (matchInsName != null) {
        pubMaddr.setMatched(4);
        pubMaddr.setSpsNameId(matchInsName.getId());
        pubMaddr.setMinsId(matchInsName.getInsId());
        pubMaddr.setAddr(
            pubMaddr.getProtoAddr().replace(matchInsName.getSpsName(), "<b>" + matchInsName.getSpsName() + "</b>"));
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
  private int matchPartInsName(Long insId, List<SpsPubMaddr> pubMaddrs) throws ServiceException {
    // 默认状态：不确定是否是机构成果.
    int matched = 3;
    for (SpsPubMaddr org : pubMaddrs) {
      if (org.getMatched() != null && org.getMatched() != 3) {
        continue;
      }
      // 匹配其他机构别名
      Map<String, Object> map = this.spsInsNameMatchService.spsNameMatchPart(org.getProtoAddr(), insId);
      if (map != null) {
        SpsInsName matchInsName = (SpsInsName) map.get("spsInsName");
        String showName = (String) map.get("showName");
        org.setMatched(2);
        org.setSpsNameId(matchInsName.getId());
        org.setMinsId(null);
        org.setAddr(showName);
        // 部分匹配上机构成果地址
        matched = 2;
      }

    }
    return matched;
  }

  @Override
  public void rematchInsPub(SpsPubAssign pubAssign) throws ServiceException {
    try {
      List<SpsPubAddr> pubAddrs = this.spsPubAddrDao.getSpsPubAddr(pubAssign.getPubId());
      if (CollectionUtils.isEmpty(pubAddrs)) {
        pubAssign.setStatus(1);
        this.spsPubAssignDao.save(pubAssign);
        return;
      }
      this.matchPubCache(pubAssign.getInsId(), pubAssign.getPubId(), pubAddrs);
    } catch (Exception e) {
      logger.error("scopus重新匹配成果到机构", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<SpsPubAssign> getRematchMatchPub(Long startId) throws ServiceException {
    try {

      return this.spsPubAssignDao.getRematchMatchPub(startId);
    } catch (Exception e) {
      logger.error("获取需要重新匹配的数据列表", e);
      throw new ServiceException("获取需要重新匹配的数据列表", e);
    }
  }
}
