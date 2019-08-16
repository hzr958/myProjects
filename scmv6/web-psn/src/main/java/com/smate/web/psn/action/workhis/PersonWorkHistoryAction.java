package com.smate.web.psn.action.workhis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.service.psn.PsnSolrInfoModifyService;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.PersonalManager;
import com.smate.web.psn.service.profile.WorkHistoryService;
import com.smate.web.psn.service.psnwork.PsnWorkHistoryInsInfoService;
import com.smate.web.psn.utils.EditValidateUtils;

/**
 * 人员工作经历Action
 * 
 * @author Administrator
 *
 */
@Results({@Result(name = "save_success", location = "${forwardUrl}", type = "redirect"),
    @Result(name = "save_error", location = "/WEB-INF/jsp/mobile/ImproveWorkOrEducationHistory.jsp"),})
public class PersonWorkHistoryAction extends ActionSupport implements ModelDriven<WorkHistory>, Preparable {

  private static final long serialVersionUID = 7984543221587825404L;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private WorkHistory form;
  @Autowired
  private WorkHistoryService workHistoryService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private SysDomainConst sysDomainConst;
  @Autowired
  private PsnSolrInfoModifyService psnSolrInfoModifyService;
  @Value("${domainMobile}")
  private String domainMobile;
  // 微信端跳转过来后需要的openId和URL，URL为绑定后跳转回微信端的地址
  private String wxOpenId;
  private String wxUnionId;
  private String wxUrl;
  @Value("${domainInnocity}")
  private String domainInnocity; // 创新城域名
  // 第三方判断(CXC_LOGIN:创新城)
  private String sysType;
  private String OPERATE_TYPE_ADD = "addPrimary";

  private String reqUrl;
  @Autowired
  private PersonalManager personalManager;
  @Autowired
  private PsnWorkHistoryInsInfoService psnWorkHistoryInsInfoService;
  @Autowired
  private RestTemplate restTemplate;


  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new WorkHistory();
    }
  }

  @Override
  public WorkHistory getModel() {
    return form;
  }

  @Action("/psnweb/mobile/saveworkhis")
  public String savePsnWorkHistory() {
    // 数据校验
    boolean validate = this.validateSaveData();
    if (!validate) {
      return "save_error";
    }
    form.setWorkId(this.getWorkId());
    form.setPsnId(SecurityUtils.getCurrentUserId());
    try {
      // 保存工作经历
      workHistoryService.saveWorkHistory(form, false, form.getAnyUser());
      // 刷新人员信息完整度
      personManager.refreshCompleteByPsnId(form.getPsnId());
      // 更新人员solr索引信息
      // psnSolrInfoModifyService.updateSolrPsnInfo(form.getPsnId());
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
      logger.info("保存工作经历后跳转到:" + form.getForwardUrl());
      return "save_success";
    } catch (Exception e) {
      logger.error("保存工作经历出错", e);
    }
    form.setErrorMsg("保存工作经历出错");
    return "save_error";
  }

  /**
   * 新的新增或更新工作经历
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @Actions({@Action("/psnweb/workhistory/ajaxsave"), @Action("/psndata/mobile/workhistory/ajaxsave")})
  public String saveWorkHistory() {
    boolean validate = this.validateSaveData();
    Map data = new HashMap();
    if (!validate) {
      data.put("result", "error");
      Struts2Utils.renderJson(data, "encoding:UTF-8");
      return null;
    }
    form.setWorkId(this.getWorkId());
    form.setPsnId(NumberUtils.isNotNullOrZero(SecurityUtils.getCurrentUserId()) ? SecurityUtils.getCurrentUserId()
        : form.getPsnId());
    try {
      boolean isOwner = true;
      // 若是编辑工作经历，则要判断是否是本人的工作经历
      if (form.getWorkId() != null) {
        isOwner = workHistoryService.isOwnerOfWorkHistory(form.getPsnId(), form.getWorkId());
      }
      if (isOwner) {
        // 保存工作经历
        Long isPrimary = 0L;
        if (OPERATE_TYPE_ADD.equals(form.getOperateType())) {
          isPrimary = 1L;
        } else {
          isPrimary = workHistoryService.isPrimaryWorkHistory(form.getPsnId(), form.getWorkId());
        }
        form.setIsPrimary(isPrimary);
        Long workId = workHistoryService.saveWorkHistory(form, false, 7, isPrimary);
        // 刷新人员信息完整度
        personManager.refreshCompleteByPsnId(form.getPsnId());
        initPsnFundRecommend(form.getPsnId());// 初始化基金推荐条件
        // 更新solr信息
        // psnSolrInfoModifyService.updateSolrPsnInfo(form.getPsnId());
        personalManager.refreshPsnSolrInfoByTask(form.getPsnId());
        // 更新sie的人员信息
        personalManager.updateSIEPersonInfo(form.getPsnId());
        data.put("result", "success");
        data.put("workId", workId);
      } else {
        data.put("result", "isNotOwner");
      }
      Struts2Utils.renderJson(data, "encoding:UTF-8");
      return null;
    } catch (Exception e) {
      logger.error("保存工作经历出错", e);
    }
    form.setErrorMsg("保存工作经历出错");

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

  // 获取个人工作经历详情
  @Action("/psndata/mobile/workhistory/get")
  public String getWorkHistoryByWorkId() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      WorkHistory workHistory = psnWorkHistoryInsInfoService.getWorkHistoryByWorkId(form.getPsnId(), form.getWorkId());
      map.put("status", "success");
      map.put("result", workHistory);
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("获取个人经历失败， psnId = " + form.getPsnId() + ", workId = " + form.getWorkId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 首页-完善工作经历
   * 
   * @return
   */
  @Action("/psnweb/workhistory/ajaximprovesave")
  public String improveWork() {
    Map<String, Object> data = new HashMap<String, Object>();
    try {
      boolean validate = this.validateSaveData();
      if (!validate) {
        data.put("result", "error");
        Struts2Utils.renderJson(data, "encoding:UTF-8");
        return null;
      }
      form.setPsnId(SecurityUtils.getCurrentUserId());
      Long workId = workHistoryService.saveWorkHistory(form, false, 7, 1l);
      // 刷新人员信息完整度
      personManager.refreshCompleteByPsnId(form.getPsnId());
      // 更新人员solr信息
      // psnSolrInfoModifyService.updateSolrPsnInfo(form.getPsnId());
      personalManager.refreshPsnSolrInfoByTask(form.getPsnId());
      // 更新sie的人员信息
      personalManager.updateSIEPersonInfo(form.getPsnId());
      if (workId != null && workId > 0l) {
        data.put("result", "success");
      } else {
        data.put("result", "error");
      }
      Struts2Utils.renderJson(data, "encoding:UTF-8");
    } catch (Exception e) {
      data.put("result", "error");
      Struts2Utils.renderJson(data, "encoding:UTF-8");
      logger.error("首页-完善工作经历出错， psnId = " + form.getPsnId(), e);
    }
    return null;
  }

  /**
   * 获取人员的工作经历情况，第一次登录可能工作经历已经存在，可以是open接口产生的人员信息，这个人员信息带有工作经历
   * 
   * @return
   */
  @Action("/psnweb/workhistory/ajaximproveget")
  public String getWorkHistory() {
    Map<String, Object> data = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (NumberUtils.isNullOrZero(psnId)) {
        data.put("result", "noPerson");
        Struts2Utils.renderJson(data, "encoding:UTF-8");
        return null;
      }
      WorkHistory workHistory = workHistoryService.getWorkHistoryByPsnId(psnId);
      if (workHistory == null) {
        data.put("result", "noWork");
        Struts2Utils.renderJson(data, "encoding:UTF-8");
        return null;
      }
      data.put("insName", workHistory.getInsName());
      data.put("code", workHistory.getInsId());
      data.put("fromYear", workHistory.getFromYear());
      data.put("fromMonth", workHistory.getFromMonth());
      data.put("toYear", workHistory.getToYear());
      data.put("toMonth", workHistory.getToMonth());
      data.put("department", workHistory.getDepartment());
      data.put("position", workHistory.getPosition());
      Struts2Utils.renderJson(data, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("获取用户工作经历失败， psnId=" + psnId, e);
      data.put("result", "error");
      Struts2Utils.renderJson(data, "encoding:UTF-8");
    }
    return null;
  }

  /**
   * 删除工作经历
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @Actions({@Action("/psnweb/workhistory/ajaxdel"), @Action("/psndata/workhistory/ajaxdel")})
  public String delWorkHistory() {
    Map data = new HashMap();
    try {
      form.setWorkId(this.getWorkId());
      if (NumberUtils.isNullOrZero(form.getPsnId())) {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      // 本人才能删除自己的工作经历
      boolean isOwner = workHistoryService.isOwnerOfWorkHistory(form.getPsnId(), form.getWorkId());
      if (isOwner) {
        String result = workHistoryService.delWorkHistory(form.getWorkId(), form.getPsnId());
        // 同时更新人员solr信息
        // psnSolrInfoModifyService.updateSolrPsnInfo(form.getPsnId());
        personalManager.refreshPsnSolrInfoByTask(form.getPsnId());
        // 更新sie的人员信息
        personalManager.updateSIEPersonInfo(form.getPsnId());
        data.put("result", result);
      } else {
        data.put("result", "isNotOwner");
      }
    } catch (Exception e) {
      logger.error("删除人员工作经历出错， psnId = " + form.getPsnId() + ", workId = " + form.getWorkId(), e);
      data.put("result", "error");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  public Long getWorkId() {

    if (form.getWorkId() == null && StringUtils.isNotBlank(form.getDes3Id())) {
      String tp = ServiceUtil.decodeFromDes3(form.getDes3Id());
      if (tp != null) {
        form.setWorkId(Long.valueOf(tp));
      }
    }
    return form.getWorkId();
  }

  /**
   * 数据校验.
   * 
   * @return
   */
  private boolean validateSaveData() {

    boolean pass = true;
    if (EditValidateUtils.hasParam(form.getInsName(), 200, null)) {
      form.setWorkInsNameError("单位名称有误");
      pass = false;
    }
    // 将200改为601,为兼容从rol同步过来的单位部门长度
    if (form.getDepartment() != null && form.getDepartment().length() > 601) {
      form.setDepartmentError("部门名称有误");
      pass = false;
    }
    if (form.getPosition() != null && form.getPosition().length() > 100) {
      form.setPositionError("职称有误");
      pass = false;
    }
    if (form.getFromYear() == null) {
      pass = false;
    }
    return pass;
  }

  public WorkHistory getForm() {
    return form;
  }

  public void setForm(WorkHistory form) {
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
