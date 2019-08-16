package com.smate.sie.web.application.action.validate;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.sie.core.base.utils.model.validate.KpiValidateMainUpload;
import com.smate.sie.web.application.form.validate.KpiValidateForm;
import com.smate.sie.web.application.service.validate.KpiValidateService;

@Results({@Result(name = "maint", location = "/WEB-INF/jsp/validate/kpi_validate_maint.jsp"),
    @Result(name = "maint-list", location = "/WEB-INF/jsp/validate/kpi_validate_list.jsp"),
    @Result(name = "to_add", location = "/WEB-INF/jsp/validate/kpi_validate_add.jsp"),
    @Result(name = "to_pay", location = "/WEB-INF/jsp/validate/kpi_validate_pay.jsp"),
    @Result(name = "view", location = "/WEB-INF/jsp/validate/kpi_validate_view.jsp")})
public class KpiValidateAction extends ActionSupport implements ModelDriven<KpiValidateForm>, Preparable {
  /**
   * 
   */
  private static final long serialVersionUID = 8433742911880591516L;
  private KpiValidateForm form;
  private String sourceFileFileName;
  private Page<KpiValidateMainUpload> page = new Page<KpiValidateMainUpload>();
  @Value("${importKpiValidateFileSaveRoot}")
  private String savePathRoot;
  private File filedata;
  private String filedataFileName;
  @Autowired
  private KpiValidateService kpiValidateService;

  // 初始化左侧
  @Action("/application/validate/maint")
  public String maint() throws SysServiceException {
    Long userId = SecurityUtils.getCurrentUserId();
    String userRealIP = Struts2Utils.getRemoteAddr();
    form.setUserId(userId);
    form.setClientIP(userRealIP);
    Integer isPay = kpiValidateService.isAlreadyPaid(userId, userRealIP);
    form.setIsPay(isPay);
    kpiValidateService.initLeftMenu(form);
    return "maint";
  }

  // 文件列表查询
  @Action("/application/validate/ajaxvalidatelist")
  public String mainList() throws SysServiceException {
    Long userId = SecurityUtils.getCurrentUserId();
    form.setUserId(userId);
    kpiValidateService.queryKpiValidateList(form, page);
    return "maint-list";
  }

  // 统计数
  @Action("/application/validate/ajaxvalidatecount")
  public String ajaxPubCountByPrvo() throws Exception {
    Long userId = SecurityUtils.getCurrentUserId();
    form.setUserId(userId);
    kpiValidateService.getPsnCountByCon(form);
    Struts2Utils.renderJson(form.getResultMap(), "encoding:UTF-8");
    return null;

  }

  // 新增页面
  @Action("/application/validate/toadd")
  public String toAddPage() throws Exception {
    // String isPay = Des3Utils.decodeFromDes3(form.getDes3IsPay());
    Long userId = SecurityUtils.getCurrentUserId();
    Long insId = SecurityUtils.getCurrentInsId();
    String userRealIP = Struts2Utils.getRemoteAddr();
    Integer isPay = kpiValidateService.isAlreadyPaid(userId, userRealIP);
    String zhName = "";
    if (insId != null) { // 若从机构版跳转个人版，且存在insId
      zhName = kpiValidateService.findPsnNameByInsIdAndPsnId(userId, insId);
    } else { // 直接进入个人版，不存在psnId
      zhName = kpiValidateService.findPsnNameByPsnId(userId);
    }
    if (isPay == 0) {
      kpiValidateService.clickAdd(isPay, userId, zhName, userRealIP);
      return "to_pay";
    } else {
      return "to_add";
    }

  }


  // 删除
  @Action("/application/validate/ajaxdelete")
  public String delete() throws Exception {
    String uploadId = Des3Utils.decodeFromDes3(form.getDes3Id());
    if (StringUtils.isNotBlank(uploadId)) {
      kpiValidateService.delValidateMainUpload(Long.valueOf(uploadId));
      String result = "{\"result\":\"success\",\"msg\":\"删除成功\"}";
      Struts2Utils.renderJson(result, "encoding:UTF-8");
    } else {
      String result = "{\"result\":\"error\",\"msg\":\"删除失败\"}";
      Struts2Utils.renderJson(result, "encoding:UTF-8");
    }
    return null;
  }



  // 提交前校验
  @Action("/application/validate/ajaxsubmitbefore")
  public String beforeSubmitFile() throws Exception {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    String userRealIP = Struts2Utils.getRemoteAddr();
    Long userId = SecurityUtils.getCurrentUserId();
    form.setUserId(userId);
    form.setClientIP(userRealIP);
    Integer isPay = kpiValidateService.isAlreadyPaid(userId, userRealIP);
    // 检查是否购买
    if (isPay == 0) {
      returnMap.put("msg", "toPay");
    } else {
      returnMap = kpiValidateService.beforeSubMit(form);
    }
    Struts2Utils.renderJson(returnMap, "encoding:UTF-8");
    return null;
  }

  // 提交
  @Action("/application/validate/ajaxsubmitfile")
  public String submitFileAndExtract() throws Exception {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Long userId = SecurityUtils.getCurrentUserId();
    String userRealIP = Struts2Utils.getRemoteAddr();
    form.setUserId(userId);
    form.setClientIP(userRealIP);
    Integer isPay = kpiValidateService.isAlreadyPaid(userId, userRealIP);
    returnMap = kpiValidateService.beforeSubMit(form);
    if (isPay == 0) {
      returnMap = new HashMap<String, Object>();
      returnMap.put("msg", "toPay");
    } else if ("false".equals(returnMap.get("flag"))) { // 超过最大使用次数
      // Struts2Utils.renderJson(returnMap, "encoding:UTF-8");
      returnMap.put("msg", "false");
    } else {
      returnMap = kpiValidateService.extractPDFFileContent(form, savePathRoot);
    }
    HttpServletResponse response = Struts2Utils.getResponse();
    response.setHeader("Charset", "UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    response.getWriter().print(JacksonUtils.jsonObjectSerializer(returnMap));
    return null;
  }

  // 查看详情
  @Action("/application/validate/viewdetail")
  public String viewDetail() throws Exception {
    String des3UuId = Struts2Utils.getParameter("des3UuId");
    String uuId = Des3Utils.decodeFromDes3(des3UuId);
    kpiValidateService.viewValidateDetail(uuId, form);
    form.setIfOutside(0);
    return "view";
  }

  // 站外详情
  @Action("/application/outside/validate/viewdetail")
  public String viewOutsideDetail() throws Exception {
    String des3UuId = Struts2Utils.getParameter("des3UuId");
    String uuId = Des3Utils.decodeFromDes3(des3UuId);
    kpiValidateService.viewValidateDetail(uuId, form);
    form.setIfOutside(1);
    String str = Struts2Utils.getHttpReferer();
    if (StringUtils.isNotBlank(str)) {
      URL url = new URL(str);
      String domain = url.getProtocol() + "://" + url.getHost();
      Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", domain);
      Struts2Utils.getResponse().addHeader("Access-Control-Allow-Credentials", "true");
    }
    return "view";
  }



  // 判断是否超时
  @Action("/application/validate/ajaxcheckupload")
  public String checkPub() throws Exception {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    resultMap.put("result", SUCCESS);
    Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    return null;
  }

  // 刷新当前数据
  @Action("/application/validate/ajaxrefreshstatus")
  public String refreshStatus() throws Exception {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    kpiValidateService.refreshStatusCurrent(form);
    resultMap.put("result", SUCCESS);
    resultMap.put("status", form.getStatus());
    Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new KpiValidateForm();
    }
  }

  @Override
  public KpiValidateForm getModel() {
    return form;
  }

  public Page<KpiValidateMainUpload> getPage() {
    return page;
  }

  public void setPage(Page<KpiValidateMainUpload> page) {
    this.page = page;
  }

  public String getSourceFileFileName() {
    return sourceFileFileName;
  }

  public void setSourceFileFileName(String sourceFileFileName) {
    this.sourceFileFileName = sourceFileFileName;
  }

  public File getFiledata() {
    return filedata;
  }

  public void setFiledata(File filedata) {
    this.filedata = filedata;
  }

  public String getFiledataFileName() {
    return filedataFileName;
  }

  public void setFiledataFileName(String filedataFileName) {
    this.filedataFileName = filedataFileName;
  }
}
