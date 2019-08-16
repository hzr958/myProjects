package com.smate.center.batch.dao.sns.pub;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.KeywordsDic;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 关键词字典，用于拆分成果标题、摘要等使用.
 * 
 * @author lqh
 * 
 */
@Repository
public class KeywordsDicDao extends SnsHibernateDao<KeywordsDic, Long> {

  /**
   * 根据特征hash获取关键词列表.
   * 
   * @param fhashs
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<KeywordsDic> loadKwListByFturesHash(Collection<Long> fhashs) {

    String hql = "from KeywordsDic t where t.fturesHash in (:fhashs)";
    return super.createQuery(hql).setParameterList("fhashs", fhashs).list();
  }
}
