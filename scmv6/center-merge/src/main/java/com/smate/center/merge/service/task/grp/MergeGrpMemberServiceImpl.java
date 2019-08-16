package com.smate.center.merge.service.task.grp;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.grp.GrpMemberDao;
import com.smate.center.merge.model.sns.grp.GrpMember;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;

/**
 * 群组合并->群组成员 （我加入的群组处理）
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class MergeGrpMemberServiceImpl extends MergeBaseService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GrpMemberDao grpMemberDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<GrpMember> list = grpMemberDao.getGrpByPsnId(delPsnId);
    if (CollectionUtils.isEmpty(list)) {
      // 删除帐号没有需要合并的群组
      return false;
    }
    return true;
  }

  /**
   * 处理群组成员表 (处理我加入的群组)
   */
  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<GrpMember> list = grpMemberDao.getGrpByPsnId(delPsnId);
      for (GrpMember grpMember : list) {
        try {
          // 保存备份记录
          String desc = "合并群组成员 表，群组成员将被替换成保留人id V_GRP_MEMBER ";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_MERGE, grpMember);
          // 判断保留人与删除人是否是同在一个群组 如果是 就保留权限最高的一个
          // 查保留人是否也在这个群组
          GrpMember saveGrpMember = grpMemberDao.getGrpMemberByGrpIdAndPsnId(savePsnId, grpMember.getGrpId());
          // 如果在
          if (saveGrpMember != null) {
            // 如果权限大于 删除人在群组的权限 就保存保留人的关系 删除删除人的关系
            if (saveGrpMember.getGrpRole() < grpMember.getGrpRole()) {
              grpMemberDao.delete(grpMember);
            } else {
              // 删除原关系
              grpMemberDao.deleteGrpMember(saveGrpMember);
              // 保留 删除帐号的关系
              grpMember.setPsnId(savePsnId);
              grpMemberDao.save(grpMember);
            }
          } else {
            // 保留 删除帐号的关系
            grpMember.setPsnId(savePsnId);
            grpMemberDao.save(grpMember);
          }
          // 更新备份数据状态
          super.updateAccountsMergeDataStatus(accountsMergeData);
        } catch (Exception e) {
          logger.error("帐号合并->群组合并->我加入的群组合并 出错    grpId=" + grpMember.getGrpId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
          throw new Exception("帐号合并->群组合并->我加入的群组合并 出错     grpId=" + grpMember.getGrpId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("帐号合并->群组合并->我加入的群组合并 出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并->群组合并->我加入的群组合并 出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }

}
