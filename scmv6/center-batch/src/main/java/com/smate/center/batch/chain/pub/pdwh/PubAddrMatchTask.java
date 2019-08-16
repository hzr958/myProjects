package com.smate.center.batch.chain.pub.pdwh;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;


/**
 * 单位匹配
 * 
 * @author zll
 *
 */
public class PubAddrMatchTask implements PdwhPubHandleTask {
  private final String name = "pub_addr_match";
  @Autowired
  private PdwhPublicationService pdwhPublicationService;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PdwhPublication pdwhPub, PdwhPubImportContext context) {
    if (context.getCurrentPsnId() != 2L || context.getInsId() == null || context.getInsId() == 0l) {// sns,rol同步至基准库的成果不再指派
      return false;
    }
    // 暂时都不匹配
    return true;
  }

  @Override
  public boolean run(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception {
    // 单位匹配
    pdwhPublicationService.pubAddrMatch(pdwhPub, context);
    return true;
  }

}
