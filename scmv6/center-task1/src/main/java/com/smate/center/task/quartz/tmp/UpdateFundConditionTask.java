package com.smate.center.task.quartz.tmp;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.fund.sns.FundRecommendConditions;
import com.smate.center.task.service.tmp.UpdateFundConditionTaskService;

/**
 * 
 * @author LTL
 * @date 2018年4月12日
 */
public class UpdateFundConditionTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private UpdateFundConditionTaskService updateFundConditionTaskService;
  private final static Integer SIZE = 500; // 每次处理的个数

  public UpdateFundConditionTask() {
    super();
  }

  public UpdateFundConditionTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========UpdateFundConditionTask已关闭==========");
      return;
    }
    List<FundRecommendConditions> fundList = null;
    try {
      fundList = updateFundConditionTaskService.getFundRecommendConditions(SIZE);
      if (CollectionUtils.isNotEmpty(fundList)) {
        for (FundRecommendConditions fund : fundList) {
          updateFundConditionTaskService.updateFundRecommend(fund);
          updateFundConditionTaskService.updateFundRecommendConditionStatus(fund.getPsnId());
        }
      }
    } catch (Exception e) {
      logger.error("UpdateFundConditionTask获取更新数据失败", e);
    }

    if (CollectionUtils.isEmpty(fundList)) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("UpdateFundConditionTask关闭失败", e);
      }
    }
    for (FundRecommendConditions fund : fundList) {
      try {
        updateFundConditionTaskService.updateFundRecommend(fund);
      } catch (Exception e) {
        logger.error("UpdateFundConditionTask 匹配错误，关键词id:" + fund.getPsnId(), e);
      }
    }

  }

}
