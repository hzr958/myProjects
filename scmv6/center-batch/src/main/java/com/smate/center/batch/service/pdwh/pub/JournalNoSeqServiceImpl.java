package com.smate.center.batch.service.pdwh.pub;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.BaseJournalSearchDao;
import com.smate.center.batch.dao.pdwh.pub.BaseJournalTitleDao;
import com.smate.center.batch.dao.pdwh.pub.JournalDao;
import com.smate.center.batch.dao.pdwh.pub.JournalNoSeqDao;
import com.smate.center.batch.model.pdwh.pub.BaseJournalTitleTo;
import com.smate.center.batch.model.pdwh.pub.JournalNoSeq;

/**
 * 期刊消息同步 服务实现类
 * 
 * @author tsz
 * 
 */
@Service("journalNoSeqService")
@Transactional(rollbackFor = Exception.class)
public class JournalNoSeqServiceImpl implements JournalNoSeqService {

  @Autowired
  private JournalNoSeqDao journalNoSeqDao;
  @Autowired
  private JournalDao journalDao;
  @Autowired
  private BaseJournalTitleDao baseJournalTitleDao;
  @Autowired
  private BaseJournalSearchDao baseJournalSearchDao;

  @Override
  public void addJournal(JournalNoSeq j) {
    // 保存 匹配基础期刊
    Long baseJnlId = j.getMatchBaseJnlId();
    if (baseJnlId == null) {
      String jname = j.getEnName() == null ? j.getZhName() : j.getEnName();
      List<BaseJournalTitleTo> bjtList = baseJournalTitleDao.snsJnlMatchBaseJnlId(jname, j.getIssn());
      baseJnlId = CollectionUtils.isNotEmpty(bjtList) ? bjtList.get(0).getJnlId() : null;
    }
    if (baseJnlId != null) {
      j.setMatchBaseJnlId(baseJnlId);
    }
    j.setMatchBaseStatus(1);
    journalNoSeqDao.getSession().save(j);
    // 新增期刊同步更新期刊刷新表，供期刊统计用.
    this.journalDao.syncJournalFlag(j.getId());
  }

  @Override
  public Map<Long, String> getRomeoColour(Set<Long> jidSet) {
    return journalNoSeqDao.getRomeoColour(jidSet);
  }

  @Override
  public List<BaseJournalTitleTo> getSnsJnlMatchBaseJnlId(String jname, String issn) {
    return baseJournalTitleDao.snsJnlMatchBaseJnlId(jname, issn);
  }
}
