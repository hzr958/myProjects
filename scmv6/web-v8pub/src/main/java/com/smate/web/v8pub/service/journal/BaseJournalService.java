package com.smate.web.v8pub.service.journal;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.journal.BaseJournal2;
import com.smate.web.v8pub.po.journal.BaseJournalPO;

public interface BaseJournalService {

  /**
   * 通过jid获取基础期刊对象
   * 
   * @param jnlId
   * @return
   * @throws ServiceException
   */
  BaseJournalPO getById(Long jnlId) throws ServiceException;

  /**
   * 根据期刊名和期刊issn匹配基础期刊的jid
   * 
   * @param jname
   * @param issn
   * @return
   * @throws ServiceException
   */
  Long searchJnlMatchBaseJnlId(String jname, String issn) throws ServiceException;

  /**
   * 
   * @param baseJnlId
   * @return
   */
  BaseJournal2 getBaseJournal2Title(Long baseJnlId) throws ServiceException;

  /**
   * 查找基准库的期刊 影响因子
   * 
   * @param jnlId
   * @return
   */
  String findPdwhPubImpactFactors(Long jnlId,Integer publishYear);

}
