package com.smate.center.batch.chain.pub;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.psn.PsnStatisticsService;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.service.pub.PubReaderService;
import com.smate.center.batch.service.pub.PubRelatedService;
import com.smate.center.batch.service.pub.PublicationService;
import com.smate.center.batch.service.pub.mq.PsnStatisticsRefreshMessageProducer;
import com.smate.center.batch.util.pub.PublicationArticleType;
import com.smate.core.base.utils.constant.PubXmlConstants;


/**
 * @author lichangwen 成果编辑保存时同步更新其它相关
 */
public class PubSyncOtherTask implements IPubXmlTask {
  private String name = "pub_syncOther";
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private PubRelatedService pubRelatedService;
  @Autowired
  private PubReaderService pubReaderService;
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private PsnStatisticsRefreshMessageProducer psnStatisticsRefreshMessageProducer;

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return true;
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    // 更新人员成果信息统计；并不是真正的mq，只是标记使用多数据源保存数据
    psnStatisticsRefreshMessageProducer.refreshPsnPubStatistics(context.getCurrentUserId());
    // 同步更新成果相关文献推荐任务Id表
    pubRelatedService.saveTaskPubRelatedIds(context.getCurrentPubId());
    // 同步更新成果读者推荐任务Id表
    if (context.getArticleType() == PublicationArticleType.OUTPUT) {
      pubReaderService.saveTaskPubReadIds(context.getCurrentPubId());
    }

    // 同步保存pub_know表中
    String authorMatchOwner = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "match_owner");
    int isOwner = 0;
    if (StringUtils.isNotBlank(authorMatchOwner) && "true".equals(authorMatchOwner)) {
      isOwner = 1;// 如果配置上了，则设置为1
    }
    publicationService.savePubKnow(context.getCurrentPubId(), context.getCurrentUserId(), isOwner);
    return true;
  }
}
