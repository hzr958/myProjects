package com.smate.center.batch.chain.pub.pdwh;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.service.pdwh.pub.PdwhPubCitedTimesService;

public class PubCitedRelationTask implements PdwhPubHandleTask {
  private String name = "PubCitedRelationTask";
  @Autowired
  private PdwhPubCitedTimesService pdwhPubCitedTimesService;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PdwhPublication pdwhPub, PdwhPubImportContext context) {
    if (context.getPdwhPubInfoMap().get("pdwhPubId") == null) {
      return false;
    } else {
      return true;
    }

  }

  @Override
  public boolean run(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception {
    pdwhPubCitedTimesService.updatePdwhPubCitedRelation(pdwhPub, context);
    return true;
  }

}
