package com.smate.web.psn.dao.psnlog;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.psnlog.PsnUpdateWorkLog;

/**
 * 更新工作经历后,保存日志，发邮件通知好友Dao
 * 
 * @author zk
 *
 */
@Repository
public class PsnUpdateWorkLogDao extends SnsHibernateDao<PsnUpdateWorkLog, Long> {

  /**
   * 查找是否有相同记录
   */
  public PsnUpdateWorkLog getPsnUpdateWorkLog(Long psnId, Long workId) {
    return super.findUnique("from PsnUpdateWorkLog where psnId=? and status = 0", psnId);
  }

  public void savePsnUpdateWorkLog(PsnUpdateWorkLog log) {
    super.save(log);
  }

  /**
   * 获取批量未处理数据
   */
  @SuppressWarnings("unchecked")
  public List<PsnUpdateWorkLog> getPsnUpdateWorkLog(Integer size) throws DaoException {
    return super.createQuery("from PsnUpdateWorkLog where status = 0 order by id").setMaxResults(100)
        .setFirstResult(size * 100).list();
  }
}
