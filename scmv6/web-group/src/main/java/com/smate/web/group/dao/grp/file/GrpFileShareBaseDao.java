package com.smate.web.group.dao.grp.file;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.grp.file.GrpFileShareBase;

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
