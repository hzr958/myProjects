package com.smate.center.batch.dao.pdwh.pub.isi;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.isi.IsiPubMatchJournal;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * ISI成果匹配表的期刊表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class IsiPubMatchJournalDao extends PdwhHibernateDao<IsiPubMatchJournal, Long> {

  /**
   * 根据成果ID获取期刊的ISSN和ISSN的hash值列表.
   * 
   * @param pubId
   * @return
   */
  public List<IsiPubMatchJournal> getIsiPubMatchJournalByPubId(Long pubId) {
    String hql = "select new IsiPubMatchJournal(issn,issnHash) from IsiPubMatchJournal t where t.pubId=? ";
    return super.find(hql, pubId);
  }

  /**
   * 保存成果期刊表记录_MJG_SCM-4104.
   * 
   * @param journal
   */
  public void saveIsiPubMatchJournal(IsiPubMatchJournal journal) {
    IsiPubMatchJournal mjournal = this.getIsiPubMatchJournal(journal.getPubId(), journal.getIssn());
    if (mjournal != null) {
      mjournal.setIssnHash(journal.getIssnHash());
      mjournal.setjName(journal.getjName());
      super.getSession().update(mjournal);
    } else {
      super.save(journal);
    }
  }

  /**
   * 获取成果期刊记录.
   * 
   * @param pubId
   * @param issn
   * @return
   */
  public IsiPubMatchJournal getIsiPubMatchJournal(Long pubId, String issn) {
    String hql = "from IsiPubMatchJournal t where t.pubId=? and t.issn=? ";
    List<IsiPubMatchJournal> journalList = super.find(hql, pubId, issn);
    if (CollectionUtils.isNotEmpty(journalList)) {
      return journalList.get(0);
    }
    return null;
  }
}
