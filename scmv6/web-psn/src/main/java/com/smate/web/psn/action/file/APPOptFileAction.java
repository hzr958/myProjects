package com.smate.web.psn.action.file;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.form.FileMainForm;
import com.smate.web.psn.service.file.MyFileService;
import com.smate.web.psn.service.file.MyFileShareService;

/**
 * app我的文件操作数据接口
 * 
 * @author LIJUN
 *
 */
public class APPOptFileAction extends ActionSupport implements ModelDriven<FileMainForm>, Preparable, Serializable {
  private static final long serialVersionUID = 1L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private FileMainForm form;
  @Resource
  private MyFileShareService myFileShareService;
  @Resource
  private MyFileService myFileService;
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态

  @Override
  public FileMainForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FileMainForm();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  /**
   * 删除我的文件
   * 
   * @return
   */
  @Action("/app/psnweb/myfile/ajaxdelmyfile")
  public String delMyFile() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (NumberUtils.isNotNullOrZero(form.getFileId())) {
        myFileService.delMyFile(form);
        map.put("result", "success");
        status = IOSHttpStatus.OK;
      } else {
        map.put("result", "error");
        status = IOSHttpStatus.BAD_REQUEST;
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      map.put("result", "error");
      logger.error("删除我的文件出错  form=" + form, e);
    }
    AppActionUtils.renderAPPReturnJson(map.get("result"), total, status);
    return null;
  }

  /**
   * 保存文件描述
   * 
   * @return
   */
  @Action("/app/psnweb/myfile/ajaxsavefiledesc")
  public String saveFileDesc() {
    try {
      if (NumberUtils.isNotNullOrZero(form.getFileId())) {
        myFileService.saveFileDesc(form);
        status = IOSHttpStatus.OK;
      } else {
        status = IOSHttpStatus.BAD_REQUEST;
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("保存文件描述出错  form=" + form, e);
    }
    AppActionUtils.renderAPPReturnJson(form.getResultMap(), total, status);
    return null;
  }

  /**
   * 文件批量分享
   * 
   * @return
   */
  @Action("/app/psnweb/myfile/ajaxsharemyfiles")
  public String shareFileAll() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L && StringUtils.isNoneBlank(form.getDes3FileIds())
          && StringUtils.isNoneBlank(form.getDes3ReceiverIds()) && StringUtils.isNoneBlank(form.getFileNames())) {
        myFileShareService.shareAllMyFiles(form);
        map.put("result", "success");
        status = IOSHttpStatus.OK;
      } else {
        status = IOSHttpStatus.BAD_REQUEST;
        map.put("result", "error");
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("个人文件分享，异常psnId=" + form.getPsnId(), e);
      map.put("result", "error");
    }
    AppActionUtils.renderAPPReturnJson(map.get("result"), total, status);
    return null;
  }

}
