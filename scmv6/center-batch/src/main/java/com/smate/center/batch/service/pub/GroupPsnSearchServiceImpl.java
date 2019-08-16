package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.GroupBaseinfoDao;
import com.smate.center.batch.dao.sns.pub.GroupControlDao;
import com.smate.center.batch.dao.sns.pub.GroupFilterDao;
import com.smate.center.batch.dao.sns.pub.GroupKeyDiscDao;
import com.smate.center.batch.dao.sns.pub.GroupPsnDao;
import com.smate.center.batch.dao.sns.pub.GroupStatisticsDao;
import com.smate.center.batch.model.sns.pub.GroupBaseInfo;
import com.smate.center.batch.model.sns.pub.GroupControl;
import com.smate.center.batch.model.sns.pub.GroupFilter;
import com.smate.center.batch.model.sns.pub.GroupKeyDisc;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.GroupStatistics;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 群组检索服务(原group_psn表拆分为多个表，为减少对其他模块的影响，增加此服务以便数据封装)_SCM-6000.
 * 
 * @author mjg
 * @since 2014-11-28
 */
@Service("groupPsnSearchService")
@Transactional(rollbackFor = Exception.class)
public class GroupPsnSearchServiceImpl implements GroupPsnSearchService {

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
  @Autowired
  private GroupPsnDao groupPsnDao;

  /**
   * 获取群组统计记录.
   * 
   * @param groupId
   * @return
   */
  public GroupStatistics getStatistics(Long groupId) {
    return groupStatisticsDao.getStatistics(groupId);
  }

  /**
   * 获取群组基本信息记录.
   * 
   * @param groupId
   * @return
   */
  public GroupBaseInfo getBaseInfo(Long groupId) {
    return groupBaseInfoDao.getGroupBaseInfo(groupId);
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

  /**
   * 获取群组基本信息.
   * 
   * @param groupId
   * @return
   */
  public GroupPsn getGroupBaseInfo(Long groupId) {
    GroupBaseInfo baseInfo = this.getBaseInfo(groupId);
    if (baseInfo != null) {
      GroupPsn groupPsn = new GroupPsn();
      groupPsn = this.rebuildGroupBaseInfo(groupPsn, baseInfo);
      groupPsn.setGroupNodeId(ServiceConstants.SCHOLAR_NODE_ID_1);
      groupPsn.setDes3GroupId(ServiceUtil.encodeToDes3(groupId.toString()));
      groupPsn.setDes3GroupNodeId(ServiceUtil.encodeToDes3(groupPsn.getGroupNodeId().toString()));
      // 因群组检索过滤信息较多地方使用，因此在检索群组基本信息时同步封装状态等信息，以便通用_MJG_SCM-6000.
      GroupFilter filter = this.getFileter(groupId);
      if (filter != null) {
        groupPsn = this.rebuildGroupFilter(groupPsn, filter);
      }
      return groupPsn;
    }
    return null;
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
   * 获取群组控制信息.
   */
  @Override
  public GroupPsn getGroupControl(Long groupId) {
    GroupControl groupControl = this.getControl(groupId);
    if (groupControl != null) {
      GroupPsn groupPsn = new GroupPsn();
      groupPsn = this.rebuildGroupControl(groupPsn, groupControl);
      return groupPsn;
    }
    return null;
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
    groupPsn.setIsSave(groupControl.getIsSave());
    groupPsn.setIsShareFile(groupControl.getIsShareFile());
    groupPsn.setIsWorkView(groupControl.getIsWorkView());
    groupPsn.setPrjViewType(groupControl.getPrjViewType());
    groupPsn.setPubViewType(groupControl.getPubViewType());
    groupPsn.setRefViewType(groupControl.getRefViewType());
    groupPsn.setShareFileType(groupControl.getShareFileType());
    return groupPsn;
  }

  /**
   * 获取群组关键词学科记录.
   */
  @Override
  public GroupPsn getGroupKeyDisc(Long groupId) {
    GroupKeyDisc keyDisc = this.getKeyDisc(groupId);
    if (keyDisc != null) {
      GroupPsn groupPsn = new GroupPsn();
      groupPsn = this.rebuildGroupKeyDisc(groupPsn, keyDisc);
      return groupPsn;
    }
    return null;
  }

  private GroupPsn rebuildGroupKeyDisc(GroupPsn groupPsn, GroupKeyDisc keyDisc) {
    groupPsn.setDiscipline1(keyDisc.getDiscipline1());
    groupPsn.setDisciplines(keyDisc.getDisciplines());
    groupPsn.setDiscCodes(keyDisc.getDiscCodes());
    groupPsn.setEnKeyWords(keyDisc.getEnKeyWords());
    groupPsn.setEnKeyWords1(keyDisc.getEnKeyWords1());
    groupPsn.setEnKeyWordsList(keyDisc.getEnKeyWordsList());
    groupPsn.setGroupId(keyDisc.getGroupId());
    groupPsn.setKeyWords(keyDisc.getKeyWords());
    groupPsn.setKeyWords1(keyDisc.getKeyWords1());
    groupPsn.setKewWordsList(keyDisc.getKewWordsList());
    return groupPsn;
  }

  /**
   * 获取群组统计记录.
   */
  @Override
  public GroupPsn getGroupStatistics(Long groupId) {
    GroupStatistics statistics = this.getStatistics(groupId);
    if (statistics != null) {
      GroupPsn groupPsn = new GroupPsn();
      groupPsn = this.rebuildGroupStatistics(groupPsn, statistics);
      return groupPsn;
    }
    return null;
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

  /**
   * 获取群组检索过滤信息记录.
   */
  @Override
  public GroupPsn getGroupFilter(Long groupId) {
    GroupFilter filter = this.getFileter(groupId);
    if (filter != null) {
      GroupPsn groupPsn = new GroupPsn();
      groupPsn = this.rebuildGroupFilter(groupPsn, filter);
      return groupPsn;
    }
    return null;
  }

  private GroupPsn rebuildGroupFilter(GroupPsn groupPsn, GroupFilter filter) {
    groupPsn.setGroupCode(filter.getGroupCode());
    groupPsn.setGroupId(filter.getGroupId());
    groupPsn.setOpenType(filter.getOpenType());
    groupPsn.setOwnerPsnId(filter.getOwnerPsnId());
    groupPsn.setStatus(filter.getStatus());
    return groupPsn;
  }



  @Override
  public GroupPsn getGroupPsn(Long groupId) {
    GroupBaseInfo baseInfo = this.getBaseInfo(groupId);
    // 如果群组基本信息为空，则直接返回，不构建其他参数对象.
    if (baseInfo != null) {
      GroupPsn groupPsn = new GroupPsn();
      groupPsn = this.rebuildGroupBaseInfo(groupPsn, baseInfo);
      groupPsn.setGroupDescription(null);// SCM-6988
      groupPsn.setGroupNodeId(ServiceConstants.SCHOLAR_NODE_ID_1);
      groupPsn.setDes3GroupId(ServiceUtil.encodeToDes3(groupId.toString()));
      groupPsn.setDes3GroupNodeId(ServiceUtil.encodeToDes3(groupPsn.getGroupNodeId().toString()));

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


  @Override
  public Map<String, Object> getGroupPsnMap(Long groupId, Long psnId) {
    // 获取需要的Map信息
    Map<String, Object> result;
    result = groupBaseInfoDao.getGroupInviteInfoMap(groupId, psnId);
    if (result != null) {
      return result;
    }
    // 如果群组基本信息为空，则直接返回，不构建其他参数对象.
    return null;
  }

  /**
   * 获取迁移的任务记录列表.
   * 
   * @param startGroupId
   * @param maxSize
   * @return
   */
  public List<Long> getTaskIdList(Long startGroupId, int maxSize) {
    return groupPsnDao.getTaskIdList(startGroupId, maxSize);
  }

  /**
   * 获取群组记录.
   */
  public GroupPsn getGroupPsnRecord(Long groupId) {
    return groupPsnDao.get(groupId);
  }

  /**
   * 获取group_baseInfo表最大群组ID.
   * 
   * @return
   */
  public Long getMaxBaseInfoGroupId() {
    return groupBaseInfoDao.getMaxGroupId();
  }

  /**
   * 根据groupId构建GroupPsn对象
   */
  @Override
  public GroupPsn getBuildGroupPsn(Long groupId) {
    if (groupId == null) {
      return null;
    }
    GroupBaseInfo baseInfo = groupBaseInfoDao.getGroupBaseInfo(groupId);
    if (baseInfo == null) {
      return null;
    }
    GroupPsn gp = new GroupPsn();
    gp.setGroupId(groupId);
    gp = BuildGroupPsnFormGroupBaseInfo(gp, baseInfo);
    gp = BuildGroupPsnFormGroupFilter(gp, groupFilterDao.getGroupFilter(groupId));
    gp = BuildGroupPsnFormGroupControl(gp, groupControlDao.getGroupControl(groupId));
    gp = BuildGroupPsnFormGroupStatistics(gp, groupStatisticsDao.getStatistics(groupId));
    gp = BuildGroupPsnFormGroupKeyDisc(gp, groupKeyDiscDao.getGroupKeyDisc(groupId));
    return gp;
  }

  /**
   * 从群组基础信息表构建GroupPsn对象数据
   * 
   * @param gp
   * @return
   */
  public GroupPsn BuildGroupPsnFormGroupBaseInfo(GroupPsn gp, GroupBaseInfo bi) {
    gp.setGroupNo(bi.getGroupNo());
    gp.setGroupName(bi.getGroupName());
    gp.setGroupDescription(bi.getGroupDescription());
    gp.setGroupCategory(bi.getGroupCategory());
    gp.setGroupAnnouncement(bi.getGroupAnnouncement());
    gp.setEmail(bi.getEmail());
    gp.setTel(bi.getTel());
    gp.setAddress(bi.getAddress());
    gp.setFundingTypes(bi.getFundingTypes());
    gp.setFileId(bi.getFileId());
    gp.setGroupImgUrl(bi.getGroupImgUrl());
    gp.setCreateDate(bi.getCreateDate());
    gp.setUpdateDate(bi.getUpdateDate());
    gp.setLastVisitDate(bi.getLastVisitDate());
    gp.setDes3GroupId(bi.getDes3GroupId());
    gp.setCategoryName(bi.getCategoryName());
    gp.setSumToMembers(bi.getSumMembers());
    gp.setGroupRole(bi.getGroupRole());
    gp.setIsAccept(bi.getIsAccept());
    gp.setDisciplines(bi.getDisciplines());
    gp.setOpenType(bi.getOpenType());
    gp.setDiscCodes(bi.getDiscCodes());
    gp.setKeyWords(bi.getKeyWords());
    gp.setIsisGuid(bi.getIsisGuid());
    gp.setSumMembers(bi.getSumMembers());
    gp.setGroupNodeId(bi.getNodeId());
    // TODO 没有处理的数据 psnId groupNoOrder
    return gp;
  }

  /**
   * 从群组检索过滤表构建GroupPsn对象数据
   * 
   * @param gp
   * @return
   */
  public GroupPsn BuildGroupPsnFormGroupFilter(GroupPsn gp, GroupFilter gf) {
    if (gf != null) {
      gp.setGroupCode(gf.getGroupCode());
      gp.setOpenType(gf.getOpenType());
      gp.setStatus(gf.getStatus());
      gp.setOwnerPsnId(gf.getOwnerPsnId());
    }
    return gp;
  }

  /**
   * 从群组控制开关表构建GroupPsn对象数据
   * 
   * @param gp
   * @return
   */
  public GroupPsn BuildGroupPsnFormGroupControl(GroupPsn gp, GroupControl gc) {
    if (gc != null) {
      gp.setIsGroupMemberView(gc.getIsGroupMemberView());
      gp.setIsDiscuss(gc.getIsDiscuss());
      gp.setIsShareFile(gc.getIsShareFile());
      gp.setShareFileType(gc.getShareFileType());
      gp.setIsPrjView(gc.getIsPrjView());
      gp.setPrjViewType(gc.getPrjViewType());
      gp.setIsPubView(gc.getIsPubView());
      gp.setPubViewType(gc.getPubViewType());
      gp.setIsRefView(gc.getIsRefView());
      gp.setRefViewType(gc.getRefViewType());
      gp.setIsWorkView(gc.getIsWorkView());
      gp.setIsMaterialView(gc.getIsMaterialView());
      gp.setIsMemberPublish(gc.getIsMemberPublish());
      gp.setIsPageOpen(gc.getIsPageOpen());
      gp.setIsPageDescOpen(gc.getIsPageDescOpen());
      gp.setIsPageMemberOpen(gc.getIsPageMemberOpen());
      gp.setIsPagePrjOpen(gc.getIsPagePrjOpen());
      gp.setIsPagePubOpen(gc.getIsPagePubOpen());
      gp.setIsPageRefOpen(gc.getIsPageRefOpen());
      gp.setIsPageFileOpen(gc.getIsPageFileOpen());
      gp.setIsPageWorkOpen(gc.getIsPageWorkOpen());
      gp.setIsPageMaterialOpen(gc.getIsPageMaterialOpen());
      gp.setIsSave(gc.getIsSave());
      gp.setIsIsisPrj(gc.getIsIsisPrj());
      // TODO 没有处理的数据 workViewType materialViewType
    }
    return gp;
  }

  /**
   * 从群组统计信息表构建GroupPsn对象数据
   * 
   * @param gp
   * @return
   */
  public GroupPsn BuildGroupPsnFormGroupStatistics(GroupPsn gp, GroupStatistics gs) {
    if (gs != null) {
      gp.setSumMembers(gs.getSumMembers());
      gp.setSumToMembers(gs.getSumToMembers());
      gp.setSumSubjects(gs.getSumSubjects());
      gp.setSumPubs(gs.getSumPubs());
      gp.setSumPrjs(gs.getSumPrjs());
      gp.setSumRefs(gs.getSumRefs());
      gp.setSumFiles(gs.getSumFiles());
      gp.setSumWorks(gs.getSumWorks());
      gp.setSumMaterials(gs.getSumMaterials());
      gp.setSumPubsNfolder(gs.getSumPubsNfolder());
      gp.setSumPrjsNfolder(gs.getSumPrjsNfolder());
      gp.setSumRefsNfolder(gs.getSumRefsNfolder());
      gp.setSumFilesNfolder(gs.getSumFilesNfolder());
      gp.setSumWorksNfolder(gs.getSumWorksNfolder());
      gp.setSumMaterialsNfolder(gs.getSumMaterialsNfolder());
      gp.setVisitCount(gs.getVisitCount());
    }
    return gp;
  }

  /**
   * 从群组学科关键词表构建GroupPsn对象数据
   * 
   * @param gp
   * @return
   */
  public GroupPsn BuildGroupPsnFormGroupKeyDisc(GroupPsn gp, GroupKeyDisc gkd) {
    if (gkd != null) {
      gp.setDisciplines(gkd.getDisciplines());
      gp.setDiscipline1(gkd.getDiscipline1());
      gp.setDiscCodes(gkd.getDiscCodes());
      gp.setKeyWords1(gkd.getKeyWords1());
      gp.setKeyWords(gkd.getKeyWords());
      gp.setKewWordsList(gkd.getKewWordsList());
      gp.setEnKeyWords1(gkd.getEnKeyWords1());
      gp.setEnKeyWords(gkd.getEnKeyWords());
      gp.setEnKeyWordsList(gkd.getEnKeyWordsList());
    }
    return gp;
  }
}
