package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.RcmdSyncGroupInfo;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 推荐系统同步人与群组信息Dao
 * 
 * @author zk
 *
 */
@Repository
public class RcmdSyncGroupInfoDao extends SnsHibernateDao<RcmdSyncGroupInfo, Long> {

  /**
   * 获取需要同步的记录
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<RcmdSyncGroupInfo> loadSync(Long psnId) throws DaoException {
    String hql = "from RcmdSyncGroupInfo i where i.psnId = ? and i.status =0 ";
    return super.createQuery(hql, psnId).list();
  }

  /**
   * 删除需要同步的记录
   * 
   * @param psnId
   * @param groupId
   * @throws DaoException
   */
  public void delSync(Long groupId) throws DaoException {
    String hql = "delete from RcmdSyncGroupInfo i where i.groupId = ? ";
    super.createQuery(hql, groupId).executeUpdate();
  }
}
