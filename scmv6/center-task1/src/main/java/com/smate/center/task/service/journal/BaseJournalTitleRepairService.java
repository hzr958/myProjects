package com.smate.center.task.service.journal;

import java.util.List;
import java.util.Map;

public interface BaseJournalTitleRepairService {

  List<Map<String, Object>> getNeedRepairJnlIds();

  void repairData(Long jnlId);
}
