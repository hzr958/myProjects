package com.smate.core.base.psn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 用户文件.
 * 
 * @author zk.
 * 
 */
@Entity
@Table(name = "STATION_FILE")
public class StationFile implements Serializable {

  private static final long serialVersionUID = -7587694014422023176L;

  private Long fileId;// 文件编号.

  private Long psnId;// 文件拥有者编号.

  private String fileName;// 文件全名称.

  private String fileViewName;// 列表显示文件名称.

  private String filePath;// 文件存放路径.

  private String fileType;// 文件类型.

  private String fileViewType;// 列表显示文件类型.

  private Long fileSize;// 文件大小.

  private String fileDesc;// 文件描述.

  /** 文件状态，0未删除/1已删除. */
  private int fileStatus;// 文件状态.

  private int fileScore;// 文件评分.

  private Date uploadTime;// 上传时间.

  private Date createTime;// 分享发起时间

  private int fileNodeId;// 文件存放节点.

  /** 是否共享，０不是/1是/2推荐未确认/3已忽略. */
  private int isShare;

  private int isGroup;// 是否群组

  private int pemission;// 0:所有人，1：好友；2：仅本人.

  private String des3Id;// 加密文件编号.

  // 总评分
  private Integer fileStart = 0;
  // 评分总人数
  private Integer fileStartPsns = 0;
  // 评价人数
  private Integer fileReviews = 0;
  // 截取转义文件描述
  private String subFileDesc;
  // 转义文件描述
  private String escapeFileDesc;
  // 节点域名
  private String domain;

  private Date from;
  private Date to;
  private String searchKey;
  private Long archiveFileId;
  private String des3ArchiveFileId;
  private String fileSizeStr;
  private int averageStar;
  private int isSuffix;
  // 是否好友可见
  private boolean isFriView;
  // 是否赞过
  private boolean isAward;
  private Long awardIds;
  private Long shareTimes;
  /** 推荐者头像. */
  private String psnAvatars;
  /** 人员推荐显示头衔：头衔->单位->国家地区. */
  private String titolo;
  /** 共享人id. */
  private Long recommendPsnId;
  /** 共享人名称. */
  private String recommendPsnName;
  private Long resRecId;
  private String downloadUrl;

  public StationFile() {}

  public StationFile(Long fileId, Long psnId, int fileStatus) {
    super();
    this.fileId = fileId;
    this.psnId = psnId;
    this.fileStatus = fileStatus;
  }

  public StationFile(Long fileId, String fileName) {
    super();
    this.fileId = fileId;
    this.fileName = fileName;
  }

  public StationFile(Long fileId, Long recommendPsnId, String fileName, String fileType, String filePath,
      int fileNodeId) {
    this.fileId = fileId;
    this.recommendPsnId = recommendPsnId;
    this.fileName = fileName;
    this.fileType = fileType;
    this.filePath = filePath;
    this.fileNodeId = fileNodeId;
  }

  public StationFile(Long fileId, Long recommendPsnId, String fileName, String fileType, String filePath,
      int fileNodeId, Date createTime) {
    this.fileId = fileId;
    this.recommendPsnId = recommendPsnId;
    this.fileName = fileName;
    this.fileType = fileType;
    this.filePath = filePath;
    this.fileNodeId = fileNodeId;
    this.createTime = createTime;
  }

  public StationFile(Long fileId, Long recommendPsnId, String fileName, String fileType, String filePath,
      int fileNodeId, Date createTime, Long resRecId) {
    this.fileId = fileId;
    this.recommendPsnId = recommendPsnId;
    this.fileName = fileName;
    this.fileType = fileType;
    this.filePath = filePath;
    this.fileNodeId = fileNodeId;
    this.createTime = createTime;
    this.resRecId = resRecId;
  }

  public StationFile(Long fileId, Long psnId, String fileName, String filePath, String fileType, Long fileSize,
      String fileDesc, Long archiveFileId) {
    this.fileId = fileId;
    this.psnId = psnId;
    this.fileName = fileName;
    this.filePath = filePath;
    this.fileSize = fileSize;
    this.fileDesc = fileDesc;
    this.fileType = fileType;
    this.archiveFileId = archiveFileId;
  }

  /**
   * 群组文件模块，我的文件列表
   * 
   * @param fileId
   * @param fileName
   * @param filePath
   * @param fileType
   * @param fileDesc
   */
  public StationFile(Long fileId, String fileName, String filePath, String fileType, String fileDesc) {
    super();
    this.fileId = fileId;
    this.fileName = fileName;
    this.filePath = filePath;
    this.fileType = fileType;
    this.fileDesc = fileDesc;
  }

  /**
   * 群组文件模块，我的文件列表 grp
   * 
   * @param fileId
   * @param fileName
   * @param filePath
   * @param fileType
   * @param fileDesc
   */
  public StationFile(Long fileId, String fileName, String filePath, String fileType, String fileDesc, Date uploadTime,
      Long archiveFileId) {
    super();
    this.fileId = fileId;
    this.fileName = fileName;
    this.filePath = filePath;
    this.fileType = fileType;
    this.fileDesc = fileDesc;
    this.uploadTime = uploadTime;
    this.archiveFileId = archiveFileId;
  }

  @Id
  @Column(name = "FILE_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_STATION_FILE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  @Column(name = "OWNER_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "FILE_NAME")
  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  @Column(name = "FILE_PATH")
  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  @Column(name = "FILE_TYPE")
  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  @Column(name = "FILE_SIZE")
  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  @Column(name = "FILE_DESC")
  public String getFileDesc() {
    return fileDesc;
  }

  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  @Column(name = "FILE_STATUS")
  public int getFileStatus() {
    return fileStatus;
  }

  public void setFileStatus(int fileStatus) {
    this.fileStatus = fileStatus;
  }

  @Column(name = "UPLOAD_TIME")
  public Date getUploadTime() {
    return uploadTime;
  }

  public void setUploadTime(Date uploadTime) {
    this.uploadTime = uploadTime;
  }

  @Transient
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Column(name = "FILE_SCORE")
  public int getFileScore() {
    return fileScore;
  }

  public void setFileScore(int fileScore) {
    this.fileScore = fileScore;
  }

  @Column(name = "FILE_NODE_ID")
  public int getFileNodeId() {
    return fileNodeId;
  }

  public void setFileNodeId(int fileNodeId) {
    this.fileNodeId = fileNodeId;
  }

  @Column(name = "IS_SHARE")
  public int getIsShare() {
    return isShare;
  }

  public void setIsShare(int isShare) {
    this.isShare = isShare;
  }

  @Column(name = "IS_GROUP")
  public int getIsGroup() {
    return isGroup;
  }

  public void setIsGroup(int isGroup) {
    this.isGroup = isGroup;
  }

  @Column(name = "PEMISSION")
  public int getPemission() {
    return pemission;
  }

  public void setPemission(int pemission) {
    this.pemission = pemission;
  }

  @Column(name = "FILE_START")
  public Integer getFileStart() {
    return fileStart;
  }

  public void setFileStart(Integer fileStart) {
    this.fileStart = fileStart;
  }

  @Column(name = "FILE_REVIEWS")
  public Integer getFileStartPsns() {
    return fileStartPsns;
  }

  public void setFileStartPsns(Integer fileStartPsns) {
    this.fileStartPsns = fileStartPsns;
  }

  @Column(name = "FILE_START_PSNS")
  public Integer getFileReviews() {
    return fileReviews;
  }

  public void setFileReviews(Integer fileReviews) {
    this.fileReviews = fileReviews;
  }

  @Transient
  public String getDes3Id() {
    if (this.fileId != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.fileId.toString());
    }
    return des3Id;
  }

  @Transient
  public String getSubFileDesc() {
    if (this.fileDesc != null) {
      if (this.fileDesc.length() > 32)
        subFileDesc = this.fileDesc.substring(0, 32) + "...";
      else
        subFileDesc = this.fileDesc;
    }
    return HtmlUtils.toHtml(subFileDesc);
  }

  @Transient
  public String getEscapeFileDesc() {
    if (this.fileDesc != null)
      escapeFileDesc = HtmlUtils.toHtml(this.fileDesc);
    return escapeFileDesc;
  }

  @Transient
  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  /**
   * @return the from
   */
  @Transient
  public Date getFrom() {
    return from;
  }

  /**
   * @param from the from to set
   */
  public void setFrom(Date from) {
    this.from = from;
  }

  /**
   * @return the to
   */
  @Transient
  public Date getTo() {
    return to;
  }

  /**
   * @param to the to to set
   */
  public void setTo(Date to) {
    this.to = to;
  }

  /**
   * @return the searchKey
   */
  @Transient
  public String getSearchKey() {
    return searchKey;
  }

  /**
   * @param searchKey the searchKey to set
   */
  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  @Column(name = "ARCHIVE_FILE_ID")
  public Long getArchiveFileId() {
    return archiveFileId;
  }

  public void setArchiveFileId(Long archiveFileId) {
    this.archiveFileId = archiveFileId;
  }

  @Transient
  public int getAverageStar() {
    if (fileStartPsns != 0) {
      averageStar = fileStart / fileStartPsns;
      int remainder = fileStart % fileStartPsns;
      if (remainder != 0)
        averageStar = averageStar + 1;
    } else
      averageStar = 0;
    return averageStar;
  }

  @Transient
  public int getIsSuffix() {
    return isSuffix;
  }

  public void setIsSuffix(int isSuffix) {
    this.isSuffix = isSuffix;
  }

  @Transient
  public boolean getIsAward() {
    return isAward;
  }

  public void setIsAward(boolean isAward) {
    this.isAward = isAward;
  }

  @Transient
  public Long getAwardIds() {
    return awardIds;
  }

  public void setAwardIds(Long awardIds) {
    this.awardIds = awardIds;
  }

  @Transient
  public Long getShareTimes() {
    return shareTimes;
  }

  public void setShareTimes(Long shareTimes) {
    this.shareTimes = shareTimes;
  }

  @Transient
  public String getDes3ArchiveFileId() {
    return ServiceUtil.encodeToDes3(this.getArchiveFileId().toString());
  }

  public void setDes3ArchiveFileId(String des3ArchiveFileId) {
    this.des3ArchiveFileId = des3ArchiveFileId;
  }

  @Transient
  public String getFileSizeStr() {
    return fileSizeStr;
  }

  public void setFileSizeStr(String fileSizeStr) {
    this.fileSizeStr = fileSizeStr;
  }

  @Transient
  public String getPsnAvatars() {
    return psnAvatars;
  }

  public void setPsnAvatars(String psnAvatars) {
    this.psnAvatars = psnAvatars;
  }

  @Transient
  public String getTitolo() {
    return titolo;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  @Transient
  public Long getRecommendPsnId() {
    return recommendPsnId;
  }

  public void setRecommendPsnId(Long recommendPsnId) {
    this.recommendPsnId = recommendPsnId;
  }

  @Transient
  public String getRecommendPsnName() {
    return recommendPsnName;
  }

  public void setRecommendPsnName(String recommendPsnName) {
    this.recommendPsnName = recommendPsnName;
  }

  @Transient
  public String getFileViewType() {
    if (StringUtils.isNotBlank(fileName)) {
      fileViewType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
    return fileViewType;
  }

  public void setFileViewType(String fileViewType) {
    this.fileViewType = fileViewType;
  }

  @Transient
  public String getFileViewName() {
    if (StringUtils.isNotBlank(fileName)) {
      fileViewName = fileName.substring(0, fileName.lastIndexOf("."));
    }
    return fileViewName;
  }

  public void setFileViewName(String fileViewName) {
    this.fileViewName = fileViewName;
  }

  @Transient
  public Long getResRecId() {
    return resRecId;
  }

  public void setResRecId(Long resRecId) {
    this.resRecId = resRecId;
  }

  @Transient
  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }
}
