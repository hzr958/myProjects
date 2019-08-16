package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.pub.Des3PubId;
import com.smate.center.task.service.sns.quartz.GenerateDes3PubIdService;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 批量生成加密pubId
 * 
 * @author LJ
 *
 *         2017年7月5日
 */
public class GenerateDes3PubIdTask extends TaskAbstract {
  private static final int BATCH_SIZE = 200;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GenerateDes3PubIdService generateDes3PubIdService;

  public GenerateDes3PubIdTask() {
    super();
  }

  public GenerateDes3PubIdTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }

    while (true) {
      int index = 0;
      index++;// 分页取数据
      List<Des3PubId> pubIdList = generateDes3PubIdService.getPubIdList(index, BATCH_SIZE);
      if (CollectionUtils.isEmpty(pubIdList)) {
        logger.info("所有的pubid加密处理完毕！");
        break;
      }

      // 单个加密处理
      for (Des3PubId pubId : pubIdList) {
        try {
          generateDes3PubIdService.GenerateDesPubId(pubId);
        } catch (Exception e) {
          logger.error("生成成果加密Id出错，pubId:" + pubId.getPubId(), e);
        }
      }
    }



  }


  // 测试
  public static void main(String[] args) {

    int index = (int) (Math.random() * 9000 + 1000);// 生成随机数
    String desPubString = index + "|" + "1000006571522";
    String encodeToDes3 = Des3Utils.encodeToDes3(desPubString);
    System.out.println(encodeToDes3);
    System.err.println(new GenerateDes3PubIdTask().getCurrentPubId());

  }

  public Long getCurrentPubId() {
    String Des3Id = "7NV3q1drlAKRJOdd%2BV%2FNjc2oXeBof7Az";// |
    Long pubId = null;
    if (pubId == null && !StringUtils.isBlank(Des3Id)) {
      if (StringUtils.indexOf(Des3Id, ",") > -1) {
        String[] ids = StringUtils.split(Des3Id, ",");
        String pId = null;
        pId = ServiceUtil.decodeFromDes3(ids[0]).split(",")[0];
        String[] pidStr = pId.split("\\|");// 兼容“随机数|desid”
        if (pidStr.length >= 2) {
          pId = pidStr[1];
        }
        pubId = Long.valueOf(pId);
      } else {
        String tp = ServiceUtil.decodeFromDes3(Des3Id);
        if (tp != null) {
          String[] pidStr = tp.split("\\|");// 兼容“随机数|desid”
          if (pidStr.length >= 2) {
            tp = pidStr[1];
          }
          pubId = Long.valueOf(tp);
        }
      }

    }

    return pubId;
  }
}
