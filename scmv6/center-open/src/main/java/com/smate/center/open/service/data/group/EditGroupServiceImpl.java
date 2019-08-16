package com.smate.center.open.service.data.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsNormalFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenCreateGroupException;
import com.smate.center.open.model.group.GroupBaseInfo;
import com.smate.center.open.model.group.GroupControl;
import com.smate.center.open.model.group.GroupFilter;
import com.smate.center.open.model.group.GroupKeyDisc;
import com.smate.center.open.model.group.GroupPsn;
import com.smate.center.open.model.group.GroupStatistics;
import com.smate.center.open.model.group.pub.RcmdSyncFlagMessage;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.group.psn.GroupPsnSearchService;
import com.smate.center.open.utils.xml.WebServiceUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 群组编辑基本数据同步服务实现
 * 
 * @author zjh
 *
 */
@Transactional(rollbackFor = Exception.class)
public class EditGroupServiceImpl extends ThirdDataTypeBase {

  @Autowired
  private BatchJobsNormalFactory batchJobsNormalFactory;
  @Autowired
  private BatchJobsService batchJobsService;
  @Autowired
  private GroupPsnSearchService groupPsnSearchService;

  /**
   * 进行判断，是否缺少一些必要的数据
   */
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    // 校验数据参数
    try {
      Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
      if (temp.get(OpenConsts.RESULT_STATUS) != null
          && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
        return temp;
      }
      Object syncXml = serviceData.get(OpenConsts.MAP_SYNCXML);
      if (syncXml == null || "".equals(syncXml.toString())) {
        logger.error("同步群组数据 群组基本数据不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_242, paramet, "");
        return temp;
      }
      Object psnId = paramet.get(OpenConsts.MAP_PSNID);
      if (psnId == null || "".equals(psnId.toString())) {
        logger.error("同步群组数据创建人psnId不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_243, paramet, "");
        return temp;
      }
      GroupPsn groupPsn = WebServiceUtils.toCreateGroup(syncXml.toString());
      if (groupPsn.getGroupId() == null) {
        Long groupId = NumberUtils.toLong(paramet.get("groupId").toString());
        groupPsn.setGroupId(groupId);
      }
      if (groupPsn == null) {
        logger.error("同步群组数据群组基本数据 构造对象失败");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_244, paramet, "");
        return temp;
      }
      if (groupPsn.getGroupId() == null || groupPsn.getGroupId() == 0L) {
        logger.error("同步编辑的群组基本数据 groupId不能为空 ");
      }
      if (StringUtils.isBlank(groupPsn.getGroupName())) {
        logger.error("同步群组数据 群组基本数据 群组名称 groupName不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_245, paramet, "");
        return temp;
      }
      if (StringUtils.isBlank(groupPsn.getGroupCategory())) {
        logger.error("群组创建 群组基本数据 群组分类 groupCategory不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_246, paramet, "");
        return temp;
      }
      if (IrisNumberUtils.toInteger(groupPsn.getGroupCategory()) == null) {
        logger.error("群组创建 群组基本数据 群组分类groupCategory要为数字类型:10表示兴趣群组,11表示项目群组");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_247, paramet, "");
        return temp;
      }
      if (StringUtils.isBlank(groupPsn.getOpenType())) {
        logger.error("群组创建 群组基本数据 群组公开类型 openType不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_248, paramet, "");
        return temp;
      }
      paramet.put(OpenConsts.MAP_DATAPATAMETMAP, serviceData);
      paramet.put(OpenConsts.MAP_DATA_GROUP_PSN, groupPsn);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    } catch (Exception e) {
      logger.error("群组创建，参数校验出错", e);
      throw new OpenCreateGroupException("群组创建，参数校验出错", e);
    }
    return temp;
  }

  /**
   * 判断后进行数据同步
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, null);
    if (serviceData == null) {
      return super.successMap("同步群组数据失败", null);
    }
    GroupPsn groupPsn = (GroupPsn) paramet.get(OpenConsts.MAP_DATA_GROUP_PSN);
    Long psnId = (Long) paramet.get(OpenConsts.MAP_PSNID);
    Long openId = NumberUtils.toLong(paramet.get(OpenConsts.MAP_OPENID).toString());
    boolean flag = syncGroupInfo(groupPsn, psnId, openId);
    return serviceData;
  }

  /**
   * 编辑群组同步逻辑
   * 
   * @param groupPsn
   * @param psnId
   * @return
   * @throws Exception
   */
  public boolean syncGroupInfo(GroupPsn groupPsn, Long psnId, Long openId) {

    // 6.后台任务
    try {
      // GroupPsn buildgroupPsn = getBuildGroupPsn(groupPsn);
      buildCreateGroupTask(groupPsn, psnId);
    } catch (Exception e) {
      logger.error("后台任务出错", e);
      throw new OpenCreateGroupException("后台任务出错", e);
    }

    return true;

  }

  /**
   * 重新构造GroupPsn对象
   * 
   * @return
   */
  private GroupPsn getBuildGroupPsn(GroupPsn gp) {
    if (gp == null) {
      return null;
    }
    GroupBaseInfo baseInfo = groupPsnSearchService.getBaseInfo(gp.getGroupId());
    if (baseInfo == null) {
      return null;
    }
    gp = BuildGroupPsnFormGroupBaseInfo(gp, baseInfo);
    gp = BuildGroupPsnFormGroupFilter(gp, groupPsnSearchService.getGroupFilterByGroupId(gp.getGroupId()));
    gp = BuildGroupPsnFormGroupControl(gp, groupPsnSearchService.getGroupControlByGroupId(gp.getGroupId()));
    gp = BuildGroupPsnFormGroupStatistics(gp, groupPsnSearchService.getGroupStatisticsByGroupId(gp.getGroupId()));
    gp = BuildGroupPsnFormGroupKeyDisc(gp, groupPsnSearchService.getGroupKeyDiscByGroupId(gp.getGroupId()));
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
      // gp.setKewWordsList(gkd.getKewWordsList());
      gp.setEnKeyWords1(gkd.getEnKeyWords1());
      gp.setEnKeyWords(gkd.getEnKeyWords());
      // gp.setEnKeyWordsList(gkd.getEnKeyWordsList());
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
      // gp.setIsSave(gc.getIsSave());
      gp.setIsIsisPrj(gc.getIsIsisPrj());
      // TODO 没有处理的数据 workViewType materialViewType
    }
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
    // gp.setDes3GroupId(bi.getDes3GroupId());
    // gp.setCategoryName(bi.getCategoryName());
    gp.setSumToMembers(bi.getSumMembers());
    // gp.setGroupRole(bi.getGroupRole());
    // gp.setIsAccept(bi.getIsAccept());
    gp.setDisciplines(bi.getDisciplines());
    gp.setOpenType(bi.getOpenType());
    gp.setDiscCodes(bi.getDiscCodes());
    gp.setKeyWords(bi.getKeyWords());
    // gp.setIsisGuid(bi.getIsisGuid());
    gp.setSumMembers(bi.getSumMembers());
    // gp.setGroupNodeId(bi.getNodeId());
    // TODO 没有处理的数据 psnId groupNoOrder
    return gp;
  }

  /**
   * 构建创建群组后台任务
   * 
   * @param groupPsn
   */
  private void buildCreateGroupTask(GroupPsn groupPsn, Long psnId) {
    List<BatchJobs> jobsList = new ArrayList<BatchJobs>();
    // String groupPsnStr = JacksonUtils.jsonObjectSerializer(groupPsn);
    RcmdSyncFlagMessage message = RcmdSyncFlagMessage.getInstance(psnId);
    message.setPsnId(psnId);
    message.setGoup(groupPsn.getGroupId(), 0);
    String messageStr = JacksonUtils.jsonObjectSerializer(message);
    // 同步群组信息到群组成员
    BatchJobs job1 = batchJobsNormalFactory.getBatchJob(BatchOpenCodeEnum.SYSNC_GROUP_INFO, groupPsn.getGroupId(),
        BatchWeightEnum.B.toString());
    jobsList.add(job1);
    // a、同步群组信息(保存群组编辑信息的时候也会用到)
    BatchJobs job2 =
        batchJobsNormalFactory.getBatchJob1(BatchJobUtil.getGroupContext(groupPsn.getGroupId().toString(), 1),
            BatchWeightEnum.B.toString(), BatchOpenCodeEnum.SYSNC_GROUP_PSN_TO_SNS);
    jobsList.add(job2);
    // c、群组信息和成员有变动，同步到ROL(合作分析)(传递GroupPsn对象)(保存群组编辑信息的时候也会用到)
    BatchJobs job4 = batchJobsNormalFactory.getBatchJob1(BatchJobUtil.getContext1(groupPsn.getGroupId().toString()),
        BatchWeightEnum.B.toString(), BatchOpenCodeEnum.SYSNC_FOR_ALL_GROUP_UPDATE_TO_ROL);
    jobsList.add(job4);
    // d、更新人员统计表群组数
    BatchJobs job5 = batchJobsNormalFactory.getBatchJob1(BatchJobUtil.getContext(psnId), BatchWeightEnum.B.toString(),
        BatchOpenCodeEnum.SYSNC_GROUP_STATISTICS);
    jobsList.add(job5);
    // e、群组信息和成员变动，同步到推荐服务(保存群组编辑信息中也会用到)
    BatchJobs job6 = batchJobsNormalFactory.getBatchJob1(BatchJobUtil.getContext1(messageStr),
        BatchWeightEnum.B.toString(), BatchOpenCodeEnum.SYSNC_RCMD_SYNC_INFO);
    jobsList.add(job6);

    for (BatchJobs b : jobsList) {
      batchJobsService.saveJob(b);
    }
  }

}
