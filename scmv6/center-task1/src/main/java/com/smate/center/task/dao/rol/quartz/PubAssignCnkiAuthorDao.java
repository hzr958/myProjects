package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubAssignCnkiAuthor;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PubAssignCnkiAuthorDao extends RolHibernateDao<PubAssignCnkiAuthor, Long> {

  public List<PubAssignCnkiAuthor> getPubAuthor(Long pubId) {
    String hql = "from PubAssignCnkiAuthor where pubId =:pubId";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
