package com.smate.center.open.service.nsfc;



import com.smate.center.open.model.nsfc.NsfcwsPerson;


/**
 * 基金委webservice集成smate成果接口获取GoogleScholar库相关人员信息服务.
 * 
 * @author ai jiang bin
 * 
 */
public interface NsfcwsPsnService {


  /**
   * 通过人员ID获取人员信息.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  NsfcwsPerson getNsfcwsPersonByPsnId(Long psnId) throws Exception;

}
