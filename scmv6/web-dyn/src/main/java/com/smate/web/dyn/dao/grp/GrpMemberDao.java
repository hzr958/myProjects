package com.smate.web.dyn.dao.grp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.grp.GrpMember;

/**
 * 群组成员关系Dao类
 * 
 * @author zzx
 *
 */
@Repository
public class GrpMemberDao extends SnsHibernateDao<GrpMember, Long> {
  /**
   * 获取群组成员统计数
   * 
   * @return
   */
  public Long getGrpMemberCount(Long grpId) {
    String hql = "select count(t.id) from GrpMember t where t.status=01 and t.grpId=:grpId ";
    return (Long) this.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
  }

  /**
   * 获取群组置顶状态
   * 
   * @param grpIds
   * @param psnId
   * @return
   */
  public Integer getTopGrp(Long grpIds, Long psnId) {
    // 是否置顶， 1=已经置顶 ，0=没有置顶
    String hql = " select t.topDate from GrpMember t where t.grpId =:grpIds and t.psnId=:psnId";
    Date topDate =
        (Date) this.createQuery(hql).setParameter("grpIds", grpIds).setParameter("psnId", psnId).uniqueResult();
    if (topDate != null) {
      return 1;
    }
    return 0;
  }

  /**
   * 获取各个群组角色统计数
   * 
   * @param psnIds
   */
  public List<Map<String, Object>> getRoleCount(List<Long> grpIds, Long psnId) {
    String hql =
        " select new Map(t.grpRole as grpRole,nvl(count(t.id),0) as count) from GrpMember t where t.grpId in(:grpIds) and t.psnId=:psnId group by t.grpRole";

    return this.createQuery(hql).setParameterList("grpIds", grpIds).setParameter("psnId", psnId).list();
  }

  /**
   * 获取我的所有群组Id
   * 
   * @param psnId
   * @param searchByRole
   * @return
   */
  public List<Long> getMyGrpIds(Long psnId, Integer searchByRole, Integer grpCategory) {
    String hql = "select 1 from GrpBaseinfo t1 where t1.status='01' and t1.grpId=t.grpId";
    if (grpCategory != null && grpCategory != 0) {
      hql += " and t1.grpCategory = " + grpCategory;
    }
    String hql1 = "select t.grpId from GrpMember t where exists(" + hql + ") and t.status='01'";
    String hql21 = " and (t.grpRole=1 or t.grpRole=2)";
    String hql22 = " and t.grpRole=3";
    String hql3 = "and t.psnId=:psnId order by t.topDate desc nulls last, t.lastVisitDate desc nulls last";
    StringBuilder sb = new StringBuilder();
    sb.append(hql1);
    if (searchByRole == null) {
      sb.append(hql3);
      return this.createQuery(sb.toString()).setParameter("psnId", psnId).list();
    }
    if (searchByRole == 1) {// 所有我的群组
    } else if (searchByRole == 2) {// 我管理的群组
      sb.append(hql21);
    } else if (searchByRole == 3) {// 我是普通成员的群组
      sb.append(hql22);
    }
    sb.append(hql3);
    return this.createQuery(sb.toString()).setParameter("psnId", psnId).list();
  }


  /**
   * 获取群组成员角色
   * 
   * @param psnId
   * @param grpId
   * @return
   */
  public Integer getRoleById(Long psnId, Long grpId) {
    String hql = "select t.grpRole from GrpMember t where t.psnId=:psnId and t.grpId=:grpId";
    return (Integer) this.createQuery(hql).setParameter("psnId", psnId).setParameter("grpId", grpId).uniqueResult();
  }

  /**
   * 修改群组成员角色
   * 
   * @param newRol
   * @param psnId
   * @param grpId
   * @return
   */
  public boolean updateRol(Integer newRol, Long psnId, Long grpId) {
    GrpMember grpMember = this.getByPsnIdAndGrpId(psnId, grpId);
    if (grpMember != null) {
      grpMember.setGrpRole(newRol);
      this.save(grpMember);
      return true;
    } else {
      return false;
    }
  }

  /**
   * 通过psnId和grpId获取GrpMember对象
   * 
   * @param psnId
   */
  public GrpMember getByPsnIdAndGrpId(Long psnId, Long grpId) {
    String hql = "from GrpMember t where t.psnId=:psnId and t.grpId=:grpId";
    return (GrpMember) this.createQuery(hql).setParameter("psnId", psnId).setParameter("grpId", grpId).uniqueResult();
  }

  /**
   * 修改群组成员状态
   * 
   * @param psnId
   * @param grpId
   * @return
   */
  public boolean setStatusById(Long psnId, Long grpId, String status) {
    GrpMember grpMember = getByPsnIdAndGrpId(psnId, grpId);
    if (grpMember == null) {
      return false;
    } else {
      grpMember.setStatus(status);
      this.save(grpMember);
      return true;
    }

  }

  /**
   * 判断是否是群组成员
   * 
   * @param invitePsnId
   * @return
   */
  public boolean isMember(Long psnId, Long grpId) {
    return false;
  }

  /**
   * 添加群组成员
   * 
   * @param GrpMember
   * @return
   */
  public void addGrpMember(GrpMember g) {
    GrpMember member = this.getByPsnIdAndGrpId(g.getPsnId(), g.getGrpId());
    if (member != null) {// 如果是原本被移除的成员
      member.setStatus("01");
      member.setGrpRole(3);
      this.save(member);
    } else {// 新成员
      g.setGrpRole(3);
      g.setStatus("01");
      g.setCreateDate(new Date());
      this.save(g);
    }
  }

  /**
   * 获取群组好友成员psnid 成果数量最多 5个
   * 
   * @param grpId
   * @param ownerPsnId
   * @return
   */
  public List<Object[]> getGrpFriendMemberForDiscuss(Long grpId, Long ownerPsnId) {

    String hql = " select  ps.psnId  , ps.pubSum    " + " from PsnStatistics  ps " + " where ps.psnId in "
        + "( select  g.psnId   " + "from GrpMember  g  " + "where g.psnId in " + "  (select  f.friendPsnId "
        + "  from Friend f"
        + "   where  f.psnId =:ownerPsnId )and  g.status = '01'  and g.grpId=:grpId )  order by  ps.pubSum desc";
    @SuppressWarnings("unchecked")
    List<Object[]> list = this.createQuery(hql).setParameter("ownerPsnId", ownerPsnId).setParameter("grpId", grpId)
        .setMaxResults(5).list();
    return list;
  }

  /**
   * 获取群组成员psnid 成果数量最
   * 
   * @param grpId
   * @param ownerPsnId
   * @return
   */
  public List<Object[]> getGrpMemberForDiscuss(Long grpId, Long ownerPsnId, List<Long> notInPsnId) {

    String hql = " select  ps.psnId  , ps.pubSum    " + " from PsnStatistics  ps " + " where ps.psnId in "
        + "( select  g.psnId   " + "from GrpMember  g  " + "where    g.status = '01'  and g.grpId=:grpId )  ";
    if (notInPsnId != null && notInPsnId.size() > 0) {
      hql += " and  ps.psnId  not in (:notInPsnId)  ";
    }
    hql += " order by ps.pubSum desc";;
    Query query = this.createQuery(hql).setParameter("grpId", grpId);

    if (notInPsnId != null && notInPsnId.size() > 0) {
      query.setParameterList("notInPsnId", notInPsnId);
    }

    List<Object[]> list = query.list();
    return list;
  }

}
