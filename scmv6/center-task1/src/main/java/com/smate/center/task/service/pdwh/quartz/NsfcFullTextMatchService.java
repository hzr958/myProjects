package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

public interface NsfcFullTextMatchService {

  List<Long> batchGetPdwhPubIds(Integer size) throws Exception;

  boolean matchSnsPubFulltext(Long pubId) throws Exception;

  void matchPdwhPubFulltext(Long pubId) throws Exception;

  void updateTaskStatus(Long pubId, int i, String string);

}
