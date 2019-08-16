package com.smate.web.fund.action.pc.agency;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.agency.model.AgencySearchForm;
import com.smate.web.fund.service.agency.AgencySearchService;

@Results({@Result(name = "ins_search_main", location = "/WEB-INF/jsp/search/ins_search_main.jsp"),
    @Result(name = "ins_list", location = "/WEB-INF/jsp/search/ins_list.jsp"),
    @Result(name = "insSearchFilter", location = "/WEB-INF/jsp/search/ins_search_filter_new.jsp"),
    @Result(name = "character_menu", location = "/WEB-INF/jsp/search/ins_character_menu.jsp"),
    @Result(name = "region_menu", location = "/WEB-INF/jsp/search/ins_region_menu.jsp"),
    @Result(name = "new_ins_search_main", location = "/WEB-INF/jsp/search/new_ins_search_main.jsp"),
    @Result(name = "ins_search_list_menu", location = "/WEB-INF/jsp/search/ins_search_list_menu.jsp")})
public class AgencySearchAction extends ActionSupport implements Preparable, ModelDriven<AgencySearchForm> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private AgencySearchForm form;
  @Autowired
  private AgencySearchService agencySearchService;

  @Override
  public AgencySearchForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new AgencySearchForm();
    }
    if (SecurityUtils.getCurrentUserId() > 0L) {
      form.setHasLogin(1);
    }
  }

  /**
   * 进入机构检索主页面
   * 
   * @return
   */
  @Action("/prjweb/outside/agency/searchins")
  public String insSearchMain() {
    return "new_ins_search_main";
  }

  /**
   * ajax查询机构列表
   * 
   * @return
   */
  @Action("/prjweb/outside/agency/ajaxshowins")
  public String ajaxShowIns() {
    try {
      form = agencySearchService.searchInstitution(form);
    } catch (Exception e) {
      logger.error("获取机构列表出错, 检索条件： " + form.toString(), e);
    }
    return "ins_list";
  }

  /**
   * 检索机构后信息回显
   * 
   * @return
   */
  @Action("/prjweb/outside/agency/ajaxsearchcallback")
  public String searchInsCallBack() {
    try {
      form.setShowCharacterMenu(true);
      form.setShowRegionMenu(true);
      String result = agencySearchService.searchInsCallbackNew(form);
      if (form.getCreateMenu() == 1) {
        return "insSearchFilter";
      } else {
        Struts2Utils.renderJson(result, "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("检索机构获取信息回显出错，检索条件：" + form.toString(), e);
    }
    return null;
  }

  /**
   * 显示类别菜单
   * 
   * @return
   */
  @Action("/prjweb/outside/agency/ajaxcharactermenu")
  public String searchInsCharacterMenu() {
    try {
      form.setShowCharacterMenu(true);
      agencySearchService.searchInsCallbackNew(form);
    } catch (Exception e) {
      logger.error("检索机构获取信息回显出错，检索条件：" + form.toString(), e);
    }
    return "character_menu";
  }

  /**
   * 显示地区菜单
   * 
   * @return
   */
  @Action("/prjweb/outside/agency/ajaxregionmenu")
  public String searchInsRegionMenu() {
    try {
      form.setShowRegionMenu(true);
      agencySearchService.searchInsCallbackNew(form);
    } catch (Exception e) {
      logger.error("检索机构获取信息回显出错，检索条件：" + form.toString(), e);
    }
    return "region_menu";
  }

  /**
   * 赞、取消赞机构操作
   * 
   * @return
   */
  @Action("/prjweb/agency/award/ajaxopt")
  public String ajaxPsnAwardIns() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      // TODO 数据检查
      boolean validate = true;
      if (form.getInsId() == null && StringUtils.isBlank(form.getDesInsId())) {
        validate = false;
      }
      if (form.getStatus() == null) {
        validate = false;
      }
      if (!validate) {
        result.put("result", "params validate not pass");
      } else {
        form.setPsnId(SecurityUtils.getCurrentUserId());
        Integer likeSum = agencySearchService.awardInsOpt(form);
        if (likeSum != null && likeSum >= 0) {
          result.put("likeSum", likeSum.toString());
        }
        result.put("result", "success");
      }
    } catch (Exception e) {
      logger.error("赞/取消赞机构操作出错，psnId = " + form.getPsnId() + ", insId = " + form.getInsId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  /**
   * 初始化赞
   * 
   * @return
   */
  @Action("/prjweb/outside/agency/ajaxinitaward")
  public String ajaxInitAwardStatus() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      String initStr = agencySearchService.initInsInfo(form);
      result.put("result", "success");
      result.put("insId", initStr);
    } catch (Exception e) {
      logger.error("初始化机构赞状态出错， 需要初始化信息的机构ID为： " + form.getInitInsIds(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  /**
   * 获取机构url
   * 
   * @return
   */
  @Action("/prjweb/outside/agency/ajaxurl")
  public String ajaxGetInsUrl() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      String initStr = agencySearchService.findInsUrls(form);
      result.put("result", "success");
      result.put("insUrl", initStr);
    } catch (Exception e) {
      logger.error("获取机构url出错, 机构ID: " + form.getInitInsIds(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  /**
   * 机构社交元素操作
   * 
   * @return
   */
  @Action("/prjweb/agency/ins/ajaxoptins")
  public String ajaxOptIns() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      // TODO 数据检查
      boolean validate = true;
      if (form.getInsId() == null && StringUtils.isBlank(form.getDesInsId())) {
        validate = false;
      }
      if (form.getType() == null) {
        validate = false;
      }
      if (!validate) {
        result.put("result", "params validate not pass");
      } else {
        form.setPsnId(SecurityUtils.getCurrentUserId());
        String status = agencySearchService.optIns(form);
        result.put("result", status);
      }
    } catch (Exception e) {
      logger.error(
          "机构社交元素操作出错，psnId = " + form.getPsnId() + ", insId = " + form.getInsId() + ", type = " + form.getType(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  /**
   * 进入机构检索主页面
   * 
   * @return
   */
  // @Action("/prjweb/outside/agency/newsearchins")
  // public String insSearchNewMain() {
  // return "new_ins_search_main";
  // }

  /**
   * ajax查询机构页面
   * 
   * @return
   */
  @Action("/prjweb/outside/agency/ajaxshownewins")
  public String ajaxShowNewIns() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form = agencySearchService.searchNewInstitution(form);
    } catch (Exception e) {
      logger.error("获取机构列表出错, 检索条件： " + form.toString(), e);
    }
    return "ins_search_list_menu";
  }
}
