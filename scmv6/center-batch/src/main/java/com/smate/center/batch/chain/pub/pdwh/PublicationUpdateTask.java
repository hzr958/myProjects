package com.smate.center.batch.chain.pub.pdwh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;

/**
 * 更新PDWH_PUBLICATION, PDWH_PUB_DUP相关内容
 * 
 **/
public class PublicationUpdateTask implements PdwhPubHandleTask {

  private final String name = "PublicationUpdateTask";

  private final Logger logger = LoggerFactory.getLogger(getClass());

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
    this.pdwhPublicationService.updatePdwhPubInfo(pdwhPub, context);
    this.pdwhPublicationService.updatePdwhDupPubInfo(pdwhPub, context);
    return true;
  }

}
