package com.smate.center.batch.jobdetail.wechatpreprocesspsn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.wechat.WeChatMessagePsn;
import com.smate.center.batch.service.WeChatPreProcessPsnTaskService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class WeChatPreProcessPsnReader implements ItemReader<List<WeChatMessagePsn>> {

  @Autowired
  WeChatPreProcessPsnTaskService weChatPreProcessPsnTaskService;

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public List<WeChatMessagePsn> read()
      throws BatchTaskException, UnexpectedInputException, ParseException, NonTransientResourceException {
    List<WeChatMessagePsn> taskList = new ArrayList<WeChatMessagePsn>();
    taskList = weChatPreProcessPsnTaskService.getWeChatMessagePsnListByStatus(0);
    if (CollectionUtils.isEmpty(taskList)) {
      logger.debug("===========WeChatPreProcessPsnTask, WeChatPreProcessPsn表中无需要处理的数据!============");
      return null;
    } else {
      return taskList;
    }
  }

}
