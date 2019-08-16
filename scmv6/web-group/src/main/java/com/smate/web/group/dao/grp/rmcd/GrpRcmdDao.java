package com.smate.web.group.dao.grp.rmcd;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.action.grp.form.GrpBaseForm;
import com.smate.web.group.model.grp.rcmd.GrpRcmd;

/**
 * 群组推荐到
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class GrpRcmdDao extends SnsHibernateDao<GrpRcmd, Long> {

  /**
   * 获取群组推荐列表
   * 
   * @param form
   * @param onlyCount
   * @param
   * @param isRcmd
   */
  @SuppressWarnings("unchecked")
  public Integer getGrpRcmdList(GrpBaseForm form, boolean onlyCount) {

    String countsql = " select  count(1) ";
    String hql = "select gb.grp_id as grpId, gb.grp_category";
    StringBuffer sb = new StringBuffer();
    sb.append(" from v_grp_baseinfo gb inner join v_grp_rcmd  gr   on gr.grp_id=gb.grp_id where "
        + " not exists ( select 1 from v_grp_member gm where gm.grp_id = gb.grp_id  and gm.psn_id =:psnId and gm.status = 01 )  "
        + " and   not exists ( select 1 from v_grp_proposer gp  where gb.grp_id =  gp.grp_id and  gp.psn_id =:psnId and gp.is_accept =2  and gp.type = 1 )");

    // 判断检索条件 如果有检索条件就加检索条件 不加 成员数 成果数过
    String searchKey = null;
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      searchKey = form.getSearchKey().replaceAll("\'", "&#39;");
      searchKey = searchKey.toUpperCase().trim();
      sb.append("  and instr(upper(gb.grp_name),:searchKey)>0  ");
    } else {// 成员数大于等于2或者成果数大于0
      sb.append(
          "  and exists  ( select 1  from   v_grp_statistics  gs where   (gs.sum_member >=2 or gs.sum_pubs>0) and gs.grp_id = gb.grp_id  ) ");
    }
    // 群组类别
    if (form.getGrpCategory() != null) {
      sb.append(" and gb.grp_category =:grpCategory  ");
    }
    // 研究领域学科代码
    if (form.getDisciplineCategory() != null) {
      sb.append(
          "and exists (select 1 from v_grp_kw_disc gk where gk.grp_id =gb.grp_id and gk.first_category_id= :firstCategoryId)");
    }
    sb.append(
        "  and gb.status = '01' and  ( gb.open_type = 'O' or gb.open_type = 'H' ) And gr.status=0 and gr.psn_id= :psnId");
    String orderSql = " order by gr.rcmd_score desc  nulls last,gr.grp_id Desc";

    Query queryCount =
        this.getSession().createSQLQuery(countsql + sb.toString()).setParameter("psnId", form.getPsnId());
    Query query =
        this.getSession().createSQLQuery(hql + sb.toString() + orderSql).setParameter("psnId", form.getPsnId());
    // 检索信息
    if (StringUtils.isNotBlank(searchKey)) {
      queryCount.setParameter("searchKey", searchKey);
      query.setParameter("searchKey", searchKey);
    }
    // 群组类别
    if (form.getGrpCategory() != null) {
      queryCount.setParameter("grpCategory", form.getGrpCategory());
      query.setParameter("grpCategory", form.getGrpCategory());
    }
    // 学科代码
    if (form.getDisciplineCategory() != null) {
      queryCount.setParameter("firstCategoryId", form.getDisciplineCategory());
      query.setParameter("firstCategoryId", form.getDisciplineCategory());
    }
    Object countObj = queryCount.uniqueResult();
    Integer count = 0;
    if (count != null) {
      count = NumberUtils.toInt(countObj.toString());
    }
    if (onlyCount) {
      return count;
    } else {
      form.getPage().setTotalCount(count);
      List<Object[]> grpList =
          query.setFirstResult(form.getPage().getFirst() - 1).setMaxResults(form.getPage().getPageSize()).list();
      List<Object> grpIdList = new ArrayList<Object>();
      for (Object[] obj : grpList) {
        grpIdList.add(Objects.toString(obj[0]));
      }
      form.getPage().setResult(grpIdList);
    }
    return null;

  }

  /**
   * 群组推荐表的群组类别统计数
   * 
   * @param form
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getGrpRcmdCategoryStatistic(GrpBaseForm form) {
    StringBuffer sb = new StringBuffer();
    sb.append(
        "  select   gb.grp_Category ,  count(1)   from v_grp_baseinfo gb inner join v_grp_rcmd  gr   on gr.grp_id=gb.grp_id where "
            + "  not exists ( select 1 from v_grp_member gm where gm.grp_id = gb.grp_id  and gm.psn_id =:psnId and gm.status = 01 ) "
            + " and   not exists ( select 1 from v_grp_proposer gp  where gb.grp_id =  gp.grp_id and  gp.psn_id =:psnId and gp.is_accept =2  and gp.type = 1 )");

    // 判断检索条件 如果有检索条件就加检索条件 不加 成员数 成果数过
    String searchKey = null;
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      searchKey = form.getSearchKey().replaceAll("\'", "&#39;");
      searchKey = searchKey.toUpperCase().trim();
      sb.append("  and instr(upper(gb.grp_name),:searchKey)>0  ");
    } else {// 成员数大于等于2或者成果数大于0
      sb.append(
          "  and exists  ( select 1  from   v_grp_statistics  gs where   (gs.sum_member >=2 or gs.sum_pubs>0) and gs.grp_id = gb.grp_id  ) ");
    }
    // 研究领域学科代码
    if (form.getDisciplineCategory() != null) {
      sb.append(
          "and exists (select 1 from v_grp_kw_disc gk where gk.grp_id =gb.grp_id and gk.first_category_id= :firstCategoryId)");
    }
    sb.append(
        "  and gb.status = '01' and  ( gb.open_type = 'O' or gb.open_type = 'H' ) And gr.status=0 and gr.psn_id= :psnId");
    sb.append(" group by  gb.grp_Category   ");

    Query query = this.getSession().createSQLQuery(sb.toString()).setParameter("psnId", form.getPsnId());
    // 检索信息
    if (StringUtils.isNotBlank(searchKey)) {
      query.setParameter("searchKey", searchKey);
    }
    // 学科代码
    if (form.getDisciplineCategory() != null) {
      query.setParameter("firstCategoryId", form.getDisciplineCategory());
    }

    List<Object[]> list = query.list();
    if (list != null) {
      return list;
    }
    return null;

  }

  /**
   * 群组推荐表的研究领域
   * 
   * @param form
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getGrpRcmdDisciplineStatistic(GrpBaseForm form) {

    StringBuffer sb = new StringBuffer();
    sb.append(
        "select gk.first_Category_Id ,count(1) from v_grp_baseinfo gb inner join v_grp_rcmd  gr   on gr.grp_id=gb.grp_id "
            + "inner join v_grp_kw_disc gk on gb.grp_id =gk.grp_id  where "
            + "  not exists ( select 1 from v_grp_member gm where gm.grp_id = gb.grp_id  and gm.psn_id =:psnId and gm.status = 01 )"
            + " and   not exists ( select 1 from v_grp_proposer gp  where gb.grp_id =  gp.grp_id and  gp.psn_id =:psnId and gp.is_accept =2  and gp.type = 1 )  ");

    // 判断检索条件 如果有检索条件就加检索条件 不加 成员数 成果数过
    String searchKey = null;
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      searchKey = form.getSearchKey().replaceAll("\'", "&#39;");
      searchKey = searchKey.toUpperCase().trim();
      sb.append("  and instr(upper(gb.grp_name),:searchKey)>0  ");
    } else {// 成员数大于等于2或者成果数大于0
      sb.append(
          "  and exists  ( select 1  from   v_grp_statistics  gs where   (gs.sum_member >=2 or gs.sum_pubs>0) and gs.grp_id = gb.grp_id  ) ");
    }
    // 群组类别
    if (form.getGrpCategory() != null) {
      sb.append(" and gb.grp_Category =:grpCategory  ");
    }
    sb.append(
        "  and gb.status = '01' and  ( gb.open_type = 'O' or gb.open_type = 'H' ) And gr.status=0 and gr.psn_id= :psnId ");
    sb.append(" group by gk.first_Category_Id ");

    Query query = this.getSession().createSQLQuery(sb.toString()).setParameter("psnId", form.getPsnId());
    // 检索信息
    if (StringUtils.isNotBlank(searchKey)) {
      query.setParameter("searchKey", searchKey);
    }
    // 群组类别
    if (form.getGrpCategory() != null) {
      query.setParameter("grpCategory", form.getGrpCategory());
    }

    List<Object[]> list = query.list();
    if (list != null) {
      return list;
    }
    return null;

  }



  public void updateOptStatus(Long psnId, Long grpId, Integer status) {
    String hql = "update GrpRcmd  g  set g.status = :status   where  g.psnId =:psnId and g.grpId =:grpId ";
    this.createQuery(hql).setParameter("status", status.toString()).setParameter("psnId", psnId)
        .setParameter("grpId", grpId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public void getGrpRcmdBaseList(GrpBaseForm form) {
    String countHql = " select  count(1) ";
    StringBuffer sb = new StringBuffer();
    String hql = "select gb.grpId ";
    sb.append("from GrpBaseinfo  gb  where  not exists "
        + "( select  gm.grpId  from GrpMember gm where gm.grpId = gb.grpId  and gm.psnId =:psnId and gm.status = 01 )  "
        + " and  not exists ( select  gr.grpId from   GrpRcmd  gr where gr.grpId = gb.grpId  and gr.psnId =:psnId  and gr.status = 9  )  "
        + " and   not exists ( select   gp.grpId from   GrpProposer  gp  where gb.grpId =  gp.grpId and  gp.psnId =:psnId and gp.isAccept =2  and gp.type = 1 )");

    // 判断检索条件 如果有检索条件就加检索条件 不加 成员数 成果数过
    String searchKey = null;
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      searchKey = form.getSearchKey().replaceAll("\'", "&#39;");
      searchKey = searchKey.toUpperCase().trim();
      sb.append("  and instr(upper(gb.grpName),:searchKey)>0  ");
    } else {
      // 成员数大于等于2
      sb.append(
          "  and exists  ( select gs.grpId  from   GrpStatistics  gs where   (gs.sumMember >=2 or gs.sumPubs>0) and gs.grpId = gb.grpId  ) ");
    }

    // 群组类别
    if (form.getGrpCategory() != null) {
      sb.append(" and gb.grpCategory =:grpCategory  ");
    }
    // 研究领域学科代码
    if (form.getDisciplineCategory() != null) {
      sb.append(
          "and exists ( select  gk.grpId from GrpKwDisc  gk where gk.grpId = gb.grpId and gk.firstCategoryId =:firstCategoryId ) ");
    }
    sb.append("  and gb.status = '01' and  ( gb.openType = 'O' or gb.openType = 'H' )  ");

    Query queryCount = this.createQuery(countHql + sb.toString()).setParameter("psnId", form.getPsnId());
    Query query = this.createQuery(hql + sb.toString()).setParameter("psnId", form.getPsnId());
    // 检索信息
    if (StringUtils.isNotBlank(searchKey)) {
      queryCount.setParameter("searchKey", searchKey);
      query.setParameter("searchKey", searchKey);
    }
    // 群组类别
    if (form.getGrpCategory() != null) {
      queryCount.setParameter("grpCategory", form.getGrpCategory());
      query.setParameter("grpCategory", form.getGrpCategory());
    }
    // 学科代码
    if (form.getDisciplineCategory() != null) {
      queryCount.setParameter("firstCategoryId", form.getDisciplineCategory());
      query.setParameter("firstCategoryId", form.getDisciplineCategory());
    }
    Object countObj = queryCount.uniqueResult();
    Integer count = 0;
    if (count != null) {
      count = NumberUtils.toInt(countObj.toString());
    }
    form.getPage().setTotalCount(count);
    List<Object> grpList =
        query.setFirstResult(form.getPage().getFirst() - 1).setMaxResults(form.getPage().getPageSize()).list();
    form.getPage().setResult(grpList);

  }

  @SuppressWarnings("unchecked")
  public List<Object[]> getGrpRcmdBaseCategoryStatistic(GrpBaseForm form) {
    StringBuffer sb = new StringBuffer();
    sb.append("  select   gb.grpCategory ,  count(1)   from GrpBaseinfo  gb  where  not exists "
        + "( select  gm.grpId  from GrpMember gm where gm.grpId = gb.grpId  and gm.psnId =:psnId and gm.status = 01 )  "
        + " and  not exists ( select  gr.grpId from   GrpRcmd  gr where gr.grpId = gb.grpId  and gr.psnId =:psnId  and gr.status = 9  )  "
        + " and   not exists (select   gp.grpId from   GrpProposer  gp  where gb.grpId =  gp.grpId and  gp.psnId =:psnId and gp.isAccept =2 and gp.type = 1 )");

    // 判断检索条件 如果有检索条件就加检索条件 不加 成员数 成果数过
    String searchKey = null;
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      searchKey = form.getSearchKey().replaceAll("\'", "&#39;");
      searchKey = searchKey.toUpperCase().trim();
      sb.append("  and instr(upper(gb.grpName),:searchKey)>0  ");
    } else {
      // 成员数大于等于2 ，成果数大于等于5
      sb.append(
          "  and exists  ( select gs.grpId  from   GrpStatistics  gs where   (gs.sumMember >=2 or gs.sumPubs>0)  and gs.grpId = gb.grpId  ) ");
    }
    // 研究领域学科代码
    if (form.getDisciplineCategory() != null) {
      sb.append(
          "and exists ( select  gk.grpId from GrpKwDisc  gk where gk.grpId = gb.grpId and gk.firstCategoryId =:firstCategoryId  )");
    }

    sb.append("  and gb.status = '01' and  ( gb.openType = 'O' or gb.openType = 'H' )  ");

    sb.append(" group by  gb.grpCategory   ");

    Query query = this.createQuery(sb.toString()).setParameter("psnId", form.getPsnId());
    // 检索信息
    if (StringUtils.isNotBlank(searchKey)) {
      query.setParameter("searchKey", searchKey);
    }
    // 学科代码
    if (form.getDisciplineCategory() != null) {
      query.setParameter("firstCategoryId", form.getDisciplineCategory());
    }

    List<Object[]> list = query.list();
    if (list != null) {
      return list;
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Object[]> getGrpRcmdBaseDisciplineStatistic(GrpBaseForm form) {
    StringBuffer sb = new StringBuffer();
    sb.append(" select  gk.firstCategoryId , count (1) from GrpKwDisc  gk  " + " where exists (  "
        + " select   gb.grpId   from GrpBaseinfo  gb  where  not exists ( select  gm.grpId  from GrpMember gm where gm.grpId = gb.grpId  and gm.psnId =:psnId and gm.status = 01 ) "
        + " and  not exists ( select  gr.grpId from  GrpRcmd  gr where gr.grpId = gb.grpId  and gr.psnId =:psnId  and gr.status = 9  )  "
        + " and  not exists (select   gp.grpId from   GrpProposer  gp  where gb.grpId =  gp.grpId and  gp.psnId =:psnId and gp.isAccept =2 and gp.type = 1 )  ");

    // 判断检索条件 如果有检索条件就加检索条件 不加 成员数 成果数过
    String searchKey = null;
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      searchKey = form.getSearchKey().replaceAll("\'", "&#39;");
      searchKey = searchKey.toUpperCase().trim();
      sb.append("  and instr(upper(gb.grpName),:searchKey)>0  ");
    } else {
      // 成员数大于等于2 ，成果数大于等于5
      sb.append(
          "  and exists  ( select gs.grpId  from   GrpStatistics  gs where   (gs.sumMember >=2 or gs.sumPubs>0)   and gs.grpId = gb.grpId  ) ");
    }

    // 群组类别

    if (form.getGrpCategory() != null) {
      sb.append(" and gb.grpCategory =:grpCategory  ");
    }
    sb.append("  and gb.status = '01' and  ( gb.openType = 'O' or gb.openType = 'H' )  ");
    sb.append(" and gb.grpId = gk.grpId  ) ");
    sb.append(" group by gk.firstCategoryId ");

    Query query = this.createQuery(sb.toString()).setParameter("psnId", form.getPsnId());
    // 检索信息
    if (StringUtils.isNotBlank(searchKey)) {
      query.setParameter("searchKey", searchKey);
    }
    // 群组类别
    if (form.getGrpCategory() != null) {
      query.setParameter("grpCategory", form.getGrpCategory());
    }

    List<Object[]> list = query.list();
    if (list != null) {
      return list;
    }
    return null;
  }

  public GrpRcmd getGrpRcmd(Long psnId, Long grpId) {
    String hql = "from GrpRcmd  where psnId = :psnId and grpId = :grpId";
    return (GrpRcmd) super.createQuery(hql).setParameter("psnId", psnId).setParameter("grpId", grpId).uniqueResult();
  }

}
