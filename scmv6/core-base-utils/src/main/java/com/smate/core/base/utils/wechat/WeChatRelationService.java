package com.smate.core.base.utils.wechat;

import java.util.List;

public interface WeChatRelationService {

  /**
   * 通过微信opsnid获取人员id
   * 
   * @param webChatOpenId
   * @return
   */
  Long querypsnIdByWeChatOpenid(String webChatOpenId);

  /**
   * 通过微信Unionid获取人员id
   * 
   * @param openId
   * @param unionId
   */
  Long querypsnIdByWeChatUnionid(String webChatUnionId);

  /**
   * 获取没有UnionId数据的openId集合
   * 
   * @return
   */
  List<String> findWeChatNoUnionIdList();

  /**
   * 更新数据库UnionId信息
   * 
   * @param openId
   * @param unionId
   */
  void refreshUnionId(String openId, String unionId) throws Exception;

  /**
   * 绑定微信
   * 
   * @param webChatOpenId
   * @param webChatUnionId
   * @param psnId
   * @param nickName
   * @throws Exception
   */
  String bindWeChat(String webChatOpenId, String webChatUnionId, Integer bindType, Long psnId, String nickName)
      throws Exception;

  /**
   * 通过微信unionId获取关联的科研之友人员openId
   * 
   * @param wxUnionId
   * @return
   * @throws Exception
   */
  Long findSmateOpenIdByWxUnionId(String wxUnionId) throws Exception;

}
