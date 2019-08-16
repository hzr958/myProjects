package com.smate.center.batch.service.group;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.psn.RolPsnInsDao;
import com.smate.center.batch.dao.rol.pub.GroupCooperationStatisticsDao;
import com.smate.center.batch.dao.rol.pub.GroupPsnRelationDao;
import com.smate.center.batch.dao.sns.prj.GroupInvitePsnDao;
import com.smate.center.batch.dao.sns.pub.GroupInvitePsnFriendDao;
import com.smate.center.batch.dao.sns.pub.GroupInvitePsnFriendNodeDao;
import com.smate.center.batch.dao.sns.pub.GroupInvitePsnNodeDao;
import com.smate.center.batch.dao.sns.pub.GroupPsnDao;
import com.smate.center.batch.dao.sns.pub.GroupPsnNodeDao;
import com.smate.center.batch.dao.sns.pub.GroupPsnNodeSumDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.center.batch.model.rol.pub.GroupCooperationStatistics;
import com.smate.center.batch.model.rol.pub.GroupPsnRelation;
import com.smate.center.batch.model.sns.prj.GroupInvitePsn;
import com.smate.center.batch.model.sns.psn.FriendGroupOperateTaskInfo;
import com.smate.center.batch.model.sns.pub.GroupInvitePsnFriendNode;
import com.smate.center.batch.model.sns.pub.GroupInvitePsnNode;
import com.smate.center.batch.model.sns.pub.GroupInvitePsnNodePk;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.GroupPsnNode;
import com.smate.center.batch.model.sns.pub.GroupPsnNodeSum;
import com.smate.center.batch.model.sns.pub.InviteUrlValue;
import com.smate.center.batch.service.pub.GroupPsnSearchService;
import com.smate.center.batch.service.pub.GroupService;

/**
 * 同步群组信息到群组成员
 * 
 * @author zzx
 *
 */
@Service("syncGroupService")
@Transactional(rollbackFor = Exception.class)
public class SyncGroupServiceImpl implements SyncGroupService {
  @Autowired
  private GroupPsnSearchService groupPsnSearchService;
  @Autowired
  private InviteUrlValueService inviteUrlValueService;
  @Autowired
  private TaskInfoService taskInfoService;
  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;
  @Autowired
  private GroupInvitePsnNodeDao groupInvitePsnNodeDao;
  @Autowired
  private GroupInvitePsnFriendDao groupInvitePsnFriendDao;
  @Autowired
  private GroupPsnNodeDao groupPsnNodeDao;
  @Autowired
  private GroupPsnDao groupPsnDao;
  @Autowired
  private GroupInvitePsnFriendNodeDao groupInvitePsnFriendNodeDao;
  @Autowired
  private GroupPsnNodeSumDao groupPsnNodeSumDao;
  @Autowired
  private GroupCooperationStatisticsDao groupCooperationStatisticsDao;
  @Autowired
  private GroupPsnRelationDao groupPsnRelationDao;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${snsContext}")
  private String snsContext;

  /**
   * 同步群组信息到群组成员
   * 
   * @throws Exception
   */
  @Override
  public void syncGroupInfoToGroupPsn(Long groupId) throws Exception {
    GroupPsn groupPsn = groupPsnSearchService.getBuildGroupPsn(groupId);
    if (groupPsn != null) {
      groupPsn.setGroupNodeId(1);
      // 获取群组成员Id
      List<Long> psnIdList = groupInvitePsnDao.findGroupMemberPsnIdByGroupId(groupId);
      // 删除群组好友监听信息
      delGroupInvitePsnFriend(psnIdList);
      List<GroupInvitePsnNode> groupInvitePsnNodeList = groupInvitePsnNodeDao.findGroupInvitePsnNodeList(groupId);
      // 同步GROUP_PSN的统计数据等冗余数据到GROUP_INVITE_PSN_NODE
      syncGroupPsn(groupPsn, groupInvitePsnNodeList);
      GroupPsnNode groupPsnNode = groupPsnNodeDao.findGroupPnsNode(groupId);
      if (groupPsnNode != null) {
        groupPsnNode.setSumMembers(groupPsn.getSumMembers() == 0 ? 1 : groupPsn.getSumMembers());
        // 更新GROUP_PSN_NODE
        groupPsnNodeDao.save(groupPsnNode);
      } else {
        groupPsnNode = new GroupPsnNode();
        groupPsnNode.setGroupId(groupPsn.getGroupId());
        groupPsnNode.setGroupName(groupPsn.getGroupName());
        groupPsnNode.setNodeId(groupPsn.getGroupNodeId());
        groupPsnNode.setDisciplines(groupPsn.getDisciplines());
        groupPsnNode.setOpenType(groupPsn.getOpenType());
        groupPsnNode.setGroupCategory(groupPsn.getGroupCategory());
        groupPsnNode.setDiscCodes(groupPsn.getDiscCodes());
        groupPsnNode.setKeyWords(groupPsn.getKeyWords());
        groupPsnNode.setCreateDate(groupPsn.getCreateDate());
        groupPsnNode.setSumMembers(groupPsn.getSumMembers() == 0 ? 1 : groupPsn.getSumMembers());
        groupPsnNodeDao.save(groupPsnNode);
      }
    }

  }

  /**
   * 同步群组信息
   * 
   * @throws Exception
   */
  @Override
  public void syncGroupPsnToSns(Integer nodeId, GroupPsn groupPsn) throws Exception {
    List<GroupPsnNodeSum> groupPsnNodeSumList = groupPsnDao.sumGroupByCategory(nodeId);
    // 同步GROUP_PSN_NODE信息
    updateGroupPsnNode(groupPsn);
    List<GroupInvitePsnNode> groupInvitePsnNodeList =
        groupInvitePsnNodeDao.findGroupInvitePsnNodeList(groupPsn.getGroupId());
    // 同步GROUP_PSN的冗余数据到GROUP_INVITE_PSN_NODE群组与人员的邀请关系表
    syncGroupPsn(groupPsn, groupInvitePsnNodeList);
    List<GroupInvitePsnFriendNode> groupInvitePsnFriendNodeList =
        groupInvitePsnFriendNodeDao.findGroupInvitePsnFriendNodeList(groupPsn.getGroupId());
    // 同步GROUP_PSN的冗余数据到GroupInvitePsnFriendNode群组与人员的邀请关系表
    updateGroupInvitePsnFriendNode(groupPsn, groupInvitePsnFriendNodeList);
    // 同步GroupPsnNodeSum群组统计表
    upateGroupPsnNodeSum(groupPsnNodeSumList);
    // 同步群组邀请链接信息
    updateInviteUrlValue(groupPsn);
  }

  /**
   * 同步群组成员（传递GroupPsn对象和invite_psn_id）
   */
  @Override
  public void syncGroupInvitePsnToSns(Long invitePsnId, GroupPsn groupPsn) throws Exception {
    if (invitePsnId != null && invitePsnId != 0L) {
      GroupInvitePsn groupInvitePsn = groupInvitePsnDao.get(invitePsnId);
      if (groupInvitePsn != null) {
        GroupInvitePsnNodePk pk = new GroupInvitePsnNodePk();
        pk.setGroupId(groupInvitePsn.getGroupId());
        pk.setPsnId(groupInvitePsn.getPsnId());
        GroupInvitePsnNode groupInvitePsnNode = groupInvitePsnNodeDao.findGroupInvitePsnNode(pk);
        if (groupInvitePsnNode == null) {
          groupInvitePsnNode = new GroupInvitePsnNode();
          groupInvitePsnNode.setId(pk);
        }
        // 同步groupPsn和groupInvitePsn的冗余数据到GROUP_INVITE_PSN_NODE群组与人员的邀请关系表
        groupInvitePsnNode.setGroupRole(groupInvitePsn.getGroupRole());
        groupInvitePsnNode.setIsAccept(groupInvitePsn.getIsAccept());
        List<GroupInvitePsnNode> groupInvitePsnNodeList = new ArrayList<GroupInvitePsnNode>();
        groupInvitePsnNodeList.add(groupInvitePsnNode);
        syncGroupPsn(groupPsn, groupInvitePsnNodeList);
        // 只有用户为管理员或者成员请求加入被接受才执行，防止群组成员未被接受前执行了好友群组关系的更新
        if (GroupService.GROUP_ROLE_CREATOR.equals(groupInvitePsn.getGroupRole())
            || GroupService.ACCEPT.equals(groupInvitePsn.getIsAccept())) {
          // 生成加入群组操作任务，用于同步好友群组关系数据
          FriendGroupOperateTaskInfo taskInfo =
              new FriendGroupOperateTaskInfo(groupInvitePsn.getPsnId(), groupInvitePsn.getGroupId(), 3);
          if (!taskInfoService.isDataExist(taskInfo)) {
            taskInfoService.addTaskInfo(taskInfo);
          }
        }
      }
    }
  }

  /**
   * 群组信息和成员有变动，同步到ROL(合作分析)(传递GroupPsn对象)
   */
  @Override
  public void syncForAllGroupUpdateToRol(GroupPsn groupPsn) throws Exception {
    GroupCooperationStatistics st = groupCooperationStatisticsDao.findByGroupId(groupPsn.getGroupId());
    if (st == null) {
      st = new GroupCooperationStatistics();
    }
    st.setGroupId(groupPsn.getGroupId());
    st.setNodeId(groupPsn.getGroupNodeId());
    st.setGroupName(groupPsn.getGroupName());
    st.setGroupCategory(groupPsn.getGroupCategory());
    st.setCreateDate(groupPsn.getCreateDate());
    st.setSumMemebers(groupPsn.getSumMembers());
    st.setVisitCount(groupPsn.getVisitCount());
    st.setCategoryName(groupPsn.getCategoryName());
    // TODO SumActivity SumBiz
    groupCooperationStatisticsDao.save(st);
    List<GroupPsnRelation> groupPsnRelationList = groupPsnRelationDao.findGroupPsnRelationList(groupPsn.getGroupId());

    List<Long> psnIdList = groupInvitePsnDao.findGroupMemberPsnIdByGroupId(groupPsn.getGroupId());
    // 清理群组人员关系
    List<GroupPsnRelation> existList = clearOldGroupPsnRelation(groupPsnRelationList, psnIdList);
    // 新增群组人员关系
    existList = addNewGroupPsnRelation(existList, psnIdList, groupPsn);
  }

  /**
   * 新增群组人员关系
   * 
   * @param gList
   * @param psnIdList
   * @return
   * @throws Exception
   */
  private List<GroupPsnRelation> addNewGroupPsnRelation(List<GroupPsnRelation> existList, List<Long> psnIdList,
      GroupPsn g) throws Exception {
    if (existList != null && existList.size() > 0) {
      for (Long checkPsnId : psnIdList) {
        List<RolPsnIns> rList = rolPsnInsDao.findRolPsnInsList(checkPsnId);
        if (rList.size() > 0) {
          // 检查人员单位关系与人员群组关系是否一致
          for (RolPsnIns rolPsnIns : rList) {
            Long psnId = rolPsnIns.getPsnId();
            Long groupId = g.getGroupId();
            Integer nodeId = g.getGroupNodeId();
            Long insId = rolPsnIns.getPk().getInsId();
            Long unitId = rolPsnIns.getUnitId();
            Long superUnitId = rolPsnIns.getSuperUnitId();
            // 不存在记录，则新增
            if (!existList.contains(new GroupPsnRelation(psnId, groupId, nodeId, insId, unitId, superUnitId))) {
              // 新增
              GroupPsnRelation relation = groupPsnRelationDao.findGroupPsnRelation(groupId, psnId, insId);
              if (relation == null) {// 解决唯一索引冲突问题
                relation = new GroupPsnRelation();
              }
              relation.setPsnId(checkPsnId);
              relation.setGroupId(groupId);
              relation.setNodeId(nodeId);
              relation.setInsId(insId);
              relation.setUnitId(unitId);
              relation.setSuperUnitId(superUnitId);
              groupPsnRelationDao.save(relation);
            }
          }
        } else {
          Long psnId = checkPsnId;
          Long groupId = g.getGroupId();
          Integer nodeId = g.getGroupNodeId();
          Long insId = NOTEXIST_INSID;
          Long unitId = null;
          Long superUnitId = null;
          // 不存在记录，则新增
          if (!existList.contains(new GroupPsnRelation(psnId, groupId, nodeId, insId, unitId, superUnitId))) {
            // 新增
            GroupPsnRelation relation = groupPsnRelationDao.findGroupPsnRelation(groupId, psnId, insId);
            if (relation == null) {// 解决唯一索引冲突问题
              relation = new GroupPsnRelation();
            }
            relation.setPsnId(checkPsnId);
            relation.setGroupId(groupId);
            relation.setNodeId(nodeId);
            relation.setInsId(insId);
            relation.setUnitId(unitId);
            relation.setSuperUnitId(superUnitId);
            groupPsnRelationDao.save(relation);
          }
        }
      }
    }
    return existList;
  }

  /**
   * 清理群组人员关系
   * 
   * @param groupPsnRelationList
   * @param rolPsnInsList
   * @param psnIdList
   */
  private List<GroupPsnRelation> clearOldGroupPsnRelation(List<GroupPsnRelation> gList, List<Long> psnIdList) {
    List<GroupPsnRelation> existList = null;
    if (gList != null && gList.size() > 0) {
      // 保留合作分析存在的记录
      existList = new ArrayList<GroupPsnRelation>();
      for (GroupPsnRelation relation : gList) {
        existList.add(relation);// 临时记录数据
        // 单位和部门已经不存在
        boolean isUnitNotExist = true;
        List<RolPsnIns> rolPsnInsList = rolPsnInsDao.findRolPsnInsList(relation.getPsnId());
        if (rolPsnInsList.size() > 0) {
          // 检查人员单位关系与人员群组关系是否一致
          for (RolPsnIns rolPsnIns : rolPsnInsList) {
            Long psnId = rolPsnIns.getPsnId();
            Long groupId = relation.getGroupId();
            Integer nodeId = relation.getNodeId();
            Long insId = rolPsnIns.getPk().getInsId();
            Long unitId = rolPsnIns.getUnitId();
            Long superUnitId = rolPsnIns.getSuperUnitId();
            if (relation.equals(new GroupPsnRelation(psnId, groupId, nodeId, insId, unitId, superUnitId))) {
              isUnitNotExist = false;
              break;
            }
          }
        } else {// 没有单位也保留群组和人员的关系
          Long psnId = relation.getPsnId();
          Long groupId = relation.getGroupId();
          Integer nodeId = relation.getNodeId();
          Long insId = NOTEXIST_INSID;
          Long unitId = null;
          Long superUnitId = null;
          if (relation.equals(new GroupPsnRelation(psnId, groupId, nodeId, insId, unitId, superUnitId))) {
            isUnitNotExist = false;
          }
        }
        // 群组成员已经不存在
        boolean isMemberNotExist = true;
        // 检查人员群组关系是否发生变化
        for (Long psnId : psnIdList) {
          if (psnId.equals(relation.getPsnId())) {
            isMemberNotExist = false;
            break;
          }
        }
        // 单位或人员不在群组中，都属于脏数据，需要清除
        if (isUnitNotExist || isMemberNotExist) {
          existList.remove(relation);// 移除要删除的数据
          groupPsnRelationDao.delete(relation);
        }
      }
    }
    return existList;
  }

  /**
   * 更新群组邀请链接信息
   * 
   * @throws Exception
   */
  private void updateInviteUrlValue(GroupPsn groupPsn) throws Exception {
    InviteUrlValue inviteUrlValue = inviteUrlValueService.findInviteUrlValueByRefId(groupPsn.getGroupId());
    if (inviteUrlValue != null) {
      String groupUrl = domainscm + snsContext;
      String groupDoMain = "";
      if (StringUtils.isBlank(groupPsn.getGroupPageUrl())) {
        groupDoMain = groupUrl + "/group/" + groupPsn.getGroupId();
      } else {
        groupDoMain = groupUrl + "/group/" + groupPsn.getGroupPageUrl();
      }
      inviteUrlValue.setValue("{menuId:34,groupPsn.groupNodeId:" + groupPsn.getGroupNodeId() + ",groupPsn.des3GroupId:'"
          + groupPsn.getDes3GroupId() + "',groupPsn.navAction:'groupDyn'" + ",groupName:'" + groupPsn.getGroupName()
          + "',groupDomain:'" + groupDoMain + "',imgUrl:'" + groupPsn.getGroupImgUrl() + "',groupDetail:'"
          + (groupPsn.getGroupDescription() == null ? "" : groupPsn.getGroupDescription()).replaceAll("\r\n", "")
          + "'}");
      this.inviteUrlValueService.saveInviteUrlValue(inviteUrlValue);
    }
  }

  /**
   * 更新GroupPsnNodeSum群组统计表
   * 
   * @throws DaoException
   */
  private void upateGroupPsnNodeSum(List<GroupPsnNodeSum> groupPsnNodeSumList) throws DaoException {
    if (groupPsnNodeSumList != null && groupPsnNodeSumList.size() > 0) {
      for (GroupPsnNodeSum g : groupPsnNodeSumList) {
        GroupPsnNodeSum updateGroupPsnSum = groupPsnNodeSumDao.findGroupPsnSum(g.getId());
        if (updateGroupPsnSum != null) {
          updateGroupPsnSum.setCategorySum(g.getCategorySum());
          groupPsnNodeSumDao.save(updateGroupPsnSum);
        } else {
          groupPsnNodeSumDao.save(g);
        }

      }
    }
  }

  /**
   * 更新groupInvitePsnFriendNodeList信息
   * 
   * @param gp
   * @param groupInvitePsnFriendNodeList
   */
  private void updateGroupInvitePsnFriendNode(GroupPsn gp,
      List<GroupInvitePsnFriendNode> groupInvitePsnFriendNodeList) {
    if (groupInvitePsnFriendNodeList != null && groupInvitePsnFriendNodeList.size() > 0) {
      for (GroupInvitePsnFriendNode g : groupInvitePsnFriendNodeList) {
        g.setNodeId(gp.getGroupNodeId());
        g.setGroupName(gp.getGroupName());
        g.setGroupDescription(gp.getGroupDescription());
        g.setGroupCategory(gp.getGroupCategory());
        g.setSumMembers(gp.getSumMembers());
        g.setSumSubjects(gp.getSumSubjects());
        g.setSumPubs(gp.getSumPubs());
        g.setSumPrjs(gp.getSumPrjs());
        g.setSumRefs(gp.getSumRefs());
        g.setSumFiles(gp.getSumFiles());
        g.setOwnerPsnId(gp.getOwnerPsnId());
        g.setGroupImgUrl(gp.getGroupImgUrl());
        g.setGroupDescriptionSub(gp.getGroupDescriptionSub());
        g.setOpenType(gp.getOpenType());
        g.setDes3GroupNodeId(gp.getDes3GroupNodeId());
        g.setCreateDate(gp.getCreateDate());
        // TODO 没有处理的数据 id psnIds psnNames
      }
    }
  }

  /**
   * 更新GROUP_PSN_NODE信息
   * 
   * @param groupId
   */
  private void updateGroupPsnNode(GroupPsn gp) {
    GroupPsnNode groupPnsNode = groupPsnNodeDao.findGroupPnsNode(gp.getGroupId());
    if (groupPnsNode == null) {
      groupPnsNode = new GroupPsnNode();
      groupPnsNode.setGroupId(gp.getGroupId());
    }
    groupPnsNode.setSumMembers(gp.getSumMembers() == 0 ? 1 : gp.getSumMembers());
    groupPnsNode.setGroupName(gp.getGroupName());
    groupPnsNode.setDes3GroupId(gp.getDes3GroupId());
    groupPnsNode.setNodeId(gp.getGroupNodeId());
    groupPnsNode.setDisciplines(gp.getDisciplines());
    groupPnsNode.setGroupCategory(gp.getGroupCategory());
    groupPnsNode.setOpenType(gp.getOpenType());
    groupPnsNode.setDiscCodes(gp.getDiscCodes());
    groupPnsNode.setKeyWords(gp.getKeyWords());
    groupPnsNode.setIsisGuid(gp.getIsisGuid());
    groupPnsNode.setIsPrjView(gp.getIsPrjView());
    groupPnsNode.setIsShareFile(gp.getIsShareFile());
    groupPnsNode.setCreateDate(gp.getCreateDate());
    groupPnsNode.setGroupRole(gp.getGroupRole());
    // TODO escapeGroupName 待确认
    groupPnsNode.setEscapeGroupName(gp.getGroupName());
    // TODO 没有处理的数据 count;
    groupPsnNodeDao.save(groupPnsNode);
  }

  /**
   * 提取群组名称首字母
   * 
   * @param groupName
   * @return
   */
  private Character getFirstLetterByName(String groupName) {
    if (groupName != null && groupName.length() > 0) {
      HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
      format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
      char[] names = groupName.trim().toCharArray();
      for (char c : names) {
        if (Character.isLetter(c)) {
          if (String.valueOf(c).matches("^[a-zA-Z]{1}$")) {
            return Character.toUpperCase(c);
          }
          try {
            String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
            if (pinyin != null && pinyin.length > 0) {
              return (StringUtils.upperCase((pinyin[0])).toCharArray())[0];
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
    return null;
  }

  /**
   * 同步冗余字段
   * 
   * @param groupPsn
   * @param groupInvitePsnNodeList
   */
  private void syncGroupPsn(GroupPsn groupPsn, List<GroupInvitePsnNode> groupInvitePsnNodeList) {
    if (groupInvitePsnNodeList != null && groupInvitePsnNodeList.size() > 0) {
      for (GroupInvitePsnNode groupInvitePsnNode : groupInvitePsnNodeList) {
        groupInvitePsnNode.setNodeId(groupPsn.getGroupNodeId());
        groupInvitePsnNode.setGroupName(groupPsn.getGroupName());
        Character c = this.getFirstLetterByName(groupPsn.getGroupName());
        groupInvitePsnNode.setFirstLetter(c != null ? c.toString() : "0");
        groupInvitePsnNode.setGroupDescription(groupPsn.getGroupDescription());
        groupInvitePsnNode.setGroupCategory(groupPsn.getGroupCategory());
        groupInvitePsnNode.setSumFiles(groupPsn.getSumFiles());
        groupInvitePsnNode.setSumPubs(groupPsn.getSumPubs());
        groupInvitePsnNode.setSumPrjs(groupPsn.getSumPrjs());
        groupInvitePsnNode.setSumMembers(groupPsn.getSumMembers() == null ? 0 : groupPsn.getSumMembers());
        groupInvitePsnNode.setSumToMembers(groupPsn.getSumToMembers());
        groupInvitePsnNode.setSumRefs(groupPsn.getSumRefs());
        groupInvitePsnNode.setSumSubjects(groupPsn.getSumSubjects());
        groupInvitePsnNode.setOwnerPsnId(groupPsn.getOwnerPsnId());
        groupInvitePsnNode.setOpenType(groupPsn.getOpenType());
        groupInvitePsnNode.setGroupImgUrl(groupPsn.getGroupImgUrl());
        groupInvitePsnNode.setCreateDate(groupPsn.getCreateDate());
        groupInvitePsnNode.setLastVisitDate(groupPsn.getUpdateDate());
        groupInvitePsnNode.setIsDiscuss(groupPsn.getIsDiscuss());
        groupInvitePsnNode.setIsGroupMemberView(groupPsn.getIsGroupMemberView());
        groupInvitePsnNode.setIsMaterialView(groupPsn.getIsMaterialView());
        groupInvitePsnNode.setIsPrjView(groupPsn.getIsPrjView());
        groupInvitePsnNode.setIsPubView(groupPsn.getIsPubView());
        groupInvitePsnNode.setIsRefView(groupPsn.getIsRefView());
        groupInvitePsnNode.setIsShareFile(groupPsn.getIsShareFile());
        groupInvitePsnNode.setIsWorkView(groupPsn.getIsWorkView());

        groupInvitePsnNodeDao.save(groupInvitePsnNode);
      }
    } else {
      // 创建群组保存记录到GROUP_INVITE_PSN_NODE表_lhd_SCM-10725
      GroupInvitePsnNode groupInvitePsnNode = new GroupInvitePsnNode();
      GroupInvitePsnNodePk pk = new GroupInvitePsnNodePk(groupPsn.getGroupId(), groupPsn.getOwnerPsnId());
      groupInvitePsnNode.setId(pk);
      groupInvitePsnNode.setNodeId(groupPsn.getGroupNodeId());
      groupInvitePsnNode.setGroupName(groupPsn.getGroupName());
      Character c = this.getFirstLetterByName(groupPsn.getGroupName());
      groupInvitePsnNode.setFirstLetter(c != null ? c.toString() : "0");
      groupInvitePsnNode.setGroupDescription(groupPsn.getGroupDescription());
      groupInvitePsnNode.setGroupCategory(groupPsn.getGroupCategory());
      groupInvitePsnNode.setSumFiles(groupPsn.getSumFiles());
      groupInvitePsnNode.setSumPubs(groupPsn.getSumPubs());
      groupInvitePsnNode.setSumPrjs(groupPsn.getSumPrjs());
      groupInvitePsnNode.setSumMembers(groupPsn.getSumMembers() == null ? 0 : groupPsn.getSumMembers());
      groupInvitePsnNode.setSumToMembers(groupPsn.getSumToMembers());
      groupInvitePsnNode.setSumRefs(groupPsn.getSumRefs());
      groupInvitePsnNode.setSumSubjects(groupPsn.getSumSubjects());
      groupInvitePsnNode.setOwnerPsnId(groupPsn.getOwnerPsnId());
      groupInvitePsnNode.setOpenType(groupPsn.getOpenType());
      groupInvitePsnNode.setGroupImgUrl(groupPsn.getGroupImgUrl());
      groupInvitePsnNode.setCreateDate(groupPsn.getCreateDate());
      groupInvitePsnNode.setLastVisitDate(groupPsn.getUpdateDate());
      groupInvitePsnNode.setIsDiscuss(groupPsn.getIsDiscuss());
      groupInvitePsnNode.setIsGroupMemberView(groupPsn.getIsGroupMemberView());
      groupInvitePsnNode.setIsMaterialView(groupPsn.getIsMaterialView());
      groupInvitePsnNode.setIsPrjView(groupPsn.getIsPrjView());
      groupInvitePsnNode.setIsPubView(groupPsn.getIsPubView());
      groupInvitePsnNode.setIsRefView(groupPsn.getIsRefView());
      groupInvitePsnNode.setIsShareFile(groupPsn.getIsShareFile());
      groupInvitePsnNode.setIsWorkView(groupPsn.getIsWorkView());
      groupInvitePsnNode.setGroupRole("1");// 创建人群组角色为1
      groupInvitePsnNodeDao.save(groupInvitePsnNode);
    }
  }

  /**
   * 删除好友变化监听信息
   * 
   * @throws Exception
   */
  private void delGroupInvitePsnFriend(List<Long> groupIdList) throws Exception {
    if (groupIdList != null && groupIdList.size() > 0) {
      for (Long psnId : groupIdList) {
        if (psnId != 0L) {
          groupInvitePsnFriendDao.deleteGroupInvitePsnFriendByPsnId(psnId);
        }
      }
    }
  }



}
