package com.smate.center.oauth.dao.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.oauth.model.pub.Publication;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @author yamingd 个人成果、文献管理查询专用。
 */
@Repository
public class PublicationQueryDao extends SnsHibernateDao<Publication, Long> {

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
   * 得到来自ISI的个人成果.
   * 
   * @param psnId
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Publication> queryPubsByPsnId(Long psnId) {
    String hql =
        "select new Publication(t.citedTimes,t.id,t.zhTitleHash) from Publication t where t.articleType=1 and t.status = 0 and  t.psnId = :psnId order by nvl(t.citedTimes,-9999999) desc,t.id";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }


  /**
   * 统计成果数.
   * 
   * @param psnId
   * @return
   */
  public Long countPubByPsnId(Long psnId, Integer pubType) {
    String hql = "select count(t.id) from Publication t where t.psnId=? and t.articleType=? and t.status=?";
    List<Object> params = new ArrayList<Object>();
    params.add(psnId);
    params.add(1);
    params.add(0);
    if (pubType != null) {
      hql += " and t.typeId=?";
      params.add(pubType);
    }
    return super.countHqlResult(hql, params.toArray());
  }

}
