package com.smate.sie.center.task.quartz.tmp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.model.tmp.SieTaskSplitPubOrgs;
import com.smate.sie.center.task.service.tmp.SieTaskSplitPubOrgsService;

/**
 * 轮询task_split_pub_orgs，从PUB_JSON 字段拆分署名单位和通讯作者到 orgs、email_author两个字段
 * 
 * @author ztg
 *
 */
public class SieSplitPubOrgsTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 100;// 一次最多处理数量

  @Autowired
  private SieTaskSplitPubOrgsService sieTaskSplitPubOrgsServiceImpl;

  public SieSplitPubOrgsTask() {
    super();
  }


  public SieSplitPubOrgsTask(String beanName) {
    super(beanName);
  }


  public void doRun() throws Exception {

    if (!super.isAllowExecution()) {
      return;
    }

    // 获取表中待处理梳理的条数
    Long count = sieTaskSplitPubOrgsServiceImpl.countNeedHandleKeyId();
    if (count.intValue() == 0) {
      return;
    }
    while (true) {
      // 读取需要统计Id
      List<SieTaskSplitPubOrgs> splitPubOrgsList = sieTaskSplitPubOrgsServiceImpl.loadNeedHandleKeyId(BATCH_SIZE);
      if (splitPubOrgsList == null || splitPubOrgsList.size() == 0) {
        return;
      }
      for (SieTaskSplitPubOrgs splitPubOrgs : splitPubOrgsList) {
        try {
          sieTaskSplitPubOrgsServiceImpl.doSplit(splitPubOrgs);
        } catch (Exception e) {
          logger.error("轮询task_split_pub_orgs表,出错", splitPubOrgs.getPubId(), e);
        }
      }
    }
  }
}

