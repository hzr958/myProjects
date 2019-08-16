package com.smate.web.group.dao.grp.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.group.action.grp.form.GrpMemberForm;
import com.smate.web.group.model.grp.member.GrpMember;

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
    String hql =
        "select count(t.id) from GrpMember t where exists(select 1 from Person p where p.personId = t.psnId) and t.status=01 and t.grpId=:grpId ";
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
   * 获取群组在查询出的列表中的rowNum
   * 
   * @param grpId
   * @param psnId
   * @return
   */
  public BigDecimal findGrpRownum(Long grpId, Long psnId) {
    String sql =
        "select grp_column from ( select rownum as grp_column, tmp.grp_id from (select t.grp_id from V_GRP_MEMBER t where t.psn_id = :psnId and t.status = '01' order by t.top_date desc nulls last, t.last_visit_date desc nulls last) tmp ) where grp_id = :grpId";
    return (BigDecimal) this.getSession().createSQLQuery(sql).setParameter("grpId", grpId).setParameter("psnId", psnId)
        .uniqueResult();
  }

  /**
   * 获取各个群组角色统计数
   * 
   * @param psnIds
   */
  public List<Map<String, Object>> getRoleCount(List<Long> grpIds, Long psnId) {
    String hql =
        " select new Map(t.grpRole as grpRole,nvl(count(t.id),0) as count) from GrpMember t where t.grpId in(:grpIds) and t.status = '01'  and t.psnId=:psnId group by t.grpRole";
    return this.createQuery(hql).setParameterList("grpIds", grpIds).setParameter("psnId", psnId).list();
  }

  /**
   * 获取我的所有群组Id
   * 
   * @param psnId
   * @param searchByRole
   * @return
   */
  public List<Long> getMyGrpIds(Long psnId, Integer searchByRole, Integer grpCategory, String searchKey) {
    String hql = "select 1 from GrpBaseinfo t1 where t1.status='01' and t1.grpId=t.grpId ";
    if (StringUtils.isNotBlank(searchKey)) {
      hql += " and instr(upper(t1.grpName),upper('" + searchKey + "'))>0  ";
    }
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
   * 获取群组成员列表 PsnStatistics
   * 
   * @return
   */
  public List<GrpMember> getMembersBySearch(GrpMemberForm form) {
    StringBuilder sb = new StringBuilder();
    Page page = form.getPage();
    sb.append(" from GrpMember t,PsnStatistics t2 where t.psnId=t2.psnId and ");
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      sb.append(
          "exists( select 1 from Person t1 where  " + "(instr(upper(nvl(t1.name,t1.firstName||t1.lastName)),upper('"
              + form.getSearchKey() + "'))>0 or " + "instr(upper(nvl(t1.ename,t1.firstName||t1.lastName)),upper('"
              + form.getSearchKey() + "'))>0) and" + " t1.personId=t.psnId  ) and ");
    } else {
      // 增加 exists(select 1 from Person p where p.personId = t.psnId) 判断人员存不存在
      sb.append(" exists(select 1 from Person p where p.personId = t.psnId) and ");
    }
    sb.append(" t.status=01 and t.grpId=:grpId  ");
    String hqlCount = "select count(t.id) " + sb.toString();
    String hql = "select new GrpMember(t.id,t.psnId) "
        + sb.append(" order by t.grpRole asc ,t2.pubSum desc ,t.id asc nulls last").toString();
    Long count = (Long) this.createQuery(hqlCount).setParameter("grpId", form.getGrpId()).uniqueResult();
    form.setPsnCount(count.intValue());
    return this.createQuery(hql).setParameter("grpId", form.getGrpId()).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  }

  /**
   * 获取所有群组成员
   * 
   * @param form
   * @return
   */
  public List<GrpMember> getMembers(GrpMemberForm form) {
    // 需求变更 - 排序由按成果数排序修改为按姓名排序
    // String hql = "select new GrpMember(t.id,t.psnId) from GrpMember t,PsnStatistics t1 where
    // t.psnId=t1.psnId and t1.pubSum>0 and t.status=01 and t.grpId=:grpId order by t1.pubSum desc nulls
    // last";
    String hql =
        "select new GrpMember(t.id,t.psnId) from GrpMember t where  exists(select 1 from PsnStatistics t1 where t1.pubSum>0 and t.psnId=t1.psnId ) "
            + "and exists(select 1 from Person p where p.personId = t.psnId) " + "and  t.status=01 and t.grpId=:grpId ";
    return this.createQuery(hql).setParameter("grpId", form.getGrpId()).list();
  }

  public Long getMembersForOne(Long grpId) {
    String hql = "select t.psnId from GrpMember t,PsnStatistics t1 where t.psnId=t1.psnId "
        + "and exists(select 1 from Person p where p.personId = t.psnId) "
        + "and t1.pubSum>0 and  t.status=01 and t.grpId=:grpId order by t1.pubSum desc nulls last";
    List<Long> list = this.createQuery(hql).setParameter("grpId", grpId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  /**
   * 获取群组成员角色
   * 
   * @param psnId
   * @param grpId
   * @return
   */
  public Integer getRoleById(Long psnId, Long grpId) {
    String hql = "select t.grpRole from GrpMember t where t.psnId=:psnId and t.grpId=:grpId and t.status='01'";
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
   * 是否是群组成员
   * 
   * @param psnId
   */
  public boolean isGrpMember(Long psnId, Long grpId) {
    String hql = "select count(t.id) from GrpMember t where t.psnId=:psnId and t.status='01' and t.grpId=:grpId";
    Object object = this.createQuery(hql).setParameter("psnId", psnId).setParameter("grpId", grpId).uniqueResult();
    if (object != null && (Long) object > 0) {
      return true;
    } else {
      return false;
    }
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
    Date date = new Date();
    if (member != null) {// 如果是原本被移除的成员
      member.setStatus("01");
      member.setGrpRole(3);
      member.setCreateDate(date);
      member.setLastVisitDate(date);
      this.save(member);
    } else {// 新成员
      g.setGrpRole(3);
      g.setStatus("01");
      g.setCreateDate(date);
      g.setLastVisitDate(date);
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
    String hql = "select ps.psnId,ps.pubSum from PsnStatistics ps " + "where exists ( " + "select 1 from GrpMember g "
        + "where exists (select 1 from Friend f where g.psnId = f.friendPsnId and f.psnId =:ownerPsnId ) "
        + "and exists(select 1 from Person p where p.personId = g.psnId) "
        + "and ps.psnId=g.psnId and g.status = '01' and g.grpId=:grpId ) " + "order by ps.pubSum desc";
    @SuppressWarnings("unchecked")
    List<Object[]> list = this.createQuery(hql).setParameter("ownerPsnId", ownerPsnId).setParameter("grpId", grpId)
        .setMaxResults(5).list();
    return list;
  }

  /**
   * 获取自己,成果数量
   * 
   * @param grpId
   * @return
   */
  public Object[] getSelfPubSum(Long grpId, Long ownerPsnId) {
    String HQL = "select ps.psnId,ps.pubSum from PsnStatistics ps "
        + "where exists ( select 1 from GrpMember g where ps.psnId=g.psnId and g.status = '01' and g.grpId=:grpId ) "
        + " and ps.psnId =:psnId order by ps.pubSum desc";
    Query query = getSession().createQuery(HQL).setParameter("psnId", ownerPsnId).setParameter("grpId", grpId);
    Object[] obj = (Object[]) query.uniqueResult();
    return obj;
  }

  /**
   * 获取群组成员psnid 成果数量最
   * 
   * @param grpId
   * @param ownerPsnId
   * @return
   */
  public List<Object[]> getGrpMemberForDiscuss(Long grpId, Long ownerPsnId, List<Long> notInPsnId) {
    String hql = "select ps.psnId,ps.pubSum from PsnStatistics ps " + " where ps.psnId in "
        + " (select g.psnId from GrpMember g where exists(select 1 from Person p where p.personId = g.psnId) "
        + " and g.status='01' and g.grpId=:grpId ) ";
    if (notInPsnId != null && notInPsnId.size() > 0) {
      hql += "and ps.psnId not in (:notInPsnId) ";
    }
    hql += " order by ps.pubSum desc";;
    Query query = this.createQuery(hql).setParameter("grpId", grpId);
    if (notInPsnId != null && notInPsnId.size() > 0) {
      query.setParameterList("notInPsnId", notInPsnId);
    }
    List<Object[]> list = query.list();
    return list;
  }

  public List<Long> getGrpManagers(Long grpId) {
    String hql = "select g.psnId from GrpMember g where exists(select 1 from Person p where p.personId = g.psnId) "
        + "and g.grpId=:grpId and grpRole in(1,2) and status='01'";
    return this.createQuery(hql).setParameter("grpId", grpId).list();
  }

  public List<Long> getGrpMembers(Long grpId) {
    // 老sq：存在的问题，没有对不存在人员进行判断 select g.psnId from GrpMember g where g.grpId=:grpId and status='01'
    String hql = "select p.personId from Person p where "
        + "exists (select 1 from GrpMember g where p.personId = g.psnId and g.grpId=:grpId and g.status='01')";
    return this.createQuery(hql).setParameter("grpId", grpId).list();
  }

  /**
   * 查询管理员的psnId集合
   * 
   * @return
   */
  public List<Long> findManagerPsnIdByGrpId(Long grpId, List<Integer> roleList) {
    String hql = "select g.psnId from GrpMember g where exists(select 1 from Person p where p.personId = g.psnId) "
        + "and g.grpId=:grpId and status='01' and g.grpRole in ( :roleList ) ";
    return this.createQuery(hql).setParameter("grpId", grpId).setParameterList("roleList", roleList).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPrjCoBygrpId(Long prjGroupId, Long psnId) {
    String hql =
        "select distinct t.psnId from GrpMember t where t.grpId = :grpId and t.status='01'  and t.psnId not in (:psnId)";
    return createQuery(hql).setParameter("grpId", prjGroupId).setParameter("psnId", psnId).list();
  }
}
