package com.smate.web.group.service.group;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.group.dao.group.DisciplineDao;
import com.smate.web.group.dao.group.GroupBaseinfoDao;
import com.smate.web.group.dao.group.GroupFileDao;
import com.smate.web.group.dao.group.GroupFilterDao;
import com.smate.web.group.dao.group.GroupKeyDiscDao;
import com.smate.web.group.dao.group.GroupKeywordsDao;
import com.smate.web.group.dao.group.GroupPsnDao;
import com.smate.web.group.dao.group.GroupStatisticsDao;
import com.smate.web.group.dao.group.OpenGroupUnionDao;
import com.smate.web.group.dao.group.invit.GroupInvitePsnDao;
import com.smate.web.group.dao.group.pub.GroupPubsDao;
import com.smate.web.group.exception.GroupException;
import com.smate.web.group.exception.GroupNotExistException;
import com.smate.web.group.form.GroupInfoForm;
import com.smate.web.group.model.group.GroupBaseInfo;
import com.smate.web.group.model.group.GroupFile;
import com.smate.web.group.model.group.GroupKeyDisc;
import com.smate.web.group.model.group.GroupPsn;
import com.smate.web.group.model.group.invit.GroupInvitePsn;
import com.smate.web.group.model.group.psn.PsnInfo;
import com.smate.web.group.model.group.pub.Discipline;
import com.smate.web.group.service.group.psn.PsnInfoService;


/**
 * 群组信息服务实现类
 * 
 * @author zk
 *
 */
@Service("groupInfoService")
@Transactional(rollbackOn = Exception.class)
public class GroupInfoServiceImpl implements GroupInfoService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupPsnDao groupPsnDao;
  @Autowired
  private GroupStatisticsDao groupStatisticsDao;
  @Autowired
  private GroupBaseinfoDao groupBaseInfoDao;
  @Autowired
  private GroupKeywordsDao groupKeywordsDao;
  @Autowired
  private PsnInfoService psnInfoService;
  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;
  @Autowired
  private GroupPubsDao groupPubsDao;
  @Autowired
  private GroupBaseinfoDao groupBaseinfoDao;
  @Autowired
  private GroupFileDao groupFileDao;
  @Autowired
  private OpenGroupUnionDao openGroupUnionDao;
  @Autowired
  private GroupFilterDao groupFilterDao;
  @Autowired
  private GroupKeyDiscDao groupKeyDiscDao;
  @Autowired
  private DisciplineDao disciplineDao;
  @Autowired
  private GroupOptService groupOptService;
  @Autowired
  private PersonDao personDao;

  @Override
  public void getGroupMain(GroupInfoForm form) {
    // 判断群组是否被删除
    Boolean groupStatus = groupFilterDao.getCheckGroupStatus(form.getGroupId());
    if (groupStatus) {
      logger.error("访问的群组不存在" + form.getGroupId());
      throw new GroupNotExistException("访问的群组不存在" + form.getGroupId());
    }
    // 更新群组最后访问时间
    groupBaseinfoDao.updateLastVisitDate(form.getGroupId());
    GroupPsn groupPsn = groupPsnDao.findMyGroupName(form.getGroupId());
    // 防止上面查询到的结果为空
    if (groupPsn == null) {
      groupPsn = new GroupPsn();
      GroupBaseInfo groupBaseInfo = groupBaseinfoDao.getGroupNameAndImage(form.getGroupId());
      if (groupBaseInfo != null) {
        groupPsn.setGroupName(groupBaseInfo.getGroupName());
        groupPsn.setGroupCategory(groupBaseInfo.getGroupCategory());
        // 默认图片
        groupPsn.setGroupImgUrl(StringUtils.isNotBlank(groupBaseInfo.getGroupImgUrl()) ? groupBaseInfo.getGroupImgUrl()
            : "/resscmwebsns/images_v5/50X50g.gif");
        groupPsn.setGroupDescription(groupBaseInfo.getGroupDescription());
      }
    } else if (StringUtils.isBlank(groupPsn.getGroupImgUrl())) {
      // 当groupPsn没有默认图片时
      groupPsn.setGroupImgUrl("/resscmwebsns/images_v5/50X50g.gif");
    }
    String openType = groupFilterDao.getGroupOpenType(form.getGroupId());
    if (StringUtils.isNotBlank(openType)) {
      groupPsn.setOpenType(openType);
    }
    form.setGroupPsn(groupPsn);
    form.setGroupStatistics(groupStatisticsDao.getGroupStaticForTop(form.getGroupId()));
    // 获取群组研究领域
    GroupKeyDisc groupKeyDisc = groupKeyDiscDao.getGroupKeyDisc(form.getGroupId());
    if (groupKeyDisc != null) {
      if (groupKeyDisc.getCategoryId() != null) {
        try {
          Discipline discipline = disciplineDao.getTopDisciplineById(groupKeyDisc.getCategoryId());
          if (discipline != null) {
            if (StringUtils.isNotBlank(discipline.getTopCategoryZhName())) {
              form.setGroupCategory(discipline.getTopCategoryZhName());
            } else {
              form.setGroupCategory(discipline.getTopCategoryEnName());
            }
          }
        } catch (Exception e) {
          logger.error("群组头部-获取群组研究领域出错!", e);
        }
      }
    }
    // 处理角色
    form.setCurrentPsnGroupRoleStatus(groupOptService.getRelationWithGroup(form.getPsnId(), form.getGroupId()));
    // dealGroupRole(form);
    // 自己的信息
    Person person = personDao.findPersonBaseIncludeIns(SecurityUtils.getCurrentUserId());
    buildPersonInfo(person);
    form.setPerson(person);
  }



  @Override
  public void getGgroupIntroMain(GroupInfoForm form) {
    // 简介
    GroupBaseInfo groupBaseInfo = groupBaseInfoDao.getGroupBaseInfo(form.getGroupId());
    GroupPsn groupPsn = new GroupPsn();
    form.setGroupPsn(groupPsn);
    if (groupBaseInfo != null) {
      String brief = handleDescrAlabel(groupBaseInfo);
      form.getGroupPsn().setGroupDescription(brief);
    }
    // 关键词SCM-10830
    /*
     * List<GroupKeywords> groupKeywords = groupKeywordsDao.getGroupKeywords(form.getGroupId()) ;
     * if(groupKeywords!=null &&groupKeywords.size()>0){ String keywords = ""; // 从1开始 因为 0 是 群组名 for
     * (int i = 1 ; i<groupKeywords.size() ; i++) { if(i==groupKeywords.size()-1){ keywords +=
     * groupKeywords.get(i).getKeyword(); }else{ keywords += groupKeywords.get(i).getKeyword() + "; "; }
     * } form.getGroupPsn().setKeyWords(keywords); }
     */

    GroupKeyDisc keyDisc = this.getKeyDisc(form.getGroupId());
    if (keyDisc != null && StringUtils.isNotBlank(keyDisc.getKeyWords1())) {
      // 关键词添加空格
      String keyWords = keyDisc.getKeyWords1().replace(";", "; ");
      form.getGroupPsn().setKeyWords(keyWords);
    }
    // 5个成员
    form.setGroupInvitePsns(groupInvitePsnDao.findFiveMemers(form.getGroupId()));
    // 5个成果
    form.setGroupPubs(groupPubsDao.findFivePubs(form.getGroupId()));
  }


  // 构建姓名
  void buildPersonInfo(Person person) {
    if (person == null) {
      person = new Person();
      return;
    }
    if (StringUtils.isBlank(person.getName())) {
      person.setName(person.getFirstName() + person.getLastName());
    }
    if (StringUtils.isBlank(person.getEname())) {
      person.setEname(person.getFirstName() + person.getLastName());
    }
    if (StringUtils.isBlank(person.getAvatars())) {
      person.setAvatars("/avatars/head_nan_photo.jpg");
    }

  }


  /**
   * 获取群组学科关键词信息.
   * 
   * @param groupId
   * @return
   */
  public GroupKeyDisc getKeyDisc(Long groupId) {
    return groupKeyDiscDao.getGroupKeyDisc(groupId);
  }

  /**
   * // 群组角色状态 0 陌生人 没有申请 获取拒绝后可以再次申请 ， 1 ，陌生人 申请中 ， 2 是群组普通成员 3 群组管理成员
   * 
   * @param form
   */
  private void dealGroupRole(GroupInfoForm form) {
    GroupInvitePsn groupInvitePsn = groupInvitePsnDao.getGroupInvitePsn(form.getGroupId(), form.getPsnId());
    if (groupInvitePsn == null) {
      form.setCurrentPsnGroupRoleStatus(0);
    } else if ("1".equals(groupInvitePsn.getIsAccept())) {
      if ("1".equals(groupInvitePsn.getGroupRole()) || "2".equals(groupInvitePsn.getGroupRole())) {
        form.setCurrentPsnGroupRoleStatus(3);
      } else {
        form.setCurrentPsnGroupRoleStatus(2);
      }

    } else if ("0".equals(groupInvitePsn.getIsAccept())) {
      form.setCurrentPsnGroupRoleStatus(0);
    } else if ("2".equals(groupInvitePsn.getIsAccept()) && form.getCurrentPsnGroupRoleStatus() == 0) {
      form.setCurrentPsnGroupRoleStatus(0);
    } else {
      form.setCurrentPsnGroupRoleStatus(1);
    }

    if (groupInvitePsn != null) {
      form.setGroupInvitePsn(groupInvitePsn);
    }
  }

  // 对a标签进行正则替换成空
  public String handleDescrAlabel(GroupBaseInfo groupBaseInfo) {
    String desc = groupBaseInfo.getGroupDescription();
    if (StringUtils.isNotBlank(desc)) {
      String regEx_head = "<a[^>]*>"; // a的头部
      String regEx_toil = "</a>"; // a的尾部
      desc = desc.replaceAll(regEx_head, "");
      desc = desc.replaceAll(regEx_toil, "");
    }

    return desc;
  }



  @Override
  public void findGroupFile(GroupInfoForm form) throws GroupException {
    if (groupInvitePsnDao.isGroupAdmin(form.getGroupId(), form.getPsnId())) {
      form.setIsAdmin(1);
    } else {
      form.setIsAdmin(0);
    }
    String searchKey = StringEscapeUtils.unescapeHtml4(form.getSearchKey());
    form.setSearchKey(searchKey);
    groupFileDao.findGroupFile(form);
    this.handlePsnName(form);
  }

  /**
   * 处理人员姓名
   * 
   * @param form
   */
  @SuppressWarnings("unchecked")
  private void handlePsnName(GroupInfoForm form) {
    String locale = LocaleContextHolder.getLocale().toString();
    List<GroupFile> gfList = form.getPage().getResult();
    if (CollectionUtils.isNotEmpty(gfList)) {
      for (GroupFile gf : gfList) {
        if (gf.getPsnId() != null) {
          gf.setPsnName(psnInfoService.getPsnName(gf.getPsnId(), locale));
        }
      }
    }
  }

  /**
   * 获取群组成员列表
   */
  @Override
  public void getMemberList(GroupInfoForm form) throws GroupException {
    List<PsnInfo> psnInfoList = new ArrayList<PsnInfo>();
    // 获取群组成员psnId,角色,头像,名称
    List<GroupInvitePsn> groupMembersList = groupInvitePsnDao.getGroupMembers(form);
    if (CollectionUtils.isNotEmpty(groupMembersList)) {
      for (GroupInvitePsn groupInvitePsn : groupMembersList) {
        PsnInfo psnInfo = new PsnInfo();
        psnInfo.setPsnId(groupInvitePsn.getPsnId());// 人员psnId
        psnInfo.setInvitePsnId(groupInvitePsn.getInvitePsnId());// 群组与人员的关系ID
        psnInfo.setGroupRole(groupInvitePsn.getGroupRole());// 群组中的角色[1=创建人,2=管理员,
        // 3=组员]
        psnInfo.setAvatarUrl(groupInvitePsn.getAvatars());// 人员头像
        psnInfo.setName(groupInvitePsn.getPsnName());// 人员名称
        psnInfoService.packageGroupMemberInfo(psnInfo);
        psnInfoList.add(psnInfo);
      }
    }
    form.setPsnInfoList(psnInfoList);
  }

  /**
   * 获取群组成员待审核列表
   */
  @Override
  public void getPendingList(GroupInfoForm form) throws GroupException {
    List<PsnInfo> psnInfoList = new ArrayList<PsnInfo>();
    // 获取待审核群组成员invitePsnId,psnId,角色,头像,名称
    List<GroupInvitePsn> groupPendingsList = groupInvitePsnDao.getGroupPendings(form);
    if (CollectionUtils.isNotEmpty(groupPendingsList)) {
      for (GroupInvitePsn groupInvitePsn : groupPendingsList) {
        PsnInfo psnInfo = new PsnInfo();
        psnInfo.setInvitePsnId(groupInvitePsn.getInvitePsnId());// 群组与人员的关系ID
        psnInfo.setPsnId(groupInvitePsn.getPsnId());// 人员psnId
        psnInfo.setGroupRole(groupInvitePsn.getGroupRole());// 群组中的角色[1=创建人,2=管理员,
        // 3=组员]
        psnInfo.setAvatarUrl(groupInvitePsn.getAvatars());// 人员头像
        psnInfo.setName(groupInvitePsn.getPsnName());// 人员名称
        psnInfoService.packageGroupMemberInfo(psnInfo);
        psnInfoList.add(psnInfo);
      }
    }
    form.setPsnInfoList(psnInfoList);
  }


  /**
   * 获取当前人的群组角色
   */
  @Override
  public void getCurrentPsnGroupRole(GroupInfoForm form) throws GroupException {
    groupInvitePsnDao.getGroupRole(form);
    form.setCurrentPsnGroupRoleStatus(groupOptService.getRelationWithGroup(form.getPsnId(), form.getGroupId()));
    if (form.getCurrentPsnGroupRoleStatus() == 3 || form.getCurrentPsnGroupRoleStatus() == 4) {
      // 查询申请人数
      groupInvitePsnDao.findApplyPsn(form);
    }
  }



  @Override
  public Long groupCodeGetGroupId(GroupInfoForm form) throws GroupException {
    // TODO Auto-generated method stub
    return openGroupUnionDao.findGroupIdByGroupCode(form.getGroupCode());
  }


}
