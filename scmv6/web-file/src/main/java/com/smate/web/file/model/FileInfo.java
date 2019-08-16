package com.smate.web.file.model;

import java.io.File;

import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 文件信息 对象 给来做后台方法数据传输参数用
 * 
 * @author tsz
 *
 */
public class FileInfo {

  private File file; // 临时文件对象

  private String fileName;// 文件名字

  private String fileContentType; // 文件内容类型

  private String filePath; // 文件路径

  private Long fileSize; // 文件大小

  private String filePathName; // 文件真实存放名字

  private ArchiveFile archiveFile; // 文件保存后对象

  private String fileDealType; // 文件处理类型

  private String rootPath; // 文件根路径

  private String resultMsg; // 上传结果信息

  private String fileSuffix; // 文件后缀

  private Long createPsnId; // 文件上传人

  private String imgData; // 图片编码

  private String fileUniqueKey; // 唯一文件 的路径key

  private String fileDesc; // 文件描述

  private String fileForm; // 文件来源

  public FileInfo() {
    super();
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileContentType() {
    return fileContentType;
  }

  public void setFileContentType(String fileContentType) {
    this.fileContentType = fileContentType;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getFilePathName() {
    return filePathName;
  }

  public void setFilePathName(String filePathName) {
    this.filePathName = filePathName;
  }

  public ArchiveFile getArchiveFile() {
    return archiveFile;
  }

  public void setArchiveFile(ArchiveFile archiveFile) {
    this.archiveFile = archiveFile;
  }

  public String getFileDealType() {
    return fileDealType;
  }

  public void setFileDealType(String fileDealType) {
    this.fileDealType = fileDealType;
  }

  public String getRootPath() {
    return rootPath;
  }

  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  public String getResultMsg() {
    return resultMsg;
  }

  public void setResultMsg(String resultMsg) {
    this.resultMsg = resultMsg;
  }

  public String getFileSuffix() {
    return fileSuffix;
  }

  public void setFileSuffix(String fileSuffix) {
    this.fileSuffix = fileSuffix;
  }

  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  @Override
  public String toString() {
    return JacksonUtils.jsonObjectSerializer(this);
  }

  public String getImgData() {
    return imgData;
  }

  public void setImgData(String imgData) {
    this.imgData = imgData;
  }

  public String getFileUniqueKey() {
    return fileUniqueKey;
  }

  public void setFileUniqueKey(String fileUniqueKey) {
    this.fileUniqueKey = fileUniqueKey;
  }

  /**
   * @return fileDesc
   */
  public String getFileDesc() {
    return fileDesc;
  }

  /**
   * @param fileDesc 要设置的 fileDesc
   */
  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  public String getFileForm() {
    return fileForm;
  }

  public void setFileForm(String fileForm) {
    this.fileForm = fileForm;
  }

}
