package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.model.innocity.InnoCityReqRcmdForPat;
import com.smate.center.task.single.service.pub.PatentAndRequirementRcmdService;

/**
 * 创新城专利推荐需求书任务
 * 
 * 
 */
public class RequirementRcmdTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public RequirementRcmdTask() {
    super();
  }

  public RequirementRcmdTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private PatentAndRequirementRcmdService patentAndRequirementRcmdService;

  public void doRun() throws Exception {

    if (!super.isAllowExecution()) {
      logger.info("RequirementRcmdTask已经关闭！");
      return;
    }

    List<InnoCityReqRcmdForPat> patList = patentAndRequirementRcmdService.getToHandlePatentList();
    if (patList == null || patList.size() == 0) {
      logger.info("RequirementRcmdTask无需要处理数据！");
      return;
    }

    for (InnoCityReqRcmdForPat pat : patList) {
      try {
        String extractKws = patentAndRequirementRcmdService.reqRcmdForPatent(pat);

        if (StringUtils.isNotBlank(extractKws)) {
          if (extractKws.startsWith("rsNull||")) {
            // 推荐结果为空
            patentAndRequirementRcmdService.updateReqRcmdForPatentStatus(pat, 4, extractKws);
          } else {
            patentAndRequirementRcmdService.updateReqRcmdForPatentStatus(pat, 1, extractKws);
          }
        } else {
          // 没有获取到关键词，进行标记
          patentAndRequirementRcmdService.updateReqRcmdForPatentStatus(pat, 2);
        }
      } catch (Exception e) {
        logger.error("为Patent推荐Requirement错误，patent id：" + pat.getPatentId() + " !", e);
        patentAndRequirementRcmdService.updateReqRcmdForPatentStatus(pat, 3);
      }
    }

  }
}
