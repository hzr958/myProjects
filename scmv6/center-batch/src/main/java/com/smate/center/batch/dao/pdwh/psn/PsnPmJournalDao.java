package com.smate.center.batch.dao.pdwh.psn;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.psn.PsnPmJournal;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果匹配-用户期刊表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class PsnPmJournalDao extends PdwhHibernateDao<PsnPmJournal, Long> {

  /**
   * 获取用户成果匹配英文名记录.
   * 
   * @param psnId
   * @return
   */
  public List<PsnPmJournal> getPsnPmJournalList(Long psnId) {
    String hql = "from PsnPmJournal t where t.psnId=? ";
    return super.find(hql, psnId);
  }

  public PsnPmJournal findPsnPmJournal(Long psnId, String issn) {
    String hql = "from PsnPmJournal t where t.psnId=? and t.issn=?";
    return super.findUnique(hql, psnId, issn);
  }

  public void deleteNotExists(Long psnId, List<Long> hashList) {
    Collection<Collection<Long>> container = ServiceUtil.splitList(hashList, 80);

    StringBuilder hql = new StringBuilder("delete from PsnPmJournal t where t.psnId=:psnId ");

    for (int i = 0; i < container.size(); i++) {
      hql.append(" and t.issnHash not in(:hashList").append(i).append(")");
    }

    Query query = super.createQuery(hql.toString()).setParameter("psnId", psnId);

    int j = 0;
    for (Collection<Long> c : container) {
      query.setParameterList("hashList" + j, c);
      j++;
    }
    query.executeUpdate();
  }

  public void deleteAllByPsnId(Long psnId) {
    String hql = "delete from PsnPmJournal t where t.psnId = ?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }
}
