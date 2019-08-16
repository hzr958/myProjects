package com.smate.center.batch.tasklet.wechatmsg;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.wechat.WeChatPreProcessPsn;
import com.smate.center.batch.service.WeChatMsgPsnService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.exception.BatchTaskException;

public class WeChatMsgTasklet extends BaseTasklet {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private WeChatMsgPsnService weChatMsgPsnService;

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    String withData = String.valueOf(jobContentMap.get("msg_id"));
    Long id = Long.parseLong(withData);
    WeChatPreProcessPsn msg = weChatMsgPsnService.getUnProcessedWeChatPreProcessPsnById(id);
    Long msgId = msg.getId();
    // 发送消息,一个jason格式的内容，具体见xys邮件;一个access token，找tsz；组成一个map。map的键值在WeChatConstant.java中查找
    try {
      // 一个smateopenid可能对应多个微信Openid，需要发送多条微信消息
      List<Map> msgSentFeedbackMaps = weChatMsgPsnService.sendMsgPsn(msg);

      // 处理微信的返回值
      for (Map msgSentFeedback : msgSentFeedbackMaps) {
        int errorCode = (int) msgSentFeedback.get(WeChatConstant.ERRCODE_KEY);
        String errorMsg = msgSentFeedback.get(WeChatConstant.ERRMSG_KEY).toString();

        if (errorCode == WeChatConstant.ERRCODE_0 && errorMsg.equals(WeChatConstant.ERRMSG_OK)) {
          // 更新状态，并把成功发送的消息和任务写入历史表
          continue;
        } else {
          String eMsg = "WeChatMsgPsnTask发送信息微信返回错误, msgId = " + msgId + "; errorMsg= " + errorMsg;
          logger.error(eMsg);
          weChatMsgPsnService.updateStatusError(msgId);
        }
      }

      weChatMsgPsnService.updateStatusSuccess(msgId);
    } catch (InterruptedException e) {
      // 任务超时后，线程被强制停止时会抛出InterruptedException，此时做处理，返回即关闭当前线程
      weChatMsgPsnService.updateStatusError(msgId);
      logger.debug("WeChatMsgTasklet捕获InterruptedException", e);
      return;
    } catch (Exception e) {
      weChatMsgPsnService.updateStatusError(msgId);
      logger.debug("WeChatMsgTasklet发送微信信息出错", e);
      throw new BatchTaskException(e);
    }
  }

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    Long id = Long.parseLong(withData);
    WeChatPreProcessPsn msgCorr = weChatMsgPsnService.getWeChatPreProcessPsnById(id);

    // 判断列表中对应job为空是真正为空；为空，则抛出为找到对应信息异常
    if (msgCorr == null) {
      return DataVerificationStatus.NULL;
    }

    return DataVerificationStatus.TRUE;
  }
}
