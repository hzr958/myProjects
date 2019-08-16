package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.BaseJournal;
import com.smate.center.batch.model.pdwh.pub.BaseJournalToSnsRefresh;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 基础期刊冗余到sns dao
 * 
 * @author tsz
 * 
 */
@Repository
public class BaseJournalToSnsRefreshDao extends PdwhHibernateDao<BaseJournalToSnsRefresh, Long> {

  /**
   * 根据base期刊ID 获取刷新记录
   */

  public BaseJournalToSnsRefresh findJ(Long jouId) {
    String hql = "from BaseJournalToSnsRefresh p  where  p.bjid=?";
    List<BaseJournalToSnsRefresh> list = super.createQuery(hql, jouId).list();
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
  public List<BaseJournalToSnsRefresh> findNeedRefresh(Integer max) {

    String hql = "from BaseJournalToSnsRefresh p where   p.status=1";
    return super.createQuery(hql).setMaxResults(max).list();
  }

  /**
   * 获取需要刷新的对象 // 除了status =1 外 还需要加上不在刷新表 但在基础表的数据 tsz
   */
  @SuppressWarnings("unchecked")
  public List<BaseJournal> getNeedRefresh(Integer maxsize) {
    // String hql =
    // "from BaseJournal p where not exists(select 1 from BaseJournalToSnsRefresh t where t.bjid=p.jouId
    // ) or exists(select 1 from BaseJournalToSnsRefresh t1 where t1.bjid=p.jouId and t1.status=1) ";
    String hql = "from BaseJournal p where not exists(select 1 from BaseJournalToSnsRefresh t where t.bjid=p.jouId ) ";
    return super.createQuery(hql).setMaxResults(maxsize).list();
  }

}
