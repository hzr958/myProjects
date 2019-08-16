package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.GroupPublicationService;

/**
 * 成果与群组的同步.
 * 
 * <pre>
 * 有以下一个同步:
 * 	<li>成果加入群组时，同步到群组的创建节点。</li>
 * 	<li>成果编辑时，同步到群组的创建节点。</li>
 * 	<li>成果删除时，同步到群组的创建节点。</li>
 * 	<li>成果移除群组时，同步到群组的创建节点。</li>
 * 	<li>群组中推荐成果时，同步成果到推荐的节点，并同步成果信息到推荐历史表.</li>
 * </pre>
 * 
 * sns
 * 
 * @author LY
 * 
 */
@Component("groupPublicationSyncMsgConsumer")
public class GroupPublicationSyncMsgConsumer {

  @Autowired
  private GroupPublicationService groupPublicationService;

  public void receive(GroupPubRemoveSyncMessage message) throws ServiceException {
    // 成果文献加入群组
    if (message instanceof GroupPubRemoveSyncMessage) {
      // 成果文献移除群组.mypub,mygroup.
      GroupPubRemoveSyncMessage receiveMessage = (GroupPubRemoveSyncMessage) message;
      this.groupPublicationService.receivePublicationRemoveGroupSync(receiveMessage);
    }
  }

}
