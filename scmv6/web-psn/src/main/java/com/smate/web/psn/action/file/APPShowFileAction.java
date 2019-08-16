package com.smate.web.psn.action.file;

import java.io.Serializable;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.form.FileMainForm;
import com.smate.web.psn.form.PsnFileInfo;
import com.smate.web.psn.service.file.MyFileService;

/**
 * app个人文件数据接口
 * 
 * @author LJ
 *
 *         2017年10月18日
 */
public class APPShowFileAction extends ActionSupport implements ModelDriven<FileMainForm>, Preparable, Serializable {
  private static final long serialVersionUID = 1L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private FileMainForm form;
  @Autowired
  private MyFileService myFileService;
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态

  /**
   * 我的文件列表
   * 
   * @return
   */
  @Action("/app/psnweb/myfile/ajaxmyfilelist")
  public String showMyFileListItem() {
    try {
      myFileService.getFileList(form);
      total = form.getPage().getTotalCount().intValue();
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error(" 获取我的文件列表出错  form=" + form, e);
    }

    AppActionUtils.renderAPPReturnJson(form.getPsnFileInfoList(), total, status);
    return null;

  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FileMainForm();
      form.setPage(new Page<PsnFileInfo>());
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  @Override
  public FileMainForm getModel() {
    return form;
  }

}
