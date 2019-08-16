package com.smate.web.psn.action.mobile.file;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.form.FileMainForm;
import com.smate.web.psn.form.PsnFileInfo;
import com.smate.web.psn.service.file.MyFileService;
import com.smate.web.psn.service.file.MyFileShareService;

@Results({@Result(name = "file_main", location = "/WEB-INF/jsp/mobile/file/mobile_file_main.jsp"),
    @Result(name = "file_list", location = "/WEB-INF/jsp/mobile/file/mobile_file_list.jsp"),
    @Result(name = "viewFileShareFromEmail", location = "/WEB-INF/jsp/mobile/file/mobile_share_file.jsp")

})
public class FileViewAction extends ActionSupport implements ModelDriven<FileMainForm>, Preparable, Serializable {

  /**
   * 移动端文件操作
   */
  private static final long serialVersionUID = 1L;
  private FileMainForm form;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MyFileService myFileService;
  @Autowired
  private MyFileShareService myFileShareService;

  /**
   * 移动端打开邮件分享文件
   */
  @Action("/psnweb/mobile/emailsharefile")
  public String viewFileShareFromEmail() throws Exception {
    // 不需要new判断 ， 如果报错请不要添加 2018-01-04ajb
    if (StringUtils.isNotBlank(form.getA()) && StringUtils.isNotBlank(form.getB())) {
      Long resSendId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getA()), 0L);
      Long resReveiverId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getB()), 0L);
      Long baseId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getC()), 0L);

      // 群组分享的文件
      if (form.getGrpId() != null && resSendId != 0L && resReveiverId != 0L && baseId != 0L) {
        int status = myFileShareService.checkGrpFileShareStatus(baseId);
        if (status == -1) {
          if (SecurityUtils.getCurrentUserId() == null || SecurityUtils.getCurrentUserId() == 0l) {
            form.setStatus(0);
          } else {
            form.setStatus(1);
          }
          return "viewFileShareFromEmail";
        }
        myFileShareService.getGrpFileShareDataInSendSide(resSendId, resReveiverId, baseId, form);
        if (SecurityUtils.getCurrentUserId() == null || SecurityUtils.getCurrentUserId() == 0l) {
          form.setStatus(0);
          HttpServletRequest requerst = Struts2Utils.getRequest();
          String uri = requerst.getServletPath() + "?from=new&A=" + ServiceUtil.encodeToDes3(resSendId.toString())
              + "&B=" + ServiceUtil.encodeToDes3(resReveiverId.toString()) + "&C="
              + ServiceUtil.encodeToDes3(baseId.toString()) + "&des3GrpId=" + form.getDes3GrpId();
          form.setUrl("/oauth/index?service=" + ServiceUtil.encodeToDes3(uri));
        } else {
          form.setStatus(1);
        }
      } else if (resSendId != 0L && resReveiverId != 0L && baseId != 0L) {
        // int status = myFileShareService.checkNewShareStatus(baseId);
        // 不管状态如何都需要设置登录uri 和登陆之后跳转的逻辑 SCM-22742
        myFileShareService.getFileShareDataInSendSide(resSendId, resReveiverId, baseId, form);
        if (SecurityUtils.getCurrentUserId() == null || SecurityUtils.getCurrentUserId() == 0l) {
          form.setStatus(0);
          HttpServletRequest requerst = Struts2Utils.getRequest();
          String uri = requerst.getServletPath() + "?from=new&A=" + ServiceUtil.encodeToDes3(resSendId.toString())
              + "&B=" + ServiceUtil.encodeToDes3(resReveiverId.toString()) + "&C="
              + ServiceUtil.encodeToDes3(baseId.toString());
          form.setUrl("/oauth/index?service=" + ServiceUtil.encodeToDes3(uri));
        } else {
          form.setStatus(1);
        }
      }
    }
    return "viewFileShareFromEmail";
  }

  /**
   * 文件主页
   * 
   * @return
   */
  @Action("/psnweb/mobile/filemain")
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
  @Action("/psnweb/mobile/ajaxmyfilelist")
  public String showMyFileListItem() {
    try {
      myFileService.getFileList(form);

    } catch (Exception e) {
      logger.error(" 获取我的文件列表出错  form=" + form, e);
    }
    return "file_list";

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
    // TODO Auto-generated method stub
    return form;
  }

}
