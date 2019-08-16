package com.smate.center.oauth.service.bind;

import com.smate.center.oauth.model.bind.ThirdBindForm;
import com.smate.core.base.utils.model.wechat.WeChatRelation;

public interface WeChatBindService {

  /**
   * 微信绑定科研之友unionid
   * 
   * @param form
   * @throws Exception
   */
  void pcBindUser(ThirdBindForm form) throws Exception;

  WeChatRelation findWeChatRelationByPsnId(Long psnId);

  /**
   * 微信账号是否已绑定过科研之友账号
   * 
   * @param wxUnionId
   * @return
   */
  boolean wxHasBinded(String wxUnionId);

  /**
   * 保存微信关联关系
   * 
   * @param form
   */
  public void persistenceWeChatRelation(ThirdBindForm form);

  /**
   * 从v_open_user_union表查找人员openId
   * 
   * @param psnId
   * @return
   */
  public Long findPsnOpenIdInUserUnion(Long psnId);

}
