package com.smate.center.batch.chain.pub.pdwh;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;

/**
 * 解析成果地址任务
 * 
 * @author zjh
 *
 */
public class ExtractPubAddressTask implements PdwhPubHandleTask {
  private String name = "ExtractPubAddressTask";
  @Autowired
  private PdwhPublicationService pdwhPublicationService;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PdwhPublication pdwhPub, PdwhPubImportContext context) {
    // 新增或者更新都需要解析成果地址
    return true;

  }

  @Override
  public boolean run(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception {
    pdwhPublicationService.savePubAddress(pdwhPub, context);
    return true;
  }

}
