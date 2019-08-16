package com.smate.center.merge.service.task.grp;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.grp.GrpBaseinfoDao;
import com.smate.center.merge.dao.grp.GrpProposerDao;
import com.smate.center.merge.model.sns.grp.GrpBaseinfo;
import com.smate.center.merge.model.sns.grp.GrpProposer;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;

/**
 * 群组合并->删除保留帐号 在 删除帐号所拥有群组的请求记录
 * 
 * @author yhx
 *
 */
@Transactional(rollbackFor = Exception.class)
public class MergeGrpProposerServiceImpl extends MergeBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpBaseinfoDao grpBaseinfoDao;
  @Autowired
  private GrpProposerDao grpProposerDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<GrpProposer> list = grpProposerDao.queryGrpReq(savePsnId);
    if (CollectionUtils.isEmpty(list)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<GrpProposer> list = grpProposerDao.queryGrpReq(savePsnId);
      List<GrpBaseinfo> baseList = grpBaseinfoDao.getGrpByOwnerPsnId(delPsnId);
      for (GrpProposer grpProposer : list) {
        try {
          for (GrpBaseinfo grpBaseinfo : baseList) {
            // SCM-24596
            if (grpProposer.getGrpId().equals(grpBaseinfo.getGrpId())) {
              // 保存备份记录
              String desc = "删除群组请求记录 表，处理保留帐号在删除帐号所拥有群组的请求记录 V_GRP_PROPOSER";
              AccountsMergeData accountsMergeData =
                  super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_DEL, grpProposer);
              grpProposerDao.delete(grpProposer);
              // 更新备份数据状态
              super.updateAccountsMergeDataStatus(accountsMergeData);
            }
          }
        } catch (Exception e) {
          logger.error("帐号合并->群组合并->删除保留帐号在删除帐号所拥有群组的请求记录  出错   grpId=" + grpProposer.getGrpId() + ", savePsnId="
              + savePsnId + ",delPsnId=" + delPsnId, e);
          throw new Exception("帐号合并->群组合并->删除保留帐号在删除帐号所拥有群组的请求记录  出错   grpId=" + grpProposer.getGrpId() + ", savePsnId="
              + savePsnId + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("帐号合并->群组合并->删除群组的请求记录 出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并->群组合并->删除群组的请求记录 出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}
