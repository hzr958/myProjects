package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.Patent20161012Fulltext;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class Patent20161012FulltextDao extends SnsHibernateDao<Patent20161012Fulltext, Long> {

  @SuppressWarnings("unchecked")
  public List<Patent20161012Fulltext> getAll() {
    String hql = "from Patent20161012Fulltext t where t.fullTextUrl is null";
    return super.createQuery(hql).list();
  }
}
