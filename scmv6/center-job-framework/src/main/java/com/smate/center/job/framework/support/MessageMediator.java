package com.smate.center.job.framework.support;

import com.smate.center.job.framework.support.AbstractMediator;
import com.smate.center.job.framework.support.MessageQueueColleague;
import com.smate.center.job.framework.zookeeper.queue.Message;
import org.springframework.stereotype.Component;

/**
 * @author Created by hcj
 * @date 2018/07/19 15:20
 */
@Component
public class MessageMediator extends AbstractMediator<MessageQueueColleague, Message<String>> {

  public boolean postMessage(String who, Message<String> msg) {
    return notify(who, msg);
  }
}
