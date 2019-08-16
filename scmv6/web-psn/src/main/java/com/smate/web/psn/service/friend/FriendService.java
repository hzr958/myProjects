package com.smate.web.psn.service.friend;

import java.util.List;
import java.util.Map;

import com.smate.core.base.psn.dto.profile.Personal;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.friend.FriendForm;
import com.smate.web.psn.model.friend.FriendTemp;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.psninfo.PsnInfo;
import com.smate.web.psn.model.psninfo.SyncPerson;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;

/**
 * 好友服务接口
 * 
 * @author zk
 *
 */
public interface FriendService {
  // 缓存推荐人员id
  static final String RECOMMEND_PSN_CACHE = "recommend_psn_cache";
  // 缓存推荐人员id的key
  static final String RECOMMEND_PSNIDS = "recommend_psnids";
  // 邀请人员加为好友的类型标示.
  static final String FRIEND_INVITE_KEY = "frd_invite";
  // 邀请人员加为好友的邮件模板(系统外用户).
  static final String REQ_ADD_FRIEND_OUT_TEMPLATE = "Req_Outside_Add_Friend_Template";
  // 通过邮件邀请人员加入群组的邀请码生成规则.
  static final String INVITATION_PARAM_INVITEID = "des3InviteId";
  static final String INVITATION_PARAM_NODEID = "nodeId";
  static final String INVITATION_PARAM_MAILID = "des3mailId";
  static final String INVITATION_PARAM_INBOXID = "des3inboxId";
  static final String INVITATION_PARAM_PSNID = "des3PsnId";
  /*
   * static final String FRIEND_INVITATION_CODE_RULE = INVITATION_PARAM_INVITEID + "-" +
   * INVITATION_PARAM_NODEID + "-" + INVITATION_PARAM_MAILID + "-" + INVITATION_PARAM_INBOXID + "-" +
   * INVITATION_PARAM_PSNID;
   */
  static final String FRIEND_INVITATION_CODE_RULE =
      INVITATION_PARAM_INVITEID + "-" + INVITATION_PARAM_NODEID + "-" + INVITATION_PARAM_PSNID;

  /**
   * 判断 是否为好友
   * 
   * @param currendPsnId
   * @param friendPsnId
   * @return
   */
  public boolean isFriend(Long currendPsnId, Long friendPsnId);

  /**
   * task 1：获取人员所有单位和教育经历单位，2：获取人员所在国家、地区.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  SyncPerson syncPersonByIns(Long psnId) throws ServiceException;

  /**
   * 根据人员Id，获取人员好友列表
   * 
   * @param psnId
   * @return
   */

  void findFriendLis(FriendForm form) throws Exception;

  /**
   * 对给定的人员列表进行排序分类
   * 
   * @param psnList
   * @return
   */
  Map<String, List<PsnInfo>> sortFriendName(List<PsnInfo> psnList);

  /**
   * 删除人员工作经历评价信息
   * 
   * @param psnId
   * @param workId
   */
  void delPersonFappraisal(Long psnId, Long workId);

  /**
   * 添加好友请求.
   * 
   * @param friendSystempId
   * @param groupIdList
   * @throws ServiceException
   */
  void addFriendReq(Long receiverId, String domainscm, Personal form) throws ServiceException;

  /**
   * 是否能够添加好友
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Boolean isPsnAddFrdPrivacy(Long psnId) throws ServiceException;

  /**
   * 根据人员ID获取可能认识的人员List
   * 
   * @param psnId
   * @param pubCpt
   * @param prjCpt
   * @return
   * @throws ServiceException
   */
  List<Person> findMayKnowPersonListByPsnIds(Page<Person> page, Long psnId, boolean firstPage, String pubCpt,
      String prjCpt) throws ServiceException;

  /**
   * 可能认识的人列表
   * 
   * @param psnId
   * @param pageSize
   * @param lastPsnId
   * @return
   * @throws ServiceException
   */
  public List<Person> findPersonMayKnowByCurrentPsnId(Long psnId, boolean firstPage, Page<Person> page)
      throws ServiceException;

  /**
   * 基本信息的封装
   * 
   * @param knowList
   */
  public void bulidBaseInfo(List<Person> knowList, Page<PsnInfo> page, boolean firstPage, String mayCount);

  /**
   * 获取人员的好友ID和已经请求过的人员的ID
   * 
   * @param psnId
   * @throws ServiceException
   */
  public List<Long> findFriendAndHasReqFriendIds(Long psnId) throws ServiceException;

  /**
   * 调用接口发送邮件
   * 
   * @param receiverId
   * @param email
   * @param sendName
   * @return
   * @throws ServiceException
   */
  public void restSendAddFriendEmail(Long receiverId, String email, String receiverName, Personal form,
      String emailLanguage) throws ServiceException;

  /*
   * 保存发送邮件数据
   */
  public void saveMailInitData(Map<String, Object> dataMap) throws ServiceException;

  /**
   * 删除好友
   * 
   * @param psnId
   * @param friendId
   * @throws ServiceException
   */
  public void delFriend(Long psnId, Long friendId) throws ServiceException;

  /**
   * 批量删除好友
   * 
   * @param psnIds
   * @throws ServiceException
   */
  public void delFriendByPsnIds(String psnIds) throws ServiceException;

  /**
   * 根据psnId 获取好友id
   */
  public List<Long> getFriendListByPsnId(Long psnId) throws ServiceException;

  /**
   * 根据个人id获取好友ID
   * 
   * @author lhd
   * @param form
   * @return
   * @throws ServiceException
   */
  public List<Long> getFriendIds(PsnListViewForm form) throws Exception;

  /**
   * 好友按地区分组统计
   * 
   * @author lhd
   * @param form
   * @return
   * @throws Exception
   */
  public void sortFriendByReg(PsnListViewForm form) throws Exception;

  /**
   * 好友列表数据回显
   * 
   * @param form
   * @throws Exception
   */
  public Map<String, Object> getFriendsCallBack(PsnListViewForm form) throws Exception;

  /**
   * 获取可能认识的人
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public List<Long> getRecommendPsnListByPsnId(Long psnId) throws Exception;

  /**
   * 邀请好友
   * 
   * @param form
   * @throws Exception
   */
  void sendMail(Personal form) throws Exception;

  /**
   * 获取推荐人员
   * 
   * @param form
   * @throws Exception
   */
  public List<Long> recommendPsn(PsnListViewForm form) throws Exception;


  /**
   * 同意 某个人员的好友请求后真正开始互相添加好友的处理
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public void acceptSomeOneAddFriendRequest(Long psnId, Long reqPsnId) throws ServiceException;

  /**
   * 获取未被处理的历史好友请求的Id
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public List<Long> findSendFriendReqHistory(PsnListViewForm form) throws Exception;

  /**
   * 推荐列表-移除
   * 
   * @param form
   * @throws Exception
   */
  public void addFriendTempSys(PsnListViewForm form) throws Exception;

  /**
   * 取消发送请求
   * 
   * @throws Exception
   */

  public void removeAddFriendReq(Long receiverId) throws Exception;

  /**
   * 好友请求列表
   * 
   * @param form
   * @throws Exception
   */
  public List<Long> showFriendRequest(PsnListViewForm form) throws Exception;

  /**
   * 好友请求列表-忽略操作
   * 
   * @param form
   * @throws Exception
   */
  public void negletFriendReq(PsnListViewForm form) throws Exception;

  public void getMsgChatPsnList(PsnListViewForm form) throws Exception;

  public void getVistStatisticsPsnList(PsnListViewForm form) throws Exception;

  /**
   * 获取可能认识的人分页操作
   * 
   * @return
   * @throws Exception
   */
  public List<Long> getMobileRecommendPsn(Integer pageNo, List<Long> recommendIds) throws Exception;

  /**
   * 移动端获取可能认识的人
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public List<Long> getMobileRecommendPsnList(Long psnId) throws Exception;

  /**
   * 获取好友请求数量
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public Long getReqFriendNumber(Long psnId) throws Exception;

  public void queryFriendsReq(PsnListViewForm form) throws Exception;

  /**
   * 同意好友请求
   * 
   * @return
   * @throws ServiceException
   */
  public String acceptAddFriendRequest(Long reqPsnId, Long currentPsnId) throws ServiceException;

  public List<Person> getContactfriend(String mobile) throws ServiceException;

  public List<FriendTemp> checkFriendTempExists(Long reqPsnId, Long currentPsnId);

  public Person getPsnInfo(Long psnId);

  /**
   * 新好友请求列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public List<Long> newFriendRequest(PsnListViewForm form) throws Exception;

  /**
   * 
   * @Title: getPersonBasePage
   * @Description: 批量查询person
   * @param @param psnIds
   * @param @return 入参
   * @return List<Person> 返回类型
   */
  public List<Person> getPersonBasePage(List<Long> psnIds) throws ServiceException;

  /**
   * 第一次登录或者关注人员少于5个人的情况，自动关注人员至50人
   * 
   * 
   */
  public void autoFollowingPsn() throws Exception;

  /**
   * 同步数据.
   * 
   */
  void updatePersonInfo(SnsPersonSyncMessage msg);

  /**
   * 是否存在当前邮件
   * 
   * @param email
   * @return
   */
  public boolean IsExistsEmail(String email);

  /**
   * 获取 邀请合作者psnId
   * 
   * @param form
   * @throws Exception
   */
  public List<Long> getInviteFriendIds(Long rolPubId, Long psnId) throws Exception;

  /**
   * 获取人员好友统计数
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public Long getPsnFriendCount(Long psnId) throws ServiceException;

  public void autoAddFriend(Long invitPsnId) throws ServiceException;


  /**
   * 处理添加好友请求
   * 
   * @param form
   * @return
   * @throws PsnException
   */
  Map<String, String> dealWithAddFriendReq(FriendForm form) throws ServiceException;
}
