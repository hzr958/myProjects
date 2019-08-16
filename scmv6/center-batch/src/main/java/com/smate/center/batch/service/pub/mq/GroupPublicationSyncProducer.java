package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 成果与群组的同步.
 * 
 * <pre>
 * 有以下一个同步:
 * 	<li>成果加入群组时，同步到群组的创建节点。</li>
 * 	<li>成果编辑时，同步到群组的创建节点。</li>
 * 	<li>成果删除时，同步到群组的创建节点。</li>
 * 	<li>从“我的成果”中把成果移除群组时，同步到群组的创建节点。</li>
 * 	<li>从“我的群组”中把成果移除群组时，同步到群组的创建节点。</li>
 * 	<li>群组中推荐成果时，同步成果到推荐的节点，并同步成果信息到推荐历史表.</li>
 * 
 * </pre>
 * 
 * @author LY
 * 
 */
@Component
public class GroupPublicationSyncProducer {

  @Autowired
  private GroupPublicationSyncMsgConsumer groupPublicationSyncMsgConsumer;

  /**
   * 成果移除群组. mypub,mygroup.
   * 
   * @param message
   * @throws ServiceException
   */
  public void publicationRemoveGroupSync(GroupPubRemoveSyncMessage message) throws ServiceException {
    if (message.getPsnId() == null || message.getPubId() == null || message.getGroupId() == null
        || message.getOpAction() == null) {
      throw new ServiceException("成果移除群组信息不全.");
    }
    groupPublicationSyncMsgConsumer.receive(message);
  }
}
