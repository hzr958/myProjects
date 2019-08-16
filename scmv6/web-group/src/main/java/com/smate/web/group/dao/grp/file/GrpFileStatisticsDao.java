package com.smate.web.group.dao.grp.file;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.grp.file.GrpFileStatistics;

/**
 * @description 群组文件统计dao
 * @author xiexing
 * @date 2019年5月15日
 */
@Repository
public class GrpFileStatisticsDao extends SnsHibernateDao<GrpFileStatistics, Long> {
  /**
   * 查询群组文件分享统计数
   * 
   * @return
   */
  public List<Map<String, Object>> queryShareStatistics() {
    String HQL = "select new Map(t.grpFileId as grpFileId,nvl(t.shareCount,0) as shareCount) from GrpFileStatistics t";
    return getSession().createQuery(HQL).list();
  }
}
