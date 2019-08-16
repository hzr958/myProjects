package com.smate.core.base.keywords.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.keywords.model.DiscKeyword;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 关键词实体类dao
 * 
 * @author zk
 *
 */
@Repository
public class DiscKeywordDao extends SnsHibernateDao<DiscKeyword, Long> {

  /**
   * 批量获取zhKey
   */
  @SuppressWarnings("unchecked")
  public List<String> findDiscKwZhkey(Integer pageNo, Integer pageSize) {
    String hql = "select dk.zhKey from DiscKeyword dk order by dk.keyId asc";
    return super.createQuery(hql).setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
  }
}
