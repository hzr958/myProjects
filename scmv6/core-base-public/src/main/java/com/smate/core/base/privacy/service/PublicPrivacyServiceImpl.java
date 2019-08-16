package com.smate.core.base.privacy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.privacy.dao.PrivacySettingsDao;
import com.smate.core.base.privacy.model.PrivacySettings;
import com.smate.core.base.privacy.model.PrivacySettingsPK;

/**
 * 公共的隐私权限服务类，获取用户操作权限
 * 
 * @author aijiangbin
 *
 */
@Service("publicPrivacyService")
@Transactional(rollbackFor = Exception.class)
public class PublicPrivacyServiceImpl implements PublicPrivacyService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 发送消息
   */
  final public static String sendMsg = "sendMsg";
  /**
   * 请求添加好友
   */
  final public static String reqAddFrd = "reqAddFrd";
  /**
   * 查看好友列表
   */
  final public static String vFrdList = "vFrdList";
  /**
   * 查看我的文献
   */
  final public static String vMyLiter = "vMyLiter";

  @Autowired
  private PrivacySettingsDao privacySettingsDao;

  /**
   * 能否给用户发送消息 查看权限0=任何人1=好友
   * 
   * @param currentPsnId 当前人
   * @param consumerPsnId 发消息的用户
   * @return
   */
  @Override
  public Boolean canSendMsg(Long currentPsnId, Long consumerPsnId) {
    if (!checkParams(currentPsnId, consumerPsnId)) {
      return false;
    }
    PrivacySettingsPK pk = new PrivacySettingsPK();
    pk.setPsnId(consumerPsnId);
    pk.setPrivacyAction(sendMsg);
    PrivacySettings ps = privacySettingsDao.get(pk);
    if (ps == null) {
      return false;
    } else if (ps.getPermission() == 0) {
      return true;
    } else if (privacySettingsDao.isMyFriend(currentPsnId, consumerPsnId)) {
      return true;
    }

    return false;
  }

  @Override
  public Boolean canAddFriend(Long currentPsnId, Long consumerPsnId) {

    return false;
  }

  /**
   * 能否查看用户的用户为好友 查看权限0=任何人1=好友 2=仅本人
   * 
   * @param sendPsnId 当前人
   * @param consumerPsnId 用户
   * @return
   */
  @Override
  public Boolean canLookConsumerFriends(Long currentPsnId, Long consumerPsnId) {
    // if(!checkParams(currentPsnId,consumerPsnId )) {
    // return false ;
    // }
    if (currentPsnId.longValue() == consumerPsnId.longValue()) {
      return true;
    }
    PrivacySettingsPK pk = new PrivacySettingsPK();
    pk.setPsnId(consumerPsnId);
    pk.setPrivacyAction(vFrdList);
    PrivacySettings ps = privacySettingsDao.get(pk);
    if (ps == null) {
      return false;
    } else if (ps.getPermission() == 0) { // 任何人
      return true;
    } else if (ps.getPermission() == 2) {// 本人
      return false;
    } else if (privacySettingsDao.isMyFriend(currentPsnId, consumerPsnId)) {
      return true;
    }

    return false;
  }

  /**
   * 能否查看用户的添加文献的动态 1== 好友， 2== 本人
   * 
   * @param sendPsnId 当前人
   * @param consumerPsnId 用户
   * @return
   */
  @Override
  public Boolean canLookConsumerAddRefDyn(Long currentPsnId, Long consumerPsnId) {
    if (!checkParams(currentPsnId, consumerPsnId)) {
      return false;
    }
    if (currentPsnId.longValue() == consumerPsnId.longValue()) {
      return true;
    }
    PrivacySettingsPK pk = new PrivacySettingsPK();
    pk.setPsnId(consumerPsnId);
    pk.setPrivacyAction(vMyLiter);
    PrivacySettings ps = privacySettingsDao.get(pk);
    if (ps == null) {
      return false;
    } else if (ps.getPermission() == 0) {// 任何人
      return true;
    } else if (ps.getPermission() == 1 && privacySettingsDao.isMyFriend(currentPsnId, consumerPsnId)) {
      return true;
    }
    return false;
  }

  Boolean checkParams(Long currentPsnId, Long consumerPsnId) {
    if (currentPsnId == null || currentPsnId == 0L) {
      return false;
    }
    if (consumerPsnId == null || consumerPsnId == 0L) {
      return false;
    }
    return true;
  }

}
