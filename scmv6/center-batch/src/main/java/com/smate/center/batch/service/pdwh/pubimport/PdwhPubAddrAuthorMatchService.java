package com.smate.center.batch.service.pdwh.pubimport;

import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

public interface PdwhPubAddrAuthorMatchService {

  public void saveOrUpdateData(PubPdwhDetailDOM pdwhPub, String operate) throws Exception;

}
