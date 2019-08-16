package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.ResumeConfig;
import com.smate.core.base.utils.data.SnsHibernateDao;



/**
 * 设置公开信息DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class ResumeDao extends SnsHibernateDao<ResumeConfig, Long> {

  @SuppressWarnings("rawtypes")
  public List findPubAuthorityList(Long pubId) {
    String sql = "select t.tmp_id from resume_config t,resume_pub t2 where t.conf_id=t2.conf_id and t2.pub_id=?";
    return super.queryForList(sql, new Object[] {pubId});
  }

  @SuppressWarnings("rawtypes")
  public List findWorkAuthorityList(Long workId) {
    String sql = "select t.tmp_id from resume_config t,resume_work t2 where t.conf_id=t2.conf_id and t2.work_id=?";
    return super.queryForList(sql, new Object[] {workId});
  }
}
