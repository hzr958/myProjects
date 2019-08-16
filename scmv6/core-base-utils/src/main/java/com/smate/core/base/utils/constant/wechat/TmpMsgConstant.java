package com.smate.core.base.utils.constant.wechat;

/**
 * 模板消息常量.
 * 
 * @author xys
 *
 */
public class TmpMsgConstant {

  /**
   * 普通用户openid.
   */
  public static final String TO_USER = "touser";
  /**
   * 模板ID.
   */
  public static final String TEMPLATE_ID = "template_id";
  public static final String URL = "url";
  public static final String TOPCOLOR = "topcolor";
  public static final String FIRST = "first";
  public static final String REMARK = "remark";
  public static final String VALUE = "value";
  public static final String COLOR = "color";
  public static final String REQ_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send";
  public static final String SMATE_TEMPLATE_ID_KEY = "smateTempId";
  /**
   * smate模板前缀.
   */
  public static final String SMATE_TEMPLATE_ = "smate_template_";

  /**
   * smate模板2包含url的消息类型.
   */
  public static final String SMATE_TEMPLATE_2_PUB = "论文推荐";
  public static final String SMATE_TEMPLATE_2_FUND = "基金推荐";
}
