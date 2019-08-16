package com.smate.web.group.dao.group.invit;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.group.form.GroupInfoForm;
import com.smate.web.group.form.GroupPsnForm;
import com.smate.web.group.model.group.invit.GroupInvitePsn;

/**
 * 群组 成员邀请表 dao
 * 
 * @author tsz
 *
 */

@Repository
public class GroupInvitePsnDao extends SnsHibernateDao<GroupInvitePsn, Long> {
  /**
   * 获取我的未删除的群组id集合
   * 
   * @param psnId
   * @return
   */
  public List<Long> getMyGroupIdList(Long psnId) {
    String hql = "select g.groupId from GroupInvitePsn g where exists("
        + "select 1 from GroupFilter gf where gf.status='01' and g.groupId = gf.groupId ) "
        + " and g.isAccept='1' and g.status='01' and g.psnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 判断是否为群组管理员或创建人
   * 
   * @param groupId
   * @param psnId
   * @return
   */
  public Boolean isGroupAdmin(Long groupId, Long psnId) {
    String hql =
        "select gi.invitePsnId from GroupInvitePsn gi where gi.groupId=:groupId and gi.psnId=:psnId and gi.groupRole in ('1','2') and gi.isAccept='1' and gi.status='01' ";
    Object obj = super.createQuery(hql).setParameter("groupId", groupId).setParameter("psnId", psnId).uniqueResult();
    if (obj != null) {
      return true;
    }
    return false;
  }

  /**
   * 获取 群组人员 邀请记录
   * 
   * @param groupId
   * @param psnId
   * @return
   */
  public GroupInvitePsn findGroupInvitePsn(Long groupId, Long psnId) {
    String hql = "from GroupInvitePsn where groupId = ? and psnId=? and status = '01' and isAccept = '1'";
    return this.findUnique(hql, groupId, psnId);
  }

  /**
   * 根据groupId和psnId获取群组记录
   * 
   * @author lhd
   * @param groupId
   * @param psnId
   * @return
   */
  public GroupInvitePsn getGroupInvitePsn(Long groupId, Long psnId) {
    String hql = "from GroupInvitePsn where groupId = ? and psnId=? and status = '01'";
    return this.findUnique(hql, groupId, psnId);
  }

  /**
   * 查询全部（包括待加入）群组成员
   * 
   * @param form
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GroupInvitePsn> findGroupInvitePsnForLeft(GroupPsnForm form) {
    String hql = "from GroupInvitePsn where groupId = ? and status = '01' and (isAccept is null or isAccept <> '0')";
    return super.createQuery(hql, form.getGroupId()).list();
  }

  /**
   * 查询全部群组管理成员（2016-10-10）
   * 
   * @param form
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GroupInvitePsn> findGroupInvitePsnForGroupRole(GroupPsnForm form) {
    String hql =
        "from GroupInvitePsn g where g.groupId = ? and g.status = '01' and g.isAccept =1 order by g.groupRole ";// and
    // g.groupRole
    // in
    // ('1','2')
    return super.createQuery(hql, form.getGroupId()).list();
  }

  /**
   * 获取群组五个成员，创建者 管理员
   * 
   * @param form
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GroupInvitePsn> findFiveMemers(Long groupId) {

    /*
     * String hql =
     * "from GroupInvitePsn where groupId=:groupId and status=:status and groupRole in ( 1,2)  and  isAccept=:isAccept  order by groupRole asc ,createDate asc"
     * ; Query query = createQuery(hql).setParameter("groupId", groupId).setParameter("status", "01")
     * .setParameter("isAccept", "1").setMaxResults(5); return query.list();
     */
    /*
     * String sql = "select psnTemp.Psn_Id  ,  psnTemp.avatars  , psnTemp.Group_Role  ,pubTemp.pub_sum "
     * +"from" +
     * "( select  p.psn_id  , p.group_role  ,p.avatars  from group_invite_psn p where   p.group_id =:groupId  and p.is_accept = '1' and p.status='01'  ) psnTemp "
     * +" left join " +
     * "( select s.psn_id ,  s.pub_sum  from  psn_statistics s  where s.psn_id in (  select  p.psn_id   from group_invite_psn p where   p.group_id =:groupId  and p.is_accept = '1' and p.status='01'  ) ) pubTemp "
     * +
     * "on  psnTemp.Psn_Id = pubTemp.Psn_Id   order by  psnTemp.Group_Role  asc   nulls last  , pubTemp.pub_sum desc    nulls last "
     * ;
     */
    String sql =
        "select g.psn_id, g.avatars, g.group_role, p.pub_sum from psn_statistics p left join group_invite_psn g on p.psn_id = g.psn_id where g.group_id =:groupId and g.is_accept = '1' and g.status = '01' order by g.group_role asc nulls last, p.pub_sum desc nulls last";
    List<List<Object>> resultList = (List<List<Object>>) this.getSession().createSQLQuery(sql)
        .setParameter("groupId", groupId).setResultTransformer(Transformers.TO_LIST).setMaxResults(5).list();
    List<GroupInvitePsn> psnList = new ArrayList<GroupInvitePsn>();
    GroupInvitePsn groupInvitePsn = null;
    for (int i = 0; i < resultList.size(); i++) {
      groupInvitePsn = new GroupInvitePsn();
      Object psnIdObj = resultList.get(i).get(0);
      Object avatarsObj = resultList.get(i).get(1);
      if (psnIdObj != null)
        groupInvitePsn.setPsnId(NumberUtils.toLong(psnIdObj.toString()));
      if (avatarsObj != null)
        groupInvitePsn.setAvatars(avatarsObj.toString());
      psnList.add(groupInvitePsn);
    }
    return psnList;
  }

  /**
   * 获取群组成员列表
   * 
   * @param form
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<GroupInvitePsn> getGroupMembers(GroupInfoForm form) {
    String countHql = "select count(t.invitePsnId)";
    String listHql = "select new GroupInvitePsn(t.invitePsnId,t.psnId,t.groupRole,t.avatars,t.psnName)";
    String orderHql = " order by";
    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupInvitePsn t");
    List<Object> params = new ArrayList<Object>();
    hql.append(" where t.groupId=? and t.status = '01' and t.isAccept='1' ");
    params.add(form.getGroupId());
    // 检索人员(检索姓名即可)
    if (StringUtils.isNotEmpty(form.getSearchKey())) {
      String searchKey = form.getSearchKey().toUpperCase().trim();
      hql.append(" and");
      hql.append(" ( ");
      hql.append(" instr(upper(t.psnName),?)>0");// 姓名
      params.add(searchKey);
      hql.append(" )");
    }
    // 加入时间排序(暂时不处理)
    // if (form.getSortType() != null) {
    //
    // } else {
    // 默认排序
    orderHql += " t.groupRole asc,t.invitePsnId desc";
    // }
    Page page = form.getPage();
    // 总记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);
    // 总页数
    if (page.getPageSize() != null && page.getPageSize() > 0) {
      if (page.getTotalCount() % page.getPageSize() == 0) {
        page.setTotalPages(page.getTotalCount() / page.getPageSize());
      } else {
        page.setTotalPages(page.getTotalCount() / page.getPageSize() + 1);
      }
    }
    // 查询数据实体
    return super.createQuery(listHql + hql + orderHql, params.toArray()).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  }

  /**
   * 删除群组成员
   * 
   * @param groupId
   * @param memberId
   */
  public void deleteMember(Long groupId, Long memberId) {
    String hql =
        "update GroupInvitePsn t set t.status='99', t.isAccept='0',t.groupRole='3' where t.groupId=:groupId and t.psnId=:memberId";
    super.createQuery(hql).setParameter("groupId", groupId).setParameter("memberId", memberId).executeUpdate();
  }

  /**
   * 获取群组成员待审核列表
   * 
   * @param form
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<GroupInvitePsn> getGroupPendings(GroupInfoForm form) {
    String countHql = "select count(t.invitePsnId)";
    String listHql = "select new GroupInvitePsn(t.invitePsnId,t.psnId,t.groupRole,t.avatars,t.psnName)";
    String orderHql = " order by";
    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupInvitePsn t");
    List<Object> params = new ArrayList<Object>();
    hql.append(" where t.groupId=? and t.status = '01' and t.isAccept is null ");
    params.add(form.getGroupId());
    // 检索人员(检索姓名即可)
    if (StringUtils.isNotEmpty(form.getSearchKey())) {
      String searchKey = form.getSearchKey().toUpperCase().trim();
      hql.append(" and");
      hql.append(" ( ");
      hql.append(" instr(upper(t.psnName),?)>0");// 中文标题
      params.add(searchKey);
      hql.append(" )");
    }
    // 加入时间排序(暂时不处理)
    // if (form.getSortType() != null) {
    //
    // } else {
    // 默认排序
    orderHql += " t.invitePsnId desc";
    // }
    Page page = form.getPage();
    // 总记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);
    // 总页数
    if (page.getPageSize() != null && page.getPageSize() > 0) {
      if (page.getTotalCount() % page.getPageSize() == 0) {
        page.setTotalPages(page.getTotalCount() / page.getPageSize());
      } else {
        page.setTotalPages(page.getTotalCount() / page.getPageSize() + 1);
      }
    }
    // 查询数据实体
    return super.createQuery(listHql + hql + orderHql, params.toArray()).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  }

  /**
   * 查询申请人数
   * 
   * @param groupId
   */
  @SuppressWarnings("rawtypes")
  public void findApplyPsn(GroupInfoForm form) {
    String hql =
        "select count(t.invitePsnId) from GroupInvitePsn t where t.groupId=:groupId and t.status = '01' and t.isAccept is null";
    Long totalCount = (Long) super.createQuery(hql).setParameter("groupId", form.getGroupId()).uniqueResult();
    Page page = form.getPage();
    page.setTotalCount(totalCount);
  }

  @SuppressWarnings("unchecked")
  public List<Long> findGroupMemberPsnIdByGroupId(Long groupId) throws Exception {
    String hql = "select psnId from GroupInvitePsn where groupId = ? and status = '01' and isAccept='1'";
    Query query = createQuery(hql, groupId);
    return query.list();
  }

  /**
   * 查询当前人在群组的群组角色
   * 
   * @author lhd
   * @param form
   */
  public void getGroupRole(GroupInfoForm form) {
    String hql =
        "select gi.groupRole from GroupInvitePsn gi where gi.groupId=:groupId and gi.psnId=:psnId and gi.isAccept='1' and gi.status='01' ";
    String currentPsnGroupRole = (String) super.createQuery(hql).setParameter("groupId", form.getGroupId())
        .setParameter("psnId", form.getPsnId()).uniqueResult();
    if (StringUtils.isNotBlank(currentPsnGroupRole)) {
      form.setCurrentPsnGroupRole(Integer.parseInt(currentPsnGroupRole));
    }
  }

}
