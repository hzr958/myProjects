package com.smate.center.task.quartz.tmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.tmp.FundAgencyAddressService;

/**
 * a
 * 
 * @author zx
 *
 */
public class FundAgencyAddressTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 5000; // 每次处理的个数
  @Autowired
  private FundAgencyAddressService fundAgencyAddressService;

  public FundAgencyAddressTask() {
    super();
  }

  public FundAgencyAddressTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========FundAgencyAddressTask已关闭==========");
      return;

    }
    try {
      fundAgencyAddressService.startProcessing();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      logger.error("FundAgencyAddressTask出错！", e);
    }
  }

}
