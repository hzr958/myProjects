package com.smate.web.psn.dao.profile;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.profile.PsnDiscScm;

/**
 * 人员分类信息DAO
 * 
 * @author WSN
 *
 *         2017年8月15日 下午2:06:50
 *
 */

@Repository
public class PsnDiscScmDao extends SnsHibernateDao<PsnDiscScm, Long> {

  /**
   * 找到人员分类
   * 
   * @param psnId
   * @return
   */
  public List<PsnDiscScm> findPsnDisc(Long psnId) {
    String hql = "select new PsnDiscScm(psnId, scmDiscNo) from PsnDiscScm t where t.psnId = :psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).setMaxResults(5).list();
  }
}
