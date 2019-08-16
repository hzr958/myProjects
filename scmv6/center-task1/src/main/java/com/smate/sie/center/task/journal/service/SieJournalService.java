package com.smate.sie.center.task.journal.service;

import java.util.List;

import com.smate.sie.center.task.journal.model.SieJournal;

/**
 * 期刊查找.
 * 
 * @author sjzhou
 *
 */
public interface SieJournalService {

  /**
   * 获取journal数据.
   * 
   * @param startWith
   * @param size
   * @param uid
   * @return
   * @throws ServiceException
   */
  List<SieJournal> querySieJournalList(String jname) throws Exception;

  SieJournal getSieJournalById(Long jid) throws Exception;

  SieJournal addJournalByPubEnter(String name, String issn, Long currentInsId) throws Exception;

  Long getBaseJournalJnlId(String journalName, String issn) throws Exception;
}
