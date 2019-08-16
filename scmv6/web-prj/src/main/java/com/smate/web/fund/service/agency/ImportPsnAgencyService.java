package com.smate.web.fund.service.agency;

public interface ImportPsnAgencyService {

  /**
   * 同步单位下的人员关注的资助机构
   * 
   * @param insId
   * @param angencyIdsStr
   * @throws Exception
   */
  public void importPsnAgency(Long insId, String angencyIdsStr) throws Exception;
}
