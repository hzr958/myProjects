package com.smate.center.task.service.sns.quartz;

import java.util.List;

public interface InitPsnRecommendFundService {

  public Integer getApplicationQuartzSettingValue(String name);

  public void closeQuartzApplication(String name);

  void initPsnFundRecommend(List<Long> psnIdList) throws Exception;

  public List<Long> getPsnIds(Long lastPsnId);
}
