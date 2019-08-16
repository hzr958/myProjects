package com.smate.core.base.pub.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.model.PubSnsPublicPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人成果基础信息查询DAO
 * 
 * @author houchuanjie
 * @date 2018/06/01 16:51
 */
@Repository
public class PubSnsPublicDAO extends SnsHibernateDao<PubSnsPublicPO, Long> {
  /**
   * 获取人员Hindex值
   * 
   * @param psnId
   * @return
   */
  public Map<String, Object> findPsnHindex(Long psnId) {
    String hql =
        "select count(1) as Hindex from (select t.citations from v_pub_sns t where t.status = 0 and exists ( select 1 from V_PSN_PUB b where t.pub_Id = b.pub_Id and b.OWNER_PSN_ID=:psnId and b.status=0 ) order by nvl(t.citations, 0) desc) where citations >= rownum";
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
        "select nvl(t.citations, 0) from PubSnsPublicPO t where t.status = 0 and exists ( select 1 from PsnPubPO b where t.pubId = b.pubId and b.ownerPsnId=:psnId and b.status=0 )  order by nvl(t.citations, 0) desc";
    return super.createQuery(hql).setParameter("psnId", psnId).setMaxResults(maxResult).list();
  }
}
