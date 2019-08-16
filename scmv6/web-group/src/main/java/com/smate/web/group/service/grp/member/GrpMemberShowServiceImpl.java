package com.smate.web.group.service.grp.member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.group.action.grp.form.GrpDiscussShowMemberInfo;
import com.smate.web.group.action.grp.form.GrpMainForm;
import com.smate.web.group.action.grp.form.GrpMemberForm;
import com.smate.web.group.dao.group.psn.FriendDao;
import com.smate.web.group.dao.grp.grpbase.GrpBaseInfoDao;
import com.smate.web.group.dao.grp.member.GrpMemberDao;
import com.smate.web.group.dao.grp.member.GrpMemberRcmdDao;
import com.smate.web.group.dao.grp.member.GrpProposerDao;
import com.smate.web.group.dao.grp.member.PsnScienceAreaDao;
import com.smate.web.group.dao.grp.pub.GrpPubsDao;
import com.smate.web.group.model.group.psn.PsnInfo;
import com.smate.web.group.model.grp.member.GrpMember;
import com.smate.web.group.model.grp.member.GrpMemberRcmd;
import com.smate.web.group.model.grp.member.GrpProposer;
import com.smate.web.group.model.grp.member.PsnScienceArea;

/**
 * 群组成员显示service实现类
 * 
 * @author zzx
 *
 */
@Service("grpMemberShowService")
@Transactional(rollbackFor = Exception.class)
public class GrpMemberShowServiceImpl implements GrpMemberShowService {
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private GrpProposerDao grpProposerDao;
  @Autowired
  private GrpMemberRcmdDao grpMemberRcmdDao;
  @Autowired
  private BuildPsnInfoService buildPsnInfoService;
  @Autowired
  private GrpRoleService grpRoleService;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private GrpPubsDao grpPubsDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public void getGrpMemberListForShow(GrpMemberForm form) {
    List<PsnInfo> psnInfoList = null;
    Integer grpRole = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
    form.setRole(grpRole);
    List<GrpMember> grpMemberList = grpMemberDao.getMembersBySearch(form);
    if (grpMemberList != null && grpMemberList.size() > 0) {
      psnInfoList = new ArrayList<PsnInfo>();
      for (GrpMember g : grpMemberList) {
        // TODO 数据构造
        PsnInfo psnInfo = buildPsnInfoService.buildPersonInfo(g.getPsnId(), 2);
        psnInfo.setRole(grpRoleService.getGrpRole(g.getPsnId(), form.getGrpId()));
        psnInfo.setIsFriend(friendDao.isFriend(form.getPsnId(), g.getPsnId()).intValue() > 0 ? 1 : 0);
        psnInfo.setIsGrpOwner((grpRole == 1 && (psnInfo.getRole() == 2 || psnInfo.getRole() == 3)) ? 1 : 0);
        psnInfo.setIsRemove(((grpRole == 1 || grpRole == 2) && (grpRole < psnInfo.getRole())) ? 1 : 0);
        psnInfo.setIsSetAdmin(getIsSetAdmin(grpRole, psnInfo.getRole()));
        psnInfo.setDes3PsnId(Des3Utils.encodeToDes3(g.getPsnId().toString()));
        psnInfo.setIsSelf(CommonUtils.compareLongValue(form.getPsnId(), g.getPsnId()) ? 1 : 0);
        psnInfoList.add(psnInfo);
      }
    }
    form.setPsnInfoList(psnInfoList);
  }

  @Override
  public void getGrpProposerListForShow(GrpMemberForm form) {
    List<PsnInfo> psnInfoList = null;
    form.setPsnCount(grpProposerDao.getProposerCount(form.getGrpId()).intValue());
    List<GrpProposer> grpProposerList = grpProposerDao.getProposerBySearch(form);
    if (grpProposerList != null && grpProposerList.size() > 0) {
      psnInfoList = new ArrayList<PsnInfo>();
      for (GrpProposer g : grpProposerList) {
        // TODO 数据构造
        PsnInfo psnInfo = buildPsnInfoService.buildPersonInfo(g.getPsnId(), 2);
        psnInfo.setDes3PsnId(Des3Utils.encodeToDes3(g.getPsnId().toString()));
        psnInfoList.add(psnInfo);
      }
    }
    form.setPsnInfoList(psnInfoList);
  }

  @Override
  public void getGrpRecommendListForShow(GrpMemberForm form) {
    List<PsnInfo> psnInfoList = null;
    List<GrpMemberRcmd> grpRecommendList = grpMemberRcmdDao.getRecommendBySearch(form);
    if (grpRecommendList != null && grpRecommendList.size() > 0) {
      psnInfoList = new ArrayList<PsnInfo>();
      for (GrpMemberRcmd g : grpRecommendList) {
        // TODO 数据构造
        PsnInfo psnInfo = buildPsnInfoService.buildPersonInfo(g.getRecommendPsnId(), 2);
        psnInfo.setDes3PsnId(Des3Utils.encodeToDes3(g.getRecommendPsnId().toString()));
        psnInfoList.add(psnInfo);
      }
    }
    form.setPsnInfoList(psnInfoList);
  }

  private Integer getIsSetAdmin(Integer cRole, Integer oRole) {
    if (cRole == 1 && oRole == 3) {
      return 1;// 设置管理员
    } else if (cRole == 1 && oRole == 2) {
      return 2;// 设置普通成员
    } else if (oRole == 1) {
      return 0;// 不显示
    }
    return 0;
  }

  @Override
  public void buildGrpMemberShowInfo(GrpMember grpMember, GrpMemberForm form) {
    // TODO Auto-generated method stub

  }

  @Override
  public void buildGrpProposerShowInfo(GrpProposer grpProposer, GrpMemberForm form) {
    // TODO Auto-generated method stub

  }

  @Override
  public void buildGrpRecommendShowInfo(GrpMemberRcmd grpRecommend, GrpMemberForm form) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<Long> getMyGrpIds(GrpMainForm form) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<GrpDiscussShowMemberInfo> getFiveMemberForGrpDiscuss(Long grpId, Long ownerPsnId) throws Exception {
    // TODO 破代码
    // 首先获取五个群组好友 ，
    List<GrpDiscussShowMemberInfo> memberInfoList = new ArrayList<GrpDiscussShowMemberInfo>();
    List<Long> notInPsnId = new ArrayList<Long>();
    List<Object[]> list = grpMemberDao.getGrpFriendMemberForDiscuss(grpId, ownerPsnId);
    if (list != null && list.size() > 0) {
      for (int i = 0; i < list.size(); i++) {
        GrpDiscussShowMemberInfo memberInfo = new GrpDiscussShowMemberInfo();
        memberInfo.setMemberId(NumberUtils.toLong(list.get(i)[0].toString()));
        memberInfo.setPubNum(NumberUtils.toInt(list.get(i)[1].toString()));
        notInPsnId.add(memberInfo.getMemberId());
        memberInfo.setIsFriend(true);
        memberInfoList.add(memberInfo);

      }
    }

    if (memberInfoList.size() < 5) {
      // 不足在获取其他 群组成员
      List<Object[]> list2 = grpMemberDao.getGrpMemberForDiscuss(grpId, ownerPsnId, notInPsnId);
      if (list2 != null && list2.size() > 0) {
        for (int i = 0; i < list2.size() && memberInfoList.size() < 5; i++) {
          GrpDiscussShowMemberInfo memberInfo = new GrpDiscussShowMemberInfo();
          memberInfo.setMemberId(NumberUtils.toLong(list2.get(i)[0].toString()));
          memberInfo.setPubNum(NumberUtils.toInt(list2.get(i)[1].toString()));
          notInPsnId.add(memberInfo.getMemberId());
          if (memberInfo.getMemberId().equals(ownerPsnId)) {
            memberInfo.setIsFriend(true); // 将自己设置成好友，页面就不要加好友了
          }
          memberInfoList.add(memberInfo);
        }
      }

    }
    // TODO 构建成员列表的信息
    if (memberInfoList != null && memberInfoList.size() > 0) {
      for (GrpDiscussShowMemberInfo g : memberInfoList) {
        // TODO 数据构造
        PsnInfo psnInfo = buildPsnInfoService.buildPersonInfo(g.getMemberId(), 1);
        g.setMemberAvatur(psnInfo.getPerson().getAvatars());
        g.setMemberName(psnInfo.getName());
        g.setInsName(psnInfo.getPerson().getInsName());
      }
    }

    return memberInfoList;
  }

  @Override
  public void getGrpMemberPubSum(GrpMemberForm form) {
    List<PsnInfo> psnInfoList = null;
    List<GrpMember> grpMemberList = grpMemberDao.getMembers(form);
    if (grpMemberList != null && grpMemberList.size() > 0) {
      psnInfoList = new ArrayList<PsnInfo>();
      for (GrpMember g : grpMemberList) {
        // TODO 数据构造
        PsnInfo psnInfo = buildPsnInfoService.buildPersonInfo(g.getPsnId(), 2);
        psnInfoList.add(psnInfo);
      }
    }
    Collections.sort(psnInfoList);
    form.setPsnInfoList(psnInfoList);
  }

  @Override
  public void getFriendListForShow(GrpMemberForm form) {
    List<PsnInfo> psnInfoList = null;
    List<Object> friendList = friendDao.getFriendList(form.getPsnId());
    if (friendList != null && friendList.size() > 0) {
      psnInfoList = new ArrayList<PsnInfo>();
      for (Object psnId : friendList) {
        PsnInfo psnInfo = buildPsnInfoService.buildPersonInfo(Long.parseLong(psnId.toString()), 2);
        psnInfo.setDes3PsnId(Des3Utils.encodeToDes3(psnId.toString()));
        if (form.getGrpId() != null && grpMemberDao.isGrpMember(Long.parseLong(psnId.toString()), form.getGrpId())) {
          psnInfo.setIsGrpMember(1);
        } else {
          psnInfo.setIsGrpMember(0);
        }
        psnInfoList.add(psnInfo);
      }
    }
    form.setPsnInfoList(psnInfoList);

  }

  @Override
  public void getChatPsnCard(GrpMemberForm form) {
    PsnInfo psnInfo = buildPsnInfoService.buildPersonInfo(form.getTargetPsnId(), 2);
    if (psnInfo != null) {
      form.setPsnInfoList(new ArrayList<PsnInfo>());
      psnInfo.setIsFriend(friendDao.isFriend(form.getPsnId(), form.getTargetPsnId()).intValue() > 0 ? 1 : 0);
      PsnProfileUrl profileUrl = psnProfileUrlDao.get(form.getTargetPsnId());
      if (profileUrl != null && StringUtils.isNotEmpty(profileUrl.getPsnIndexUrl())) {
        psnInfo.setPsnShortUrl(domainscm + "/P/" + profileUrl.getPsnIndexUrl());
      } else {
        String psnUrl =
            domainscm + "/psnweb/homepage/show?des3PsnId=" + Des3Utils.encodeToDes3(form.getTargetPsnId().toString());
        psnInfo.setPsnShortUrl(psnUrl);
      }
      List<PsnScienceArea> list = psnScienceAreaDao.findPsnScienceAreaList(form.getTargetPsnId(), 1);
      if (CollectionUtils.isNotEmpty(list)) {
        for (PsnScienceArea area : list) {
          if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
            area.setShowScienceArea(
                StringUtils.isNotBlank(area.getEnScienceArea()) ? area.getEnScienceArea() : area.getScienceArea());
          } else {
            area.setShowScienceArea(
                StringUtils.isNotBlank(area.getScienceArea()) ? area.getScienceArea() : area.getEnScienceArea());
          }
        }
        psnInfo.setPsnScienceAreaList(list);
      }
      form.getPsnInfoList().add(psnInfo);
    }
  }

  @Override
  public void findGrpPubPsnList(GrpMemberForm form) throws Exception {
    if (form.getGrpId() == null) {
      throw new Exception("群组主键为null");
    }
    if (form.getIsProjectPub() == null) {
      form.setIsProjectPub(0);// 默认为文献 2018-11-16 ajb
    }
    List<PsnInfo> psnInfoList = null;
    PsnInfo psnInfo = null;
    Long psnId = null;
    Integer pubSum = null;
    // 课程群组查找所有的 SCM-21613
    Integer grpCategory = grpBaseInfoDao.getGrpCatetory(form.getGrpId());
    if (grpCategory == 10) {
      form.setIsProjectPub(null);
    }
    List<Object[]> list = grpPubsDao.findImpPubMember(form.getGrpId(), form.getIsProjectPub());
    if (list != null && list.size() > 0) {
      psnInfoList = new ArrayList<PsnInfo>();
      for (Object[] objs : list) {
        if (objs != null && objs.length >= 2) {

          psnId = Long.parseLong(objs[0].toString());
          pubSum = Integer.parseInt(objs[1].toString());
          psnInfo = buildPsnInfoService.buildPersonInfo(psnId, 2);
          psnInfo.setPubSum(pubSum);

          psnInfoList.add(psnInfo);
        }
        form.setPsnInfoList(psnInfoList);
      }
    }

  }

  @Override
  public List<PsnInfo> queryGrpMembers(GrpMemberForm form) throws Exception {
    List<GrpMember> members = grpMemberDao.getMembers(form);
    List<PsnInfo> psnInfos = new ArrayList<PsnInfo>();
    if (members != null && members.size() > 0) {
      for (GrpMember member : members) {
        PsnInfo psnInfo = buildPsnInfoService.buildPersonInfo(member.getPsnId(), 2);
        psnInfos.add(psnInfo);
      }
    }
    return psnInfos;
  }

}
