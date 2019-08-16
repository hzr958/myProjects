package com.smate.web.prj.dao.wechat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.prj.form.wechat.PrjWeChatForm;
import com.smate.web.prj.model.common.PrjFolder;

/**
 * 
 * 
 * @author tj
 * 
 */
@Repository
public class ProjectWeChatDao extends SnsHibernateDao<Project, Long> {

  @SuppressWarnings("unchecked")
  public void queryPrjForWeChat(PrjWeChatForm form) {
    List<Object> params = new ArrayList<Object>();
    String hql =
        "select new Project(p.id, p.zhTitle, p.enTitle, p.authorNames,p.authorNamesEn, p.briefDesc, p.briefDescEn) from Project p where p.psnId = ? ";
    params.add(form.getPsnId());
    if ("true".equals(form.getOther()) && form.getCnfId() != null) {
      hql +=
          " and p.status=0 and exists (select 1 from PsnConfigPrj pcp where pcp.id.prjId = p.id and pcp.anyUser = 7 and pcp.id.cnfId = ?)";
      params.add(form.getCnfId());
    }
    String orderHql = "";
    String orderRule = form.getOrderRule();
    // 排序
    if ("1".equals(form.getOrderType())) {
      if ("asc".equals(orderRule)) {
        orderHql += "  order by  nvl(p.amount," + Float.MAX_VALUE + ") " + orderRule + " ,enTitle " + orderRule
            + " ,zhTitle " + orderRule;
      } else {
        orderHql += "  order by  nvl(p.amount,-1) " + orderRule + " ,enTitle " + orderRule + " ,zhTitle " + orderRule;
      }

    } else if ("0".equals(form.getOrderType())) {
      orderHql += "  order by enTitle " + orderRule + " ,zhTitle " + orderRule;
    }

    // 过虑条件
    // 状态
    List<String> temp = null;
    if (form.getSelectStatus() != null && form.getSelectStatus().length() > 0) {
      temp = Arrays.asList(form.getSelectStatus().split(","));
      hql += " and state in (:alist) ";

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
        hql += " and (";
        hql += " agencyName  IN (select nameZh from ConstFundAgency where type in (:blist)) ";
        hql += " or enAgencyName  IN (select nameEn from ConstFundAgency where type in (:blist))";
        hql += " or (agencyName is null and enAgencyName is null) ";
        hql +=
            " or ( not exists (select id from ConstFundAgency t1 where p.agencyName=t1.nameZh) and not exists (select id from ConstFundAgency t1 where p.enAgencyName=t1.nameEn))";
        hql += " or agencyName  IN (select nameZh from ConstFundAgency where type not in ('120','130','140','160')) ";
        hql += " or enAgencyName  IN (select nameEn from ConstFundAgency where type not in ('120','130','140','160'))";
        hql += " ) ";
      } else {
        hql +=
            " and (agencyName  IN (select nameZh from ConstFundAgency where type in (:blist)) or enAgencyName  IN (select nameEn from ConstFundAgency where type in (:blist))) ";
      }
    }

    Query query = null;
    if (form.getNextId() == null) {
      query = super.createQuery(hql + orderHql, params.toArray()).setFirstResult(0).setMaxResults(form.getSize());

    } else {
      query = super.createQuery(hql + orderHql, params.toArray())
          .setFirstResult(Integer.valueOf(form.getNextId()) * form.getSize()).setMaxResults(form.getSize());
    }
    if (temp != null && temp.size() > 0) {
      query.setParameterList("alist", temp);
    }
    if (temp1 != null && temp1.size() > 0) {
      query.setParameterList("blist", temp1);
    }
    form.setPrjList(query.list());
  }

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

}
