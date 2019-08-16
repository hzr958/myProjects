package com.smate.center.batch.chain.pub.pdwh;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;

/**
 * 更新成果xml
 * 
 * @author zjh
 *
 */
public class XmlUpdateTask implements PdwhPubHandleTask {
  private String name = "XmlUpdateTask";
  @Autowired
  private PdwhPublicationService pdwhPublicationService;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PdwhPublication pdwhPub, PdwhPubImportContext context) {
    // 没有重复的pub_id说明是应该新增，直接跳出
    if (context.getDupPubId() != null && context.getOperation() == "update") {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean run(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception {
    pdwhPublicationService.updateXml(pdwhPub, context);
    return true;
  }

}
