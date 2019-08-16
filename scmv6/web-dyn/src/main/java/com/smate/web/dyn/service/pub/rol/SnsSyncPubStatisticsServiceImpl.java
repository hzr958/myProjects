package com.smate.web.dyn.service.pub.rol;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.consts.PubStatisticsConstant;
import com.smate.core.base.pub.consts.StatisticsSyncConstant;
import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.dao.pub.rol.SnsSyncPubStatisticsDao;
import com.smate.web.dyn.model.pub.rol.SnsSyncPubStatistics;

/**
 * SNS同步成果相关统计.
 * 
 * @author xys
 * 
 */
@Service("snsSyncPubStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class SnsSyncPubStatisticsServiceImpl implements SnsSyncPubStatisticsService {

  /**
   * 
   */
  private static final long serialVersionUID = -8078105600353670621L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SnsSyncPubStatisticsDao snsSyncPubStatisticsDao;

  @Autowired
  private PubStatisticsService pubStatisticsService;

  @Override
  public void handleSyncStatics(Long siePubId, Long snsPubId, Integer actionType, Integer syncValue)
      throws DynException {
    if (siePubId != null && siePubId.longValue() > 0 && snsPubId != null && snsPubId.longValue() > 0) {
      this.saveOrUpdatePubStatics(siePubId, snsPubId, actionType, syncValue);
    }
  }

  @Override
  public void saveOrUpdatePubStatics(Long siePubId, Long snsPubId, Integer actionType, Integer syncValue)
      throws DynException {
    try {
      SnsSyncPubStatistics pubStis = this.snsSyncPubStatisticsDao.findPubStatistics(siePubId, snsPubId);
      if (pubStis == null) {
        pubStis = new SnsSyncPubStatistics(siePubId, snsPubId);
      }
      int statisticType = 0;
      Long addValue = 0l;
      switch (actionType) {
        case StatisticsSyncConstant.ACTION_TYPE_READ:
          statisticType = PubStatisticsConstant.STATISTIC_TYPE_READ;
          pubStis.setReadNum(pubStis.getReadNum() + syncValue);
          addValue = pubStis.getReadNum();
          break;
        case StatisticsSyncConstant.ACTION_TYPE_AWARD:
          statisticType = PubStatisticsConstant.STATISTIC_TYPE_AWARD;
          pubStis.setAwardNum(pubStis.getAwardNum() + syncValue);
          addValue = pubStis.getAwardNum();
          break;
        case StatisticsSyncConstant.ACTION_TYPE_SHARE:
          statisticType = PubStatisticsConstant.STATISTIC_TYPE_SHARE;
          pubStis.setShareNum(pubStis.getShareNum() + syncValue);
          addValue = pubStis.getShareNum();
          break;
        case StatisticsSyncConstant.ACTION_TYPE_DOWNLOAD:
          statisticType = PubStatisticsConstant.STATISTIC_TYPE_DOWNLOAD;
          pubStis.setDownloadNum(pubStis.getDownloadNum() + syncValue);
          addValue = pubStis.getDownloadNum();
          break;

        default:
          break;
      }
      if (statisticType > 0) {
        this.snsSyncPubStatisticsDao.save(pubStis);
        // 保存或更新成果相关统计
        this.pubStatisticsService.saveOrUpdatePubStatisticNew(pubStis.getSiePubId(), statisticType, addValue);
      }
    } catch (Exception e) {
      logger.error(
          "保存或更新SNS同步的成果相关统计出现问题！siePubId=" + siePubId + "，actionType=" + actionType + ",syncValue=" + syncValue, e);
      throw new DynException(
          "保存或更新SNS同步的成果相关统计出现问题！siePubId=" + siePubId + "，actionType=" + actionType + ",syncValue=" + syncValue, e);
    }
  }

  @Override
  public Long getSnsSyncPubStatistic(Long siePubId, int statType) throws DynException {
    String fieldName = "";
    switch (statType) {
      case PubStatisticsConstant.STATISTIC_TYPE_READ:// 阅读统计
        fieldName = PubStatisticsConstant.FIELD_NAME_READ;
        break;
      case PubStatisticsConstant.STATISTIC_TYPE_AWARD:// 赞统计
        fieldName = PubStatisticsConstant.FIELD_NAME_AWARD;
        break;
      case PubStatisticsConstant.STATISTIC_TYPE_SHARE:// 分享统计
        fieldName = PubStatisticsConstant.FIELD_NAME_SHARE;
        break;
      case PubStatisticsConstant.STATISTIC_TYPE_DOWNLOAD:// 下载数统计
        fieldName = PubStatisticsConstant.FIELD_NAME_DOWNLOAD;
        break;

      default:
        break;
    }
    if (!StringUtils.isBlank(fieldName)) {
      return this.snsSyncPubStatisticsDao.getSnsSyncPubStatistic(siePubId, fieldName);
    }
    return null;
  }

}
