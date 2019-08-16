package com.smate.web.group.dao.grp.member;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.action.grp.form.GrpMemberForm;
import com.smate.web.group.model.grp.member.GrpMemberRcmd;

/**
 * 群组成员推荐Dao类
 * 
 * @author zzx
 *
 */
@Repository
public class GrpMemberRcmdDao extends SnsHibernateDao<GrpMemberRcmd, Long> {
  /**
   * 获取群组推荐成员列表
   * 
   * @return
   */
  public List<GrpMemberRcmd> getRecommendBySearch(GrpMemberForm form) {
    String hql =
        "select new GrpMemberRcmd(t.id,t.recommendPsnId) from GrpMemberRcmd t where t.isAccept=2 and  t.grpId=:grpId ";
    return this.createQuery(hql).setParameter("grpId", form.getGrpId())
        .setFirstResult((form.getPage().getPageNo() - 1) * form.getPage().getPageSize())
        .setMaxResults(form.getPage().getPageSize()).list();
  }

  /**
   * 记录群组推荐成员邀请记录
   * 
   * @param recommendPsnId
   * @param grpId
   * @param isAccept
   */
  public void setAccept(Long recommendPsnId, Long grpId, Integer isAccept) {
    String hql =
        "update GrpMemberRcmd t set t.isAccept=:isAccept where t.recommendPsnId=:recommendPsnId and t.grpId=:grpId";
    this.createQuery(hql).setParameter("isAccept", isAccept).setParameter("recommendPsnId", recommendPsnId)
        .setParameter("grpId", grpId).executeUpdate();
  }

}
