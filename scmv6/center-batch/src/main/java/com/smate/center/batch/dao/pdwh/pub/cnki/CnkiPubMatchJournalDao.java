package com.smate.center.batch.dao.pdwh.pub.cnki;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMatchJournal;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * CNKI成果拆分期刊匹配表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class CnkiPubMatchJournalDao extends PdwhHibernateDao<CnkiPubMatchJournal, Long> {

  /**
   * 获取成果的期刊列表.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPubMatchJournal> getMatchedJournalList(Long pubId) {
    String hql = "from CnkiPubMatchJournal t where t.pubId=? ";
    Query query = createQuery(hql, pubId);
    return query.list();
  }

  /**
   * 保存成果期刊表记录_MJG_SCM-4104.
   * 
   * @param journal
   */
  public void saveCNKIPubMatchJournal(CnkiPubMatchJournal journal) {
    CnkiPubMatchJournal mjournal = this.getCnkiPubMatchJournal(journal.getPubId(), journal.getIssn());
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
  public CnkiPubMatchJournal getCnkiPubMatchJournal(Long pubId, String issn) {
    String hql = "from CnkiPubMatchJournal t where t.pubId=? and t.issn=? ";
    List<CnkiPubMatchJournal> journalList = super.find(hql, pubId, issn);
    if (CollectionUtils.isNotEmpty(journalList)) {
      return journalList.get(0);
    }
    return null;
  }
}
