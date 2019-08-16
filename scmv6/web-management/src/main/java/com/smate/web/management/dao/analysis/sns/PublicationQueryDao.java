package com.smate.web.management.dao.analysis.sns;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.model.Publication;
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
        "select sum(t.citedTimes) from Publication t where t.articleType=1 and t.status = 0 and  t.ownerPsnId = :psnId";
    List list = super.createQuery(hql).setParameter("psnId", psnId).list();
    if (CollectionUtils.isEmpty(list) || list.get(0) == null) {
      return 0;
    }
    return Integer.parseInt(String.valueOf(list.get(0)));
  }

}
