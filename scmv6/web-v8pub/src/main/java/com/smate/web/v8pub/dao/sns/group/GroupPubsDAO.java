package com.smate.web.v8pub.dao.sns.group;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class GroupPubsDAO extends SnsHibernateDao<GroupPubPO, Long> {
  /**
   * 更新所有含有pubId的更新时间字段
   *
   * @param pubId
   */
  public void updateGrpPubsGmtModified(Long pubId) {
    String hql = "update GroupPubPO t set t.updateDate =:updateDate where t.pubId =:pubId ";
    super.createQuery(hql).setParameter("updateDate", new Date()).setParameter("pubId", pubId).executeUpdate();
  }

  /**
   * 更新群组与个人库成果的状态值
   * 
   * @param grpId
   * @param pubId
   * @param status 0：正常 1：删除
   */
  public void updateStatusByGrpIdAndPubId(Long grpId, Long pubId, Integer status) {
    String hql =
        "update GroupPubPO t set t.status =:status,t.updateDate =:updateDate where t.pubId =:pubId and t.grpId=:grpId";
    super.createQuery(hql).setParameter("status", status).setParameter("pubId", pubId).setParameter("grpId", grpId)
        .setParameter("updateDate", new Date()).executeUpdate();
  }

  /**
   * 查找群组成果 不要设置状态
   *
   * @param grpId
   * @param pubId
   * @return
   */
  public GroupPubPO findByGrpIdAndPubId(Long grpId, Long pubId) {
    String hql = " from  GroupPubPO t  where t.pubId =:pubId and t.grpId=:grpId and t.status = 0";
    List list = super.createQuery(hql).setParameter("pubId", pubId).setParameter("grpId", grpId).list();
    if (list != null && list.size() > 0) {
      return (GroupPubPO) list.get(0);
    }
    return null;
  }
  /**
   * 查找群组成果 不要设置状态
   * 查找群组成果 不要设置状态
   * 查找群组成果 不要设置状态
   * 查找群组成果 不要设置状态
   * 查找群组成果 不要设置状态
   * 查找群组成果 不要设置状态
   *
   * @param grpId
   * @param pubId
   * @return
   */
  public GroupPubPO findGrpPub(Long grpId, Long pubId) {
    String hql = " from  GroupPubPO t  where t.pubId =:pubId and t.grpId=:grpId ";
    List list = super.createQuery(hql).setParameter("pubId", pubId).setParameter("grpId", grpId).list();
    if (list != null && list.size() > 0) {
      return (GroupPubPO) list.get(0);
    }
    return null;
  }

  /**
   * 查找群组成果 设置状态为0
   *
   * @param grpId
   * @param pubId
   * @return
   */
  public GroupPubPO findGrpPubByGrpIdAndPubId(Long grpId, Long pubId) {
    String hql = " from  GroupPubPO t  where t.pubId =:pubId and t.grpId=:grpId and t.status = 0";
    List list = super.createQuery(hql).setParameter("pubId", pubId).setParameter("grpId", grpId).list();
    if (list != null && list.size() > 0) {
      return (GroupPubPO) list.get(0);
    }
    return null;
  }

  /**
   * 群组成果的删除状态
   *
   * @param pubId
   * @param grpId
   * @return
   */
  public Integer findGrpPubsStatus(Long pubId, Long grpId) {
    String hql = "select g.status  from GrpPubs  g where g.pubId=:pubId  and g.grpId=:grpId ";
    Object obj = this.createQuery(hql).setParameter("pubId", pubId).setParameter("grpId", grpId).uniqueResult();
    if (obj != null) {
      return Integer.parseInt(obj.toString());
    }
    return 1;
  }

  public GroupPubPO findByPubId(Long pubId) {
    String hql = " from  GroupPubPO t  where t.pubId =:pubId order by t.createDate desc";
    List list = super.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (GroupPubPO) list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Integer> getStatus(Long pubId) {
    String hql = "select t.status from GroupPubPO t where t.pubId =:pubId and t.status = 0";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }
}
