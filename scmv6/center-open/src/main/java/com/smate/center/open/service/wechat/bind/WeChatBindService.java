package com.smate.center.open.service.wechat.bind;

import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.wechat.bind.WeChatBindForm;

public interface WeChatBindService {

  /**
   * 微信绑定科研之友openid
   * 
   * @param form
   * @throws OpenException
   */
  void bindUser(WeChatBindForm form) throws OpenException;

  /**
   * 取消用户绑定
   * 
   * @param form
   * @throws OpenException
   */
  void cancelBindUser(String wxOpenId) throws Exception;

  /**
   * 微信绑定科研之友-----通过微信unionId
   * 
   * @param form
   * @throws OpenException
   */
  void bindUserWithWxUnionId(WeChatBindForm form) throws OpenException;

  /**
   * 获取人员的微信openId和wxUnionId-------通过微信跳转链接返回的code
   * 
   * @param form
   * @throws OpenException
   */
  void getPsnWxUnionIdAndWxOpenId(WeChatBindForm form) throws OpenException;

}
