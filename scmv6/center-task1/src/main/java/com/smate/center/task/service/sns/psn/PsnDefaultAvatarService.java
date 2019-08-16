package com.smate.center.task.service.sns.psn;

import java.util.List;

public interface PsnDefaultAvatarService {

  void startGenerateAvatars(Long psnId) throws Exception;

  void updateTaskStatus(Long psnId, int i, String string);

  List<Long> getNeedTohandleList(int batchSize) throws Exception;

}
