package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PsnKwNsfcprj;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnKwNsfcprjDao extends SnsHibernateDao<PsnKwNsfcprj, Long> {

  public Long getPrjkeywordCount(String keywordTxt, Long psnId) {
    String hql = "select count(1) from PsnKwNsfcprj t where t.keywordTxt=:keywordTxt and psnId=:psnId ";
    return (Long) super.createQuery(hql).setParameter("keywordTxt", keywordTxt).setParameter("psnId", psnId)
        .uniqueResult();
  }

  public List<Object[]> getPsnKwNsfcprj(Long psnId) {
    String hql =
        "select t.psnId,t.zhKwTxt,count(1) from PsnKwNsfcprj t where t.prjYear>=2007 and t.zhKwLen>2 and  t.psnId=:psnId and not exists(select 1 from PsnKwRmcTmp t2 where t2.psnId=t.psnId and t.zhKwTxt=t2.keyWordTxt) group by t.psnId,t.zhKwTxt ";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  public List<Object[]> getEnPsnKwNsfcprj(Long psnId) {
    String hql =
        "select t.psnId,t.enKwTxt,count(1) from PsnKwNsfcprj t where t.prjYear>=2007 and t.enKwLen>1 and  t.psnId=:psnId and not exists(select 1 from PsnKwRmcTmp t2 where t2.psnId=t.psnId and t.enKwTxt=t2.keyWordTxt) group by t.psnId,t.enKwTxt ";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
