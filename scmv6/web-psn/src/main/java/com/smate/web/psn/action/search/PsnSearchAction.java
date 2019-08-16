package com.smate.web.psn.action.search;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.util.HtmlUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.utils.filter.FilterAllSpecialCharacter;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.JnlFormateUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.model.psninfo.PsnInfo;
import com.smate.web.psn.model.search.PersonSearch;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.search.PsnSearchService;

/**
 * 人员检索Action.
 * 
 * @author xys
 *
 */
@Results({@Result(name = "main", location = "/WEB-INF/jsp/search/main.jsp"),
    @Result(name = "mobile_main", location = "/WEB-INF/jsp/search/mobile_main_2.jsp"),
    @Result(name = "mobile_list", location = "/WEB-INF/jsp/search/mobile_list.jsp"),
    @Result(name = "psnList", location = "/WEB-INF/jsp/search/list.jsp"),
    @Result(name = "none", location = "/WEB-INF/jsp/search/none_list.jsp"),
    @Result(name = "mobile_search_friend_list", location = "/WEB-INF/jsp/search/mobile_search_friend_list.jsp"),
    @Result(name = "mobile_search_friend_main", location = "/WEB-INF/jsp/search/mobile_search_friend_main.jsp"),
    @Result(name = "find_psn_list", location = "/WEB-INF/jsp/friend/findfriend/find_psn_list.jsp"),
    @Result(name = "msg_chat_all_list2", location = "/WEB-INF/jsp/friend/msg_chat_all_list2.jsp"),
    @Result(name = "msg_chat_all_list", location = "/WEB-INF/jsp/friend/msg_chat_all_list.jsp"),
    @Result(name = "mobile_msg_chat_all_list", location = "/WEB-INF/jsp/friend/mobile_msg_chat_all_list.jsp")

})
public class PsnSearchAction extends ActionSupport implements ModelDriven<QueryFields>, Preparable {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private static final long serialVersionUID = 1137416006900050687L;

  /**
   * 每页显示10条记录.
   */
  private int defaultPageSize = 10;
  private QueryFields queryFields;
  private Page<PersonSearch> page = new Page<PersonSearch>(defaultPageSize);

  @Autowired
  private PsnSearchService psnSearchService;
  @Autowired
  private PersonManager personManager;

  private String des3PsnIdsStr;// 批量加密的psnId，以逗号分隔
  private String des3PsnId;
  private Long psnId;

  private String avatars;
  private List<PsnInfo> psnInfo;
  @Value("${domainscm}")
  private String domainScm;

  /**
   * PC-人员检索.
   * 
   * @return
   */
  @Action("/psnweb/search")
  public String getMain() {
    /* 要判断有没有登陆 */
    String[] temp = Struts2Utils.getSession().getValueNames();
    if (des3PsnId != null && !"".equals(des3PsnId) && temp.length > 0) {
      Long psnId = Long.valueOf(Des3Utils.decodeFromDes3(des3PsnId));
      com.smate.core.base.utils.model.security.Person person = personManager.getPersonInfo(psnId);
      avatars = person.getAvatars();
      queryFields.setLogin("true");
    }
    return "main";
  }

  /**
   * Mobile-人员检索.
   * 
   * @return
   */
  @Actions({@Action("/psnweb/mobile/search"), @Action("/psnweb/mobile/frdsearch")})
  public String getMobileMain() {
    psnId = SecurityUtils.getCurrentUserId();
    des3PsnId = ServiceUtil.encodeToDes3(SecurityUtils.getCurrentUserId() + "");
    if ("mobileSearchFriend".equals(queryFields.getFromPage())) {
      return "mobile_search_friend_main";
    }
    if (psnId == 0L) {
      Struts2Utils.getRequest().setAttribute("hasLogin", "no");
    }
    try {
      queryFields.setSearchString(URLDecoder.decode(HtmlUtils.htmlUnescape(queryFields.getSearchString()), "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      logger.error("移动端全站检索--》人员检索--》检索关键词转化异常, psnId = " + SecurityUtils.getCurrentUserId() + "searchKey="
          + queryFields.getSearchString(), e);
    }
    return "mobile_main";
  }

  /**
   * Mobile-人员列表.
   * 
   * @return
   */
  @Action("/psnweb/mobile/ajaxlist")
  public String getMobileList() {
    try {
      String searchString = StringEscapeUtils.unescapeHtml4(queryFields.getSearchString());
      Boolean isDoi = JnlFormateUtils.isDoi(searchString);
      // 特殊字符处理
      if (isDoi) {
        queryFields.setIsDoi(true);
        queryFields.setSearchString(FilterAllSpecialCharacter.StringFilter(searchString));
      } else {
        queryFields.setIsDoi(false);
        // 只有这五种特殊字符 , exit
        if (FilterAllSpecialCharacter.isExcludedChars(searchString)) {
          queryFields.setSearchString("");
        } /*
           * else {
           * queryFields.setSearchString(FilterAllSpecialCharacter.stringFilterExcludeFiveChar(searchString));
           * }
           */
      }
      if (StringUtils.isNotBlank(queryFields.getSearchString())) {
        page = psnSearchService.getPsns(page, queryFields);
      }
    } catch (Exception e) {
      logger.error("Mobile-人员列表获取失败");
    }
    if ("mobileSearchFriend".equals(queryFields.getFromPage())) {
      return "mobile_search_friend_list";
    }
    return "mobile_list";
  }

  /**
   * Mobile-批量获取人员头像.
   * 
   * @return
   */
  @Actions({@Action("/psnweb/mobile/ajaxavatarurls"), @Action("/psnweb/findpsn/ajaxavatarurls")})
  public String getMobileAvatarUrls() {
    try {
      String avatarUrls = psnSearchService.getAvatarUrls(des3PsnIdsStr);
      Struts2Utils.renderJson(avatarUrls, "encoding:utf-8");
    } catch (Exception e) {
      logger.error("Mobile-批量获取人员头像出现异常了喔！");
    }
    return null;
  }

  /**
   * 消息中心全站检索
   * 
   * @return
   */
  @Action("/psnweb/search/ajaxmsgallpsnlist")
  public String getPsnsForMsg() {
    try {
      psnSearchService.getPsnsForMsg(page, queryFields);
    } catch (Exception e) {
      logger.error("消息中心人员全站检索失败", e);
    }
    if (1 == queryFields.getSearchType()) {
      return "msg_chat_all_list2";
    } else {
      return "msg_chat_all_list";
    }
  }

  /**
   * 消息中心全站检索
   * 
   * @return
   */
  @Action("/psnweb/mobile/ajaxmsgallpsnlist")
  public String getMidPsnsForMsg() {
    try {
      psnSearchService.getPsnsForMsg(page, queryFields);
    } catch (Exception e) {
      logger.error("消息中心人员全站检索失败", e);
    }
    return "mobile_msg_chat_all_list";
  }

  @Action("/psnweb/search/ajaxlist")
  public String getPsns() {
    // queryFields.setSearchString("中国人");
    try {
      Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", domainScm);
      if (SecurityUtils.getCurrentUserId() != null && SecurityUtils.getCurrentUserId() != 0) {
        queryFields.setLogin("true");
      }
      String searchString = StringEscapeUtils.unescapeHtml4(queryFields.getSearchString());
      Boolean isDoi = JnlFormateUtils.isDoi(searchString);
      // 特殊字符处理
      if (isDoi) {
        queryFields.setIsDoi(true);
        this.handleSearchString(searchString, queryFields);
      } else {
        queryFields.setIsDoi(false);
        // 只有这五种特殊字符 , exit
        if (FilterAllSpecialCharacter.isExcludedChars(searchString)) {
          queryFields.setSearchString("");
        } /*
           * else { this.handleSearchString(searchString, queryFields); }
           */
        if (StringUtils.isNotBlank(queryFields.getSearchString())) {
          psnSearchService.getPsns(page, queryFields);
        }
      }
    } catch (Exception e) {
      logger.error("人员列表获取失败", e);
    }
    if (page.getResult() != null && page.getResult().size() > 0) {
      // SCM-24288
      Long openId = page.getResult().get(0).getOpenId();
      if (page.getResult().size() == 1 && StringUtils.isNotBlank(queryFields.getSearchString())
          && queryFields.getSearchString().equals(openId.toString())) {
        try {
          String requestUrl = "/psnweb/outside/homepage?des3PsnId="
              + URLEncoder.encode(page.getResult().get(0).getDes3PsnId(), "UTF-8");
          Struts2Utils.getResponse().sendRedirect(domainScm + requestUrl);
        } catch (Exception e) {
          logger.error("检索人员进入人员主页失败", e);
        }
        return null;
      } else {
        return "psnList";
      }
    } else {
      return "none";
    }
  }

  private void handleSearchString(String searchString, QueryFields queryFields) {
    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    Matcher m = p.matcher(searchString);
    if (m.find()) {
      String[] searchStrings = null;
      if (searchString.contains(";")) {
        searchStrings = searchString.split(";");
      } else if (searchString.contains(",")) {
        searchStrings = searchString.split(",");
      } else if (searchString.contains(" ")) {
        searchStrings = searchString.split(" ");
      }
      StringBuffer sbuffer = new StringBuffer();
      if (searchStrings != null) {
        for (String string : searchStrings) {
          sbuffer.append("\"");
          sbuffer.append(string);
          sbuffer.append("\"");
        }
      } else {
        sbuffer.append(searchString);
      }
      queryFields.setSearchString(FilterAllSpecialCharacter.StringFilter(sbuffer.toString()));
    } else {
      queryFields.setSearchString("\"" + FilterAllSpecialCharacter.StringFilter(searchString) + "\"");
    }
  }

  @Action("/psnweb/search/ajaxPsnOtherInfos")
  public String getPsnOtherInfos() {
    try {
      String des3PsnIdsStrs = Struts2Utils.getParameter("des3PsnIds");
      // String PsnIdsStrs="fdsk8,kad8sdf,sd89d";
      String rtnJson = psnSearchService.getPsnOtherInfo(des3PsnIdsStrs);
      Struts2Utils.renderJson("{\"result\":\"success\",\"rtnJson\":" + rtnJson + "}", "encoding:utf-8");
    } catch (Exception e) {
      logger.error("人员其他信息获取出现异常");
    }
    return null;
  }

  @Action("/psnweb/findpsn/ajaxfind")
  public String findFriend() {
    try {
      String isEmail = Struts2Utils.getParameter("isEmail");
      // 设置语言环境
      queryFields.setLanguage(LocaleContextHolder.getLocale().toString());
      // 检索条件是邮箱格式的则匹配账号
      if ("true".equals(isEmail)) {
        personManager.matchPsnByEmail(page, queryFields.getSearchString());
      } else {
        // 不需要排除掉好友，只是不显示添加好友按钮
        queryFields.setNeedExcludeFriendId(false);
        String searchString = StringEscapeUtils.unescapeHtml4(queryFields.getSearchString());
        queryFields.setIsDoi(false);
        // 特殊字符处理
        // 只有这五种特殊字符 , exit
        if (FilterAllSpecialCharacter.isExcludedChars(searchString)) {
          queryFields.setSearchString("");
        } else {
          queryFields.setSearchString(FilterAllSpecialCharacter.stringFilterExcludeFiveChar(searchString));
        }
        if (StringUtils.isNotBlank(queryFields.getSearchString())) {
          page = psnSearchService.findPsn(page, queryFields);
        }
      }
      // 构建列表所需信息
      if (CollectionUtils.isNotEmpty(page.getResult())) {
        List<Long> psnIds = new ArrayList<Long>();
        for (PersonSearch ps : page.getResult()) {
          psnIds.add(ps.getPsnId());
        }
        List<PersonSearch> infoList = personManager.buildPsnInfoForList(psnIds);
        List<Long> noButtonIds = psnSearchService.findNotNeedFriendReqPsnIds(SecurityUtils.getCurrentUserId());
        if (CollectionUtils.isNotEmpty(infoList) && CollectionUtils.isNotEmpty(noButtonIds)) {
          for (PersonSearch psn : infoList) {
            if (noButtonIds.contains(psn.getPsnId())) {
              psn.setNeedButton(false);
            }
          }
        }
        page.setResult(infoList);
      }
    } catch (Exception e) {
      logger.error("PC---查找人员失败", e);
    }
    return "find_psn_list";
  }

  @Override
  public QueryFields getModel() {
    return queryFields;
  }

  @Override
  public void prepare() throws Exception {
    if (queryFields == null) {
      queryFields = new QueryFields();
    }
    if (queryFields != null && queryFields.getPage() == null) {
      queryFields.setPage(this.getPage());
    }

  }

  public Page<PersonSearch> getPage() {
    return page;
  }

  public void setPage(Page<PersonSearch> page) {
    this.page = page;
  }

  public String getDes3PsnIdsStr() {
    return des3PsnIdsStr;
  }

  public void setDes3PsnIdsStr(String des3PsnIdsStr) {
    this.des3PsnIdsStr = des3PsnIdsStr;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  protected List<PsnInfo> getPsnInfo() {
    return psnInfo;
  }

  protected void setPsnInfo(List<PsnInfo> psnInfo) {
    this.psnInfo = psnInfo;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

}
