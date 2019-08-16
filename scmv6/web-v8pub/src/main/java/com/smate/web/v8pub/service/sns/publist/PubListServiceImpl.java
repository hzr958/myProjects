package com.smate.web.v8pub.service.sns.publist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.sns.PubLikeDAO;
import com.smate.web.v8pub.dao.sns.PubStatisticsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubStatisticsPO;
import com.smate.web.v8pub.service.query.PubQueryhandlerService;
import com.smate.web.v8pub.service.sns.PsnStatisticsService;
import com.smate.web.v8pub.vo.PubListResult;
import com.smate.web.v8pub.vo.PubListVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class PubListServiceImpl implements PubListService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubQueryhandlerService pubQueryhandlerService;
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;
  @Autowired
  private PubLikeDAO pubLikeDAO;

  @Override
  public void showPubList(PubListVO pubListVO) {
    try {
      PubListResult result = pubQueryhandlerService.queryPub(pubListVO.getPubQueryDTO());
      if (result.status.equals(V8pubConst.SUCCESS)) {
        pubListVO.setResultList(result.getResultList());
        pubListVO.setTotalCount(result.totalCount);
      }

      if (result.getResultList() != null && result.getResultList().size() > 0) {

      }
      logger.debug("success");
    } catch (Exception e) {
      logger.error("查询成果列表 异常", e);
    }

  }

  /**
   * 初始化信息
   * 
   * @param pubListVO
   * @throws ServiceException
   */
  public void initInfo(PubInfo pubInfo, Long userId) throws ServiceException {
    PubStatisticsPO pubStatistics = pubStatisticsDAO.get(pubInfo.getPubId());
    if (pubStatistics == null) {
      pubInfo.setShareCount(0);
      pubInfo.setAwardCount(0);
    } else {
      Integer shareCount = pubStatistics.getShareCount() == null ? 0 : pubStatistics.getShareCount();
      pubInfo.setShareCount(shareCount);
      Integer awardCount = pubStatistics.getAwardCount() == null ? 0 : pubStatistics.getAwardCount();
      pubInfo.setAwardCount(awardCount);
    }
    long count = pubLikeDAO.getLikeRecord(pubInfo.getPubId(), userId);
    if (count > 0) {
      pubInfo.setIsAward(1);
    }

  }

  @Override
  public void dealPubStatistics(PubListVO pubListVO) {
    // 获取统计数
    PsnStatistics ps = psnStatisticsService.getPsnStatistics(pubListVO.getPsnId());
    if (ps != null) {
      pubListVO.setCitedSum(ps.getCitedSum() != null ? ps.getCitedSum() : 0);
      pubListVO.setTotalCount(ps.getPubSum() != null ? ps.getPubSum() : 0);
      pubListVO.sethIndex(ps.getHindex() != null ? ps.getHindex() : 0);
      pubListVO.setConfirmCount(ps.getPcfPubSum() != null ? ps.getPcfPubSum() : 0);
    }
  }

}
