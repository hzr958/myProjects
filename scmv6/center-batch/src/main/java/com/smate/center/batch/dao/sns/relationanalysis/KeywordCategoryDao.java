package com.smate.center.batch.dao.sns.relationanalysis;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.relationanalysis.KeywordCategory;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class KeywordCategoryDao extends SnsHibernateDao<KeywordCategory, Integer> {

  public KeywordCategory getDataById(Integer id) {

    String hql = "from KeywordCategory t where t.kcId = ?";

    return super.findUnique(hql, id);
  }

}
