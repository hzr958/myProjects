package com.smate.center.batch.service.mail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import com.smate.center.batch.dao.mail.FullTextMailBoxDao;
import com.smate.center.batch.dao.mail.InsideInboxDao;
import com.smate.center.batch.dao.mail.InsideMailBoxConDao;
import com.smate.center.batch.dao.mail.InsideMailBoxDao;
import com.smate.center.batch.dao.mail.InviteInboxDao;
import com.smate.center.batch.dao.mail.InviteMailBoxConDao;
import com.smate.center.batch.dao.mail.InviteMailBoxDao;
import com.smate.center.batch.dao.mail.MsgBoxTemplateDao;
import com.smate.center.batch.dao.mail.MsgNoticeOutBoxDao;
import com.smate.center.batch.dao.mail.PsnInsideMailBoxDao;
import com.smate.center.batch.dao.mail.PsnInviteMailBoxDao;
import com.smate.center.batch.dao.mail.PsnMsgNoticeOutBoxDao;
import com.smate.center.batch.dao.mail.PsnReqMailBoxDao;
import com.smate.center.batch.dao.mail.PsnShareMailBoxDao;
import com.smate.center.batch.dao.mail.ReqMailBoxDao;
import com.smate.center.batch.dao.mail.ShareMailBoxConDao;
import com.smate.center.batch.dao.mail.ShareMailBoxDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.InsideInbox;
import com.smate.center.batch.model.mail.InsideMailBox;
import com.smate.center.batch.model.mail.InsideMailBoxCon;
import com.smate.center.batch.model.mail.InviteInbox;
import com.smate.center.batch.model.mail.InviteMailBox;
import com.smate.center.batch.model.mail.InviteMailBoxCon;
import com.smate.center.batch.model.mail.MsgBoxTemplate;
import com.smate.center.batch.model.mail.PsnInsideMailBox;
import com.smate.center.batch.model.mail.PsnInviteMailBox;
import com.smate.center.batch.model.mail.PsnMessageNoticeOutBox;
import com.smate.center.batch.model.mail.PsnReqMailBox;
import com.smate.center.batch.model.mail.PsnShareMailBox;
import com.smate.center.batch.model.mail.ShareMailBoxCon;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

import freemarker.template.Configuration;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 短信.
 * 
 * @author chenxiangrong
 * 
 */
@Service("mailBoxService")
@Transactional(rollbackFor = Exception.class)
public class MailBoxServiceImpl implements MailBoxService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private static final String ENCODING = "utf-8";

  @Autowired
  private PsnInsideMailBoxDao psnInsideMailBoxDao;
  @Autowired
  private PsnReqMailBoxDao psnReqMailBoxDao;
  @Autowired
  private PsnShareMailBoxDao psnShareMailBoxDao;
  @Autowired
  private InviteMailBoxDao inviteMailBoxDao;
  @Autowired
  private PsnInviteMailBoxDao psnInviteMailBoxDao;
  @Autowired
  private InviteMailBoxConDao inviteMailBoxConDao;
  @Autowired
  private InsideMailBoxDao insideMailBoxDao;
  @Autowired
  private InsideMailBoxConDao insideMailBoxConDao;
  @Autowired
  private ReqMailBoxDao reqMailBoxDao;
  @Autowired
  private ShareMailBoxDao shareMailBoxDao;
  @Autowired
  private PsnMsgNoticeOutBoxDao msgNoticeOutDao;
  @Autowired
  private MsgNoticeOutBoxDao msgNoticeOutBoxDao;
  @Autowired
  private InsideInboxDao insideInboxDao;
  @Autowired
  private InviteInboxDao inviteInboxDao;
  @Autowired
  private FullTextMailBoxDao fullTextMailBoxDao;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private MsgBoxTemplateDao msgBoxTemplateDao;
  @Autowired
  private ShareMailBoxConDao shareMailBoxConDao;
  @Autowired
  private Configuration msgFreemarkereConfiguration;
  @Autowired
  private SysDomainConst sysDomainConst;

  @Override
  public Page<PsnInsideMailBox> loadInsideMailBox(Page<PsnInsideMailBox> page, Message message)
      throws ServiceException {

    try {
      page = this.psnInsideMailBoxDao.getPsnMailBox(page, message);
      // 重新读取发件箱内容_MJG_SCM-5910.
      if (page != null && CollectionUtils.isNotEmpty(page.getResult())) {
        List<PsnInsideMailBox> mailboxList = page.getResult();
        for (PsnInsideMailBox mailBox : mailboxList) {
          mailBox = this.rebuildPsnInsideMailBox(mailBox);
        }
      }
      return page;
    } catch (DaoException e) {

      logger.error("读取发件箱数据失败！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public Page<PsnReqMailBox> loadReqMailBox(Page<PsnReqMailBox> page) throws ServiceException {

    try {
      return this.psnReqMailBoxDao.getPsnMailBox(page);
    } catch (Exception e) {

      logger.error("读取发件箱数据失败！", e);
      throw new ServiceException();
    }

  }

  @Override
  public Page<PsnShareMailBox> loadShareMailBox(Page<PsnShareMailBox> page, Message message) throws ServiceException {

    try {
      return this.psnShareMailBoxDao.getPsnMailBox(page, message);
    } catch (Exception e) {

      logger.error("读取发件箱数据失败！", e);
      throw new ServiceException();
    }

  }

  @Override
  public Page<PsnInviteMailBox> loadInviteMailBox(Page<PsnInviteMailBox> page, Message message)
      throws ServiceException {

    try {
      page = this.psnInviteMailBoxDao.getPsnMailBox(page, message);
      // 重新构建站内邀请发件内容_MJG_SCM-5910.
      if (page != null && CollectionUtils.isNotEmpty(page.getResult())) {
        List<PsnInviteMailBox> mailboxList = page.getResult();
        for (PsnInviteMailBox mailBox : mailboxList) {
          mailBox = this.rebuildPsnInviteMailBox(mailBox);
        }
      }
      return page;
    } catch (Exception e) {

      logger.error("读取发件箱数据失败！", e);
      throw new ServiceException();
    }

  }

  @Override
  public PsnInsideMailBox getMailDetailById(Message msg) throws ServiceException {

    try {
      if (StringUtils.isNotEmpty(msg.getMailId())) {
        String mailId = ServiceUtil.decodeFromDes3(msg.getMailId());

        // 人员发件记录的标题内容迁移到内容表_MJG_SCM-5910.
        PsnInsideMailBox psnIMB = psnInsideMailBoxDao.get(Long.parseLong(mailId));
        psnIMB = this.rebuildPsnInsideMailBox(psnIMB);
        Long currUserId = SecurityUtils.getCurrentUserId();
        if (currUserId.equals(psnIMB.getSenderId())) {// 如果当前用户是发信人，则取掉“点击处理”的链接
          if (psnIMB.getContent() == null) {
            psnIMB.setContent("");
          }
          StringBuffer content = new StringBuffer(psnIMB.getContent());
          if (content.lastIndexOf(",") > 0) {
            content = content.delete(content.lastIndexOf(","), content.length());
          }
          Locale locale = LocaleContextHolder.getLocale();
          if ("en".equals(locale.getLanguage().toLowerCase())) {
            content.append(".");
          } else {
            content.append("。");
          }
          psnIMB.setStrContent(content.toString());
        }
        if (psnIMB != null) {
          if (psnIMB.getMsgType() != null && 1 == psnIMB.getMsgType()) {// 如果是好友评价
            String extOtherInfo = psnIMB.getExtOtherInfo();
            JSONObject jsonObject = JSONObject.fromObject(extOtherInfo);
            psnIMB.setPsnWork(jsonObject.getString("psnWork"));
            psnIMB.setPsnRelation(jsonObject.getString("psnRelation"));
            psnIMB.setEvaContent(jsonObject.getString("evaContent"));
          }
        }
        return psnIMB;

      } else {

        logger.error("参数为空！");
        throw new ServiceException();
      }

    } catch (Exception e) {
      logger.error("查看邮件详细失败！", e);
      throw new ServiceException();
    }

  }

  @Override
  public Page<PsnInsideMailBox> getPsnMailForBpo(Message msg, Page<PsnInsideMailBox> page) throws ServiceException {

    try {
      this.psnInsideMailBoxDao.getPsnMailBoxForBpo(page, msg);
      List<PsnInsideMailBox> result = page.getResult();
      if (result == null) {
        return null;
      }
      for (PsnInsideMailBox mailBox : result) {
        // 获取封装发件信息标题内容_MJG_SCM-5910.
        mailBox = this.rebuildPsnInsideMailBox(mailBox);
        if (mailBox.getMsgType() != null && mailBox.getMsgType() == 1) {// 如果是好友评价
          String extOtherInfo = mailBox.getExtOtherInfo();
          JSONObject jsonObject = JSONObject.fromObject(extOtherInfo);
          mailBox.setPsnWork(jsonObject.getString("psnWork"));
          mailBox.setPsnRelation(jsonObject.getString("psnRelation"));
          mailBox.setEvaContent(jsonObject.getString("evaContent"));
        }

      }
      return page;

    } catch (Exception e) {
      logger.error("查看邮件详细失败！", e);
      throw new ServiceException();
    }

  }

  @Override
  public Page<PsnInviteMailBox> getPsnInviteMailForBpo(Message msg, Page<PsnInviteMailBox> page)
      throws ServiceException {

    try {
      page = this.psnInviteMailBoxDao.getPsnMailBoxForBpo(page, msg);
      // 重新构建站内邀请发件内容_MJG_SCM-5910.
      if (page != null && CollectionUtils.isNotEmpty(page.getResult())) {
        List<PsnInviteMailBox> mailboxList = page.getResult();
        for (PsnInviteMailBox mailBox : mailboxList) {
          mailBox = this.rebuildPsnInviteMailBox(mailBox);
        }
      }
      return page;

    } catch (Exception e) {
      logger.error("查看邮件详细失败！", e);
      throw new ServiceException();
    }

  }

  /**
   * 根据发信箱Id获取实体.
   * 
   * @param msg
   * @return
   * @throws ServiceException
   */
  @Override
  public PsnMessageNoticeOutBox getNoticeOutDetailById(Message msg) throws ServiceException {

    try {
      if (StringUtils.isNotEmpty(msg.getMailId())) {
        String noticeId = ServiceUtil.decodeFromDes3(msg.getMailId());

        return msgNoticeOutDao.get(Long.parseLong(noticeId));

      } else {

        logger.error("参数为空！");
        throw new ServiceException();
      }

    } catch (Exception e) {
      logger.error("查看站内通知详细失败！", e);
      throw new ServiceException();
    }

  }

  @Override
  public PsnInviteMailBox getInviteMailDetailById(Message msg) throws ServiceException {

    try {
      if (StringUtils.isNotEmpty(msg.getMailId())) {
        String mailId = ServiceUtil.decodeFromDes3(msg.getMailId());
        PsnInviteMailBox mailBox = psnInviteMailBoxDao.get(Long.parseLong(mailId));
        // 重新构建邀请发件内容_MJG_SCM-5910.
        if (mailBox != null) {
          mailBox = this.rebuildPsnInviteMailBox(mailBox);
        }
        return mailBox;

      } else {

        logger.error("参数为空！");
        throw new ServiceException();
      }

    } catch (Exception e) {
      logger.error("查看邮件详细失败！", e);
      throw new ServiceException();
    }

  }

  @Override
  public void deleteMailBoxById(String ids, String type) throws ServiceException {
    if (StringUtils.isEmpty(ids)) {

      logger.error("传入参数为空，错误！");
      throw new ServiceException();
    }
    try {
      for (String id : ids.split(",")) {

        Long key = Long.parseLong(id);
        updateMailBoxStatus(key, 1, type);

      }
    } catch (Exception e) {

      logger.error("删除邮件失败！", e);
      throw new ServiceException();
    }

  }

  @Override
  public void updateMailBoxStatus(Long id, Integer status, String type) throws ServiceException {
    try {
      if ("inside".equals(type)) { // 站内短消息
        // 更新站内短消息的状态.
        this.insideMailBoxDao.updateMailBoxStatus(status, id);
      } else if ("invite".equals(type)) { // 站内邀请
        // 更新站内邀请发件记录.
        this.psnInviteMailBoxDao.updateInviteMailStatus(status, id);
      } else if ("ftrequest".equals(type)) {
        fullTextMailBoxDao.updateMailBoxStatus(id, status);
      } else if ("req".equals(type)) { // 站内请求
        // 更新站内请求发件记录状态.
        this.psnReqMailBoxDao.updateReqMailBox(id, status);
      } else if ("share".equals(type)) {// 站内推荐
        // 更新站内推荐记录状态_MJG_SCM-5910.
        this.psnShareMailBoxDao.updateShareMailStatus(status, id);
      }
    } catch (Exception e) {

      logger.error("更改收件箱状态失败！", e);
      throw new ServiceException();
    }

  }

  @Override
  public String getLinkInfo(Message msg) throws ServiceException {
    try {
      String rtn = "";
      if (StringUtils.isNotEmpty(msg.getMailId())) {
        String mailId = ServiceUtil.decodeFromDes3(msg.getMailId());
        if ("inside".equalsIgnoreCase(msg.getType())) {
          PsnInsideMailBox preMailBox = this.psnInsideMailBoxDao.getPrev(Long.parseLong(mailId));
          PsnInsideMailBox nextMailBox = this.psnInsideMailBoxDao.getNext(Long.parseLong(mailId));
          rtn = "{\"action\":\"success\",\"prev\":" + (preMailBox != null) + ",\"next\":" + (nextMailBox != null) + "";

          if (preMailBox != null) {

            rtn += ",\"prevId\":\"" + ServiceUtil.encodeToDes3(preMailBox.getMailId().toString()) + "\" ";
          }

          if (nextMailBox != null) {

            rtn += ",\"nextId\":\"" + ServiceUtil.encodeToDes3(nextMailBox.getMailId().toString()) + "\"";
          }

          rtn += "}";
        } else if ("share".equalsIgnoreCase(msg.getType())) {

          PsnShareMailBox preMailBox = this.psnShareMailBoxDao.getPrev(Long.parseLong(mailId));
          // 获取封装发件信息标题内容_MJG_SCM-5910.
          preMailBox = this.rebuildPsnShareMailBox(preMailBox);
          PsnShareMailBox nextMailBox = this.psnShareMailBoxDao.getNext(Long.parseLong(mailId));
          // 获取封装发件信息标题内容_MJG_SCM-5910.
          nextMailBox = this.rebuildPsnShareMailBox(nextMailBox);
          Map<String, String> map = new HashMap<String, String>();
          map.put("action", "success");
          map.put("prev", String.valueOf((preMailBox != null)));
          map.put("next", String.valueOf((nextMailBox != null)));

          if (preMailBox != null) {
            map.put("prevId", preMailBox.getMailId().toString());
            map.put("prevTitle", preMailBox.getTitle());
          }

          if (nextMailBox != null) {
            map.put("nextId", nextMailBox.getMailId().toString());
            map.put("nextTitle", nextMailBox.getTitle());
          }

          rtn = JSONObject.fromObject(map).toString();

        }

        return rtn;
      }
    } catch (Exception e) {

      logger.error("查询数据失败！", e);
      throw new ServiceException(e);

    }

    return null;
  }

  @Override
  public InviteMailBox findInviteMailById(Long mailId) throws ServiceException {
    InviteMailBox mailBox = inviteMailBoxDao.get(mailId);
    // 重新获取站内邀请发件内容_MJG_SCM-5910.
    if (mailBox != null) {
      mailBox = this.rebuildInviteMailBox(mailBox);
    }
    return mailBox;
  }

  @Override
  public void syncPersonInfo(Person person) throws ServiceException {

    try {
      // this.insideMailBoxDao.updatePersonInfo(message);
      // this.inviteMailBoxDao.updatePersonInfo(person);
      // this.reqMailBoxDao.updatePersonInfo(person);
      // this.shareMailBoxDao.updatePersonInfo(message);
      this.msgNoticeOutBoxDao.updatePersonInfo(person);
    } catch (Exception e) {

      logger.error("同步发件箱人员数据失败！", e);

    }

  }

  @Override
  public void removeInsideMailBox(Long key) throws ServiceException {
    Assert.notNull(key, "站内短消息key不允许为空！");
    // 检索该短消息是否在发件箱里面有记录
    PsnInsideMailBox mailBox = this.psnInsideMailBoxDao.get(key);
    List<InsideInbox> inboxs = mailBox.getInboxs();
    if (inboxs != null && inboxs.size() > 0) {
      for (InsideInbox inbox : inboxs) {
        this.insideInboxDao.delete(inbox.getId());
      }
    }
    this.psnInsideMailBoxDao.delete(mailBox);
  }

  @Override
  public void removeInviteMailBox(Long key) throws ServiceException {
    Assert.notNull(key, "站内短消息key不允许为空！");

    // 检索该短消息是否在发件箱里面有记录
    PsnInviteMailBox mailBox = this.psnInviteMailBoxDao.get(key);
    List<InviteInbox> inboxs = mailBox.getInboxs();
    if (inboxs != null && inboxs.size() > 0) {
      for (InviteInbox inbox : inboxs) {
        this.inviteInboxDao.delete(inbox.getId());
      }
    }
    this.psnInviteMailBoxDao.delete(mailBox);
  }

  @Override
  public PsnShareMailBox findPsnShareMailBoxById(Long mailId) throws ServiceException {
    PsnShareMailBox mailBox = psnShareMailBoxDao.get(mailId);
    // 重新获取站内分享发件内容_MJG_SCM-5910.
    mailBox = this.rebuildPsnShareMailBox(mailBox);
    return mailBox;
  }

  /**
   * 获取要更新冗余收件人数据的发件箱列表.
   */
  @Override
  public List<PsnInsideMailBox> getInsideMailBoxsByRecv(int maxSize) throws ServiceException {
    try {
      List<PsnInsideMailBox> mailBoxList = this.psnInsideMailBoxDao.getMailBoxsByRecv(maxSize);
      // 重新构建发件箱记录的内容_MJG_SCM-5910.
      if (CollectionUtils.isNotEmpty(mailBoxList)) {
        for (PsnInsideMailBox mailBox : mailBoxList) {
          mailBox = this.rebuildPsnInsideMailBox(mailBox);
        }
      }
      return mailBoxList;

    } catch (DaoException e) {
      logger.error("查询要要更新冗余收件人数据的发件箱列表时出错啦！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PsnInviteMailBox> getRequestMailBoxsByRecv(int maxSize) throws ServiceException {
    try {
      List<PsnInviteMailBox> mailBoxList = this.psnInviteMailBoxDao.getMailBoxsByRecv(maxSize);
      // 重新构建发件箱记录的内容_MJG_SCM-5910.
      if (CollectionUtils.isNotEmpty(mailBoxList)) {
        for (PsnInviteMailBox mailBox : mailBoxList) {
          mailBox = this.rebuildPsnInviteMailBox(mailBox);
        }
      }
      return mailBoxList;
    } catch (DaoException e) {
      logger.error("查询要要更新冗余收件人数据的发件箱列表时出错啦！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PsnShareMailBox> getShareMailBoxsByRecv(int maxSize) throws ServiceException {
    try {
      List<PsnShareMailBox> mailBoxList = this.psnShareMailBoxDao.getMailBoxsByRecv(maxSize);
      // 重新构建发件箱记录的内容_MJG_SCM-5910.
      if (CollectionUtils.isNotEmpty(mailBoxList)) {
        for (PsnShareMailBox mailBox : mailBoxList) {
          mailBox = this.rebuildPsnShareMailBox(mailBox);
        }
      }
      return mailBoxList;
    } catch (DaoException e) {
      logger.error("查询要要更新冗余收件人数据的发件箱列表时出错啦！", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 更新冗余的收件人信息.
   */
  @Override
  public void updateMailBoxRecv(Long mailId, String zhReceivers, String enReceivers, int type) throws ServiceException {
    try {
      switch (type) {
        case 1:// 短信
          this.psnInsideMailBoxDao.updateMailBoxRecv(mailId, zhReceivers, enReceivers);
          break;
        case 2:// 邀请
          this.psnInviteMailBoxDao.updateMailBoxRecv(mailId, zhReceivers, enReceivers);
          break;
        case 3:// 分享
          this.psnShareMailBoxDao.updateMailBoxRecv(mailId, zhReceivers, enReceivers);
          break;
      }
    } catch (DaoException e) {
      logger.error("更新收件人冗余数据时出错啦！", e);
    }

  }

  /**
   * 保存站内邀请发件箱记录.
   * 
   * @see Map 类型参数mailParam中的key值须与InviteMailBox的属性名保持一致.
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("javadoc")
  @Override
  public InviteMailBox saveInviteMailBox(Person person, Map<String, Object> mailParam) throws ServiceException {
    InviteMailBox inviteMailBox = new InviteMailBox();
    inviteMailBox.setSenderId(SecurityUtils.getCurrentUserId());
    inviteMailBox.setOptDate(new Date());
    inviteMailBox.setStatus(0);
    inviteMailBox.setSenderId(person.getPersonId());
    inviteMailBox.setPsnName(person.getName());
    inviteMailBox.setFirstName(person.getFirstName());
    inviteMailBox.setLastName(person.getLastName());
    inviteMailBox.setPsnHeadUrl(person.getAvatars());
    // 邀请类型.
    String inviteType = ObjectUtils.toString(mailParam.get("inviteType"));
    inviteMailBox.setInviteType(StringUtils.isBlank(inviteType) ? 0 : Integer.valueOf(inviteType));
    // 发送者基本信息.
    String jsonSenderInfo = ObjectUtils.toString(mailParam.get("senderInfo"));
    inviteMailBox.setSenderInfo(jsonSenderInfo);
    // 请求加入群组的人员id.
    String receiverId = ObjectUtils.toString(mailParam.get("invitePsnId"));
    inviteMailBox.setInvitePsnId(StringUtils.isBlank(receiverId) ? 0L : Long.valueOf(receiverId));
    inviteMailBox.setOptDate(new Date());
    inviteMailBoxDao.save(inviteMailBox);

    // 保存邀请发件记录内容_MJG_SCM-5910.
    InviteMailBoxCon inviteMailBoxCon = new InviteMailBoxCon();
    inviteMailBoxCon.setMailId(inviteMailBox.getMailId());
    inviteMailBoxCon.setTitleZh(ObjectUtils.toString(mailParam.get("title")));
    inviteMailBoxCon.setTitleEn(ObjectUtils.toString(mailParam.get("enTitle")));
    inviteMailBoxCon.setContent(ObjectUtils.toString(mailParam.get("content")));
    // 扩展信息.
    String extOtherInfo = ObjectUtils.toString(mailParam.get("extOtherInfo"));
    inviteMailBoxCon.setExtOtherInfo(extOtherInfo);
    inviteMailBoxConDao.saveMailBoxCon(inviteMailBoxCon);

    // @TODO
    // 此部分赋值逻辑需修改:验证测试系统短消息后要删除inside_mailbox对象下述几个属性的映射_MJG_2014-11-24
    inviteMailBox.setTitle(StringUtils.trimToEmpty(ObjectUtils.toString(mailParam.get("title"))));
    inviteMailBox.setEnTitle(StringUtils.trimToEmpty(ObjectUtils.toString(mailParam.get("enTitle"))));
    inviteMailBox.setExtOtherInfo(ObjectUtils.toString(mailParam.get("extOtherInfo")));
    inviteMailBox.setContent(StringUtils.trimToEmpty(ObjectUtils.toString(mailParam.get("content"))));
    return inviteMailBox;
  }

  /**
   * 保存邀请发件记录内容_MJG_SCM-5910.
   * 
   * @param inviteMailBox
   */
  @Override
  public void saveInviteMailboxCon(InviteMailBox inviteMailBox) {
    InviteMailBoxCon inviteMailBoxCon = new InviteMailBoxCon();
    inviteMailBoxCon.setTitleZh(inviteMailBox.getTitle());
    inviteMailBoxCon.setTitleEn(inviteMailBox.getEnTitle());
    inviteMailBoxCon.setContent(inviteMailBox.getContent());
    // 扩展信息.
    inviteMailBoxCon.setExtOtherInfo(inviteMailBox.getExtOtherInfo());
    inviteMailBoxCon.setMailId(inviteMailBox.getMailId());
    inviteMailBoxConDao.saveMailBoxCon(inviteMailBoxCon);
  }

  @Override
  public InsideMailBox saveInsideMailBox(Map<String, Object> map) throws ServiceException {
    try {
      InsideMailBox insideMail = new InsideMailBox();

      Long newPsnId = SecurityUtils.getCurrentUserId();
      if (newPsnId == null || newPsnId == 0L) {
        newPsnId = NumberUtils.toLong(ObjectUtils.toString(map.get("newPsnId")));
      }
      insideMail.setPsnId(newPsnId);
      insideMail.setMsgType(NumberUtils.createInteger(ObjectUtils.toString(map.get("msgType"))));
      insideMail.setTmpId(NumberUtils.createInteger(ObjectUtils.toString(map.get("tmpId"))));
      insideMail.setSendDate(new Date());
      insideMail.setStatus(0);
      insideMailBoxDao.save(insideMail);

      // 保存发件箱记录内容_MJG_SCM-5910.
      InsideMailBoxCon insideMailCon = new InsideMailBoxCon();
      insideMailCon.setMailId(insideMail.getMailId());
      insideMailCon.setTitleZh(StringUtils.trimToEmpty(ObjectUtils.toString(map.get("title"))));
      insideMailCon.setTitleEn(StringUtils.trimToEmpty(ObjectUtils.toString(map.get("enTitle"))));
      insideMailCon.setExtOtherInfo(ObjectUtils.toString(map.get("extOtherInfo")));
      insideMailCon.setContent(StringUtils.trimToEmpty(ObjectUtils.toString(map.get("content"))));
      insideMailBoxConDao.saveMailBoxCon(insideMailCon);

      // @TODO 此部分赋值逻辑需修改:
      // 验证测试系统短消息后要删除inside_mailbox对象下述几个属性的映射_MJG_2014-11-24
      insideMail.setTitle(StringUtils.trimToEmpty(ObjectUtils.toString(map.get("title"))));
      insideMail.setEnTitle(StringUtils.trimToEmpty(ObjectUtils.toString(map.get("enTitle"))));
      insideMail.setExtOtherInfo(ObjectUtils.toString(map.get("extOtherInfo")));
      insideMail.setContent(StringUtils.trimToEmpty(ObjectUtils.toString(map.get("content"))));
      return insideMail;
    } catch (Exception e) {
      logger.error("保存发件箱记录出现异常：", e);
      throw new ServiceException();
    }
  }

  /**
   * 保存发件箱记录内容_MJG_SCM-5910
   * 
   * @param insideMail
   */
  @Override
  @Deprecated
  public void saveInsideMailboxCon(InsideMailBox insideMail) {
    InsideMailBoxCon insideMailCon = new InsideMailBoxCon();
    insideMailCon.setTitleZh(insideMail.getTitle());
    insideMailCon.setTitleEn(insideMail.getEnTitle());
    insideMailCon.setExtOtherInfo(insideMail.getExtOtherInfo());
    insideMailCon.setContent(insideMail.getContent());
    insideMailCon.setMailId(insideMail.getMailId());
    insideMailBoxConDao.saveMailBoxCon(insideMailCon);
  }

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  @Override
  public void delInsideMailBoxByPsnId(Long psnId) {
    this.insideMailBoxDao.delMailBoxByPsnId(psnId);
  }

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  @Override
  public void delInviteMailBoxByPsnId(Long psnId) {
    this.inviteMailBoxDao.delMailBoxByPsnId(psnId);
  }

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  @Override
  public void delReqMailBoxByPsnId(Long psnId) {
    this.reqMailBoxDao.delMailBoxByPsnId(psnId);
  }

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  @Override
  public void delShareMailBoxByPsnId(Long psnId) {
    this.shareMailBoxDao.delMailBoxByPsnId(psnId);
  }

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  @Override
  public void delNoticeOutBoxByPsnId(Long psnId) {
    this.msgNoticeOutBoxDao.delMailBoxByPsnId(psnId);
  }

  @Override
  public Page<InsideMailBox> getInsideMailBoxByPage(Page<InsideMailBox> page, Message message) throws ServiceException {
    try {
      // scm-5731 先获取要更新收件人姓名的记录然后更新
      List<InsideMailBox> updateInsideMailBoxList = this.insideMailBoxDao.getUpdateInsideMailBoxList();
      if (CollectionUtils.isNotEmpty(updateInsideMailBoxList)) {
        List<Long> receiverIdList = null;
        List<Person> receiverList = null;
        JSONObject extOtherInfo = null;
        JSONArray receivers = null;
        String psnIdStr = null;
        Person person = null;
        for (InsideMailBox insideMailBox : updateInsideMailBoxList) {
          String zhReceivers = "";
          String enReceivers = "";
          // 重新构建发件箱记录的内容_MJG_SCM-5910.
          insideMailBox = this.rebuildInsideMailBox(insideMailBox);

          receiverIdList = new ArrayList<Long>();
          if (StringUtils.isNotBlank(insideMailBox.getExtOtherInfo())) {
            extOtherInfo = JSONObject.fromObject(StringUtils.replace(insideMailBox.getExtOtherInfo(), "/[\n]/ig", ""));
            if (extOtherInfo.containsKey("receivers")) {
              receivers = extOtherInfo.getJSONArray("receivers");
              for (int i = 0, size = receivers.size(); i < size; i++) {
                psnIdStr = receivers.getJSONObject(i).getString("psnId");
                if (StringUtils.isNotBlank(psnIdStr) && !receiverIdList.contains(Long.valueOf(psnIdStr))) {
                  receiverIdList.add(Long.valueOf(psnIdStr));

                }
              }
            }
          }

          if (CollectionUtils.isEmpty(receiverIdList)) {
            receiverIdList = this.insideInboxDao.queryReceiverIdByMail(insideMailBox.getMailId());
          }

          if (CollectionUtils.isNotEmpty(receiverIdList)) {
            receiverList = personManager.findPersonList(receiverIdList);
            if (receiverList.size() > 0) {
              for (Person p : receiverList) {
                zhReceivers += p.getName() + ",";
                enReceivers += p.getFirstName() + " " + p.getLastName() + ",";
              }
              this.insideMailBoxDao.updateReceiverNames(zhReceivers, enReceivers, insideMailBox.getMailId());
            }
          }
        }
      }

      page = this.insideMailBoxDao.queryInsideMailBoxByPage(page, message);

      List<InsideMailBox> insideMailBoxList = page.getResult();

      if (CollectionUtils.isNotEmpty(insideMailBoxList)) {
        List<Long> receiverIdList = null;
        List<Person> receiverList = null;
        JSONObject extOtherInfo = null;
        JSONArray receivers = null;
        String psnIdStr = null;
        Person person = null;
        String avatars = this.personManager.initPersonAvatars(null, null);
        for (InsideMailBox insideMailBox : insideMailBoxList) {
          // 重新构建发件箱记录的内容_MJG_SCM-5910.
          insideMailBox = this.rebuildInsideMailBox(insideMailBox);

          receiverIdList = new ArrayList<Long>();
          if (StringUtils.isNotBlank(insideMailBox.getExtOtherInfo())) {
            extOtherInfo = JSONObject.fromObject(StringUtils.replace(insideMailBox.getExtOtherInfo(), "/[\n]/ig", ""));
            if (extOtherInfo.containsKey("receivers")) {
              receivers = extOtherInfo.getJSONArray("receivers");
              for (int i = 0, size = receivers.size(); i < size; i++) {
                psnIdStr = receivers.getJSONObject(i).getString("psnId");
                if (StringUtils.isNotBlank(psnIdStr) && !receiverIdList.contains(Long.valueOf(psnIdStr))) {
                  receiverIdList.add(Long.valueOf(psnIdStr));

                }
              }
            }
          }

          if (CollectionUtils.isEmpty(receiverIdList)) {
            receiverIdList = this.insideInboxDao.queryReceiverIdByMail(insideMailBox.getMailId());
          }

          if (CollectionUtils.isNotEmpty(receiverIdList)) {
            receiverList = personManager.findPersonList(receiverIdList);
            if (receiverList.size() > 0) {
              person = receiverList.get(0);
              insideMailBox.setReceiverId(person.getPersonId());
              insideMailBox.setReceiverName(personManager.getPsnName(person));
              insideMailBox.setReceiverAvatars(person.getAvatars());
              insideMailBox.setReceiverNum(receiverList.size());
            }
          }
          if (insideMailBox.getReceiverId() == null) {
            insideMailBox.setReceiverAvatars(avatars);
          }
        }
      }


      return page;
    } catch (DaoException e) {
      logger.error("读取发件箱数据出现异常！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String getInsideMailBoxDetail(Long mailId) throws ServiceException {
    try {
      String msgHtmlContent = "";
      InsideMailBox insideMailBox = this.insideMailBoxDao.get(mailId);
      if (insideMailBox != null) {
        // 重新构建站内消息记录_MJG_SCM-5910.
        insideMailBox = this.rebuildInsideMailBox(insideMailBox);
        JSONObject tmpParam = new JSONObject();
        if (StringUtils.isNotBlank(insideMailBox.getExtOtherInfo())) {
          tmpParam = JSONObject.fromObject(StringUtils.replace(insideMailBox.getExtOtherInfo(), "/[\n]/ig", ""));
          if (tmpParam.containsKey("receivers")) {
            JSONArray receiverJson = JSONArray.fromObject(tmpParam.get("receivers"));
            List<Map<String, String>> psnDetailList = this.getPsnInfoViewByJson(receiverJson, "receiver");
            tmpParam.remove("receivers");
            tmpParam.accumulate("receiverList", psnDetailList);
            tmpParam.accumulate("receiverNum", psnDetailList.size());
          }

          if (tmpParam.containsKey("commenders")) {
            JSONArray commenderJson = JSONArray.fromObject(tmpParam.get("commenders"));
            List<Map<String, String>> psnDetailList = this.getPsnInfoViewByJson(commenderJson, "commender");
            tmpParam.remove("commenders");
            tmpParam.accumulate("commenderList", psnDetailList);
            tmpParam.accumulate("commenderNum", psnDetailList.size());
          }
        }
        // 兼容旧站内信
        if (!tmpParam.containsKey("receiverList")) {
          List<Long> receiverIdList = this.insideInboxDao.queryReceiverIdByMail(mailId);
          if (CollectionUtils.isNotEmpty(receiverIdList)) {
            List<Map<String, String>> psnDetailList = this.getPsnInfoView(receiverIdList, "receiver");
            tmpParam.accumulate("receiverList", psnDetailList);
            tmpParam.accumulate("receiverNum", psnDetailList.size());
          }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        tmpParam.accumulate("sendDate", dateFormat.format(insideMailBox.getSendDate()));
        tmpParam.accumulate("mailId", insideMailBox.getMailId());
        tmpParam.accumulate("des3MailId", ServiceUtil.encodeToDes3(insideMailBox.getMailId().toString()));
        tmpParam.accumulate("msgType", insideMailBox.getMsgType());
        // 替换title中的"为&quot;是因为IE8中标签的title属性值有"符号显示会有问题.
        tmpParam.accumulate("viewTitle", StringUtils.replace(insideMailBox.getViewTitle(), "\"", "&quot;"));
        tmpParam.accumulate("content", insideMailBox.getContent() == null ? "" : insideMailBox.getContent());
        tmpParam.accumulate("webDomain", sysDomainConst.getSnsDomain());
        tmpParam.accumulate("defaultAvatars", this.personManager.initPersonAvatars(null, null));
        try {
          InsideMailBox tmpMailBox = this.insideMailBoxDao.getPrev(mailId);
          if (tmpMailBox != null) {
            tmpParam.accumulate("preDes3MailId", ServiceUtil.encodeToDes3(tmpMailBox.getMailId().toString()));
          }
          tmpMailBox = this.insideMailBoxDao.getNext(mailId);
          if (tmpMailBox != null) {
            tmpParam.accumulate("nextDes3MailId", ServiceUtil.encodeToDes3(tmpMailBox.getMailId().toString()));
          }
        } catch (Exception e) {
          logger.error("短信-发件箱查看发件详情构造上一封和下一封时出现异常mailId=" + mailId, e);
        }
        // 获取模板路径
        MsgBoxTemplate msgBoxTemplate = this.msgBoxTemplateDao.get(insideMailBox.getTmpId());
        String msgTemplateName =
            msgBoxTemplate.getTmpName().replace("${locale}", LocaleContextHolder.getLocale().toString());
        msgHtmlContent = FreeMarkerTemplateUtils
            .processTemplateIntoString(msgFreemarkereConfiguration.getTemplate(msgTemplateName, ENCODING), tmpParam);
      }
      return msgHtmlContent;
    } catch (Exception e) {
      logger.error("短信-发件箱查看发件详情 出现异常mailId=" + mailId, e);
      throw new ServiceException();
    }
  }

  private List<Map<String, String>> getPsnInfoViewByJson(JSONArray psnJson, String createStr) throws ServiceException {
    String psnIdStr = null;
    List<Long> psnIdList = new ArrayList<Long>();
    for (int i = 0, size = psnJson.size(); i < size; i++) {
      psnIdStr = psnJson.getJSONObject(i).getString("psnId");
      if (StringUtils.isNotBlank(psnIdStr) && !psnIdList.contains(Long.valueOf(psnIdStr))) {
        psnIdList.add(Long.valueOf(psnIdStr));
      }
    }

    return this.getPsnInfoView(psnIdList, createStr);
  }

  private List<Map<String, String>> getPsnInfoView(List<Long> psnIdList, String createStr) throws ServiceException {
    List<Person> psnList = personManager.findPersonList(psnIdList);
    Map<String, String> map = null;
    List<Map<String, String>> psnDetailList = new ArrayList<Map<String, String>>();
    for (int i = 0, len = psnList.size(); i < len; i++) {
      map = new HashMap<String, String>();
      Person person = psnList.get(i);
      map.put(createStr + "Id", person.getPersonId().toString());
      map.put(createStr + "Des3Id", ServiceUtil.encodeToDes3(person.getPersonId().toString()));
      map.put(createStr + "Avatars", person.getAvatars());
      map.put(createStr + "Name", personManager.getPsnName(person));
      psnDetailList.add(map);
    }

    return psnDetailList;
  }

  /**
   * 站内消息发件箱.
   * 
   * @param mailId
   * @return
   */
  private PsnInsideMailBox rebuildPsnInsideMailBox(PsnInsideMailBox mailBox) {
    if (mailBox != null) {
      InsideMailBoxCon mailBoxCon = insideMailBoxConDao.getMailBoxCon(mailBox.getMailId());
      if (mailBoxCon != null) {
        mailBox.setContent(mailBoxCon.getContent());
        mailBox.setExtOtherInfo(mailBoxCon.getExtOtherInfo());
        mailBox.setTitle(mailBoxCon.getTitleZh());
        mailBox.setEnTitle(mailBoxCon.getTitleEn());
      }
    }
    return mailBox;
  }

  /**
   * 站内分享发件箱.
   * 
   * @param mailBox
   * @return
   */
  private PsnShareMailBox rebuildPsnShareMailBox(PsnShareMailBox mailBox) {
    if (mailBox != null) {
      ShareMailBoxCon mailboxCon = shareMailBoxConDao.getMailBoxCon(mailBox.getMailId());
      if (mailboxCon != null) {
        String title = ("zh_CN".equals(LocaleContextHolder.getLocale().toString())) ? mailboxCon.getTitleZh()
            : mailboxCon.getTitleEn();
        mailBox.setTitle(title);
      }
    }
    return null;
  }

  /**
   * 站内邀请发件箱.
   * 
   * @param mailBox
   * @return
   */
  private PsnInviteMailBox rebuildPsnInviteMailBox(PsnInviteMailBox mailBox) {
    if (mailBox != null) {
      InviteMailBoxCon mailboxCon = inviteMailBoxConDao.getMailBoxCon(mailBox.getMailId());
      if (mailboxCon != null) {
        mailBox.setTitle(mailboxCon.getTitleZh());
        mailBox.setEnTitle(mailboxCon.getTitleEn());
        mailBox.setExtOtherInfo(mailboxCon.getExtOtherInfo());
        mailBox.setContent(mailboxCon.getContent());
      }
    }
    return mailBox;
  }

  /**
   * 站内消息发件箱.
   * 
   * @param mailId
   * @return
   */
  private InsideMailBox rebuildInsideMailBox(InsideMailBox insideMailBox) {
    if (insideMailBox != null) {
      InsideMailBoxCon mailBoxCon = insideMailBoxConDao.getMailBoxCon(insideMailBox.getMailId());
      if (mailBoxCon != null) {
        insideMailBox.setContent(mailBoxCon.getContent());
        insideMailBox.setExtOtherInfo(mailBoxCon.getExtOtherInfo());
        insideMailBox.setTitle(mailBoxCon.getTitleZh());
        insideMailBox.setEnTitle(mailBoxCon.getTitleEn());
      }
    }
    return insideMailBox;
  }

  /**
   * 站内邀请发件箱.
   * 
   * @param mailBox
   * @return
   */
  private InviteMailBox rebuildInviteMailBox(InviteMailBox mailBox) {
    if (mailBox != null) {
      InviteMailBoxCon mailboxCon = inviteMailBoxConDao.getMailBoxCon(mailBox.getMailId());
      if (mailboxCon != null) {
        mailBox.setTitle(mailboxCon.getTitleZh());
        mailBox.setEnTitle(mailboxCon.getTitleEn());
        mailBox.setExtOtherInfo(mailboxCon.getExtOtherInfo());
        mailBox.setContent(mailboxCon.getContent());
      }
    }
    return mailBox;
  }

  /**
   * 获取站内信息发件内容.
   * 
   * @param mailId
   * @return
   */
  @Override
  public InsideMailBoxCon getInsideMailBoxCon(Long mailId) {
    return insideMailBoxConDao.getMailBoxCon(mailId);
  }

  /**
   * 获取站内邀请发件内容.
   * 
   * @param mailId
   * @return
   */
  @Override
  public InviteMailBoxCon getInviteMailBoxCon(Long mailId) {
    return inviteMailBoxConDao.getMailBoxCon(mailId);
  }

  /**
   * 清除站内消息发件内容_MJG_SCM-6097.
   * 
   * @param mailBox
   */
  @Override
  public void cleanInsideMailbox(InsideMailBox mailBox) {
    insideMailBoxDao.cleanInsideMailBox(mailBox);
  }

  /**
   * 清除站内邀请发件内容_MJG_SCM-6097.
   * 
   * @param mailBox
   */
  @Override
  public void cleanInviteMailbox(InviteMailBox mailBox) {
    inviteMailBoxDao.cleanInviteMailBox(mailBox);
  }
}
