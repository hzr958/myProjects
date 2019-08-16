package com.smate.center.task.service.fund;

import java.util.Date;
import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.fund.rcmd.ConstFundCategory;
import com.smate.center.task.model.fund.rcmd.FundRecommendContext;
import com.smate.center.task.model.fund.sns.PsnFundRecommend;

public interface PsnFundRecommendService {

  List<ConstFundCategory> initFundList();

  public List<Long> getTaskPsnList(Long lastPsnId, Integer type) throws ServiceException;

  /**
   * 封装构建必要条件中人员信息.
   * 
   * @param psnId
   * @param context
   * @return
   */
  void buildPsnInfo(Long psnId, FundRecommendContext context);

  void saveReFundList(PsnFundRecommend reFund);

  List<Long> getFundRegionId(Long fundId);

  void deletePsnFundRecommend(Long id, Date updateDate);

  void deletePsnFundRecommend(Long psnId);
}
