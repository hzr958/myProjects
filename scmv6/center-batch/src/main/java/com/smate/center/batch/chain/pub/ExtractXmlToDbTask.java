package com.smate.center.batch.chain.pub;

import javax.annotation.Resource;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.service.pub.PublicationSaveService;

/**
 * @author yamingd 保存成果xml到数据库
 */
public class ExtractXmlToDbTask implements IPubXmlTask {

  private String name = "extract_xml_to_db";

  // @Autowired
  // private PublicationService publicationService;
  @Resource(name = "publicationService")
  private PublicationSaveService publicationSaveService;

  // @Resource(name = "pubInsSyncMessageProducer")
  // private PubInsSyncMessageProducer pubInsSyncMessageProducer;

  // @Autowired
  // private PublicationCacheService publicationCacheService;

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

    // 1.按成果ID判断是否是新增publication
    // 1.1 新增publication
    // 1.2修改publication
    // 新增publication后要把PubId写入到pub_meta/pub_id中

    // 2.更新pub_member
    // 2.1 读取旧的pub_member
    // 2.2比较xml的pub_member和旧的pub_member,得到新增的pub_member、删除的pub_member
    // 2.3按pm_id更新pub_member
    // 2.4新增pub_member
    // 2.5删除pub_member

    // 3.遍历xml的pub_member/@ins_id1 ... /@ins_id5,得到全部作者的单位id集合
    // 3.1和数据库比较单位id集合，得到删除的单位id、新增的单位id
    // 3.2把新增的单位id,更新进pub_ins表
    // 3.3从pub_ins表删除已删除的单位id

    // 4.清除数据库的pub_error_fields
    // 4.1遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields

    // 5. 保存xml到pub_xml表

    // 6. 缓存一些字段

    if (xmlDocument.isExistPubId() && context.getPubSimple().getSimpleTask() == 1) {
      publicationSaveService.savePubEdit(xmlDocument, context);

    } else if (context.getPubSimple().getSimpleTask() == 0) {
      publicationSaveService.savePubCreate(xmlDocument, context);

    }
    // TODO 是否需要同步成果到单位ROL库
    // 发送pub-ins同步消息的数据
    // if (context.getArticleType() == PublicationArticleType.OUTPUT) {
    // String key = "scholar:pub-ins:" + context.getCurrentPubId();
    // Object value = this.publicationCacheService.get(key);
    // if (value != null) {
    // String publishYear =
    // xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH,
    // "publish_year");
    // String ctitle =
    // xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH,
    // "zh_title_text");
    // String etitle =
    // xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH,
    // "en_title_text");
    // String pubType =
    // xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH,
    // "id");
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("publishYear", publishYear);
    // params.put("ctitle", ctitle);
    // params.put("etitle", etitle);
    // params.put("pubType", pubType);
    //
    // this.pubInsSyncMessageProducer.addIns(context.getCurrentUserId(),
    // context.getCurrentPubId(),
    // (HashSet<Long>) value, params);
    // this.publicationCacheService.remove(key);
    // }
    //
    // key = "scholar:pub-deleted-ins:" + context.getCurrentPubId();
    // value = this.publicationCacheService.get(key);
    // if (value != null) {
    // this.pubInsSyncMessageProducer.deleteIns(context.getCurrentUserId(),
    // context.getCurrentPubId(),
    // (HashSet<Long>) value);
    // this.publicationCacheService.remove(key);
    // }
    // } else {
    // String key = "scholar:pub-ins:" + context.getCurrentPubId();
    // this.publicationCacheService.remove(key);
    // key = "scholar:pub-deleted-ins:" + context.getCurrentPubId();
    // this.publicationCacheService.remove(key);
    // }
    return false;
  }
}
