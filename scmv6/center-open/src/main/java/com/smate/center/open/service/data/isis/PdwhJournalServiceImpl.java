package com.smate.center.open.service.data.isis;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.data.isis.BaseJournalDao;
import com.smate.center.open.dao.data.isis.JournalCoreDao;


/**
 * 获取sns项目结题数据
 * 
 * @author hp
 * @date 2015-10-23
 */
@Service("pdwhJournalService")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class PdwhJournalServiceImpl implements PdwhJournalService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private BaseJournalDao baseJournalDao;
  @Autowired
  private JournalCoreDao journalCoreDao;

  @Override
  public Map<Long, String> getJournalRomeoColour(Set<Long> jidSet) {
    return baseJournalDao.getRomeoColour(jidSet);
  }

  @Override
  public Map<Long, Long> getHxj(Set<Long> jidSet) {
    return journalCoreDao.getHxj(jidSet);
  }

  @Override
  public List<Long> getIsInJournalCategory(Set<Long> jidSet) {
    return baseJournalDao.getIsInJournalCategory(jidSet);
  }
}
