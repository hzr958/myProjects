package com.smate.center.merge.service.task.grp;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.grp.GrpMemberDao;
import com.smate.center.merge.dao.grp.GrpStatisticsDao;
import com.smate.center.merge.model.sns.grp.GrpMember;
import com.smate.center.merge.model.sns.grp.GrpStatistics;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;

/**
 * 群组合并->群组群组统计服务
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class MergeGrpStatisticsServiceImpl extends MergeBaseService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private GrpStatisticsDao grpStatisticsDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    // 查找当前人所有群组
    List<GrpMember> list = grpMemberDao.getGrpByPsnId(savePsnId);
    if (CollectionUtils.isEmpty(list)) {
      // 删除帐号没有需要合并的群组 ， 有群组才要修改统计数
      return false;
    }
    return true;
  }



  /**
   * 处理群组统计表表
   */
  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<GrpMember> list = grpMemberDao.getGrpByPsnId(savePsnId);
      for (GrpMember grpMember : list) {
        try {
          GrpStatistics grpStatistics = grpStatisticsDao.get(grpMember.getGrpId());
          if (grpStatistics == null) {
            grpStatistics = new GrpStatistics();
            grpStatistics.setGrpId(grpMember.getGrpId());
          }
          // 保存备份记录
          String desc = "合并群组 ,群组统计，群组成员将被替换成保留人id  V_GRP_FILE ";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_MERGE, grpStatistics);
          // 修改群组文件统计数上传者
          Integer count = grpMemberDao.countGrpMenberNum(grpMember.getGrpId());
          grpStatistics.setSumMember(count);
          grpStatisticsDao.save(grpStatistics);
          // 更新备份数据状态
          super.updateAccountsMergeDataStatus(accountsMergeData);
        } catch (Exception e) {
          logger.error("帐号合并->群组合并->群组统计出错    grpId=" + grpMember.getGrpId() + ", savePsnId=" + savePsnId + ",delPsnId="
              + delPsnId, e);
          throw new Exception("帐号合并->群组合并->群组统计 出错     grpId=" + grpMember.getGrpId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("帐号合并->群组合并->群组统计 出错 savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并->群组合并->群组统计 出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }

}
