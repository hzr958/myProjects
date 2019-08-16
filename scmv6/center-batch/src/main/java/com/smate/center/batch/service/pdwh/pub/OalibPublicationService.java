package com.smate.center.batch.service.pdwh.pub;

public interface OalibPublicationService {

  Long getDupPub(Long titleHashValue, Long unionHashValue, Long sourceIdHash);

  void saveOalibPubDup(Long pubId, Long titleHashValue, Long unionHashValue, Long sourceIdHash);

  void saveOalibPubExtend(Long pubId, String xmlString);

}
