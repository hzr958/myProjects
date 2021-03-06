package com.smate.core.base.project.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.core.base.project.model.PrjComment;
import com.smate.core.base.project.model.PrjErrorFields;
import com.smate.core.base.project.model.PrjMember;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 项目DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class ProjectDao extends SnsHibernateDao<Project, Long> {

  public Long findPsnId(Long prjId) {
    String hql = "select t.psnId from Project t where t.id=:prjId ";
    return (Long) this.createQuery(hql).setParameter("prjId", prjId).uniqueResult();
  }

  /**
   * 获取项目/人员关系信息.
   * 
   * @param id
   * @return @
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<PrjMember> getPrjMembersByPrjId(Long prjId) {
    Query query = super.createQuery("from PrjMember t where t.prjId= ? order by seqNo", prjId);
    List list = query.list();
    return list;
  }

  /**
   * 获取项目/人员关系信息.
   * 
   * @param id
   * @return @
   */
  public PrjMember getPrjMemberById(Long id) {

    return super.findUnique("from PrjMember t where t.id= ? ", id);
  }

  /**
   * 获取项目/人员关系信息.
   * 
   * @param prjId
   * @param pmId
   * @return @
   */
  public PrjMember getPrjMemberByPrjPmId(Long prjId, Long pmId) {

    return (PrjMember) super.findUnique("from PrjMember t where t.prjId = ? and t.id= ? ", prjId, pmId);
  }

  /**
   * 删除项目/人员关系信息.
   * 
   * @param id @
   */
  public PrjMember removePrjMemberById(Long id) {

    PrjMember prjMember = this.getPrjMemberById(id);
    super.getSession().delete(prjMember);
    return prjMember;
  }

  /**
   * 保存项目/人员关系信息.
   * 
   * @param prjMember @
   */
  public void savePrjMember(PrjMember prjMember) {

    super.getSession().save(prjMember);
  }

  /**
   * 保存项目的错误信息.
   * 
   * @param error @
   */
  public void savePrjErrorFields(PrjErrorFields error) {

    super.getSession().save(error);
  }

  /**
   * 删除项目的错误信息.
   * 
   * @param pubId @
   */
  public void removePrjErrorFieldsByPrjId(Long prjId) {

    super.createQuery("delete from PrjErrorFields t where t.prjId = ? ", prjId).executeUpdate();

  }

  /**
   * 通过项目ID(，逗号分隔)获取项目列表.
   * 
   * @param prjIds
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<Project> getProjectByPrjIds(String prjIds) {

    if (prjIds != null && prjIds.matches(ServiceConstants.IDPATTERN))
      return super.createQuery("from Project t where t.id in (" + prjIds + ")").list();
    return null;
  }

  /**
   * 获取项目总数.
   * 
   * @param psnId
   * @return
   */
  public Integer getSumProject(Long psnId) {
    String hql = "select count(t.id) from Project t where t.psnId = ? and t.status = 0 ";
    Long count = super.findUnique(hql, psnId);
    return count.intValue();
  }

  /**
   * 批量获取Project表数据，数据同步后重构XML用.
   * 
   * @param lastId
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Project> getPrjByBatchForOld(Long lastId, int batchSize) {

    String sql = "from Project p where p.id > ? order by id asc";

    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  /**
   * 获取具体年份项目统计数据.
   * 
   * @param year
   * @param psnId
   * @return
   */
  public int getPrjYearNum(Integer year, Long psnId) {
    String hql = "select count(id) from Project t where t.status = 0 and t.fundingYear = ?  and t.psnId = ? ";
    Long count = super.findUnique(hql, year, psnId);
    return count.intValue();
  }

  /**
   * 获取未归类年份项目统计数据.
   * 
   * @param year
   * @param psnId
   * @return
   */
  public int getNoPrjYearNum(Long psnId) {
    String hql = "select count(id) from Project t where t.status = 0 and t.fundingYear is null and t.psnId = ? ";
    Long count = super.findUnique(hql, psnId);
    return count.intValue();
  }

  /**
   * 获取加入群组的项目统计数.
   * 
   * @param groupId
   * @param psnId
   * @return
   */
  public int getGroupPrjNum(Long groupId, Long psnId) {
    String hql = "select count(id) from Project t where t.status = 0  and t.psnId = ? and t.id in("
        + "select prjId from GroupPrjNode where groupId = ? and psnId = ? )";
    Long count = super.findUnique(hql, psnId, groupId, psnId);
    return count.intValue();
  }

  /**
   * 个人项目的总数.
   * 
   * @param psnId @
   */
  public Long getPsnMergeTotalPrjs(Long psnId) {
    Long count = super.findUnique("select count(t.id) from Project t where t.psnId=? and t.status in(0,5)",
        new Object[] {psnId});
    return count;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPrjByBatchAmountUnitDbId13(Long lastId, int batchSize) {
    String hql = "select id from Project where id > ? and dbId=13 and amountUnit is not null order by id asc";
    return super.createQuery(hql, lastId).setMaxResults(batchSize).list();
  }

  /**
   * 批量获取数据库表中的数据重构XML用.
   * 
   * @param lastId
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Project> loadRebuildPrjId(Long lastId, int batchSize) {

    String hql = "select new Project(id) from Project where id > ? order by id asc";
    return super.createQuery(hql, lastId).setMaxResults(batchSize).list();
  }

  /**
   * 删除评论和分数时更新评分总数和人数.
   * 
   * @param id
   * @param score
   */
  public void updateScore(Long id, Integer score) {
    Project pro = this.get(id);
    if (score != null && score > 0) {
      pro.setPrjStart(pro.getPrjStart() - score); // 减去分数
      pro.setPrjStartPsns(pro.getPrjStartPsns() - 1); // 评分总人数 -1
      pro.setPrjReviews(pro.getPrjReviews() - 1); // 评价总人数 -1
    } else {
      pro.setPrjReviews(pro.getPrjReviews() - 1); // 评价总人数 -1
    }
    this.save(pro);
  }

  /**
   * 批量获取项目ID.
   * 
   * @param lastId
   * @param batchSize
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPrjIdsBatch(Long lastId, int batchSize) {
    String hql = "select t.id from Project t where t.id > ? order by t.id asc";
    return super.createQuery(hql, lastId).setMaxResults(batchSize).list();
  }

  public Project getPrjOwnerPsnIdOrStatus(Long prjId) {
    String hql = "select new Project(id,psnId,status) from Project where id=?";
    return findUnique(hql, prjId);
  }

  public Long getPrjOwner(Long prjId) {
    String hql = "select psnId from Project where id=?";
    return findUnique(hql, prjId);
  }

  /**
   * 
   * @author liangguokeng
   */
  @SuppressWarnings("unchecked")
  public List<Project> findPrjIdsByPsnId(Long psnId) {
    String hql = "from Project t where psnId=? and t.status = 0 ";
    return super.createQuery(hql, psnId).list();
  }

  @SuppressWarnings({"rawtypes"})
  public List findRebuildPrjId(int size) {
    String sql = "select t.prj_id from task_prj_author_ids t where t.status=? and rownum<=?";
    return super.queryForList(sql, new Object[] {1, size});
  }

  public void updatePrjAuthorNames(Long prjId) {
    String sql =
        "update project t set t.author_names=replace(t.author_names,',',';'),t.author_names_en=replace(t.author_names_en,',',';') where t.project_id=?";
    super.update(sql, new Object[] {prjId});
  }

  public void updateTaskPrjAuthor(Long prjId) {
    String sql = "update task_prj_author_ids set status=? where prj_id=?";
    super.update(sql, new Object[] {0, prjId});
  }

  public Long queryPrjCountByPsnIdAndTitle(Long psnId, Integer zhTitleHash, Integer enTitleHash) {
    String hql = "select count(t.id) from Project t where";
    List<Object> params = new ArrayList<Object>();
    if (zhTitleHash != null || enTitleHash != null) {
      if (zhTitleHash != null && enTitleHash != null) {
        hql = hql + " (lower(t.zhTitleHash)=? or lower(t.enTitleHash)=?)";
        params.add(zhTitleHash);
        params.add(enTitleHash);
      } else if (zhTitleHash != null) {
        hql = hql + " lower(t.zhTitleHash)=?";
        params.add(zhTitleHash);
      } else {
        hql = hql + " lower(t.enTitleHash)=?";
        params.add(enTitleHash);
      }
    }

    hql = hql + " and t.psnId=?";
    params.add(psnId);
    Long count = (Long) super.createQuery(hql, params.toArray()).uniqueResult();

    return count;
  }

  @SuppressWarnings("unchecked")
  public List<PrjComment> queryPrjCommentByPsnId(Long psnid) {
    return super.createQuery("from PrjComment t where t.psnId=?", psnid).list();
  }

  /**
   * 获取来源于群组的项目.
   * 
   * @param prjId
   * @return
   */
  public Project getPrjFromGroup(Long prjId) {
    String hql = "from Project t where t.id=? and t.status=5";
    return findUnique(hql, prjId);
  }

  public Long getOpenPrjSum(Long psnId, Integer anyUser) {

    String hql = "select count(p.id) from Project p where p.psnId =:psnId and p.status = 0 "
        + "and exists ( select 1 from PsnConfigPrj pcp where pcp.id.cnfId ="
        + " (select t.cnfId from PsnConfig t where t.psnId=:psnId) and pcp.anyUser =:anyUser and pcp.id.prjId = p.id)";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("anyUser", anyUser).uniqueResult();
  }

  public Long getPsnPrjCounts(Long psnId) {
    String hql = "select count(1) from Project p where p.psnId =:psnId and p.status = 0";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param internalNo 本机构批准号
   * @param externalNo 资助机构批准号
   * @param agencyName 资助机构名称
   * @param title 项目名称
   * @return true 重复
   */
  public boolean queryRepeat(String internalNo, String externalNo, String agencyName, String enAgencyName,
      String zhTitle, String enTitle, Long psnId) {
    String hql1 = " from Project where internalNo=? and psnId=? and status=0 ";
    String hql2 = " from Project where replace(lower(externalNo),' ','')=lower(?) and psnId=? and status=0 ";
    String hql3 = " from Project where 1=1 ";
    List<Object> params = new ArrayList<Object>();
    if (agencyName != null && !"".equals(agencyName.trim())) {
      hql3 += " and lower(nvl(agencyName,' '))=lower(?) ";
      params.add(agencyName);
    } else {
      hql3 += " and lower(nvl(agencyName,' '))=lower(?) ";
      params.add(" ");
    }
    if (zhTitle != null && !"".equals(zhTitle.trim())) {
      hql3 += " and lower(nvl(zhTitle,' '))=lower(?) ";
      params.add(zhTitle);
    } else {
      hql3 += " and lower(nvl(zhTitle,' '))=lower(?) ";
      params.add(" ");
    }
    if (enTitle != null && !"".equals(enTitle.trim())) {
      hql3 += " and lower(nvl(enTitle,' '))=lower(?) ";
      params.add(enTitle);
    } else {
      hql3 += " and lower(nvl(enTitle,' '))=lower(?) ";
      params.add(" ");
    }
    hql3 += " and psnId=? and status=0 ";
    params.add(psnId);
    List list1 = super.createQuery(hql1, internalNo, psnId).list();
    if (list1 != null && list1.size() > 0) {
      return true;
    }
    List list2 = super.createQuery(hql2, externalNo, psnId).list();
    if (list2 != null && list2.size() > 0) {
      return true;
    }
    List list3 = super.createQuery(hql3, params.toArray()).list();
    if (list3 != null && list3.size() > 0) {
      return true;
    }
    return false;
  }

  public Project findPrjById(Long prjId) {
    String hql = "from Project t where t.id=?";
    return super.findUnique(hql, prjId);
  }

  public Long createPrjId() {
    String sql = "select SEQ_PROJECT.nextval from dual";
    SQLQuery query = this.getSession().createSQLQuery(sql);
    Object obj = query.uniqueResult();
    if (obj != null) {
      return NumberUtils.toLong(obj.toString());
    }
    return 0L;
  }

}
