package com.smate.center.task.quartz;

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

public class GeneratePsnPrjCopartnerTask extends TaskAbstract {
  private static final int BATCH_SIZE = 50;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GeneratePsnCopartnerService generatePsnCopartnerService;


  public GeneratePsnPrjCopartnerTask() {
    super();
  }

  public GeneratePsnPrjCopartnerTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    Long lastPsnId = startPsnId() == null ? 0L : startPsnId();
    List<Long> psnIdlist = generatePsnCopartnerService.gethandlePrjPsnId(lastPsnId, BATCH_SIZE);
    try {
      if (CollectionUtils.isEmpty(psnIdlist)) {
        super.closeOneTimeTask();
        return;
      }
      for (Long psnId : psnIdlist) {
        // 删除项目合作者记录
        generatePsnCopartnerService.deletePsnCopartner(psnId, 2);
        // 找到项目群组
        List<Long> prjGroupIds = generatePsnCopartnerService.findPrjGroupIdsByPsnId(psnId);
        if (CollectionUtils.isEmpty(prjGroupIds)) {
          continue;
        }
        for (Long prjGroupId : prjGroupIds) {
          // 根据项目群组找到合作者
          List<Long> coPsnIdList = generatePsnCopartnerService.getPrjCoBygrpId(prjGroupId, psnId);
          // 保存项目合作者记录
          generatePsnCopartnerService.savePsnPrjCopartner(psnId, coPsnIdList, prjGroupId);
        }
      }
      generatePsnCopartnerService.upAppSettingConstant(AppSettingConstants.SNS_PSN_PRJ_COPARTNER_START,
          psnIdlist.get(psnIdlist.size() - 1));
    } catch (Exception e) {
      logger.error("=========GeneratePsnPrjCopartnerTask出错===========", e);
    }
  }

  private Long startPsnId() {
    return AppSettingContext.getLongValue(AppSettingConstants.SNS_PSN_PRJ_COPARTNER_START);
  }
}
