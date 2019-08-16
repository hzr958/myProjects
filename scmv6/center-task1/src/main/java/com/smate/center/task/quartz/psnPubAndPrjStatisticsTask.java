package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.pub.PsnStatisticsPubPrj;
import com.smate.center.task.single.service.person.PsnStatisticsService;
import com.smate.center.task.single.service.pub.MyProjectQueryService;
import com.smate.center.task.single.service.pub.MyPublicationQueryService;
import com.smate.center.task.single.service.pub.PsnStatisticsPubPrjService;
import com.smate.core.base.psn.model.PsnStatistics;

public class psnPubAndPrjStatisticsTask extends TaskAbstract {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MyPublicationQueryService myPublicationQueryService;
  @Autowired
  private MyProjectQueryService myProjectQueryService;
  @Autowired
  private PsnStatisticsPubPrjService psnStatisticsPubPrjService;
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  private final static Integer SIZE = 1000; // 每次刷新获取的个数

  public psnPubAndPrjStatisticsTask() {
    super();
  }

  public psnPubAndPrjStatisticsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    List<PsnStatisticsPubPrj> psnStaList = psnStatisticsPubPrjService.getPsnStatisticsList(SIZE);// 获取待处理数据
    if (psnStaList == null || psnStaList.size() == 0) {
      logger.info("统计个人的公开成果和项目没有需要处理的数据");
      return;
    }
    for (PsnStatisticsPubPrj psnSta : psnStaList) {
      try {
        Long pubSum = myPublicationQueryService.getOpenPub(psnSta.getPsnId());// 获取公开成果数
        Long prjSum = myProjectQueryService.getOpenPrjSum(psnSta.getPsnId());
        psnSta.setPubSum(pubSum);
        psnSta.setPrjSum(prjSum);
        psnSta.setStatus(1);// 1已处理
        psnStatisticsPubPrjService.savePsnStatisticsPubPrj(psnSta);// 保存更新
        PsnStatistics psn = psnStatisticsService.getPsnStatistics(psnSta.getPsnId());
        if (psn != null) {// 更新公开成果和项目的统计数
          psn.setOpenPubSum(pubSum.intValue());
          psn.setOpenPrjSum(prjSum.intValue());
          psnStatisticsService.savePsnStatistics(psn);
        }
      } catch (ServiceException e) {
        psnSta.setStatus(-1);// 设置状态为异常
        psnStatisticsPubPrjService.savePsnStatisticsPubPrj(psnSta);
        logger.error("统计个人公开成果和项目的后台任务出错，psn_id=" + psnSta.getPsnId(), e);
      }

    }
  }
}
