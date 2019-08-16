package com.smate.web.psn.dao.profile;



import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;

import com.smate.web.psn.model.profile.RecommandKwDropHistory;

/**
 * @author zyx
 * 
 */
@Repository
public class RecommandKwDropHistoryDao extends SnsHibernateDao<RecommandKwDropHistory, Long> {
  public RecommandKwDropHistory findByIdAndKw(Long psnId, String kwTxt) {
    String hql = "from RecommandKwDropHistory t where t.psnId=? and t.kwTxt=?";
    return super.findUnique(hql, psnId, kwTxt);
  }


}
