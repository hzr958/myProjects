package com.smate.center.batch.chain.pub;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.service.pub.PublicationService;
import com.smate.center.batch.service.utils.BatchRestfulUtilsService;


/**
 * 处理动态
 * 
 * @author liqinghua
 * 
 */
public class PublicationDynamicTask implements IPubXmlTask {

  /**
   * 
   */
  private final String name = "pub_dynamic";
  @Autowired
  private PublicationService publicationService;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private BatchRestfulUtilsService batchRestfulUtilsService;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {

    // 如果是批量导入 外面批量生成动态 不需要每一条成果都生成一条动态。
    if ((context.getIsBatch() != null && "yes".equals(context.getIsBatch()))) {
      return false;
    }
    return true;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    Assert.notNull(context.getPubTypeId());
    Assert.notNull(context.getCurrentUserId());
    Assert.notNull(context.getArticleType());
    Assert.notNull(context.getCurrentAction());
    Assert.notNull(context.getCurrentNodeId());

    // 判断是更新还是新增
    if (context.getCurrentAction().equals(XmlOperationEnum.Enter)
        || context.getCurrentAction().equals(XmlOperationEnum.Import)) {
      publicationService.pubCreateDynamic(xmlDocument, context);
    } else if (context.getCurrentAction().equals(XmlOperationEnum.Edit)) {
      publicationService.updateFullTextDynamic(xmlDocument, context);
    }

    return true;
  }
}
