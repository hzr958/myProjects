package com.smate.center.batch.service.pub.mq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.constant.PdwhDynamicConstants;
import com.smate.center.batch.model.pdwh.pub.PdwhPublicationAll;
import com.smate.center.batch.util.pub.LogUtils;
import com.smate.core.base.utils.constant.ServiceConstants;


/**
 * 
 * 推荐论文相关参数到科研之友生成推荐动态广告_MJG_SCM-5988.
 * 
 * @author mjg
 * 
 */
@Component("dynRecommendPubAllProducer")
public class DynRecommendPubAllProducer {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private String queneName = "sendDynRecommendMsg";

  @Autowired
  private DynRecommendConsumer dynRecommendConsumer;

  /**
   * 推荐论文相关参数到科研之友生成推荐动态广告.
   * 
   * @param puball
   */
  @SuppressWarnings("rawtypes")
  public void sendPubAllMsg(Map<Long, PdwhPublicationAll> pubAllMap) {
    try {
      if (pubAllMap != null && pubAllMap.size() > 0) {
        List<DynRecommendThesisForm> formList = new ArrayList<DynRecommendThesisForm>();
        // 遍历成果列表并封装.
        Set<Long> keySet = pubAllMap.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
          Long psnId = Long.valueOf(ObjectUtils.toString(iterator.next()));
          PdwhPublicationAll puball = pubAllMap.get(psnId);
          DynRecommendThesisForm thesisForm = new DynRecommendThesisForm(puball.getId(), puball.getDes3Id(), psnId,
              puball.getDbid(), puball.getZhTitle(), puball.getEnTitle(), puball.getAuthorNames(),
              puball.getBriefDescZh(), puball.getBriefDescEn());

          formList.add(thesisForm);

        }
        // 发送推荐论文相关参数.
        PubAllBuildReDynMessage message = new PubAllBuildReDynMessage(PdwhDynamicConstants.DYN_RECOMMEND_TYPE_THESIS,
            PdwhDynamicConstants.DYN_RCMD_ACTION_SAVE, formList, ServiceConstants.PDWH_NODE_ID);
        sendMessage(message);
      }

    } catch (Exception e) {
      LogUtils.error(logger, e, "推荐论文相关参数到SNS主站生成推荐动态广告失败");
    }
  }

  public void sendRemovePsnPubAllRcmdMsg(List<Long> psnIds) {
    if (CollectionUtils.isEmpty(psnIds)) {
      return;
    }
    try {
      PubAllBuildReDynMessage message = new PubAllBuildReDynMessage(PdwhDynamicConstants.DYN_RECOMMEND_TYPE_THESIS,
          PdwhDynamicConstants.DYN_RCMD_ACTION_DEL, psnIds, ServiceConstants.PDWH_NODE_ID);
      sendMessage(message);
    } catch (Exception e) {
      LogUtils.error(logger, e, "发送移除人员论文推荐动态消息错误");
    }
  }

  private void sendMessage(PubAllBuildReDynMessage message) {
    this.dynRecommendConsumer.receive(message);
  }

}
