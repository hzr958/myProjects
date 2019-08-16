package com.smate.center.task.service.email;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.model.security.Person;

/**
 * 推荐研究领域的业务逻辑实现接口<获取封装发送推荐邮件的参数逻辑>.
 * 
 * @author mjg
 * 
 */
public interface BaseRaRecmdService {
  // 邮件标题内容.
  final static String MAIL_TITLE_CON = "你有以下等{0}个可能熟悉的研究领域。";
  final static Integer MAIL_VIEW_SHOW_LIMIT_COUNT = 3;// 列表显示的关键词数.
  // 邮件模版中的标签参数名.
  final static String MAIL_PARAM_KEY_PSN_ID = "receivePsnId";// 收件人ID.
  final static String EMAIL_RECEIVEEMAIL = "email_receiveEmail";// 收件人邮箱
  final static String MAIL_PARAM_KEY_PSN_NAME = "receivePsnName";// 收件人名称.
  final static String MAIL_PARAM_KEY_OPERAT_URL = "operatUrl";// 查看详情链接.
  final static String MAIL_PARAM_LIST_KEY = "kwList";// 期刊列表.
  final static String MAIL_PARAM_LIST_SIZE = "totals";// 期刊列表记录总数.
  final static String MAIL_PARAM_LIST_KEY_KW_URL = "kwUrl";// 列表-关键词链接地址.
  final static String MAIL_PARAM_LIST_KEY_KW_TXT = "kwTxt";// 列表-关键词显示内容.
  final static String MAIL_PARAM_LIST_KEY_KW_ID = "kwId";// 列表-关键词ID.
  final static String EMAIL_TEMPLATE_KEY = "email_Template"; // 邮件模板
  public static final String EMAIL_SUBJECT_KEY = "email_subject"; // 邮件主题
  // 研究领域推荐
  final String RESEACHAREA_RECMD = "researchAreaRecmd";
  final int RESEACHAREA_RECMD_CODE = 22;
  final String RESEACHAREA_RECMD_TEMPLATE = "ResearchArea_Recommend";
  final static String LOCALE_ZH_CN = "zh_CN";// 当前语言环境-中文.
  final String ZH_LOCALE = "zh_CN";
  final String EN_LOCALE = "en_US";
  final static String TEMPLATE_SUFFIX = ".ftl";// 邮件模版的后缀名.

  /**
   * 构建保存邮件发送记录实体信息.
   * 
   * @param person 收件人.
   * @param reJnlList
   */
  void buildMailLogEntity(Person person, List<String> rKwList) throws ServiceException, UnsupportedEncodingException;
}
