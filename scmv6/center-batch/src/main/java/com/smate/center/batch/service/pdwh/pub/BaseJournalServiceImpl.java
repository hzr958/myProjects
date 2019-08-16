package com.smate.center.batch.service.pdwh.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.BaseJournalDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.BaseJournal;

@Service("baseJournalService")
@Transactional(rollbackFor = Exception.class)
public class BaseJournalServiceImpl implements BaseJournalService {
  private static final long serialVersionUID = -3131795568765610598L;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private Long jnlId = null;
  @Autowired
  private BaseJournalDao baseJournalDao;

  // 成功导入记录数
  public final String SUCCESS = "success";
  // 导入失败记录数
  public final String FAILED = "failed";

  @Override
  public String getRomeoColourByJid(Long jid) throws ServiceException {
    return baseJournalDao.getRomeoColourByJid(jid);
  }

  /**
   * 通过issns获取期刊名
   * 
   * @author zk
   */
  @Override
  public List<BaseJournal> findBaseJournalsByIssns(List<String> issns) throws ServiceException {
    return baseJournalDao.getBaseJournalsByIssns(issns);
  }
}
