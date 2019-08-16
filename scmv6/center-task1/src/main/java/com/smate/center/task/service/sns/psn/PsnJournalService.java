package com.smate.center.task.service.sns.psn;

import java.util.List;
import java.util.Map;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.RcmdPsnJournal;


public interface PsnJournalService {

  /**
   * 获取用户发表期刊最高档次
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  int getPsnJnlMaxGrade(Long psnId) throws ServiceException;

  /**
   * 获取个人发表期刊等级
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<RcmdPsnJournal> getPsnJournal(Long psnId) throws ServiceException;

  /**
   * 用户发表期刊的次数
   * 
   * @param issn
   * @return
   * @throws ServiceException
   */
  int getPsnJnlCountByIssn(Long psnId, String issn) throws ServiceException;


}
