package com.smate.core.base.utils.constant;

/**
 * 
 * 邮件常用常量类
 * 
 * @author zk
 *
 */
public class EmailConstants {

  // 邮件类型keymailType key
  public static final String EMAIL_TYPE_KEY = "emailTypeKey";

  // 正常
  public static final String NORMAL = "normal";
  // 成功
  public static final String SUCCESS = "success";
  // 失败
  public static final String FAILURE = "failure";

  // 邮件类型:普通邮件
  public static final Integer COMMON_EMAIL = 0;
  // 邮件类型:推广邮件（初始状态，手工触发）
  public static final Integer PROMOTE_EMAIL = 5;
  // 邮件类型：推广邮件（后台任务推广邮件）
  public static final Integer PROMOTE_EMAIL_HT = 6;
  // 邮件类型：推广邮件（可以发送状态）
  public static final Integer PROMOTE_EMAIL_SEND = 6;

  // 默认语言
  public static final String DEFAULT_LOCALE = "zh_CN";
  // 中文
  public static final String ZH_LOCALE = "zh_CN";
  // 英文
  public static final String EN_LOCALE = "en_US";
  // 编码
  public static final String ENCODING = "utf-8";

  // 收件邮箱
  public static final String EMAIL_RECEIVEEMAIL_KEY = "email_receiveEmail";
  // 邮件主题
  public static final String EMAIL_SUBJECT_KEY = "email_subject";
  // 邮件模板
  public static final String EMAIL_TEMPLATE_KEY = "email_Template";
  // 收件人
  public static final String EMAIL_RECEIVE_PSNID_KEY = "email_receive_psnId";
  // 发件人 scm-21063
  public static final String EMAIL_SENDER_PSNID_KEY = "email_sender_psnId";
  // 退订链接
  public static final String EMAIL_UNSUBURL_KEY = "unsubscribeUrl";
  // 预览链接
  public static final String EMAIL_VIEWURL_KEY = "viewMailUrl";
  // 邮件id
  public static final String EMAIL_MAILID_KEY = "email_mailId";
  // 文件
  public static final String EMAIL_FILENAME_KEY = "email_fileName";
  // 附件
  public static final String Email_ATTACHNAME_KEY = "email_attachName";
  // 优先级
  public static final String EMAIL_PRIOR_CODE_KEY = "email_priorCode";
  // 邮件初始状态
  public static final String EMAIL_STATUS_KEY = "email_status";

  public static final String BASE_URL = "https://www.scholarmate.com/resmod/images_v5/";

  public static final String NO_FULLTEXT_IMG = "images2016/file_img.jpg";

  public static final String HTML_IMG = "html_img.jpg";
  public static final String DOC_IMG = "doc_img.jpg";
  public static final String txt_IMG = "txt_img.jpg";
  public static final String ZIP_IMG = "zip_img.jpg";

  // 邮件的初始来源

  // 生产机邮件记录初始状态
  public static final Integer runStatus = 0;
  // 长宽邮件记录初始状态
  public static final Integer ckStatus = 6;

  // 邮件服务节点
  public static final Integer NODE_ID = 5;
}
