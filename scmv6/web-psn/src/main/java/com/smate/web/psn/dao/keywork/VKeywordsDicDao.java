package com.smate.web.psn.dao.keywork;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.keyword.VKeywordsDic;

@Repository
public class VKeywordsDicDao extends SnsHibernateDao<VKeywordsDic, Long> {

  /**
   * 根据nsfcIds 获取关键词
   * 
   * @param nsfcIds
   * @param locale
   * @return
   */
  public List<VKeywordsDic> findKeywordsByNsfcCategoryIds(List<String> nsfcIds, int locale) {
    String hql =
        "select new VKeywordsDic(id,applicationId,year,applicationCode1,applicationCode2,keyword,kwtxt,type,wlen)"
            + "from VKeywordsDic t where (t.applicationCode1 in (:nsfcIds) or t.applicationCode2 in (:nsfcIds)) and t.type =:locale order by t.year desc";
    return super.createQuery(hql).setParameterList("nsfcIds", nsfcIds).setParameter("locale", locale).setMaxResults(30)
        .list();
  }
}
