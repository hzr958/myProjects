package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PdwhPublication;
import com.smate.center.task.model.pdwh.quartz.PdwhPublicationAll;
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
        "select new Map(t.id as id, t.pubId as pubId, t.dbid as dbid, t.briefDescZh as briefDescZh, t.briefDescEn as briefDescEn) from PdwhPublicationAll t where t.pubId=? and t.dbid=?";
    return (Map<String, Object>) findUnique(hql, pubId, dbid);
  }

  public PdwhPublicationAll getPubAll(Long pubId, int dbid) {
    String hql = "from PdwhPublicationAll t where t.pubId=? and t.dbid=?";
    return findUnique(hql, pubId, dbid);
  }

  public PdwhPublicationAll getPuballById(Long puballId) {
    String hql = "select new PdwhPublicationAll(t.id,t.pubId,t.dbid,t.jnlId) from PdwhPublicationAll t where t.id=?";
    return super.findUnique(hql, puballId);
  }

  public Map getBaseJnlByIssn(Long jnlId) {
    String sql = "select t.pissn from base_journal t where t.jnl_id=?";
    List list = super.queryForList(sql, new Object[] {jnlId});
    return CollectionUtils.isEmpty(list) ? null : (Map<String, Object>) list.get(0);
  }

  public PdwhPublicationAll getIsiPub(Long pubId) {
    String hql = "from PdwhPublicationAll t where t.pubId=? and t.dbid in (2, 15, 16, 17)";
    return findUnique(hql, pubId);
  }

  public Long getCount(String code) {
    String hql = "select count(t.pubId) from PdwhPublication t ";
    if (!StringUtils.equalsIgnoreCase("other", code)) {
      hql += "where t.enTitle like ? ";
      return (Long) super.createQuery(hql, code + "%").list().get(0);
    } else {
      hql += "where t.enTitle is not null and LENGTH(substr(t.enTitle,0,1)) <> LENGTHB(substr(t.enTitle,0,1))";
      return (Long) super.createQuery(hql).list().get(0);
    }
  }

  /**
   * 获取成果列表
   * 
   * @param string
   * @return
   */
  public List<PdwhPublication> getPubByIndexCode(String code, int startIndex, int maxCount) {
    String hql = "from PdwhPublication t ";
    String orderBy = " order by substr(t.enTitle,2,3),t.id";
    // Other类别的成果（非以英文字母开头的标题的成果）
    if (StringUtils.equalsIgnoreCase("other", code)) {
      hql += "where t.enTitle is not null and LENGTH(substr(t.enTitle,0,1)) <> LENGTHB(substr(t.enTitle,0,1))";
      Query q = super.createQuery(hql + orderBy);
      q.setFirstResult(startIndex);
      q.setMaxResults(maxCount);
      return q.list();
    } else {
      hql += "where t.enTitle like ? ";
      Query q = super.createQuery(hql + orderBy, code + "%");
      q.setFirstResult(startIndex);
      q.setMaxResults(maxCount);
      return q.list();
    }
  }

  /**
   * 
   *
   * 根据pubid/dbid查询成果
   */
  public PdwhPublicationAll getMaxPubTitlteHashByPubId(Long pubId, Integer dbId) {
    String hql =
        "select new PdwhPublicationAll(id, pubId, zhTitle, enTitle, zhTitleHash, enTitleHash) from PdwhPublicationAll where pubId=:pubId and dbid=:dbid order by pubId desc";
    @SuppressWarnings("unchecked")
    List<PdwhPublicationAll> list =
        super.createQuery(hql).setParameter("pubId", pubId).setParameter("dbid", dbId).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }
}
