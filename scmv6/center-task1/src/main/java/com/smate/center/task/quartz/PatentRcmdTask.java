package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.model.innocity.InnoCityPatRcmdForReq;
import com.smate.center.task.single.service.pub.PatentAndRequirementRcmdService;

/**
 * 创新城需求书推荐专利任务
 * 
 * 
 * 
 **/
public class PatentRcmdTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public PatentRcmdTask() {
    super();
  }

  public PatentRcmdTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private PatentAndRequirementRcmdService patentAndRequirementRcmdService;

  public void doRun() throws Exception {

    if (!super.isAllowExecution()) {
      logger.info("PatentRcmdTask已经关闭！");
      return;
    }

    List<InnoCityPatRcmdForReq> reqList = patentAndRequirementRcmdService.getToHandleRequirementList();
    if (reqList == null || reqList.size() == 0) {
      logger.info("PatentRcmdTask无需要处理数据！");
      return;
    }

    for (InnoCityPatRcmdForReq req : reqList) {
      try {
        String extractKws = patentAndRequirementRcmdService.patRcmdForRequirement(req);
        if (StringUtils.isNotEmpty(extractKws)) {
          if (extractKws.startsWith("rsNull||")) {
            // 推荐结果为空
            patentAndRequirementRcmdService.updatePatRcmdForReqStatus(req, 4, extractKws);
          } else {
            patentAndRequirementRcmdService.updatePatRcmdForReqStatus(req, 1, extractKws);
          }
        } else {
          // 没有获取到关键词，进行标记
          patentAndRequirementRcmdService.updatePatRcmdForReqStatus(req, 2);
        }
      } catch (Exception e) {
        logger.error("PatentRcmdTask推荐错误，requirement id：" + req.getReqId() + " !", e);
        patentAndRequirementRcmdService.updatePatRcmdForReqStatus(req, 3);
      }
    }

  }


}
