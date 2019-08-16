package com.smate.center.batch.service.mail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.center.batch.constant.MessageConstant;
import com.smate.center.batch.dao.mail.FullTextInboxDao;
import com.smate.center.batch.dao.mail.InsideInboxConDao;
import com.smate.center.batch.dao.mail.InsideInboxDao;
import com.smate.center.batch.dao.mail.InsideMailBoxConDao;
import com.smate.center.batch.dao.mail.InviteBoxConDao;
import com.smate.center.batch.dao.mail.InviteInboxDao;
import com.smate.center.batch.dao.mail.InviteMailBoxConDao;
import com.smate.center.batch.dao.mail.InviteMailBoxDao;
import com.smate.center.batch.dao.mail.MsgBoxTemplateDao;
import com.smate.center.batch.dao.mail.MsgNoticeInBoxDao;
import com.smate.center.batch.dao.mail.PsnInsideInboxDao;
import com.smate.center.batch.dao.mail.PsnInviteInboxDao;
import com.smate.center.batch.dao.mail.PsnMsgNoticeInBoxDao;
import com.smate.center.batch.dao.mail.PsnReqInboxDao;
import com.smate.center.batch.dao.mail.PsnShareInboxDao;
import com.smate.center.batch.dao.mail.ReqInboxDao;
import com.smate.center.batch.dao.mail.ShareInboxDao;
import com.smate.center.batch.dao.mail.ShareMailBoxDao;
import com.smate.center.batch.dao.sns.psn.FriendTempDao;
import com.smate.center.batch.dao.sns.psn.ResReceiveResDao;
import com.smate.center.batch.dao.sns.pub.RecordRelationDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.InsideInbox;
import com.smate.center.batch.model.mail.InsideInboxCon;
import com.smate.center.batch.model.mail.InsideMailBox;
import com.smate.center.batch.model.mail.InsideMailBoxCon;
import com.smate.center.batch.model.mail.InviteBoxCon;
import com.smate.center.batch.model.mail.InviteInbox;
import com.smate.center.batch.model.mail.InviteMailBox;
import com.smate.center.batch.model.mail.InviteMailBoxCon;
import com.smate.center.batch.model.mail.MsgBoxTemplate;
import com.smate.center.batch.model.mail.PsnInsideInbox;
import com.smate.center.batch.model.mail.PsnInsideMailBox;
import com.smate.center.batch.model.mail.PsnInviteInbox;
import com.smate.center.batch.model.mail.PsnInviteMailBox;
import com.smate.center.batch.model.mail.PsnMessageNoticeInBox;
import com.smate.center.batch.model.mail.PsnMessageNoticeOutBox;
import com.smate.center.batch.model.mail.PsnReqInbox;
import com.smate.center.batch.model.mail.PsnShareInbox;
import com.smate.center.batch.model.mail.ShareMailBox;
import com.smate.center.batch.model.sns.pub.FriendTemp;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.center.batch.model.sns.pub.RecordRelation;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.center.batch.service.pub.FriendService;
import com.smate.center.batch.service.pub.GroupAdminInviteService;
import com.smate.center.batch.service.pub.GroupMemberManageService;
import com.smate.center.batch.service.pub.archiveFiles.ArchiveFilesService;
import com.smate.center.batch.service.pub.mq.MessageService;
import com.smate.center.batch.util.pub.JsonUtils;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 收件箱.
 * 
 * @author chenxiangrong
 * 
 */
@Service("inboxService")
@Transactional(rollbackFor = Exception.class)
public class InboxServiceImpl implements InboxService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private static final String ENCODING = "utf-8";
  @Autowired
  private PsnInsideInboxDao psnInsideInboxDao;
  @Autowired
  private PsnReqInboxDao psnReqInboxDao;
  @Autowired
  private InsideInboxDao insideInboxDao;
  @Autowired
  private InsideInboxConDao insideInboxConDao;
  @Autowired
  private InsideMailBoxConDao insideMailboxConDao;
  @Autowired
  private MsgNoticeInBoxDao msgNoticeInboxDao;
  @Autowired
  private PsnMsgNoticeInBoxDao psnMsgNoticeInboxDao;
  @Autowired
  private PsnShareInboxDao psnShareInboxDao;
  @Autowired
  private PsnInviteInboxDao psnInviteInboxDao;
  @Autowired
  private InviteInboxDao inviteInboxDao;
  @Autowired
  private InviteMailBoxDao inviteMailBoxDao;
  @Autowired
  private InviteMailBoxConDao inviteMailBoxConDao;
  @Autowired
  private ShareMailBoxDao shareMailBoxDao;
  @Autowired
  private InviteBoxConDao inviteBoxConDao;
  @Autowired
  private ShareInboxDao shareInboxDao;
  @Autowired
  private ReqInboxDao reqInboxDao;
  @Autowired
  private FriendTempDao friendTempDao;
  @Autowired
  private Configuration dynFreemarkerConfiguration;
  @Autowired
  private MessageService messageService;
  @Autowired
  private ArchiveFilesService archiveFilesService;
  @Autowired
  private GroupMemberManageService groupMemberManageService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private RecordRelationDao recordRelationDao;
  @Autowired
  private FullTextInboxDao fullTextInboxDao;
  @Autowired
  private MsgBoxTemplateDao msgBoxTemplateDao;
  @Autowired
  private ResReceiveResDao resReceiveResDao;
  @Autowired
  private Configuration msgFreemarkereConfiguration;
  @Autowired
  private FriendService friendService;
  @Autowired
  private PersonEmailManager personEmailManager;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private SysDomainConst sysDomainConst;
  @Autowired
  private GroupAdminInviteService groupAdminInviteService;

  @SuppressWarnings("rawtypes")
  @Override
  public Page<PsnInsideInbox> loadInsideInbox(Page<PsnInsideInbox> page, Map paramMap) throws ServiceException {
    try {
      return psnInsideInboxDao.getPsnInbox(page, paramMap);
    } catch (Exception e) {

      logger.error("查询psnId={}站内短消息收件箱数据失败！", SecurityUtils.getCurrentUserId());
      throw new ServiceException();
    }
  }

  @Override
  public Page<PsnInsideInbox> loadInsideInbox(Page<PsnInsideInbox> page, String searchKey) throws ServiceException {
    try {
      return psnInsideInboxDao.getPsnInbox(page, searchKey);
    } catch (Exception e) {

      logger.error("查询psnId={}站内短消息收件箱数据失败！", SecurityUtils.getCurrentUserId(), e);
      throw new ServiceException(e);
    }
  }

  /**
   * 加载站内通知 收信箱.
   * 
   * @author yangpeihai
   * @return
   */
  @SuppressWarnings("rawtypes")
  @Override
  public Page<PsnMessageNoticeInBox> loadMessageNoticeInBox(Page<PsnMessageNoticeInBox> page, Map paramMap)
      throws ServiceException {
    try {
      Page<PsnMessageNoticeInBox> pagePsnMsgNoticeInBox = psnMsgNoticeInboxDao.getPsnInbox(page, paramMap);
      List<PsnMessageNoticeInBox> lstPsnMsgNoticeInBox = pagePsnMsgNoticeInBox.getResult();
      String noticeTemplate = "DynMsg_noticeIn_Template_" + LocaleContextHolder.getLocale().toString() + ".ftl";
      Template template = dynFreemarkerConfiguration.getTemplate(noticeTemplate, ENCODING);
      JSONObject json = null;
      for (PsnMessageNoticeInBox inBox : lstPsnMsgNoticeInBox) {
        PsnMessageNoticeOutBox outBox = inBox.getMessageNoticeOutBox();
        json = JSONObject.fromObject(outBox);
        String extotherInfo = (String) json.get("extOtherInfo");
        if (extotherInfo != null) {
          extotherInfo = extotherInfo.replace("\\\\{", "{").replace("\\\\}", "}");
        }
        JSONObject extOtherInfo = JSONObject.fromObject(extotherInfo);
        json.remove("extOtherInfo");
        json.accumulate("extOtherInfo", extOtherInfo);
        // 科研在线单位删除人员消息，是用content字段来保存单位的中英文名称，用Json格式
        if (outBox.getMsgType() != null && outBox.getMsgType().intValue() == 6) {
          String content = (String) json.get("content");
          JSONObject contentJson = null;
          if (content != null) {
            content = content.replace("\\\\{", "{").replace("\\\\}", "}");
          }
          contentJson = JSONObject.fromObject(content);
          json.remove("content");
          json.accumulate("content", contentJson);
        }
        // 当前登录人是否为消息的发送者
        if (outBox.getSenderId().equals(SecurityUtils.getCurrentUserId())) {
          json.accumulate("isSender", "yes");
        } else {
          json.accumulate("isSender", "no");
        }
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, json);
        outBox.setReadOnlyTitle(content);
      }
      pagePsnMsgNoticeInBox.setResult(lstPsnMsgNoticeInBox);
      return pagePsnMsgNoticeInBox;
    } catch (Exception ex) {
      logger.error("查询psnId={}站内通知 收信箱数据时出错！", SecurityUtils.getCurrentUserId(), ex);
      throw new ServiceException(ex);

    }
  }

  /**
   * 站内搜索,加载站内通知 收件箱.
   * 
   * @param page
   * @param searchKey
   * @return
   * @throws ServiceException
   */
  @Override
  public Page<PsnMessageNoticeInBox> loadMessageNoticeInBox(Page<PsnMessageNoticeInBox> page, String searchKey)
      throws ServiceException {
    try {
      // return psnMsgNoticeInboxDao.getPsnInbox(page, searchKey);

      Page<PsnMessageNoticeInBox> pagePsnMsgNoticeInBox = psnMsgNoticeInboxDao.getPsnInbox(page);
      List<PsnMessageNoticeInBox> lstPsnMsgNoticeInBox = pagePsnMsgNoticeInBox.getResult();
      String noticeTemplate = "DynMsg_noticeIn_Template_" + LocaleContextHolder.getLocale().toString() + ".ftl";
      Template template = dynFreemarkerConfiguration.getTemplate(noticeTemplate, ENCODING);

      JSONObject json = null;
      ArrayList<PsnMessageNoticeInBox> searchLst = new ArrayList<PsnMessageNoticeInBox>();
      ArrayList<PsnMessageNoticeInBox> resulthLst = new ArrayList<PsnMessageNoticeInBox>();
      for (PsnMessageNoticeInBox inBox : lstPsnMsgNoticeInBox) {
        PsnMessageNoticeOutBox outBox = inBox.getMessageNoticeOutBox();
        json = JSONObject.fromObject(outBox);
        String extotherInfo = (String) json.get("extOtherInfo");
        if (extotherInfo != null) {
          extotherInfo = extotherInfo.replace("\\\\{", "{").replace("\\\\}", "}");
        }
        JSONObject extOtherInfo = JSONObject.fromObject(extotherInfo);
        json.remove("extOtherInfo");
        json.accumulate("extOtherInfo", extOtherInfo);

        if (outBox.getMsgType() != null && outBox.getMsgType().intValue() == 6) {
          String content = (String) json.get("content");
          JSONObject contentJson = null;
          if (content != null) {
            content = content.replace("\\\\{", "{").replace("\\\\}", "}");
          }
          contentJson = JSONObject.fromObject(content);
          json.remove("content");
          json.accumulate("content", contentJson);
        }

        // 当前登录人是否为消息的发送者
        if (outBox.getSenderId().equals(SecurityUtils.getCurrentUserId())) {
          json.accumulate("isSender", "yes");
        } else {
          json.accumulate("isSender", "no");
        }
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, json);
        outBox.setReadOnlyTitle(content);

        if (content.toLowerCase().indexOf(searchKey.toLowerCase()) > -1) {
          outBox.setReadOnlyTitle(content);
          searchLst.add(inBox);
        }
      }
      page.setTotalCount(searchLst.size());

      for (int i = (page.getPageNo() - 1) * page.getPageSize(); i < page.getPageNo() * page.getPageSize()
          && i < searchLst.size(); i++) {
        resulthLst.add(searchLst.get(i));
      }
      pagePsnMsgNoticeInBox.setResult(resulthLst);
      return pagePsnMsgNoticeInBox;

    } catch (Exception ex) {
      logger.error("查询psnId={}站内通知 收信箱数据时出错！", SecurityUtils.getCurrentUserId(), ex);
      throw new ServiceException(ex);
    }
  }

  /**
   * 删除站内通知.
   * 
   * @author yangpeihai
   * @param ids
   * @param type
   * @throws ServiceException
   */
  @Override
  public void deleteMessageNoticeById(String ids, String type) throws ServiceException {
    if (StringUtils.isEmpty(ids)) {
      logger.error("传入参数为空，错误！");
      throw new ServiceException();
    }
    try {
      for (String id : ids.split(",")) {
        Long key = Long.parseLong(id);
        updateInboxStatus(key, 2, type);

      }
    } catch (Exception e) {
      logger.error("删除邮件失败！", e);
      throw new ServiceException();
    }
  }

  @Override
  public PsnMessageNoticeOutBox getNoticeOutDetailById(Message msg) throws ServiceException {
    try {
      if (StringUtils.isNotEmpty(msg.getRecvId())) {
        String recvId = ServiceUtil.decodeFromDes3(msg.getRecvId());
        PsnMessageNoticeInBox inbox = psnMsgNoticeInboxDao.get(Long.parseLong(recvId));
        if (inbox.getStatus().equals(0)) {
          inbox.setStatus(1);
        }
        PsnMessageNoticeOutBox msgOutBox = inbox != null ? inbox.getMessageNoticeOutBox() : null;
        String noticeContentTemplate =
            "DynMsg_noticeInContent_Template_" + LocaleContextHolder.getLocale().toString() + ".ftl";
        String noticeTitleTemplate = "DynMsg_noticeIn_Template_" + LocaleContextHolder.getLocale().toString() + ".ftl";
        JSONObject json = JSONObject.fromObject(msgOutBox);

        JSONObject jsonTitle = JSONObject.fromObject(json.get("title"));
        json.remove("title");
        json.accumulate("title", jsonTitle);

        Template templateContent = dynFreemarkerConfiguration.getTemplate(noticeContentTemplate, ENCODING);
        Template templateTitle = dynFreemarkerConfiguration.getTemplate(noticeTitleTemplate, ENCODING);
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(templateContent, json);
        String title = FreeMarkerTemplateUtils.processTemplateIntoString(templateTitle, json);
        msgOutBox.setReadOnlyContent(content);
        msgOutBox.setReadOnlyTitle(title);
        return msgOutBox;
      } else {
        logger.error("参数为空！");
        throw new ServiceException();
      }
    } catch (Exception e) {
      logger.error("查看站内通知详细失败！", e);
      throw new ServiceException();
    }

  }

  @SuppressWarnings("rawtypes")
  @Override
  public Page<PsnReqInbox> loadReqInbox(Page<PsnReqInbox> page, Map paramMap) throws ServiceException {
    try {
      return psnReqInboxDao.getPsnInbox(page, paramMap);
    } catch (Exception e) {

      logger.error("查询psnId={}站内请求收件箱数据失败！", SecurityUtils.getCurrentUserId(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Page<PsnReqInbox> loadReqInbox(Page<PsnReqInbox> page, String searchKey) throws ServiceException {
    try {
      return psnReqInboxDao.getPsnInbox(page, searchKey);
    } catch (Exception e) {

      logger.error("查询psnId={}站内请求收件箱数据失败！", SecurityUtils.getCurrentUserId(), e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Page<PsnShareInbox> loadShareInbox(Page<PsnShareInbox> page, Map paramMap) throws ServiceException {
    try {
      return psnShareInboxDao.getPsnInbox(page, paramMap);
    } catch (Exception e) {

      logger.error("查询psnId={}站内推荐收件箱数据失败！", SecurityUtils.getCurrentUserId(), e);
      throw new ServiceException();
    }
  }

  @Override
  public Page<PsnShareInbox> loadShareInbox(Page<PsnShareInbox> page, String searchKey) throws ServiceException {
    try {
      return psnShareInboxDao.getPsnInbox(page, searchKey);
    } catch (Exception e) {

      logger.error("查询psnId={}站内推荐收件箱数据失败！", SecurityUtils.getCurrentUserId(), e);
      throw new ServiceException();
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Page<PsnInviteInbox> loadInviteInbox(Page<PsnInviteInbox> page, Map paramMap) throws ServiceException {
    try {
      Page<PsnInviteInbox> psnInviteInbox = psnInviteInboxDao.getPsnInbox(page, paramMap);

      List<PsnInviteInbox> lstInviteInbox = psnInviteInbox.getResult();
      String noticeTemplate = "DynMsg_Invite_Template_" + LocaleContextHolder.getLocale().toString() + ".ftl";
      Template template = dynFreemarkerConfiguration.getTemplate(noticeTemplate, ENCODING);
      JSONObject json = null;
      for (PsnInviteInbox inBox : lstInviteInbox) {
        PsnInviteMailBox inviteMailBox = inBox.getMailBox();
        // 重新构建邀请发件内容_MJG_SCM-5910.
        inviteMailBox = this.rebuildPsnInviteMailCon(inviteMailBox);
        try {
          json = JSONObject.fromObject(inviteMailBox);
          String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, json);
          inviteMailBox.setReadOnlyTitle(content);
          if (inviteMailBox.getSenderInfo() != null) {
            JSONObject jsonSenderInfo = JSONObject.fromObject(inviteMailBox.getSenderInfo());
            inviteMailBox.setTitolo(ObjectUtils.toString(jsonSenderInfo.get("titolo")));
            inviteMailBox.setPosition(ObjectUtils.toString(jsonSenderInfo.get("position")));
            inviteMailBox.setPrimaryUtil(ObjectUtils.toString(jsonSenderInfo.get("primaryUtil")));
            inviteMailBox.setDept(ObjectUtils.toString(jsonSenderInfo.get("dept")));
          }
          if (inviteMailBox.getExtOtherInfo() != null) {
            JSONObject jsonExtOtherInfo =
                JSONObject.fromObject(HtmlUtils.replaceBlank(inviteMailBox.getExtOtherInfo()));
            inviteMailBox.setGroupName(ObjectUtils.toString(jsonExtOtherInfo.get("groupName")));
            inviteMailBox.setGroupImgURL(ObjectUtils.toString(jsonExtOtherInfo.get("groupImgUrl")));
            inviteMailBox.setGroupUrl(ObjectUtils.toString(jsonExtOtherInfo.get("groupURL")));
            inviteMailBox.setGroupDesc(ObjectUtils.toString(jsonExtOtherInfo.get("groupDesc")));
          }

        } catch (Exception e) {

          logger.error("查询psnId={},mailId={}站内邀请短消息收件箱数据失败！",
              new Object[] {SecurityUtils.getCurrentUserId(), inviteMailBox.getMailId(), e});
          continue;

        }
      }
      return psnInviteInbox;
    } catch (Exception e) {

      logger.error("查询psnId={}站内邀请短消息收件箱数据失败！", SecurityUtils.getCurrentUserId(), e);
      throw new ServiceException();
    }
  }

  @Override
  public String getInviteInboxJsonStrById(Long[] ids) throws ServiceException {
    try {
      List<PsnInviteInbox> psnInviteInboxList = psnInviteInboxDao.getPsnInboxById(ids);
      List<Map<String, Object>> jsonList = new ArrayList<Map<String, Object>>();
      Map<String, Object> con = null;
      PsnInviteInbox psnInviteInbox = null;
      for (int i = 0, len = psnInviteInboxList.size(); i < len; i++) {
        con = new HashMap<String, Object>();
        psnInviteInbox = psnInviteInboxList.get(i);
        con.put("msgId", psnInviteInbox.getId());
        con.put("inviteType", psnInviteInbox.getMailBox().getInviteType());
        con.put("optStatus", psnInviteInbox.getOptStatus());
        jsonList.add(con);
      }
      JSONArray json = JSONArray.fromObject(jsonList);
      return json.toString();
    } catch (DaoException e) {
      logger.error("查询站内邀请短消息收件箱数据失败！", SecurityUtils.getCurrentUserId(), e);
      throw new ServiceException();
    }
  }

  @Override
  public Page<PsnInviteInbox> loadInviteInbox(Page<PsnInviteInbox> page, String searchKey) throws ServiceException {
    try {
      Page<PsnInviteInbox> psnInviteInbox = psnInviteInboxDao.getPsnInbox(page, searchKey);

      List<PsnInviteInbox> lstInviteInbox = psnInviteInbox.getResult();
      String noticeTemplate = "DynMsg_Invite_Template_" + LocaleContextHolder.getLocale().toString() + ".ftl";
      Template template = dynFreemarkerConfiguration.getTemplate(noticeTemplate, ENCODING);
      JSONObject json = null;
      ArrayList<PsnInviteInbox> searchLst = new ArrayList<PsnInviteInbox>();
      ArrayList<PsnInviteInbox> resulthLst = new ArrayList<PsnInviteInbox>();

      for (PsnInviteInbox inBox : lstInviteInbox) {
        // 重新构建站内短消息收件内容和发件内容_MJG_SCM-5910.
        inBox.setMailBox(this.rebuildPsnInviteMailCon(inBox.getMailBox()));
        json = JSONObject.fromObject(inBox.getMailBox());
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, json);
        if (content.indexOf(searchKey) > -1) {
          inBox.getMailBox().setReadOnlyTitle(content);
          searchLst.add(inBox);
        }
      }
      page.setTotalCount(searchLst.size());
      for (int i = (page.getPageNo() - 1) * page.getPageSize(); i < page.getPageNo() * page.getPageSize()
          && i < searchLst.size(); i++) {
        resulthLst.add(searchLst.get(i));
      }
      psnInviteInbox.setResult(resulthLst);

      return psnInviteInbox;

    } catch (Exception e) {

      logger.error("查询psnId={}站内邀请短消息收件箱数据失败！", SecurityUtils.getCurrentUserId(), e);
      throw new ServiceException();
    }
  }

  @Override
  public void deleteInboxMailById(String ids, String type) throws ServiceException {
    if (StringUtils.isEmpty(ids)) {

      logger.error("传入参数为空，错误！");
      throw new ServiceException();
    }
    try {
      for (String id : ids.split(",")) {
        Long key = Long.parseLong(id);
        updateInboxStatus(key, 2, type);
      }
      String cacheKey = MessageConstant.MSG_TIP_CACHE_KEY + SecurityUtils.getCurrentUserId();
      cacheService.remove(MessageConstant.MSG_TIP_CACHE_NAME, cacheKey);
    } catch (Exception e) {

      logger.error("删除邮件失败！", e);
      throw new ServiceException();
    }

  }

  /**
   * 忽略邮件.
   */
  @Override
  public void deleteInboxMailByIdx(String ids, String type, String rid, String rType) throws ServiceException {
    // TODO
  }

  @Override
  public void setUnReadStatus(String ids, String type) throws ServiceException {
    if (StringUtils.isEmpty(ids)) {

      logger.error("传入参数为空，错误！");
      throw new ServiceException();
    }
    try {
      for (String id : ids.split(",")) {
        Long key = Long.parseLong(id);
        updateInboxStatus(key, 0, type);

      }
    } catch (Exception e) {

      logger.error("设置未读邮件状态失败！", e);
      throw new ServiceException();
    }
  }

  @Override
  public void setReadStatus(String ids, String type) throws ServiceException {
    if (StringUtils.isEmpty(ids)) {

      logger.error("传入参数为空，错误！");
      throw new ServiceException();
    }
    try {
      for (String id : ids.split(",")) {
        Long key = Long.parseLong(id);
        updateInboxStatus(key, 1, type);

      }
    } catch (Exception e) {

      logger.error("设置已读邮件状态失败！", e);
      throw new ServiceException();
    }
  }

  @Override
  public void setMailStatus(String ids, String type, String statusType) throws ServiceException {
    if (StringUtils.isEmpty(ids)) {

      logger.error("传入参数为空，错误！");
      throw new ServiceException();
    }
    try {
      if ("unread".equalsIgnoreCase(statusType)) {
        setUnReadStatus(ids, type);
      } else if ("read".equalsIgnoreCase(statusType)) {
        setReadStatus(ids, type);
      }
      String cacheKey = MessageConstant.MSG_TIP_CACHE_KEY + SecurityUtils.getCurrentUserId();
      cacheService.remove(MessageConstant.MSG_TIP_CACHE_NAME, cacheKey);
    } catch (Exception e) {

      logger.error("设置邮件状态失败！", e);
      throw new ServiceException();
    }
  }

  @Override
  public void updateInboxStatus(Long id, Integer status, String type) throws ServiceException {
    try {
      if ("inside".equals(type)) { // 站内短消息
        // 更新站内短消息收件状态.
        this.insideInboxDao.updateInboxStatus(status, id);
      } else if ("invite".equals(type)) { // 站内邀请
        // 更新站内邀请收件状态.
        this.inviteInboxDao.updateInboxStatus(status, id);
      } else if ("ftrequest".equals(type)) {
        fullTextInboxDao.updateInboxStatus(id, status);
      } else if ("req".equals(type)) { // 站内请求
        // 更新站内请求收件状态.
        this.psnReqInboxDao.updateReqInboxStatus(status, id);
      } else if ("share".equals(type)) {// 站内推荐
        // 更新站内推荐收件状态.
        this.shareInboxDao.updateInboxStatus(status, id);
      } else if ("msgNoticeStyle".equals(type)) {// 站内通知
        // 更新站内通知收件状态.
        this.psnMsgNoticeInboxDao.updateInboxStatus(status, id);
      }
    } catch (Exception e) {

      logger.error("更改收件箱状态失败！", e);
      throw new ServiceException();
    }

  }


  @Override
  public PsnInviteMailBox getInviteMailDetailById(Message msg) throws ServiceException {

    try {
      if (StringUtils.isNotEmpty(msg.getRecvId())) {
        String recvId = ServiceUtil.decodeFromDes3(msg.getRecvId());
        PsnInviteInbox inbox = psnInviteInboxDao.get(Long.parseLong(recvId));
        if (inbox == null) {

          return null;
        }
        if (inbox.getStatus().equals(0)) {

          inbox.setStatus(1);
        }
        PsnInviteMailBox mailBox = inbox.getMailBox();
        mailBox.setOptStatus(inbox.getOptStatus());
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
  public String getLinkInfo(Message msg) throws ServiceException {

    try {
      String rtn = "";

      if (StringUtils.isNotEmpty(msg.getRecvId())) {
        String recvId = ServiceUtil.decodeFromDes3(msg.getRecvId());
        if ("inside".equalsIgnoreCase(msg.getType())) {

          PsnInsideInbox preInbox = psnInsideInboxDao.getPrevPsnInbox(msg.getSearchKey(), Long.parseLong(recvId));
          PsnInsideInbox nextInbox = psnInsideInboxDao.getNextPsnInbox(msg.getSearchKey(), Long.parseLong(recvId));
          rtn = "{\"action\":\"success\",\"prev\":" + (preInbox != null) + ",\"next\":" + (nextInbox != null) + "";
          if (preInbox != null) {

            rtn += ",\"prevId\":\"" + ServiceUtil.encodeToDes3(preInbox.getId().toString()) + "\"";
          }

          if (nextInbox != null) {

            rtn += ",\"nextId\":\"" + ServiceUtil.encodeToDes3(nextInbox.getId().toString()) + "\"";
          }

          rtn += "}";

        } else if ("share".equalsIgnoreCase(msg.getType())) {
          PsnShareInbox preInbox =
              this.psnShareInboxDao.getPrevPsnShareInbox(msg.getSearchKey(), Long.parseLong(recvId));
          PsnShareInbox nextInbox =
              this.psnShareInboxDao.getNextPsnShareInbox(msg.getSearchKey(), Long.parseLong(recvId));
          Map<String, String> map = new HashMap<String, String>();
          map.put("action", "success");
          map.put("prev", String.valueOf((preInbox != null)));
          map.put("next", String.valueOf((nextInbox != null)));
          if (preInbox != null) {
            map.put("prevRecvId", preInbox.getId().toString());
            map.put("prevMailId", preInbox.getMailBox().getMailId().toString());
            map.put("prevTitle", preInbox.getMailBox().getTitle());
          }

          if (nextInbox != null) {
            map.put("nextRecvId", nextInbox.getId().toString());
            map.put("nextMailId", nextInbox.getMailBox().getMailId().toString());
            map.put("nextTitle", nextInbox.getMailBox().getTitle());
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

  /**
   * 
   */
  @Override
  public String batchDisposeFrdInvite(Message[] messages, String type) throws ServiceException {
    // TODO
    return null;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public String disposeFrdInvite(Message message) throws ServiceException {
    // TODO
    return null;
  }

  /**
   * 获取人员简历列表.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  private String getPersonResumeUrl(Long psnId) throws ServiceException {
    try {
      return "/scmwebsns/resume/view?des3PsnId=" + ServiceUtil.encodeToDes3(psnId.toString());
    } catch (Exception e) {
      logger.error("读取用户个人链接失败！", e);
      return null;

    }

  }

  @Override
  public String batchDisposeGroupInvite(Message[] messages, String type) throws ServiceException {
    String rtn = "";
    String flag = "failure";
    if ("accept".equalsIgnoreCase(type)) {
      for (Message message : messages) {
        try {
          PsnInviteInbox inbox = this.psnInviteInboxDao.get(Long.parseLong(message.getRecvId()));
          if (inbox.getPsnName() == null || "".equals(inbox.getPsnName())) {
            inbox.setPsnName(inbox.getFirstName() + " " + inbox.getLastName());
          }
          Map<String, Object> inviteMap = new HashMap<String, Object>();
          inviteMap.put("inviteId", message.getInviteId());
          inviteMap.put("casUrl", message.getCasUrl());
          rtn = groupMemberManageService.toApprove(inviteMap);
          if ("success".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(1); // 接受
          } else if ("failure".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(3); // 操作失败
          } else if ("exist".equalsIgnoreCase(rtn)) {
            flag = rtn;
            inbox.setOptStatus(5); // 已经是群组成员
          } else if ("resolved".equalsIgnoreCase(rtn)) {
            flag = rtn;
            inbox.setOptStatus(6);// 已经处理
          }
          this.psnInviteInboxDao.save(inbox);
        } catch (Exception e) {
          PsnInviteInbox inbox = this.psnInviteInboxDao.get(Long.parseLong(message.getRecvId()));
          inbox.setOptStatus(3);
          this.psnInviteInboxDao.save(inbox);
          logger.error("处理群组邀请操作失败！", e);
        }
      }
    } else {
      for (Message message : messages) {
        try {
          PsnInviteInbox inbox = this.psnInviteInboxDao.get(Long.parseLong(message.getRecvId()));
          rtn = groupMemberManageService.toRefuse(message.getInviteId(), message.getGroupNodeId());
          if ("success".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(2); // 拒绝
          } else if ("exist".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(5); // 已经是群组成员
          } else if ("failure".equalsIgnoreCase(rtn) || "delete".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(3);// 操作失败
          }
          this.psnInviteInboxDao.save(inbox);
        } catch (Exception e) {
          PsnInviteInbox inbox = this.psnInviteInboxDao.get(Long.parseLong(message.getRecvId()));
          inbox.setOptStatus(3);
          this.psnInviteInboxDao.save(inbox);
          logger.error("处理群组邀请操作失败！", e);
        }
      }
    }
    if (!"failure".equals(flag)) {
      return "success";
    }
    return flag;
  }

  @Override
  public String disposeGroupInvite(Message message) throws ServiceException {
    String rtn = "";
    try {
      Long recvId = Long.parseLong(ServiceUtil.decodeFromDes3(message.getRecvId()));
      PsnInviteInbox inbox = this.psnInviteInboxDao.get(recvId);
      if (inbox.getMailBox().getSenderId() != -1) {
        if (inbox.getPsnName() == null || "".equals(inbox.getPsnName())) {
          inbox.setPsnName(inbox.getFirstName() + " " + inbox.getLastName());
        }
        if ("accept".equalsIgnoreCase(message.getActionKey())) {// 接受
          Map<String, Object> inviteMap = new HashMap<String, Object>();
          inviteMap.put("inviteId", message.getInviteId());
          inviteMap.put("casUrl", message.getCasUrl());
          rtn = groupMemberManageService.toApprove(inviteMap);
          if ("success".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(1); // 接受
          } else if ("failure".equalsIgnoreCase(rtn) || "delete".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(3); // 操作失败
          } else if ("exist".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(5); // 已经是群组成员
          } else if ("resolved".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(6);// 已经处理
          } else if ("empty".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(3);// 已经处理
            inbox.setStatus(3);// 邀请已失效(群组记录为空).
          }
        } else {// 拒绝
          rtn = groupMemberManageService.toRefuse(message.getInviteId(), message.getGroupNodeId());
          if ("success".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(2); // 拒绝
          } else if ("exist".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(5); // 已经是群组成员
          } else if ("failure".equalsIgnoreCase(rtn) || "delete".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(3);// 操作失败
          } else if ("empty".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(3);// 已经处理
            inbox.setStatus(3);// 邀请已失效(群组记录为空).
          }
        }
      } else {
        rtn = "inviteIsDel";
        inbox.setOptStatus(8);
      }

      this.psnInviteInboxDao.save(inbox);
      cacheService.remove(MessageConstant.MSG_TIP_CACHE_NAME,
          MessageConstant.MSG_TIP_CACHE_KEY + SecurityUtils.getCurrentUserId());
    } catch (Exception e) {
      Long recvId = Long.parseLong(ServiceUtil.decodeFromDes3(message.getRecvId()));
      PsnInviteInbox inbox = this.psnInviteInboxDao.get(recvId);
      inbox.setOptStatus(3);
      this.psnInviteInboxDao.save(inbox);
      logger.error("处理群组邀请操作失败！", e);
    }
    return rtn;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public String batchDisposeApplyJoinGroup(Message[] messages, String type) throws ServiceException {
    String rtn = "";
    String flag = "failure";
    if ("accept".equalsIgnoreCase(type)) {
      for (Message message : messages) {
        try {
          Long recvId = Long.parseLong(message.getRecvId());
          PsnInviteInbox inbox = this.psnInviteInboxDao.get(recvId);
          if (inbox.getPsnName() == null || "".equalsIgnoreCase(inbox.getPsnName())) {
            inbox.setPsnName(inbox.getFirstName() + " " + inbox.getLastName());
          }
          Map ctxMap = new HashMap();
          ctxMap.put("inviteId", message.getInviteId());
          ctxMap.put("casUrl", message.getCasUrl());
          rtn = groupMemberManageService.dealToJoinApprove(ctxMap);

          if ("success".equalsIgnoreCase(rtn)) {
            flag = rtn;
            inbox.setOptStatus(1); // 接受
          } else if ("failure".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(3); // 操作失败
          } else if ("exist".equalsIgnoreCase(rtn)) {
            flag = rtn;
            inbox.setOptStatus(5); // 已经是群组成员
          } else if ("resolved".equalsIgnoreCase(rtn)) {
            flag = rtn;
            inbox.setOptStatus(6);// 已经处理
          }
          this.psnInviteInboxDao.save(inbox);
          this.recordRelationDao.deleteRecord(inbox.getId());
        } catch (Exception e) {
          PsnInviteInbox inbox = this.psnInviteInboxDao.get(Long.parseLong(message.getRecvId()));
          inbox.setStatus(3);
          this.psnInviteInboxDao.save(inbox);
          logger.error("批量处理群组邀请操作失败!", e);
        }
      }
    } else {
      for (Message message : messages) {
        try {
          Long recvId = Long.parseLong(message.getRecvId());
          PsnInviteInbox inbox = this.psnInviteInboxDao.get(recvId);
          rtn = groupMemberManageService.toJoinRefuseApply(message.getInviteId(), message.getGroupNodeId());
          if ("success".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(2); // 拒绝
          } else if ("exist".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(5); // 已经是群组成员
          } else if ("failure".equalsIgnoreCase(rtn) || "delete".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(3);// 操作失败
          } else if ("resolved".equalsIgnoreCase(rtn)) {
            inbox.setOptStatus(6);// 已经处理
          }
          this.psnInviteInboxDao.save(inbox);
          this.recordRelationDao.deleteRecord(inbox.getId());
        } catch (Exception e) {
          PsnInviteInbox inbox = this.psnInviteInboxDao.get(Long.parseLong(message.getRecvId()));
          inbox.setStatus(3);
          this.psnInviteInboxDao.save(inbox);
          logger.error("批量处理群组邀请操作失败!", e);
        }
      }
    }
    if (!"failure".equals(flag)) {
      return "success";
    }
    return flag;
  }

  @Override
  public String disposeApplyJoinGroup(Message message) throws ServiceException {
    String rtn = "";
    try {
      Long recvId = Long.parseLong(ServiceUtil.decodeFromDes3(message.getRecvId()));
      PsnInviteInbox inbox = this.psnInviteInboxDao.get(recvId);
      if (inbox.getPsnName() == null || "".equals(inbox.getPsnName())) {
        inbox.setPsnName(inbox.getFirstName() + " " + inbox.getLastName());
      }
      if ("accept".equalsIgnoreCase(message.getActionKey())) {// 接受
        Map<String, Object> inviteMap = new HashMap<String, Object>();
        inviteMap.put("inviteId", message.getInviteId());
        inviteMap.put("casUrl", message.getCasUrl());
        rtn = groupMemberManageService.dealToJoinApprove(inviteMap);
        if ("success".equalsIgnoreCase(rtn)) {
          inbox.setOptStatus(1); // 接受
        } else if ("failure".equalsIgnoreCase(rtn) || "delete".equalsIgnoreCase(rtn)) {
          inbox.setOptStatus(3); // 操作失败
        } else if ("exist".equalsIgnoreCase(rtn)) {
          inbox.setOptStatus(5); // 已经是群组成员
        } else if ("resolved".equalsIgnoreCase(rtn)) {
          inbox.setOptStatus(6);// 已经处理
        } else if ("inviteIsDel".equalsIgnoreCase(rtn)) {
          inbox.setOptStatus(8);// 邀请已失效
        }
      } else {// 拒绝
        rtn = groupMemberManageService.toJoinRefuseApply(message.getInviteId(), message.getGroupNodeId());
        if ("success".equalsIgnoreCase(rtn)) {
          inbox.setOptStatus(2); // 拒绝
        } else if ("exist".equalsIgnoreCase(rtn)) {
          inbox.setOptStatus(5); // 已经是群组成员
        } else if ("failure".equalsIgnoreCase(rtn) || "delete".equalsIgnoreCase(rtn)) {
          inbox.setOptStatus(3);// 操作失败
        } else if ("resolved".equalsIgnoreCase(rtn)) {
          inbox.setOptStatus(6);// 已经处理
        } else if ("inviteIsDel".equalsIgnoreCase(rtn)) {
          inbox.setOptStatus(8);// 邀请已失效
        }
      }
      this.psnInviteInboxDao.save(inbox);
      this.recordRelationDao.deleteRecord(inbox.getId());
      cacheService.remove(MessageConstant.MSG_TIP_CACHE_NAME,
          MessageConstant.MSG_TIP_CACHE_KEY + SecurityUtils.getCurrentUserId());
    } catch (Exception e) {
      Long recvId = Long.parseLong(ServiceUtil.decodeFromDes3(message.getRecvId()));
      PsnInviteInbox inbox = this.psnInviteInboxDao.get(recvId);
      inbox.setOptStatus(3);
      this.psnInviteInboxDao.save(inbox);
      logger.error("处理群组邀请操作失败！", e);
    }
    return rtn;
  }

  @Override
  public InviteInbox getInviteInbox(Long psnId, Long mailId) throws ServiceException {

    try {
      return inviteInboxDao.getInviteInbox(psnId, mailId);
    } catch (Exception e) {
      logger.error("查询收件箱数据失败！", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 根据收件箱ID检索记录.
   * 
   * @param inboxId
   * @return
   * @throws ServiceException
   */
  @Override
  public InviteInbox getInviteInboxById(Long inboxId) throws ServiceException {
    try {
      return inviteInboxDao.get(inboxId);
    } catch (Exception e) {
      logger.error("查询收件箱数据失败！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveInviteInbox(InviteInbox inbox) throws ServiceException {
    inviteInboxDao.save(inbox);

  }

  @Override
  public void syncPersonInfo(Person person) throws ServiceException {

    try {
      this.inviteInboxDao.updatePersonInfo(person);
      this.reqInboxDao.updatePersonInfo(person);
      this.msgNoticeInboxDao.updatePersonInfo(person);

    } catch (Exception e) {

      logger.error("同步收件箱人员数据失败！", e);

    }
  }

  /**
   * 判断是否已经有了相同的邀请.
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type 1.好友邀请；2.群组邀请 3.请求加入群组
   * @return
   */
  @Override
  public boolean isRepeatInvite(Long recId, Long senderId, String type) {
    return inviteInboxDao.isRepeatInvite(recId, senderId, type);
  }

  /**
   * 根据接收者Id和被邀请加入组的组名，更改收信箱的信息状态为已读.
   * 
   * @param psnId 个人Id
   * @param groupName 组名
   */
  @Override
  public void updateInvitedGroupInboxStatus(Long recId, String groupName) {
    try {
      this.insideInboxDao.updateInvitedGroupInboxStatusToReaded(recId, groupName);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }

  /**
   * 忽略重复的邀请.
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type 1.好友邀请；
   */
  @Override
  public void ignoreRepeatInvite(Long recId, Long senderId, String type) throws ServiceException {
    inviteInboxDao.ignoreRepeatInvite(recId, senderId, type);
  }

  /**
   * 忽略重复的邀请.
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type group//2.群组邀请 3.请求加入群组
   * @param groupName 群组名称.
   */
  @Override
  public void ignoreRepeatInvite(Long recId, Long senderId, String type, Long groupId) throws ServiceException {

    if (isRepeatInvite(recId, senderId, type, groupId)) {
      inviteInboxDao.ignoreRepeatInvite(recId, senderId, type, groupId);

    }
  }

  /**
   * 判断是否已经有了相同的邀请.
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type group//2.群组邀请 3.请求加入群组
   * @return
   */
  @Override
  public boolean isRepeatInvite(Long recId, Long senderId, String type, Long groupId) throws ServiceException {

    return this.inviteInboxDao.isRepeatInvite(recId, senderId, type, groupId);
  }

  /**
   * 根据传过来的数据构造message数组 在根据inviteType来决定调用什么处理方法.
   * 
   */
  @Override
  public String batchControl(String[] ids, String[] groupNodeIds, String[] inviteIds, String[] groupIds,
      String[] inviteTypes, String type, String casUrl) throws ServiceException {
    try {
      int typeZero = 0;
      int typeOne = 0;
      int typeTwo = 0;
      Message ms;
      Message[] messageTypeZero = null, messageTypeOne = null, messageTypeTwo = null;
      String rtn = "";
      String[] result = new String[3];
      for (int i = 0; i < inviteTypes.length; i++) {
        if ("0".equals(inviteTypes[i])) {
          typeZero++;
        } else if ("1".equals(inviteTypes[i])) {
          typeOne++;
        } else if ("2".equals(inviteTypes[i])) {
          typeTwo++;
        }
      }
      if (typeZero > 0) {
        messageTypeZero = new Message[typeZero];
      }
      if (typeOne > 0) {
        messageTypeOne = new Message[typeOne];
      }
      if (typeTwo > 0) {
        messageTypeTwo = new Message[typeTwo];
      }
      for (int i = 0; i < ids.length; i++) {
        ms = new Message();
        ms.setRecvId(ids[i]);
        ms.setInviteType(Integer.parseInt(inviteTypes[i]));
        ms.setCasUrl(casUrl);
        if ("0".equals(inviteTypes[i])) {
          messageTypeZero[--typeZero] = ms;
        } else if ("1".equals(inviteTypes[i])) {
          ms.setInviteId(Long.parseLong(inviteIds[i]));
          ms.setGroupNodeId(Integer.parseInt(groupNodeIds[i]));
          messageTypeOne[--typeOne] = ms;
        } else if ("2".equals(inviteTypes[i])) {
          ms.setInviteId(Long.parseLong(inviteIds[i]));
          ms.setGroupNodeId(Integer.parseInt(groupNodeIds[i]));
          ms.setGroupId(Long.parseLong(groupIds[i]));
          messageTypeTwo[--typeTwo] = ms;
        }
      }
      if (messageTypeZero != null) {
        rtn = this.batchDisposeFrdInvite(messageTypeZero, type);
        result[0] = rtn;
      }
      if (messageTypeOne != null) {
        rtn = this.batchDisposeGroupInvite(messageTypeOne, type);
        result[1] = rtn;
      }
      if (messageTypeTwo != null) {
        rtn = this.batchDisposeApplyJoinGroup(messageTypeTwo, type);
        result[2] = rtn;
      }
      cacheService.remove(MessageConstant.MSG_TIP_CACHE_NAME,
          MessageConstant.MSG_TIP_CACHE_KEY + SecurityUtils.getCurrentUserId());
      for (String element : result) {
        if (!"failure".equals(element)) {
          return "success";
        }
      }
      return "failure";

    } catch (Exception e) {
      logger.error("批量处理异常", e);
      return "failure";
    }

  }

  @Override
  public void setStatusOnInviteInbox(String rtn, String actionType, RecordRelation rel) throws ServiceException {

    try {
      PsnInviteInbox psnInviteInbox = this.psnInviteInboxDao.getPsnInviteInboxById(rel.getInviteId());
      if (psnInviteInbox != null) {
        if ("accept".equalsIgnoreCase(actionType)) {
          if ("success".equalsIgnoreCase(rtn)) {
            psnInviteInbox.setOptStatus(1);
          }
        } else if ("refuse".equalsIgnoreCase(actionType)) {
          if ("success".equalsIgnoreCase(rtn)) {
            psnInviteInbox.setOptStatus(2);
          }
        } else if ("neglect".equalsIgnoreCase(actionType)) {
          psnInviteInbox.setOptStatus(7);
        }
        this.psnInviteInboxDao.save(psnInviteInbox);
      }
    } catch (Exception e) {
      logger.error("处理群组邀请操作失败！", e);
    }

  }

  @Override
  public void setStatusOnInviteInbox(String rtn, String actionType, Long invitePsnId) throws ServiceException {
    try {
      RecordRelation rel = this.recordRelationDao.findRecord(invitePsnId, 1);
      setStatusOnInviteInbox(rtn, actionType, rel);
      this.recordRelationDao.delete(rel);
    } catch (Exception e) {
      logger.error("处理群组邀请操作失败！", e);
    }

  }

  /**
   * 根据请求参数对当前邀请进行处理.
   * 
   * @param paramMap
   * @return 0-成功；1-邀请已被其他用户绑定；2-群组已被删除.
   * @throws ServiceException
   */
  @Override
  public Integer dealInviteBusiness(String key, Map<String, String> paramMap) throws ServiceException {
    Integer result = 0;
    // 获取请求参数.
    Long currentPsnId = SecurityUtils.getCurrentUserId();// 当前用户ID.
    Integer currentNodeId = SecurityUtils.getCurrentUserNodeId();// 当前用户节点ID.
    String des3InviteId = null;
    String des3InboxId = null;
    String des3MailId = null;
    String verifyEmail = null;
    if (FriendMailService.FRIEND_INVITE_KEY.equals(key)) {// 好友邀请.
      des3InviteId = paramMap.get(FriendMailService.INVITATION_PARAM_INVITEID);
      des3InboxId = paramMap.get(FriendMailService.INVITATION_PARAM_INBOXID);
      des3MailId = paramMap.get(FriendMailService.INVITATION_PARAM_MAILID);
    } else if (GroupAdminInviteService.GROUP_INVITE_KEY.equals(key)) {// 群组邀请.
      des3InviteId = paramMap.get(GroupAdminInviteService.INVITATION_PARAM_INVITEID);
      des3InboxId = paramMap.get(GroupAdminInviteService.INVITATION_PARAM_INBOXID);
      des3MailId = paramMap.get(GroupAdminInviteService.INVITATION_PARAM_MAILID);
      verifyEmail = paramMap.get("verifyEmail");
    }
    // 处理邀请.
    Person person = personManager.getPersonByRecommend(currentPsnId);// 当前登录用户.
    result = this.dealSameNodeInvite(des3MailId, des3InboxId, des3InviteId, person, currentNodeId);
    if (0 == result) {// 如果邀请处理成功且当前用户的邮件地址与邀请的邮件地址相同，则将其置为已确认_2013-05-28_SCM-2545.
      if (person.getEmail().equals(verifyEmail)) {
        // 更新邮件地址的确认状态.
        personEmailManager.updateLoginEmailVerifyStatus(person.getPersonId(), person.getEmail());
      }
    }
    return result;
  }

  /**
   * 修复SCM-2545问题时架空此方法，待确定有无其他调用，可删掉_MJG_2013-05-28.
   */
  @Override
  public Integer initFrdInviteInbox(String des3MailId, String des3InboxId, Integer nodeId, String des3InviteId)
      throws ServiceException {
    Integer result = 0;

    Long currentPsnId = SecurityUtils.getCurrentUserId();
    Integer currentNodeId = SecurityUtils.getCurrentUserNodeId();// 当前用户节点ID.
    Person person = personManager.getPerson(currentPsnId);// 当前登录用户.
    if (currentNodeId.equals(nodeId)) {// 如果是相同节点则更新inviteInbox及friendTemp
      result = this.dealSameNodeInvite(des3MailId, des3InboxId, des3InviteId, person, currentNodeId);
    } else {// 如果是不同节点则同步记录.
      result = this.dealDiffNodeInvite(des3MailId, des3InboxId, des3InviteId, person, currentNodeId);
    }
    return result;
  }

  /**
   * 处理相同节点的邀请.
   * 
   * @param des3MailId
   * @param des3InboxId
   * @param des3InviteId
   * @param person
   * @param currentNodeId
   * @return
   * @throws ServiceException
   */
  @Deprecated
  private Integer dealSameNodeInvite(String des3MailId, String des3InboxId, String des3InviteId, Person person,
      Integer currentNodeId) throws ServiceException {
    Long mailId = new Long(ServiceUtil.decodeFromDes3(des3MailId));// 邀请发件箱的主键ID.
    Long inboxId = new Long(ServiceUtil.decodeFromDes3(des3InboxId));// 邀请收件箱的主键ID.
    Long inviteId = new Long(ServiceUtil.decodeFromDes3(des3InviteId));// 邀请记录的主键ID.
    Long currentPsnId = person.getPersonId();
    InviteMailBox mailBox = this.inviteMailBoxDao.get(mailId);
    InviteInbox inbox = null;
    if (inboxId != null && inboxId != 0L) {
      inbox = this.getInviteInboxById(inboxId);
    } else {// 如果参数收件箱ID为空，则根据发件箱ID查询对应的收件箱记录<psnId的值对应MessageServiceImpl.java类中的dealInviteMessageByEmail方法>.
      inbox = this.getInviteInbox(-1L, mailId);
    }
    if (inbox != null) {
      // 如果邀请信息已被绑定且不是当前用户，则返回1，给出提示“邀请链接已经失效了或已被其他用户使用！”.
      if (inbox.getPsnId() != null && inbox.getPsnId() >= 0
          && !String.valueOf(inbox.getPsnId()).equals(String.valueOf(currentPsnId))) {
        return 1;
      }
    } else {// 如果没有未绑定的收件箱记录，则同样返回1，给出提示“邀请链接已经失效了或已被其他用户使用！”_2012-12-26_SCM-1415.
      return 1;
    }
    // 邀请类型为0-好友请求.
    if (mailBox.getInviteType() != null && mailBox.getInviteType() == 0) {
      // 忽略重复之前的好友邀请
      this.removalRepeatInvite(currentPsnId, mailBox.getSenderId());
      FriendTemp friendTemp = this.friendTempDao.get(inviteId);
      if (friendTemp != null) {
        friendTemp.setTempPsnId(currentPsnId);
        friendTempDao.save(friendTemp);
      }
    } else {
      mailBox = rebuildInviteMailCon(mailBox);
      Integer groupDealResult =
          groupAdminInviteService.dealGroupInvite(mailBox, inbox, currentPsnId, inviteId, currentNodeId);
      // 如果群组不存在则更新收件箱记录.
      if (GroupAdminInviteService.GROUP_EXISTS_NONE == groupDealResult) {
        inbox.setStatus(3);// 3-无效.
        inbox.setOptStatus(6);// 6-已经处理.
        inviteInboxDao.save(inbox);
        return groupDealResult;
      }
    }
    // 更新收件箱记录(保存收件人).
    if (inbox != null && inbox.getPsnId() <= 0) {
      inbox.setPsnId(currentPsnId);
      inbox.setPsnName(person.getName());
      inbox.setFirstName(person.getFirstName());
      inbox.setLastName(person.getLastName());
      inviteInboxDao.save(inbox);
    }
    return 0;
  }

  /**
   * 处理不同节点的邀请.
   * 
   * @param des3MailId
   * @param des3InboxId
   * @param person
   * @param des3InviteId
   * @param nodeId
   * @return
   * @throws ServiceException
   */
  @Deprecated
  private Integer dealDiffNodeInvite(String des3MailId, String des3InboxId, String des3InviteId, Person person,
      Integer nodeId) throws ServiceException {
    Long currentPsnId = person.getPersonId();
    // 节点不同需要同步
    Map<String, String> map = new HashMap<String, String>();
    map.put("des3MailId", des3MailId);
    map.put("des3InboxId", des3InboxId);
    map.put("des3InviteId", des3InviteId);
    map.put("currentPsnId", currentPsnId.toString());
    map.put("name", person.getName());
    map.put("firstName", person.getFirstName());
    map.put("lastName", person.getLastName());
    InviteMailBox mailBox = this.syncInitFrdInviteInbox(map);
    // 邀请类型为0-好友邀请,则沿用原逻辑，类型为1-群组邀请，则无后续操作.
    if (mailBox.getInviteType() != null && mailBox.getInviteType() == 0) {
      mailBox.setMailId(null);
      this.inviteMailBoxDao.save(mailBox);
      // 忽略重复之前的好友邀请
      this.removalRepeatInvite(currentPsnId, mailBox.getSenderId());
      InviteInbox inbox = new InviteInbox();
      inbox.setPsnId(currentPsnId);
      inbox.setMailId(mailBox.getMailId());
      inbox.setPsnName(person.getName());
      inbox.setFirstName(person.getFirstName());
      inbox.setLastName(person.getLastName());
      inbox.setPsnHeadUrl(person.getAvatars());
      this.inviteInboxDao.save(inbox);
    }
    return 0;
  }

  @Override
  @Deprecated
  public InviteMailBox syncInitFrdInviteInbox(Map<String, String> paramMap) throws ServiceException {

    Long mailId = new Long(ServiceUtil.decodeFromDes3(paramMap.get("des3MailId")));
    Long inboxId = new Long(ServiceUtil.decodeFromDes3(paramMap.get("des3InboxId")));
    Long inviteId = new Long(ServiceUtil.decodeFromDes3(paramMap.get("des3InviteId")));
    Long currentPsnId = new Long(paramMap.get("currentPsnId"));
    // 更新 friendTemp表和inviteInbox表
    try {
      InviteMailBox mailBox = this.inviteMailBoxDao.get(mailId);
      if (mailBox != null) {
        mailBox = this.rebuildInviteMailCon(mailBox);
      }
      // 邀请类型为1-群组邀请.
      if (mailBox.getInviteType() != null && mailBox.getInviteType() == 1) {
        return this.syncInitGrpInviteInbox(mailBox, paramMap);
      } else {
        // 忽略重复之前的好友邀请
        this.removalRepeatInvite(currentPsnId, mailBox.getSenderId());
        InviteInbox inbox = this.inviteInboxDao.get(inboxId);
        inbox.setPsnId(currentPsnId);
        inbox.setPsnName(paramMap.get("name"));
        inbox.setFirstName(paramMap.get("firstName"));
        inbox.setLastName(paramMap.get("lastName"));
        inviteInboxDao.save(inbox);

        FriendTemp friendTemp = this.friendTempDao.get(inviteId);
        friendTemp.setTempPsnId(currentPsnId);
        friendTempDao.save(friendTemp);
        return this.inviteMailBoxDao.get(mailId);
      }

    } catch (Exception e) {
      logger.error("设置inviteInbox出错", e);
    }

    return null;
  }

  /**
   * 同步不同节点的邀请发件箱和收件箱的信息.
   * 
   * @param paramMap
   * @return
   * @throws ServiceException
   */
  @Override
  @Deprecated
  public InviteMailBox syncInitGrpInviteInbox(InviteMailBox mailBox, Map<String, String> paramMap)
      throws ServiceException {
    Long mailId = new Long(ServiceUtil.decodeFromDes3(paramMap.get("des3MailId")));
    Long inboxId = new Long(ServiceUtil.decodeFromDes3(paramMap.get("des3InboxId")));
    Long inviteId = new Long(ServiceUtil.decodeFromDes3(paramMap.get("des3InviteId")));
    Long currentPsnId = new Long(paramMap.get("currentPsnId"));
    // 更新 friendTemp表和inviteInbox表
    try {
      // 清除重复邀请记录.
      this.clearRepeatRecord(inviteId, currentPsnId, mailBox);
      InviteInbox inbox = null;
      if (inboxId != null && inboxId != 0L) {
        inbox = this.inviteInboxDao.get(inboxId);
      }
      // 如果参数收件箱ID为空，则根据发件箱ID查询对应的收件箱记录<psnId的值对应MessageServiceImpl.java类中的dealInviteMessageByEmail方法>.
      else {
        try {
          inbox = this.inviteInboxDao.getInviteInbox(-1L, mailId);
        } catch (DaoException e) {
          e.printStackTrace();
        }
      }
      if (inbox != null) {
        inbox.setPsnId(currentPsnId);
        inbox.setPsnName(paramMap.get("name"));
        inbox.setFirstName(paramMap.get("firstName"));
        inbox.setLastName(paramMap.get("lastName"));
        inbox.setPsnHeadUrl(paramMap.get("avatars"));
        inviteInboxDao.save(inbox);
      }

      return this.inviteMailBoxDao.get(mailId);
    } catch (Exception e) {
      logger.error("设置inviteInbox出错", e);
    }

    return null;
  }

  @Override
  public void removalRepeatInvite(Long sendPsnId, Long senderPsnId) throws ServiceException {

    boolean isRepeat = this.isRepeatInvite(sendPsnId, senderPsnId, "friend");
    if (isRepeat) {// 如果重复则忽略之前的好友请求
      this.ignoreRepeatInvite(sendPsnId, senderPsnId, "friend");
    }

  }

  /**
   * 忽略重复的群组请求.
   * 
   * @param sendPsnId 接收人ID.
   * @param senderPsnId 发送人ID.
   * @param groupName 群组名称.
   * @throws ServiceException
   */
  private void removalRepeatInvite(Long sendPsnId, Long senderPsnId, Long groupId) throws ServiceException {
    this.ignoreRepeatInvite(sendPsnId, senderPsnId, "group", groupId);
  }

  /**
   * 清除重复的邀请.
   * 
   * @param inviteId
   * @param currentPsnId
   * @param mailBox
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  private void clearRepeatRecord(Long inviteId, Long currentPsnId, InviteMailBox mailBox) throws ServiceException {
    // 邀请类型为0-好友请求.
    if (mailBox.getInviteType() != null && mailBox.getInviteType() == 0) {
      // 忽略重复之前的好友邀请
      this.removalRepeatInvite(currentPsnId, mailBox.getSenderId());
      FriendTemp friendTemp = this.friendTempDao.get(inviteId);
      friendTemp.setTempPsnId(currentPsnId);
      friendTempDao.save(friendTemp);
    } else {
      // 解析获取群组名称.
      String extOtherInfo = mailBox.getExtOtherInfo();
      if (extOtherInfo != null && !"".equals(extOtherInfo)) {
        Map infoMap = JsonUtils.getMap4Json(extOtherInfo);
        if (infoMap.containsKey("groupId")) {
          String groupName = (String) infoMap.get("groupName");
          Long groupId = (Long) infoMap.get("groupId");
          // 忽略重复的群组邀请.
          if (groupName != null && !"".equals(groupName)) {
            this.removalRepeatInvite(currentPsnId, mailBox.getSenderId(), groupId);
          }
        }
      }
    }
  }

  /**
   * 分享邮件查看，更新收件箱状态.
   */
  @Override
  public void updateEmailShareStatus(Long senderId, Long receiverId, String des3Sid) throws ServiceException {
    try {
      List<ShareMailBox> shareMailBoxList = this.shareMailBoxDao.getShareMailBoxBySid(senderId, des3Sid);
      List<Long> mailIdList = new ArrayList<Long>();
      for (int i = 0, size = shareMailBoxList.size(); i < size; i++) {
        mailIdList.add(shareMailBoxList.get(i).getMailId());
      }

      if (mailIdList.size() > 0) {
        this.shareInboxDao.updateStatus(mailIdList, receiverId);
      }
    } catch (DaoException e) {
      logger.error("邮件更新收件箱状态时出错啦！", e);
    }
  }

  /**
   * 拼装收件箱记录参数.
   * 
   * @param param
   * @param recvPerson
   * @return
   * @throws ServiceException
   */
  @Override
  @Deprecated
  public InviteInbox saveInviteInbox(Map<String, Object> param, Person recvPerson) throws ServiceException {
    String mailId = ObjectUtils.toString(param.get("mailId"));
    Long mailid = (StringUtils.isBlank(mailId)) ? 0L : Long.valueOf(mailId);
    String status = ObjectUtils.toString(param.get("status"));
    Integer stat = (StringUtils.isBlank(status)) ? 0 : Integer.valueOf(status);
    String optStatus = ObjectUtils.toString(param.get("optStatus"));
    Integer optstatus = (StringUtils.isBlank(optStatus)) ? 0 : Integer.valueOf(optStatus);

    Long psnId = recvPerson.getPersonId();
    // 如果已存储相应收件箱记录，则对该记录进行更新.
    InviteInbox inviteInbox = this.getInviteInbox(psnId, mailid);
    if (inviteInbox == null) {
      inviteInbox = new InviteInbox(psnId, mailid, stat, optstatus);
    }
    inviteInbox.setFirstName(recvPerson.getFirstName());
    inviteInbox.setLastName(recvPerson.getLastName());
    inviteInbox.setPsnName(recvPerson.getName());
    inviteInbox.setPsnHeadUrl(recvPerson.getAvatars());
    inviteInboxDao.save(inviteInbox);
    return inviteInbox;
  }

  /**
   * 保存站内邀请的收件箱及相关记录.
   * 
   * @param invitePsnId
   * @param inviteInboxId
   * @param psnId
   * @throws ServiceException
   */
  @Override
  public void saveInviteRelationRecord(Map<String, Object> inviteInParam, Person recvPerson,
      Map<String, Object> dataParam) throws ServiceException {
    // 保存收件箱记录.
    InviteInbox inbox = this.saveInviteInbox(inviteInParam, recvPerson);

    String invitePsnIdString = ObjectUtils.toString(dataParam.get("invitePsnId"));
    String mailContent = ObjectUtils.toString(dataParam.get("mailContent"));
    // 保存记录引用关系.
    if (StringUtils.isNotBlank(invitePsnIdString)) {
      Long invitePsnId = Long.valueOf(invitePsnIdString);
      String psnIdStr = ObjectUtils.toString(dataParam.get("psnId"));
      Long psnId = (StringUtils.isBlank(psnIdStr)) ? 0L : Long.valueOf(psnIdStr);
      RecordRelation rel = this.recordRelationDao.findRecord(invitePsnId, 1, psnId);
      if (rel == null) {
        rel = new RecordRelation(invitePsnId, inbox.getId(), 1);
        this.recordRelationDao.save(rel);
      } else {
        this.setStatusOnInviteInbox("success", "neglect", rel);
        rel.setInviteId(inbox.getId());
        this.recordRelationDao.save(rel);
      }
    }
    // 保存站内邀请内容记录.
    InviteBoxCon inviteBoxCon = new InviteBoxCon(inbox.getId(), "", mailContent);
    inviteBoxConDao.save(inviteBoxCon);
  }

  @Override
  public List<PsnInviteInbox> findInviteReg(Long psnId, Integer inviteType, int maxSize) throws ServiceException {
    try {
      List<PsnInviteInbox> result = this.psnInviteInboxDao.findInviteReg(psnId, inviteType, maxSize);
      for (PsnInviteInbox inBox : result) {
        PsnInviteMailBox inviteMailBox = inBox.getMailBox();

        if (inviteMailBox != null) {
          Person person = personManager.getPerson(inviteMailBox.getSenderId());
          if (person != null) {
            inviteMailBox.setTitolo(person.getViewTitolo());
          }
          if (inviteMailBox.getSenderInfo() != null) {
            JSONObject jsonSenderInfo = JSONObject.fromObject(inviteMailBox.getSenderInfo());
            // inviteMailBox.setTitolo(ObjectUtils.toString(jsonSenderInfo.get("titolo")));
            inviteMailBox.setPosition(ObjectUtils.toString(jsonSenderInfo.get("position")));
            inviteMailBox.setPrimaryUtil(ObjectUtils.toString(jsonSenderInfo.get("primaryUtil")));
            inviteMailBox.setDept(ObjectUtils.toString(jsonSenderInfo.get("dept")));
          }
        }

      }
      return result;
    } catch (DaoException e) {
      logger.error("查询某个人的某种邀请信息出错啦！PsnId=" + psnId + "inviteType=" + inviteType + "maxSize=" + maxSize, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveInsideInbox(Long recieverId, InsideMailBox insideMailBox) throws ServiceException {
    try {
      InsideInbox insideInbox = new InsideInbox();
      insideInbox.setPsnId(recieverId);
      insideInbox.setMailId(insideMailBox.getMailId());
      insideInbox.setSenderId(insideMailBox.getPsnId());
      insideInbox.setMsgType(insideMailBox.getMsgType());
      insideInbox.setTmpId(insideMailBox.getTmpId() + 1);
      insideInbox.setTitle(insideMailBox.getTitle());
      insideInbox.setEnTitle(insideMailBox.getEnTitle());
      insideInbox.setContent(insideMailBox.getContent());
      insideInbox.setRecieveDate(new Date());
      insideInbox.setStatus(0);
      insideInboxDao.save(insideInbox);

      // 保存站内消息收件箱记录_MJG_SCM-5910.
      InsideInboxCon insideInboxCon = new InsideInboxCon();
      insideInboxCon.setInboxId(insideInbox.getId());
      insideInboxCon.setTitleZh(insideMailBox.getTitle());
      insideInboxCon.setTitleEn(insideMailBox.getEnTitle());
      if (insideMailBox.getExtOtherInfo() != null) {
        JSONObject jsonObject = JSONObject.fromObject(insideMailBox.getExtOtherInfo());
        jsonObject.remove("receivers");
        insideInboxCon.setExtOtherInfo(jsonObject.isEmpty() ? null : jsonObject.toString());
      }
      insideInboxCon.setContent(insideMailBox.getContent());
      insideInboxConDao.saveInboxCon(insideInboxCon);
    } catch (Exception e) {
      logger.error("保存站内消息收件箱记录出现异常：", e);
      throw new ServiceException(e);
    }

  }

  /**
   * 保存站内消息收件箱记录_MJG_SCM-5910。
   * 
   * @param insideInbox
   */
  @Override
  public void saveInsideInboxCon(InsideInbox insideInbox) {
    InsideInboxCon insideInboxCon = new InsideInboxCon();
    insideInboxCon.setTitleZh(insideInbox.getTitle());
    insideInboxCon.setTitleEn(insideInbox.getEnTitle());
    insideInboxCon.setExtOtherInfo(insideInbox.getExtOtherInfo());
    insideInboxCon.setContent(insideInbox.getContent());
    insideInboxCon.setInboxId(insideInbox.getId());
    insideInboxConDao.saveInboxCon(insideInboxCon);
  }

  @Override
  public Page<InsideInbox> getInsideInboxByPage(Page<InsideInbox> page, Map<String, String> paramMap)
      throws ServiceException {
    try {
      page = this.insideInboxDao.queryInsideInboxByPage(page, paramMap);
      List<InsideInbox> insideInboxList = page.getResult();

      if (CollectionUtils.isNotEmpty(insideInboxList)) {
        String avatars = this.personManager.initPersonAvatars(null, null);
        for (InsideInbox insideInbox : insideInboxList) {
          // 重新获取收件箱内容数据_MJG_SCM-5910.
          insideInbox = this.rebuildInsideInbox(insideInbox);
          if (insideInbox.getSenderId() != -1) {
            Person person = personManager.getPsnNameAndAvatars(insideInbox.getSenderId());
            if (person != null) {
              insideInbox.setSenderName(personManager.getPsnName(person));
              insideInbox.setSenderAvatars(person.getAvatars());
            }
          } else {
            insideInbox.setSenderAvatars(avatars);
          }
        }
      }
      return page;
    } catch (Exception e) {
      logger.error(String.format("查询psnId=%s站内收件箱数据出现异常：", SecurityUtils.getCurrentUserId()), e);
      throw new ServiceException();
    }
  }

  @Override
  public String getInsideInboxDetail(Long id) throws ServiceException {
    try {
      String insideInboxDetail = null;
      InsideInbox insideInbox = insideInboxDao.get(id);
      if (insideInbox != null) {
        // 重新构建收件记录_MJG_SCM-5910.
        insideInbox = this.rebuildInsideInbox(insideInbox);
        JSONObject jsonObject = new JSONObject();
        if (insideInbox.getExtOtherInfo() != null) {
          jsonObject = JSONObject.fromObject(StringUtils.replace(insideInbox.getExtOtherInfo(), "/[\n]/ig", ""));
        }
        if (jsonObject.containsKey("commenders")) {
          JSONArray commenderJson = JSONArray.fromObject(jsonObject.get("commenders"));
          List<Map<String, String>> psnDetailList = this.getPsnInfoView(commenderJson, "commender");
          jsonObject.remove("commenders");
          jsonObject.accumulate("commenderList", psnDetailList);
          jsonObject.accumulate("commenderNum", psnDetailList.size());
        }

        // 获取消息发送人信息
        Person person = personManager.getPsnNameAndAvatars(insideInbox.getSenderId());
        if (person != null) {
          jsonObject.accumulate("senderId", person.getPersonId().toString());
          jsonObject.accumulate("senderDes3Id", ServiceUtil.encodeToDes3(person.getPersonId().toString()));
          jsonObject.accumulate("senderName", personManager.getPsnName(person));
          jsonObject.accumulate("senderAvatars", person.getAvatars());
        } else {
          jsonObject.accumulate("senderAvatars", this.personManager.initPersonAvatars(null, null));
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        jsonObject.accumulate("receiverDate", dateFormat.format(insideInbox.getRecieveDate()));

        // 获取消息接收人信息
        person = personManager.getPsnNameAndAvatars(SecurityUtils.getCurrentUserId());
        if (person != null) {
          jsonObject.accumulate("psnName", personManager.getPsnName(person));
        }

        jsonObject.accumulate("id", id);
        jsonObject.accumulate("des3Id", ServiceUtil.encodeToDes3(id.toString()));
        jsonObject.accumulate("msgType", insideInbox.getMsgType());
        jsonObject.accumulate("viewTitle", StringUtils.replace(insideInbox.getViewTitle(), "\"", "&quot;"));
        jsonObject.accumulate("content", (insideInbox.getContent() == null) ? "" : insideInbox.getContent());
        jsonObject.accumulate("webDomain", sysDomainConst.getSnsDomain());
        try {
          InsideInbox tmpInbox = this.insideInboxDao.getPrev(id);
          if (tmpInbox != null) {
            jsonObject.accumulate("preDes3Id", ServiceUtil.encodeToDes3(tmpInbox.getId().toString()));
          }
          tmpInbox = this.insideInboxDao.getNext(id);
          if (tmpInbox != null) {
            jsonObject.accumulate("nextDes3Id", ServiceUtil.encodeToDes3(tmpInbox.getId().toString()));
          }
        } catch (Exception e) {
          logger.error("短信-收件箱查看收件详情构造上一封和下一封时出现异常id=" + id, e);
        }

        MsgBoxTemplate msgBoxTemplate = this.msgBoxTemplateDao.get(insideInbox.getTmpId());
        if (msgBoxTemplate != null) {
          insideInboxDetail = FreeMarkerTemplateUtils.processTemplateIntoString(msgFreemarkereConfiguration.getTemplate(
              msgBoxTemplate.getTmpName().replace("${locale}", LocaleContextHolder.getLocale().toString()), ENCODING),
              jsonObject);
        }
        // 将收件箱记录设为已读状态
        this.insideInboxDao.updateInboxStatus(1, id);
        String cacheKey = MessageConstant.MSG_TIP_CACHE_KEY + SecurityUtils.getCurrentUserId();
        cacheService.remove(MessageConstant.MSG_TIP_CACHE_NAME, cacheKey);
      }
      return insideInboxDetail;
    } catch (Exception e) {
      logger.error("短信-收件箱查看收件详情出现异常id=" + id, e);
      throw new ServiceException();
    }
  }

  @Override
  public String getInsideInboxContent(Long id) throws ServiceException {
    JSONObject returnJson = new JSONObject();
    try {
      InsideInbox insideInbox = insideInboxDao.get(id);
      if (insideInbox != null) {
        // 重新构建收件内容记录_MJG_SCM-5910.
        insideInbox = rebuildInsideInbox(insideInbox);
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotBlank(insideInbox.getExtOtherInfo())) {
          jsonObject = JSONObject.fromObject(StringUtils.replace(insideInbox.getExtOtherInfo(), "/[\n]/ig", ""));
          if (jsonObject.containsKey("commenders")) {
            JSONArray commenderJson = JSONArray.fromObject(jsonObject.get("commenders"));
            List<Map<String, String>> psnDetailList = this.getPsnInfoView(commenderJson, "commender");
            jsonObject.remove("commenders");
            jsonObject.accumulate("commenderList", psnDetailList);
          }
        }
        jsonObject.accumulate("webDomain", sysDomainConst.getSnsDomain());
        jsonObject.accumulate("msgType", insideInbox.getMsgType());
        jsonObject.accumulate("content", insideInbox.getContent());

        String content = FreeMarkerTemplateUtils.processTemplateIntoString(
            msgFreemarkereConfiguration.getTemplate(
                "msgbox_tmp_inside_content_" + LocaleContextHolder.getLocale().toString() + ".ftl", ENCODING),
            jsonObject);
        returnJson.accumulate("content", content);

        String fileList = FreeMarkerTemplateUtils.processTemplateIntoString(
            msgFreemarkereConfiguration.getTemplate("msgbox_tmp_inside_attachFile.ftl", ENCODING), jsonObject);
        returnJson.accumulate("fileList", fileList);
      }
    } catch (Exception e) {
      logger.error("短信-收件箱查看收件详情出现异常id=" + id, e);
    }
    return returnJson.toString();
  }

  private List<Map<String, String>> getPsnInfoView(JSONArray psnJson, String createStr) throws ServiceException {
    String psnIdStr = null;
    List<Long> psnIdList = new ArrayList<Long>();
    for (int i = 0, size = psnJson.size(); i < size; i++) {
      psnIdStr = psnJson.getJSONObject(i).getString("psnId");
      if (StringUtils.isNotBlank(psnIdStr) && !psnIdList.contains(Long.valueOf(psnIdStr))) {
        psnIdList.add(Long.valueOf(psnIdStr));
      }
    }

    List<Person> psnList = personManager.findPersonList(psnIdList);
    Map<Long, Integer> isFriendMap = friendService.isPsnFirend(SecurityUtils.getCurrentUserId(), psnIdList);
    Map<String, String> map = null;
    List<Map<String, String>> psnDetailList = new ArrayList<Map<String, String>>();
    for (int i = 0, len = psnList.size(); i < len; i++) {
      map = new HashMap<String, String>();
      Person person = psnList.get(i);
      map.put(createStr + "Id", person.getPersonId().toString());
      map.put(createStr + "Des3Id", ServiceUtil.encodeToDes3(person.getPersonId().toString()));
      map.put(createStr + "Avatars", person.getAvatars());
      map.put(createStr + "Name", personManager.getPsnName(person));
      map.put("isFriend", ObjectUtils.toString(isFriendMap.get(person.getPersonId())));
      psnDetailList.add(map);
    }

    return psnDetailList;
  }

  /**
   * 获取站内信息内容.
   * 
   * @param inboxId
   * @return
   */
  @Override
  public InsideInboxCon getInsideInboxCon(Long inboxId) {
    return insideInboxConDao.getInsideInboxCon(inboxId);
  }

  /**
   * 获取站内邀请内容.
   * 
   * @param inboxId
   * @return
   */
  @Override
  public InviteBoxCon getInviteInboxCon(Long inboxId) {
    return inviteBoxConDao.get(inboxId);
  }

  /**
   * 清除站内消息收件箱内容记录_MJG_SCM-6097.
   * 
   * @param id
   */
  @Override
  public void cleanInsideInboxCon(InsideInbox inbox) {
    insideInboxDao.cleanInsideInbox(inbox);
  }

  /**
   * 获取短信收件记录.
   * 
   * @param inboxId
   * @return
   */
  private InsideInbox rebuildInsideInbox(InsideInbox insideInbox) {
    // 重新构建收件记录内容.
    if (insideInbox != null) {
      InsideInboxCon insideInboxCon = insideInboxConDao.getInsideInboxCon(insideInbox.getId());
      if (insideInboxCon != null) {
        insideInbox.setContent(insideInboxCon.getContent());
        insideInbox.setExtOtherInfo(insideInboxCon.getExtOtherInfo());
        insideInbox.setTitle(insideInboxCon.getTitleZh());
        insideInbox.setEnTitle(insideInboxCon.getTitleEn());
      }
    }
    return insideInbox;
  }

  /**
   * 站内短信发件箱.
   * 
   * @param mailBox
   * @return
   */
  private PsnInsideMailBox rebuildPsnInsideMailBox(PsnInsideMailBox mailBox) {
    if (mailBox != null) {
      InsideMailBoxCon mailboxCon = insideMailboxConDao.getMailBoxCon(mailBox.getMailId());
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
   * 站内邀请发件记录.
   * 
   * @param mailBox
   * @return
   */
  private InviteMailBox rebuildInviteMailCon(InviteMailBox mailBox) {
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
   * 站内邀请发件记录.
   * 
   * @param mailBox
   * @return
   */
  private PsnInviteMailBox rebuildPsnInviteMailCon(PsnInviteMailBox mailBox) {
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
}
