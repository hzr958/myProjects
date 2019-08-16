package com.smate.center.task.service.sns.quartz;

import java.util.List;

public interface GenerateGroupCodeService {

  public List<Long> getNeedTohandleList(int batchSize) throws Exception;

  public void startGenerateGrpCode(Long info) throws Exception;

  public void updateTaskStatus(Long psnId, int i, String string);

}
