package com.smate.web.psn.dao.keywork;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.keyword.ScienceAreaIdentification;

@Repository
public class ScienceAreaIdentificationDao extends SnsHibernateDao<ScienceAreaIdentification, Long> {

  /**
   * 查找人员认同科技领域记录
   * 
   * @param psnId
   * @param scienceAreaId
   * @param friendId
   * @return
   */
  public ScienceAreaIdentification findScienceAreaIdentificationRecord(Long psnId, Integer scienceAreaId,
      Long friendId) {
    String hql =
        "from ScienceAreaIdentification t where t.psnId = :psnId and t.scienceAreaId = :scienceAreaId and t.operatePsnId =:friendId";
    return (ScienceAreaIdentification) super.createQuery(hql).setParameter("psnId", psnId)
        .setParameter("scienceAreaId", scienceAreaId).setParameter("friendId", friendId).uniqueResult();
  }

  /**
   * 查询科技领域认同的所有人员ID
   * 
   * @param psnId
   * @param scienceAreaId
   * @return
   */
  public List<Long> findIdentifyPsnIds(Long psnId, Integer scienceAreaId) {
    String hql =
        "select t.operatePsnId from ScienceAreaIdentification t where t.psnId = :psnId and t.scienceAreaId = :scienceAreaId";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("scienceAreaId", scienceAreaId).list();
  }

  /**
   * 分页查询科技领域认同的人员ID
   * 
   * @param psnId
   * @param scienceAreaId
   * @return
   */
  public PsnListViewForm findIdentifyPsnIdsByForm(PsnListViewForm form) {
    if (StringUtils.isNotBlank(form.getScienceAreaId())) {
      Integer areaId = NumberUtils.toInt(form.getScienceAreaId());
      String orderByHql = " order by t.operateDate desc ";
      String countHql = "select count(t.operatePsnId) ";
      String queryHql = "select t.operatePsnId ";
      StringBuilder hql = new StringBuilder();
      List<Object> params = new ArrayList<Object>();
      hql.append(" from ScienceAreaIdentification t where t.psnId = ? and t.scienceAreaId = ?");
      params.add(form.getPsnId());
      params.add(areaId);
      Long totalCount = super.findUnique(countHql + hql, params.toArray());
      Page page = form.getPage();
      page.setTotalCount(totalCount);
      List<Long> psnIds = super.createQuery(queryHql + hql + orderByHql, params.toArray())
          .setFirstResult((page.getParamPageNo() - 1) * page.getPageSize()).setMaxResults(page.getPageSize()).list();
      form.setPsnIds(psnIds);
    }
    return form;
  }

}
