package com.smate.web.group.service.grp.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.group.action.grp.form.GrpMemberForm;
import com.smate.web.group.dao.grp.grpbase.GrpBaseInfoDao;
import com.smate.web.group.dao.grp.member.GrpMemberDao;
import com.smate.web.group.dao.grp.member.GrpProposerDao;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.model.grp.member.GrpProposer;

/**
 * 群组权限以及业务判断service实现类
 * 
 * @author zzx
 *
 */
@Service("grpRoleService")
@Transactional(rollbackFor = Exception.class)
public class GrpRoleServiceImpl implements GrpRoleService {
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private GrpProposerDao grpProposerDao;

  @Override
  public Integer getGrpRole(Long psnId, Long grpId) {
    Integer role = grpMemberDao.getRoleById(psnId, grpId);// 1=群组拥有者,2=管理员,3=组员
    if (role == null) {
      GrpProposer grpProposer = grpProposerDao.getGrpProposer(psnId, grpId, 1);
      if (grpProposer != null) {
        role = 4;// 申请中
      } else {
        role = 9; // 群组外成员
      }
    }
    return role;
  }

  @Override
  public boolean getGrpRoleForDelMember(GrpMemberForm form) {
    Integer currentRole = grpMemberDao.getRoleById(form.getPsnId(), form.getGrpId());
    form.setRole(currentRole);
    Integer operatorRole = grpMemberDao.getRoleById(form.getTargetPsnId(), form.getGrpId());
    form.setTargetRole(operatorRole);
    if (operatorRole != null && (currentRole == 1 || currentRole == 2) && (currentRole < operatorRole)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 自动退出群组操作
   * 
   * @param form
   * @return
   */
  @Override
  public boolean isAutoExit(GrpMemberForm form) {
    if (form.getPsnId().equals(form.getTargetPsnId())) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean checkRoleVisitGrp(Long psnId, Long grpId) throws Exception {
    if (grpBaseInfoDao.isExist(grpId)) {
      return true;
      /*
       * GrpBaseinfo grpBaseinfo = grpBaseInfoDao.get(grpId); Integer role = getGrpRole(psnId, grpId);//
       * 人员角色 1=群组拥有者,2=管理员, 3=组员, String openType = grpBaseinfo.getOpenType();// 群组公开类型 P=隐私群组， H= //
       * 半公开群组，O= 公开群组 Integer grpCategory = grpBaseinfo.getGrpCategory();// 群组类型 10:兴趣群组 ， // 11项目群组 //
       * 邀请记录 GrpProposer grpProposer = grpProposerDao.getByPsnIdAndGrpId(psnId, grpId, 2, 2);
       * 
       * if (grpCategory == 12) { // 兴趣群组不作权限判断 return true; } if ("O".equals(openType)) { return true; }
       * else if ("H".equals(openType)) { return true; } else if ("P".equals(openType) && (role == 1 ||
       * role == 2 || role == 3)) { return true;
       * 
       * } else if (grpProposer != null) { // 如果查看人 是被邀请人 则可以查看该群组 return true; }
       */
    }
    return false;
  }

  @Override
  public boolean IsisExistGrp(Long grpId) {
    return grpBaseInfoDao.isExist(grpId);
  }

}
