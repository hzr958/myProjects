package com.smate.center.mail.service;

import com.smate.center.mail.dao.MailBlacklistDao;
import com.smate.center.mail.dao.MailConnectErrorDao;
import com.smate.center.mail.dao.MailReturnedRecordDao;
import com.smate.center.mail.dao.MailSenderDao;
import com.smate.center.mail.model.MailBlacklist;
import com.smate.center.mail.model.MailConnectError;
import com.smate.center.mail.model.MailSender;
import com.smate.center.mail.mongodb.model.MailReturnedRecord;
import com.sun.mail.imap.IMAPFolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮件发送失败监控服务
 * 
 * @author zzx
 *
 */
@Service("mailSendFailedMonService")
@Transactional(rollbackFor = Exception.class)
public class MailSendFailedMonServiceImpl implements MailSendFailedMonService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailSenderDao mailSenderDao;
  @Autowired
  private MailReturnedRecordDao mailReturnedRecordDao;
  @Autowired
  private MailBlacklistDao mailBlacklistDao;
  @Autowired
  private MailConnectErrorDao mailConnectErrorDao;

  @Override
  public List<MailSender> findSenderList() {
    return mailSenderDao.findAll();
  }

  /**
   * 处理邮件信息
   * 
   * @param one
   * @throws Exception
   */
  @Override
  public void handleMessage(Message one, String account) throws Exception {
    MailReturnedRecord m = new MailReturnedRecord();
    // 邮件内容解析
    Object obj = getMailContent(one);
    buildMailContent(obj, m);

    // 邮件接收时间
    Date date = one.getSentDate();
    // 邮件主题
    String subject = one.getSubject();
    m.setAccount(account);
    m.setSendDate(date);
    m.setSubject(subject);
    m.setCreateDate(new Date());

    if (!isBounceReply(subject) && !isBounceReply(m.getContent())) {// 包含自动回复的不保存
      if (isBounce(m.getContent())) {// 邮件内容处理-退信-加入黑名单
        handleBounce(m);
      } else {
        m.setAddress(findMail(m.getContent()));
      }
      m.setType("0");
      mailReturnedRecordDao.save(m);
    }
    // 标记邮件为已读
    Flags flags = one.getFlags();
    if (!flags.contains(Flags.Flag.SEEN)) {
      one.setFlag(Flags.Flag.SEEN, true);
    }
  }

  private Object getMailContent(Message one) throws Exception {
    Object obj = null;
    try {
      obj = one.getContent();
    } catch (MessagingException e) {
      // handling the bug
      if (one instanceof MimeMessage && "Unable to load BODYSTRUCTURE".equalsIgnoreCase(e.getMessage())) {
        obj = new MimeMessage((MimeMessage) one).getContent();
      }
    }
    return obj;
  }

  /**
   * 退信处理 1、邮件地址不存在，则加入黑名单； 2、TODO
   */
  private void handleBounce(MailReturnedRecord m) {
    Pattern pattern = Pattern.compile("收件人邮件地址(.+?)不存在|收件方域名不存在|所属域名不存在|被所在服务商暂时停用|收件地址不存在");
    Matcher matcher = pattern.matcher(m.getContent());
    if (matcher.find()) {
      String mail = findMail(m.getContent());
      if (mail != null) {
        m.setAddress(mail);
        addBlacklist(mail);
      }
    }
  }

  private void addBlacklist(String mail) {
    MailBlacklist email = mailBlacklistDao.getByEmail(mail);
    if (email == null) {
      MailBlacklist mb = new MailBlacklist();
      mb.setEmail(mail);
      mb.setMsg("后台任务-退信-添加到黑名单");
      mb.setUpdateDate(new Date());
      mb.setStatus(0);
      mb.setType(0);
      mailBlacklistDao.save(mb);
    }
  }

  /**
   * 是否是退信
   * 
   * @param content
   * @return
   */
  private boolean isBounce(String content) {
    if (content.indexOf("退信") != -1) {
      return true;
    }
    return false;
  }

  /**
   * 是否是自动回复
   * 
   * @param content
   * @return
   */
  private boolean isBounceReply(String content) {
    if (content.indexOf("自动回复") != -1) {
      return true;
    }
    return false;
  }

  /**
   * 截取邮箱
   * 
   * @param content
   * @return
   */
  private String findMail(String content) {
    String str = "";
    if (content.indexOf("收件人") != -1) {
      str = content.substring(content.indexOf("收件人"), content.indexOf("收件人") + 50);
    } else if (content.indexOf("收件地址") != -1) {
      str = content.substring(content.indexOf("收件地址"), content.indexOf("收件地址") + 50);
    }
    Pattern p = Pattern.compile("[a-z0-9]+[a-z0-9_\\-.]*@([a-z0-9]+\\.)+[a-z]{2,10}");
    Matcher m = p.matcher(str);
    if (m.find()) {
      return m.group().trim();
    }
    Pattern pattern = Pattern.compile("(?<=\\（)[a-z0-9]+[a-z0-9_\\-.]*@([a-z0-9]+\\.)+[a-z]{2,10}(?=\\）)");
    Matcher matcher = pattern.matcher(content);
    if (matcher.find()) {
      return matcher.group().trim();
    }
    return null;
  }

  /**
   * 解析邮件内容
   * 
   * @param obj
   * @throws Exception
   */
  private void buildMailContent(Object obj, MailReturnedRecord m) throws Exception {
    if (obj != null) {
      String content = "";
      if (obj instanceof Multipart) {
        Multipart multipart = (Multipart) obj;
        content = loopFindContent(multipart);
      } else {
        content = obj.toString();
      }
      // 超长保护
      if (content.length() > 2000) {
        content = removeHtml(content);
      }
      if (content.length() > 2000) {
        content = content.substring(0, 2000);
      }
      m.setContent(content);
    }
  }

  /**
   * BodyPart 单个部件 注意：单个部件有可能又为一个Multipart，层层嵌套 所以这里递归解析邮件内容
   * 
   * @param multipart
   * @return
   * @throws Exception
   */
  private String loopFindContent(Multipart multipart) throws Exception {
    String content = "";
    int count = multipart.getCount();
    for (int i = 0; i < count; i++) {
      BodyPart part = multipart.getBodyPart(i);
      String type = part.getContentType().split(";")[0];
      if ("MULTIPART/ALTERNATIVE".equals(type.toUpperCase())) {
        Multipart m = (Multipart) part.getContent();
        content += loopFindContent(m);
      } else if (part.getContent() != null) {
        content += part.getContent().toString();
      }
    }
    return content;
  }

  /**
   * 通过IMAP获取收件箱
   * 
   * @param host
   * @param name
   * @param pwd
   * @return
   */
  @Override
  public Message[] findInboxImap(String host, String name, String pwd) {
    host = changeHost(host);
    Message[] messages = null;
    if (checkAvailable(name)) {
      return null;
    }
    try {
      Properties props = System.getProperties();
      Session session = Session.getDefaultInstance(props, null);
      Store store = session.getStore("imap");
      store.connect(host, name, pwd);
      IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
      folder.open(Folder.READ_WRITE); // 打开收件箱
      FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);// false代表未读，true代表已读
      /*
       * messages = folder.getMessages(folder.getMessageCount() - folder.getUnreadMessageCount() + 1,
       * folder.getMessageCount());
       */
      messages = folder.search(ft); // 根据设置好的条件获取message 获取未读邮件列表
    } catch (Exception e) {
      saveError(host, name, pwd, e.getMessage());
      logger.error("邮箱：" + name + "连接到收件箱异常,请及时处理");
    }
    return messages;
  }

  private void saveError(String host, String name, String pwd, String msg) {
    MailConnectError m = new MailConnectError();
    Date date = new Date();
    m.setAccount(name);
    m.setHost(host);
    m.setPwd(pwd);
    m.setMsg(msg);
    m.setCreateDate(date);
    m.setUpdateDate(date);
    mailConnectErrorDao.save(m);
  }

  private boolean checkAvailable(String name) {
    return mailConnectErrorDao.isExist(name);
  }

  private String changeHost(String host) {
    if ("smtp.126.com".equals(host)) {
      host = "imap.126.com";
    } else if ("smtp.qiye.163.com".equals(host)) {
      host = "imap.qiye.163.com";
    }
    return host;
  }

  private String filterMail(String str) {
    if (StringUtils.isBlank(str)) {
      return "";
    }
    int start = str.lastIndexOf("<");
    if (start == -1) {
      return str;
    }
    int end = str.lastIndexOf(">");
    if (end == -1) {
      return str;
    }
    return str.substring(start + 1, end);
  }

  /**
   * 去空格
   * 
   * @param title
   * @return
   */
  private String removeBlank(String str) {
    if (StringUtils.isBlank(str)) {
      return "";
    }
    return str.replaceAll("&nbsp;", "");
  }

  /**
   * 过滤html
   * 
   * @param title
   * @return
   */
  private String removeHtml(String str) {
    if (StringUtils.isBlank(str)) {
      return "";
    }
    Pattern pattern = Pattern.compile("<([^>]*)>");
    Matcher matcher = pattern.matcher(str);
    StringBuffer sb = new StringBuffer();
    boolean result1 = matcher.find();
    while (result1) {
      matcher.appendReplacement(sb, "");
      result1 = matcher.find();
    }
    matcher.appendTail(sb);
    return sb.toString();
  }

}
