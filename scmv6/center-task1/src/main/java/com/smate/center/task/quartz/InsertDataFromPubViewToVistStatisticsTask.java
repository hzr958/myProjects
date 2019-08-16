package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.pdwh.quartz.InsertDataFromPubViewToVistStatisticsService;
import com.smate.center.task.v8pub.sns.po.PubViewPO;

/**
 * 修复成果改造导致VIST_STATISTICS表中缺失的部分数据和部分成果查看数量
 * 用于将数据从v_pub_view表同步到vist_statistics表中的临时任务用于修复浏览数（创建时间2018-10-29）
 * 
 * @author syl
 *
 */
public class InsertDataFromPubViewToVistStatisticsTask extends TaskAbstract {
  private static final int BATCH_SIZE = 520;// 每次操作的数据条数
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InsertDataFromPubViewToVistStatisticsService insertDataFromPubViewToVistStatisticsService;

  public InsertDataFromPubViewToVistStatisticsTask() {
    super();
  }

  public InsertDataFromPubViewToVistStatisticsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========InsertDataFromPubViewToVistStatisticsTask 已关闭==========");
      return;
    }
    Long allNum = insertDataFromPubViewToVistStatisticsService.getCountNum();
    if (allNum != null && allNum != 0) {
      for (int start = 0; start < allNum; start += BATCH_SIZE) {
        List<PubViewPO> list = insertDataFromPubViewToVistStatisticsService.queryNeedInsertData(start, BATCH_SIZE);
        if (CollectionUtils.isNotEmpty(list)) {
          for (PubViewPO pubViewPO : list) {
            if (pubViewPO != null) {
              try {
                insertDataFromPubViewToVistStatisticsService.doInsertData(pubViewPO);
              } catch (Exception e) {
                logger.error("修复成果改造导致VIST_STATISTICS表中缺失的部分数据和部分成果查看数量任务出错，pubId=" + pubViewPO.getPubId(), e);
              }
            }
          }
          list = null;
        }
      }
    }
    try {
      super.closeOneTimeTask();
    } catch (TaskException e) {
    }
  }

}
