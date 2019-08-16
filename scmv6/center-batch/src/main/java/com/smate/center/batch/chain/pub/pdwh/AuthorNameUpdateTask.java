package com.smate.center.batch.chain.pub.pdwh;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;

/**
 * 重复成果xml作者信息更新
 * 
 * @author zjh
 *
 */
public class AuthorNameUpdateTask implements PdwhPubHandleTask {
  private String name = "AuthorNameUpdateTask";
  @Autowired
  private PdwhPublicationService pdwhPublicationService;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PdwhPublication pdwhPub, PdwhPubImportContext context) {
    // ReplaceFlag为1是完全替换，如果replaceFlag为1的话XmlUpate已经进行了完全替换，所以这里不再需要再覆盖
    if (context.getReplaceFlag() == 0 && context.getDupPubId() != null && context.getOperation() == "update") {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean run(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception {
    pdwhPublicationService.updateAuthorInfo(pdwhPub, context);
    return true;
  }

}
