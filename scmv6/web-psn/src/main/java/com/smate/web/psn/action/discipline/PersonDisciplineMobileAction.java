package com.smate.web.psn.action.discipline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.form.mobile.PsnHomepageMobileForm;
import com.smate.web.psn.model.keyword.CategoryMapBaseInfo;
import com.smate.web.psn.model.keyword.PsnScienceAreaInfo;
import com.smate.web.psn.service.keyword.CategoryMapBaseService;
import com.smate.web.psn.service.keyword.PsnKeywordService;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.PsnDisciplineKeyService;
import com.smate.web.psn.service.sciencearea.ScienceAreaService;

@Results({
    @Result(name = "morePersonKeywords", location = "/WEB-INF/jsp/mobile/homepage/mobilePsnHomepageKeywordMore.jsp"),
    @Result(name = "morePersonScienceAreas",
        location = "/WEB-INF/jsp/mobile/homepage/mobilePsnHomepageScienceAreaMore.jsp"),
    @Result(name = "mobile_area", location = "/WEB-INF/jsp/discipline/mobile_area_info.jsp"),
    @Result(name = "improve_area", location = "/WEB-INF/jsp/discipline/mobile_improve_area_main.jsp"),
    @Result(name = "mobile_improve_keywords", location = "/WEB-INF/jsp/discipline/mobile_improve_keywords.jsp")})
public class PersonDisciplineMobileAction extends ActionSupport
    implements ModelDriven<PsnHomepageMobileForm>, Preparable {

  private static final long serialVersionUID = -2700156214728128232L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private PsnHomepageMobileForm form;

  @Autowired
  private PsnKeywordService psnKeywordService;
  @Autowired
  private ScienceAreaService scienceAreaService;
  @Autowired
  private CategoryMapBaseService categoryMapBaseService;
  @Value("${domainoauth}")
  private String domainoauth;
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private PsnDisciplineKeyService psnDisciplineKeyService;
  @Autowired
  private PersonManager personManager;

  /**
   * 获取更多的个人关键词信息
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/mobile/morePersonKeywords")
  public String AjaxMorePersonKeywords() throws Exception {
    checkPsnId(form);
    if (form.getPsnId() == null) {
      try {
        // 不正确 重向到登录页面
        Struts2Utils.getResponse().sendRedirect(domainoauth + "/oauth/mobile/index");
      } catch (Exception e) {
        logger.error("获取人员失败，并且重定向登录页面异常！", e);
      }

      return null;
    }
    form.setKeywordIdentificationForm(psnKeywordService.getPsnKeyWord(form.getPsnId(), 3));
    return "morePersonKeywords";
  }

  /**
   * 获取更多的个人科研领域信息
   * 
   * @return
   * @throws Exception
   */
  @Actions({@Action("/psnweb/mobile/morePersonScienAreas"), @Action("/psnweb/outside/ajaxmoremobilescienareas")})
  public String AjaxMorePersonScienAreas() throws Exception {
    checkPsnId(form);
    if (form.getPsnId() == null) {
      try {
        // 不正确 重向到登录页面
        Struts2Utils.getResponse().sendRedirect(domainoauth + "/oauth/mobile/index");
      } catch (Exception e) {
        logger.error("获取人员失败，并且重定向登录页面异常！", e);
      }

      return null;
    }
    form.setPsnScienceAreaFormList(scienceAreaService.getPsnScienceAreaFormList(form.getPsnId()));
    return "morePersonScienceAreas";
  }

  /**
   * 移动端人员完善科技领域
   * 
   * @Description: /psn/mobile/improvearea
   * @return
   */
  @Deprecated
  @Action("/psnweb/mobile/improvearea")
  public String psnImproveAreaInfo() {
    return "improve_area";
  }

  /**
   * 查找科技领域信息
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @Action("/psnweb/mobile/ajaxareas")
  public String ajaxFindAreas() {
    // TODO 若是编辑科技领域，需要查询已选的科技领域
    form.setDomain(domainMobile);
    try {
      // 获取科技领域构建成的Map
      Map<String, Object> areaMap = categoryMapBaseService.buildAllScienceAreaInfo();
      if (areaMap != null) {
        List<CategoryMapBaseInfo> firstLevel = (List<CategoryMapBaseInfo>) areaMap.get("area_first");
        List<CategoryMapBaseInfo> secondLevel = (List<CategoryMapBaseInfo>) areaMap.get("area_second");
        form.setFirstLevel(firstLevel);
        form.setSecondLevel(secondLevel);
        // 找到已选中的科技领域
        List<PsnScienceAreaInfo> areaInfo =
            scienceAreaService.findPsnScienceAreaInfo(SecurityUtils.getCurrentUserId(), 1);
        if (CollectionUtils.isNotEmpty(areaInfo)) {
          for (PsnScienceAreaInfo area : areaInfo) {
            for (CategoryMapBaseInfo info : secondLevel) {
              // 不要直接用==,会有问题
              if (area.getScienceAreaId().compareTo(info.getCategoryId()) == 0) {
                info.setAdded(true);
                break;
              }
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("移动端获取科技领域信息出错，psnId=" + form.getPsnId(), e);
    }
    return "mobile_area";
  }

  /**
   * 完善关键词主页面
   * 
   * @return
   */
  @Action("/psnweb/mobile/improvekeywords")
  public String improvePsnKeywords() {
    // 若是编辑关键词，需查询已选关键词
    try {
      form.setKeywords(psnDisciplineKeyService.findPsnEffectiveKeywods(SecurityUtils.getCurrentUserId()));
      form.setDes3PsnId(Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
    } catch (Exception e) {
      logger.error("进入移动端关键词完善页面出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return "mobile_improve_keywords";
  }

  /**
   * 移动端保存人员关键词
   * 
   * @return
   */
  @Action("/psnweb/mobile/keywords/ajaxsave")
  public String ajaxSavePsnKeywords() {
    Map<String, Object> data = new HashMap<String, Object>();
    try {
      // 校验是否超过10个关键词
      List<String> keyList = new ArrayList<String>();
      if (StringUtils.isNotBlank(form.getPsnKeyStr())) {
        keyList = Arrays.asList(form.getPsnKeyStr().split("@@"));
      }
      if (CollectionUtils.isNotEmpty(keyList) && keyList.size() > 10) {
        data.put("result", "sumLimit");
      } else {
        psnDisciplineKeyService.mobileSavePsnKeywords(SecurityUtils.getCurrentUserId(), keyList, form.getAnyUser());
        personManager.refreshComplete(SecurityUtils.getCurrentUserId());
        data.put("result", "success");
      }
    } catch (Exception e) {
      logger.error("移动端保存人员关键词信息出错, psnId = " + SecurityUtils.getCurrentUserId() + ", keywords=" + form.getPsnKeyStr(),
          e);
      data.put("result", "error");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  @Action("/psnweb/mobile/registersuccess")
  public String registerSuccess() {
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("registerResult", "success");
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  public void checkPsnId(PsnHomepageMobileForm form) throws PsnException {

    if (form.getPsnId() != null) {
      return;
    } else if (form.getDes3PsnId() != null) {// 用传进来的des3psnId进行研究领域信息的查询
      String psnId = ServiceUtil.decodeFromDes3(form.getDes3PsnId().toString());
      form.setPsnId(NumberUtils.toLong(psnId));
    }
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnHomepageMobileForm();
    }
  }

  @Override
  public PsnHomepageMobileForm getModel() {
    return form;
  }

}
