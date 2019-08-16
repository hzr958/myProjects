package com.smate.web.group.service.group;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.dao.group.GroupBaseinfoDao;
import com.smate.web.group.dao.group.GroupFileDao;
import com.smate.web.group.dao.group.GroupStatisticsDao;
import com.smate.web.group.dao.group.invit.GroupInvitePsnDao;
import com.smate.web.group.dao.group.psn.GroupInvitePsnNodeDao;
import com.smate.web.group.dao.group.psn.GroupPsnNodeDao;
import com.smate.web.group.exception.GroupException;
import com.smate.web.group.form.GroupOptForm;
import com.smate.web.group.model.group.GroupBaseInfo;
import com.smate.web.group.model.group.GroupFile;
import com.smate.web.group.model.group.GroupStatistics;
import com.smate.web.group.model.group.invit.GroupInvitePsn;
import com.smate.web.group.model.group.psn.GroupInvitePsnNode;
import com.smate.web.group.service.group.psn.PsnInfoService;

/**
 * 群组操作服务类
 * 
 * @author zk
 *
 */
@Service("groupOptService")
@Transactional(rollbackOn = Exception.class)
public class GroupOptServiceImpl implements GroupOptService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupFileDao groupFileDao;
  @Autowired
  private PsnInfoService psnInfoService;
  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;
  @Autowired
  private GroupInvitePsnNodeDao groupInvitePsnNodeDao;
  @Autowired
  private GroupStatisticsDao groupStatisticsDao;
  @Autowired
  private GroupPsnNodeDao groupPsnNodeDao;
  @Autowired
  private GroupBaseinfoDao groupBaseinfoDao;


  @Override
  public Boolean editGroupFile(GroupOptForm form) throws GroupException {
    if (form.getGroupFileId() == null || form.getGroupFileId() <= 0) {
      return false;
    }
    if (form.getFileDesc() == null) {
      form.setFileDesc("");
    }
    groupFileDao.updateGroupFile(form.getFileDesc(), form.getGroupFileId(), form.getPsnId());
    return true;
  }

  @Override
  public Boolean addGroupFile(GroupOptForm form) throws GroupException {
    if (form.getStataionFileId() == null) {
      logger.error("添加群组文件未获取到stationFileId值");
      return false;
    }
    if (form.getGroupId() == null || form.getGroupId() < 0) {
      logger.error("添加群组文件未获取到groupId值");
      return false;
    }
    StationFile stationFile = groupFileDao.findStationFileBysnsFileId(form.getStataionFileId());
    if (stationFile == null) {
      return false;
    }
    if (groupFileDao.checkHasSave(form.getStataionFileId(), form.getGroupId(), form.getPsnId())) {
      return false;
    }
    this.handleAndSaveGroupFile(stationFile, form);
    // 统计群组资源数量
    updateGroupFileCount(form);



    return true;
  }

  // 统计群组资源数量
  private void updateGroupFileCount(GroupOptForm form) {
    GroupStatistics groupStatistics = groupStatisticsDao.getStatistics(form.getGroupId());
    if (groupStatistics == null) {
      groupStatistics = new GroupStatistics();
      groupStatistics.setGroupId(form.getGroupId());
    }
    groupStatistics.setSumFiles(groupStatistics.getSumFiles() + 1);
    groupStatistics.setSumFilesNfolder(groupStatistics.getSumFilesNfolder() + 1);
    groupStatisticsDao.save(groupStatistics);
    List<GroupInvitePsnNode> groupInvitePsnNodeList =
        groupInvitePsnNodeDao.findGroupInvitePsnNodeList(form.getGroupId());
    if (groupInvitePsnNodeList != null && groupInvitePsnNodeList.size() > 0) {
      for (GroupInvitePsnNode groupInvitePsnNode : groupInvitePsnNodeList) {
        groupInvitePsnNode.setSumFiles(groupStatistics.getSumFiles());
        groupInvitePsnNodeDao.save(groupInvitePsnNode);
      }
    }
  }

  @Override
  public Boolean deleteGroupFile(GroupOptForm form) throws GroupException {
    if (form.getGroupFileId() == null || form.getGroupFileId() <= 0) {
      logger.error("删除群组文件未获取到GroupFileId值");
    }
    if (form.getGroupId() == null || form.getGroupId() <= 0) {
      logger.error("删除群组文件未获取到groupId值");
    }
    if (form.getPsnId() == null || form.getPsnId() <= 0) {
      logger.error("删除群组文件未获取到PsnId值");
    }
    if (groupFileDao.checkGroupFileIsMe(form.getGroupFileId(), form.getGroupId(), form.getPsnId())
        || groupInvitePsnDao.isGroupAdmin(form.getGroupId(), form.getPsnId())) {
      int result = groupFileDao.deleteGroupFile(form.getGroupFileId(), form.getGroupId());
      return result == 1 ? true : false;
    } else {
      form.setFlag(true);
      return false;
    }
  }

  /**
   * 封装并保存GroupFile
   * 
   * @param sf
   * @param form
   * @throws GroupException
   */
  private void handleAndSaveGroupFile(StationFile sf, GroupOptForm form) throws GroupException {
    GroupFile gf = new GroupFile();
    gf.setFileId(sf.getFileId());
    gf.setGroupId(form.getGroupId());
    gf.setPsnId(sf.getPsnId());
    gf.setFileName(sf.getFileName());
    gf.setFilePath(sf.getFilePath());
    gf.setFileType(sf.getFileType());
    gf.setFileSize(sf.getFileSize());
    gf.setFileDesc(sf.getFileDesc());
    gf.setArchiveFileId(sf.getArchiveFileId());
    gf.setFileStatus(0);
    gf.setGroupFolderIds("-1");
    gf.setNodeId(1);
    gf.setUploadTime(new Date());
    gf.setFileModuleType(0);
    gf.setAuthorName(psnInfoService.getPsnName(sf.getPsnId(), null));
    groupFileDao.save(gf);
  }

  /**
   * 删除群组成员
   */
  @Override
  public Boolean deleteGroupMember(GroupOptForm form) throws GroupException {
    if (form.getMemberId() == null || form.getMemberId() <= 0) {
      logger.error("删除群组成员未获取到memberId值");
    }
    if (form.getPsnId() == null || form.getPsnId() <= 0) {
      logger.error("删除群组成员未获取到PsnId值");
    }
    if (form.getMemberId().equals(form.getPsnId())) {
      return false;
    }
    Boolean flag = groupInvitePsnDao.isGroupAdmin(form.getGroupId(), form.getPsnId());
    if (flag) {
      groupInvitePsnDao.deleteMember(form.getGroupId(), form.getMemberId());
      groupStatisticsDao.getSumMembersByGroupId(form.getGroupId());
      groupInvitePsnNodeDao.deleteMember(form.getGroupId(), form.getMemberId());
      GroupStatistics groupStatistics = groupStatisticsDao.getStatistics(form.getGroupId());
      groupPsnNodeDao.updateSumMembers(groupStatistics.getSumMembers(), form.getGroupId());
      return true;
    }
    return false;

  }

  @Override
  public Boolean isMyGroupFile(GroupOptForm form) throws GroupException {
    return groupFileDao.checkGroupFileIsMe(form.getGroupFileId(), form.getGroupId(), form.getPsnId());
  }

  /**
   * 获取人与群组的关系
   * 
   * 
   * 群组角色状态 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ， 2 是群组普通成员 3 群组管理成员 4.群主
   */

  @Override
  public Integer getRelationWithGroup(Long psnId, Long groupId) throws GroupException {
    Integer oaInteger = new Integer(0);
    GroupInvitePsn groupInvitePsn = groupInvitePsnDao.getGroupInvitePsn(groupId, psnId);
    if (groupInvitePsn == null) {
      oaInteger = 0;
    } else if ("1".equals(groupInvitePsn.getIsAccept())) {
      if ("1".equals(groupInvitePsn.getGroupRole())) {
        oaInteger = 4;
      } else if ("2".equals(groupInvitePsn.getGroupRole())) {
        oaInteger = 3;
      } else {
        oaInteger = 2;
      }
    } else if (StringUtils.isBlank(groupInvitePsn.getIsAccept())) {
      oaInteger = 1;
    } else {
      oaInteger = 0;
    }
    return oaInteger;
  }

  @Override
  public List<GroupBaseInfo> getGroupNames(GroupOptForm form) throws GroupException {
    HttpServletRequest request = Struts2Utils.getRequest();
    List<GroupBaseInfo> groupBaseInfoList = null;
    String maxResults = request.getParameter("maxResults");
    Integer size = null;
    if (StringUtils.isNoneBlank(request.getParameter("maxResults"))) {
      size = Integer.parseInt(maxResults);
    }
    List<Long> myGroupIds = getMyGroupIds(form);
    if (myGroupIds != null) {
      groupBaseInfoList = groupBaseinfoDao.getGroupNames(myGroupIds, request.getParameter("q"), size);
    }
    return groupBaseInfoList;
  }

  /**
   * 获取我的未删除的群组id列表
   * 
   * @param form
   * @return
   */
  private List<Long> getMyGroupIds(GroupOptForm form) {
    List<Long> groupIdList = groupInvitePsnDao.getMyGroupIdList(form.getPsnId());
    if (groupIdList != null && groupIdList.size() > 0) {
      return groupIdList;
    } else {
      return null;
    }

  }
}
