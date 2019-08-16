package com.smate.center.oauth.service.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;

/**
 * @author changwen
 * 
 */
@Service("psnStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class PsnStatisticsServiceImpl implements PsnStatisticsService {
  private static final long serialVersionUID = 1875829508379902571L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnStatisticsUpdateService psnStatisticsUpdateService;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;

  @Override
  public PsnStatistics getPsnStatistics(Long psnId) {
    PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
    if (psnStatistics == null) {
      return psnStatisticsUpdateService.initPsnStatics(psnId);
    }
    return psnStatistics;
  }
}
