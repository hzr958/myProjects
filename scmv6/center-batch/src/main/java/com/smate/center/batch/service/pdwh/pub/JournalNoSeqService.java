package com.smate.center.batch.service.pdwh.pub;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smate.center.batch.model.pdwh.pub.BaseJournalTitleTo;
import com.smate.center.batch.model.pdwh.pub.JournalNoSeq;

/**
 * 期刊同步
 * 
 * @author tsz
 * 
 */
public interface JournalNoSeqService {

  /**
   * 添加期刊 带主建id
   * 
   * @param j
   */
  public void addJournal(JournalNoSeq j);

  public Map<Long, String> getRomeoColour(Set<Long> jidSet);

  public List<BaseJournalTitleTo> getSnsJnlMatchBaseJnlId(String jname, String issn);
}
