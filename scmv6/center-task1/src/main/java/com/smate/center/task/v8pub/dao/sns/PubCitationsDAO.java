package com.smate.center.task.v8pub.dao.sns;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.smate.center.task.v8pub.sns.po.PubCitationsPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubCitationsDAO extends SnsHibernateDao<PubCitationsPO, Long> {

  public boolean isExistsPubCitations(Long pubId) {
    String hql = "from PubCitationsPO t where t.pubId =:pubId";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (!list.isEmpty() && list.size() > 0) {
      return true;
    }
    return false;
  }

  /**
   * 是否更新引用次数记录 条件： 当前引用次数大于数据库内的记录时才允许添加一条记录
   * 
   * @return
   */
  public PubCitationsPO getByPubId(Long pubId) {
    String hql = "FROM PubCitationsPO t where t.pubId=:pubId";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (PubCitationsPO) list.get(0);
    }
    return null;
  }

  public void deleteByPubId(Long pubId) {
    String hql = "delete from PubCitationsPO t where t.pubId=:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

}
