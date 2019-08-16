package com.smate.center.task.dao.rol.quartz;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.rol.quartz.PubPsnRol;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PubPsnRolDao extends RolHibernateDao<PubPsnRol, Long> {

  public PubPsnRol saveAuthorConfirmResult(Long psnId, Long insPubId, Long cofirmPmId, int confirmResult) {
    PubPsnRol pubpsn = this.getAllStatusPubPsn(insPubId, psnId);
    if (pubpsn != null) {
      pubpsn.setConfirmResult(confirmResult);
      pubpsn.setConfirmDate(new Date());
      if (cofirmPmId != null)
        pubpsn.setAuthorPMId(cofirmPmId);
    }
    return pubpsn;
  }

  /**
   * 获取人员和成果关系.
   * 
   * @throws DaoException
   */
  private PubPsnRol getAllStatusPubPsn(Long insPubId, Long psnId) {
    // 先判断是否存在，不存在的话，直接返回（修改原因是因为PubPsnRol表太大，直接通过索引先判断比较合适）
    Long count =
        (Long) super.createQuery("select count(t.id) from PubPsnRol t where t.psnId =:psnId and t.pubId =:pubId ")
            .setParameter("psnId", psnId).setParameter("pubId", insPubId).uniqueResult();
    if (count == 0) {
      return null;
    }
    List<PubPsnRol> list = super.createQuery("from PubPsnRol t where t.psnId =:psnId and t.pubId =:pubId")
        .setParameter("psnId", psnId).setParameter("pubId", insPubId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 获取与单位某成果关联的人员ID列表.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public List<Long> getPubPsnIds(Long pubId, Long insId) {
    String hql =
        "select psnId from PubPsnRol t where t.confirmResult in(0,1,2) and t.pubId =:pubId and t.insId =:insId ";
    return super.createQuery(hql, pubId, insId).list();
  }

  /**
   * 更新用户成果登录状态.
   * 
   * @param psnId
   */
  public void setPsnLogin(Long psnId) {

    String hql = "update PubPsnRol t set t.psnLogin = 1 where  t.psnId = :psnId and t.psnLogin = 0 ";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

}
