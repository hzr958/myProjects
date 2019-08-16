package com.smate.web.psn.dao.share;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.share.FundShareBase;

/**
 * 基金分享主表dao
 * 
 * @author Administrator
 *
 */
@Repository
public class FundShareBaseDao extends SnsHibernateDao<FundShareBase, Long> {

  /**
   * 得到主键
   * 
   * @return
   */
  public Long getId() {
    String sql = "select  seq_v_fund_share_base.nextval  from dual ";
    return this.queryForLong(sql);
  }

  // @SuppressWarnings("unchecked")
  // public List<FundShareBase> findListByPsnId(Long psnId, Page<PsnFundInfo>
  // page) {
  // String counthql = "select count(1) ";
  // String hql =
  // "from FundShareBase t where t.status=0 and t.sharerId=:psnId ";
  // page.setTotalCount((Long) this.createQuery(counthql +
  // hql).setParameter("psnId", psnId).uniqueResult());
  // hql += " order by t.updateDate desc , t.id desc";
  // return this.createQuery(hql).setParameter("psnId",
  // psnId).setFirstResult(page.getFirst() - 1)
  // .setMaxResults(page.getPageSize()).list();
  // }

}
