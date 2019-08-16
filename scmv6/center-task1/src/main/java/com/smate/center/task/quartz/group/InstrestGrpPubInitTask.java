package com.smate.center.task.quartz.group;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.group.GrpService;

public class InstrestGrpPubInitTask extends TaskAbstract {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpService grpService;

  public InstrestGrpPubInitTask() {

  }

  public InstrestGrpPubInitTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========InstrestGrpPubInitTask 已关闭==========");
      return;
    }
    List<Map<String, String>> grpMapList = grpService.getInstrestGrpInfo();
    try {
      for (Map<String, String> map : grpMapList) {
        Long grpId = Long.valueOf(map.get("grpId").toString());
        // 一个群组只初始化一次
        Boolean flag = grpService.grpHasInit(grpId);
        if (!flag) {
          if (map.get("nsfcCatId") != null) {
            String nsfcCatId = String.valueOf(map.get("nsfcCatId"));
            if (StringUtils.isNotBlank(nsfcCatId)) {
              List<Long> rcmdPdwhPubIds = grpService.getRcmdPdwhPubIds(nsfcCatId);
              for (Long pdwhPubId : rcmdPdwhPubIds) {
                grpService.insertIntoRcmdPdwh(pdwhPubId, grpId);
              }
            }
          }
        }
      }
      super.closeOneTimeTask();
    } catch (Exception e) {
      logger.error("InstrestGrpPubInitTask出错------", e);
    }
  }
}
