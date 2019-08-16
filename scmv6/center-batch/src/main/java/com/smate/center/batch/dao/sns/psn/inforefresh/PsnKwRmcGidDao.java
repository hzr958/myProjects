package com.smate.center.batch.dao.sns.psn.inforefresh;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.psn.PsnKwRmcGid;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @author lichangwen
 */
@Repository
public class PsnKwRmcGidDao extends SnsHibernateDao<PsnKwRmcGid, Long> {

  public void delPsnKwRmcGid(Long psnId) {
    String hql = "delete from PsnKwRmcGid g where g.psnId=?";
    super.createQuery(hql, psnId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnKwRmcGids(Long psnId) {
    String hql = "select gid from PsnKwRmcGid where psnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getPsnIds(List<Long> kwGids) {

    if (CollectionUtils.isEmpty(kwGids)) {
      return null;
    }
    String hql =
        "select psnId as psnId,count(id) as count from PsnKwRmcGid where gid in(:gids) and psnId not in(select pp.psnId from PsnPrivate pp) group by psnId";
    return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
        .setParameterList("gids", kwGids).list();
  }
}
