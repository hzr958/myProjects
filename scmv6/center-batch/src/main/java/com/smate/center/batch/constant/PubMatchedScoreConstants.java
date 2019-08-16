package com.smate.center.batch.constant;

/**
 * 成果匹配得分的常量配置类.
 * 
 * @author mjg
 * 
 */
public class PubMatchedScoreConstants {

  // 精确匹配.
  public final static Integer EXACT_FNAME_EMAIL_SCORE = 300;// 姓名全称和邮件匹配.
  public final static Integer EXACT_INAME_EMAIL_SCORE = 270;// 姓名简称和邮件匹配.
  // 非精确匹配.
  public final static Integer FNAME_SCORE = 50;// 本人全称匹配.
  public final static Integer INAME_SCORE = 20;// 本人简称匹配.
  public final static Integer CO_FNAME_SCORE = 20;// 合作者全称匹配.
  public final static Integer CO_INAME_SCORE = 10;// 合作者简称匹配.
  public final static Integer CO_EMAIL_SCORE = 25;// 合作者邮件匹配.
  public final static Integer KEYWORD_SCORE = 20;// 关键词匹配.
  public final static Integer JOURNAL_SCORE = 20;// 期刊匹配.

  public final static Integer MATCH_LIMIT_SIZE = 7;// 即时匹配的成果结果数限制.
}
