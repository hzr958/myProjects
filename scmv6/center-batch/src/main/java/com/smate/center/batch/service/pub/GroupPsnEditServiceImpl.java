package com.smate.center.batch.service.pub;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.GroupBaseinfoDao;
import com.smate.center.batch.dao.sns.pub.GroupControlDao;
import com.smate.center.batch.dao.sns.pub.GroupFilterDao;
import com.smate.center.batch.dao.sns.pub.GroupInvitePsnNodeDao;
import com.smate.center.batch.dao.sns.pub.GroupKeyDiscDao;
import com.smate.center.batch.dao.sns.pub.GroupStatisticsDao;
import com.smate.center.batch.model.sns.pub.GroupBaseInfo;
import com.smate.center.batch.model.sns.pub.GroupControl;
import com.smate.center.batch.model.sns.pub.GroupFilter;
import com.smate.center.batch.model.sns.pub.GroupKeyDisc;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.GroupStatistics;

/**
 * 群组编辑服务(原group_psn表拆分为多个表，为减少对其他模块的影响，增加此服务以便数据封装)_SCM-6000.
 * 
 * @author mjg
 * @since 2014-11-28
 */
@Service(value = "groupPsnEditService")
@Transactional(rollbackFor = Exception.class)
public class GroupPsnEditServiceImpl implements GroupPsnEditService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupInvitePsnNodeDao groupInvitePsnNodeDao;
  @Autowired
  private GroupBaseinfoDao groupBaseInfoDao;
  @Autowired
  private GroupControlDao groupControlDao;
  @Autowired
  private GroupFilterDao groupFilterDao;
  @Autowired
  private GroupKeyDiscDao groupKeyDiscDao;
  @Autowired
  private GroupStatisticsDao groupStatisticsDao;

  /**
   * 保存群组记录.
   * 
   * @param groupPsn
   */
  @Override
  public void saveGroupPsn(GroupPsn groupPsn) {

    if (groupPsn != null) {
      // 保存群组基本信息.
      GroupBaseInfo baseInfo = this.buildBaseInfo(groupPsn);
      if (baseInfo != null) {
        this.saveBaseInfo(baseInfo);
      }
      // 保存群组控制信息.
      GroupControl groupControl = this.buildControl(groupPsn);
      if (groupControl != null) {
        this.saveControl(groupControl);
      }
      // 保存群组检索过滤信息.
      GroupFilter groupFilter = this.buildFilter(groupPsn);
      if (groupFilter != null) {
        this.saveFilter(groupFilter);
      }
      // 保存群组检索过滤信息.
      GroupKeyDisc keyDisc = this.buildKeyDisc(groupPsn);
      if (keyDisc != null) {
        this.saveKeyDisc(keyDisc);
      }
      // 保存群组统计信息.
      GroupStatistics statistics = this.buildStatistics(groupPsn);
      if (statistics != null) {
        this.saveStatistics(statistics);
      }
    }
  }

  @Override
  public void saveBaseInfo(GroupBaseInfo groupBaseInfo) {
    if (groupBaseInfo != null) {
      groupBaseInfoDao.saveBaseInfo(groupBaseInfo);
    }
  }

  /**
   * 保存群组基本信息(包含group_filter表信息).
   * 
   * @param groupPsn
   */
  @Override
  public void saveGroupBaseInfo(GroupPsn groupPsn) {
    GroupBaseInfo baseInfo = this.buildBaseInfo(groupPsn);
    if (baseInfo != null) {
      this.saveBaseInfo(baseInfo);
    }
    GroupFilter filter = this.buildFilter(groupPsn);
    if (filter != null) {
      this.saveFilter(filter);
    }
  }

  /**
   * 转换封装群组基本信息.
   * 
   * @param groupPsn
   * @return
   */
  private GroupBaseInfo buildBaseInfo(GroupPsn groupPsn) {
    GroupBaseInfo baseInfo = groupBaseInfoDao.getGroupBaseInfo(groupPsn.getGroupId());
    boolean flag = false;
    if (baseInfo == null) {
      baseInfo = new GroupBaseInfo();
      if (groupPsn.getGroupId() != null) {
        baseInfo.setGroupId(groupPsn.getGroupId());
      }
      if (groupPsn.getGroupNo() != null) {
        baseInfo.setGroupNo(groupPsn.getGroupNo());
      }
    }
    if (StringUtils.isNotBlank(groupPsn.getGroupAnnouncement())) {
      flag = true;
      baseInfo.setGroupAnnouncement(groupPsn.getGroupAnnouncement());
    }
    if (StringUtils.isNotBlank(groupPsn.getGroupCategory())) {
      flag = true;
      baseInfo.setGroupCategory(groupPsn.getGroupCategory());
    }
    if (StringUtils.isNotBlank(groupPsn.getGroupDescription())) {
      flag = true;
      baseInfo.setGroupDescription(groupPsn.getGroupDescription());
    }
    if (StringUtils.isNotBlank(groupPsn.getGroupImgUrl())) {
      flag = true;
      baseInfo.setGroupImgUrl(groupPsn.getGroupImgUrl());
    }
    if (StringUtils.isNotBlank(groupPsn.getGroupName())) {
      flag = true;
      baseInfo.setGroupName(groupPsn.getGroupName());
    }
    if (StringUtils.isNotBlank(groupPsn.getGroupPageUrl())) {
      flag = true;
      baseInfo.setGroupPageUrl(groupPsn.getGroupPageUrl());
    }
    if (StringUtils.isNotBlank(groupPsn.getAddress())) {
      flag = true;
      baseInfo.setAddress(groupPsn.getAddress());
    }
    if (groupPsn.getCreateDate() != null) {
      flag = true;
      baseInfo.setCreateDate(groupPsn.getCreateDate());
    }
    if (StringUtils.isNotBlank(groupPsn.getEmail())) {
      flag = true;
      baseInfo.setEmail(groupPsn.getEmail());
    }
    if (groupPsn.getFileId() != null) {
      flag = true;
      baseInfo.setFileId(groupPsn.getFileId());
    }
    if (StringUtils.isNotBlank(groupPsn.getFundingTypes())) {
      flag = true;
      baseInfo.setFundingTypes(groupPsn.getFundingTypes());
    }
    if (groupPsn.getLastVisitDate() != null) {
      flag = true;
      baseInfo.setLastVisitDate(groupPsn.getLastVisitDate());
    } else {
      // 如果群组基本信息保存时未设置最后访问日期，则将其默认设置为群组创建时间.
      baseInfo.setLastVisitDate(groupPsn.getCreateDate());
    }
    if (StringUtils.isNotBlank(groupPsn.getTel())) {
      flag = true;
      baseInfo.setTel(groupPsn.getTel());
    }
    if (groupPsn.getUpdateDate() != null) {
      flag = true;
      baseInfo.setUpdateDate(groupPsn.getUpdateDate());
    }
    if (!flag) {
      return null;
    }
    return baseInfo;
  }

  @Override
  public void saveControl(GroupControl groupControl) {
    groupControlDao.saveGroupControl(groupControl);
  }

  /**
   * 转换封装群组控制信息.
   * 
   * @param groupPsn
   * @return
   */
  private GroupControl buildControl(GroupPsn groupPsn) {
    GroupControl groupControl = groupControlDao.getGroupControl(groupPsn.getGroupId());
    boolean flag = false;
    if (groupControl == null) {
      groupControl = new GroupControl();
      if (groupPsn.getGroupId() != null) {
        groupControl.setGroupId(groupPsn.getGroupId());
      }
    }
    if (StringUtils.isNotBlank(groupPsn.getIsDiscuss())) {
      flag = true;
      groupControl.setIsDiscuss(groupPsn.getIsDiscuss());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsGroupMemberView())) {
      flag = true;
      groupControl.setIsGroupMemberView(groupPsn.getIsGroupMemberView());
    }
    if (groupPsn.getIsIsisPrj() != null) {
      flag = true;
      groupControl.setIsIsisPrj(groupPsn.getIsIsisPrj());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsMaterialView())) {
      flag = true;
      groupControl.setIsMaterialView(groupPsn.getIsMaterialView());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsMemberPublish())) {
      flag = true;
      groupControl.setIsMemberPublish(groupPsn.getIsMemberPublish());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsPageDescOpen())) {
      flag = true;
      groupControl.setIsPageDescOpen(groupPsn.getIsPageDescOpen());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsPageFileOpen())) {
      flag = true;
      groupControl.setIsPageFileOpen(groupPsn.getIsPageFileOpen());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsPageMaterialOpen())) {
      flag = true;
      groupControl.setIsPageMaterialOpen(groupPsn.getIsPageMaterialOpen());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsPageMemberOpen())) {
      flag = true;
      groupControl.setIsPageMemberOpen(groupPsn.getIsPageMemberOpen());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsPageOpen())) {
      flag = true;
      groupControl.setIsPageOpen(groupPsn.getIsPageOpen());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsPagePrjOpen())) {
      flag = true;
      groupControl.setIsPagePrjOpen(groupPsn.getIsPagePrjOpen());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsPagePubOpen())) {
      flag = true;
      groupControl.setIsPagePubOpen(groupPsn.getIsPagePubOpen());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsPageRefOpen())) {
      flag = true;
      groupControl.setIsPageRefOpen(groupPsn.getIsPageRefOpen());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsPageWorkOpen())) {
      flag = true;
      groupControl.setIsPageWorkOpen(groupPsn.getIsPageWorkOpen());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsPrjView())) {
      flag = true;
      groupControl.setIsPrjView(groupPsn.getIsPrjView());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsPubView())) {
      flag = true;
      groupControl.setIsPubView(groupPsn.getIsPubView());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsRefView())) {
      flag = true;
      groupControl.setIsRefView(groupPsn.getIsRefView());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsSave())) {
      flag = true;
      groupControl.setIsSave(groupPsn.getIsSave());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsShareFile())) {
      flag = true;
      groupControl.setIsShareFile(groupPsn.getIsShareFile());
    }
    if (StringUtils.isNotBlank(groupPsn.getIsWorkView())) {
      flag = true;
      groupControl.setIsWorkView(groupPsn.getIsWorkView());
    }
    if (StringUtils.isNotBlank(groupPsn.getPrjViewType())) {
      flag = true;
      groupControl.setPrjViewType(groupPsn.getPrjViewType());
    }
    if (StringUtils.isNotBlank(groupPsn.getPubViewType())) {
      flag = true;
      groupControl.setPubViewType(groupPsn.getPubViewType());
    }
    if (StringUtils.isNotBlank(groupPsn.getRefViewType())) {
      flag = true;
      groupControl.setRefViewType(groupPsn.getRefViewType());
    }
    if (StringUtils.isNotBlank(groupPsn.getShareFileType())) {
      flag = true;
      groupControl.setShareFileType(groupPsn.getShareFileType());
    }
    if (!flag) {
      return null;
    }
    return groupControl;
  }

  @Override
  public void saveFilter(GroupFilter groupFilter) {
    groupFilterDao.saveGroupFilter(groupFilter);
  }

  /**
   * 转换封装群组过滤信息.
   * 
   * @param groupPsn
   * @return
   */
  private GroupFilter buildFilter(GroupPsn groupPsn) {
    GroupFilter groupFilter = groupFilterDao.getGroupFilter(groupPsn.getGroupId());
    boolean flag = false;
    if (groupFilter == null) {
      groupFilter = new GroupFilter();
      if (groupPsn.getGroupId() != null) {
        groupFilter.setGroupId(groupPsn.getGroupId());
      }
    }
    if (StringUtils.isNotBlank(groupPsn.getGroupCode())) {
      flag = true;
      groupFilter.setGroupCode(groupPsn.getGroupCode());
    }
    if (StringUtils.isNotBlank(groupPsn.getOpenType())) {
      flag = true;
      groupFilter.setOpenType(groupPsn.getOpenType());
    }
    if (groupPsn.getOwnerPsnId() != null) {
      flag = true;
      groupFilter.setOwnerPsnId(groupPsn.getOwnerPsnId());
    }
    if (StringUtils.isNotBlank(groupPsn.getStatus())) {
      flag = true;
      groupFilter.setStatus(groupPsn.getStatus());
    }
    if (!flag) {
      return null;
    }
    return groupFilter;
  }

  @Override
  public void saveKeyDisc(GroupKeyDisc groupKeyDisc) {
    groupKeyDiscDao.saveGroupKeyDisc(groupKeyDisc);
  }

  /**
   * 转换封装群组学科关键词信息.
   * 
   * @param groupPsn
   * @return
   */
  private GroupKeyDisc buildKeyDisc(GroupPsn groupPsn) {
    GroupKeyDisc keyDisc = groupKeyDiscDao.getGroupKeyDisc(groupPsn.getGroupId());
    boolean flag = false;
    if (keyDisc == null) {
      keyDisc = new GroupKeyDisc();
      if (groupPsn.getGroupId() != null) {
        keyDisc.setGroupId(groupPsn.getGroupId());
      }
    }
    if (groupPsn.getDiscipline1() != null) {
      flag = true;
      keyDisc.setDiscipline1(groupPsn.getDiscipline1());
    }
    if (StringUtils.isNotBlank(groupPsn.getDisciplines())) {
      flag = true;
      keyDisc.setDisciplines(groupPsn.getDisciplines());
    }
    if (StringUtils.isNotBlank(groupPsn.getDiscCodes())) {
      flag = true;
      keyDisc.setDiscCodes(groupPsn.getDiscCodes());
    }
    if (StringUtils.isNotBlank(groupPsn.getEnKeyWords())) {
      flag = true;
      keyDisc.setEnKeyWords(groupPsn.getEnKeyWords());
    }
    if (StringUtils.isNotBlank(groupPsn.getEnKeyWords1())) {
      flag = true;
      keyDisc.setEnKeyWords1(groupPsn.getEnKeyWords1());
    }
    if (CollectionUtils.isNotEmpty(groupPsn.getEnKeyWordsList())) {
      flag = true;
      keyDisc.setEnKeyWordsList(groupPsn.getEnKeyWordsList());
    }
    if (StringUtils.isNotBlank(groupPsn.getKeyWords())) {
      flag = true;
      keyDisc.setKeyWords(groupPsn.getKeyWords());
    }
    if (StringUtils.isNotBlank(groupPsn.getKeyWords1())) {
      flag = true;
      keyDisc.setKeyWords1(groupPsn.getKeyWords1());
    }
    if (CollectionUtils.isNotEmpty(groupPsn.getKewWordsList())) {
      flag = true;
      keyDisc.setKewWordsList(groupPsn.getKewWordsList());
    }
    if (!flag) {
      return null;
    }
    return keyDisc;
  }

  @Override
  public void saveStatistics(GroupStatistics groupStat) {
    groupStatisticsDao.saveGroupStatistics(groupStat);
  }

  /**
   * 转换封装群组统计信息.
   * 
   * @param groupPsn
   * @return
   */
  private GroupStatistics buildStatistics(GroupPsn groupPsn) {
    GroupStatistics statistics = groupStatisticsDao.getStatistics(groupPsn.getGroupId());
    boolean flag = false;
    if (statistics == null) {
      statistics = new GroupStatistics();
      if (groupPsn.getGroupId() != null && groupPsn.getGroupId().longValue() > 0) {
        statistics.setGroupId(groupPsn.getGroupId());
      }
    }
    if (groupPsn.getSumFiles() != null && groupPsn.getSumFiles().intValue() > 0) {
      flag = true;
      statistics.setSumFiles(groupPsn.getSumFiles());
    }
    if (groupPsn.getSumFilesNfolder() != null && groupPsn.getSumFilesNfolder().intValue() > 0) {
      flag = true;
      statistics.setSumFilesNfolder(groupPsn.getSumFilesNfolder());
    }
    if (groupPsn.getSumFilesNfolder() != null && groupPsn.getSumFilesNfolder().intValue() > 0) {
      flag = true;
      statistics.setSumMaterials(groupPsn.getSumMaterials());
    }
    if (groupPsn.getSumMaterialsNfolder() != null && groupPsn.getSumMaterialsNfolder().intValue() > 0) {
      flag = true;
      statistics.setSumMaterialsNfolder(groupPsn.getSumMaterialsNfolder());
    }
    if (groupPsn.getSumMembers() != null && groupPsn.getSumMembers().intValue() > 0) {
      flag = true;
      statistics.setSumMembers(groupPsn.getSumMembers());
    }
    if (groupPsn.getSumPrjs() != null && groupPsn.getSumPrjs().intValue() > 0) {
      flag = true;
      statistics.setSumPrjs(groupPsn.getSumPrjs());
    }
    if (groupPsn.getSumPrjsNfolder() != null && groupPsn.getSumPrjsNfolder().intValue() > 0) {
      flag = true;
      statistics.setSumPrjsNfolder(groupPsn.getSumPrjsNfolder());
    }
    if (groupPsn.getSumPubs() != null && groupPsn.getSumPubs().intValue() > 0) {
      flag = true;
      statistics.setSumPubs(groupPsn.getSumPubs());
    }
    if (groupPsn.getSumPubsNfolder() != null && groupPsn.getSumPubsNfolder().intValue() > 0) {
      flag = true;
      statistics.setSumPubsNfolder(groupPsn.getSumPubsNfolder());
    }
    if (groupPsn.getSumRefs() != null && groupPsn.getSumRefs().intValue() > 0) {
      flag = true;
      statistics.setSumRefs(groupPsn.getSumRefs());
    }
    if (groupPsn.getSumRefsNfolder() != null && groupPsn.getSumRefsNfolder().intValue() > 0) {
      flag = true;
      statistics.setSumRefsNfolder(groupPsn.getSumRefsNfolder());
    }
    if (groupPsn.getSumSubjects() != null && groupPsn.getSumSubjects().intValue() > 0) {
      flag = true;
      statistics.setSumSubjects(groupPsn.getSumSubjects());
    }
    if (groupPsn.getSumToMembers() != null && groupPsn.getSumToMembers().intValue() > 0) {
      flag = true;
      statistics.setSumToMembers(groupPsn.getSumToMembers());
    }
    if (groupPsn.getSumWorks() != null && groupPsn.getSumWorks().intValue() > 0) {
      flag = true;
      statistics.setSumWorks(groupPsn.getSumWorks());
    }
    if (groupPsn.getSumWorksNfolder() != null && groupPsn.getSumWorksNfolder().intValue() > 0) {
      flag = true;
      statistics.setSumWorksNfolder(groupPsn.getSumWorksNfolder());
    }
    if (groupPsn.getVisitCount() != null && groupPsn.getVisitCount().longValue() > 0) {
      flag = true;
      statistics.setVisitCount(groupPsn.getVisitCount());
    }
    if (!flag) {
      return null;
    }
    return statistics;
  }

  /**
   * 更新群组简介.
   * 
   * @param sourceGroupId 源群组ID
   * @param targetGroupId 目标群组ID.
   */
  @Override
  public void updateGroupDesc(Long sourceGroupId, Long targetGroupId) {
    groupBaseInfoDao.updateGroupDesc(sourceGroupId, targetGroupId);
  }

  @Override
  public void saveGroupControl(GroupPsn groupPsn) {
    // 保存群组控制信息.
    GroupControl groupControl = this.buildControl(groupPsn);
    if (groupControl != null) {
      this.saveControl(groupControl);
    }
  }

  @Override
  public void saveGroupFilter(GroupPsn groupPsn) {
    // 保存群组检索过滤信息.
    GroupFilter groupFilter = this.buildFilter(groupPsn);
    if (groupFilter != null) {
      this.saveFilter(groupFilter);
    }
  }

  @Override
  public void saveGroupKeyDisc(GroupPsn groupPsn) {
    // 保存群组检索过滤信息.
    GroupKeyDisc keyDisc = this.buildKeyDisc(groupPsn);
    if (keyDisc != null) {
      this.saveKeyDisc(keyDisc);
    }
  }

  @Override
  public void saveGroupStatistics(GroupPsn groupPsn) {
    // 保存群组统计信息.
    GroupStatistics statistics = this.buildStatistics(groupPsn);
    if (statistics != null) {
      this.saveStatistics(statistics);
    }
  }

  /**
   * 更新群组所属人.
   * 
   * @param ownerPsnId
   * @param groupId
   */
  @Override
  public void updateGroupOwner(Long ownerPsnId, Long groupId) {
    groupFilterDao.updateGroupOwner(ownerPsnId, groupId);
  }

  /**
   * 更改群组主页可见权限.
   * 
   * @param groupId
   * @param pageOpenStatus
   */
  @Override
  public void updateGroupPageOpen(Long groupId, int pageOpenStatus) {
    groupControlDao.updateGroupPageOpen(groupId, pageOpenStatus);
  }

}
