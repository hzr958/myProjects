package com.smate.center.batch.service.pdwh.pub;

public interface RainPatPublicationService {

  Long getDupPub(Long titleHashValue, Long patentNoHash, Long patentOpenNoHash);

  void saveRainPatPubDup(Long pubId, Long titleHashValue, Long patentNoHash, Long patentOpenNoHash);

  void saveRainPatPubExtend(Long pubId, String xmlString);


}
