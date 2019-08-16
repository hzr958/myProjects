package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.InviteUrlValue;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 邀请链接信息Dao
 * 
 * @author zzx
 *
 */
@Repository
public class InviteUrlValueDao extends SnsHibernateDao<InviteUrlValue, Long> {
  /**
   * 根据引用ID找到对应邀请链接的参数值
   * 
   * @param id
   * @return InviteUrlValue
   */
  public InviteUrlValue findInviteUrlValueByRefId(Long id) throws DaoException {
    String hql = "from InviteUrlValue iuv where iuv.reference_id = ?";
    return super.findUnique(hql, id);
  }
}
