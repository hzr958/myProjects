package com.smate.center.open.service.fund;

public interface FundService {

  void deleteSolrFundInfo(Long fundId) throws Exception;

  void updateSolrFundInfo(Long fundId) throws Exception;

}
