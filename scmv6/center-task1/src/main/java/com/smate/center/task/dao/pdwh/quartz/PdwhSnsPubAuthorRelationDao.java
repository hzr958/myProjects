package com.smate.center.task.dao.pdwh.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PdwhSnsPubAuthorRelation;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhSnsPubAuthorRelationDao extends PdwhHibernateDao<PdwhSnsPubAuthorRelation, Long> {
  /**
   * 存在则不插入数据
   * 
   * @param entity
   */
  public void saveUnique(PdwhSnsPubAuthorRelation entity) {
    String hql =
        "from PdwhSnsPubAuthorRelation t where t.pubId=:pubId and t.psnId=:psnId and t.relationemail=:relationemail";

    PdwhSnsPubAuthorRelation Result = (PdwhSnsPubAuthorRelation) super.createQuery(hql)
        .setParameter("pubId", entity.getPubId()).setParameter("psnId", entity.getPsnId())
        .setParameter("relationemail", entity.getRelationemail()).uniqueResult();
    if (Result == null) {
      this.save(entity);
    }

  }

  /**
   * 存在则不插入数据
   * 
   * @param entity
   */
  public void saveUniqueNameOrg(PdwhSnsPubAuthorRelation entity) {
    String hql = "from PdwhSnsPubAuthorRelation t where t.pubId=:pubId and t.psnId=:psnId";

    PdwhSnsPubAuthorRelation Result = (PdwhSnsPubAuthorRelation) super.createQuery(hql)
        .setParameter("pubId", entity.getPubId()).setParameter("psnId", entity.getPsnId()).uniqueResult();
    if (Result == null) {
      this.save(entity);
    }

  }
}
