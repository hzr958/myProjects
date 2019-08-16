package com.smate.center.task.service.tmp;

import java.util.List;

public interface FundAgencyAddressService {

  public List<Long> getFundAgency(Integer size);

  public void startProcessing() throws Exception;

}
