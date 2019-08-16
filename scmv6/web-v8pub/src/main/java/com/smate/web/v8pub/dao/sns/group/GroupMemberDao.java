package com.smate.web.v8pub.dao.sns.group;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.group.GrpMemberPO;

/**
 * 群组成员DAO
 * 
 * @author wsn
 * @date 2018年8月20日
 */
@Repository
public class GroupMemberDao extends SnsHibernateDao<GrpMemberPO, Long> {

  /**
   * 是否群组成员
   * 
   * @param groupId
   * @param psnId
   * @return
   */
  public boolean isExistGrpMember(Long groupId, Long psnId) {
    String sql = "select t.status from GrpMemberPO t where t.psnId=:psnId and t.grpId=:grpId";
    String status =
        (String) super.createQuery(sql).setParameter("psnId", psnId).setParameter("grpId", groupId).uniqueResult();
    if (status == null) {
      return false;
    } else if (status.equals("01")) {
      return true;
    } else {
      return false;
    }
  }
}
