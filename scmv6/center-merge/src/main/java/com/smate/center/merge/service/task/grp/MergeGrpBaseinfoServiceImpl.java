package com.smate.center.merge.service.task.grp;

import com.smate.center.merge.dao.grp.GrpBaseinfoDao;
import com.smate.center.merge.model.sns.grp.GrpBaseinfo;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 群组合并->群组基础信息表（属于我的群组关系处理）.
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class MergeGrpBaseinfoServiceImpl extends MergeBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpBaseinfoDao grpBaseinfoDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<GrpBaseinfo> list = grpBaseinfoDao.getGrpByOwnerPsnId(delPsnId);
    if (CollectionUtils.isEmpty(list)) {
      // 删除帐号没有需要合并的群组
      return false;
    }
    return true;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<GrpBaseinfo> list = grpBaseinfoDao.getGrpByOwnerPsnId(delPsnId);
      for (GrpBaseinfo grpBaseinfo : list) {
        try {
          // 保存备份记录
          String desc = "合并群组基础信息 表，群组拥有人替换为保留人id  V_GRP_BASEINFO";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_MERGE, grpBaseinfo);
          updateGrpOwnerPsn(savePsnId, grpBaseinfo);
          // 更新备份数据状态
          super.updateAccountsMergeDataStatus(accountsMergeData);
        } catch (Exception e) {
          logger.error("帐号合并->群组合并->群组主表群组拥有人修改 出错   grpId=" + grpBaseinfo.getGrpId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
          throw new Exception("帐号合并->群组合并->群组主表群组拥有人修改 出错   grpId=" + grpBaseinfo.getGrpId() + ", savePsnId="
              + savePsnId + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("帐号合并->群组合并->群组主表群组拥有人修改 出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并->群组合并->群组主表群组拥有人修改 出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }

  private void updateGrpOwnerPsn(Long savePsnId, GrpBaseinfo grpBaseinfo) {
    // 修改群组拥有人
    grpBaseinfo.setOwnerPsnId(savePsnId);
    grpBaseinfo.setUpdateDate(new Date());
    grpBaseinfoDao.save(grpBaseinfo);
  }
}
