package com.smate.center.open.service.wechat.user;

import java.util.List;
import java.util.Map;

/**
 * 微信用户服务.
 * 
 * @author xys
 *
 */
public interface UserService {

  /**
   * 获取用户列表.
   * 
   * @param userList 当前用户列表.
   * @param curCount 当前拉取的OPENID个数.
   * @param nextOpenid 拉取列表的最后一个用户的OPENID，不填默认从头开始拉取.
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<Map> getUserList(List<Map> userList, int curCount, String nextOpenid);

  /**
   * 获取OpenID列表.
   * 
   * @return
   */
  public List<String> getOpenIdList();
}
