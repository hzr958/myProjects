package com.smate.center.task.service.sns.psn;

import java.util.List;

public interface GeneratePersonNullNameService {

  void updateTaskStatus(Long psnId, int i, String string);

  void startGeneratePsnNames(Long psnId);

  List<Long> getNeedTohandleList(int batchSize) throws Exception;

}
