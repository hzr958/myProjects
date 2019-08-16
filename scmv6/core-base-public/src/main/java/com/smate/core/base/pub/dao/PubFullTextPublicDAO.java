package com.smate.core.base.pub.dao;

import com.smate.core.base.pub.model.PubFullTextPublicPO;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 个人库成果全文Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PubFullTextPublicDAO extends SnsHibernateDao<PubFullTextPublicPO, Long> {
  /**
   * 根据pubId和fileId获取成果全文对象
   * 
   * @param pubId 成果id
   * @param fileId 文件id
   * @return
   */
  public PubFullTextPublicPO getPubFullTextByPubId(Long pubId) {
    String hql = "from PubFullTextPublicPO p where p.pubId =:pubId  order by p.gmtCreate desc";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (PubFullTextPublicPO) list.get(0);
    }
    return null;
  }
}
