package com.smate.web.dyn.dao.file;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.file.PsnFileShareBase;

/**
 * 个人文件分享主表dao
 * 
 * @author Administrator
 *
 */
@Repository
public class PsnFileShareBaseDao extends SnsHibernateDao<PsnFileShareBase, Long> {

  /**
   * 得到主键
   * 
   * @return
   */
  public Long getId() {
    String sql = "select  seq_v_psn_file_share_base.nextval  from dual ";
    return this.queryForLong(sql);
  }



}
