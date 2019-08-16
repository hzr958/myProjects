package com.smate.web.group.service.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.group.dao.group.GroupBaseinfoDao;
import com.smate.web.group.dao.group.GroupControlDao;
import com.smate.web.group.dao.group.GroupFilterDao;
import com.smate.web.group.dao.group.GroupKeyDiscDao;
import com.smate.web.group.dao.group.GroupKeywordsDao;
import com.smate.web.group.dao.group.GroupStatisticsDao;
import com.smate.web.group.model.group.GroupBaseInfo;
import com.smate.web.group.model.group.GroupControl;
import com.smate.web.group.model.group.GroupFilter;
import com.smate.web.group.model.group.GroupKeyDisc;
import com.smate.web.group.model.group.GroupPsn;
import com.smate.web.group.model.group.GroupStatistics;

/**
 * 群组检索服务
 * 
 * @author lhd
 *
 */
@Service("groupPsnSearchService")
@Transactional(rollbackFor = Exception.class)
public class GroupPsnSearchServiceImpl implements GroupPsnSearchService {
  @Autowired
  private GroupBaseinfoDao groupBaseInfoDao;
  @Autowired
  private GroupFilterDao groupFilterDao;
  @Autowired
  private GroupStatisticsDao groupStatisticsDao;
  @Autowired
  private GroupControlDao groupControlDao;
  @Autowired
  private GroupKeyDiscDao groupKeyDiscDao;
  @Autowired
  private GroupKeywordsDao groupKeywordsDao;

  /**
   * 获取群组基本信息记录.
   * 
   * @author zjh
   * @param groupId
   * @return
   */
  public GroupBaseInfo getBaseInfo(Long groupId) {
    return groupBaseInfoDao.getGroupBaseInfo(groupId);
  }

  @Override
  public GroupPsn getBuildGroupPsn(Long groupId) {
    GroupBaseInfo baseInfo = this.getBaseInfo(groupId);
    // 如果群组基本信息为空，则直接返回，不构建其他参数对象.
    if (baseInfo != null) {
      GroupPsn groupPsn = new GroupPsn();
      groupPsn = this.rebuildGroupBaseInfo(groupPsn, baseInfo);
      groupPsn.setDes3GroupId(ServiceUtil.encodeToDes3(groupId.toString()));

      // 群组检索过滤基本信息.
      GroupFilter groupFilter = this.getFileter(groupId);
      if (groupFilter != null) {
        groupPsn = this.rebuildGroupFilter(groupPsn, groupFilter);
      }
      // 群组控制信息.
      GroupControl groupControl = this.getControl(groupId);
      if (groupControl != null) {
        groupPsn = this.rebuildGroupControl(groupPsn, groupControl);
      }
      // 群组统计信息.
      GroupStatistics statistics = this.getStatistics(groupId);
      if (statistics != null) {
        groupPsn = this.rebuildGroupStatistics(groupPsn, statistics);
      }
      // 群组学科关键词信息.
      GroupKeyDisc keyDisc = this.getKeyDisc(groupId);
      if (keyDisc != null) {
        groupPsn = this.rebuildGroupKeyDisc(groupPsn, keyDisc);
      }

      return groupPsn;
    }
    return null;
  }

  private GroupPsn rebuildGroupKeyDisc(GroupPsn groupPsn, GroupKeyDisc keyDisc) {
    groupPsn.setDiscipline1(keyDisc.getCategoryId());
    groupPsn.setDisciplines(keyDisc.getDisciplines());
    groupPsn.setDiscCodes(keyDisc.getDiscCodes());
    groupPsn.setEnKeyWords(keyDisc.getEnKeyWords());
    groupPsn.setEnKeyWords1(keyDisc.getEnKeyWords1());
    /* groupPsn.setEnKeyWordsList(keyDisc.getEnKeyWordsList()); */
    groupPsn.setGroupId(keyDisc.getGroupId());
    groupPsn.setKeyWords(keyDisc.getKeyWords());
    groupPsn.setKeyWords1(keyDisc.getKeyWords1());
    /* groupPsn.setKewWordsList(keyDisc.getKewWordsList()); */
    return groupPsn;
  }

  /**
   * 获取群组学科关键词信息.
   * 
   * @param groupId
   * @return
   */
  @Override
  public GroupKeyDisc getKeyDisc(Long groupId) {
    return groupKeyDiscDao.getGroupKeyDisc(groupId);
  }

  private GroupPsn rebuildGroupBaseInfo(GroupPsn groupPsn, GroupBaseInfo baseInfo) {
    groupPsn.setGroupId(baseInfo.getGroupId());
    groupPsn.setGroupAnnouncement(baseInfo.getGroupAnnouncement());
    groupPsn.setGroupCategory(baseInfo.getGroupCategory());
    groupPsn.setGroupDescription(baseInfo.getGroupDescription());
    groupPsn.setGroupImgUrl(baseInfo.getGroupImgUrl());
    groupPsn.setGroupName(baseInfo.getGroupName());
    groupPsn.setGroupNo(baseInfo.getGroupNo());
    groupPsn.setGroupPageUrl(baseInfo.getGroupPageUrl());
    groupPsn.setAddress(baseInfo.getAddress());
    groupPsn.setCreateDate(baseInfo.getCreateDate());
    groupPsn.setEmail(baseInfo.getEmail());
    groupPsn.setFileId(baseInfo.getFileId());
    groupPsn.setFundingTypes(baseInfo.getFundingTypes());
    groupPsn.setLastVisitDate(baseInfo.getLastVisitDate());
    groupPsn.setTel(baseInfo.getTel());
    groupPsn.setUpdateDate(baseInfo.getUpdateDate());
    return groupPsn;
  }

  /**
   * 获取群组检索过滤信息.
   * 
   * @param groupId
   * @return
   */
  @Override
  public GroupFilter getFileter(Long groupId) {
    return groupFilterDao.getGroupFilter(groupId);
  }

  private GroupPsn rebuildGroupFilter(GroupPsn groupPsn, GroupFilter filter) {
    groupPsn.setGroupCode(filter.getGroupCode());
    groupPsn.setGroupId(filter.getGroupId());
    groupPsn.setOpenType(filter.getOpenType());
    groupPsn.setOwnerPsnId(filter.getOwnerPsnId());
    groupPsn.setStatus(filter.getStatus());
    return groupPsn;
  }

  /**
   * 获取群组控制信息.
   * 
   * @param groupId
   * @return
   */
  @Override
  public GroupControl getControl(Long groupId) {
    return groupControlDao.getGroupControl(groupId);
  }

  private GroupPsn rebuildGroupControl(GroupPsn groupPsn, GroupControl groupControl) {
    groupPsn.setGroupId(groupControl.getGroupId());
    groupPsn.setIsDiscuss(groupControl.getIsDiscuss());
    groupPsn.setIsGroupMemberView(groupControl.getIsGroupMemberView());
    groupPsn.setIsIsisPrj(groupControl.getIsIsisPrj());
    groupPsn.setIsMaterialView(groupControl.getIsMaterialView());
    groupPsn.setIsMemberPublish(groupControl.getIsMemberPublish());
    groupPsn.setIsPageDescOpen(groupControl.getIsPageDescOpen());
    groupPsn.setIsPageFileOpen(groupControl.getIsPageFileOpen());
    groupPsn.setIsPageMaterialOpen(groupControl.getIsPageMaterialOpen());
    groupPsn.setIsPageMemberOpen(groupControl.getIsPageMemberOpen());
    groupPsn.setIsPageOpen(groupControl.getIsPageOpen());
    groupPsn.setIsPagePrjOpen(groupControl.getIsPagePrjOpen());
    groupPsn.setIsPagePubOpen(groupControl.getIsPagePubOpen());
    groupPsn.setIsPageRefOpen(groupControl.getIsPageRefOpen());
    groupPsn.setIsPageWorkOpen(groupControl.getIsPageWorkOpen());
    groupPsn.setIsPrjView(groupControl.getIsPrjView());
    groupPsn.setIsPubView(groupControl.getIsPubView());
    groupPsn.setIsRefView(groupControl.getIsRefView());
    /* groupPsn.setIsSave(groupControl.getIsSave()); */
    groupPsn.setIsShareFile(groupControl.getIsShareFile());
    groupPsn.setIsWorkView(groupControl.getIsWorkView());
    groupPsn.setPrjViewType(groupControl.getPrjViewType());
    groupPsn.setPubViewType(groupControl.getPubViewType());
    groupPsn.setRefViewType(groupControl.getRefViewType());
    groupPsn.setShareFileType(groupControl.getShareFileType());
    return groupPsn;
  }

  /**
   * 获取群组统计记录.
   * 
   * @param groupId
   * @return
   */
  public GroupStatistics getStatistics(Long groupId) {
    return groupStatisticsDao.getStatistics(groupId);
  }

  private GroupPsn rebuildGroupStatistics(GroupPsn groupPsn, GroupStatistics statistics) {
    groupPsn.setGroupId(statistics.getGroupId());
    groupPsn.setSumFiles(statistics.getSumFiles());
    groupPsn.setSumFilesNfolder(statistics.getSumFilesNfolder());
    groupPsn.setSumMaterials(statistics.getSumMaterials());
    groupPsn.setSumMaterialsNfolder(statistics.getSumMaterialsNfolder());
    groupPsn.setSumMembers(statistics.getSumMembers());
    groupPsn.setSumPrjs(statistics.getSumPrjs());
    groupPsn.setSumPrjsNfolder(statistics.getSumPrjsNfolder());
    groupPsn.setSumPubs(statistics.getSumPubs());
    groupPsn.setSumPubsNfolder(statistics.getSumPubsNfolder());
    groupPsn.setSumRefs(statistics.getSumRefs());
    groupPsn.setSumRefsNfolder(statistics.getSumRefsNfolder());
    groupPsn.setSumSubjects(statistics.getSumSubjects());
    groupPsn.setSumToMembers(statistics.getSumToMembers());
    groupPsn.setSumWorks(statistics.getSumWorks());
    groupPsn.setSumWorksNfolder(statistics.getSumWorksNfolder());
    groupPsn.setVisitCount(statistics.getVisitCount());
    return groupPsn;
  }

}
