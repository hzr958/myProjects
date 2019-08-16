package com.smate.center.task.single.service.pub;

public interface MyProjectQueryService {
  /**
   * 获取个人的项目公开数
   */
  public Long getOpenPrjSum(Long psnId);
}
