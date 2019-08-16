package com.smate.web.management.dao.journal;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.management.model.journal.BaseConstCategory;

@Repository
public class BaseConstCategoryDao extends PdwhHibernateDao<BaseConstCategory, Long> {

  /**
   * 根根据分类名称和dbid查询对应的所有分类id
   * 
   * @param catName
   * @param dbId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> searchByCategoryName(String catName, Long dbId) {
    String hql =
        "select categoryId from BaseConstCategory where (trim(categoryEn)= :catName or trim(categoryXx)= :catName) and dbId = :dbId ";
    return super.createQuery(hql).setParameter("catName", catName).setParameter("dbId", dbId).list();
  }

  public int getBaseJnlCat(Long jnlId, Long catId, Long dbId) {
    String sql = "select count(t.id) from base_journal_category t where t.jnl_id=? and t.category_id=? and t.dbid=?";
    return super.queryForInt(sql, new Object[] {jnlId, catId, dbId});
  }

  public Long getBaseJournalCategoryId() {
    String hql = "select SEQ_BASE_JOURNAL_CATEGORY.NEXTVAL from dual";
    return super.queryForLong(hql);
  }
}
