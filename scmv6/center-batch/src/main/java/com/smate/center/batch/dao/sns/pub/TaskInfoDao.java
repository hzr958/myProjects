package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.psn.FriendGroupOperateTaskInfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 任务信息Dao
 * 
 * @author zzx
 *
 */
@Repository
public class TaskInfoDao extends SnsHibernateDao<FriendGroupOperateTaskInfo, Long> {
  /**
   * 检测任务是否存在
   * 
   * @param taskInfo
   * @return
   * @throws DaoException
   */
  public boolean isDataExist(FriendGroupOperateTaskInfo taskInfo) throws DaoException {
    String sql =
        "SELECT count(t.id) FROM friend_group_operate_task t WHERE t.psn_id=? AND t.biz_id=? AND t.operation=?";
    int count =
        super.queryForInt(sql, new Object[] {taskInfo.getPsnId(), taskInfo.getBizId(), taskInfo.getOperation()});
    if (count > 0) {
      return true;
    }
    return false;
  }

}
