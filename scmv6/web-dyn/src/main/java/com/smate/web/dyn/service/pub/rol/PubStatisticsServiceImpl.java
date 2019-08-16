package com.smate.web.dyn.service.pub.rol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.consts.PubStatisticsConstant;
import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.dao.pub.rol.PubStatisticsDao;
import com.smate.web.dyn.model.pub.rol.PubStatistics;

/**
 * 成果统计.
 * 
 * @author xys
 * 
 */
@Service("pubStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class PubStatisticsServiceImpl implements PubStatisticsService {

  private static final long serialVersionUID = 534076136865672318L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubStatisticsDao pubStatisticsDao;
  @Autowired
  private SiePubStatisticsService siePubStatisticsService;
  @Autowired
  private SnsSyncPubStatisticsService snsSyncPubStatisticsService;

  @Override
  public void saveOrUpdatePubStatistic(Long pubId, int statType) throws DynException {
    try {
      Long statNum = 0l;
      Long sieStatNum = this.siePubStatisticsService.getSiePubStatistic(pubId, statType);// SIE成果相关统计
      Long snsSyncStatNum = this.snsSyncPubStatisticsService.getSnsSyncPubStatistic(pubId, statType);// SNS同步的成果相关统计
      sieStatNum = sieStatNum == null ? 0l : sieStatNum;
      snsSyncStatNum = snsSyncStatNum == null ? 0l : snsSyncStatNum;
      statNum = sieStatNum + snsSyncStatNum;
      PubStatistics pubStatistics = this.pubStatisticsDao.get(pubId);
      if (pubStatistics == null) {
        pubStatistics = new PubStatistics(pubId);
      }
      switch (statType) {
        case PubStatisticsConstant.STATISTIC_TYPE_READ:// 阅读统计
          pubStatistics.setReadNum(statNum);
          break;
        case PubStatisticsConstant.STATISTIC_TYPE_AWARD:// 赞统计
          pubStatistics.setAwardNum(statNum);
          break;
        case PubStatisticsConstant.STATISTIC_TYPE_SHARE:// 分享统计
          pubStatistics.setShareNum(statNum);
          break;
        case PubStatisticsConstant.STATISTIC_TYPE_DOWNLOAD:// 下载量统计
          pubStatistics.setDownloadNum(statNum);

        default:
          break;
      }
      this.pubStatisticsDao.save(pubStatistics);
    } catch (Exception e) {
      logger.error("保存或更新指定统计类型的成果统计错误：pubId:{},statType:{}", pubId, statType, e);
      throw new DynException(e);
    }
  }

  @Override
  public void saveOrUpdatePubStatisticNew(Long pubId, int statType, Long snsSyncStatNum) throws DynException {
    try {
      Long statNum = 0l;
      Long sieStatNum = this.siePubStatisticsService.getSiePubStatistic(pubId, statType);// SIE成果相关统计
      sieStatNum = sieStatNum == null ? 0l : sieStatNum;
      snsSyncStatNum = snsSyncStatNum == null ? 0l : snsSyncStatNum;
      statNum = sieStatNum + snsSyncStatNum;
      PubStatistics pubStatistics = this.pubStatisticsDao.get(pubId);
      if (pubStatistics == null) {
        pubStatistics = new PubStatistics(pubId);
      }
      switch (statType) {
        case PubStatisticsConstant.STATISTIC_TYPE_READ:// 阅读统计
          pubStatistics.setReadNum(statNum);
          break;
        case PubStatisticsConstant.STATISTIC_TYPE_AWARD:// 赞统计
          pubStatistics.setAwardNum(statNum);
          break;
        case PubStatisticsConstant.STATISTIC_TYPE_SHARE:// 分享统计
          pubStatistics.setShareNum(statNum);
          break;
        case PubStatisticsConstant.STATISTIC_TYPE_DOWNLOAD:// 下载量统计
          pubStatistics.setDownloadNum(statNum);

        default:
          break;
      }
      this.pubStatisticsDao.save(pubStatistics);
    } catch (Exception e) {
      logger.error("保存或更新指定统计类型的成果统计错误：pubId:{},statType:{}", pubId, statType, e);
      throw new DynException(e);
    }

  }
}
