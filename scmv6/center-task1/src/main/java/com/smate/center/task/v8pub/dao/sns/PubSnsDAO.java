package com.smate.center.task.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubSnsPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人成果基础信息查询DAO
 * 
 * @author houchuanjie
 * @date 2018/06/01 16:51
 */
@Repository
public class PubSnsDAO extends SnsHibernateDao<PubSnsPO, Long> {
  @SuppressWarnings("unchecked")
  public List<PubSnsPO> getSnsPubList(List<Long> snsPubIds) {
    String hql =
        "from PubSnsPO  t where not exists(select 1 from GrpPubs f where t.pubId=f.pubId) and t.pubId in (:snsPubIds) ";
    return super.createQuery(hql).setParameterList("snsPubIds", snsPubIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getScmPubIds() {
    String hql =
        "select t.pubId from PubSnsPO  t where not exists (select 1 from PubPdwhScmRol f where t.pubId=f.pubId)";
    return super.createQuery(hql).list();
  }

  /**
   * 得到个人成果(计算hindex用).
   * 
   * @param psnId
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubSnsPO> queryPubsByPsnId(Long psnId) {
    String hql = "from PubSnsPO t where  t.status = 0 and "
        + "exists(select 1 from PsnPubPO t1 where t1.pubId=t.pubId and t1.status=0 and t1.ownerPsnId=:psnId) "
        + "order by nvl(t.citations,-9999999) desc,t.pubId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  @SuppressWarnings("rawtypes")
  public List<PubSnsPO> findByTitle(String xssCtitle, String xssEtitle, String ctitle, String etitle, Long psnId) {
    xssCtitle = xssCtitle.toLowerCase();
    xssEtitle = xssEtitle.toLowerCase();
    ctitle = ctitle.toLowerCase();
    etitle = etitle.toLowerCase();
    String hql = "from PubSnsPO t where "
        + "(lower(t.title) =:xssCtitle or lower(t.title) =:xssEtitle or lower(t.title) =:etitle or lower(t.title) =:ctitle) "
        + "and exists(select 1 from PubMemberPO t1 where t1.seqNo is null and t1.pubId = t.pubId)"
        + "and t.createPsnId =:psnId " + "and t.recordFrom =1 and t.updateMark = 1 ";
    List list = super.createQuery(hql).setParameter("psnId", psnId).setParameter("xssCtitle", xssCtitle)
        .setParameter("xssEtitle", xssEtitle).setParameter("ctitle", ctitle).setParameter("etitle", etitle).list();
    return list;
  }

  public Long getCreatePsnId(Long pubId) {
    String hql = "select t.createPsnId from PubSnsPO t where t.pubId=:pubId";
    return (Long) this.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  public PubSnsPO getSnsPubById(Long snsPubId) {
    String hql =
        "from PubSnsPO  t where not exists(select 1 from GrpPubs f where t.pubId=f.pubId) and t.pubId = :pubId and t.status=0";
    return (PubSnsPO) this.createQuery(hql).setParameter("pubId", snsPubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PubSnsPO> batchGetPublist(Long lastId) {
    String hql = "from PubSnsPO t where  t.pubId>:lastId order by t.pubId asc";
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(2000).list();

  }

  public PubSnsPO getPubsnsById(Long pubId) {
    String hql = "from PubSnsPO t where  t.status = 0 and t.pubId = :pubId";
    return (PubSnsPO) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }
}
