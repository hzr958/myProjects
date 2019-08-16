package com.smate.center.task.single.service.pub;

import java.util.List;

import com.smate.center.task.model.sns.pub.PsnStatisticsPubPrj;

/**
 * \ 获取需要统计公开成果和项目的记录
 */
public interface PsnStatisticsPubPrjService {
  /**
   * 获取size条未处理的psnid
   */
  public List<PsnStatisticsPubPrj> getPsnStatisticsList(Integer size);

  public void savePsnStatisticsPubPrj(PsnStatisticsPubPrj p);
}
