package com.smate.web.group.action.grp.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.group.model.grp.file.GrpFile;

/**
 * 群组文件表单
 * 
 * @author AiJiangBin
 */
public class GrpFileForm {

  private String module; // 模块 curware=课件 ， work == 作业
  private String des3GrpId; // 加密的群组id
  private Long grpId; // 群组id
  private String des3PsnId; // 加密的人员id
  private Page<GrpFile> page = new Page<GrpFile>();
  private List<GrpFileShowInfo> GrpFileShowInfos = new ArrayList<GrpFileShowInfo>();
  private Long psnId; // 人员id
  private String searchKey; // 查询的关键词
  private List<Integer> searchFileType = new ArrayList<Integer>(); // 查询的文件类型1:作业;2:教学课件
  private Long searchGrpFileMemberId; // 查询群组的人员文件的id
  private String searchDes3GrpFileMemberId; // 查询群组的人员文件的id

  private String des3StationFileId;// 个人文件加密id
  private Long stationFileId; // 个人文件id
  private String des3GrpFileId; // 群组文件加密id
  private String des3GrpFileIdList; // 群组文件加密id列表
  private Integer batchCount = 0; // 处理的数量
  private Long grpFileId; // 群组文件id
  private String grpFileDesc; // 群组文件描述
  private Integer isNeedSendGroupEmail = 0;// 是否需要发送群组邮件通知，默认不需要，如果用户选址了需要发送的复选框，则该值为1

  private List<StationFile> stationFileList; // /群组文件个人文件
  private List<GrpFile> grpFileList; // 群组文件列表 +
  private Map<Long, String> psnIdNameMap; // 人员的id 和名字对应的map
  private List<GrpFileMember> grpFileMemberList; // 组员文件信息列表
  private String searchGrpFileMemberName; // 查询文件成员
  private Integer grpCategory; // 群组类别，课程，项目， 兴趣
  private Integer grpFileType; // [0: 群组文件;1: 作业;2: 教学课件]
  private Long fileSize;
  private String fileType; // 文件类型 例如： imge
  private String filePath; // 文件路径
  private Integer workFileType; // 1: 作业
  private Integer courseFileType;// 2: 教学课件
  private Integer grpRole; // 群组角色 [1=拥有者,2=管理员, 3=组员]

  private Long archiveFileId;// 文件id
  private String des3ArchiveFileid; // 加密的文件id
  private String stationFileIdStr; // 文件id字符串
  private List<Long> stationFileIdList;
  private List<Long> grpFileIdList; // 群组文件id
  private List<String> delDes3FileIds; // 删除文件的id

  private String des3ReceiverIds; // 分享时，接受者id集合逗号隔离
  private String fileNames; // 文件名字 多个就用逗号分隔
  private String textContent; // 发送消息的内容
  private String ReceiverEmails; // 接收者的邮件 ，逗号分隔
  private Long grpLabelId; // 群组标签id
  private String des3GrpLabelId; // 群组标签id
  private String des3GrpLabelIds; // 群组标签ids集合，逗号分开
  private List<Long> grpLabelIdList; // 群组标签ids
  private String grpLabelIds;//
  private String resType; // 资源类型 pub/file
  private String tempType; // 动态模板 PUBLISHDYN 发布动态 /ADDRES 添加成果和文件 /SHARE
  private boolean shortFileUrl = false;// 是否获取群组文件的短地址

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getDes3GrpId() {
    if (StringUtils.isBlank(des3GrpId) && grpId != null && StringUtils.isNotBlank(grpId.toString())) {
      des3GrpId = Des3Utils.encodeToDes3(grpId.toString());
    }
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public Long getGrpId() {
    if ((grpId == null || grpId == 0L) && StringUtils.isNotBlank(des3GrpId)) {
      grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpId));
      // Long.getLong("");
    }
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getDes3PsnId() {

    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getDes3StationFileId() {
    return des3StationFileId;
  }

  public void setDes3StationFileId(String des3StationFileId) {
    this.des3StationFileId = des3StationFileId;
  }

  public Long getStationFileId() {
    if (stationFileId == null && StringUtils.isNotBlank(des3StationFileId)) {
      stationFileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3StationFileId));
    }
    return stationFileId;
  }

  public void setStationFileId(Long stationFileId) {
    this.stationFileId = stationFileId;
  }

  public String getDes3GrpFileId() {
    return des3GrpFileId;
  }

  public void setDes3GrpFileId(String des3GrpFileId) {
    this.des3GrpFileId = des3GrpFileId;
  }

  public Long getGrpFileId() {
    if ((grpFileId == null || grpFileId == 0L) && StringUtils.isNotBlank(des3GrpFileId)) {
      grpFileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpFileId));
    }
    return grpFileId;
  }

  public void setGrpFileId(Long grpFileId) {
    this.grpFileId = grpFileId;
  }

  public List<StationFile> getStationFileList() {
    return stationFileList;
  }

  public void setStationFileList(List<StationFile> stationFileList) {
    this.stationFileList = stationFileList;
  }

  public List<GrpFile> getGrpFileList() {
    return grpFileList;
  }

  public void setGrpFileList(List<GrpFile> grpFileList) {
    this.grpFileList = grpFileList;
  }

  public List<GrpFileMember> getGrpFileMemberList() {
    return grpFileMemberList;
  }

  public void setGrpFileMemberList(List<GrpFileMember> grpFileMemberList) {
    this.grpFileMemberList = grpFileMemberList;
  }

  public Page<GrpFile> getPage() {
    return page;
  }

  public void setPage(Page<GrpFile> page) {
    this.page = page;
  }

  public List<GrpFileShowInfo> getGrpFileShowInfos() {
    return GrpFileShowInfos;
  }

  public void setGrpFileShowInfos(List<GrpFileShowInfo> grpFileShowInfos) {
    GrpFileShowInfos = grpFileShowInfos;
  }

  public Integer getGrpCategory() {
    return grpCategory;
  }

  public void setGrpCategory(Integer grpCategory) {
    this.grpCategory = grpCategory;
  }

  public Integer getGrpFileType() {
    if (grpFileType == null) {
      grpFileType = 0;// 默认为群组文件，
    }
    return grpFileType;
  }

  public void setGrpFileType(Integer grpFileType) {
    this.grpFileType = grpFileType;
  }

  public Integer getGrpRole() {
    return grpRole;
  }

  public void setGrpRole(Integer grpRole) {
    this.grpRole = grpRole;
  }

  public List<Integer> getSearchFileType() {
    if (searchFileType != null && searchFileType.size() > 0) {
      return searchFileType;
    }
    if (workFileType != null && courseFileType == null) {
      searchFileType.add(1);
    } else if (workFileType == null && courseFileType != null) {
      searchFileType.add(2);
    } else if (workFileType != null && courseFileType != null) {
      searchFileType.add(1);
      searchFileType.add(2);
    } else {
      searchFileType.add(0); // 默认查询文件
    }
    return searchFileType;
  }

  public void setSearchFileType(List<Integer> searchFileType) {
    this.searchFileType = searchFileType;
  }

  public Long getSearchGrpFileMemberId() {
    if (searchGrpFileMemberId == null && StringUtils.isNotBlank(searchDes3GrpFileMemberId)) {
      searchGrpFileMemberId = NumberUtils.toLong(Des3Utils.decodeFromDes3(searchDes3GrpFileMemberId));
    }
    return searchGrpFileMemberId;
  }

  public void setSearchGrpFileMemberId(Long searchGrpFileMemberId) {
    this.searchGrpFileMemberId = searchGrpFileMemberId;
  }

  public Long getArchiveFileId() {
    if ((archiveFileId == null || archiveFileId == 0L) && StringUtils.isNotBlank(des3ArchiveFileid)) {
      archiveFileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3ArchiveFileid));
    }
    return archiveFileId;
  }

  public void setArchiveFileId(Long archiveFileId) {
    this.archiveFileId = archiveFileId;
  }

  public String getDes3ArchiveFileid() {
    return des3ArchiveFileid;
  }

  public void setDes3ArchiveFileid(String des3ArchiveFileid) {
    this.des3ArchiveFileid = des3ArchiveFileid;
  }

  public String getGrpFileDesc() {
    return grpFileDesc;
  }

  public void setGrpFileDesc(String grpFileDesc) {
    this.grpFileDesc = grpFileDesc;
  }

  public Map<Long, String> getPsnIdNameMap() {
    return psnIdNameMap;
  }

  public void setPsnIdNameMap(Map<Long, String> psnIdNameMap) {
    this.psnIdNameMap = psnIdNameMap;
  }

  public String getSearchGrpFileMemberName() {
    return searchGrpFileMemberName;
  }

  public void setSearchGrpFileMemberName(String searchGrpFileMemberName) {
    this.searchGrpFileMemberName = searchGrpFileMemberName;
  }

  public String getSearchDes3GrpFileMemberId() {
    return searchDes3GrpFileMemberId;
  }

  public void setSearchDes3GrpFileMemberId(String searchDes3GrpFileMemberId) {
    this.searchDes3GrpFileMemberId = searchDes3GrpFileMemberId;
  }

  public String getStationFileIdStr() {
    return stationFileIdStr;
  }

  public void setStationFileIdStr(String stationFileIdStr) {
    this.stationFileIdStr = stationFileIdStr;
  }

  public List<Long> getStationFileIdList() {
    if (stationFileIdList == null && StringUtils.isNotBlank(stationFileIdStr)) {
      stationFileIdList = new ArrayList<Long>();
      String[] idStr = stationFileIdStr.split(",");
      for (int i = 0; i < idStr.length && StringUtils.isNoneBlank(idStr[i]); i++) {
        stationFileIdList.add(NumberUtils.toLong(idStr[i]));
      }
    }
    return stationFileIdList;
  }

  public void setStationFileIdList(List<Long> stationFileIdList) {
    this.stationFileIdList = stationFileIdList;
  }

  public Integer getWorkFileType() {
    return workFileType;
  }

  public void setWorkFileType(Integer workFileType) {
    this.workFileType = workFileType;
  }

  public Integer getCourseFileType() {
    return courseFileType;
  }

  public void setCourseFileType(Integer courseFileType) {
    this.courseFileType = courseFileType;
  }

  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public List<Long> getGrpFileIdList() {
    if (this.grpFileIdList != null && this.grpFileIdList.size() > 0) {
      return this.grpFileIdList;
    }
    List<Long> temp = new ArrayList<Long>();
    if (StringUtils.isNotBlank(this.des3GrpFileId)) { // 单个文件
      temp.add(NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3GrpFileId)));
    } else if (StringUtils.isNotBlank(this.getDes3GrpFileIdList())) { // 批量处理
      String[] grpFileIdArr = this.getDes3GrpFileIdList().split(",");
      if (grpFileIdArr != null && grpFileIdArr.length > 0) {
        for (String string : grpFileIdArr) {
          temp.add(NumberUtils.toLong(Des3Utils.decodeFromDes3(string)));
        }
      }

    }
    this.grpFileIdList = temp;
    return this.grpFileIdList;
  }

  public void setGrpFileIdList(List<Long> grpFileIdList) {
    this.grpFileIdList = grpFileIdList;
  }

  public String getDes3GrpFileIdList() {
    return des3GrpFileIdList;
  }

  public void setDes3GrpFileIdList(String des3GrpFileIdList) {
    this.des3GrpFileIdList = des3GrpFileIdList;
  }

  public Integer getBatchCount() {
    return batchCount;
  }

  public void setBatchCount(Integer batchCount) {
    this.batchCount = batchCount;
  }

  public List<String> getDelDes3FileIds() {
    return delDes3FileIds;
  }

  public void setDelDes3FileIds(List<String> delDes3FileIds) {
    this.delDes3FileIds = delDes3FileIds;
  }

  public String getDes3ReceiverIds() {
    return des3ReceiverIds;
  }

  public void setDes3ReceiverIds(String des3ReceiverIds) {
    this.des3ReceiverIds = des3ReceiverIds;
  }

  public String getFileNames() {
    return fileNames;
  }

  public void setFileNames(String fileNames) {
    this.fileNames = fileNames;
  }

  public String getTextContent() {
    return textContent;
  }

  public void setTextContent(String textContent) {
    this.textContent = textContent;
  }

  public String getReceiverEmails() {
    return ReceiverEmails;
  }

  public void setReceiverEmails(String receiverEmails) {
    ReceiverEmails = receiverEmails;
  }

  public Integer getIsNeedSendGroupEmail() {
    return isNeedSendGroupEmail;
  }

  public void setIsNeedSendGroupEmail(Integer isNeedSendGroupEmail) {
    this.isNeedSendGroupEmail = isNeedSendGroupEmail;
  }

  public Long getGrpLabelId() {
    if (this.grpLabelId == null && StringUtils.isNotBlank(this.des3GrpLabelId)) {
      this.grpLabelId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpLabelId), 0L);
    }
    return grpLabelId;
  }

  public void setGrpLabelId(Long grpLabelId) {
    this.grpLabelId = grpLabelId;
  }

  public String getDes3GrpLabelId() {
    return des3GrpLabelId;
  }

  public void setDes3GrpLabelId(String des3GrpLabelId) {
    this.des3GrpLabelId = des3GrpLabelId;
  }

  public String getDes3GrpLabelIds() {
    return des3GrpLabelIds;
  }

  public void setDes3GrpLabelIds(String des3GrpLabelIds) {
    this.des3GrpLabelIds = des3GrpLabelIds;
  }

  public List<Long> getGrpLabelIdList() {
    if (grpLabelIdList == null && StringUtils.isNotBlank(des3GrpLabelIds)) {
      grpLabelIdList = new ArrayList<>();
      String[] split = des3GrpLabelIds.split(",");
      for (String str : split) {
        if (StringUtils.isBlank(str)) {
          continue;
        }
        long grpLabelId = NumberUtils.toLong(Des3Utils.decodeFromDes3(str), 0L);
        grpLabelIdList.add(grpLabelId);
      }
    }
    return grpLabelIdList;
  }

  public void setGrpLabelIdList(List<Long> grpLabelIdList) {
    this.grpLabelIdList = grpLabelIdList;
  }

  public String getGrpLabelIds() {
    if (StringUtils.isBlank(grpLabelIds) && getGrpLabelIdList() != null && getGrpLabelIdList().size() > 0) {
      grpLabelIds = "";
      for (Long grpLabelId : grpLabelIdList) {
        grpLabelIds = grpLabelIds + grpLabelId + ",";
      }
      grpLabelIds = grpLabelIds.substring(0, grpLabelIds.length() - 1);
    }

    return grpLabelIds;
  }

  public void setGrpLabelIds(String grpLabelIds) {
    this.grpLabelIds = grpLabelIds;
  }

  public String getResType() {
    return resType;
  }

  public void setResType(String resType) {
    this.resType = resType;
  }

  public String getTempType() {
    return tempType;
  }

  public void setTempType(String tempType) {
    this.tempType = tempType;
  }

  public boolean getShortFileUrl() {
    return shortFileUrl;
  }

  public void setShortFileUrl(boolean shortFileUrl) {
    this.shortFileUrl = shortFileUrl;
  }


}
