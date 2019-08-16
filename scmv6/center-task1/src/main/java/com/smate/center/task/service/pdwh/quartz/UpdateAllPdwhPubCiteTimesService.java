package com.smate.center.task.service.pdwh.quartz;

import java.util.List;
import java.util.Map;

public interface UpdateAllPdwhPubCiteTimesService {

  public List<Long> getPdwhPubList(Integer batchSize) throws Exception;

  public Map<Integer, Integer> handlePdwhCiteTimes(Long pubId) throws Exception;

  public boolean needUpdate();

  public void updateCitedTimes(Long pdwhPubId, Integer dbId, Integer citetimes) throws Exception;

  void updateTaskStatus(Long psnId, int status, String err);

}
