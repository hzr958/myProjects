package com.smate.web.psn.action.file;

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
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.form.FileMainForm;
import com.smate.web.psn.form.PsnFileInfo;
import com.smate.web.psn.service.file.MyFileService;

/**
 * 文件显示Action
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "file_main", location = "/WEB-INF/jsp/file/file_main.jsp"),
    @Result(name = "file_myfile_list", location = "/WEB-INF/jsp/file/myfile_list.jsp"),
    @Result(name = "mobile_myfile_list", location = "/WEB-INF/jsp/file/mobile_myfile_list.jsp"),
    @Result(name = "file_for_psn", location = "/WEB-INF/jsp/myfile/myfileforgrp.jsp"),
    @Result(name = "file_share_records_list", location = "/WEB-INF/jsp/file/sharerecords_list.jsp")

})
public class ShowFileAction extends ActionSupport implements ModelDriven<FileMainForm>, Preparable, Serializable {
  private static final long serialVersionUID = 1L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private FileMainForm form;
  @Autowired
  private MyFileService myFileService;

  /**
   * 文件主页
   * 
   * @return
   */
  @Action("/psnweb/myfile/filemain")
  public String showFileMain() {
    try {
    } catch (Exception e) {
      logger.error(" 进入文件主页出错  form=" + form, e);
    }
    return "file_main";
  }

  /**
   * 我的文件列表
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxmyfilelist")
  public String showMyFileListItem() {
    try {
      myFileService.getFileListForPsn(form);
    } catch (Exception e) {
      logger.error(" 获取我的文件列表出错  form=" + form, e);
    }
    if ("isPFBox".equals(form.getSource())) {
      return "file_for_psn";
    } else {
      return "file_myfile_list";
    }
  }

  /**
   * 移动端 我的文件列表
   * 
   * @return
   */
  @Action("/psnweb/mobile/ajaxmsgfilelist")
  public String midShowMyFileListItem() {
    try {
      myFileService.getFileListForPsn(form);
    } catch (Exception e) {
      logger.error(" 获取我的文件列表出错  form=" + form, e);
    }
    return "mobile_myfile_list";
  }

  /**
   * 我的文件列表-统计数据
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxmyfilelistcallback")
  public String showMyFileListCallback() {
    try {
      myFileService.getFileListCallBack(form);
    } catch (Exception e) {
      logger.error("我的文件列表-统计数据出错 form=" + form, e);
    }
    Struts2Utils.renderJson(form.getResultMap(), "encoding:UTF-8");
    return null;
  }

  /**
   * 文件推荐列表
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxfilerecommendlistitem")
  public String showFileRecommendItem() {
    try {
      // TODO
    } catch (Exception e) {
      logger.error("   form=" + form, e);
    }
    return "file_recommend_list_item";
  }

  /**
   * 文件分享记录列表
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxfilesharerecordslist")
  public String showFileShareRecordsList() {
    try {
      myFileService.getShareRecordsList(form);
    } catch (Exception e) {
      logger.error(" 文件分享记录列表出错  form=" + form, e);
    }
    return "file_share_records_list";
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FileMainForm();
      form.setPage(new Page<PsnFileInfo>());
    }
  }

  @Override
  public FileMainForm getModel() {
    return form;
  }

}
