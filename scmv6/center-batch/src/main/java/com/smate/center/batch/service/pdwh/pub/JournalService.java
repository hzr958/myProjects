package com.smate.center.batch.service.pdwh.pub;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.BaseJournalSearch;
import com.smate.center.batch.model.pdwh.pub.Journal;
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
   * @throws ServiceException
   */
  Long getMatchBaseJnlId(Long jid) throws ServiceException;

  /**
   * 通过期刊id取得基准库期刊.
   * 
   * @param jnlId
   * @return
   * @throws ServiceException
   */
  BaseJournalSearch getBaseJournalById(final long jnlId) throws ServiceException;

  /**
   * 获取基础期刊最近一年影响因子.
   * 
   * @param bjId
   * @return
   * @throws ServiceException
   */
  String getBaseJournalImpactors(Long bjId) throws ServiceException;

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
   * @throws ServiceException
   */
  public Journal findJournalByNameIssn(String jname, String issn, Long psnId) throws SysServiceException;

  public Journal addJournal(String jname, String jissn, long currentUserId, String from) throws SysServiceException;

}
