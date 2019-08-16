package com.smate.web.v8pub.dao.sns;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubCitationsPO;

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
  public Boolean allowUpdate(PubCitationsPO pc) {
    String hql = "SELECT max(t.citations) FROM PubCitationsPO t where t.pubId=:pubId";
    Integer count = (Integer) this.createQuery(hql).setParameter("pubId", pc.getPubId()).uniqueResult();
    if (count != null && pc.getCitations() != null && count < pc.getCitations()) {
      return true;
    }
    return false;
  }

  /**
   * 获取成果引用数更新时间
   * 
   * @param pubId
   * @return
   */
  public Date findPubCitationModifyDate(Long pubId) {
    String hql = "SELECT max(t.gmtModified) FROM PubCitationsPO t where t.pubId=:pubId";
    return (Date) this.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  /**
   * 查找成果的引用次数
   * 
   * @param pubId
   * @return
   */
  public Integer findPubCitations(Long pubId) {
    String hql = "SELECT max(t.citations) FROM PubCitationsPO t where t.pubId=:pubId";
    return (Integer) this.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  /**
   * 根据id获取成果引用次数记录
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("rawtypes")
  public PubCitationsPO findByPubId(Long pubId) {
    String hql = "FROM PubCitationsPO t where t.pubId=:pubId";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (PubCitationsPO) list.get(0);
    }
    return null;
  }

}
