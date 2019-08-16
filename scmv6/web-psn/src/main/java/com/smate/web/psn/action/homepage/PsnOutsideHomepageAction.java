package com.smate.web.psn.action.homepage;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.web.util.HtmlUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.service.pc.homepage.PsnOutsideHomepageService;
import com.smate.web.psn.service.profile.EducationHistoryService;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.PersonalManager;
import com.smate.web.psn.service.profile.PsnDisciplineKeyService;
import com.smate.web.psn.service.profile.WorkHistoryService;
import com.smate.web.psn.service.representprj.RepresentPrjService;
import com.smate.web.psn.service.sciencearea.ScienceAreaService;

/**
 * 站外主页Action
 *
 * @author wsn
 * @createTime 2017年4月25日 上午11:39:20
 *
 */
@Results({@Result(name = "outsideHomepage", location = "/WEB-INF/jsp/outsidehomepage/outside_psn_homepage_main.jsp"),
    @Result(name = "outsideBrief", location = "/WEB-INF/jsp/outsidehomepage/outside_psn_briefdesc.jsp"),
    @Result(name = "outsideKw", location = "/WEB-INF/jsp/outsidehomepage/outside_psn_keywords.jsp"),
    @Result(name = "outsideprj", location = "/WEB-INF/jsp/outsidehomepage/outside_psn_representprj.jsp"),
    @Result(name = "outsideWork", location = "/WEB-INF/jsp/outsidehomepage/outside_psn_workhistory.jsp"),
    @Result(name = "outsideEdu", location = "/WEB-INF/jsp/outsidehomepage/outside_psn_eduhistory.jsp"),
    @Result(name = "contactInfo", location = "/WEB-INF/jsp/outsidehomepage/psn_contactinfo.jsp"),
    @Result(name = "outsideSA", location = "/WEB-INF/jsp/outsidehomepage/outside_psn_sciencearea.jsp")})
public class PsnOutsideHomepageAction extends ActionSupport implements ModelDriven<PersonProfileForm>, Preparable {

  private static final long serialVersionUID = -3688545454024677805L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private PersonProfileForm form;
  @Value("${domainscm}")
  private String domainScm;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PersonalManager personalManager;
  @Autowired
  private WorkHistoryService workHistoryService;
  @Autowired
  private EducationHistoryService educationHistoryService;
  @Autowired
  private ScienceAreaService scienceAreaService;
  @Autowired
  private PsnDisciplineKeyService psnDisciplineKeyService;
  @Autowired
  private RepresentPrjService representPrjService;
  @Autowired
  private PsnOutsideHomepageService psnOutsideHomepageService;
  private String realUrlParamet;

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PersonProfileForm();
    }
    // 设置语言环境
    form.setLanguage(LocaleContextHolder.getLocale().toString());
  }

  @Override
  public PersonProfileForm getModel() {
    return form;
  }

  /**
   * 进入站外主页
   * 
   * @return
   */
  @Action("/psnweb/outside/homepage")
  public String psnOutsideHomepage() {
    try {
      // 短地址跳转
      if (StringUtils.isNotBlank(this.getRealUrlParamet())) {
        Map<String, String> map = JacksonUtils.jsonToMap(this.getRealUrlParamet());
        if (map != null) {
          form.setDes3PsnId(map.get("des3PsnId"));
          if (StringUtils.isNotBlank(map.get("psnId"))) {
            form.setPsnId(Long.parseLong(map.get("psnId")));
          }
        }
      }
      // 是否已登录
      boolean loginStatus = this.isLogin();
      // 已登录跳转到站内查看他人主页的页面
      if (loginStatus) {
        Struts2Utils.getResponse().sendRedirect(
            domainScm + "/psnweb/homepage/show?des3PsnId=" + URLEncoder.encode(form.getDes3PsnId(), "UTF-8"));
        return null;
      }
      // 未登录继续站外主页查看逻辑,获取人员ID
      form.setIsLogin(false);
      this.getPsnIdToForm();
      // 获取人员基本信息
      form.setNeedStatistics(true);
      form = personManager.getUnifiedPsnInfoByForm(form);
      /**
       * 如果是保留账号,获取保留账号id后,再获取一次个人信息
       */
      if (psnOutsideHomepageService.checkHasMerge(form)) {
        form = personManager.getUnifiedPsnInfoByForm(form);
      }
      builSeoTitle(form);
      // 获取模块配置信息，决定模块是否显示
      personManager.buildPsnInfoConfig(form);
      // 需要个人简介，关键词构建seo标签 （个人简介 1<<12 关键词1<<10）
      buildSeoTagInfo(form, (1 << 12) == (form.getCnfAnyMode() & (1 << 12)),
          (1 << 10) == (form.getCnfAnyMode() & (1 << 10)));
      // 创建或获取人员主页url
    } catch (PsnCnfException e) {
      personalManager.initPsnConfigInfoByTask(form.getPsnId());
      logger.error("个人模块配置为空，进行初始化，psnId = " + form.getPsnId(), e);
    } catch (Exception e) {
      logger.error("进入人员站外主页出错，psnId = " + form.getPsnId(), e);
    }

    return "outsideHomepage";
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

  /**
   * 站外主页工作经历
   * 
   * @return
   */
  @Action("/psnweb/outside/ajaxwork")
  public String psnOutsideWorkHistory() {
    try {
      this.getPsnIdToForm();
      form = workHistoryService.buildPsnWorkHistoryListInfo(form);
    } catch (Exception e) {
      logger.error("站外主页显示工作经历出错, psnId = " + form.getPsnId(), e);
    }
    return "outsideWork";
  }

  /**
   * 站外主页教育经历
   * 
   * @return
   */
  @Action("/psnweb/outside/ajaxedu")
  public String psnOutsideEduHistory() {
    try {
      this.getPsnIdToForm();
      form = educationHistoryService.buildPsnEduHistoryListInfo(form);
    } catch (Exception e) {
      logger.error("站外主页显示教育经历出错, psnId = " + form.getPsnId(), e);
    }
    return "outsideEdu";
  }

  /**
   * 站外主页个人简介
   * 
   * @return
   */
  @Action("/psnweb/outside/ajaxbrief")
  public String psnOutsideBrief() {
    try {
      this.getPsnIdToForm();
      form.setPsnBriefDesc(personManager.getPersonBrief(form.getPsnId()));
    } catch (Exception e) {
      logger.error("站外主页显示个人简介出错, psnId = " + form.getPsnId(), e);
    }
    return "outsideBrief";
  }

  /**
   * 站外主页科技领域
   * 
   * @return
   */
  @Action("/psnweb/outside/ajaxsciencearea")
  public String psnOutsideScienceArea() {
    try {
      this.getPsnIdToForm();
      form = scienceAreaService.findPsnScienceArea(form);
    } catch (Exception e) {
      logger.error("站外主页显示科技领域出错, psnId = " + form.getPsnId(), e);
    }
    return "outsideSA";
  }

  /**
   * 站外主页关键词
   * 
   * @return
   */
  @Action("/psnweb/outside/ajaxkeywords")
  public String psnOutsideKeywords() {
    try {
      this.getPsnIdToForm();
      List<PsnDisciplineKey> keyList = psnDisciplineKeyService.findPsnKeyWords(form.getPsnId());
      form.setKeywords(keyList);
    } catch (Exception e) {
      logger.error("站外主页显示关键词出错, psnId = " + form.getPsnId(), e);
    }
    return "outsideKw";
  }

  /**
   * 站外主页代表性项目
   * 
   * @return
   */
  @Action("/psnweb/outside/ajaxrepresentprj")
  public String psnOutsideRepresentPrj() {
    try {
      this.getPsnIdToForm();
      form = representPrjService.buildPsnRepresentPrjInfo(form);
    } catch (Exception e) {
      logger.error("站外主页显示代表性项目出错, psnId = " + form.getPsnId(), e);
    }
    return "outsideprj";
  }

  /**
   * 显示联系信息
   * 
   * @return
   */
  @Action("/psnweb/outside/ajaxcontactshow")
  public String showPsnContactInfo() {
    try {
      Person psn = personManager.findPersonContactInfo(form.getPsnId());
      if (psn != null) {
        form.setPsnId(form.getPsnId());
        form.setEmail(psn.getEmail());
        form.setTel(psn.getTel());
        buildShowTelEmail();
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
   * 获取人员ID,放到form中
   */
  private void getPsnIdToForm() {
    if (form.getPsnId() == null || form.getPsnId() == 0) {
      if (StringUtils.isNotBlank(form.getDes3PsnId())) {
        form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())));
      }
    }
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

  /**
   * 构建seo标签需要使用的人员简介和关键词信息
   * 
   * @param form
   * @param isbrief 是否构建简介
   * @param iskeywords 是否构建关键词
   */

  private void buildSeoTagInfo(PersonProfileForm form, Boolean isbrief, Boolean iskeywords) {
    Locale locale = LocaleContextHolder.getLocale();
    if (isbrief) {
      form.setPsnBriefDesc(personManager.getPersonBrief(form.getPsnId()));
    }
    if (iskeywords) {
      List<PsnDisciplineKey> keyList = psnDisciplineKeyService.findPsnKeyWords(form.getPsnId());
      StringBuilder sb = new StringBuilder();
      String splitTag = locale.equals(locale.US) ? ", " : "，";
      // 有限温，有限密，层状强关联系统，Dyson-Schwinger方程
      if (keyList != null && keyList.size() > 0) {
        List<String> wordList = new ArrayList<String>();
        for (PsnDisciplineKey psnKeywords : keyList) {
          if (StringUtils.isNotBlank(psnKeywords.getKeyWords())) {
            wordList.add(psnKeywords.getKeyWords());
          }
        }
        if (wordList.size() > 0) {
          form.setPsnKeywordsStr(StringUtils.join(wordList.toArray(), splitTag));
        }

        /*
         * for (PsnDisciplineKey psnKeywords : keyList) { sb.append(splitTag + psnKeywords.getKeyWords()); }
         * form.setPsnKeywordsStr(locale.equals(locale.US) ? (sb.toString()).substring(2) :
         * (sb.toString()).substring(1));
         */
      }

    }

  }

  public String getRealUrlParamet() {
    return realUrlParamet;
  }

  public void setRealUrlParamet(String realUrlParamet) {
    this.realUrlParamet = realUrlParamet;
  }
}
