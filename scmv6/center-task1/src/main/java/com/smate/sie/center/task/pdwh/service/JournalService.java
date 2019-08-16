package com.smate.sie.center.task.pdwh.service;

import com.smate.center.task.model.pdwh.quartz.Journal;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.exception.SysServiceException;

/**
 * 期刊查找.
 * 
 * @author yamingd
 */
public interface JournalService {

  /**
   * @param jid
   * @return
   * @throws SysServiceException
   */
  Long getMatchBaseJnlId(Long jid) throws SysServiceException;

  /**
   * @param jid 期刊ID
   * @return Journal
   * @throws BatchTaskException
   * 
   */
  Journal getById(final long jid) throws BatchTaskException;

  /**
   * 根据名字 查找单个期刊
   * 
   * @param jname
   * @param issn
   * @param psnId
   * @return
   * @throws SysServiceException
   */
  public Journal findJournalByNameIssn(String jname, String issn, Long psnId) throws SysServiceException;

  /**
   * 根据jnl_id查找期刊
   * 
   * @param jname
   * @param issn
   * @param psnId
   * @return
   * @throws SysServiceException
   */
  public Long findJournalByJnlId(Long jnlId) throws SysServiceException;

  public Journal addJournal(String jname, String jissn, long currentUserId, String from) throws SysServiceException;

}
