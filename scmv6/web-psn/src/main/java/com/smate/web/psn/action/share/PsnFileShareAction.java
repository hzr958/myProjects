package com.smate.web.psn.action.share;

import java.io.Serializable;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.model.share.FileShareForm;
import com.smate.web.psn.service.share.GrpFileShareService;
import com.smate.web.psn.service.share.PsnFileShareService;

/**
 * 个人文件分享
 * 
 * @author aijiangbin
 *
 */
@Results({@Result(name = "emailviewfiles", location = "/WEB-INF/jsp/file/emailviewfiles.jsp"),})
public class PsnFileShareAction extends ActionSupport implements ModelDriven<FileShareForm>, Preparable, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6971233771456286764L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private FileShareForm form;
  /**
   * 每页显示10条记录.
   */
  private int defaultPageSize = 10;
  private Page<FileShareForm> page = new Page<FileShareForm>(defaultPageSize);

  @Autowired
  private PsnFileShareService psnFileShareService;
  @Autowired
  private GrpFileShareService grpFileShareService;


  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FileShareForm();
    }

  }

  @Override
  public FileShareForm getModel() {
    return form;
  }


  public Page<FileShareForm> getPage() {
    return page;
  }

  public void setPage(Page<FileShareForm> page) {
    this.page = page;
  }

  /**
   * 邮件链接显示文件列表,个人和群组公用一个jsp
   * 
   * @return
   */
  @Action("/psnweb/fileshare/emailviewfiles")
  public String emailViewFiles() {
    try {
      if (form.getGrpId() != null && form.getGrpId() != 0L) {
        grpFileShareService.findEmailVileFiles(form, page);
      } else {
        psnFileShareService.findEmailVileFiles(form, page);
      }
    } catch (Exception e) {
      logger.error("获取分享文件列表失败：", e);
    }
    return "emailviewfiles";
  }

}
