package com.smate.web.file.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.file.model.rol.RolArchiveFile;

/**
 * 
 * @author tsz
 *
 */
public class FileDownloadForm {

  private String des3fids; // 业务文件加密id列表，以“,”分割

  private Long fileId; // 业务文件id

  private String fileType; // 文件类型

  private String key; // 下载连接随机key

  private boolean shortUrl = false; // 是否短地址 默认false

  private ArchiveFile archiveFile;

  private RolArchiveFile rolArchiveFile;

  private Long currentPsnId;

  private String resPath;

  private Boolean result;

  private String resultMsg;

  // 文件所有人
  private Long ownerPsnId;

  private Long pubId;

  private Boolean formSie = false;// 来源sie

  private List<Long> fileIdList; // 多个文件id列表
  private String from;// 来源sns（个人库）pdwh（基准库）

  /**
   * 下载文件资源Map集合，DownloadFileRes的id作为key，DownloadFileRes实例对象作为value
   */
  private Map<Long, DownloadFileRes> downloadFileResMap;

  public FileDownloadForm() {
    super();
    downloadFileResMap = new HashMap<>();
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public ArchiveFile getArchiveFile() {
    return archiveFile;
  }

  public void setArchiveFile(ArchiveFile archiveFile) {
    this.archiveFile = archiveFile;
  }

  public Long getCurrentPsnId() {
    if (currentPsnId == null) {
      currentPsnId = SecurityUtils.getCurrentUserId();
    }
    return currentPsnId;
  }

  public void setCurrentPsnId(Long currentPsnId) {
    this.currentPsnId = currentPsnId;
  }

  public String getResPath() {
    return resPath;
  }

  public void setResPath(String resPath) {
    this.resPath = resPath;
  }

  public FileTypeEnum getFileType() {
    FileTypeEnum fileTypeEnum = FileTypeEnum.valueOf(Integer.parseInt(fileType));
    return fileTypeEnum;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public Boolean getResult() {
    return result;
  }

  public void setResult(Boolean result) {
    this.result = result;
  }

  public String getResultMsg() {
    return resultMsg;
  }

  public void setResultMsg(String resultMsg) {
    this.resultMsg = resultMsg;
  }

  public Long getFileId() {

    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  /**
   * @return the shortUrl
   */
  public boolean isShortUrl() {
    return shortUrl;
  }

  /**
   * @param shortUrl the shortUrl to set
   */
  public void setShortUrl(boolean shortUrl) {
    this.shortUrl = shortUrl;
  }

  @Override
  public String toString() {
    return JacksonUtils.jsonObjectSerializer(this);
  }

  /**
   *
   * @author houchuanjie
   * @date 2017年12月1日 下午2:51:19
   * @return
   */
  public Long getOwnerPsnId() {
    return this.ownerPsnId;
  }

  /**
   * @param ownerPsnId 要设置的 ownerPsnId
   */
  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  public Boolean getFormSie() {
    return formSie;
  }

  public void setFormSie(Boolean formSie) {
    this.formSie = formSie;
  }

  public RolArchiveFile getRolArchiveFile() {
    return rolArchiveFile;
  }

  public void setRolArchiveFile(RolArchiveFile rolArchiveFile) {
    this.rolArchiveFile = rolArchiveFile;
  }

  /**
   * @return des3fids
   */
  public String getDes3fids() {
    return des3fids;
  }

  /**
   * @param des3fids 要设置的 des3fids
   */
  public void setDes3fids(String des3fids) {
    this.des3fids = des3fids;
  }

  /**
   * @return fileIdList
   */
  public List<Long> getFileIdList() {
    return fileIdList;
  }

  /**
   * @param fileIdList 要设置的 fileIdList
   */
  public void setFileIdList(List<Long> fileIdList) {
    this.fileIdList = fileIdList;
  }

  /**
   * 获取下载文件资源集合
   * 
   * @return downloadFileResMap
   */
  public Map<Long, DownloadFileRes> getDownloadFileResMap() {
    return downloadFileResMap;
  }

  /**
   * 设置下载文件资源集合
   * 
   * @param downloadFileResMap 要设置的 downloadFileResMap
   */
  public void setDownloadFileResMap(Map<Long, DownloadFileRes> downloadFileResMap) {
    this.downloadFileResMap = downloadFileResMap;
  }

  /**
   * 增加一个文件下载资源，DownloadFileRes的id作为key，res作为value放入downloadFileResMap中，如果id已存在的，则会覆盖原值
   *
   * @author houchuanjie
   * @date 2018年3月7日 下午3:19:18
   * @param res
   */
  public void addDownloadFileRes(DownloadFileRes res) {
    this.downloadFileResMap.put(res.getId(), res);
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

}
