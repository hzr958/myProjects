package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.DiscKeywordHot;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class DiscKeywordHotDao extends SnsHibernateDao<DiscKeywordHot, Long> {
  public Long queryHotKwCount(Long kid, Long psnId) {
    StringBuffer bf = new StringBuffer();
    bf.append("select count(1) from DiscKeywordHot h where h.id=:kid and not exists");
    bf.append("(select 1 from RecommandKwDropHistory th where th.psnId=:psnId and h.kwTxt=th.kwTxt)");
    bf.append("and not exists(select 1 from PsnDisciplineKey tk where h.kwTxt=lower(tk.keyWords) and tk.psnId=:psnId)");
    return (Long) super.createQuery(bf.toString()).setParameter("kid", kid).setParameter("psnId", psnId)
        .setParameter("psnId", psnId).uniqueResult();
  }
}
