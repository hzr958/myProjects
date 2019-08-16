package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PsnPubSendFlag;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 用于标记是否发送指派数据到用户，默认冗余psn_ins表is_login.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPubSendFlagDao extends RolHibernateDao<PsnPubSendFlag, Long> {

  @Autowired
  private PubPsnRolDao pubPsnRolDao;

  /**
   * 用于标记是否发送指派数据到用户.
   * 
   * @param psnId
   * @param flag
   */
  public void saveRolPsnSendFlag(Long psnId, boolean flag) {
    // 一般只记录1的情况
    if (flag) {
      PsnPubSendFlag login = super.get(psnId);
      if (login == null) {
        login = new PsnPubSendFlag(psnId, 1);
        // 已经标记为1
      } else if (login.getFlag() == 1) {
        return;
      } else {
        login.setFlag(1);
      }
      super.save(login);
      // 更新人员成果登录状态
      pubPsnRolDao.setPsnLogin(psnId);
    }
  }

  /**
   * 判断是否能发送指派成果到用户.
   * 
   * @param psnId
   * @return
   */
  public boolean isCanSend(Long psnId) {
    PsnPubSendFlag login = super.get(psnId);
    if (login == null) {
      return false;
    } else if (login.getFlag() == 1) {
      return true;
    }
    return false;
  }

  /**
   * 是否能发送指派成果到用户.
   * 
   * @param psnId
   * @return
   */
  public Integer getSendFlag(Long psnId) {
    if (this.isCanSend(psnId)) {
      return 1;
    }
    return 0;
  }

  /**
   * 删除指派数据.
   * 
   * @param psnId
   */
  public void delPsnPubSendFlag(Long psnId) {
    String hql = "delete from PsnPubSendFlag where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 根据人员获取记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPubSendFlag> getPsnPubSendFlagList(Long psnId) {

    String ql = "from PsnPubSendFlag where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }
}
