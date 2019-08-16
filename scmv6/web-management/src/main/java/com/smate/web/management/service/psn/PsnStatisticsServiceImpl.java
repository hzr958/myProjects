package com.smate.web.management.service.psn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;

/**
 * 人员信息统计服务接口 实现
 * 
 * @author tsz
 *
 */

@Service("psnStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class PsnStatisticsServiceImpl implements PsnStatisticsService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;

  @Override
  public PsnStatistics getPsnStatistics(Long psnId) {

    return psnStatisticsDao.getPsnStatistics(psnId);
  }


}
