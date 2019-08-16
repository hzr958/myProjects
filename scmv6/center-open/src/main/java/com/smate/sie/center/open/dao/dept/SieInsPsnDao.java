package com.smate.sie.center.open.dao.dept;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.sie.publication.RolPsnIns;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.sie.center.open.model.dept.InsPsn;

@Repository
public class SieInsPsnDao extends RolHibernateDao<RolPsnIns, Long> {

  /**
   * 获取单位人员数
   * 
   * @param insId
   * @return
   */
  public Long getNums(Long insId) {
    Long personNum = super.findUnique(
        "select count(*) from RolPsnIns p where p.pk.insId = ? and p.isIns = 0 and p.status = 1", insId);
    return personNum;
  }

  /**
   * 根据email获取人员
   * 
   * @param email
   * @return
   */
  public InsPsn getPsnByEmail(String email) {
    String hql = "From RolPsnIns t where t.psnEmail = ? ";
    return (InsPsn) super.createQuery(hql, new Object[] {email}).uniqueResult();
  }
}
