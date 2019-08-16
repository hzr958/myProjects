package com.smate.web.psn.action.file;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.form.FileMainForm;
import com.smate.web.psn.service.file.MyFileService;
import com.smate.web.psn.service.file.MyFileShareService;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件操作Action
 * 
 * @author zzx
 *
 */
@Results({})
public class OptFileAction extends ActionSupport implements ModelDriven<FileMainForm>, Preparable, Serializable {
  private static final long serialVersionUID = 1L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private FileMainForm form;
  @Resource
  private MyFileShareService myFileShareService;
  @Resource
  private MyFileService myFileService;

  /**
   * 取消文件分享
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxcancelfileshare")
  public String cancelFileShare() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      myFileService.cancelFileShare(form);
      myFileService.updateContent(form);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("取消文件分享出错  form=" + form, e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 批量删除我的文件
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxbatchdeletepsnfile")
  public String batchDeleteMyFile() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(form.getDes3FileIds())) {
        form.setPsnId(SecurityUtils.getCurrentUserId());
        myFileService.batchDelMyFile(form);
        map.put("count", form.getBatchCount().toString());
        map.put("status", "success");
      } else {
        map.put("status", "error");
        logger.error("需要删除的文件id不能为空:form.getDes3FileIds() = " + form.getDes3FileIds());
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("批量删除我的文件出错 form = " + form, e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 删除我的文件
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxdelmyfile")
  public String delMyFile() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      myFileService.delMyFile(form);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("删除我的文件出错  form=" + form, e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 保存文件描述
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxsavefiledesc")
  public String saveFileDesc() {
    try {
      myFileService.saveFileDesc(form);
    } catch (Exception e) {
      logger.error("保存文件描述出错  form=" + form, e);
    }
    Struts2Utils.renderJson(form.getResultMap(), "encoding:UTF-8");
    return null;
  }

  /**
   * 个人文件分享
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxsharemyfile")
  public String ajaxShareMyFile() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L && form.getReceiverId() != null
          && form.getMsgRelationId() != null && form.getShareBaseId() != null) {
        myFileShareService.shareMyFile(form);
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("个人文件分享，异常psnId=" + form.getPsnId(), e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 文件批量分享
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxsharemyfiles")
  public String shareFileAll() {
    form.setResultMap(new HashMap<String, Object>());
    int recount = 0, emcount = 0;// 只是为了返回提示信息的作用
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L && StringUtils.isNoneBlank(form.getDes3FileIds())
          && (StringUtils.isNoneBlank(form.getDes3ReceiverIds()) || StringUtils.isNotBlank(form.getReceiverEmails()))) {
        if (StringUtils.isNoneBlank(form.getDes3ReceiverIds())) {
          myFileShareService.shareAllMyFiles(form);
          recount++;
        }
        if (StringUtils.isNotBlank(form.getReceiverEmails())) {
          myFileShareService.shareAllMyFilesByEmails(form);
          emcount++;
        }
        if (recount == 1 && emcount == 1) {
          form.getResultMap().put("result", "success");
        }
      } else {
        form.getResultMap().put("result", "error");
      }
    } catch (Exception e) {
      logger.error("个人文件分享，异常psnId=" + form.getPsnId(), e);
      form.getResultMap().put("result", "error");
    }
    Struts2Utils.renderJson(form.getResultMap(), "encoding:UTF-8");
    return null;
  }

  /**
   * 获取个人文件分享主表的id
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxgetsharebaseid")
  public String ajaxGetShareBaseId() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L) {
        myFileShareService.getPsnFileShareBaseId(form);
        map.put("result", "success");
        map.put("shareBaseId", form.getShareBaseId());
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("个人文件分享，异常psnId=" + form.getPsnId(), e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 保存个人上传的文件
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajasavemyuploadfile")
  public String ajaSaveMyUploadFile() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L && form.getArchiveFileId() != null) {
        Long id = myFileService.saveMyUploadFile(form);
        map.put("result", "success");
        map.put("psnFileId", Des3Utils.encodeToDes3(id + ""));
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      logger.error(" 个人上传文件 保存  异常psnId=" + form.getPsnId(), e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  @Override
  public FileMainForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FileMainForm();
    }
  }
}
