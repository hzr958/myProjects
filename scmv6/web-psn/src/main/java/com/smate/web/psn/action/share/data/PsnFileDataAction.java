package com.smate.web.psn.action.share.data;

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
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.TheadLocalPsnId;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.form.FileMainForm;
import com.smate.web.psn.service.file.MyFileService;
import com.smate.web.psn.service.file.MyFileShareService;

/**
 * app我的文件操作数据接口
 * 
 * @author LIJUN
 *
 */
public class PsnFileDataAction extends ActionSupport implements ModelDriven<FileMainForm>, Preparable, Serializable {
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
  }

  /**
   * 分享个人文件给联系人
   * 
   * @return
   */
  @Action("/psndata/sharefile/tofriend")
  public void shareFileToFriend() {
    Map<String, Object> map = new HashMap<String, Object>();
    String status = "error";
    try {
      Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L);
      if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNoneBlank(form.getDes3FileIds())
          && StringUtils.isNoneBlank(form.getDes3ReceiverIds())) {
        form.setPsnId(psnId);
        TheadLocalPsnId.setPsnId(psnId);
        myFileShareService.shareAllMyFiles(form);
        status = "success";
      }
    } catch (Exception e) {
      logger.error("分享个人文件给好友异常, psnId={}, receiverIds={}, fileIds={}", form.getDes3PsnId(), form.getDes3ReceiverIds(),
          form.getDes3FileIds(), e);
    }
    map.put("status", status);
    Struts2Utils.renderJson(map, "encoding: UTF-8");
  }

}
