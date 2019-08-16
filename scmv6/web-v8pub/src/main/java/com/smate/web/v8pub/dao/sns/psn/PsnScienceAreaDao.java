package com.smate.web.v8pub.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.psn.PsnScienceArea;

@Repository
public class PsnScienceAreaDao extends SnsHibernateDao<PsnScienceArea, Long> {

  /**
   * 查找人员有效的科技领域列表
   * 
   * @param psnId
   * @param status
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnScienceArea> findPsnScienceAreaList(Long psnId, Integer status) {
    String hql = "from PsnScienceArea t where t.psnId = :psnId and t.status = :status order by updateDate desc";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status).list();
  }

}
