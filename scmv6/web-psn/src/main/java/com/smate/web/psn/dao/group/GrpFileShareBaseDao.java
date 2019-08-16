package com.smate.web.psn.dao.group;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.grp.GrpFileShareBase;

/**
 * 群组文件dao
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class GrpFileShareBaseDao extends SnsHibernateDao<GrpFileShareBase, Long> {
  /**
   * 得到主键
   * 
   * @return
   */
  public Long getId() {
    String sql = "select  seq_v_grp_file_share_base.nextval  from dual ";
    return this.queryForLong(sql);
  }
}
