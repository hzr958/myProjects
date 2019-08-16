package com.smate.web.management.service.journal;

import java.util.List;

import com.smate.web.management.model.journal.BaseJournalTitle;

public interface BaseJournalTitleService {

  /**
   * 基准库通过jname和jissn进行期刊匹配
   * 
   * @param jName
   * @param jIssn
   * @return
   * @throws ServiceException
   */
  public Long searchJournalMatchBaseJnlId(String jName, String jIssn);


  /**
   * 通过jnlId查询所有对应的title
   * 
   * @param jnlId
   * @return
   */
  List<BaseJournalTitle> getBaseJournalTitleByJnlId(Long jnlId);
}
