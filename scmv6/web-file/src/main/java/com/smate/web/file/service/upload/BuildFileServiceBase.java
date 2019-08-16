package com.smate.web.file.service.upload;

import java.util.Date;
import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.web.file.form.FileUploadForm;
import com.smate.web.file.model.FileInfo;

/**
 * 构建文件基础类
 * 
 * @author tsz
 *
 */
@Transactional(rollbackOn = Exception.class)
public abstract class BuildFileServiceBase implements BuildFileService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private static final String allowTypeAll = "ALL";
  // 最大上传50M文件
  private static final Integer FILE_SIZE_50 = 50;
  // 最大上传70M文件
  private static final Integer FILE_SIZE_70 = 70;
  /**
   * 允许上传的文件内容类型 用逗号隔开 "txt,pdf,doc,docx,ppt,pptx,xls,xlsx,rar,zip,jpg,png,gif,jpeg,htm,html,xhtml"
   * 
   * 默认没有文件类型限制
   */
  private String allowType = allowTypeAll;

  /**
   * 允许上传的文件内容 的大小 单位M
   */
  private Integer allowSize = FILE_SIZE_50; // 最大可上传文件大小 单位M

  /**
   * 文件上下文路径 区分文件要放到哪个文件夹上
   */
  private String basicPath = "/upfile";

  private String imgType = "png";

  @Autowired
  private ArchiveFileDao archiveFileDao;

  /**
   * 检查文件
   * 
   * @return
   */
  public abstract Boolean checkFile(FileInfo fileInfo) throws Exception;

  /**
   * 写入文件到文件服务器（包括写算文件路径）
   */
  public abstract void writeFile(FileInfo fileInfo) throws Exception;

  /**
   * 保存文件记录到数据库(预留扩展接口)
   */
  public abstract void saveFile(FileInfo fileInfo) throws Exception;

  @Override
  public void build(FileUploadForm form) {
    // TODO 自动生成的方法存根

  }

  @Override
  public Boolean buildFile(FileInfo fileInfo) throws Exception {
    // 通用检查 检查文件类型 文件大小
    if (!commonCheck(fileInfo)) {
      return false;
    }
    // 具体业务检查 没有就retorn true;
    if (!checkFile(fileInfo)) {
      return false;
    }
    // 写文件
    writeFile(fileInfo);
    // 保存数据库记录
    saveArchiveFile(fileInfo);
    // 扩展保存
    saveFile(fileInfo);

    return true;
  }

  /**
   * 检查文件类型 文件大小
   * 
   * @param fileInfo
   * @return
   * @throws Exception
   */
  private boolean commonCheck(FileInfo fileInfo) throws Exception {
    Locale locale = LocaleContextHolder.getLocale();
    String fileSuffix = "";
    // 上传头像， 没有文件名SCM-15899
    if (StringUtils.isNotBlank(fileInfo.getFileName())) {
      fileSuffix = fileInfo.getFileName().substring(fileInfo.getFileName().lastIndexOf(".") + 1).toLowerCase();
      fileInfo.setFileSuffix(fileSuffix);
    }
    // 检查文件类型是否被允许
    if (!allowTypeAll.equalsIgnoreCase(getAllowType())) { // 如果是所有类型 就不检查了
      if (getAllowType().indexOf(fileSuffix) < 0) {
        if (locale.equals(Locale.US)) {
          fileInfo.setResultMsg("Deny this file type for upload!");
        } else {
          fileInfo.setResultMsg("该文件类型不允许上传");
        }
        return false;
      }
    }
    // 检查文件大小
    Long fileSize = StringUtils.isBlank(fileInfo.getImgData()) ? ArchiveFileUtil.getFileSize(fileInfo.getFile())
        : Long.parseLong(ArchiveFileUtil.getFileSize(fileInfo.getImgData()).toString());
    fileInfo.setFileSize(fileSize);
    if (fileSize == 0) {
      if (locale.equals(Locale.US)) {
        fileInfo.setResultMsg("Don't upload blank file!");
      } else {
        fileInfo.setResultMsg("不能上传空文件！");
      }
      return false;
    }
    setAllowSize(getAllowSize() > FILE_SIZE_70 ? FILE_SIZE_70 : getAllowSize());
    if (fileSize > ArchiveFileUtil.getMBByte(getAllowSize())) {
      if (locale.equals(Locale.US)) {
        fileInfo.setResultMsg("Please upload a file within " + getAllowSize() + ".");
      } else {
        fileInfo.setResultMsg("请选择" + getAllowSize() + "范围内的文件。");
      }
      return false;
    }
    return true;
  }

  /**
   * 保存问文件
   */
  private void saveArchiveFile(FileInfo fileInfo) {
    // 构建 ArchiveFile 保存
    ArchiveFile archiveFile = new ArchiveFile();
    archiveFile.setCreatePsnId(fileInfo.getCreatePsnId());
    archiveFile.setCreateTime(new Date());
    archiveFile.setFileName(fileInfo.getFileName());
    archiveFile.setFilePath(fileInfo.getFilePathName());
    archiveFile.setFileType(ArchiveFileUtil.getFileType(fileInfo.getFileName()));
    archiveFile.setFileUrl(fileInfo.getFilePath());
    archiveFile.setFileSize(fileInfo.getFileSize());
    archiveFile.setFileDesc(fileInfo.getFileDesc());
    archiveFile.setStatus(0);
    archiveFile.setFileFrom(fileInfo.getFileForm());
    this.archiveFileDao.save(archiveFile);
    fileInfo.setArchiveFile(archiveFile);
  }

  public String getAllowType() {
    return allowType;
  }

  public void setAllowType(String allowType) {
    this.allowType = allowType;
  }

  public Integer getAllowSize() {
    return allowSize;
  }

  public void setAllowSize(Integer allowSize) {
    this.allowSize = allowSize;
  }

  public String getBasicPath() {
    return basicPath;
  }

  public void setBasicPath(String basicPath) {
    this.basicPath = basicPath;
  }

  public String getImgType() {
    return imgType;
  }

  public void setImgType(String imgType) {
    this.imgType = imgType;
  }

}
