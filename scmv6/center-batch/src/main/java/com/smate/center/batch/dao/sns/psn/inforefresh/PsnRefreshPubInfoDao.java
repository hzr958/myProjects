package com.smate.center.batch.dao.sns.psn.inforefresh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.psn.PsnRefreshPubInfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员成果信息冗余刷新.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnRefreshPubInfoDao extends SnsHibernateDao<PsnRefreshPubInfo, Long> {

  /**
   * 加载需要刷新的人员成果列表.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnRefreshPubInfo> loadRefreshPub(Long psnId) {

    String hql = "from PsnRefreshPubInfo t where t.status = 0 and t.psnId = ? ";
    return super.createQuery(hql, psnId).list();
  }

  /**
   * 删除成果刷新纪录.
   * 
   * @param pubId
   */
  public void delRefreshPub(Long pubId) {
    String hql = "delete from PsnRefreshPubInfo t where pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 是否存在该成果刷新纪录.
   * 
   * @param pubId
   * @return
   */
  public boolean isExistRefPub(Long pubId) {
    String hql = "select count(pubId) from PsnRefreshPubInfo t where pubId = ?";
    Long count = super.findUnique(hql, pubId);
    if (count > 0) {
      return true;
    }
    return false;
  }
}
