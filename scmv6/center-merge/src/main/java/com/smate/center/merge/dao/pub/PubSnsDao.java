package com.smate.center.merge.dao.pub;

import com.smate.center.merge.model.sns.pub.PubSns;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 个人库成果 dao.
 * 
 * @author tsz
 *
 * @date 2018年9月11日
 */
@Repository
public class PubSnsDao extends SnsHibernateDao<PubSns, Long> {
  /**
   * 获取成果 通过创建人ID.
   * 
   * @param createPsnId not null
   * @return list
   */
  @SuppressWarnings("unchecked")
  public List<PubSns> getPubsByCreatePsnId(Long createPsnId) throws Exception {
    String hql = " from PubSns t where  t.createPsnId=:createPsnId and t.status=0";
    return super.createQuery(hql).setParameter("createPsnId", createPsnId).list();
  }

  /**
   * 得到个人成果(计算hindex用).
   * 
   * @param psnId
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubSns> queryPubsByPsnId(Long psnId) {
    String hql = "from PubSns t where  t.status =0 and "
        + "exists(select 1 from PsnPub t1 where t1.pubId=t.pubId and t1.status=0 and t1.ownerPsnId=:ownerPsnId) "
        + "order by nvl(t.citations,-9999999) desc,t.pubId";
    return super.createQuery(hql).setParameter("ownerPsnId", psnId).list();
  }
}
