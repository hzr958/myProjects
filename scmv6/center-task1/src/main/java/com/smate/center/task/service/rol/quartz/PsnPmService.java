package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.PsnPmCnkiConame;
import com.smate.center.task.model.rol.quartz.PsnPmConference;
import com.smate.center.task.model.rol.quartz.PsnPmJournal;
import com.smate.center.task.model.rol.quartz.PsnPmKeyWord;

public interface PsnPmService {
  /**
   * 保存用户确认成果ISI名称.
   * 
   * @param initName
   * @param fullName
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void saveAddtPsnPmIsiName(String initName, String fullName, Long psnId) throws ServiceException;

  public void savePsnPmIsiConame(String initName, String fullName, Long psnId) throws ServiceException;

  public void savePsnPmSpsConame(String initName, Long psnId) throws ServiceException;

  public void savePsnPmSpsCoPreName(String prefixName, Long psnId) throws ServiceException;

  public void saveAddtPsnPmCnkiName(String name, Long psnId) throws ServiceException;

  public PsnPmCnkiConame savePsnPmCnkiConame(String name, Long psnId) throws ServiceException;

  public PsnPmJournal savePsnPmJournal(Long jid, String jname, String issn, Long psnId) throws ServiceException;

  public PsnPmConference savePsnPmConference(String name, Long psnId) throws ServiceException;

  public PsnPmKeyWord savePsnPmKeyWord(String keywords, Long psnId) throws ServiceException;

}
