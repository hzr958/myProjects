package com.smate.web.psn.dao.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果DAO
 *
 * @author wsn
 * @createTime 2017年6月21日 下午5:35:06
 *
 */
@Repository
public class PublicationDao extends SnsHibernateDao<Publication, Long> {

  /**
   * 统计成果数.
   * 
   * @param psnId
   * @return
   */
  public Long countPubByPsnId(Long psnId, Integer pubType) {
    String hql = "select count(t.id) from Publication t where t.ownerPsnId=? and t.articleType=? and t.status=?";
    List<Object> params = new ArrayList<Object>();
    params.add(psnId);
    params.add(1);
    params.add(0);
    if (pubType != null) {
      hql += " and t.pubType=?";
      params.add(pubType);
    }
    return super.countHqlResult(hql, params.toArray());
  }

  /**
   * 得到来自ISI的个人成果.
   * 
   * @param psnId
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Publication> queryPubsByPsnId(Long psnId) {
    String hql =
        "select new Publication(t.citedTimes,t.id,t.zhTitleHash) from Publication t where t.articleType=1 and t.status = 0 and  t.ownerPsnId = :psnId order by nvl(t.citedTimes,-9999999) desc,t.id";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 得到来自ISI的个人成果.
   * 
   * @param psnId
   * 
   * @return
   */
  @SuppressWarnings("rawtypes")
  public Integer queryPubsCiteTimesByPsnId(Long psnId) {
    String hql =
        "select sum(t.citedTimes) from Publication t where t.articleType=1 and t.status = 0 and  t.psnId = :psnId";
    List list = super.createQuery(hql).setParameter("psnId", psnId).list();
    if (CollectionUtils.isEmpty(list) || list.get(0) == null)
      return 0;
    return Integer.parseInt(String.valueOf(list.get(0)));
  }

  /**
   * 检查成果配置是否有丢失的
   * 
   * @param psnId
   * @param cnfId
   * @return
   */
  public boolean hasPsnConfigPubLost(Long psnId, Long cnfId) {
    String hql =
        "select count(1) from Publication t where t.articleType = 1 and t.status=0 and t.ownerPsnId = :psnId and not exists(select 1 from PsnConfigPub p where p.id.cnfId = :cnfId and p.id.pubId = t.pubId)";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("cnfId", cnfId).uniqueResult();
    if (count != null && count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 获取人员Hindex值
   * 
   * @param psnId
   * @return
   */
  public Map<String, Object> findPsnHindex(Long psnId) {
    String hql =
        "select count(1) as Hindex from (select t.cited_times from publication t where t.article_type = 1 and t.status = 0 and t.owner_psn_id = :psnId order by nvl(t.cited_times, 0) desc) where cited_times >= rownum";
    return (Map<String, Object>) super.getSession().createSQLQuery(hql).setParameter("psnId", psnId)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
  }

  /**
   * 获取成果引用数信息
   * 
   * @param psnId
   * @param maxResult
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Integer> findPubCitedInfo(Long psnId, int maxResult) {
    String hql =
        "select nvl(t.citedTimes, 0) from Publication t where t.articleType = 1 and t.status = 0 and t.ownerPsnId = :psnId order by nvl(t.citedTimes, 0) desc";
    return super.createQuery(hql).setParameter("psnId", psnId).setMaxResults(maxResult).list();
  }
}
