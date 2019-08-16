package com.smate.center.batch.jobdetail.wechatpreprocesspsn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.wechat.WeChatMessagePsn;
import com.smate.center.batch.service.WeChatPreProcessPsnTaskService;
import com.smate.core.base.utils.exception.BatchTaskException;


public class WeChatPreProcessPsnWriter implements ItemWriter<List<WeChatMessagePsn>> {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  WeChatPreProcessPsnTaskService weChatPreProcessPsnTaskService;

  @Override
  public void write(List<? extends List<WeChatMessagePsn>> items) throws BatchTaskException {
    if (CollectionUtils.isEmpty(items)) {
      logger.debug("============WeChatPreProcessPsn  没有数据!============");
      return;
    }
    List<WeChatMessagePsn> itemList = new ArrayList<WeChatMessagePsn>();

    itemList = items.get(0);

    if (CollectionUtils.isEmpty(itemList)) {
      logger.debug("============WeChatPreProcessPsn  没有数据!============");
      return;
    }

    for (WeChatMessagePsn single : itemList) {

      Long openId = single.getOpenId();
      String content = single.getContent();
      String contentMd5 = DigestUtils.md5Hex(content);
      Long id = single.getId();
      String token = single.getToken();
      Date createTime = single.getCreateTime();

      Long psnId = weChatPreProcessPsnTaskService.getPsnIdByOpenId(openId, token);

      if (psnId == null) {
        logger.debug("WeChatPreProcessPsnTask中weChatMessagePsn对象的OpenId对应的psnId为空，id：" + id + "; openId: " + openId);
        single.setStatus(3);
        weChatPreProcessPsnTaskService.saveToWebChatMessagePsn(single);
        return;
      }

      boolean isDuplicated = weChatPreProcessPsnTaskService.checkDuplicate(single);
      if (isDuplicated) {
        // 如果是重复数据则存入历史表，历史表中重复数+1
        weChatPreProcessPsnTaskService.saveToHistory(single, id, content, contentMd5, openId, createTime, psnId, token);
      } else {
        // 如果不是重复数据，则存入preprocesspsn表
        weChatPreProcessPsnTaskService.saveToPreProcess(single, id, content, contentMd5, openId, createTime, psnId,
            token);
        // 同时向历史表中插入新记录
        weChatPreProcessPsnTaskService.saveToHistory(single, id, content, contentMd5, openId, createTime, psnId, token);
        // 并向batch_jobs中插入一条数据，后续任务发送此消息
        weChatPreProcessPsnTaskService.saveToBatchJobs(single);


      }

    }


  }


}
