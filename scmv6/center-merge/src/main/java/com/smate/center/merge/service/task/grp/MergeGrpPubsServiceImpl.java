package com.smate.center.merge.service.task.grp;

import com.smate.center.merge.dao.grp.GrpPubsDao;
import com.smate.center.merge.model.sns.grp.GrpPubs;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 群组合并->群组成果 （我加入的群组处理）.
 * 
 * @author ajb
 * 
 *
 */
@Transactional(rollbackFor = Exception.class)
public class MergeGrpPubsServiceImpl extends MergeBaseService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GrpPubsDao grpPubsDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<GrpPubs> list = grpPubsDao.getGrpPubsByPsnId(delPsnId);
    if (CollectionUtils.isEmpty(list)) {
      // 删除帐号没有需要合并的成果
      return false;
    }
    return true;
  }

  /**
   * 处理删除人群组成果.
   */
  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<GrpPubs> list = grpPubsDao.getGrpPubsByPsnId(delPsnId);
      for (GrpPubs grpPubs : list) {
        try {
          // 保存备份记录
          String desc = "合并群组成员 表，群组成果将被替换成保留人id V_GRP_PUBS ";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_MERGE, grpPubs);
          grpPubs.setCreatePsnId(savePsnId);
          grpPubsDao.save(grpPubs);
          // 更新备份数据状态
          super.updateAccountsMergeDataStatus(accountsMergeData);
        } catch (Exception e) {
          logger.error("帐号合并->群组合并->群组成果合并 出错    grpId=" + grpPubs.getGrpId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
          throw new Exception("帐号合并->群组合并->群组成果合并 出错     grpId=" + grpPubs.getGrpId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("帐号合并->群组合并->群组成果合并 出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并->群组合并->群组成果合并 出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }

}
