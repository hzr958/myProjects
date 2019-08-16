package com.smate.web.management.service.institution;

import com.smate.web.management.model.institution.bpo.InstitutionBpo;

/**
 * 单位信息修改备份Service
 * 
 * @author WeiLong Peng
 *
 */
public interface InstitutionBakService {

  /**
   * 备份单位修改前的信息
   * 
   * @param ins
   * @param optPsnId
   * @throws ServiceException
   */
  public void backUpInstitution(InstitutionBpo ins, Long optPsnId) throws Exception;

}
