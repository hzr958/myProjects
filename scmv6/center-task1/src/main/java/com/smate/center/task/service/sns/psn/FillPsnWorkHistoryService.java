package com.smate.center.task.service.sns.psn;

import java.util.List;

import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.model.security.Person;

public interface FillPsnWorkHistoryService {

  List<Person> getHandlePsnList(Integer size);

  void saveWorkHistory(WorkHistory psnWork);

}
