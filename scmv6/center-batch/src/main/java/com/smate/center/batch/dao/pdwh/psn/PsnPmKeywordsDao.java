package com.smate.center.batch.dao.pdwh.psn;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.psn.PsnPmKeywords;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果匹配-用户关键词表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class PsnPmKeywordsDao extends PdwhHibernateDao<PsnPmKeywords, Long> {

  /**
   * 获取用户成果匹配英文名记录.
   * 
   * @param psnId
   * @return
   */
  public List<PsnPmKeywords> getPsnPmKeywordsList(Long psnId) {
    String hql = "from PsnPmKeywords t where t.psnId=? ";
    return super.find(hql, psnId);
  }

  public PsnPmKeywords findByPsnIdAndHash(Long psnId, Long hash) {
    String sql = "from PsnPmKeywords t where t.psnId = ? and t.kwHash = ?";
    return (PsnPmKeywords) super.createQuery(sql, psnId, hash).uniqueResult();
  }

  public void updateNotExists(Long psnId, List<Long> hash, Integer type) {
    StringBuilder hql = new StringBuilder("update PsnPmKeywords t set ");

    if (type == 1) {
      hql.append("t.ztKw=0");
    } else if (type == 2) {
      hql.append("t.pubKw=0");
    } else if (type == 3) {
      hql.append("t.prjKw=0");
    }

    if (hash == null || hash.size() == 0) {
      hql.append(" where t.psnId=:psnId ");
      super.createQuery(hql.toString()).setParameter("psnId", psnId).executeUpdate();
    } else {
      Collection<Collection<Long>> container = ServiceUtil.splitList(hash, 80);
      hql.append(" where t.psnId=:psnId ");

      for (int i = 0; i < container.size(); i++) {
        hql.append(" and t.kwHash not in(:kwHash").append(i).append(")");
      }

      Query query = super.createQuery(hql.toString()).setParameter("psnId", psnId);

      int j = 0;
      for (Collection<Long> c : container) {
        query.setParameterList("kwHash" + j, c);
        j++;
      }
      query.executeUpdate();
    }
  }

  public void updateNotExists(Long psnId, Integer type) {
    this.updateNotExists(psnId, null, type);
  }

  public void deleteNotExists(Long psnId, Integer type) {
    StringBuilder hql =
        new StringBuilder("delete from PsnPmKeywords t where t.psnId=:psnId and t.ztKw=0 and t.pubKw=0 and t.prjKw=0");
    super.createQuery(hql.toString()).setParameter("psnId", psnId).executeUpdate();
  }
}
