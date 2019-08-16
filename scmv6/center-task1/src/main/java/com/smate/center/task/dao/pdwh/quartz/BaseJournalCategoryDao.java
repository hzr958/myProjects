package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.pdwh.pub.BaseJournalCategory;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class BaseJournalCategoryDao extends PdwhHibernateDao<BaseJournalCategory, Long> {

  /**
   * 根据jnlIds批量获取Isi categoryIds
   * 
   * @param ids
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> getIsiCategoryIds(Long id) {
    // String hql = "select distinct categoryId from BaseJournalCategory
    // where journal.jouId =:id and dbId in (15,16,17)";
    String hql =
        "select distinct categoryId from BaseJournalCategory where journal.jouId =:id and categoryId is not null";
    return super.createQuery(hql).setParameter("id", id).list();
  }

  /**
   * 根据jnlIds批量获取cnki categoryIds
   * 
   * @param ids
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> getCnkiCategoryIds(Long id) {
    String hql =
        "select distinct categoryId from BaseJournalCategory where journal.jouId =:id and categoryId is not null";
    return super.createQuery(hql).setParameter("id", id).list();
  }

  /**
   * 根据jnlIds批量获取categoryIds
   * 
   * @param ids @return @throws
   */
  @SuppressWarnings("unchecked")
  public List<Long> getCategoryIdsByJnlId(Long id) {
    String hql = "select distinct id from BaseJournalCategory where journal.jouId =:id order by id desc";
    return super.createQuery(hql).setParameter("id", id).list();
  }

}
