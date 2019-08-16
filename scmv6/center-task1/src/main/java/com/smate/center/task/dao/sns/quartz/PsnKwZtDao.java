package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PsnKwZt;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnKwZtDao extends SnsHibernateDao<PsnKwZt, Long> {
  public List<Object[]> getEnPsnKwZt(Long psnId) {
    String hql =
        "select t.psnId,t.enKwTxt ,count(1)  from PsnKwZt t where t.enKwLen>2 and  t.psnId=:psnId and not exists(select 1 from PsnKwRmcTmp t2 where t2.psnId=t.psnId and t.enKwTxt=t2.keyWordTxt) group by t.psnId,t.enKwTxt ";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  public List<Object[]> getZhPsnKwZt(Long psnId) {
    String hql =
        "select t.psnId ,t.zhKwTxt ,count(1)  from PsnKwNsfcprj t where  t.zhKwLen>1 and  t.psnId=:psnId and not exists(select 1 from PsnKwRmcTmp t2 where t2.psnId=t.psnId and t.zhKwTxt=t2.keyWordTxt) group by t.psnId,t.zhKwTxt ";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }
}
