package com.smate.center.open.service.project;

import com.smate.center.open.model.nsfc.project.NsfcProject;



/**
 * @author aijiangbin
 * 
 */
public interface SyncRolService {



  /**
   * @param rolSyncProject
   * @throws ServiceException
   */
  void saveSyncRolProject(NsfcProject snsNsfcProject) throws Exception;

  /**
   * @param nsfcRolPrjId
   * @throws ServiceException
   */
  void deleteRolProject(Long snsPrjId) throws Exception;
}
