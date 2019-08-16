package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignEmail;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PubAssignEmailDao extends RolHibernateDao<PubAssignEmail, Long> {

  public List<String> getNotExistsCoEmail(Long psnId, Long pubId) {
    StringBuilder hql = new StringBuilder();
    hql.append("select email from PubAssignEmail t where ");
    hql.append(" not exists(select t1.id from PsnPmEmail t1 where t1.psnId = ? and t.email = t1.email ) ");
    hql.append(" and not exists(select t2.id from PsnPmCoemail t2 where t2.psnId = ? and t.email = t2.email) ");
    hql.append(" and t.pubId = ? ");

    return super.createQuery(hql.toString(), psnId, psnId, pubId).list();
  }

}
