package com.smate.core.base.utils.wechat;

import java.util.Map;

/**
 * 网页授权服务接口.
 * 
 * @author xys
 *
 */
public interface OAuth2Service {

  /**
   * 获取微信openid.
   * 
   * @param code
   * @return
   * @throws Exception
   */
  public String getWeChatOpenId(String code) throws Exception;

  /**
   * 获取access_token信息
   * 
   * @param code
   * @return
   * @throws Exception
   */
  public String getWeChatToken() throws Exception;

  /**
   * 通过access_token和openidList批量用户基本信息
   * 
   * @param code
   * @return
   * @throws Exception
   */
  public Map<String, Object> getWeChatInfos(String token, String userList) throws Exception;

  /**
   * 通过access_token和openid单个用户基本信息
   * 
   * @param code
   * @return
   * @throws Exception
   */
  public Map<String, Object> getWeChatInfoSingle(String token, String user) throws Exception;

  /**
   * 公众平台根据openid获取用户信息.
   * 
   * @param token 公众平台token 注意区分用户token
   * @param openId
   * @return
   * @throws Exception
   */
  public Map<String, Object> getWeChatInfo(String token, String openId) throws Exception;

  /**
   * 开放平台根据openid获取用户信息.
   * 
   * @param token 公众平台token 注意区分用户token
   * @param openId
   * @return
   * @throws Exception
   */
  public Map<String, Object> getOpenWeChatInfo(String token, String openId) throws Exception;

  /**
   * 开放平台获取微信数据.
   * 
   * @param code
   * @return
   * @throws Exception
   */
  public Map<String, Object> getOpenWeChatUnionInfo(String code) throws Exception;
}
