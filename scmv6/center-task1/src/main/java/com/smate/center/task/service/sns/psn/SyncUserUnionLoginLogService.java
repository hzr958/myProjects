package com.smate.center.task.service.sns.psn;

import java.util.List;

public interface SyncUserUnionLoginLogService {

  List<Long> getLoginPsnIds(Long lastPsnId);

  List<Long> getUnionPsnId();

  void saveUserUnionLoginLog(List<Long> psnIdList, boolean isLogin);



}
