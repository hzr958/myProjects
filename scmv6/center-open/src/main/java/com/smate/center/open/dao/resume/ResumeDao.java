package com.smate.center.open.dao.resume;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.resume.ResumeConfig;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * 设置公开信息DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
@Deprecated
public class ResumeDao extends HibernateDao<ResumeConfig, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  @SuppressWarnings("rawtypes")
  public List findPubAuthorityList(Long pubId) {
    String sql = "select t.tmp_id from resume_config t,resume_pub t2 where t.conf_id=t2.conf_id and t2.pub_id=?";
    return super.queryForList(sql, new Object[] {pubId});
  }

}
