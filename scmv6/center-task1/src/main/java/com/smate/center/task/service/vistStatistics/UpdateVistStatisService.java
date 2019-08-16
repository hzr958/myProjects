package com.smate.center.task.service.vistStatistics;

import java.util.List;

import com.smate.center.task.model.sns.psn.VistStatistics;

public interface UpdateVistStatisService {
  public List<VistStatistics> getVistStatisId(Long starId, Integer size) throws Exception;

  public void updateVistRegionId(VistStatistics vist);
}
