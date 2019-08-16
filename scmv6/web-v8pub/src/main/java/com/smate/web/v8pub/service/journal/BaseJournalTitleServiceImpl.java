package com.smate.web.v8pub.service.journal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.dao.journal.BaseJournalTitleDAO;
import com.smate.web.v8pub.exception.ServiceException;

@Service(value = "baseJournalTitleService")
@Transactional(rollbackFor = Exception.class)
public class BaseJournalTitleServiceImpl implements BaseJournalTitleService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private BaseJournalTitleDAO baseJournalTitleDAO;

  @Override
  public Long searchJournalMatchBaseJnlId(String jName, String jIssn) throws ServiceException {
    try {
      return baseJournalTitleDAO.searchJournalMatchBaseJnlId(jName, jIssn);
    } catch (Exception e) {
      logger.error("基准库匹配基础期刊出错！", e);
      throw new ServiceException(e);
    }
  }

}
