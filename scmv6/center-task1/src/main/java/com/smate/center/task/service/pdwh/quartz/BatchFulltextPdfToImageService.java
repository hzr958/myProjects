package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

@Deprecated
public interface BatchFulltextPdfToImageService {

  public List<Long> batchGetPdwhPubIds(Integer batchSize) throws Exception;

  public void updateTaskStatus(Long pdwhPubId, int status, String errMsg);

  public Long getBatchOnProcessingCount();

  public boolean deleteOldImage(Long pdwhPubId);

  public void batchConvertPubFulltextPdfToimage(List<Long> pdwhPubIds) throws Exception;

  void batchConvertPubFulltextPdfToimage(Long pdwhPubId) throws Exception;

}
