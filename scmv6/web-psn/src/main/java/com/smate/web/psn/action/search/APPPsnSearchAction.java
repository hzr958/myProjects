package com.smate.web.psn.action.search;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.filter.FilterAllSpecialCharacter;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.JnlFormateUtils;
import com.smate.web.psn.model.search.PersonSearch;
import com.smate.web.psn.service.search.PsnSearchService;

/**
 * APP人员检索接口
 * 
 * @author LJ
 *
 *         2017年8月3日
 */
public class APPPsnSearchAction extends ActionSupport implements ModelDriven<QueryFields>, Preparable {

  private static final long serialVersionUID = 4982664399999129621L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 每页显示10条记录.
   */
  private int defaultPageSize = 10;
  private QueryFields queryFields;
  private Page<PersonSearch> page = new Page<PersonSearch>(defaultPageSize);

  @Autowired
  private PsnSearchService psnSearchService;
  private String des3PsnIdsStr;// 批量加密的psnId，以逗号分隔
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态

  /**
   * 人员列表.
   * 
   * @return
   */
  @Action("/app/psnweb/mobile/ajaxlist")
  public String getMobileList() {
    // searchString
    try {
      String searchString = queryFields.getSearchString();
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
        status = IOSHttpStatus.OK;
        page = psnSearchService.getPsns(page, queryFields);
        total = page.getTotalCount().intValue();
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("app人员列表获取失败");
    }

    AppActionUtils.renderAPPReturnJson(page.getResult(), total, status);
    return null;

  }

  /**
   * Mobile-批量获取人员头像.
   * 
   * @return
   */
  @Action("/app/psnweb/mobile/ajaxavatarurls")
  public String getMobileAvatarUrls() {
    Map<String, Object> avatarUrlsMap = null;
    try {
      avatarUrlsMap = psnSearchService.getAvatarUrlsMap(des3PsnIdsStr);
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("app-批量获取人员头像出现异常！");
    }
    AppActionUtils.renderAPPReturnJson(avatarUrlsMap, total, status);
    return null;
  }

  /**
   * 消息中心全站检索人员
   * 
   * @return
   */
  @Action("/app/psnweb/search/ajaxmsgallpsnlist")
  public String getPsnsForMsg() {
    if (StringUtils.isNotBlank(queryFields.getSearchString())) {
      try {
        page.setPageSize(5);
        queryFields.setSearchType(0);
        psnSearchService.getPsnsForMsg(page, queryFields);
        total = page.getTotalCount().intValue();
        status = IOSHttpStatus.OK;
      } catch (Exception e) {
        logger.error("消息中心人员全站检索失败", e);
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;
    }
    AppActionUtils.renderAPPReturnJson(page.getResult(), total, status);
    return null;
  }

  public Page<PersonSearch> getPage() {
    return page;
  }

  public void setPage(Page<PersonSearch> page) {
    this.page = page;
  }

  @Override
  public QueryFields getModel() {
    return queryFields;
  }

  public String getDes3PsnIdsStr() {
    return des3PsnIdsStr;
  }

  public void setDes3PsnIdsStr(String des3PsnIdsStr) {
    this.des3PsnIdsStr = des3PsnIdsStr;
  }

  @Override
  public void prepare() throws Exception {
    if (queryFields == null) {
      queryFields = new QueryFields();
    }

  }

}
