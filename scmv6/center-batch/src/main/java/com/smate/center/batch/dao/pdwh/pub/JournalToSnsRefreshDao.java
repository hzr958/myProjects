package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.Journal;
import com.smate.center.batch.model.pdwh.pub.JournalToSnsRefresh;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 期刊冗余到sns dao
 * 
 * @author tsz
 * 
 */
@Repository
public class JournalToSnsRefreshDao extends PdwhHibernateDao<JournalToSnsRefresh, Long> {

  /**
   * 根据期刊ID 获取刷新记录
   */

  public JournalToSnsRefresh findJ(Long jId) {
    String hql = "from JournalToSnsRefresh p where  p.jid=?";
    List<JournalToSnsRefresh> list = super.createQuery(hql, jId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;

  }

  /**
   * 获取需要刷新的对象
   * 
   */
  @SuppressWarnings("unchecked")
  public List<JournalToSnsRefresh> getNeedRefresh(Integer max) {

    String hql = "from JournalToSnsRefresh p where  p.jidStatus=1";
    return super.createQuery(hql).setMaxResults(max).list();
  }

  /**
   * 获取需要刷新的对象 然后记录不存在与刷新记录表 并且需要同步了基础期刊的数据。
   * 
   */
  @SuppressWarnings("unchecked")
  public List<Journal> findNeedRefresh(Integer maxsize) {

    // String hql =
    // "from Journal p where p.matchBaseJnlId is not null and (not exists (select 1 from "
    // +
    // "JournalToSnsRefresh t where t.jid=p.id) or exists (select 1 from JournalToSnsRefresh t1 where
    // t1.jid=p.id and t1.jidStatus=1 ))";
    String hql =
        "from Journal p where p.matchBaseJnlId is not null and (not exists (select 1 from JournalToSnsRefresh t where t.jid=p.id))";
    return super.createQuery(hql).setMaxResults(maxsize).list();
  }

}
