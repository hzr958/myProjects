package com.smate.web.psn.dao.keywork;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.profile.KeywordIdentification;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.model.friend.PsnListViewForm;

/**
 * 研究领域关键词赞（认同）人员Dao
 *
 * @author wsn
 *
 */
@Repository
public class IdentificationDao extends SnsHibernateDao<KeywordIdentification, Long> {

  /**
   * 根据人员ID，研究领域关键词ID查找赞人员ID列表
   * 
   * @param psnId
   * @param discId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findKwIdentificFriendList(Long psnId, Long discId) {
    String sql = "select friendId from KeywordIdentification where psnId=? and keywordId=?";
    return super.createQuery(sql, psnId, discId).list();
  }

  /**
   * 分页查询科技领域认同的人员ID
   * 
   * @param psnId
   * @param scienceAreaId
   * @return
   */
  public PsnListViewForm findIdentifyPsnIdsByForm(PsnListViewForm form) {
    if (form.getDiscId() != null && form.getDiscId() != 0) {
      Long kwId = form.getDiscId();
      String orderByHql = " order by t.opDate desc ";
      String countHql = "select count(t.friendId) ";
      String queryHql = "select t.friendId ";
      StringBuilder hql = new StringBuilder();
      List<Object> params = new ArrayList<Object>();
      hql.append(" from KeywordIdentification t where t.psnId = ? and t.keywordId = ?");
      params.add(form.getPsnId());
      params.add(kwId);
      Long totalCount = super.findUnique(countHql + hql, params.toArray());
      Page page = form.getPage();
      page.setTotalCount(totalCount);
      page.setTotalPages(page.getTotalPages());
      List<Long> psnIds = super.createQuery(queryHql + hql + orderByHql, params.toArray())
          .setFirstResult((page.getParamPageNo() - 1) * page.getPageSize()).setMaxResults(page.getPageSize()).list();
      form.setPsnIds(psnIds);
    }
    return form;
  }

  /**
   * 人员关键词按认同数排序
   * 
   * @param kwIdList
   * @return
   */
  public List<Long> sortKwIdentification(String kwIds, Long psnId) {
    String hql = "SELECT t.keywordId FROM KeywordIdentification t WHERE  t.keywordId in (" + kwIds
        + ") and t.psnId=:psnId GROUP BY t.keywordId order by count(t.friendId) desc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 根据人员ID，研究领域关键词ID查找赞人员ID列表 只要3个，用来显示人员头像在关键词后面
   * 
   * @param psnId
   * @param discId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findSomeKwIdentificIds(Long psnId, Long discId) {
    String sql =
        "select friendId from KeywordIdentification t where t.psnId=:psnId and t.keywordId=:keywordId order by opDate desc";
    return super.createQuery(sql).setParameter("psnId", psnId).setParameter("keywordId", discId).setMaxResults(3)
        .list();
  }

}
