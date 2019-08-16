package com.smate.web.mobile.v8pub.consts;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class V8pubConst {

  public final static String SUCCESS = "success"; // 成功状态

  public final static String ERROR = "error"; // 失败状态

  public final static String RESULT = "result"; //

  public final static String ERROR_MSG = "errorMsg"; // 错误消息
  // 默认的全文图片
  public static final String PUB_DEFAULT_NOT_FULLTEXT_IMG_NEW = "/resmod/images_v5/images2016/file_img.jpg";

  public static final String PUB_ENTER_OR_EDITOR_VIEW_PREFIX = "pub/enteroreditor/";// 成果编辑页面view所在路径

  public static final Map<Integer, String> PUB_TYPE_MAP;// 成果类型ID和成果类型名称Map
  public static final Map<Integer, String> PUB_TYPE_MAP_ZH;// 成果类型ID和成果类型名称Map
  public static final Map<Integer, String> PUB_TYPE_MAP_EN;// 成果类型ID和成果类型名称Map

  static {
    Map<Integer, String> typeMap = new HashMap<Integer, String>();
    Map<Integer, String> typeMapZh = new HashMap<Integer, String>();
    Map<Integer, String> typeMapEn = new HashMap<Integer, String>();

    typeMap.put(1, "award"); // 奖励
    typeMap.put(2, "book_monograph"); // 书/著作
    typeMap.put(3, "conference_paper"); // 会议论文
    typeMap.put(4, "journal_article"); // 期刊论文
    typeMap.put(5, "patent"); // 专利
    typeMap.put(7, "other"); // 其他
    typeMap.put(8, "thesis"); // 学位论文
    typeMap.put(10, "book_chapter"); // 书籍章节

    typeMapZh.put(1, "奖励"); // 奖励
    typeMapZh.put(2, "书/著作"); // 书/著作
    typeMapZh.put(3, "会议论文"); // 会议论文
    typeMapZh.put(4, "期刊论文"); // 期刊论文
    typeMapZh.put(5, "专利"); // 专利
    typeMapZh.put(7, "其他"); // 其他
    typeMapZh.put(8, "学位论文"); // 学位论文
    typeMapZh.put(10, "书籍章节"); // 书籍章节

    typeMap.put(1, "award"); // 奖励
    typeMap.put(2, "book_monograph"); // 书/著作
    typeMap.put(3, "conference_paper"); // 会议论文
    typeMap.put(4, "journal_article"); // 期刊论文
    typeMap.put(5, "patent"); // 专利
    typeMap.put(7, "other"); // 其他
    typeMap.put(8, "thesis"); // 学位论文
    typeMap.put(10, "book_chapter"); // 书籍章节
    PUB_TYPE_MAP = Collections.unmodifiableMap(typeMap);
    PUB_TYPE_MAP_ZH = Collections.unmodifiableMap(typeMapZh);
    PUB_TYPE_MAP_EN = Collections.unmodifiableMap(typeMapEn);

  }

  public final static String PUB_ID = "pubId"; // 成果id
  public final static String DES3_PUB_ID = "des3PubId"; // 成果id
  public final static String DES3_PRJ_ID = "des3PrjId";// 项目id
  public final static String DES3_FUND_ID = "des3FundId";// 基金id
  public final static String SERVICE_TYPE = "serviceType"; // 服务类型
  // 语言设置
  public static final String LANGUAGE_ALL = "ALL";
  public static final String LANGUAGE_ZH = "ZH_CN";
  public static final String LANGUAGE_EN = "EN";

}
