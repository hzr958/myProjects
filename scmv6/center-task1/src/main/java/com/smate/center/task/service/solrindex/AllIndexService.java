package com.smate.center.task.service.solrindex;

import com.smate.center.task.service.solrindex.IndexInfoVO;

public interface AllIndexService {
  IndexInfoVO indexHandleByType(IndexInfoVO indexInfoVO);
}
