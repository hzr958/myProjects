package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubFullTextPO;
import com.smate.center.task.v8pub.sns.po.PubFullTextPO;

public interface FulltextPdfToImageService {

  List<Long> getPdwhToImageData(Integer size);

  void deleteFileByPath(String filePath);

  PdwhPubFullTextPO getPdwhPubFulltext(Long pdwhPubId);

  void ConvertPdwhPubFulltextPdfToimage(PdwhPubFullTextPO pubFulltext);

  List<Long> getSnsToImageData(Integer size);

  PubFullTextPO getSnsPubFulltext(Long snsPubId);

  void ConvertSnsPubFulltextPdfToimage(PubFullTextPO pubFulltext);

  void updatePdwhToImageStatus(Long pdwhPubId, int status, String msg);

  void updateSnsToImageStatus(Long snsPubId, int status, String msg);

}
