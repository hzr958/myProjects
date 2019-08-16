package com.smate.web.dyn.service.pub.rol;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.consts.PubStatisticsConstant;
import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.dao.pub.rol.SiePubStatisticsDao;
import com.smate.web.dyn.model.pub.rol.SiePubStatistics;

/**
 * SIE成果统计.
 * 
 * @author xys
 * 
 */
@Service("siePubStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class SiePubStatisticsServiceImpl implements SiePubStatisticsService {

  private static final long serialVersionUID = -4070014686440251161L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SiePubStatisticsDao siePubStatisticsDao;

  @Override
  public SiePubStatistics getSiePubStatistics(Long pubId) throws DynException {
    return this.siePubStatisticsDao.get(pubId);
  }


  @Override
  public Long getSiePubStatistic(Long pubId, int statType) throws DynException {
    String fieldName = "";
    switch (statType) {
      case PubStatisticsConstant.STATISTIC_TYPE_READ:// 阅读统计
        fieldName = PubStatisticsConstant.FIELD_NAME_READ;
        break;
      case PubStatisticsConstant.STATISTIC_TYPE_AWARD:// 赞统计
        fieldName = PubStatisticsConstant.FIELD_NAME_AWARD;
        break;
      case PubStatisticsConstant.STATISTIC_TYPE_DOWNLOAD:// 下载量统计
        fieldName = PubStatisticsConstant.FIELD_NAME_DOWNLOAD;

      default:
        break;
    }
    if (!StringUtils.isBlank(fieldName)) {
      return this.siePubStatisticsDao.getSiePubStatistic(pubId, fieldName);
    }
    return null;
  }

}
