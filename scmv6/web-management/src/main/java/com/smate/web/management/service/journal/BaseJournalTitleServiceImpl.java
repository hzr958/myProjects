package com.smate.web.management.service.journal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.management.dao.journal.BaseJournalTitleDao;
import com.smate.web.management.dao.journal.ConstRefDbDao;
import com.smate.web.management.model.journal.BaseJournalTitle;

@Service(value = "baseJournalTitleService")
@Transactional(rollbackFor = Exception.class)
public class BaseJournalTitleServiceImpl implements BaseJournalTitleService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private BaseJournalTitleDao baseJournalTitleDao;
  @Autowired
  private ConstRefDbDao constRefDbDao;

  @Override
  public Long searchJournalMatchBaseJnlId(String jName, String jIssn) {
    return null;
  }

  @Override
  public List<BaseJournalTitle> getBaseJournalTitleByJnlId(Long jnlId) {
    List<BaseJournalTitle> titleList = baseJournalTitleDao.findByJnlId(jnlId);
    for (BaseJournalTitle baseJournalTitle : titleList) {
      if (baseJournalTitle.getDbId() != null)
        baseJournalTitle.setDbCode(constRefDbDao.getCodeByDbId(baseJournalTitle.getDbId()));
    }
    return titleList;
  }

}
