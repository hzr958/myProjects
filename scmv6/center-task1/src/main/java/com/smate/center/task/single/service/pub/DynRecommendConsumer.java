package com.smate.center.task.single.service.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.single.constants.PdwhDynamicConstants;

/**
 * 推荐系统推送推荐动态生成信息消费者.
 * 
 * @author cxr
 * 
 */
@Component("dynRecommendConsumer")
public class DynRecommendConsumer {

  private static Logger logger = LoggerFactory.getLogger(DynRecommendConsumer.class);

  @Autowired
  private DynamicRecommendService dynamicRecommendService;

  @SuppressWarnings("unchecked")
  public void receive(PubAllBuildReDynMessage message) throws ServiceException {
    try {
      // 基准库推送推荐动态.
      PubAllBuildReDynMessage requestMessage = (PubAllBuildReDynMessage) message;
      if (requestMessage.getActionType().equals(PdwhDynamicConstants.DYN_RCMD_ACTION_DEL)) {
        dynamicRecommendService.delDynRecommendBatch(requestMessage.getReDynParamList(), requestMessage.getDynReType());
      } else {
        dynamicRecommendService.saveRecommendDyn(requestMessage.getDynReType(), requestMessage.getReDynParamList());
      }

    } catch (Exception e) {
      logger.error("推荐系统推送推荐动态生成信息时出错啦！", e);
      throw new ServiceException(e);
    }
  }

}
