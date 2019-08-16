package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.PubRolSubmission;
import com.smate.center.batch.model.rol.pub.PubRolSubmissionStatusEnum;
import com.smate.center.batch.model.rol.pub.PubSubmissionSearchForm;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 成果提交DAO.
 * 
 * @author yamingd
 * 
 */
@Repository
public class PubRolSubmissionDao extends RolHibernateDao<PubRolSubmission, Long> {

  /**
   * 获取审核成果列表，需要以单位方PubRolSubmission为主体查询，如果成果被合并，则这个主体会关联到合并后保留的成果.
   * 
   * @param insId
   * @param query
   * @param page
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public void searchRolSubmittedPub(long insId, long psnId, PubSubmissionSearchForm query, Page page)
      throws DaoException {
    // 1.提交界面，查询已提交/已批准/申请撤销成果
    List<Object> params = new ArrayList<Object>();

    String listHql =
        "select new PublicationRol(t.id,t.typeId,t.publishYear,t.impactFactors,t.citedTimes,t.citedDate,pubsub.psnId,pubsub.id, pubsub.withdrawReqDate, pubsub.submitStatus)";
    String countHql = "select count(pubsub.id) ";
    String orderHql = "";
    if (StringUtils.isBlank(query.getOrderBy())) {
      orderHql = " order by t.publishYear desc, t.publishMonth desc, t.publishDay desc, pubsub.id ";
    } else {
      orderHql = " order by " + query.getOrderBy();
    }

    StringBuilder hql = new StringBuilder();
    hql.append(" from PubRolSubmission pubsub inner join pubsub.insPub t where pubsub.insId = ? and pubsub.psnId = ?");
    params.add(insId);
    params.add(psnId);

    if (query.getStatus() == 3) {
      // 列出撤销列表（提交申请的/已同意撤销的）
      hql.append(" and pubsub.submitStatus in (3,5) ");
    } else {
      hql.append(" and pubsub.submitStatus = ? ");
      params.add(query.getStatus());
    }
    hql.append(" and t.articleType=?");
    params.add(1);

    if (query.getFromYear() != null && query.getFromYear() > 0) {
      if (query.getToYear() != null && query.getToYear() > 0) {
        int tmp = query.getToYear();
        if (tmp < query.getFromYear()) {
          query.setToYear(query.getFromYear());
          query.setFromYear(tmp);
        }
      }
    }

    if (query.getFromYear() != null && query.getFromYear() > 0) {
      hql.append(" and t.publishYear>=? ");
      params.add(query.getFromYear());
    }
    if (query.getToYear() != null && query.getToYear() > 0) {
      hql.append(" and t.publishYear<=? ");
      params.add(query.getToYear());
    }
    if (query.getTypeId() != null && query.getTypeId() > 0) {
      hql.append(" and t.typeId = ?");
      params.add(query.getTypeId());
    }

    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    Query queryResult = super.createQuery(listHql + hql + orderHql, params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
  }

  /**
   * 创建一个提交记录.
   * 
   * @param psnId
   * @param snsPubId
   * @param insId
   * @return
   * @throws DaoException
   */
  public PubRolSubmission create(Long psnId, Long snsPubId, Long insId, Long submitPsnId, int status)
      throws DaoException {
    PubRolSubmission sub = new PubRolSubmission();
    sub.setInsId(insId);
    sub.setSubmitPubId(snsPubId);
    sub.setPsnId(psnId);
    sub.setSubmitPsnId(submitPsnId);
    sub.setSubmitStatus(status);
    Date date = new Date();
    sub.setSubmitDate(date);
    // 如果状态是已批准，则设置批准时间为当前时间
    if (PubRolSubmissionStatusEnum.APPROVED == status) {
      sub.setInsConfirmDate(date);
    }
    super.save(sub);
    return sub;
  }

  /**
   * 更新一个提交记录.
   * 
   * @param psnId
   * @param snsPubId
   * @param insId
   * @return
   * @throws DaoException
   */
  public PubRolSubmission update(Long psnId, Long snsPubId, Long insId, Long submitPsnId, int status)
      throws DaoException {
    PubRolSubmission sub = this.getSubmissionBySNSPubId(snsPubId, insId);
    if (sub != null) {
      sub.setInsId(insId);
      sub.setSubmitPubId(snsPubId);
      sub.setPsnId(psnId);
      sub.setSubmitPsnId(submitPsnId);
      sub.setSubmitStatus(status);
      sub.setSubmitDate(new Date());
      super.save(sub);
    }
    return sub;
  }

  /**
   * 重新提交(撤销已提交撤销申请操作).
   * 
   * @param submitId
   * @param status
   * @throws DaoException
   */
  public PubRolSubmission reSubmit(Long submitId) throws DaoException {
    PubRolSubmission sub = super.findUnique("from PubRolSubmission t where t.id = ?", submitId);
    if (sub != null) {
      sub.setSubmitStatus(PubRolSubmissionStatusEnum.APPROVED);
      sub.setReSubmitDate(new Date());
      sub.setWithdrawReqConfirmDate(null);
      sub.setWithdrawStatus(null);
      sub.setWithdrawReqDate(null);
    }
    return sub;
  }

  /**
   * 撤销提交.
   * 
   * @param snsPubId
   * @param insId
   * @param status
   * @throws DaoException
   */
  public PubRolSubmission withdraw(Long submitId) throws DaoException {
    PubRolSubmission sub = super.findUnique("from PubRolSubmission t where t.id = ?", submitId);
    if (sub != null) {
      super.delete(sub);
    }
    return sub;
  }

  /**
   * 更新提交状态，当RO/拒绝、批准通过时要同步更行pub_submission的状态.
   * 
   * @param rolPubId
   * @param insId
   * @param status
   * @throws DaoException
   */
  public PubRolSubmission updateSubmitStatus(Long rolPubId, Long insId, Integer status) throws DaoException {
    PubRolSubmission sub =
        super.findUnique("from PubRolSubmission t where t.insId=? and t.insPub.id=?", new Object[] {insId, rolPubId});
    if (sub != null) {
      sub.setSubmitStatus(status);
      sub.setInsConfirmDate(new Date());
    }
    return sub;
  }

  /**
   * 提出申请撤销请求.
   * 
   * @param submitId
   * @return
   * @throws DaoException
   */
  public PubRolSubmission applyWithdrawReq(Long submitId) throws DaoException {
    PubRolSubmission sub = super.findUnique("from PubRolSubmission t where t.id=?", submitId);
    if (sub != null) {
      sub.setSubmitStatus(PubRolSubmissionStatusEnum.WITHDRAW_REQ);
      sub.setWithdrawReqDate(new Date());
      sub.setWithdrawStatus(0);// 提出请求
    }
    return sub;
  }

  /**
   * 删除一个提交.
   * 
   * @param snsPubId
   * @param insId
   * @return
   * @throws DaoException
   */
  public PubRolSubmission deleteSubmission(Long snsPubId, Long insId) throws DaoException {
    PubRolSubmission sub =
        super.findUnique("from PubRolSubmission t where t.insId=? and t.submitPubId=?", new Object[] {insId, snsPubId});
    if (sub != null) {
      super.getSession().delete(sub);
    }
    return sub;
  }

  /**
   * 通过rolPubId,insId获取提交信息.
   * 
   * @param rolPubId
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubRolSubmission> getSubmissionByRolPubId(Long rolPubId, Long insId) throws DaoException {
    return super.createQuery("from PubRolSubmission t where t.insId=? and t.insPub.id=?", insId, rolPubId).list();
  }

  /**
   * 通过rolPubId,insId获取提交信息.
   * 
   * @param rolPubId
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubRolSubmission> getSubmissionByRolRealPubId(Long realPubId, Long insId) throws DaoException {
    return super.createQuery("from PubRolSubmission t where t.insId=? and t.realInsPub.id=?", insId, realPubId).list();
  }

  /**
   * 通过snsPubId,insId获取提交信息.
   * 
   * @param rolPubId
   * @param insId
   * @return
   * @throws DaoException
   */
  public PubRolSubmission getSubmissionBySNSPubId(Long snsPubId, Long insId) throws DaoException {
    PubRolSubmission sub =
        super.findUnique("from PubRolSubmission t where t.insId=? and t.submitPubId=?", new Object[] {insId, snsPubId});
    return sub;
  }

  @SuppressWarnings("unchecked")
  public List<PubRolSubmission> querySubmissionByPsnId(Long psnId) throws DaoException {
    return super.createQuery("from PubRolSubmission t where t.psnId=? or t.submitPsnId=?", psnId, psnId).list();
  }
}
