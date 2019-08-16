package com.smate.center.task.dao.sns.grp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.grp.GrpPubRcmd;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class GrpPubRcmdDao extends SnsHibernateDao<GrpPubRcmd, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getPubIdByGrpId(Long grpId) {
    String hql =
        "select t.pubId from GrpPubRcmd t where t.grpId = :grpId and t.status!=8 order by t.publishYear desc,t.createDate desc";
    return super.createQuery(hql).setParameter("grpId", grpId).list();
  }

}
