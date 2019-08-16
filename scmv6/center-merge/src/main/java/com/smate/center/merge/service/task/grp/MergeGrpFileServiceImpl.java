package com.smate.center.merge.service.task.grp;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.grp.GrpFileDao;
import com.smate.center.merge.model.sns.grp.GrpFile;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;

/**
 * 群组合并->群组文件
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class MergeGrpFileServiceImpl extends MergeBaseService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GrpFileDao grpFileDao;


  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<GrpFile> list = grpFileDao.getGrpFileByPsnId(delPsnId);
    if (CollectionUtils.isEmpty(list)) {
      // 没有群组文件
      return false;
    }
    return true;
  }

  /**
   * 处理群组文件表 (处理我加入的群组)
   */
  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      List<GrpFile> list = grpFileDao.getGrpFileByPsnId(delPsnId);
      for (GrpFile grpFile : list) {
        try {
          // 保存备份记录
          String desc = "合并群组文件 表，群组成员将被替换成保留人id  V_GRP_FILE ";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_MERGE, grpFile);
          // 修改群组文件上传者
          grpFile.setUploadPsnId(savePsnId);
          grpFile.setUpdateDate(new Date());
          grpFileDao.save(grpFile);
          // 更新备份数据状态
          super.updateAccountsMergeDataStatus(accountsMergeData);
        } catch (Exception e) {
          logger.error("帐号合并->群组合并->我上传的群组文件合并 出错    grpId=" + grpFile.getGrpId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
          throw new Exception("帐号合并->群组合并->我加入的群组合并 出错     grpId=" + grpFile.getGrpId() + ", savePsnId=" + savePsnId
              + ",delPsnId=" + delPsnId, e);
        }
      }
    } catch (Exception e) {
      logger.error("帐号合并->群组合并->我上传的群组文件合并 出错 savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并->群组合并->我上传的群组文件合并 出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }

}
