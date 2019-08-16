package com.smate.web.management.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.model.analysis.ExpertDiscForm;
import com.smate.web.management.model.analysis.KeywordSplit;
import com.smate.web.management.model.analysis.PsnCmdForm;
import com.smate.web.management.service.analysis.CooperatorCmdService;
import com.smate.web.management.service.analysis.PsnCooperatorCache;

/**
 * 合作者推荐Action.
 * 
 * @author pwl
 * 
 */

@Results({@Result(name = "cooperatorCmd_main", location = "/WEB-INF/cooperator/cooperatorCmd_main.jsp"),
    @Result(name = "cooperatorCmd_detail", location = "/WEB-INF/cooperator/cooperatorCmd_detail.jsp"),
    @Result(name = "cooperator_page", location = "/WEB-INF/cooperator/cooperator_page.jsp"),
    @Result(name = "cooperator_disc", location = "/WEB-INF/cooperator/cooperator_disc.jsp")})
public class CooperatorCmdAction extends ActionSupport implements ModelDriven<PsnCmdForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = -5648390828929550310L;
  private static final int PAGE_SIZE = 10;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CooperatorCmdService cooperatorCmdService;

  @Autowired
  private PsnCooperatorCache pcc;

  private PsnCmdForm form;
  private String title;
  private String abs;
  private String urlType = "/paper";
  private String menuId = "4062";
  private List<KeywordSplit> keywordsList;
  private List<ExpertDiscForm> expertDiscList;
  private Page<PsnCmdForm> page = new Page<PsnCmdForm>(PAGE_SIZE);
  private String keywordStr;
  private String mmId;
  private String selectedId;

  @Action("/scmmanagement/cooperatorCmd/main")
  public String enterMainPage() throws Exception {
    mmId = Struts2Utils.getParameter("mmId");
    selectedId = Struts2Utils.getParameter("selectedId");

    if (StringUtils.isEmpty(mmId) || "null".equals(mmId)) {
      mmId = "mm1";
    }

    if (StringUtils.isEmpty(selectedId) || "null".equals(selectedId)) {
      selectedId = "selectmd1";
    }

    return "cooperatorCmd_main";
  }

  @Action("/scmmanagement/cooperatorCmd/detail")
  public String enterDetailPage() throws Exception {
    try {
      if (StringUtils.isNotBlank(title) || StringUtils.isNotBlank(abs)) {
        keywordsList = this.cooperatorCmdService.getKeywordsList(title, abs);
        form.setKeywordJson(this.initKeywordsJson());
      }
      if (StringUtils.isNotBlank(keywordStr)) {
        keywordsList = new ArrayList<KeywordSplit>();
        String[] kws = keywordStr.split(",");
        for (String string : kws) {
          KeywordSplit ks = new KeywordSplit();
          ks.setKeyword(string);
          keywordsList.add(ks);
        }
        form.setKeywordJson(this.initKeywordsJson());
      }
    } catch (Exception e) {
      logger.error("进入合作者推荐页面出现异常：", e);
    }

    return "cooperatorCmd_detail";
  }

  @Action("/scmmanagement/cooperatorCmd/ajaxLoadCooperator")
  public String ajaxLoadCooperatorByNew() throws Exception {
    if (StringUtils.isNotBlank(form.getKeywordJson())) {
      Long psnId = SecurityUtils.getCurrentUserId();
      String runEnv = System.getenv("RUN_ENV");
      String scmUser = System.getenv("SCM_USER");

      if (("linuxdev".equalsIgnoreCase(runEnv) || "development".equalsIgnoreCase(runEnv)) && "zym".equals(scmUser)) {// 开发机测试用
        psnId = 1000000020860L;

      }
      page = pcc.getCooperatorByPage(form, page, psnId);

    }
    return "cooperator_page";
  }


  @Action("/scmmanagement/cooperatorCmd/ajaxLoadDisc")
  public String ajaxLoadDisc() throws Exception {
    try {
      if (StringUtils.isNotBlank(form.getKeywordJson())) {
        expertDiscList =
            this.cooperatorCmdService.getCooperatorDisc(form, Struts2Utils.getRequest().getSession().getId());
      }
    } catch (Exception e) {
      logger.error("加载合作者研究领域出现异常：", e);
    }

    return "cooperator_disc";
  }

  private String initKeywordsJson() throws Exception {
    if (CollectionUtils.isEmpty(keywordsList)) {
      return "";
    }
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    Map<String, String> map = null;
    for (KeywordSplit keywords : keywordsList) {
      map = new HashMap<String, String>();
      map.put("id", ObjectUtils.toString(keywords.getDicId()));
      map.put("keyword", keywords.getKeyword());
      list.add(map);
    }
    return JacksonUtils.jsonObjectSerializer(list);
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnCmdForm();
    }
  }

  @Override
  public PsnCmdForm getModel() {

    return form;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAbs() {
    return abs;
  }

  public void setAbs(String abs) {
    this.abs = abs;
  }

  public String getUrlType() {
    return urlType;
  }

  public void setUrlType(String urlType) {
    this.urlType = urlType;
  }

  public String getMenuId() {
    return menuId;
  }

  public void setMenuId(String menuId) {
    this.menuId = menuId;
  }

  public List<KeywordSplit> getKeywordsList() {
    return keywordsList;
  }

  public void setKeywordsList(List<KeywordSplit> keywordsList) {
    this.keywordsList = keywordsList;
  }

  public List<ExpertDiscForm> getExpertDiscList() {
    return expertDiscList;
  }

  public void setExpertDiscList(List<ExpertDiscForm> expertDiscList) {
    this.expertDiscList = expertDiscList;
  }

  public Page<PsnCmdForm> getPage() {
    return page;
  }

  public void setPage(Page<PsnCmdForm> page) {
    this.page = page;
  }

  public String getKeywordStr() {
    return keywordStr;
  }

  public void setKeywordStr(String keywordStr) {
    this.keywordStr = keywordStr;
  }

  public String getMmId() {
    return mmId;
  }

  public void setMmId(String mmId) {
    this.mmId = mmId;
  }

  public String getSelectedId() {
    return selectedId;
  }

  public void setSelectedId(String selectedId) {
    this.selectedId = selectedId;
  }

}
