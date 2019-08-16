package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignAuthor;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PubAssignAuthorDao extends RolHibernateDao<PubAssignAuthor, Long> {

  public List<PubAssignAuthor> getPubAuthor(Long pubId) {
    String hql = "from PubAssignAuthor where pubId =:pubId  ";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
