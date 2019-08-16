package com.smate.web.psn.dao.recommend;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.recommend.PsnCoRmcRefresh;

/**
 * 人员合作者推荐DAO
 *
 * @author wsn
 * @createTime 2017年6月21日 下午4:23:52
 *
 */
@Repository
public class PsnCoRmcRefreshDao extends SnsHibernateDao<PsnCoRmcRefresh, Long> {
  /**
   * 标记人员合作者需要刷新.
   * 
   * @param psnId
   */
  public void setPsnCoRmcRefresh(Long psnId) {
    String hql = "from PsnCoRmcRefresh t where t.psnId = :psnId ";
    PsnCoRmcRefresh ref = (PsnCoRmcRefresh) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    if (ref == null) {
      ref = new PsnCoRmcRefresh(psnId);
    }
    ref.setStatus(0);
    ref.setMkAt(new Date());
    super.getSession().save(ref);
  }
}
