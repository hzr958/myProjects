package com.smate.center.open.dao.rcmdsync;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.smate.center.open.model.rcmdsync.RcmdSyncPsnInfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 推荐系统同步人员信息标记.
 * 
 * @author lqh
 * 
 */
@Repository
public class RcmdSyncPsnInfoDao extends SnsHibernateDao<RcmdSyncPsnInfo, Long> {

  /**
   * 加载需要同步的人员列表.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<RcmdSyncPsnInfo> loadSyncPsn() {

    String hql = "from RcmdSyncPsnInfo t where t.status = 0 ";
    return super.createQuery(hql).setMaxResults(10).list();
  }

  /**
   * 删除人员刷新纪录.
   * 
   * @param psnId
   */
  public void delSyncPsn(Long psnId) {

    String hql = "delete from RcmdSyncPsnInfo t where t.psnId = ? ";
    super.createQuery(hql, psnId).executeUpdate();
  }

  /**
   * 标记刷新人员信息失败.
   * 
   * @param psnId
   */
  public void mkSyncPsnError(Long psnId) {

    String hql = "update RcmdSyncPsnInfo t set t.status = 9 where t.psnId = ? ";
    super.createQuery(hql, psnId).executeUpdate();
  }

}
