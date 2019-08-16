package com.smate.center.task.service.sns.quartz;

import java.util.List;

import com.smate.center.task.model.sns.pub.Des3PubId;

public interface GenerateDes3PubIdService {

  public List<Des3PubId> getPubIdList(int index, int batchSize);


  public void GenerateDesPubId(Des3PubId pubId) throws Exception;


}
