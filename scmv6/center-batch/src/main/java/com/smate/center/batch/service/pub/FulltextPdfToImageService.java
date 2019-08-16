package com.smate.center.batch.service.pub;

import com.smate.center.batch.model.pdwh.pub.PdwhPubFullTextPO;
import com.smate.center.batch.model.sns.pub.PubFullTextPO;


public interface FulltextPdfToImageService {


  void deleteFileByPath(String filePath);

  PdwhPubFullTextPO getPdwhPubFulltext(Long pdwhPubId);

  void ConvertPdwhPubFulltextPdfToimage(PdwhPubFullTextPO pubFulltext);


  PubFullTextPO getSnsPubFulltext(Long snsPubId);

  void ConvertSnsPubFulltextPdfToimage(PubFullTextPO pubFulltext);

  void updatePdwhToImageStatus(Long pdwhPubId, int status, String msg);

  void updateSnsToImageStatus(Long snsPubId, int status, String msg);

}
