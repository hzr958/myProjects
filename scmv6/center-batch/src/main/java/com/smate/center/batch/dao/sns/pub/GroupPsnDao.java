package com.smate.center.batch.dao.sns.pub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.GroupPsnNodeSum;
import com.smate.center.batch.model.sns.pub.GroupPsnNodeSumPk;
import com.smate.core.base.psn.model.profile.PsnDiscipline;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 群组Dao.
 * 
 * yanmingzhuang
 */
@Deprecated
@Repository
public class GroupPsnDao extends SnsHibernateDao<GroupPsn, Long> {

  /**
   * 统计psnId有效的群组数据
   * 
   * @param psnId
   * @return
   */
  public Long getGroupCountByPsnId(Long psnId) {

    String hql = "select count(*) from GroupPsn g where g.ownerPsnId= ? and g.status = '01' ";
    return (Long) super.createQuery(hql, psnId).uniqueResult();
  }

  public Long findNewGroupId() {

    BigDecimal groupId =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_GROUP_PSN.nextval from dual").uniqueResult();
    return groupId.longValue();
  }

  public String findPersonGroupName(Long psnId) {
    String hql = "select g.groupName from GroupPsn g where g.groupId= ? ";
    return (String) super.createQuery(hql, psnId).uniqueResult();
  }

  public Long creatGroupNo() {

    BigDecimal groupNo =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_GROUP_NO.nextval from dual").uniqueResult();
    return groupNo.longValue();
  }

  public void changeGroupOwner(Long psnId, Long groupId) {

    String hql = "update GroupPsn t set t.ownerPsnId = ? where t.groupId = ? ";
    super.createQuery(hql, psnId, groupId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<GroupPsn> findMyGroupList(Long psnId, String groupCategory, String ownerType) {
    StringBuffer hql = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    hql.append("select gp from GroupInvitePsn gip");
    hql.append(" join gip.groupPsn gp ");
    hql.append(" where (gip.isAccept='1' or gip.isAccept is null) and gp.status='01' and gip.status='01'");
    hql.append(" and gip.psnId=? ");
    params.add(psnId);
    if (StringUtils.isNotEmpty(groupCategory) && !"0".equals(groupCategory)) {
      hql.append(" and gp.groupCategory=? ");
      params.add(groupCategory);
    }

    if (StringUtils.isNotEmpty(ownerType)) {

      if ("1".equals(ownerType)) {// 我创建的群组
        hql.append(" and gip.groupRole='1' ");
      } else if ("0".equals(ownerType)) {// 我参与的群组
        hql.append(" and gip.groupRole in ('2','3')");
      }
    }

    hql.append(" order by gp.groupId desc ");
    Query query = createQuery(hql.toString(), params.toArray());

    return query.list();
  }

  public GroupPsn findMyGroup(Long groupId) {
    String hql = "from GroupPsn where groupId=? and status='01'";
    return super.findUnique(hql, groupId);
  }

  public void saveGroupPsn(GroupPsn groupPsn) {
    super.getSession().save(groupPsn);
  }

  @SuppressWarnings("unchecked")
  public List<GroupPsn> findSearchGroupList(String groupCategory, String groupName, String discipline) {
    StringBuffer hql = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    hql.append("from GroupPsn where 1=1 ");

    if (StringUtils.isNotEmpty(groupCategory) && !"0".equals(groupCategory)) {
      hql.append(" and groupCategory=? ");
      params.add(groupCategory);
    }

    if (StringUtils.isNotEmpty(groupName)) {
      hql.append(" and groupName like ? ");
      params.add("%" + groupName + "%");
    }

    if (StringUtils.isNotEmpty(discipline)) {
      hql.append(" and (discipline1=? or discipline2=? or discipline3=?)");
      params.add(discipline);
      params.add(discipline);
      params.add(discipline);
    }

    hql.append(" order by groupId desc ");

    Query query = createQuery(hql.toString(), params.toArray());

    return query.list();
  }

  /**
   * 群组列表分页查询.
   * 
   * @param groupCategory
   * @param disciplines
   * @param groupName
   * @param page
   * @return @
   */
  @SuppressWarnings("unchecked")
  public Page<GroupPsn> findSearchGroupList(String groupCategory, String disciplines, String groupName,
      Page<GroupPsn> page) {

    String countHql = "select count(groupId) ";
    String orderHql = "order by groupId desc ";

    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupPsn t where status='01' and openType in('O','H')");

    List<Object> params = new ArrayList<Object>();

    if (StringUtils.isNotEmpty(groupCategory) && !"0".equals(groupCategory) && !"8".equals(groupCategory)
        && !"9".equals(groupCategory)) {
      hql.append(" and groupCategory=? ");
      params.add(groupCategory);
    }

    if (StringUtils.isNotEmpty(disciplines)) {
      hql.append(" and discCodes like ? ");
      params.add("%" + disciplines + "%");
    }

    if (StringUtils.isNotEmpty(groupName)) {
      hql.append(" and keyWords like ? ");
      params.add("%" + StringUtils.trim(groupName) + "%");
    }

    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    Query queryResult = super.createQuery(hql + orderHql, params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
    return page;
  }

  @SuppressWarnings("unchecked")
  public List<GroupPsnNodeSum> sumGroupByCategory(Integer nodeId) {
    String hql =
        "select count(groupId) as categorySum,groupCategory from GroupPsn where status='01' and openType in('O','H') and groupCategory is not null group by groupCategory";

    List<Object[]> list = super.createQuery(hql).list();
    List<GroupPsnNodeSum> groupPnsNodeSumList = new ArrayList<GroupPsnNodeSum>();
    for (Object[] object : list) {
      GroupPsnNodeSum groupPsnNodeSum = new GroupPsnNodeSum();
      groupPsnNodeSum.setCategorySum(NumberUtils.toInt(ObjectUtils.toString(object[0])));
      groupPsnNodeSum.setId(new GroupPsnNodeSumPk(ObjectUtils.toString(object[1]), nodeId));

      groupPnsNodeSumList.add(groupPsnNodeSum);
    }

    return groupPnsNodeSumList;
  }

  // 得到感兴趣的群组
  @SuppressWarnings("unchecked")
  public List<GroupPsn> findGroupPsnsByDis(List<PsnDiscipline> disciplines, Integer num, long psnId) {
    StringBuilder hql = new StringBuilder();
    hql.append(
        "select new GroupPsn(t.groupName,t.groupId,n.nodeId,t.sumMembers) from GroupPsn t,GroupPsnNode n  where t.groupId=n.groupId and  t.status='01' and t.openType in('O','H')");

    List<Object> params = new ArrayList<Object>();
    hql.append(
        " and t.groupId not in (select n.id.groupId from  GroupInvitePsnNode n where n.id.psnId=? and n.isAccept='1') ");
    params.add(psnId);
    if (disciplines.size() > 0) {
      hql.append(" and (");
      boolean flag = true;
      for (PsnDiscipline dis : disciplines) {
        if (StringUtils.isNotEmpty(dis.getDisc().getDiscCode())) {
          if (flag) {
            flag = false;
            hql.append(" t.discCodes like ? ");
            params.add("%" + dis.getDisc().getDiscCode() + "%");
          } else {
            hql.append(" or t.discCodes like ? ");
            params.add("%" + dis.getDisc().getDiscCode() + "%");
          }

        }
      }
      hql.append(" ) order by t.sumMembers desc");
    }
    Query queryResult = super.createQuery(hql.toString(), params.toArray());
    queryResult.setMaxResults(num);
    return queryResult.list();

  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Page<GroupPsn> findGroupListForBpo(Map searchMap, Page<GroupPsn> page) {
    StringBuffer hql = new StringBuffer();
    List params = new ArrayList();
    hql.append("from GroupPsn t where t.status = '01' ");
    if (StringUtils.isNotEmpty(MapUtils.getString(searchMap, "searchKey"))) {
      hql.append(" and ( t.groupName like ? or t.groupDescription like ? ) ");
      params.add("%" + MapUtils.getObject(searchMap, "searchKey") + "%");
      params.add("%" + MapUtils.getObject(searchMap, "searchKey") + "%");

    }
    if (MapUtils.getObject(searchMap, "fromDate") != null || MapUtils.getObject(searchMap, "toDate") != null) {

      hql.append("  and (t.updateDate between ?  and ? or t.createDate between ?  and ? ) ");
      params.add(MapUtils.getObject(searchMap, "fromDate"));
      params.add(MapUtils.getObject(searchMap, "toDate"));
      params.add(MapUtils.getObject(searchMap, "fromDate"));
      params.add(MapUtils.getObject(searchMap, "toDate"));

    }
    return super.findPage(page, hql.toString(), params.toArray());
  }

  /**
   * 成果推荐/群组推荐
   */
  @SuppressWarnings("unchecked")
  public List<GroupPsn> getGroupsByIds(List<Long> ids) {
    return super.createQuery(" from GroupPsn t where t.groupId in (:ids)").setParameterList("ids", ids).list();
  }

  // ==============人员合并 start============
  @SuppressWarnings("unchecked")
  public List<GroupPsn> getListByPsnId(Long delPsnId, Long groupId) {
    String hql = "from GroupPsn where ownerPsnId=? and groupId=?";
    return super.createQuery(hql, delPsnId, groupId).list();
  }

  public void updateMembersByGroupId(Long groupId, Integer sumToMembers, Integer sumMembers) {
    GroupPsn g = super.findUnique("from GroupPsn where groupId=?", groupId);
    if (g != null) {
      g.setSumToMembers(sumToMembers);
      g.setSumMembers(sumMembers);
      this.save(g);
    }
    // super.createQuery("update GroupPsn t set t.sumToMembers = ? ,
    // t.sumMembers = ? where t.groupId = ?",
    // sumToMembers, sumMembers, groupId);
  }

  // ==============人员合并 end============

  public Long getGroupOnwer(Long groupId) {

    return super.findUnique("select ownerPsnId from GroupPsn where groupId=?", groupId);
  }

  /**
   * 获取群组数据任务列表_MJG_SCM-6000.
   * 
   * @param startGroupId
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getTaskIdList(Long startGroupId, int maxSize) {
    String hql = "select groupId from GroupPsn t where t.groupId>? order by t.groupId ";
    return super.createQuery(hql, startGroupId).setMaxResults(maxSize).list();
  }
}
