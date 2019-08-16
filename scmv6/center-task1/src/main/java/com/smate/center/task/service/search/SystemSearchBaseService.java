package com.smate.center.task.service.search;

import java.util.List;
import java.util.Map;

import com.smate.center.task.model.pub.seo.PubIndexFirstLevel;
import com.smate.center.task.model.search.UserSearchResultForm;


/**
 * 站外检索基础服务接口<构建HTML页面正文内容并写入文件>.
 * 
 * @author mjg
 * 
 */
public interface SystemSearchBaseService {

  static final String ENCODING = "utf-8";
  static final String HTML_NAME_KEY = "html_key";
  static final String INDEX_PAGE_PATH = "resscmwebsns/html/indexSearch/";
  static final String TEMP_SUFFIX_NAME = ".ftl";
  static final String HTML_SUFFIX_NAME = ".html";
  static final String LOCALE_ZH = "zh_CN";
  static final String LOCALE_EN = "en_US";
  static final String PUB_CON_PAGE_NAME = "pub_maint";
  static final String PSN_CON_PAGE_NAME = "psn_maint";
  static final String FUND_CON_PAGE_NAME = "fund_maint";
  static final String JNL_CON_PAGE_NAME = "jnl_maint";
  static final String PUBLICATION_INDEX_ZH_FTL = "publication_index_zh_CN.ftl";
  static final String PUBLICATION_INDEX_EN_FTL = "publication_index_en_US.ftl";

  /**
   * 获取26个英文字母列表.
   * 
   * @return
   */
  List<String> getCodeList();

  /**
   * 将成果分层数据构建为html文件
   * 
   * @param fileRoot 文件保存路径
   * @param code
   * @param codePubList 成果分层数据
   */
  void buildPubPageCon(String fileRoot, String code, List<PubIndexFirstLevel> codePubList);

  /**
   * 构建站外检索人员内容.
   * 
   * @return
   */
  void buildPsnPageCon(String fileRoot, String locale, Map<String, List<UserSearchResultForm>> userMap);
}
