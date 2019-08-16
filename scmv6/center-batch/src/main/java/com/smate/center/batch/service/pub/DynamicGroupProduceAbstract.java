package com.smate.center.batch.service.pub;

import java.io.Serializable;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.mq.CommendDynamicMessage;
import com.smate.center.batch.service.pub.mq.CommendGroupDynamicMessageProducer;

/**
 * 群组动态服务抽象实现类.
 * 
 * @author mjg
 * 
 */
@Service("dynamicGroupProduceService")
@Transactional(rollbackFor = Exception.class)
public abstract class DynamicGroupProduceAbstract implements DynamicGroupProduceService, Serializable {

  private static final long serialVersionUID = -5844518975777048406L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  protected DynamicBuildJsonService dynamicBuildJsonService;
  @Autowired
  private CommendGroupDynamicMessageProducer commendGroupDynamicMessageProducer;

  /**
   * 生成群组动态.
   */
  @Override
  public abstract void produceGroupDynamic(String jsonParam);

  /**
   * 生成群组动态公用入口.
   */
  @Override
  public void produceGroupDynamic(JSONObject jsonObject, String dynJson, boolean extFlag) {
    // 改成通用群组动态生成 mq 所有 逻辑不变 都抽取到mq中处理 tsz_2015.1.12;
    CommendDynamicMessage message = new CommendDynamicMessage();
    message.setDynJson(dynJson);
    message.setJsonObject(jsonObject);
    message.setExtFlag(extFlag);
    try {
      commendGroupDynamicMessageProducer.syncCommendDynamicMessage(message);
    } catch (ServiceException e) {
      e.printStackTrace();
      logger.error("发送群组动态入口消息失败。", e);
    }
  }

}
