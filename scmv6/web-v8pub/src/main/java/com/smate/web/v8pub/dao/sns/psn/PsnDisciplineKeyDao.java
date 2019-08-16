package com.smate.web.v8pub.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnDisciplineKeyDao extends SnsHibernateDao<PsnDisciplineKey, Long> {

  /**
   * 查找人员关键词List
   * 
   * @param psnId
   * @param status
   * @return
   */
  public List<PsnDisciplineKey> findPsnDisciplineKeyByPsnId(Long psnId, Integer status) {
    String hql =
        "select new PsnDisciplineKey(id, keyWords, psnId) from PsnDisciplineKey t where t.psnId=:psnId and t.status=:status order by updateDate asc";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status).list();
  }

}
