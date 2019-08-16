package com.smate.center.open.dao.psn;

import com.smate.center.open.model.psn.PersonPmName;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PERSON人员姓名变换记录表
 * 
 * @author LIJUN
 * @date 2018年3月20日
 */
@Repository
public class PersonPmNameDao extends SnsHibernateDao<PersonPmName, Long> {


  @SuppressWarnings("unchecked")
  public List<PersonPmName> getPsnByPsnId(Long psnId) {
    String hql = "from PersonPmName where psnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();

  }

}
