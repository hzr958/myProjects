package com.smate.web.psn.action.workhis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.psn.PsnSolrInfoModifyService;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.PersonalManager;
import com.smate.web.psn.service.profile.WorkHistoryService;
import com.smate.web.psn.utils.EditValidateUtils;

/**
 * 
 * @author LJ
 *
 *         2017年6月28日
 */
public class APPPersonWorkHistoryAction extends ActionSupport implements ModelDriven<WorkHistory>, Preparable {
  private static final long serialVersionUID = -2759530407039974774L;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private WorkHistory form;
  @Autowired
  private WorkHistoryService workHistoryService;
  @Autowired
  private PsnSolrInfoModifyService psnSolrInfoModifyService;
  @Autowired
  private PersonManager personManager;
  private int total = 0;
  private String startDate;
  private String endDate;
  private String des3PsnId;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态
  @Autowired
  private PersonalManager personalManager;

  /**
   * 新的新增或更新工作经历
   * 
   * @return
   */
  @Action("/app/psnweb/workhistory/ajaxsave")
  public String saveWorkHistory() {
    boolean validate = this.validateSaveData();
    Map<String, String> data = new HashMap<String, String>();
    if (!validate) {
      data.put("result", "error");
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      AppActionUtils.renderAPPReturnJson(data, total, status);
      return null;
    }
    form.setWorkId(this.getWorkId());
    try {
      boolean isOwner = true;
      // 若是编辑工作经历，则要判断是否是本人的工作经历
      if (form.getWorkId() != null) {
        isOwner = workHistoryService.isOwnerOfWorkHistory(form.getPsnId(), form.getWorkId());
      }
      if (isOwner) {
        // 保存工作经历
        // form.setIsPrimary(0L);
        Long isPrimary = workHistoryService.isPrimaryWorkHistory(form.getPsnId(), form.getWorkId());
        form.setIsPrimary(isPrimary);
        workHistoryService.saveWorkHistory(form, false, 7, isPrimary);
        // 刷新人员信息完整度
        personManager.refreshCompleteByPsnId(form.getPsnId());
        // 更新人员solr信息
        personalManager.refreshPsnSolrInfoByTask(form.getPsnId());
        // 更新sie的人员信息
        personalManager.updateSIEPersonInfo(form.getPsnId());
        data.put("result", "success");
        status = IOSHttpStatus.OK;

      } else {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        data.put("result", "isNotOwner");
      }

    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("保存工作经历出错", e);
    }
    form.setErrorMsg("保存工作经历出错");
    AppActionUtils.renderAPPReturnJson(data, total, status);
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
    // 兼容APP参数传递
    try {
      String[] start = startDate.split("/");
      form.setFromYear(Long.valueOf(start[0]));
      form.setFromMonth(Long.valueOf(start[1]));
      String[] end = endDate.split("/");
      form.setToYear(Long.valueOf(end[0]));
      form.setToMonth(Long.valueOf(end[1]));
      Long psnId = form.getPsnId();
      if (psnId != null) {
        form.setPsnId(psnId);
      } else {
        throw new Exception("PSNID为空");
      }
    } catch (Exception e) {
      pass = false;
    }

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

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new WorkHistory();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  @Override
  public WorkHistory getModel() {
    return form;
  }
}
