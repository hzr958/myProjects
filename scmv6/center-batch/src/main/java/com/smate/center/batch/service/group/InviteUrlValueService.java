package com.smate.center.batch.service.group;

import com.smate.center.batch.model.sns.pub.InviteUrlValue;

/**
 * 邀请信息处理接口
 * 
 * @author zzx
 *
 */
public interface InviteUrlValueService {
  /**
   * 根据RefID查找InviteUrlValue;
   * 
   * @param id
   * @return InviteUrlValue
   */
  public InviteUrlValue findInviteUrlValueByRefId(Long id) throws Exception;

  /**
   * 保存inviteUrlValue并返回标识
   * 
   * @param inviteUrlValue
   * @return
   * @throws Exception
   */
  public Long saveInviteUrlValue(InviteUrlValue inviteUrlValue) throws Exception;
}
