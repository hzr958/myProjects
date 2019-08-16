package com.smate.center.task.dao.rcmd.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.model.rcmd.quartz.RcmdPsnJournal;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * 人员期刊.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class RcmdPsnJournalDao extends RcmdHibernateDao<RcmdPsnJournal, Long> {
  /**
   * 获取人员所有期刊.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<RcmdPsnJournal> getPsnAllJournal(Long psnId) {
    return super.createQuery("from RcmdPsnJournal t where t.psnId = ? ", psnId).list();
  }

  /**
   * 获取用户发表期刊最多档，必须是大于一次的
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("rawtypes")
  public int getPsnJnlMaxGrade(Long psnId) {
    String hql =
        "select t.grade from RcmdPsnJournal t where t.psnId=? group by t.grade order by sum(t.tf) desc,t.grade desc";
    List list = super.createQuery(hql, psnId).setMaxResults(1).list();
    return CollectionUtils.isEmpty(list) ? 0 : (Integer) list.get(0);
  }

  @SuppressWarnings("rawtypes")
  public int getPsnJnlCountByIssn(Long psnId, String issn) {
    String hql = "select tf  from RcmdPsnJournal where psnId=? and issnTxt=?";
    List list = super.createQuery(hql, psnId, issn.toLowerCase()).list();
    return CollectionUtils.isEmpty(list) ? 0 : (Integer) list.get(0);
  }

}
