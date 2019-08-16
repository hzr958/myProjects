package com.smate.center.batch.service.pub;

import java.io.Serializable;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.mq.CommendDynamicMessage;
import com.smate.center.batch.service.pub.mq.CommendDynamicMessageProducer;

public abstract class DynamicProduceAbstract implements DynamicProduceService, Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1754487115351190959L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private static final String ENCODING = "utf-8";
  @Autowired
  protected DynamicExtendService dynamicExtendService;
  @Autowired
  protected DynamicBuildJsonService dynamicBuildJsonService;
  @Autowired
  private CommendDynamicMessageProducer commendDynamicMessageProducer;

  /**
   * 生成动态.
   */
  @Override
  public abstract void produceDynamic(String jsonParam);

  /**
   * 生成动态公用入口.
   */
  @Override
  public void produceDynamic(JSONObject jsonObject, String dynJson, boolean extFlag) {
    // FIXME 2015-10-28 动态 -done
    // 改成通用动态生成 mq 所有 逻辑不变 都抽取到mq中处理 tsz_2015.1.12;
    CommendDynamicMessage message = new CommendDynamicMessage();
    message.setDynJson(dynJson);
    message.setJsonObject(jsonObject);
    message.setExtFlag(extFlag);
    try {
      commendDynamicMessageProducer.syncCommendDynamicMessage(message);
    } catch (ServiceException e) {
      e.printStackTrace();
      logger.error("发送动态入口消息失败。", e);
    }

  }
}
