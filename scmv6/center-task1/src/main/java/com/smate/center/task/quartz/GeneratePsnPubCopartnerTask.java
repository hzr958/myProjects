package com.smate.center.task.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.AppSettingConstants;
import com.smate.center.task.base.AppSettingContext;
import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.psn.GeneratePsnCopartnerService;
import com.smate.core.base.utils.collections.ListUtils;

public class GeneratePsnPubCopartnerTask extends TaskAbstract {
  private static final int BATCH_SIZE = 50;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GeneratePsnCopartnerService generatePsnPubCopartnerService;


  public GeneratePsnPubCopartnerTask() {
    super();
  }

  public GeneratePsnPubCopartnerTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    Long lastPsnId = startPsnId() == null ? 0L : startPsnId();
    List<Long> psnIdlist = generatePsnPubCopartnerService.gethandlePubPsnId(lastPsnId, BATCH_SIZE);
    try {
      if (CollectionUtils.isEmpty(psnIdlist)) {
        super.closeOneTimeTask();
        return;
      }
      for (Long psnId : psnIdlist) {
        try {
          // 先删除成果合作者记录
          generatePsnPubCopartnerService.deletePsnCopartner(psnId, 1);
          List<Long> PsnPubIdList = generatePsnPubCopartnerService.getPsnPubIdList(psnId);
          if (CollectionUtils.isNotEmpty(PsnPubIdList)) {
            List<Long> pdwhPubIdList = new ArrayList<Long>();
            if (PsnPubIdList.size() > 1000) {
              // 根据个人库PubId找到基准库PubId
              List<Long>[] split = ListUtils.split(PsnPubIdList, 1000);
              for (List<Long> list : split) {
                List<Long> pdwhPubIds = generatePsnPubCopartnerService.getpdwhPubIds(list);
                pdwhPubIdList.addAll(pdwhPubIds);
              }
            } else {
              // 根据个人库PubId找到基准库PubId
              pdwhPubIdList = generatePsnPubCopartnerService.getpdwhPubIds(PsnPubIdList);
            }
            if (CollectionUtils.isEmpty(pdwhPubIdList)) {
              continue;
            }
            // 根据基准库成果找到成果合作者
            generatePsnPubCopartnerService.savePsnPubCopartner(psnId, pdwhPubIdList);
          }


        } catch (Exception e) {
          logger.error("=========计算成果合作者出错===========", e);
        }

      }
      generatePsnPubCopartnerService.upAppSettingConstant(AppSettingConstants.SNS_PSN_PUB_COPARTNER_START,
          psnIdlist.get(psnIdlist.size() - 1));
    } catch (Exception e) {
      logger.error("=========GeneratePsnPubCopartnerTask出错===========", e);
    }
  }

  private Long startPsnId() {
    return AppSettingContext.getLongValue(AppSettingConstants.SNS_PSN_PUB_COPARTNER_START);
  }
}
