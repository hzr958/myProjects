package com.smate.web.psn.action.eduhis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.service.psn.PsnSolrInfoModifyService;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.service.profile.EducationHistoryService;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.PersonalManager;
import com.smate.web.psn.utils.EditValidateUtils;

/**
 * 人员教育经历Action
 * 
 * @author wsn
 *
 */
@Results({@Result(name = "save_success", location = "${forwardUrl}", type = "redirect"),
    @Result(name = "save_error", location = "/WEB-INF/jsp/mobile/ImproveWorkOrEducationHistory.jsp")})
public class PersonEducationHistoryAction extends ActionSupport implements ModelDriven<EducationHistory>, Preparable {

  private static final long serialVersionUID = 3971243807719947950L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private EducationHistory form;
  @Autowired
  private EducationHistoryService educationHistoryService;
  @Autowired
  private PsnSolrInfoModifyService psnSolrInfoModifyService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private SysDomainConst sysDomainConst;
  // 微信端跳转过来后需要的openId和URL，URL为绑定后跳转回微信端的地址
  private String wxOpenId;
  private String wxUnionId;
  private String wxUrl;
  @Value("${domainInnocity}")
  private String domainInnocity; // 创新城域名
  @Value("${domainMobile}")
  private String domainMobile; // 创新城域名
  // 第三方判断(CXC_LOGIN:创新城)
  private String sysType;

  private String reqUrl;
  @Autowired
  private PersonalManager personalManager;

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new EducationHistory();
    }

  }

  @Override
  public EducationHistory getModel() {
    return form;
  }

  /**
   * 保存教育经历--注册用
   * 
   * @return
   */
  @Action("/psnweb/mobile/saveeduhis")
  public String savePsnEduHistory() {
    // 校验数据
    boolean validate = this.validateSaveData();
    if (!validate) {
      return "save_error";
    }
    form.setEduId(this.getEduId());
    form.setPsnId(SecurityUtils.getCurrentUserId());
    try {
      // 保存教育经历
      form.setIsPrimary(0L);
      educationHistoryService.saveEducationHistory(form, false, form.getAnyUser());
      personManager.refreshCompleteByPsnId(form.getPsnId());
      // 更新人员solr信息
      personalManager.refreshPsnSolrInfoByTask(form.getPsnId());
      // 更新sie的人员信息
      personalManager.updateSIEPersonInfo(form.getPsnId());
      if ("CXC_LOGIN".equals(this.getSysType())) {
        form.setForwardUrl(domainMobile + "/psnweb/login/tocxc?need_login=1");
      } else {
        if (reqUrl != null) {
          form.setForwardUrl(sysDomainConst.getDomainMobile() + "/psn/mobile/improvearea?reqUrl=" + reqUrl);
        } else {
          form.setForwardUrl(sysDomainConst.getDomainMobile() + "/psn/mobile/improvearea");
        }
      }
      logger.info("保存教育经历后跳转到:" + form.getForwardUrl());
      return "save_success";
    } catch (Exception e) {
      logger.error("保存教育经历出错！", e);
    }
    form.setErrorMsg("保存教育经历出错");
    return "save_error";
  }

  @SuppressWarnings("unchecked")
  @Actions({@Action("/psnweb/eduhistory/ajaxsave"), @Action("/psndata/mobile/educatehistory/ajaxsave")})
  public String savePsnEducationHistory() {
    // 校验数据
    boolean validate = this.validateSaveData();
    Map data = new HashMap();
    if (!validate) {
      data.put("result", "error");
      Struts2Utils.renderJson(data, "encoding:UTF-8");
      return null;
    }
    form.setEduId(this.getEduId());
    form.setPsnId(com.smate.core.base.utils.number.NumberUtils.isNotNullOrZero(SecurityUtils.getCurrentUserId())
        ? SecurityUtils.getCurrentUserId()
        : form.getPsnId());
    try {
      boolean isOwner = true;
      // 如果是编辑教育经历，需要判断是否是本人
      if (form.getEduId() != null) {
        isOwner = educationHistoryService.isOwnerOfEduHistory(form.getPsnId(), form.getEduId());
      }
      if (isOwner) {
        // 保存教育经历
        form.setIsPrimary(0L);
        educationHistoryService.saveEducationHistory(form, false, form.getAnyUser(), false);
        personManager.refreshCompleteByPsnId(form.getPsnId());
        // 更新人员solr信息
        // psnSolrInfoModifyService.updateSolrPsnInfo(form.getPsnId());
        personalManager.refreshPsnSolrInfoByTask(form.getPsnId());
        // 更新sie的人员信息
        personalManager.updateSIEPersonInfo(form.getPsnId());
        data.put("result", "success");
        data.put("eduId", form.getEduId());
      } else {
        data.put("result", "isNotOwner");
      }
    } catch (Exception e) {
      logger.error("保存教育经历出错！", e);
      data.put("result", "error");
      form.setErrorMsg("保存教育经历出错");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 首页-完善教育经历
   * 
   * @return
   */
  @Action("/psnweb/eduhistory/ajaximprovesave")
  public String improveEdu() {
    try {
      Map<String, Object> map = new HashMap<String, Object>();
      // 校验数据
      boolean validate = this.validateSaveData();
      if (!validate) {
        map.put("result", "error");
        Struts2Utils.renderJson(map, "encoding:UTF-8");
        return null;
      }
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setIsPrimary(0L);
      Long eduId = educationHistoryService.saveEducationHistory(form, false, form.getAnyUser(), false);
      personManager.refreshCompleteByPsnId(form.getPsnId());
      // 更新人员solr信息
      // psnSolrInfoModifyService.updateSolrPsnInfo(form.getPsnId());
      personalManager.refreshPsnSolrInfoByTask(form.getPsnId());
      // 更新sie的人员信息
      personalManager.updateSIEPersonInfo(form.getPsnId());
      if (eduId != null && eduId > 0l) {
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
      Struts2Utils.renderJson(map, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("首页-完善教育经历出错， psnId = " + form.getPsnId(), e);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Actions({@Action("/psnweb/eduhistory/ajaxdel"), @Action("/psndata/eduhistory/ajaxdel")})
  public String delPsnEduHistory() {
    Map data = new HashMap();
    this.getEduId();
    if (com.smate.core.base.utils.number.NumberUtils.isNullOrZero(form.getPsnId())) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
    try {
      boolean isOwner = educationHistoryService.isOwnerOfEduHistory(form.getPsnId(), form.getEduId());
      if (isOwner) {
        String result = educationHistoryService.delEducationHistory(form.getPsnId(), form.getEduId());
        data.put("result", result);
      } else {
        data.put("result", "isNotOwner");
      }
    } catch (Exception e) {
      logger.error("删除教育经历出错， psnId = " + form.getPsnId() + ", eduId = " + form.getEduId(), e);
      data.put("result", "error");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  public Long getEduId() {
    if (form.getEduId() == null && StringUtils.isNotBlank(form.getDes3Id())) {
      String tp = ServiceUtil.decodeFromDes3(form.getDes3Id());
      if (tp != null) {
        form.setEduId(Long.valueOf(tp));
      }
    }
    return form.getEduId();
  }

  /**
   * 数据校验.
   * 
   * @return
   */
  private boolean validateSaveData() {

    boolean pass = true;
    if (EditValidateUtils.hasParam(form.getInsName(), 200, null)) {
      form.setInsNameError("学校名称有误");
      pass = false;
    }
    if (form.getStudy() != null && form.getStudy().length() > 200) {
      form.setStudyError("学院名称有误");
      pass = false;
    }
    if (form.getFromYear() == null) {
      form.setErrorMsg("起始年份有误");
      pass = false;
    }
    return pass;
  }

  public EducationHistory getForm() {
    return form;
  }

  public void setForm(EducationHistory form) {
    this.form = form;
  }

  public String getWxOpenId() {
    return wxOpenId;
  }

  public void setWxOpenId(String wxOpenId) {
    this.wxOpenId = wxOpenId;
  }

  public String getWxUnionId() {
    return wxUnionId;
  }

  public void setWxUnionId(String wxUnionId) {
    this.wxUnionId = wxUnionId;
  }

  public String getWxUrl() {
    return wxUrl;
  }

  public void setWxUrl(String wxUrl) {
    this.wxUrl = wxUrl;
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
    return isSelf;
  }

  public String getSysType() {
    return sysType;
  }

  public void setSysType(String sysType) {
    this.sysType = sysType;
  }

  public String getReqUrl() {
    return reqUrl;
  }

  public void setReqUrl(String reqUrl) {
    this.reqUrl = reqUrl;
  }

}
