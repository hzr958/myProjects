package com.smate.center.open.service.reschproject;

import com.smate.center.open.model.nsfc.NsfcSyncProject;
import com.smate.center.open.model.nsfc.project.NsfcProject;
import com.smate.center.open.model.nsfc.project.NsfcReschProject;

public interface SnsSyncRolService {

  NsfcReschProject saveNsfcSyncReschProject(NsfcSyncProject nsfcSyncProject) throws Exception;

  /**
   * 同步结题、进展报告的项目
   * 
   * @param nsfcSyncProject
   * @return
   * @throws ServiceException
   */
  NsfcProject syncNsfcProject(NsfcSyncProject nsfcSyncProject) throws Exception;

}
