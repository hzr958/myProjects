package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PdwhPublicationAll;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果.
 * 
 * @author lichangwen
 * 
 */
@Repository
public class PdwhPublicationAllDao extends PdwhHibernateDao<PdwhPublicationAll, Long> {

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Map getBriefDesc(Long pubId, int dbid) {
    String hql =
        "select new Map(t.id as id, t.pubId as pubId, t.dbid as dbid, t.briefDescZh as briefDescZh, t.briefDescEn as briefDescEn) "
            + " from PdwhPublicationAll t where t.pubId=? and t.dbid=? "
            + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pubId)";
    return (Map<String, Object>) findUnique(hql, pubId, dbid);
  }

  public PdwhPublicationAll getPubAll(Long pubId, int dbid) {
    String hql = "from PdwhPublicationAll t where t.pubId=? and t.dbid=? "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pubId)";
    return findUnique(hql, pubId, dbid);
  }

  public PdwhPublicationAll getPuballById(Long puballId) {
    String hql = "select new PdwhPublicationAll(t.id,t.pubId,t.dbid,t.jnlId) from PdwhPublicationAll t where t.id=? "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pubId)";
    return super.findUnique(hql, puballId);
  }

  public Map getBaseJnlByIssn(Long jnlId) {
    String sql = "select t.pissn from base_journal t where t.jnl_id=? ";
    List list = super.queryForList(sql, new Object[] {jnlId});
    return CollectionUtils.isEmpty(list) ? null : (Map<String, Object>) list.get(0);
  }

  public PdwhPublicationAll getIsiPub(Long pubId) {
    String hql = "from PdwhPublicationAll t where t.pubId=? and t.dbid in (2, 15, 16, 17) "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pubId)";
    return findUnique(hql, pubId);
  }
}
