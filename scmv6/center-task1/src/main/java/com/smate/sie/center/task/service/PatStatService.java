package com.smate.sie.center.task.service;

import com.smate.sie.center.task.model.PatStat;

/**
 * 专利统计表服务
 * 
 * @author hd
 *
 */
public interface PatStatService {

  public Long getPatStAward(Long patId);

  public PatStat getStatistics(Long patId);

}
