package com.smate.center.task.service.tmp;

import java.util.List;

import com.smate.center.task.model.fund.sns.FundRecommendConditions;

public interface UpdateFundConditionTaskService {
  public List<FundRecommendConditions> getFundRecommendConditions(Integer maxSize) throws Exception;

  public void updateFundRecommend(FundRecommendConditions fundRecommend) throws Exception;

  public void updateFundRecommendConditionStatus(Long psnId) throws Exception;
}
