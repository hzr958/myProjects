package com.smate.web.prj.dao.wechat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.prj.form.wechat.PrjWeChatForm;
import com.smate.web.prj.model.common.PrjFolder;

/**
 * 
 * 
 * @author tj
 * 
 */
@Repository
public class WechatProjectDao extends SnsHibernateDao<Project, Long> {

  // 查询项目标签列表
  public void queryPrjFoldersWc(PrjWeChatForm form) {
    String hql = "select new PrjFolder(id,name) from PrjFolder where psnId=? order by id";
    List<PrjFolder> folderList = super.createQuery(hql, form.getPsnId()).setMaxResults(8).list();
    Map<String, Object> result = form.getPrjTopMap();
    if (result == null) {
      result = new HashMap<String, Object>();
    }
    result.put("tag", folderList);
    form.setPrjTopMap(result);

  }

  // 查询状态列表
  public void queryPrjStatusForWc(PrjWeChatForm form) {

    String hql = "select new Project(state,id) from Project where psnId=?";
    List<Project> stateList = super.createQuery(hql, form.getPsnId()).list();
    Map<String, Object> result = form.getPrjTopMap();
    if (result == null) {
      result = new HashMap<String, Object>();
    }
    result.put("state", stateList);
    form.setPrjTopMap(result);

  }

  // 查找项目所属人员ID
  public Long findPrjOwnerPsnId(Long prjId) {
    String hql = "select t.psnId from Project t where t.id = :prjId";
    return (Long) super.createQuery(hql).setParameter("prjId", prjId).uniqueResult();
  }

  /// ---------------------------------------------------------------------------------------------------
  @SuppressWarnings("unchecked")
  public void queryPrjForWeChat(PrjWeChatForm form) {
    List<Object> params = new ArrayList<Object>();
    String hql =
        "select new Project(p.id, p.zhTitle, p.enTitle, p.authorNames,p.authorNamesEn, p.briefDesc, p.briefDescEn) from Project p  ";
    String countHql = "select count(1) from Project p ";
    StringBuilder conditions = new StringBuilder();
    conditions.append(" where p.psnId = ? and p.status=0 ");
    params.add(form.getPsnId());
    if (StringUtils.isNoneBlank(form.getSearchKey())) {
      conditions.append(" and (upper(p.zhTitle) like ? or upper(p.enTitle) like ?) ");
      params.add("%" + form.getSearchKey().toUpperCase() + "%");
      params.add("%" + form.getSearchKey().toUpperCase() + "%");
    }
    Long prjAllCount = (Long) super.createQuery(countHql + conditions, params.toArray()).uniqueResult();
    if (!form.getIsMyPrj() && form.getCnfId() != null) {
      conditions.append(
          "  and exists (select 1 from PsnConfigPrj pcp where pcp.id.prjId = p.id and pcp.anyUser = 7 and pcp.id.cnfId = ?)");
      params.add(form.getCnfId());
      Long prjOpenCount = (Long) super.createQuery(countHql + conditions, params.toArray()).uniqueResult();
      if (!prjAllCount.equals(prjOpenCount)) {
        form.setHasPrivatePrj(true);
      }
    }
    String orderHql = "";
    String orderRule = form.getOrderRule();
    // 排序
    if ("1".equals(form.getOrderType())) {
      /*
       * if ("asc".equals(orderRule)) { orderHql += "  order by  nvl(p.amount," + Float.MAX_VALUE + ") " +
       * orderRule + " ,enTitle " + orderRule + " ,zhTitle " + orderRule; } else {
       */
      orderHql += "  order by  nvl(p.amount,-1) desc ,enTitle asc ,zhTitle asc";
      /* } */

    } else if ("0".equals(form.getOrderType())) {
      orderHql += "  order by  nvl(zhTitle ,enTitle) " + orderRule;

    }

    // 过虑条件
    // 状态
    List<String> temp = null;
    if (form.getSelectStatus() != null && form.getSelectStatus().length() > 0) {
      temp = Arrays.asList(form.getSelectStatus().split(","));
      conditions.append(" and state in (:alist) ");
    }
    // 过虑条件
    // 状态 资助机构 类别
    List<Long> temp1 = new ArrayList<Long>();
    if (form.getSelectFundingAgencie() != null && form.getSelectFundingAgencie().length() > 0) {
      String[] str = form.getSelectFundingAgencie().split(",");
      for (String s : str) {
        if (StringUtils.isNumeric(s)) {
          temp1.add(Long.valueOf(s));
        }
      }
      if (form.getSelectFundingAgencie().indexOf("160") > -1) {
        conditions.append(" and (");
        conditions.append(" agencyName  IN (select nameZh from ConstFundAgency where type in (:blist)) ");
        conditions.append(" or enAgencyName  IN (select nameEn from ConstFundAgency where type in (:blist))");
        conditions.append(" or (agencyName is null and enAgencyName is null) ");
        conditions.append(
            " or ( not exists (select id from ConstFundAgency t1 where p.agencyName=t1.nameZh) and not exists (select id from ConstFundAgency t1 where p.enAgencyName=t1.nameEn))");
        conditions.append(
            " or agencyName  IN (select nameZh from ConstFundAgency where type not in ('120','130','140','160')) ");
        conditions.append(
            " or enAgencyName  IN (select nameEn from ConstFundAgency where type not in ('120','130','140','160'))");
        conditions.append(" ) ");
      } else {
        conditions.append(
            " and (agencyName  IN (select nameZh from ConstFundAgency where type in (:blist)) or enAgencyName  IN (select nameEn from ConstFundAgency where type in (:blist))) ");
      }
    }

    Query query = null;
    Query totalCountQuery = null;

    Page page = form.getPage();
    // 获取总记录数
    totalCountQuery = super.createQuery(countHql + conditions + orderHql, params.toArray());
    if (temp != null && temp.size() > 0) {
      totalCountQuery.setParameterList("alist", temp);
    }
    if (temp1 != null && temp1.size() > 0) {
      totalCountQuery.setParameterList("blist", temp1);
    }
    Long prjCount = NumberUtils.toLong(Objects.toString(totalCountQuery.uniqueResult()), 0L);
    page.setTotalCount(prjCount == null ? 0 : prjCount);
    Integer pageNo = page.getPageNo();
    Integer pageSize = page.getPageSize();
    query = super.createQuery(hql + conditions + orderHql, params.toArray()).setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize);
    if (temp != null && temp.size() > 0) {
      query.setParameterList("alist", temp);
    }
    if (temp1 != null && temp1.size() > 0) {
      query.setParameterList("blist", temp1);
    }
    form.setTotalPrjCount(prjCount == null ? 0 : prjCount);

    form.setPrjList(query.list());
  }

  /**
   * 查询隐私项目数量
   * 
   * @param searchPsnId
   * @return
   */
  public Long queryPrivatePrjCount(Long searchPsnId) {
    String HQL = "select count(1) from Project p1 where exists "
        + "(select 1 from PsnConfigPrj p2,PsnConfig p3 where p1.id = p2.id.prjId "
        + "and p2.id.cnfId = p3.cnfId and p2.anyUser < 7 and p3.psnId = ?) and p1.status = 0";
    return (Long) getSession().createQuery(HQL).setParameter(0, searchPsnId).uniqueResult();
  }

  /// ---------------------------------------------------------------------------------------------------

}
