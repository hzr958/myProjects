package com.smate.web.group.dao.group.inviteurl;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.inviteurl.InviteUrlValue;

@Repository
public class InviteUrlValueDao extends SnsHibernateDao<InviteUrlValue, Long> {

  /**
   * 根据引用ID找到对应邀请链接的参数值
   * 
   * @param id
   * @return InviteUrlValue
   */
  public InviteUrlValue findInviteUrlValueByRefId(Long id) throws Exception {
    String hql = "from InviteUrlValue iuv where iuv.reference_id = ?";
    return super.findUnique(hql, id);
  }
}
