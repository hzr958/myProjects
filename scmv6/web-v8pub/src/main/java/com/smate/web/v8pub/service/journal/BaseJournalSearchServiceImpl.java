package com.smate.web.v8pub.service.journal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.dao.journal.BaseJournalSearchDao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.journal.BaseJournalSearch;

/**
 * 基础期刊search实现类
 * 
 * @author YJ
 *
 *         2018年8月9日
 */

@Service("baseJournalSearchService")
@Transactional(rollbackFor = Exception.class)
public class BaseJournalSearchServiceImpl implements BaseJournalSearchService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private BaseJournalSearchDao baseJournalSearchDao;

  @Override
  public BaseJournalSearch getByJid(Long jid) throws ServiceException {
    try {
      return baseJournalSearchDao.get(jid);
    } catch (Exception e) {
      logger.error("获取基础期刊search记录出错！jid={}", jid);
      throw new ServiceException(e);
    }
  }

}
