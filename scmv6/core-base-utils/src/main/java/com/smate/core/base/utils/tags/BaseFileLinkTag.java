package com.smate.core.base.utils.tags;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 
 * 获取文件下载链接，如果fileCode为空，则直接返回不带code的链接.
 * 
 * @author zk
 * @since 6.0.1
 */
public abstract class BaseFileLinkTag extends BodyTagSupport {

  /**
   * 
   */
  private static final long serialVersionUID = -2994206630247488683L;


  public static final String SNS_CONTEXT_PATH = "/scmwebsns";
  public static final String ROL_CONTEXT_PATH = "/scmwebrol";
  // ID匹配fileid_nodeid_insid
  public static final String ID_PATTEM = "%s_%s_%s";

  private String fileCode = "";
  private String nodeId = "";
  private String insId = "";
  private Integer linkType = 0;
  private Long ownerPsnId;
  private Integer permission;

  @Override
  public int doStartTag() throws JspException {

    String bar = "";
    if (StringUtils.isNotBlank(fileCode)) {
      fileCode = fileCode.trim();
      try {
        bar = ArchiveFileUtil.getFDesId(fileCode, nodeId, insId);
      } catch (Exception e) {
        throw new JspException(e);
      }
    }
    try {
      String domain = "";
      if (StringUtils.isBlank(getDomain())) {
        domain = "/scmwebsns";
      } else {
        domain = getDomain() + "/scmwebsns";
      }
      if (linkType == null || linkType == 0) {// 无需登录
        pageContext.getOut().write(domain + ArchiveFileUtil.DOWNLOAD_ACTION_URL_NO_VER + bar);
      } else if (linkType == 1) {// 需登录
        pageContext.getOut().write(domain + ArchiveFileUtil.DOWNLOAD_ACTION_URL + bar);
      } else if (linkType == 3) {
        pageContext.getOut()
            .write(domain + ArchiveFileUtil.PERMISSION_DOWNLOAD_ACTION_URL + bar + "&permissionStr="
                + URLEncoder.encode(
                    ServiceUtil.encodeToDes3((permission == null ? 0 : permission) + "," + new Date().toString()))
                + "&ownerPsnId=" + URLEncoder.encode(ServiceUtil.encodeToDes3(ownerPsnId.toString())));
      } else {// 需要验证权限
        pageContext.getOut()
            .write(domain + ArchiveFileUtil.DOWNLOAD_ACTION_URL_PERMISSION + bar + "&permissionStr="
                + URLEncoder.encode(
                    ServiceUtil.encodeToDes3((permission == null ? 0 : permission) + "," + new Date().toString()))
                + "&ownerPsnId=" + URLEncoder.encode(ServiceUtil.encodeToDes3(ownerPsnId.toString())));
      }
    } catch (IOException e) {
      throw new JspException(e);
    }
    return SKIP_BODY;
  }

  public static String getFileUrl(String url, Long fileCode, Integer nodeId, Long insId) {

    return ArchiveFileUtil.generateDownloadLink(url, fileCode, nodeId, insId);
  }

  public abstract String getDomain() throws JspException;

  public String getFileCode() {
    return fileCode;
  }

  public void setFileCode(String fileCode) {
    this.fileCode = fileCode;
  }

  public String getNodeId() {
    return nodeId;
  }

  public String getInsId() {
    return insId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public void setInsId(String insId) {
    this.insId = insId;
  }

  public Integer getLinkType() {
    return linkType;
  }

  public void setLinkType(Integer linkType) {
    this.linkType = linkType;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  public Integer getPermission() {
    return permission;
  }

  public void setPermission(Integer permission) {
    this.permission = permission;
  }

}
