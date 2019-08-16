package com.smate.center.batch.service.mail;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.InsideMailBox;
import com.smate.center.batch.model.mail.InsideMailBoxCon;
import com.smate.center.batch.model.mail.InviteMailBox;
import com.smate.center.batch.model.mail.InviteMailBoxCon;
import com.smate.center.batch.model.mail.PsnInsideMailBox;
import com.smate.center.batch.model.mail.PsnInviteMailBox;
import com.smate.center.batch.model.mail.PsnMessageNoticeOutBox;
import com.smate.center.batch.model.mail.PsnReqMailBox;
import com.smate.center.batch.model.mail.PsnShareMailBox;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;


/**
 * 发件箱服务层.
 * 
 * @author oyh
 * 
 */

public interface MailBoxService {

  /**
   * 加载站内短消息发件箱.
   * 
   * @param page
   * @return
   * @throws ServiceException
   */

  Page<PsnInsideMailBox> loadInsideMailBox(Page<PsnInsideMailBox> page, Message message) throws ServiceException;

  /**
   * 加载站内请求更新应用次数发件箱.
   * 
   * @param page
   * @return
   * @throws ServiceException
   */
  Page<PsnReqMailBox> loadReqMailBox(Page<PsnReqMailBox> page) throws ServiceException;

  /**
   * 加载站内推荐发件箱.
   * 
   * @param page
   * @return
   * @throws ServiceException
   */
  Page<PsnShareMailBox> loadShareMailBox(Page<PsnShareMailBox> page, Message message) throws ServiceException;

  /**
   * 加载站内邀请发件箱.
   * 
   * @param page
   * @return
   * @throws ServiceException
   */
  Page<PsnInviteMailBox> loadInviteMailBox(Page<PsnInviteMailBox> page, Message message) throws ServiceException;

  /**
   * 获取发件箱邮件详细信息.
   * 
   * @param msg
   * @return
   * @throws ServiceException
   */
  PsnInsideMailBox getMailDetailById(Message msg) throws ServiceException;

  /**
   * 删除邮件.
   * 
   * @param ids
   * @param type
   * @throws ServiceException
   */
  void deleteMailBoxById(String ids, String type) throws ServiceException;

  /**
   * 根据发信箱Id获取实体.
   * 
   * @param msg
   * @return
   * @throws ServiceException
   */
  public PsnMessageNoticeOutBox getNoticeOutDetailById(Message msg) throws ServiceException;

  /**
   * 更改发件箱状态.
   * 
   * @param id
   * @param status
   * @param type
   * @throws ServiceException
   */
  void updateMailBoxStatus(Long id, Integer status, String type) throws ServiceException;

  /**
   * 获取上一条 下一条 链接状态.
   * 
   * @param msg
   * @return
   * @throws ServiceException
   */
  String getLinkInfo(Message msg) throws ServiceException;

  /**
   * 加载邀请邮件信息.
   * 
   * @param msg
   * @return
   * @throws ServiceException
   */
  PsnInviteMailBox getInviteMailDetailById(Message msg) throws ServiceException;

  /**
   * 查找邀请邮件.
   * 
   * @param mailId
   * @return
   * @throws ServiceException
   */
  InviteMailBox findInviteMailById(Long mailId) throws ServiceException;

  void syncPersonInfo(Person person) throws ServiceException;

  /**
   * 读取发件箱列表.
   * 
   * @param msg
   * @param page
   * @return
   * @throws ServiceException
   */

  Page<PsnInsideMailBox> getPsnMailForBpo(Message msg, Page<PsnInsideMailBox> page) throws ServiceException;

  /**
   * 读取邀请列表.
   * 
   * @param msg
   * @param page
   * @return
   * @throws ServiceException
   */
  Page<PsnInviteMailBox> getPsnInviteMailForBpo(Message msg, Page<PsnInviteMailBox> page) throws ServiceException;

  /**
   * 移除站内短消息.
   * 
   * @param key
   * @throws ServiceException
   */
  void removeInsideMailBox(Long key) throws ServiceException;

  /**
   * 移除邀请消息.
   * 
   * @param key
   * @throws ServiceException
   */
  void removeInviteMailBox(Long key) throws ServiceException;

  /**
   * 共享.
   * 
   * @param mailId
   * @return
   * @throws ServiceException
   */
  PsnShareMailBox findPsnShareMailBoxById(Long mailId) throws ServiceException;

  /**
   * 获取要更新冗余收件人数据的发件箱列表.
   * 
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  List<PsnInsideMailBox> getInsideMailBoxsByRecv(int maxSize) throws ServiceException;

  List<PsnInviteMailBox> getRequestMailBoxsByRecv(int maxSize) throws ServiceException;

  List<PsnShareMailBox> getShareMailBoxsByRecv(int maxSize) throws ServiceException;

  /**
   * 更新冗余的收件人信息.
   * 
   * @param mailId
   * @param zhReceivers
   * @param enReceivers
   * @param type
   * @throws ServiceException
   */
  void updateMailBoxRecv(Long mailId, String zhReceivers, String enReceivers, int type) throws ServiceException;

  /**
   * 保存站内邀请发件箱记录.
   * 
   * @param person 发件人的记录.
   * @param mailParam 发件箱参数值.
   * @return
   * @throws ServiceException
   */
  InviteMailBox saveInviteMailBox(Person person, Map<String, Object> mailParam) throws ServiceException;

  /**
   * 保存邀请发件记录内容_MJG_SCM-5910.
   * 
   * @param inviteMailBox
   */
  void saveInviteMailboxCon(InviteMailBox inviteMailBox);

  /**
   * 保存站内信发件箱记录.
   * 
   * @param map
   * @return
   * @throws ServiceException
   */
  InsideMailBox saveInsideMailBox(Map<String, Object> map) throws ServiceException;

  /**
   * 保存发件箱记录内容_MJG_SCM-5910
   * 
   * @param insideMail
   */
  void saveInsideMailboxCon(InsideMailBox insideMail);

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  void delInsideMailBoxByPsnId(Long psnId);

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  void delInviteMailBoxByPsnId(Long psnId);

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  void delReqMailBoxByPsnId(Long psnId);

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  void delShareMailBoxByPsnId(Long psnId);

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  void delNoticeOutBoxByPsnId(Long psnId);

  Page<InsideMailBox> getInsideMailBoxByPage(Page<InsideMailBox> page, Message message) throws ServiceException;

  /**
   * 查看短信发件箱详情.
   * 
   * @param mailId
   * @return
   * @throws ServiceException
   */
  String getInsideMailBoxDetail(Long mailId) throws ServiceException;

  /**
   * 获取站内信息发件内容.
   * 
   * @param mailId
   * @return
   */
  InsideMailBoxCon getInsideMailBoxCon(Long mailId);

  /**
   * 获取站内邀请发件内容.
   * 
   * @param mailId
   * @return
   */
  InviteMailBoxCon getInviteMailBoxCon(Long mailId);

  /**
   * 清除站内消息发件内容_MJG_SCM-6097.
   * 
   * @param mailBox
   */
  void cleanInsideMailbox(InsideMailBox mailBox);

  /**
   * 清除站内邀请发件内容_MJG_SCM-6097.
   * 
   * @param mailBox
   */
  void cleanInviteMailbox(InviteMailBox mailBox);
}
