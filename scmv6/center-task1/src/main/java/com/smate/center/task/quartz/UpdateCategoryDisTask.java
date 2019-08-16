package com.smate.center.task.quartz;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.fund.rcmd.ConstFundCategoryDis;
import com.smate.center.task.service.fund.ConstFundCategoryDisService;

public class UpdateCategoryDisTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ConstFundCategoryDisService constFundCategoryDisService;

  public UpdateCategoryDisTask() {
    super();
  }

  public UpdateCategoryDisTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      logger.info("=========UserClassificationTask已关闭==========");
      return;
    }
    try {
      super.closeOneTimeTask();
      List<Map<String, Object>> lists = constFundCategoryDisService.getAllCategoryDis();
      for (Map<String, Object> map : lists) {
        try {
          List<Long> scmCategoryIds = constFundCategoryDisService.getScmIdByDiscCode(map.get("discCode").toString());
          if (scmCategoryIds.size() == 0 && map.get("discCode").toString().length() > 3) {
            scmCategoryIds =
                constFundCategoryDisService.getScmIdByDiscCode(map.get("discCode").toString().substring(0, 3));
          }
          if (scmCategoryIds.size() > 1) {
            constFundCategoryDisService.updateCategroyDis(scmCategoryIds.get(0),
                Long.valueOf(map.get("id").toString()));
            constFundCategoryDisService
                .saveCategoryDis(new ConstFundCategoryDis(Long.valueOf(map.get("categoryId").toString()),
                    scmCategoryIds.get(1), Long.valueOf(scmCategoryIds.get(1).toString().substring(0, 1))));
          } else if (scmCategoryIds.size() == 1) {
            constFundCategoryDisService.updateCategroyDis(scmCategoryIds.get(0),
                Long.valueOf(map.get("id").toString()));
          }
        } catch (Exception e) {
          logger.error("更新历史科技领域出错基金类别id为:" + map.get("categoryId").toString(), e);
        }
      }
    } catch (TaskException e1) {
      logger.error("更新历史科技领域出错", e1);
    }
  }
}
