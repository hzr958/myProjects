package com.smate.center.batch.chain.pub.rol;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.service.rol.pub.PublicationRolService;

/**
 * 
 *
 */
public class ExtractXmlToRolDbTask implements IPubXmlTask {

  private String name = "extract_xml_to_roldb";

  @Autowired
  private PublicationRolService publicationRolService;

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

    // 需要特别注意fundinfo在修改和导入的时候是否抓取到值，如果没有值，是默认更新成果信息没有包含fundinfo,然后直接把pub_fundinfo中相关信息删除

    // 1.按成果ID判断是否是新增publication
    // 1.1 新增publication
    // 1.2修改publication
    // 新增publication后要把PubId写入到pub_meta/pub_id中

    // 2.更新pub_member
    // 2.1 读取旧的pub_member
    // 2.2比较xml的pub_member和旧的pub_member,得到新增的pub_member、删除的pub_member
    // 2.3按pm_id更新pub_member
    // 2.4新增pub_member
    // 2.5删除pub_member，如果该pub_member的member_psn_id不为空，则同时需要删除pub_psn表里的该人的记录

    // 3.遍历xml的pub_member/@member_psn_id得到全部作者的id集合
    // 3.1如果作者id不在pub_psn出现则在pub_psn新增一条该人的记录
    // 3.2如果pm_ide对应的member_psn_id已修改，则需要同时更新pub_psn表的psn_id.

    // 4.清除数据库的pub_error_fields
    // 4.1遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields

    // 5. 保存xml到pub_xml表

    // 6. 缓存一些字段

    if (xmlDocument.isExistPubId()) {
      publicationRolService.updatePublication(xmlDocument, context);
    } else {
      publicationRolService.createPublication(xmlDocument, context);
    }

    return false;
  }

}
