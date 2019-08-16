package com.smate.center.task.service.sns.quartz;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.open.OpenGrpCodeTempDao;
import com.smate.center.task.dao.open.OpenUserUnionDao;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.dao.sns.quartz.GrpBaseInfoDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.group.OpenGrpCodeTemp;

/**
 * 
 * @author JunLi
 *
 *         2017年11月1日
 */
@Service("generateGroupCodeService")
@Transactional(rollbackFor = Exception.class)
public class GenerateGroupCodeServiceImpl implements GenerateGroupCodeService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private OpenGrpCodeTempDao openGrpCodeTempDao;
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  private static Integer jobType = TaskJobTypeConstants.GenerateGroupCodeTask;

  @Override
  public void updateTaskStatus(Long grpId, int status, String err) {
    try {
      tmpTaskInfoRecordDao.updateTaskStatus(grpId, status, err, jobType);
    } catch (Exception e) {
      logger.error("更新任务状态记录出错！handleId,status,jobtype=" + grpId + ",status" + ",jobtype", e);
    }
  }

  @Override
  public List<Long> getNeedTohandleList(int batchSize) throws Exception {
    return tmpTaskInfoRecordDao.getbatchhandleIdList(batchSize, jobType);
  }

  @Override
  public void startGenerateGrpCode(Long groupId) throws Exception {
    Long createPsnId = grpBaseInfoDao.get(groupId).getCreatePsnId();
    Long openId = 0L;
    try {
      openId = openUserUnionDao.getOpenIdByPsnId(createPsnId);
      // openid不为空则保存
      if (openId != null && openId != 0L) {
        // 生成groupCode
        String groupCode = DigestUtils.md5Hex(openId.toString() + "_" + groupId);
        // 存在记录则更新
        if (openGrpCodeTempDao.get(groupId) == null) {
          openGrpCodeTempDao.save(new OpenGrpCodeTemp(groupId, openId, groupCode));
          this.updateTaskStatus(groupId, 1, "groupCode生成成功");
        } else {
          openGrpCodeTempDao.updateGrpCodebyGrpId(groupId, openId, groupCode);
          this.updateTaskStatus(groupId, 1, "groupCode更新成功");
        }

      } else {
        this.updateTaskStatus(groupId, 2, "获取到的openId为空");
      }
    } catch (DaoException e) {
      logger.error("根据psnId从数据库获取openId异常");
      this.updateTaskStatus(groupId, 2, "根据psnId从数据库获取openId异常");
    }
  }

}
