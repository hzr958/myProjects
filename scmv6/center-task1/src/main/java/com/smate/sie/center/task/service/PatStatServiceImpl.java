package com.smate.sie.center.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.sie.center.task.dao.SiePatStatDao;
import com.smate.sie.center.task.model.PatStat;

/**
 * 专利统计表服务实现
 * 
 * @author ztg
 *
 */
@Service("patStatService")
@Transactional(rollbackFor = Exception.class)
public class PatStatServiceImpl implements PatStatService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SiePatStatDao sieStPatDao;


  @Override
  public Long getPatStAward(Long patId) {
    return sieStPatDao.getPatStatistic(patId, "awardNum");
  }

  @Override
  public PatStat getStatistics(Long patId) {
    return sieStPatDao.findUniqueBy("patId", patId);
  }

}
