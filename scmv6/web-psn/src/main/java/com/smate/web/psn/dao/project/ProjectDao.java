package com.smate.web.psn.dao.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.form.resume.PersonalResumeForm;
import com.smate.web.psn.model.homepage.PersonProfileForm;

/**
 * 人员项目DAO
 *
 * @author wsn
 * @createTime 2017年3月19日 下午12:38:57
 *
 */
@Repository
public class ProjectDao extends SnsHibernateDao<Project, Long> {

  /**
   * 获取pubsimple的list
   * 
   * @param pubIds
   * @return
   * @throws DaoException
   */
  public List<Project> getProjectListByPrjIds(String prjIds) throws DaoException {
    String hql = " from Project t where t.id in (" + prjIds + ") and t.status=0";
    return super.createQuery(hql).list();
  }

  /**
   * 查找人员公开项目
   * 
   * @return
   */
  public List<Project> findPsnOpenPrjList(Long psnId, Long cnfId, Integer anyUser, Page page, String queryString) {
    StringBuilder hql = new StringBuilder();
    String orderBy = " order by p.id desc,p.fundingYear desc";
    List<Object> params = new ArrayList<Object>();
    hql.append(
        " from Project p where p.psnId = ? and p.status = 0 and exists ( select 1 from PsnConfigPrj pcp where pcp.id.cnfId = ? and pcp.anyUser >= ? and pcp.id.prjId = p.id) ");
    params.add(psnId);
    params.add(cnfId);
    params.add(anyUser);
    String searchKey = "";
    if (StringUtils.isNotBlank(queryString)) {
      searchKey = HtmlUtils.htmlEscape(queryString).replaceAll("\'", "&#39;");
      searchKey = searchKey.toUpperCase().trim();
      hql.append(" and ( instr(upper(p.zhTitle), ?)>0");// 中文标题
      params.add(searchKey);
      hql.append(" or instr(upper(p.enTitle), ?)>0");// 英文标题
      params.add(searchKey);
      hql.append(" or instr(upper(p.authorNames), ?)>0 )");// 作者
      params.add(searchKey);
    }
    return super.createQuery(hql.toString() + orderBy, params.toArray())
        .setFirstResult((page.getParamPageNo() - 1) * page.getPageSize()).setMaxResults(page.getPageSize()).list();
  }

  /**
   * 分页查找人员代表性项目
   * 
   * @param form
   * @return
   */
  public PersonProfileForm queryPsnOpenPrjList(PersonProfileForm form) {
    String listHql = "select p ";
    String countHql = " select count(p.id) ";
    String orderBy = " order by  p.fundingYear desc nulls last ,p.id desc";
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    hql.append(
        " from Project p where p.psnId = ? and p.status = 0 and exists ( select 1 from PsnConfigPrj pcp where pcp.id.cnfId = ? and pcp.anyUser > ? and pcp.id.prjId = p.id) ");
    params.add(form.getPsnId());
    params.add(form.getCnfId());
    params.add(form.getAnyUser());
    String searchKey = "";
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      searchKey = HtmlUtils.htmlEscape(form.getSearchKey()).replaceAll("\'", "&#39;");
      searchKey = searchKey.toUpperCase().trim();
      hql.append(" and ( instr(upper(p.zhTitle), ?)>0");// 中文标题
      params.add(searchKey);
      hql.append(" or instr(upper(p.enTitle), ?)>0");// 英文标题
      params.add(searchKey);
      hql.append(" or instr(upper(p.authorNames), ?)>0 )");// 作者
      params.add(searchKey);
    }
    Page page = form.getPage();
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);
    // 查询数据实体
    List<Project> resultList = super.createQuery(listHql + hql + orderBy, params.toArray())
        .setFirstResult((page.getParamPageNo() - 1) * page.getPageSize()).setMaxResults(page.getPageSize()).list();
    page.setResult(resultList);
    form.setOpenPrjList(resultList);
    return form;
  }

  /**
   * 查找项目全文信息
   * 
   * @param prjId
   * @param psnId
   * @param status
   * @return
   */
  public Project getProjectFullTextId(Long prjId, Long psnId, Integer status) {
    String hql =
        " select new Project(id, psnId, status, fulltextField) from Project t where t.id = :prjId and t.psnId = :psnId and t.status = 0";
    return (Project) super.createQuery(hql).setParameter("prjId", prjId).setParameter("psnId", psnId)
        .setParameter("status", status).uniqueResult();

  }

  /**
   * 查找是否是项目拥有者
   * 
   * @param psnId
   * @param prjId
   * @return
   */
  public boolean isOwnerOfProject(Long psnId, Long prjId) {
    String hql = "select t.id from Project t where t.id = :prjId and t.psnId = :psnId";
    Long id = (Long) super.createQuery(hql).setParameter("prjId", prjId).setParameter("psnId", psnId).uniqueResult();
    if (id == null) {
      return false;
    } else {
      return true;
    }
  }

  @SuppressWarnings("unchecked")
  public List<Project> findPrjIdsByPsnId(Long psnId) {
    String hql = "from Project t where psnId=? and t.status = 0 ";
    return super.createQuery(hql, psnId).list();
  }

  /**
   * 获取项目总数.
   * 
   * @param psnId
   * @return
   */
  public Integer getSumProject(Long psnId) {
    String hql = "select count(t.id) from Project t where t.psnId = :psnId and t.status = 0 ";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    return count.intValue();
  }

  @SuppressWarnings("unchecked")
  public List<Project> getPrjs(Long psnId) {
    String hql = "from Project t where psnId=:psnId and t.status = 0 order by t.fundingYear desc nulls last ,t.id desc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void queryPrjList(PersonalResumeForm form) {
    String listHql = "select t ";
    String countHql = " select count(t.id) ";
    String orderHql = " order by  t.fundingYear desc nulls last ,t.id desc";
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    hql.append(
        " from Project t where t.psnId = ? and t.status = 0  and not exists(select 1 from PsnConfig b,PsnConfigPrj p where  p.id.prjId =t.id and b.cnfId=p.id.cnfId and p.anyUser <=6)");
    params.add(form.getPsnId());
    String searchKey = form.getSearchKey();
    if (StringUtils.isNotBlank(searchKey)) {
      searchKey = StringEscapeUtils.unescapeHtml4(searchKey);
      searchKey = searchKey.toUpperCase().trim();
      hql.append(" and ( instr(upper(t.zhTitle), ?)>0");// 中文标题
      params.add(searchKey);
      hql.append(" or instr(upper(t.enTitle), ?)>0");// 英文标题
      params.add(searchKey);
      hql.append(" or instr(upper(t.authorNames), ?)>0 )");// 作者
      params.add(searchKey);
    }
    Page page = form.getPage();
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);
    // 查询数据实体
    List<Project> resultList = super.createQuery(listHql + hql + orderHql, params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(resultList);
  }

  /**
   * 是否有成果配置信息丢失
   * 
   * @param psnId
   * @param cnfId
   * @return
   */
  public boolean hasPsnConfigPrjLost(Long psnId, Long cnfId) {
    String hql =
        "select count(1) from Project t where t.status=0 and t.psnId = :psnId and not exists(select 1 from PsnConfigPrj p where p.id.cnfId = :cnfId and p.id.prjId = t.id)";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("cnfId", cnfId).uniqueResult();
    if (count != null && count > 0) {
      return true;
    }
    return false;
  }

}
