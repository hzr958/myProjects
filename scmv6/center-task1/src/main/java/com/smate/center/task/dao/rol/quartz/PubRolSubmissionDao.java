package com.smate.center.task.dao.rol.quartz;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.rol.quartz.PubRolSubmission;
import com.smate.center.task.model.rol.quartz.PubRolSubmissionStatusEnum;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PubRolSubmissionDao extends RolHibernateDao<PubRolSubmission, Long> {
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

}
