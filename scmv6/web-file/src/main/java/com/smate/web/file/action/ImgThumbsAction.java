package com.smate.web.file.action;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 缩略图action
 * 
 * @author houchuanjie
 * @date 2018年1月2日 下午4:00:13
 */
public class ImgThumbsAction extends ActionSupport {
  private static final long serialVersionUID = -5072022325910086488L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private String des3FileId;
  @Autowired
  private ArchiveFileService archiveFileService;

  @Action("/fileweb/img-thumb")
  public void getImgThumb() {
    if (StringUtils.isNotBlank(des3FileId)) {
      try {
        String thumbUrl = archiveFileService.getImgFileThumbUrl(des3FileId);
        if (StringUtils.isNotBlank(thumbUrl)) {
          Struts2Utils.redirect(thumbUrl);
        }
      } catch (IOException e) {
        logger.error("获取文件缩略图失败！archiveFileId={}，错误原因：{}", des3FileId, e.getMessage());
      }
    }
  }

  /**
   * @return des3FileId
   */
  public String getDes3FileId() {
    return des3FileId;
  }

  /**
   * @param des3FileId 要设置的 des3FileId
   */
  public void setDes3FileId(String des3FileId) {
    this.des3FileId = des3FileId;
  }
}
