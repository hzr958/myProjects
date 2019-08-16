package com.smate.center.merge.service.task.grp;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.grp.OpenGroupUnionDao;
import com.smate.center.merge.dao.grp.OpenGroupUnionHisDao;
import com.smate.center.merge.model.sns.grp.OpenGroupUnion;
import com.smate.center.merge.model.sns.grp.OpenGroupUnionHis;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;
import com.smate.core.base.utils.common.BeanUtils;

/**
 * 群组合并->群组关联记录表处理类
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class MergeGrpUnionServiceImpl extends MergeBaseService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OpenGroupUnionDao openGroupUnionDao;
  @Autowired
  private OpenGroupUnionHisDao openGroupUnionHisDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<OpenGroupUnion> list = openGroupUnionDao.findIdByOwnPsnId(delPsnId);
    if (CollectionUtils.isEmpty(list)) {
      // 删除帐号没有需要处理的数据
      return false;
    }
    return true;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<OpenGroupUnion> list = openGroupUnionDao.findIdByOwnPsnId(delPsnId);
      for (OpenGroupUnion openGroupUnion : list) {
        try {
          // 保存备份记录
          String desc = "清理群组关联表记录，移动到历史表， v_open_group_union";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_DEL, openGroupUnion);
          OpenGroupUnionHis openGroupUnionHis = new OpenGroupUnionHis();
          BeanUtils.copyProperties(openGroupUnionHis, openGroupUnion);
          openGroupUnionHis.setDelTime(new Date());
          openGroupUnionHis.setDelType("1");
          openGroupUnionHis.setStatus(0);
          openGroupUnionHisDao.save(openGroupUnionHis);
          openGroupUnionDao.delete(openGroupUnion);
          // 更新备份数据状态
          super.updateAccountsMergeDataStatus(accountsMergeData);

        } catch (Exception e) {
          logger.error("帐号合并-群组合并->处理群组关联表出错 resId=" + openGroupUnion.getGroupId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
          throw new Exception("帐号合并-群组合并->处理群组关联表出错 resId=" + openGroupUnion.getGroupId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("帐号合并-群组合并->处理群组关联表出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并-群组合并->处理群组关联表出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }

}
