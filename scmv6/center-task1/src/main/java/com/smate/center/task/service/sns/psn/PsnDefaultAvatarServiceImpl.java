package com.smate.center.task.service.sns.psn;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.person.avatars.PersonAvatarsUtils;

@Service("psnDefaultAvatarService")
@Transactional(rollbackFor = Exception.class)
public class PsnDefaultAvatarServiceImpl implements PsnDefaultAvatarService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  @Autowired
  private PersonDao personDao;
  @Value("${file.root}")
  private String rootPath;
  @Value("${domainscm}")
  private String domainscm;
  private static Integer jobType = TaskJobTypeConstants.GeneratePsnDefaultAvatarsTask;

  @Override
  public void startGenerateAvatars(Long psnId) throws Exception {
    Person person = personDao.get(psnId);

    if (person == null) {
      this.updateTaskStatus(psnId, 2, "获取到的人员信息为空");
      return;
    }
    String avatarsStr = person.getAvatars();
    // 如果人员已经手动换了头像则不处理

    if (this.checkNeedUpdate(avatarsStr) == false) {
      this.updateTaskStatus(psnId, 1, "该人员头像已经不是默认头像，可能用户已经手动更新！");
      return;

    }
    String avatarspath = null;
    // 随机生成头像
    if (StringUtils.isNotBlank(person.getFirstName()) || StringUtils.isNotBlank(person.getLastName())) {
      String a = person.getFirstName() != null ? person.getFirstName().substring(0, 1).toUpperCase() : "";
      String b = person.getLastName() != null ? person.getLastName().substring(0, 1).toUpperCase() : "";
      try {
        // SCM-15480
        String avatars = PersonAvatarsUtils.personAvatars(b + a, person.getPersonId(), rootPath + "/avatars");
        avatarspath = domainscm + "/avatars" + avatars;
      } catch (Exception e) {
        logger.error("根据英文名随机产生默认头像失败!");
        this.updateTaskStatus(psnId, 2, "根据英文名随机产生默认头像失败!");
        return;
      }
    } else {
      this.updateTaskStatus(psnId, 2, "获取到的人员lastname和firstname都为空");
      return;
    }
    // 更新头像地址
    personDao.updateAvatarsByPsnId(psnId, avatarspath);
    this.updateTaskStatus(psnId, 1, "");
  }

  @Override
  public void updateTaskStatus(Long psnId, int status, String err) {
    try {
      tmpTaskInfoRecordDao.updateTaskStatus(psnId, status, err, jobType);
    } catch (Exception e) {
      logger.error("更新任务状态记录出错！handleId,status,jobtype=" + psnId + ",status" + ",jobtype", e);
    }
  }

  @Override
  public List<Long> getNeedTohandleList(int batchSize) throws Exception {
    return tmpTaskInfoRecordDao.getbatchhandleIdList(batchSize, jobType);
  }

  /**
   * 判断是否需要更新头像
   * 
   * @param avatarsStr
   * @return
   */
  public boolean checkNeedUpdate(String avatarsStr) {
    if (StringUtils.isEmpty(avatarsStr)) {
      return true;
    }
    if (avatarsStr.contains("logo_psndefault.png")) {
      return true;
    }
    if (avatarsStr.contains("head_nan_photo.jpg")) {
      return true;
    }
    if (avatarsStr.contains("head_nv_photo.jpg")) {
      return true;
    }
    if (avatarsStr.contains("?A=D")) {
      return true;
    }
    return false;

  }
}
