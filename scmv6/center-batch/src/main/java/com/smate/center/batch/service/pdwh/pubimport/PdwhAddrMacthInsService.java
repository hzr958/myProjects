package com.smate.center.batch.service.pdwh.pubimport;

import java.util.Map;
import java.util.Set;

public interface PdwhAddrMacthInsService {

  void startMatchInsName(Set<String> orgstrs, Long pubId, String context) throws Exception;

  Map<String, Set<String>> getExtractInsName(String replaceChars) throws Exception;


}
