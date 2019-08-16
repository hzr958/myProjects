package com.smate.web.group.dao.grp.grpbase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.group.action.grp.form.GrpMainForm;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;

/**
 * 群组基础信息dao
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class GrpBaseInfoDao extends SnsHibernateDao<GrpBaseinfo, Long> {
  /**
   * 获取最近的群组列表
   * 
   * @param psnId
   * @return
   */
  public List<GrpBaseinfo> getGrpList(Long psnId, Page page) {
    String hql = "select new GrpBaseinfo(t.grpName,t.grpDescription,t.grpCategory,t.grpAuatars,t.grpId ) "
        + " from GrpBaseinfo t,GrpMember p where t.grpId=p.grpId and t.status='01' and p.psnId=:psnId and p.status='01' "
        + " order by p.topDate desc nulls last, p.lastVisitDate desc nulls last,t.grpId desc ";
    return this.createQuery(hql).setParameter("psnId", psnId).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  }

  /**
   * 获取群组信息
   * 
   * @param grpId
   * @return
   */
  public GrpBaseinfo getGrpBaseinfoForIvite(Long grpId) {
    String hql =
        "select new GrpBaseinfo(t.grpName,t.grpDescription,t.grpCategory,t.grpAuatars) from GrpBaseinfo t where t.status='01' and t.grpId=:grpId";
    return (GrpBaseinfo) this.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
  }

  /**
   * 获取所有我加入的群组
   * 
   * @param form
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GrpBaseinfo> getAllMyGrpBaseinfos(GrpMainForm form) throws Exception {

    List<Object> params = new ArrayList<Object>();
    String selectHql = "select new GrpBaseinfo(t.grpId , t.grpName, t.grpCategory, t.grpAuatars)";
    String hql =
        " from GrpBaseinfo t , GrpMember gm where gm.grpId=t.grpId and gm.status='01' and gm.psnId=? and t.status='01' ";
    params.add(form.getPsnId());
    if (StringUtils.isNoneBlank(form.getSearchKey())) {
      hql += " and instr(upper(t.grpName),?)>0";
      String searchKey = form.getSearchKey().replaceAll("\'", "&#39;");
      searchKey = searchKey.toUpperCase().trim();
      params.add(searchKey);
    }
    hql += " order by gm.lastVisitDate desc nulls last,gm.grpId asc";
    // 查询总页数
    Query queryCt = super.createQuery("select count(t.grpId) " + hql.toString(), params.toArray());
    Long count = (Long) queryCt.uniqueResult();
    form.getPage().setTotalCount(count.intValue());

    if (!form.getSelectAll()) {// 分页
      return super.createQuery(selectHql + hql, params.toArray()).setFirstResult(form.getPage().getFirst() - 1)
          .setMaxResults(10).list();
    } else {// 查全部
      return super.createQuery(selectHql + hql, params.toArray()).list();
    }
  }



  /**
   * 获取所有我已加入的群组
   * 
   * @param form
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GrpBaseinfo> searchMyGrpBaseinfos(GrpMainForm form) {
    List<Object> params = new ArrayList<Object>();
    StringBuffer countHql = new StringBuffer("select count(1)");
    StringBuffer hql = new StringBuffer("select new GrpBaseinfo(t.grpId , t.grpName )");
    hql.append(
        " from GrpBaseinfo t , GrpMember gm where gm.grpId=t.grpId and gm.status='01' and gm.psnId=? and t.status='01' ");
    countHql.append(
        " from GrpBaseinfo t , GrpMember gm where gm.grpId=t.grpId and gm.status='01' and gm.psnId=? and t.status='01' ");
    params.add(form.getPsnId());
    if (StringUtils.isNoneBlank(form.getSearchKey())) {
      hql.append(" and instr(upper(t.grpName),?)>0");
      countHql.append(" and instr(upper(t.grpName),?)>0");
      String searchKey = form.getSearchKey().replaceAll("\'", "&#39;").toUpperCase().trim();
      params.add(searchKey);
    }
    hql.append(" order by gm.lastVisitDate desc nulls last");
    Long totalCount = (Long) super.createQuery(countHql.toString(), params.toArray()).uniqueResult();
    form.getPage().setTotalCount(totalCount);
    return super.createQuery(hql.toString(), params.toArray()).setFirstResult(form.getPage().getFirst() - 1)
        .setMaxResults(10).list();

  }

  /**
   * 自动填词-获取所有我加入的群组
   * 
   * @param form
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GrpBaseinfo> getAutoGrpNames(GrpMainForm form) throws Exception {
    if (form.getMaxResults() == null || form.getMaxResults() == 0) {
      form.setMaxResults(5);
    }
    List<Object> params = new ArrayList<Object>();

    String hql = "select new GrpBaseinfo(grpId,grpName ) from GrpBaseinfo t where exists "
        + "(select 1 from GrpMember gm where gm.psnId=:psnId and  gm.grpId=t.grpId and gm.status='01' ) "
        + "and status='01' and instr(upper(t.grpName),:searchKey)>0 order by instr(upper(t.grpName),:searchKey) asc ";
    return this.createQuery(hql).setParameter("psnId", form.getPsnId())
        .setParameter("searchKey",
            StringUtils.isNotBlank(form.getSearchKey()) ? form.getSearchKey().toUpperCase().trim() : "")
        .setMaxResults(form.getMaxResults()).list();
  }

  /**
   * 通过序列获取grpId
   * 
   * @return
   */
  public Long getGrpIdBySeq() {
    String sql = "select SEQ_V_GRP_BASEINFO.nextval from dual";
    Object object = this.getSession().createSQLQuery(sql).uniqueResult();
    if (object != null) {
      return Long.parseLong(object.toString());
    } else {
      return null;
    }
  }

  /**
   * 获取群组公开类型
   * 
   * @param grpId
   * @return
   */
  public String getGrpOpenType(Long grpId) {
    String hql = "select t.openType from GrpBaseinfo t where t.grpId=:grpId";

    return (String) this.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
  }

  /**
   * 获取群组类别
   * 
   * @param grpId
   * @return
   */
  public Integer getGrpCatetory(Long grpId) {
    String hql = "select t.grpCategory from GrpBaseinfo t where t.grpId=:grpId";
    return (Integer) this.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
  }

  /**
   * 获取各个群组类型统计数
   * 
   * @return
   */
  public List<Map<String, Object>> getGrpCategoryCount(List<Long> grpIds) {
    String hql =
        " select new Map(t.grpCategory as grpCategory,nvl(count(t.grpId),0) as count) from GrpBaseinfo t where t.grpId in(:grpIds) group by t.grpCategory";
    return this.createQuery(hql).setParameterList("grpIds", grpIds).list();

  }

  public String getGrpDesc(Long grpId) {
    String hql = "select  g.grpDescription from  GrpBaseinfo  g where g.grpId =:grpId  and g.status='01'  ";
    Object obj = this.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
    if (obj != null) {
      return obj.toString();
    }
    return null;
  }

  /**
   * 判断群组是否存在
   * 
   * @param grpId
   * @return
   */
  public boolean isExist(Long grpId) {
    String hql = "select count(t.grpId) from GrpBaseinfo t where t.grpId =:grpId and t.status = 01";
    Object result = this.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
    if (result != null && (Long) result > 0) {
      return true;
    } else {
      return false;
    }

  }

  public List<Long> getGrpIdsByGrpCategory(List<Long> grpIds, Integer grpCategory) {
    if (grpIds == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    String hql1 = "select t.grpId from GrpBaseinfo t where ";
    String hql2 = " t.grpId in(:grpIds) ";
    sb.append(hql1);
    sb.append(" t.grpCategory =" + grpCategory + " and");
    sb.append(hql2);
    return this.createQuery(sb.toString()).setParameterList("grpIds", grpIds).list();
  }

  /**
   * 根据grpIdList查询我的群组列表信息
   * 
   * @param grpIds
   * @param grpCategory
   * @param firstResult
   * @param maxResults
   * @return
   */
  public List<GrpBaseinfo> getGrpBaseInfoList(List<Long> grpIds, GrpMainForm form) {
    Page page = form.getPage();
    page.setTotalCount(grpIds.size());
    StringBuilder sb = new StringBuilder();
    String hql1 = "from GrpBaseinfo t where ";
    sb.append(hql1);
    String orderby = "t.grpId in(:grpIds) ORDER BY INSTR(:grpIds2 ,t.grpId)";
    sb.append(orderby);
    int fromIndex = page.getFirst() - 1;
    int toIndex = page.getFirst() - 1 + page.getPageSize();
    if (toIndex > grpIds.size()) {
      toIndex = grpIds.size();
    }
    grpIds = grpIds.subList(fromIndex, toIndex);
    if (grpIds.size() > 0) {
      String grpIdStr = grpIds.toString();
      return this.createQuery(sb.toString()).setParameterList("grpIds", grpIds)
          .setParameter("grpIds2", grpIdStr.substring(1, grpIdStr.length() - 1)).list();
    } else {
      return null;
    }

  }

  /**
   * 通过 群组id列表 获取群组信息
   * 
   * @param grpIds
   * @return
   */
  public List<GrpBaseinfo> getGrpBaseInfoListByIds(List<Long> grpIds) {
    String hql1 = "from GrpBaseinfo g where  g.grpId in( :grpIds )  ";
    return this.createQuery(hql1).setParameterList("grpIds", grpIds).list();
  }

  /**
   * 
   * @param grpId
   * @return
   */
  public String getGrpNameByGrpId(Long grpId) {
    String hql = "select t.grpName from GrpBaseinfo t where t.grpId=:grpId";
    Object object = this.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
    if (object != null) {
      return object.toString();
    }
    return "";
  }

  /**
   * 获取项目编号
   * 
   * @param grpId
   * @return
   */
  public String getProjectNo(Long grpId) {
    String hql = "select g.projectNo from GrpBaseinfo g where g.grpId =:grpId";
    return (String) super.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
  }

  /**
   * 获取我的群组数量（包括申请中的群组）
   * 
   * @return
   */
  public Long getMyGrpCount(Long psnId) {
    String hql = "select count(t.id) from GrpBaseinfo t where ( "
        + " exists(select 1 from GrpMember t2 where t2.grpId = t.grpId and  t2.psnId=:psnId and t2.status='01' ) or "
        + " exists(select 1 from GrpProposer t3 where t3.grpId = t.grpId and t3.psnId=:psnId and t3.type=1 and t3.isAccept=2 ) "
        + ") and t.status = '01'";
    return (Long) this.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  public boolean isNsfcProject(Long groupId) {
    boolean rs = false;
    String sql = "select count(1) from group_fundinfo t where t.group_id =:groupId";
    BigDecimal count =
        (BigDecimal) this.getSession().createSQLQuery(sql).setParameter("groupId", groupId).uniqueResult();
    if (count != null && count.longValue() > 0) {
      rs = true;
    }
    return rs;
  }

}
