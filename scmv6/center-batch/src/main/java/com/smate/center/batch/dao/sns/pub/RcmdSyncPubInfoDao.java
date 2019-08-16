package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.RcmdSyncPubInfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 推荐系统同步人员信息标记.
 * 
 * @author lqh
 * 
 */
@Repository
public class RcmdSyncPubInfoDao extends SnsHibernateDao<RcmdSyncPubInfo, Long> {

  /**
   * 加载需要刷新的人员成果列表.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<RcmdSyncPubInfo> loadSyncPub(Long psnId) {

    String hql = "from RcmdSyncPubInfo t where t.status = 0 and t.psnId = ? ";
    return super.createQuery(hql, psnId).list();
  }

  /**
   * 删除成果刷新纪录.
   * 
   * @param pubId
   */
  public void delSyncPub(Long pubId) {
    String hql = "delete from RcmdSyncPubInfo t where pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 是否存在该成果刷新纪录.
   * 
   * @param pubId
   * @return
   */
  public boolean isExistRefPub(Long pubId) {
    String hql = "select count(pubId) from RcmdSyncPubInfo t where pubId = ?";
    Long count = super.findUnique(hql, pubId);
    if (count > 0) {
      return true;
    }
    return false;
  }

}
