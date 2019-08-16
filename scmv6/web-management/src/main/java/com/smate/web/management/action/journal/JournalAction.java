package com.smate.web.management.action.journal;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.model.journal.JournalForm;
import com.smate.web.management.service.journal.JournalManageService;

/**
 * 基础期刊Action.
 * 
 * @author lichangwen
 * 
 */
@Results({@Result(name = "audit", location = "/WEB-INF/journal/audit.jsp"),
    @Result(name = "importBatch", location = "/WEB-INF/journal/journal-import.jsp"),
    @Result(name = "handBatch", location = "/WEB-INF/journal/handBatch.jsp"),
    @Result(name = "batchImportData", location = "/WEB-INF/journal/list-data.jsp"),
    @Result(name = "journalProccess", location = "/WEB-INF/journal/journal-proccess.jsp"),
    @Result(name = "manager", location = "/WEB-INF/journal/manager.jsp"),
    @Result(name = "isiBatchImportData", location = "/WEB-INF/journal/isiif-list.jsp"),
    @Result(name = "auisiif", location = "/WEB-INF/journal/isiif-audit.jsp"),
    @Result(name = "IsIProcessing", location = "/WEB-INF/journal/isiif-update.jsp"),
    @Result(name = "ajaxMatchBjnl", location = "/WEB-INF/journal/ajax_match_bjnl.jsp"),
    @Result(name = "iframeJnl", location = "/WEB-INF/journal/iframe_jnl.jsp"),
    @Result(name = "manfind", location = "/WEB-INF/journal/base-journal.jsp"),
    @Result(name = "forward", location = "${forwardUrl}", type = "redirect")})
public class JournalAction extends ActionSupport implements ModelDriven<JournalForm>, Preparable {

  private static final long serialVersionUID = 1L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private JournalForm journalForm;
  @Autowired
  private JournalManageService journalManageService;

  /**
   * 期刊导入页面
   * 
   * @return
   */
  @Action("/scmmanagement/journal/importBatch")
  public String importJournal() {
    return "importBatch";
  }

  /**
   * 导入excel
   * 
   * @return
   */
  @Action("/scmmanagement/journal/importExcel")
  public String importExcel() {
    journalManageService.importExcel(journalForm);
    return "importBatch";
  }

  /**
   * 基础影响因子导入excel
   * 
   * @return
   */
  @Action("/scmmanagement/journal/importIsIExcel")
  public String importIsIExcel() {
    journalManageService.importIsIExcel(journalForm);
    return "importBatch";
  }

  /**
   * 获取批量导入之后转手工处理的数据
   * 
   * @return
   */
  @Action("/scmmanagement/journal/getBatchImportCheckData")
  public String batchImportCheckData() {
    try {
      journalManageService.getBatchImportCheckData(journalForm);
    } catch (Exception e) {
      logger.error("获取批量导入审核数据出错", e);
    }
    return "batchImportData";
  }

  /**
   * 获取isi影响因子导入之后转手工处理的数据
   * 
   * @return
   */
  @Action("/scmmanagement/journal/getIsIBatchImportCheckData")
  public String getIsIBatchImportCheckData() throws Exception {
    journalManageService.getIsIBatchImportCheckData(journalForm);
    return "isiBatchImportData";
  }

  /**
   * 跳转到期刊手工待处理页面
   * 
   * @return
   */
  @Action("/scmmanagement/journal/toJournalProcessing")
  public String toProcessing() {
    journalManageService.toProccess(journalForm);
    return "journalProccess";
  }

  /**
   * 跳转到isi待处理页面
   * 
   * @return
   * @throws Exception
   */
  @Action("/journal/toIsIProcessing")
  public String toIsIProcessing() throws Exception {
    journalManageService.toProccess(journalForm);
    return "IsIProcessing";
  }

  /**
   * 
   * 审核-保留原样
   */
  @Action("/scmmanagement/journal/toCheck")
  public void toCheck() {
    String result = journalManageService.check(journalForm);
    Struts2Utils.renderText(result);
  }

  /**
   * 审核-新增期刊
   */
  @Action("/scmmanagement/journal/toCheckAddJournal")
  public void toCheckAddJournal() {
    String result = journalManageService.checkAddJournal(journalForm);
    Struts2Utils.renderText(result);
  }

  /**
   * 审核-更新至选中期刊
   */
  @Action("/scmmanagement/journal/toCheckUpdatejournal")
  public void toCheckUpdatejournal() {
    String result = journalManageService.toCheckUpdatejournal(journalForm);
    Struts2Utils.renderJson(result);
  }

  /**
   * 修改合并页面
   * 
   * @return
   */
  @Action("/scmmanagement/journal/manager")
  public String manager() {
    try {
      journalManageService.getBaseJournal(journalForm);
    } catch (Exception e) {
      logger.error("获取基础期刊所有数据出错", e);
    }
    return "manager";
  }


  @Action("/scmmanagement/journal/manDel")
  public String manDel() throws Exception {
    try {
      journalManageService.deletBaseJournal(journalForm);
    } catch (Exception e) {
      logger.error("删除基础期刊数据出错", e);
    }
    return manager();
  }

  /**
   * 合并期刊
   * 
   * @return
   */
  @Action("/scmmanagement/journal/mergeJournal")
  public void mergeJournal() {
    int result = journalManageService.mergeJournal(journalForm);
    Struts2Utils.renderJson("{\"result\":" + result + "}", "encoding:UTF-8");
  }

  /**
   * 修改合并-查看更新页面
   * 
   * @return
   */
  @Action("/scmmanagement/journal/findOrUpdate")
  public String findOrUpdate() {
    journalManageService.getBaseJournalByTitle(journalForm, null);
    return "manfind";
  }

  /**
   * 修改合并-更新
   * 
   * @return
   */
  @Action("/scmmanagement/journal/mergeUpdate")
  public void mergeUpdate() {
    journalManageService.mergeUpdate(journalForm);
    Struts2Utils.renderJson("{\"result\":\"success\"}", "encoding:UTF-8");
  }

  /**
   * 修改合并-删除
   * 
   * @return
   */
  @Action("/scmmanagement/journal/mergeDelJournalTitle")
  public String mergeToDelJournalTitle() {
    journalManageService.delJournalTitle(journalForm);
    return "manfind";
  }

  /**
   * 修改合并-确认删除
   * 
   * @return
   */
  @Action("/scmmanagement/journal/confirmDelJournalTitle")
  public void mergeConfirmDelJournalTitle() {
    journalManageService.confirmDelJournalTitle(journalForm);
    Struts2Utils.renderJson("{\"result\":\"success\"}", "encoding:UTF-8");
  }

  /**
   * 
   * 获取批量导入审核数据
   * 
   * @return
   */
  @Deprecated
  @Action("/scmmanagement/journal/getAuditData")
  public String getAuditData() {
    journalManageService.getAuditData(journalForm);
    return "audit";
  }

  /**
   * 期刊审核通过
   * 
   * @return
   * @throws Exception
   */
  @Action("/scmmanagement/journal/auditPassed")
  public void auditPassed() throws Exception {
    try {
      String result = journalManageService.auditPassed(journalForm);
      Struts2Utils.renderText(result);
    } catch (Exception e) {
      logger.error("管理员审核期刊数据之通过操作出错", e);
    }
  }

  /**
   * 期刊审核拒绝
   * 
   * @throws Exception
   */
  @Action("/scmmanagement/journal/auditRefuse")
  public void auditRefuse() throws Exception {
    try {
      String result = journalManageService.auditRefuse(journalForm);
      Struts2Utils.renderText(result);
    } catch (Exception e) {
      logger.error("管理员审核期刊数据之拒绝操作出错", e);
    }
  }

  @Override
  public void prepare() throws Exception {
    journalForm = new JournalForm();
  }

  @Override
  public JournalForm getModel() {
    return journalForm;
  }



}
