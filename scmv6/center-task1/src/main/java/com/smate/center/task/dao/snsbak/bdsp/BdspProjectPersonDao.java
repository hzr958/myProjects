package com.smate.center.task.dao.snsbak.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.bdsp.BdspProjectPerson;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspProjectPersonDao extends SnsbakHibernateDao<BdspProjectPerson, Long> {

  @SuppressWarnings("unchecked")
  public List<String> getDspPrjPsn(Long prjCode) {
    String hql = "select t.zhName from BdspProjectPerson t where t.prjCode = :prjCode";
    return super.createQuery(hql).setParameter("prjCode", prjCode).list();
  }

  public List<BdspProjectPerson> findListByPrjCode(Long prjCode) {
    String hql =
        "select distinct new BdspProjectPerson(t.zhName,t.orgName) from BdspProjectPerson t where t.prjCode =:prjCode";
    return super.createQuery(hql).setParameter("prjCode", prjCode).list();
  }

}
