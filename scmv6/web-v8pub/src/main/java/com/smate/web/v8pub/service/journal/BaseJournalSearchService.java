package com.smate.web.v8pub.service.journal;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.journal.BaseJournalSearch;

/**
 * 
 * @author YJ
 *
 *         2018年8月9日
 */
public interface BaseJournalSearchService {


  BaseJournalSearch getByJid(Long jid) throws ServiceException;
}
