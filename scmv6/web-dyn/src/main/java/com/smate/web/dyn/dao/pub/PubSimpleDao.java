package com.smate.web.dyn.dao.pub;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.pub.PubSimple;

/**
 * V_PUB_SIMPLE表实体Dao
 * 
 * @author ajb
 * 
 * 
 */
@Repository
public class PubSimpleDao extends SnsHibernateDao<PubSimple, Long> {
  /**
   * 成果是否已被删除
   * 
   * @param pubId
   * @return
   */
  public boolean isDel(Long pubId) {
    String hql = "select t.pubId from PubSimple t where t.status=1 and t.pubId=:pubId ";
    Long pubSimpleId = (Long) this.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
    if (pubSimpleId != null && pubSimpleId != 0L) {
      return true;
    } else {
      return false;
    }
  }

  /*
   * 得到成果的简介 和 标题
   */
  public PubSimple getPubSimpleBrefAndTitle(Long pubId) {
    String hql =
        "select new PubSimple(p.pubId,p.zhTitle , p.enTitle , p.authorNames , p.briefDesc,p.briefDescEn,p.publishYear ,p.fullTextField,p.ownerPsnId) from PubSimple p where p.pubId =:pubId";
    Object obj = super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
    if (obj != null) {
      return (PubSimple) obj;
    }

    return null;
  }

  /*
   * 得到成果的psnId
   */
  public Long getPsnIdByPubId(Long pubId) {
    String hql = "select  p.ownerPsnId   from  PubSimple p where p.pubId =:pubId  ";
    Object obj = super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
    if (obj != null) {
      return (Long) obj;
    }
    return null;
  }

}
