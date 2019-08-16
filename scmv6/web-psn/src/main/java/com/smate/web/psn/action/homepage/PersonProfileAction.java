package com.smate.web.psn.action.homepage;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.vo.ProjectInfo;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.psn.model.psncnf.PsnConfigMoudle;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.psn.PsnSolrInfoModifyService;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.model.attention.AttPerson;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.keyword.PsnScienceArea;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.keyword.CategoryMapBaseService;
import com.smate.web.psn.service.keyword.CategoryScmService;
import com.smate.web.psn.service.pc.homepage.PsnOutsideHomepageService;
import com.smate.web.psn.service.profile.EducationHistoryService;
import com.smate.web.psn.service.profile.KeywordIdentificationService;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.PersonalManager;
import com.smate.web.psn.service.profile.PsnDisciplineKeyService;
import com.smate.web.psn.service.profile.PsnHomepageService;
import com.smate.web.psn.service.profile.PsnInfoImproveService;
import com.smate.web.psn.service.profile.PsnProfileUrlService;
import com.smate.web.psn.service.profile.WorkHistoryService;
import com.smate.web.psn.service.psnemail.PsnFirstEmailService;
import com.smate.web.psn.service.psnwork.PsnConstRegionService;
import com.smate.web.psn.service.psnwork.PsnWorkHistoryInsInfoService;
import com.smate.web.psn.service.referencesearch.ReferenceUpdateService;
import com.smate.web.psn.service.representprj.RepresentPrjService;
import com.smate.web.psn.service.sciencearea.ScienceAreaService;
import com.smate.web.psn.utils.EditValidateUtils;

/**
 * 
 *
 * @author wsn
 * @createTime 2017年3月9日 上午10:55:20
 *
 */
@Results({@Result(name = "homepage", location = "/WEB-INF/jsp/psnprofile/psn_homepage_main.jsp"),
    @Result(name = "psnNofind", location = "/WEB-INF/jsp/psnprofile/psn_nofindExit.jsp"),
    @Result(name = "psnbrief", location = "/WEB-INF/jsp/psnprofile/psn_briefdesc.jsp"),
    @Result(name = "contactInfo", location = "/WEB-INF/jsp/psnprofile/psn_contactinfo.jsp"),
    @Result(name = "keywords", location = "/WEB-INF/jsp/psnprofile/psn_keywords.jsp"),
    @Result(name = "representprj", location = "/WEB-INF/jsp/psnprofile/psn_representprj.jsp"),
    @Result(name = "editRepresentPrj", location = "/WEB-INF/jsp/psnprofile/psn_representprj_edit.jsp"),
    @Result(name = "openPrj", location = "/WEB-INF/jsp/psnprofile/psn_openprj_list.jsp"),
    @Result(name = "workHis", location = "/WEB-INF/jsp/psnprofile/psn_workhistory.jsp"),
    @Result(name = "editWork", location = "/WEB-INF/jsp/psnprofile/psn_workhistory_edit.jsp"),
    @Result(name = "showEdu", location = "/WEB-INF/jsp/psnprofile/psn_eduhistory.jsp"),
    @Result(name = "editEdu", location = "/WEB-INF/jsp/psnprofile/psn_eduhistory_edit.jsp"),
    @Result(name = "psnInfo", location = "/WEB-INF/jsp/psnprofile/psn_psninfo.jsp"),
    @Result(name = "psnEditScienceArea", location = "/WEB-INF/jsp/psnprofile/psn_science_area_edit.jsp"),
    @Result(name = "editScienceArea", location = "/WEB-INF/jsp/psnprofile/science_area_edit.jsp"),
    @Result(name = "addPubScienceArea", location = "/WEB-INF/jsp/psnprofile/add_pub_science_area.jsp"),
    @Result(name = "addPrjScienceArea", location = "/WEB-INF/jsp/psnprofile/add_prj_science_area.jsp"),
    @Result(name = "addScienceArea", location = "/WEB-INF/jsp/addScienceBox/add_science_area.jsp"),
    @Result(name = "sciencearea", location = "/WEB-INF/jsp/psnprofile/psn_sciencearea.jsp"),
    @Result(name = "otherHomepage", location = "/WEB-INF/jsp/psnprofile/psn_homepage_main.jsp"),
    @Result(name = "editBrief", location = "/WEB-INF/jsp/psnprofile/psn_brief_edit.jsp"),
    @Result(name = "workSelect", location = "/WEB-INF/jsp/psnprofile/psn_workhis_select.jsp"),
    @Result(name = "baseInfo", location = "/WEB-INF/jsp/psnprofile/psn_base_info.jsp"),
    @Result(name = "edit_keywords", location = "/WEB-INF/jsp/psnprofile/psn_keywords_edit.jsp"),
    @Result(name = "recommend_edit_keywords", location = "/WEB-INF/jsp/psnprofile/recommend_keywords_edit.jsp"),
    @Result(name = "improveInfo", location = "/WEB-INF/jsp/improveinfo/psn_improve_info_main.jsp"),
    @Result(name = "improve_sciencearea", location = "/WEB-INF/jsp/improveinfo/psn_science_area_improve.jsp"),
    @Result(name = "improve_keywords", location = "/WEB-INF/jsp/improveinfo/psn_keywords_improve.jsp")})
public class PersonProfileAction extends ActionSupport implements ModelDriven<PersonProfileForm>, Preparable {

  private static final long serialVersionUID = -1501906545664516778L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private PersonProfileForm form;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PsnDisciplineKeyService psnDisciplineKeyService;
  @Autowired
  private ScienceAreaService scienceAreaService;
  @Autowired
  private RepresentPrjService representPrjService;
  @Autowired
  private PsnWorkHistoryInsInfoService psnWorkHistoryInsInfoService;
  @Autowired
  private WorkHistoryService workHistoryService;
  @Autowired
  private EducationHistoryService educationHistoryService;
  @Autowired
  private PsnCnfService psnCnfService;
  @Autowired
  private CategoryMapBaseService categoryMapBaseService;
  @Autowired
  private FriendService friendService;
  @Autowired
  private PsnProfileUrlService psnProfileUrlService;
  @Autowired
  private PsnFirstEmailService PsnFirstEmailService;
  @Autowired
  private ReferenceUpdateService referenceUpdateService;
  @Autowired
  private PsnConstRegionService psnConstRegionService;
  @Autowired
  private CategoryScmService categoryScmService;
  @Autowired
  private PsnOutsideHomepageService psnOutsideHomepageService;
  private String superRegionId;// 地区ID

  @Value("${domainscm}")
  private String domainScm;

  @Autowired
  private PsnHomepageService psnHomepageService;

  @Autowired
  private PsnSolrInfoModifyService psnSolrInfoModifyService;

  @Autowired
  private PsnInfoImproveService psnInfoImproveService;
  @Autowired
  private PersonalManager personalManager;
  @Autowired
  KeywordIdentificationService identificationService;

  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;

  /**
   * 自动提示地域数据
   * 
   * @return
   */
  @Action("/psnweb/homepage/ajaxautoregion")
  public String ajaxAutoRegion() {
    try {
      form.setData("[]");
      psnConstRegionService.autoRegionPrompt(form);
    } catch (Exception e) {
      logger.error("自动提示地域数据，form = " + form, e);
    }
    Struts2Utils.renderJson(form.getData(), "encoding:UTF-8");
    return null;
  }

  /**
   * 自动提示科技领域数据
   * 
   * @return
   */
  @Action("/psnweb/scienceArea/ajaxScienceArea")
  public String ajaxScienceArea() {
    try {
      form.setData("[]");
      categoryMapBaseService.findCategoryByName(form);
    } catch (Exception e) {
      logger.error("自动提示地域数据，form = " + form, e);
    }
    Struts2Utils.renderJson(form.getData(), "encoding:UTF-8");
    return null;
  }

  /**
   * 自动提示科技领域数据
   * 
   * @return
   */
  @Action("/psnweb/scienceArea/ajaxgetScienceArea")
  public String ajaxGetScienceArea() {
    try {
      form.setData("[]");
      categoryScmService.findCategoryByName(form, false);
    } catch (Exception e) {
      logger.error("自动提示地域数据，form = " + form, e);
    }
    Struts2Utils.renderJson(form.getData(), "encoding:UTF-8");
    return null;
  }

  /**
   * 自动提示科技领域数据
   * 
   * @return
   */
  @Action("/psnweb/pubrecommend/ajaxgetScienceArea")
  public String ajaxRecommendGetScienceArea() {
    try {
      form.setData("[]");
      categoryScmService.findCategoryByName(form, true);
    } catch (Exception e) {
      logger.error("自动提示地域数据，form = " + form, e);
    }
    Struts2Utils.renderJson(form.getData(), "encoding:UTF-8");
    return null;
  }

  /**
   * 保存人员短地址
   * 
   * @return
   */
  @Action("/psnweb/homepage/ajaxsavepsnshorturl")
  public String savePsnShortUrl() {
    form.setResultMap(new HashMap<String, String>());
    try {
      if (StringUtils.isNotBlank(form.getDes3PsnId())
          && SecurityUtils.getCurrentUserId().equals(Long.parseLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())))) {
        psnProfileUrlService.savePsnShortUrl(form);
      } else {
        form.getResultMap().put("result", "error");
        form.getResultMap().put("msg", "没有权限操作");
      }
    } catch (Exception e) {
      form.getResultMap().put("result", "error");
      form.getResultMap().put("msg", "操作失败");
      logger.error("保存人员短地址异常，form = " + form, e);
    }
    Struts2Utils.renderJson(form.getResultMap(), "encoding:UTF-8");
    return null;
  }

  /**
   * 进入个人主页
   * 
   * @return
   */
  @Action("/psnweb/homepage/show")
  public String findPsnInfoMain() {
    try {
      if (!isHavePsn()) {
        // 若该账号不存在,判断是否具有合并账号
        if (!psnOutsideHomepageService.checkHasMerge(form)) {
          return "psnNofind";
        }
      }
      // 是否已登录
      boolean loginStatus = this.isLogin();
      // 已登录跳转到站内查看他人主页的页面
      if (!loginStatus) {
        if (StringUtils.isNotBlank(form.getDes3PsnId())) {
          Struts2Utils.getResponse().sendRedirect(
              domainScm + "/psnweb/outside/homepage?des3PsnId=" + URLEncoder.encode(form.getDes3PsnId(), "UTF-8"));
        }
        return null;
      }
      form.setIsLogin(true);
      boolean isMySelf = this.isMySelf();
      // 非本人，判断是否是好友关系
      if (!isMySelf) {
        form.setIsFriend(friendService.isFriend(SecurityUtils.getCurrentUserId(), form.getPsnId()));
        // 判断是否被关注
        Long currentPsnId = SecurityUtils.getCurrentUserId();
        AttPerson attPerson = personManager.payAttention(currentPsnId, form.getPsnId());
        if (attPerson != null) {
          form.setPayAttention(true);
          form.setAttentionId(attPerson.getId());
        } else {
          form.setPayAttention(false);
        }
        // 查询关注的人的主键ID
      }
      if (form.getPsnId() != null) {
        form.setNeedStatistics(true);
        form = personManager.getUnifiedPsnInfoByForm(form);
        builSeoTitle(form);
        personManager.buildPsnInfoConfig(form);
        form = workHistoryService.buildPsnWorkHistorySelector(form);
        // form.setPsnProfileUrl(psnProfileUrlService.findAndCreateUrl(form.getPsnInfo().getName(),form.getPsnId()));
        form.setDbUrl(referenceUpdateService.getDBurl());
        // 需要个人简介，关键词构建seo标签 （个人简介 1<<12 关键词1<<10）
        if (isMySelf) {
          buildSeoTagInfo(form, true, true);
        } else {
          // 不是本人，权限判断
          buildSeoTagInfo(form, (1 << 12) == (form.getCnfAnyMode() & (1 << 12)),
              (1 << 10) == (form.getCnfAnyMode() & (1 << 10)));
        }
      }
    } catch (PsnCnfException e) {
      personalManager.initPsnConfigInfoByTask(form.getPsnId());
      logger.error("个人模块配置为空，进行初始化，psnId = " + form.getPsnId(), e);
    } catch (Exception e) {
      logger.error("进入个人主页异常，psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    DateFormat df = new SimpleDateFormat("yyyy-MM");
    form.setNowTime(df.format(new Date()).toString());
    return "homepage";
  }

  /**
   * 构建seo用的title
   * 
   * @param form
   */
  private void builSeoTitle(PersonProfileForm form) {
    Locale locale = LocaleContextHolder.getLocale();
    String seoTitle = form.getName();// 页面title,不能在页面js拼接
    if (StringUtils.isNoneBlank(form.getInsAndDepInfo())) {
      seoTitle += " - " + form.getInsAndDepInfo();
    }
    form.setSeoTitle(HtmlUtils.htmlUnescape(seoTitle) + " | " + ("en_US".equals(locale.toString()) ? "Smate" : "科研之友"));
  }

  @Action("/psnweb/homepage/others")
  public String viewOthersHomePage() {
    try {
      this.isMySelf();
      if (form.getPsnId() != null) {
        form.setNeedStatistics(true);
        form = personManager.getUnifiedPsnInfoByForm(form);
        personManager.buildPsnInfoConfig(form);
      }
    } catch (PsnCnfException e) {
      personalManager.initPsnConfigInfoByTask(form.getPsnId());
      logger.error("个人模块配置为空，进行初始化，psnId = " + form.getPsnId(), e);
    } catch (Exception e) {
      logger.error("进入其他人的个人主页异常，psnId = " + form.getPsnId(), e);
    }
    return "otherHomepage";
  }

  /**
   * 显示人员简介信息
   * 
   * @return
   */
  @Action("/psnweb/briefdesc/ajaxshow")
  public String findPsnBrief() {
    try {
      this.isMySelf();
      form.setPsnBriefDesc(personManager.getPersonBrief(form.getPsnId()));
      Struts2Utils.getRequest().setAttribute("key", "test");
    } catch (Exception e) {
      logger.error("显示人员简介信息出错, psnId = " + form.getPsnId(), e);
    }
    return "psnbrief";
  }

  /**
   * 保存简介信息
   * 
   * @return
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Action("/psnweb/brief/ajaxsave")
  public String savePsnBrief() {
    Map result = new HashMap();
    try {
      if (this.isMySelf()) {
        personManager.savePersonBrief(form.getPsnBriefDesc(), form.getPsnId());
        personManager.refreshComplete(form.getPsnId());
        // 更新人员solr信息
        // psnSolrInfoModifyService.updateSolrPsnInfo(form.getPsnId());
        personalManager.refreshPsnSolrInfoByTask(form.getPsnId());
        result.put("result", "success");
      } else {
        result.put("result", "notSelf");
      }
    } catch (Exception e) {
      logger.error("保存简介信息出错， psnId = " + form.getPsnId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  /**
   * 编辑简介信息
   * 
   * @return
   */
  @Action("/psnweb/briefdesc/ajaxedit")
  public String getPsnBriefOnly() {
    try {
      this.isMySelf();
      form.setPsnBriefDesc(personManager.getPersonBrief(form.getPsnId()));
    } catch (Exception e) {
      logger.error("弹出人员简介信息编辑框出错， psnId = " + form.getPsnId(), e);
    }
    return "editBrief";
  }

  /**
   * 显示联系信息
   * 
   * @return
   */
  @Action("/psnweb/contact/ajaxshow")
  public String showPsnContactInfo() {
    try {
      this.isMySelf();
      Person psn = personManager.findPersonContactInfo(form.getPsnId());
      if (psn != null) {
        form.setPsnId(form.getPsnId());
        form.setEmail(psn.getEmail());
        form.setTel(psn.getTel());
        // 非好友只显示部分信息
        if (!form.getIsMySelf()) {
          Boolean isFriend = friendService.isFriend(SecurityUtils.getCurrentUserId(), form.getPsnId());
          if (!isFriend) {
            // 重新构建电话和邮件
            buildShowTelEmail();
          }
        }
      }
    } catch (Exception e) {
      logger.error("显示人员联系信息出错， psnId = " + form.getPsnId(), e);
    }
    return "contactInfo";
  }

  private void buildShowTelEmail() {
    String tel = form.getTel();
    String email = form.getEmail();
    int hidLen = 0;
    if (StringUtils.isNotBlank(tel)) {
      String showTel = "";
      if (tel.length() % 3 == 0) {
        hidLen = tel.length() / 3;
      } else {
        hidLen = tel.length() / 3 + 1;
      }
      int start = (tel.length() - hidLen) / 2;
      int end = start + hidLen;
      for (int i = 0; i < tel.length(); i++) {
        if (i > start && i <= end) {
          showTel += "*";
        } else {
          showTel += tel.charAt(i);
        }
      }
      form.setTel(showTel);
    }
    if (StringUtils.isNotBlank(email) && email.indexOf("@") > 0) {
      String pre = email.split("@")[0];
      String showEmail = "";
      if (pre.length() % 3 == 0) {
        hidLen = pre.length() / 3;
      } else {
        hidLen = pre.length() / 3 + 1;
      }
      for (int i = 0; i < pre.length(); i++) {
        if (i < (pre.length() - hidLen)) {
          showEmail += pre.charAt(i);
        } else {
          showEmail += "*";
        }

      }
      showEmail = showEmail + "@" + email.split("@")[1];
      form.setEmail(showEmail);
    }
  }

  /**
   * 保存联系信息, 2018-04-23不在
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @Action("/psnweb/contact/ajaxsave")
  public String savePsnContactInfo() {
    Map result = new HashMap();
    try {
      // 校验邮件格式
      String email = form.getEmail();
      if (email == null || !email.matches(EditValidateUtils.MAIL_COAD) || email.length() > 50) {
        result.put("result", "error");
        result.put("code", "email_error");
        Struts2Utils.renderJson(result, "encoding:UTF-8");
        return null;
      }
      String tel = form.getTel();
      if (StringUtils.isNotBlank(tel)) {
        if (tel.matches(EditValidateUtils.PHONE_COAD) || tel.matches(EditValidateUtils.MOBILE_COAD)) {

        } else {
          result.put("result", "error");
          result.put("code", "tel_toLong");
          Struts2Utils.renderJson(result, "encoding:UTF-8");
          return null;
        }
      }
      if (this.isMySelf()) {
        email = email.toLowerCase();
        form.setEmail(email);
        form.setPsnFirstEmail(PsnFirstEmailService.isPsnFirstEmail(form.getPsnId(), email));
        form = personManager.updatePersonContactInfo(form);
      } else {
        form = null;
      }
    } catch (Exception e) {
      logger.error("保存人员联系信息出错，psnId=" + form.getPsnId() + ", tel=" + form.getTel() + ", email=" + form.getEmail(), e);
    }
    if (form == null) {
      result.put("result", "error");
      result.put("code", "");
      Struts2Utils.renderJson(result, "encoding:UTF-8");
      return null;
    } else {
      result.put("result", "success");
      result.put("code", "");
      Struts2Utils.renderJson(result, "encoding:UTF-8");
      return null;
    }
  }

  /**
   * 编辑联系信息
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @Action("/psnweb/contact/ajaxedit")
  public String editPsnContactInfo() {
    Map data = new HashMap();
    try {
      this.isMySelf();
      Person psn = personManager.findPersonContactInfo(form.getPsnId());
      if (psn != null) {
        data.put("result", "success");
        data.put("tel", psn.getTel());
        data.put("email", psn.getEmail());
      } else {
        data.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("弹出人员联系信息编辑框出错， psnId = " + form.getPsnId(), e);
      data.put("result", "error");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 显示关键词信息
   * 
   * @return
   */
  @Action("/psnweb/keywords/ajaxshow")
  public String showPsnKeyWords() {
    this.isMySelf();
    List<PsnDisciplineKey> keyList = psnDisciplineKeyService.findPsnKeyWords(form.getPsnId());
    form.setKeywords(keyList);
    if (form.getEditKeywords() != true) {
      return "keywords";
    } else {
      List<String> recommendKeywords =
          psnDisciplineKeyService.findPsnRecommendKeyWords(form.getPsnId(), LocaleContextHolder.getLocale(), keyList);
      if (recommendKeywords != null && recommendKeywords.size() > 50) {
        recommendKeywords.subList(0, 50);
      }
      form.setRecommendKeywords(recommendKeywords);
      return "edit_keywords";
    }
  }

  /**
   * 论文推荐 -- 编辑关键词信息
   * 
   * @return
   */
  @Action("/psnweb/keywords/ajaxeditrecommend")
  public String showrecommendEditKeyWords() {
    this.isMySelf();
    List<PsnDisciplineKey> keyList = new ArrayList<>();
    String strkeyword = StringEscapeUtils.unescapeHtml4(form.getKeywordStr());
    if (StringUtils.isNotBlank(strkeyword)) {
      List<String> keyWordArray = JacksonUtils.jsonToList(strkeyword);
      for (String keyWords : keyWordArray) {
        if (StringUtils.isNoneBlank(keyWords)) {
          PsnDisciplineKey pdk = new PsnDisciplineKey();
          if (keyWords.indexOf("\\\"") != -1) {
            keyWords = StringEscapeUtils.unescapeJava(keyWords);
          }
          pdk.setKeyWords(keyWords.trim());
          keyList.add(pdk);
        }
      }
    }
    List<String> recommendKeywords =
        psnDisciplineKeyService.findPsnRecommendKeyWords(form.getPsnId(), LocaleContextHolder.getLocale(), keyList);
    if (recommendKeywords != null && recommendKeywords.size() > 50) {
      recommendKeywords.subList(0, 50);
    }
    Collections.reverse(keyList);// 按倒序显示
    form.setKeywords(keyList);
    form.setRecommendKeywords(recommendKeywords);
    return "recommend_edit_keywords";
  }

  /**
   * 保存人员关键词信息
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @Action("/psnweb/keywords/ajaxsave")
  public String savePsnKeywords() {
    Map data = new HashMap();
    try {
      if (this.isMySelf()) {
        if (form.getKeywordStr().indexOf("\"\"") == -1) {// 判断关键词是否为空
                                                         // [{"keys":""}]
          String result = psnDisciplineKeyService.savePsnKeywordsByForm(form);
          // 保存成功，删除缓存中的推荐关键词
          if ("success".equals(result)) {
            // 更新人员信息完整度
            personManager.refreshComplete(form.getPsnId());
          }
          data.put("result", "success");
          data.put("keywordsList", form.getPsnKeywords());
        } else {
          data.put("result", "blank");
        }
      } else {
        data.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("保存人员关键词信息出错， psnId = " + form.getPsnId(), e);
      data.put("result", "error");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 显示人员科技领域
   * 
   * @return
   */
  @Action("/psnweb/sciencearea/ajaxshow")
  public String showPsnScienceArea() {
    try {
      this.isMySelf();
      form = scienceAreaService.buildPsnScienceAreaInfo(form);
    } catch (Exception e) {
      logger.error("显示人员科技领域信息出错， psnId = " + form.getPsnId(), e);
    }
    return "sciencearea";
  }

  /**
   * 获取人员科技领域json数据
   * 
   * @return
   */
  @Action("/psnweb/sciencearea/ajaxjson")
  public String getPsnScienceArea() {
    try {
      this.isMySelf();
      if (form.getIsMySelf()) {
        Map<String, Object> result = new HashMap<>();
        Person person = personManager.getPerson(form.getPsnId());
        result.put("des3psnId", Des3Utils.encodeToDes3(form.getPsnId().toString()));
        result.put("zhName", person.getZhName());
        result.put("name", person.getName());
        form = scienceAreaService.findPsnScienceArea(form);
        List<PsnScienceArea> scienceAreaList = form.getScienceAreaList();
        List<String> scienceAreas = new ArrayList<>();
        for (PsnScienceArea area : scienceAreaList) {
          if (StringUtils.isNotBlank(area.getScienceArea())) {
            scienceAreas.add(area.getScienceArea());
          }
        }
        result.put("scienceArea", scienceAreas);
        Struts2Utils.renderJson(result, "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("获取人员科技领域信息出错， psnId = " + form.getPsnId(), e);
    }
    return null;
  }

  /**
   * 保存人员科技领域信息
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @Action("/psnweb/sciencearea/ajaxsave")
  public String savePsnScienceArea() {
    Map data = new HashMap();
    try {
      if (this.isMySelf()) {
        if (StringUtils.isNoneBlank(form.getScienceAreaIds())) {
          String result = scienceAreaService.savePsnScienceArea(form.getPsnId(), form.getScienceAreaIds());
          if ("success".equals(result)) {
            // 更新人员信息完整度
            personManager.refreshComplete(form.getPsnId());
            initPsnFundRecommend(form.getPsnId());// 初始化基金推荐条件
          }
          data.put("result", result);
        } else {
          data.put("result", "blank");
        }
      } else {
        data.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("保存人员科技领域信息出错， psnId = " + form.getPsnId(), e);
      data.put("result", "error");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 调用接口，初始化基金推荐条件
   * 
   * @param psnId
   */
  public void initPsnFundRecommend(Long psnId) {
    if (psnId != null) {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
      params.add("psnIds", psnId.toString());
      postUrl(params, domainMobile + "/prjdata/initpsnrecommedfund");
    }
    return;

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private Map<String, Object> postUrl(MultiValueMap param, String url) {
    HttpEntity<MultiValueMap> httpEntity = this.getEntity(param);// 创建头部信息
    return restTemplate.postForObject(url, httpEntity, Map.class);
  }

  @SuppressWarnings("rawtypes")
  private HttpEntity getEntity(MultiValueMap param) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap> HttpEntity = new HttpEntity<MultiValueMap>(param, headers);
    return HttpEntity;
  }

  /**
   * 编辑人员科技领域
   * 
   * @return
   */
  @Action("/psnweb/sciencearea/ajaxedit")
  public String editPsnScienceArea() {
    try {
      this.isMySelf();
      // 查找当前人员的科技领域
      List<PsnScienceArea> scienceAreaList = scienceAreaService.findPsnScienceAreaList(form.getPsnId(), 1);
      // 获取科技领域构建成的Map
      form.setCategoryMap(categoryMapBaseService.buildCategoryMapBaseInfo(scienceAreaList));
      // 最多5个
      if (scienceAreaList != null && scienceAreaList.size() > 5) {
        scienceAreaList = scienceAreaList.subList(0, 4);
      }
      form.setScienceAreaList(scienceAreaList);
      StringBuilder scienceAreaIds = new StringBuilder();
      for (PsnScienceArea area : scienceAreaList) {
        scienceAreaIds.append("," + area.getScienceAreaId());
      }
      form.setScienceAreaIds(scienceAreaIds.toString());
    } catch (Exception e) {
      logger.error("弹出人员科技领域弹出框出错，psnId = " + form.getPsnId(), e);
    }
    return "psnEditScienceArea";
  }

  /**
   * 论文推荐--编辑人员科技领域
   * 
   * @return
   */
  @Action("/psnweb/sciencearea/ajaxeditScienceArea")
  public String editScienceArea() {
    try {
      this.isMySelf();
      List<PsnScienceArea> scienceAreaList = new ArrayList<PsnScienceArea>();
      String scienceAreaIds = form.getScienceAreaIds();
      if (StringUtils.isNoneBlank(scienceAreaIds)) {
        scienceAreaList = scienceAreaService.findPsnScienceAreaById(form);
        // 最多3个
        if (scienceAreaList != null && scienceAreaList.size() > 3) {
          scienceAreaList = scienceAreaList.subList(0, 2);
        }
      }
      if (StringUtils.isNoneBlank(form.getScienceAreaIds())) {
        form.setScienceAreaIds("," + form.getScienceAreaIds());
      }
      form.setScienceAreaList(scienceAreaList);
      // 获取科技领域构建成的Map
      form.setCategoryMap(categoryMapBaseService.buildCategoryMapBaseInfo(scienceAreaList));
    } catch (Exception e) {
      logger.error("弹出人员科技领域弹出框出错，psnId = " + form.getPsnId(), e);
    }
    return "editScienceArea";
  }

  /**
   * 编辑人员科技领域
   * 
   * @return
   */
  @Action("/psnweb/sciencearea/ajaxaddpubarea")
  public String ajaxaddpubarea() {
    try {
      // 获取科技领域构建成的Map
      categoryMapBaseService.buildCategoryMapBaseInfo(form);
    } catch (Exception e) {
      logger.error("弹出科技领域弹出框出错，psnId = " + form.getPsnId(), e);
    }
    return "addPubScienceArea";
  }

  /**
   * 编辑人员科技领域
   * 
   * @return
   */
  @Action("/psnweb/sciencearea/ajaxaddprjarea")
  public String ajaxaddprjarea() {
    try {
      // 获取科技领域构建成的Map
      categoryMapBaseService.buildCategoryMapBaseInfo(form);
    } catch (Exception e) {
      logger.error("弹出科技领域弹出框出错，psnId = " + form.getPsnId(), e);
    }
    return "addPrjScienceArea";
  }

  /**
   * 编辑科技领域 例子基金推荐添加科技领域
   * 
   * @return
   */
  @Action("/psnweb/sciencearea/ajaxaddarea")
  public String ajaxaddarea() {
    try {
      // 默认限制选中3个科技领域
      if (form.getScienceAreaNum() == null || form.getScienceAreaNum() == 0) {
        form.setScienceAreaNum(3);
      }
      // 获取科技领域构建成的Map
      categoryMapBaseService.buildCategoryMapBaseInfo(form);
    } catch (Exception e) {
      logger.error("弹出科技领域弹出框出错，psnId = " + form.getPsnId(), e);
    }
    return "addScienceArea";
  }

  /**
   * 认同科技领域
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @Action("/psnweb/sciencearea/ajaxidentify")
  public String identifyScienceArea() {
    Map data = new HashMap();
    try {
      this.isMySelf();
      Integer sum = scienceAreaService.saveIdentificationScienceArea(form.getPsnId(), form.getOneScienceAreaId(),
          SecurityUtils.getCurrentUserId(), form.getIdenSum());
      if (sum != null) {
        data.put("result", "success");
        data.put("sum", sum.toString());
      } else {
        data.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("认同人员科技领域出错， psnId = " + form.getPsnId() + ", scienceAreaId = " + form.getOneScienceAreaId(), e);
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  // 关键词认同
  @Action("/psnweb/keyword/ajaxIdentificKeyword")
  public void identificKeyword() throws Exception {
    if (form.getDes3PsnId() != null && form.getOneKeyWordId() != null) {
      Map<String, Object> resultMap = new HashMap<String, Object>();
      try {
        Long psnId = Long.valueOf(ServiceUtil.decodeFromDes3(form.getDes3PsnId()));
        Long kwId = Long.valueOf(form.getOneKeyWordId());
        this.isMySelf();
        if (!form.getIsMySelf()) {
          // 认同关键词
          identificationService.identificKeyword(psnId, kwId, SecurityUtils.getCurrentUserId());
          Person person = personManager.getPsnNameAndAvatars(SecurityUtils.getCurrentUserId());// 获取头像用
          // 获取认同数
          resultMap.put("sum", identificationService.countOneIdentification(psnId, kwId));
          resultMap.put("avatar", person.getAvatars());
          resultMap.put("result", "success");
          Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
        } else {
          resultMap.put("result", "fail");
          Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
        }
      } catch (Exception e) {
        logger.error("认同关键词出错：认同人psnid=" + form.getPsnId() + "keywordId" + form.getOneKeyWordId(), e);
        resultMap.put("result", "fail");
        Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
      }
    }
  }

  /**
   * 显示人员代表性项目
   * 
   * @return
   */
  @Action("/psnweb/representprj/ajaxshow")
  public String showPsnRepresentPrj() {
    try {
      this.isMySelf();
      form = representPrjService.buildPsnRepresentPrjInfo(form);
      if (form.getRepresentPrjList() != null) {
        StringBuilder prjIds = new StringBuilder();
        for (Project prj : form.getRepresentPrjList()) {
          prjIds.append(prj.getId().toString() + ",");
        }
        form.setRepresentPrjIds(prjIds.toString());
      }
    } catch (Exception e) {
      logger.error("显示人员代表性项目出错， psnId = " + form.getPsnId(), e);
    }
    return "representprj";
  }

  /**
   * 编辑人员代表性项目
   * 
   * @return
   */
  @Action("/psnweb/representprj/ajaxedit")
  public String editPsnRepresentPrj() {
    try {
      this.isMySelf();
      List<Project> representPrjList =
          representPrjService.findPsnRepresentPrjInfoList(form.getPsnId(), 0, form.getIsMySelf());
      if (CollectionUtils.isNotEmpty(representPrjList)) {
        for (Project prj : representPrjList) {
          this.buildPrjInfoByLanguage(prj);
        }
      }
      form.setRepresentPrjList(representPrjList);
    } catch (Exception e) {
      logger.error("弹出人员代表性项目编辑框出错，psnId=" + form.getPsnId(), e);
    }
    return "editRepresentPrj";
  }

  /**
   * 保存人员代表性项目
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @Action("/psnweb/representprj/ajaxsave")
  public String savePsnRepresentPrj() {
    Map data = new HashMap();
    try {
      // 只有本人才能操作
      if (this.isMySelf()) {
        form.setOpenPrjList(representPrjService.savePsnRepresentPrj(form.getPsnId(), form.getRepresentPrjIds()));
        data.put("result", "success");
      } else {
        data.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("保存人员代表性项目出错， psnId = " + form.getPsnId() + ", prjIds=" + form.getRepresentPrjIds(), e);
      data.put("result", "error");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 查找人员公开项目
   */
  @Action("/psnweb/representprj/ajaxopen")
  public String findPsnOpenPrjList() {
    try {
      this.isMySelf();
      form.setAnyUser(PsnCnfConst.ALLOWS_FRIEND + PsnCnfConst.ALLOWS_SELF);
      form = representPrjService.findPsnOpenPrjListByForm(form);
    } catch (Exception e) {
      logger.error("查找人员公开项目出错， psnId = " + form.getPsnId(), e);
    }
    return "openPrj";
  }

  /**
   * 显示人员工作经历
   * 
   * @return
   */
  @Action("/psnweb/workhistory/ajaxshow")
  public String showPsnWorkHistory() {
    try {
      this.isMySelf();
      // form.setWorkList(psnWorkHistoryInsInfoService.getPsnWorkHistory(form.getPsnId()));
      form = workHistoryService.buildPsnWorkHistoryListInfo(form);
    } catch (Exception e) {
      logger.error("显示人员工作经历出错， psnId = " + form.getPsnId(), e);
    }
    return "workHis";
  }

  /**
   * 编辑人员工作经历
   * 
   * @return
   */
  @Action("/psnweb/workhistory/ajaxedit")
  public String editPsnWorkHistory() {
    // 获取工作经历记录ID
    if (form.getWorkId() == null) {
      if (form.getEncodeWrokId() != null) {
        form.setWorkId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getEncodeWrokId())));
      }
    }
    form.setWorkHistory(psnWorkHistoryInsInfoService.findWorkHistoryById(form.getWorkId()));
    return "editWork";
  }

  /**
   * 构建工作经历选项
   * 
   * @return
   */
  @Action("/psnweb/workhistory/ajaxlist")
  public String queryPsnWorkList() {
    try {
      this.isMySelf();
      form = workHistoryService.buildPsnWorkHistorySelector(form);
    } catch (Exception e) {
      logger.error("构建工作经历选项出错， psnId = " + form.getPsnId(), e);
    }
    return "workSelect";
  }

  // 移动端 构建工作经历选项
  @Action("/psndata/mobile/psninfo/ajaxworkhistorylist")
  public void queryPsnWorkListByMobile() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List<WorkHistory> showWorkList = workHistoryService.buildSimplePsnWorkHistorySelector(form.getPsnId());
      map.put("result", showWorkList);
      map.put("status", "success");
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("构建工作经历选项出错， psnId = " + form.getPsnId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }

  /**
   * 显示人员教育经历
   * 
   * @return
   */
  @Action("/psnweb/eduhistory/ajaxshow")
  public String showPsnEduHistory() {
    try {
      this.isMySelf();
      educationHistoryService.buildPsnEduHistoryListInfo(form);
    } catch (Exception e) {
      logger.error("显示人员教育经历出错， psnId = " + form.getPsnId(), e);
    }
    return "showEdu";
  }

  /**
   * 编辑人员教育经历
   * 
   * @return
   */
  @Action("/psnweb/eduhistory/ajaxedit")
  public String editPsnEduHistory() {
    try {
      this.isMySelf();
      if (form.getEduId() == null) {
        if (form.getEncodeEduId() != null) {
          form.setEduId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getEncodeEduId())));
        }
      }
      form.setEduHistory(educationHistoryService.findEducationHistoryById(form.getEduId()));
    } catch (Exception e) {
      logger.error("弹出人员教育经历编辑框出错， psnId = " + form.getPsnId() + ", eduId = " + form.getEduId(), e);
    }
    return "editEdu";
  }

  // 获取个人教育经历详情
  @Action("/psndata/mobile/educatehistory/get")
  public String getEducateHistoryByEduId() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      EducationHistory educationHistory =
          educationHistoryService.getEducationHistoryByEduId(form.getPsnId(), form.getEduId());
      map.put("status", "success");
      map.put("result", educationHistory);
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("获取个人经历失败， psnId = " + form.getPsnId() + ", workId = " + form.getWorkId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 编辑人员主页头部信息
   * 
   * @return
   */
  @Action("/psnweb/psninfo/ajaxedit")
  public String editPsnInfo() {
    try {
      this.isMySelf();
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setNeedStatistics(false);
      form = personManager.getUnifiedPsnInfoByForm(form);
      form = workHistoryService.buildPsnWorkHistorySelector(form);
      form.setPsnBriefDesc(personManager.getPersonBrief(form.getPsnId()));
      form = psnHomepageService.getPsnNameInfo(form);
    } catch (Exception e) {
      logger.error("弹出人员主页头部信息编辑框出错, psnId = " + form.getPsnId(), e);
    }
    return "psnInfo";
  }

  /**
   * 移动端 编辑人员主页头部信息
   */
  @SuppressWarnings("unchecked")
  @Action("/psndata/mobile/psninfo/edit")
  public void editPsninFoFromMobile() throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      this.isMySelf();
      form.setNeedStatistics(false);
      form = personManager.getUnifiedPsnInfoByForm(form);
      form = workHistoryService.buildPsnWorkHistorySelector(form);
      form.setPsnBriefDesc(personManager.getPersonBrief(form.getPsnId()));
      form = psnHomepageService.getPsnNameInfo(form);
      if (form.getPsnInfo() != null) {
        form.setRegionId(form.getPsnInfo().getRegionId());
        // 所在单位
        form.setInsName(form.getPsnInfo().getInsName());
        form.setDepartment(form.getPsnInfo().getDepartment());
        form.setPosition(form.getPsnInfo().getPosition());
      }
      form.setWorkId(form.getCurrentWorkId());
      map.put("result", "success");
      map.put("data", form);
    } catch (Exception e) {
      logger.error("移动端获取人员主页头部信息出错, psnId = " + form.getPsnId(), e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }

  /**
   * 移动端 编辑个人简介
   */
  @SuppressWarnings("unchecked")
  @Action("/psndata/mobile/psnintro/edit")
  public void editPsnintro() throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      String psnBriefDesc = personManager.getPersonBrief(form.getPsnId());
      map.put("status", "success");
      map.put("result", psnBriefDesc);
    } catch (Exception e) {
      logger.error("获取个人简介出错,psnId=" + form.getPsnId(), e);
      map.put("status", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }

  /**
   * 移动端 保存个人简介
   */
  @SuppressWarnings("unchecked")
  @Action("/psndata/mobile/psnintro/save")
  public void savePsnintro() throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      personManager.savePersonBrief(form.getPsnBriefDesc(), form.getPsnId());
      map.put("status", "success");
    } catch (Exception e) {
      logger.error("保存个人简介出错,psnId=" + form.getPsnId(), e);
      map.put("status", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }

  /**
   * 保存人员头像
   */
  @SuppressWarnings("unchecked")
  @Action("/psnweb/psninfo/ajaxSaveAvatar")
  public String ajaxSaveAvatar() throws Exception {
    Map jsonRes = new HashMap();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      personManager.saveAvatars(psnId, form.getAvatars());
      jsonRes.put("result", "success");
      Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("保存个人头像出错", e);
      jsonRes.put("result", "error");
      Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
      return null;
    }

    return null;
  }

  /**
   * 保存人员单位、部门等信息
   * 
   * @return
   */
  @Action("/psnweb/baseinfo/ajaxsave")
  public String savePsnInfo() {
    Map<String, String> data = new HashMap<String, String>();
    try {
      if (this.isMySelf()) {
        form.setPsnId(SecurityUtils.getCurrentUserId());
        // 校验姓名参数
        String check = this.checkNameEditParams(form);
        if (StringUtils.isNotBlank(check)) {
          data.put("result", "checkError");
          data.put("msg", check);
        } else {
          String result = personManager.editPsnName(form);
          if ("success".equals(result)) {
            personManager.savePersonBrief(form.getPsnBriefDesc(), form.getPsnId());
            personManager.savePsnInfoNew(form);
            // 更新solr个人信息
            // psnSolrInfoModifyService.updateSolrPsnInfo(form.getPsnId());
            personalManager.refreshPsnSolrInfoByTask(form.getPsnId());
            // 更新sie的人员信息
            personalManager.updateSIEPersonInfo(form.getPsnId());
            form = personManager.getUnifiedPsnInfoByForm(form);
            data.put("result", "success");
            data.put("insAndDept", form.getInsAndDepInfo());
            data.put("posiAndTitolo", form.getPositionAndTitolo());
            data.put("regionName", form.getPsnRegionInfo());
            data.put("psnName", form.getName());
            data.put("avatars", form.getAvatars());
          } else {
            data.put("result", "error");
          }
        }
      } else {
        data.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("保存人员主页人员头部信息出错，psnId = " + form.getPsnId(), e);
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }


  /**
   * 移动端 保存个人头衔
   * 
   * @return
   */
  @Action("/psndata/mobile/psninfo/save")
  public void savePsnInfoByMobile() {
    Map<String, String> data = new HashMap<String, String>();
    try {
      // 校验姓名参数
      String check = this.checkNameEditParams(form);
      if (StringUtils.isNotBlank(check)) {
        data.put("status", "error");
      } else {
        String result = personManager.editPsnName(form);
        if ("success".equals(result)) {
          personManager.savePsnInfoNew(form);
          // 更新solr个人信息
          personalManager.refreshPsnSolrInfoByTask(form.getPsnId());
          // 更新sie的人员信息
          personalManager.updateSIEPersonInfo(form.getPsnId());
          form = personManager.getUnifiedPsnInfoByForm(form);
          data.put("status", "success");
        } else {
          data.put("status", "error");
        }
      }
    } catch (Exception e) {
      data.put("status", "error");
      logger.error("移动端保存人员头衔信息出错，psnId = " + form.getPsnId(), e);
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
  }


  /**
   * 保存人员主页各模块查看权限
   * 
   * @return
   */
  @Action("/psnweb/homepage/ajaxsavemod")
  public String savePsnConfInfo() {
    try {
      if (Struts2Utils.getParameter("anyMod") != null) {
        PsnConfigMoudle cnfMoudle = new PsnConfigMoudle();
        Long anyMod = Long.valueOf(Struts2Utils.getParameter("anyMod"));
        cnfMoudle.setAnyMod(anyMod);
        psnCnfService.save(SecurityUtils.getCurrentUserId(), cnfMoudle);
        // 更新人员solr信息
        // psnSolrInfoModifyService.updateSolrPsnInfo(form.getPsnId());
        personalManager.refreshPsnSolrInfoByTask(
            com.smate.core.base.utils.number.NumberUtils.isNotNullOrZero(form.getPsnId()) ? form.getPsnId()
                : SecurityUtils.getCurrentUserId());
      }
    } catch (Exception e) {
      logger.error("保存人员主页模块查看权限出错， psnId=" + SecurityUtils.getCurrentUserId(), e);
    }
    String resultJson = "{\"result\":\"success\"}";
    Struts2Utils.renderJson(resultJson, "encoding:UTF-8");
    return null;
  }

  /**
   * 通过superRegionId查询国家和区域数据
   * 
   * @return
   */
  @Action("/psnweb/common/ajaxRegion")
  public String ajaxRegion() throws Exception {
    String jsonRes = "";
    try {
      Long superRegionIds = null;
      if (StringUtils.isNotBlank(superRegionId)) {
        superRegionIds = Long.valueOf(superRegionId);
      }
      jsonRes = psnConstRegionService.findRegionJsonData(superRegionIds);

    } catch (Exception e) {
      String resultJson = "{'result':'error'}";
      Struts2Utils.renderJson(resultJson, "encoding:UTF-8");
      return null;
    }
    Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
    return null;
  }

  /**
   * 移动端个人主页-个人头衔地区数据加载
   * 
   * @return
   */
  @Action("/psndata/mobile/psninfo/ajaxRegion")
  public String ajaxPsnInfoRegion() throws Exception {
    Map<String, String> data = new HashMap<String, String>();
    try {
      LocaleContextHolder.setLocale(Locale.CHINA, true);
      List<Map<String, String>> countryList = psnConstRegionService.findRegionList(null);
      List<Map<String, String>> provinceList = psnConstRegionService.findRegionList(156L);
      HashMap<String, List<Map<String, String>>> cityMap = new HashMap<>();
      for (Map<String, String> province : provinceList) {
        List<Map<String, String>> thirdRegionList =
            psnConstRegionService.findRegionList(Long.parseLong(province.get("code")));
        cityMap.put(province.get("code"), thirdRegionList);
      }
      data.put("countryList", JacksonUtils.listToJsonStr(countryList));
      data.put("provinceList", JacksonUtils.listToJsonStr(provinceList));
      data.put("cityMap", JacksonUtils.mapToJsonStr(cityMap));
      data.put("status", "success");
    } catch (Exception e) {
      data.put("status", "error");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 通过superRegionId来判断是否有下一级单位，默认是中国
   */
  @Action("/psnweb/psninfo/ajaxGetNextLevelRegion")
  public String getSecondRegionByName() {
    try {
      Long superRegionIds = null;
      if (StringUtils.isNotBlank(superRegionId)) {
        superRegionIds = Long.valueOf(superRegionId);
      } else {
        superRegionIds = 156L;
      }
      Map<String, String> map = psnConstRegionService.findNextLevelRegionId(superRegionIds);
      Struts2Utils.renderJson(map, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("通过国家名字来判断是否有下一级单位信息出错", e);
    }
    return null;
  }

  /**
   * 通过region_id 反过来获取地区
   * 
   * @return
   */
  @Action("/psnweb/psninfo/ajaxRegion")
  public String getRegion() {
    try {
      Long superRegionIds = null;
      if (StringUtils.isNotBlank(superRegionId)) {
        superRegionIds = Long.valueOf(superRegionId);
      }
      Map<String, String> map = psnConstRegionService.findDataByRegionId(superRegionIds);
      Struts2Utils.renderJson(map, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("查询地区基础信息出错", e);
    }
    return null;
  }

  @Action("/psnweb/psninfo/ajaxbase")
  public String loadPsnBaseInfo() {
    try {
      isMySelf();
      form = personManager.getUnifiedPsnInfoByForm(form);
    } catch (Exception e) {
      logger.error("查询人员基础信息出错， psnId = " + form.getPsnId(), e);
    }
    return "baseInfo";
  }

  @Action("/psnweb/name/ajaxedit")
  public String ajaxEditPsnName() {
    String resultJson = "";
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      // 校验姓名参数
      String check = this.checkNameEditParams(form);
      if (StringUtils.isNotBlank(check)) {
        resultJson = "{'result':'" + check + "'}";
      } else {
        resultJson = "{'result':'" + personManager.editPsnName(form) + "'}";
      }
    } catch (Exception e) {
      logger.error("修改人员姓名出错， psnId = " + form.getPsnId(), e);
      resultJson = "{'result':'error'}";
    }
    Struts2Utils.renderJson(resultJson, "encoding:UTF-8");
    return null;
  }

  @Action("/psnweb/pinyin/ajaxparse")
  public String parseWordToPinYin() {
    Map<String, String> data = new HashMap<String, String>();
    try {
      if (StringUtils.isNotBlank(form.getWord())) {
        // 中英文姓名包含的·转换为正常字符
        form.setWord(form.getWord().replaceAll("&middot;", "·"));
        data.put("result",
            psnHomepageService.parseWordToPinYin(form.getWord(), form.getWordType(), form.getWordLength()));
      }
    } catch (Exception e) {
      logger.error("将字符转成拼音失败，word = " + form.getWord(), e);
      data.put("result", "error");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 字符转拼音 用-连接 凌风 Ling-Feng
   * 
   * @return
   */
  @Action("/psnweb/pinyin/ajaxregisterparse")
  public String registerParseWordToPinYin() {
    Map<String, String> data = new HashMap<String, String>();
    try {
      if (StringUtils.isNotBlank(form.getWord())) {
        // 中英文姓名包含的·转换为正常字符
        form.setWord(form.getWord().replaceAll("&middot;", "·"));
        data.put("result", psnHomepageService.registerParseWordToPinYin(form.getWord(), form.getWordLength()));
      }
    } catch (Exception e) {
      logger.error("将字符转成拼音失败，word = " + form.getWord(), e);
      data.put("result", "error");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 提示人员完善关键词或科技领域
   * 
   * @return
   */
  @Action("/psnweb/improve/ajaxinfo")
  public String improvePsnInfo() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      // 判断是否需要弹出完善信息框
      Map<String, Boolean> needImprove = psnInfoImproveService.psnHasScienceAreaAndKeywords(form.getPsnId());
      // 需要，则查询人员分类信息，自动选择科技领域和关键词用
      if (needImprove != null && needImprove.get("needArea") != null && needImprove.get("needKeyword") != null
          && needImprove.get("needWorkEdu") != null) {
        boolean needArea = needImprove.get("needArea");
        boolean needKeyWords = needImprove.get("needKeyword");
        boolean needWorkEdu = needImprove.get("needWorkEdu");
        form.setNeedArea(needArea);
        form.setNeedKeywords(needKeyWords);
        form.setNeedWorkEdu(needWorkEdu);
        if (needArea) {
          form = psnInfoImproveService.buildPsnImproveScienceAreaInfo(form);
        } else if (needKeyWords) {
          form = psnInfoImproveService.buildPsnImproveKeywordsInfo(form);
        }
        WorkHistory workHistory = workHistoryService.getWorkHistoryByPsnId(form.getPsnId());
        if (workHistory != null) {
          form.setInsName(workHistory.getInsName());
          form.setInsId(workHistory.getInsId());
        }
      } else {
        return null;
      }
    } catch (Exception e) {
      logger.error("构建人员信息完善页面出错， psnId = " + form.getPsnId(), e);
      return null;
    }
    return "improveInfo";
  }

  /**
   * 完善科技领域信息
   * 
   * @return
   */
  @Action("/psnweb/improve/ajaxarea")
  public String improveScienceArea() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form = psnInfoImproveService.buildPsnImproveScienceAreaInfo(form);

    } catch (Exception e) {
      logger.error("构建人员关键词信息完善页面出错， psnId = " + form.getPsnId(), e);
    }
    return "improve_sciencearea";
  }

  /**
   * 完善关键词信息
   * 
   * @return
   */
  @Action("/psnweb/improve/ajaxkeywords")
  public String improveKeywords() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form = psnInfoImproveService.buildPsnImproveKeywordsInfo(form);
    } catch (Exception e) {
      logger.error("构建人员科技领域信息完善页面出错， psnId = " + form.getPsnId(), e);
    }
    return "improve_keywords";
  }

  /**
   * 检查人员配置信息（psn_conf_xxx表），有问题就重新初始化
   * 
   * @return
   */
  @Action("/psnweb/psnconf/ajaxinit")
  public String initPsnConfInfo() {
    Map<String, String> data = new HashMap<String, String>();
    try {
      // 如果配置信息有问题，就初始化一下
      personalManager.initPsnConfigInfoByTask(SecurityUtils.getCurrentUserId());
      data.put("result", "success");
    } catch (Exception e) {
      logger.error("检查或初始化人员配置信息出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      data.put("result", "error");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 判断是否是本人
   * 
   * @return
   */
  private boolean isMySelf() {
    boolean isSelf = true;
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    if (form.getPsnId() == null || form.getPsnId() == 0) {
      if (form.getDes3PsnId() != null) {
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
      }
      if (form.getPsnId() == null) {
        form.setPsnId(currentPsnId);
      }
    }
    if (currentPsnId != null && currentPsnId != 0) {
      if (currentPsnId.longValue() == form.getPsnId().longValue()) {
        isSelf = true;
      } else {
        isSelf = false;
      }
    }
    form.setIsMySelf(isSelf);
    return isSelf;
  }

  /**
   * 判断是否有这个账号
   */
  private boolean isHavePsn() {
    if (form.getDes3PsnId() != null) {
      Person psn =
          personManager.findPersonContactInfo(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
      if (psn == null) {
        return false;
      }
    }
    return true;
  }

  /**
   * 构建seo标签需要使用的人员简介和关键词信息
   * 
   * @param form
   * @param isbrief 是否构建简介
   * @param iskeywords 是否构建关键词
   */

  private void buildSeoTagInfo(PersonProfileForm form, Boolean isbrief, Boolean iskeywords) {
    if (isbrief) {
      form.setPsnBriefDesc(personManager.getPersonBrief(form.getPsnId()));
    }
    if (iskeywords) {
      List<PsnDisciplineKey> keyList = psnDisciplineKeyService.findPsnKeyWords(form.getPsnId());
      StringBuilder sb = new StringBuilder();
      String splitTag = ", ";
      if (keyList != null) {
        for (PsnDisciplineKey psnKeywords : keyList) {
          sb.append(splitTag + psnKeywords.getKeyWords());
        }
        if (StringUtils.isNotBlank(sb.toString())) {
          form.setPsnKeywordsStr(StringUtils.trim(sb.toString().substring(2)));
        }
      }
    }
  }

  public static void main(String[] args) {
    List<PsnDisciplineKey> keyList = new ArrayList<PsnDisciplineKey>();
    for (PsnDisciplineKey psnKeywords : keyList) {
    }
    keyList = null;
    for (PsnDisciplineKey psnKeywords : keyList) {
    }

  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PersonProfileForm();
    }
    // 设置语言环境
    form.setLanguage(LocaleContextHolder.getLocale().toString());

  }

  /**
   * 构建ProjectInfo对象
   * 
   * @param prj
   */
  private void buildPrjInfoByLanguage(Project prj) {
    ProjectInfo prjInfo = new ProjectInfo();
    prjInfo = prjInfo.buildProjectInfo(prj, prjInfo);
    if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
      prjInfo.setShowAuthorNames(
          StringUtils.isNotBlank(prjInfo.getAuthorNamesEn()) ? prjInfo.getAuthorNamesEn() : prjInfo.getAuthorNames());
      prjInfo.setShowTitle(StringUtils.isNotBlank(prjInfo.getEnTitle()) ? prjInfo.getEnTitle() : prjInfo.getZhTitle());
      prjInfo.setShowBriefDesc(
          StringUtils.isNotBlank(prjInfo.getBriefDescEn()) ? prjInfo.getBriefDescEn() : prjInfo.getBriefDesc());
    } else {
      prjInfo.setShowAuthorNames(
          StringUtils.isNotBlank(prjInfo.getAuthorNames()) ? prjInfo.getAuthorNames() : prjInfo.getAuthorNamesEn());
      prjInfo.setShowTitle(StringUtils.isNotBlank(prjInfo.getZhTitle()) ? prjInfo.getZhTitle() : prjInfo.getEnTitle());
      prjInfo.setShowBriefDesc(
          StringUtils.isNotBlank(prjInfo.getBriefDesc()) ? prjInfo.getBriefDesc() : prjInfo.getBriefDescEn());
    }
    prj.setPrjInfo(prjInfo);
  }

  private String checkNameEditParams(PersonProfileForm form) {
    StringBuilder sb = new StringBuilder();
    if (EditValidateUtils.isNull(form.getZhLastName(), 20, null)) {
      sb.append("中文的姓长度超长");
    }

    if (EditValidateUtils.isNull(form.getZhFirstName(), 40, null)) {
      if (sb.length() > 0) {
        sb.append(",");
      }
      sb.append("中文的名长度超长");
    }

    if (EditValidateUtils.hasParam(form.getFirstName(), 40, null)) {
      if (sb.length() > 0) {
        sb.append(",");
      }
      sb.append("名（First Name）长度超长");
    }

    if (EditValidateUtils.hasParam(form.getLastName(), 20, null)) {
      if (sb.length() > 0) {
        sb.append(",");
      }
      sb.append("姓（Last Name）长度超长");
    }

    if (EditValidateUtils.isNull(form.getOtherName(), 60, null)) {
      if (sb.length() > 0) {
        sb.append(",");
      }
      sb.append("别名（Middle Name）长度超长");
    }

    if (EditValidateUtils.isNull(form.getTitolo(), 200, null)) {
      if (sb.length() > 0) {
        sb.append(",");
      }
      sb.append("头衔长度超长");
    }

    if (sb.length() > 0) {
      return sb.toString();
    }
    return null;
  }

  /**
   * 是否已登录
   * 
   * @return
   */
  private boolean isLogin() {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId == null || psnId == 0) {
      form.setIsLogin(false);
      return false;
    } else {
      form.setIsLogin(true);
      return true;
    }
  }

  @Override
  public PersonProfileForm getModel() {
    return form;
  }

  public String getSuperRegionId() {
    return superRegionId;
  }

  public void setSuperRegionId(String superRegionId) {
    this.superRegionId = superRegionId;
  }

}
