package com.smate.center.task.quartz.email;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.group.PrjGrpTmp;
import com.smate.center.task.service.email.PromoteMailInitDataService;
import com.smate.center.task.service.group.SendPrjGrpEmailService;

public class SendPrjGrpEmailTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static final int size = 1000;
  @Autowired
  private SendPrjGrpEmailService sendPrjGrpEmailService;
  @Autowired
  private PromoteMailInitDataService promoteMailInitDataService;

  public SendPrjGrpEmailTask() {
    super();
  }

  public SendPrjGrpEmailTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SavePubPdwhScmRolTask已关闭==========");
      return;
    }
    logger.info("=========SavePubPdwhScmRolTask已开启==========");
    List<PrjGrpTmp> PrjGrpTmpList = sendPrjGrpEmailService.getPrjGrpInfo(size);
    for (PrjGrpTmp prjGrpTmp : PrjGrpTmpList) {
      try {
        Map<String, Object> params = new HashMap<String, Object>();
        Long totalPubs = sendPrjGrpEmailService.getCountGroupPub(prjGrpTmp.getGrpId());
        if (totalPubs == 0) {
          sendPrjGrpEmailService.saveOptResult(prjGrpTmp.getGrpId(), 5);
        } else {
          params.put("totalPubs", totalPubs);
          params = sendPrjGrpEmailService.buildEamilInfo(prjGrpTmp, params);
          promoteMailInitDataService.saveMailInitData(params);
          sendPrjGrpEmailService.saveOptResult(prjGrpTmp.getGrpId(), 1);
        }
      } catch (Exception e) {
        logger.error("保存项目成果邮件出错，grpId为：" + prjGrpTmp.getGrpId(), e);
        sendPrjGrpEmailService.saveOptResult(prjGrpTmp.getGrpId(), 9);
      }
    }
  }

}
