package com.smate.center.batch.chain.pub.pdwh;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;

/**
 * 分库保存原始xml信息
 * 
 * @author zll
 */
public class PdwhOriginalPubxmlSaveTask implements PdwhPubHandleTask {
  private final String name = "pdwh_original_pub_xml";
  @Autowired
  private PdwhPublicationService pdwhPublicationService;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PdwhPublication pdwhPub, PdwhPubImportContext context) {
    return true;
  }

  @Override
  public boolean run(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception {
    // 分库保存原始xml信息
    pdwhPublicationService.handleInfo(pdwhPub);
    return true;
  }

}
