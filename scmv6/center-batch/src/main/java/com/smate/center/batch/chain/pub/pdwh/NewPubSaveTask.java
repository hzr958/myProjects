package com.smate.center.batch.chain.pub.pdwh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;

public class NewPubSaveTask implements PdwhPubHandleTask {
  private final String name = "NewPubSaveTask";

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPublicationService pdwhPublicationService;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PdwhPublication pdwhPub, PdwhPubImportContext context) {
    if (context.getOperation() == "saveNew") { // 只有添加新成果才运行
      return true;
    }
    return false;
  }

  @Override
  public boolean run(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception {
    pdwhPub.setCreateDate(context.getNow());
    pdwhPub.setUpdateDate(context.getNow());
    // 设置后台导入更新：2L
    pdwhPub.setCreatePsnId(context.getCurrentPsnId());
    pdwhPub.setUpdatePsnId(context.getCurrentPsnId());
    this.pdwhPublicationService.savePdwhPublication(pdwhPub);
    return true;
  }

}
