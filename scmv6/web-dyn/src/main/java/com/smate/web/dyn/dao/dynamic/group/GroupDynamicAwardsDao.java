package com.smate.web.dyn.dao.dynamic.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.dynamic.group.GroupDynamicAwards;

/**
 * 群组动态赞 记录 dao
 * 
 * @author tsz
 *
 */
@Repository
public class GroupDynamicAwardsDao extends SnsHibernateDao<GroupDynamicAwards, Long> {

  /**
   * 获取群组动态赞记录
   * 
   * @param dynId
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public GroupDynamicAwards getGroupDynamicAwardsBydynIdAndPsnId(Long dynId, Long psnId) {
    String hql = " from GroupDynamicAwards   g where g.dynId=:dynId  and  g.awardPsnId=:awardPsnId";
    List<GroupDynamicAwards> list =
        this.createQuery(hql).setParameter("dynId", dynId).setParameter("awardPsnId", psnId).list();
    if (list != null && list.size() > 0)
      return list.get(0);
    return null;
  }

  /**
   * 获取群组动态赞状态
   * 
   * @param dynId
   * @param psnId
   * @return
   */
  public LikeStatusEnum getLikeStatusEnum(Long dynId, Long psnId) {
    String hql = "select g.status from GroupDynamicAwards   g where g.dynId=:dynId  and  g.awardPsnId=:awardPsnId";
    LikeStatusEnum status = (LikeStatusEnum) this.createQuery(hql).setParameter("dynId", dynId)
        .setParameter("awardPsnId", psnId).uniqueResult();
    return status;

  }

  /**
   * 获取群组动态赞状态
   * 
   * @param dynId
   * @param psnId
   * @return
   */
  public Integer getGroupDynamicAwardsStatusBydynIdAndPsnId(Long dynId, Long psnId) {
    String hql = "select g.status from GroupDynamicAwards   g where g.dynId=:dynId  and  g.awardPsnId=:awardPsnId";
    Object result = this.createQuery(hql).setParameter("dynId", dynId).setParameter("awardPsnId", psnId).uniqueResult();
    if (result != null) {
      return (Integer) result;
    } else {
      return null;
    }

  }

}
