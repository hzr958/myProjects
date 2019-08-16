package com.smate.center.task.service.sns.quartz;

public interface RegisterIsisgetOpenIdService {

  public Long getOpenId(String token, Long psnId, int createType) throws Exception;
}
