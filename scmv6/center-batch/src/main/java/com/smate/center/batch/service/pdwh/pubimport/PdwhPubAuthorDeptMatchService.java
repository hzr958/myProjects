package com.smate.center.batch.service.pdwh.pubimport;

import java.util.Map;

import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;


public interface PdwhPubAuthorDeptMatchService {

  void saveMemberInsData(Map<String, Object> memberMap, PubPdwhDetailDOM pdwhPub);

  void updateInsCount(Long pubId);

}
