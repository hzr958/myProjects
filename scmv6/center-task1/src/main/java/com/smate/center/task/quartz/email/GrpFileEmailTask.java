package com.smate.center.task.quartz.email;

import java.util.Date;
import java.util.List;

import org.objectweb.asm.tree.TryCatchBlockNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.dao.sns.grp.EmailGrpFilePsnDao;
import com.smate.center.task.model.grp.EmailGrpFilePsn;
import com.smate.center.task.model.sns.quartz.GrpFile;
import com.smate.center.task.service.group.GrpFileAddEmailService;

/**
 * 群组文件邮件任务， 每天只发一封邮件
 * 
 * @author aijiangbin
 *
 */
public class GrpFileEmailTask extends TaskAbstract {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpFileAddEmailService grpFileAddEmailService;



  public GrpFileEmailTask() {}

  public GrpFileEmailTask(String beanName) {
    super(beanName);
  }

  public void run() {

    try {
      logger.info("执行群组添加文件发送邮件任务");
      if (!super.isAllowExecution()) {
        return;
      }
      // 群组成员为群组添加文件或成果时，要发邮件通知，每个群组一天只有第一次通知，后面就不用通知了
      List<Long> grpIds = grpFileAddEmailService.findTodayUploadFileGrpId();
      if (grpIds == null || grpIds.size() == 0) {
        return;
      }
      for (Long grpId : grpIds) {
        GrpFile grpFile = grpFileAddEmailService.findTodayUplaodGrpPubsByGrpId(grpId);
        if (grpFile != null) {
          EmailGrpFilePsn eg = null;
          eg = new EmailGrpFilePsn();
          eg.setCreateDate(new Date());
          eg.setGrpId(grpFile.getGrpId());
          eg.setPsnId(grpFile.getUploadPsnId());
          eg.setGrpFileId(grpFile.getGrpFileId());
          eg.setStatus("0");
          eg.setFileModuleType(grpFile.getFileModuleType());
          grpFileAddEmailService.saveEmailGrpFilePsn(eg);
          try {
            grpFileAddEmailService.sendUploadFileEamilNotify(grpFile);
            eg.setStatus("1");
            grpFileAddEmailService.saveEmailGrpFilePsn(eg);
          } catch (Exception e) {
            eg.setStatus("99");
            grpFileAddEmailService.saveEmailGrpFilePsn(eg);
            throw new Exception("上传群组文件，给组员发送邮件，异常", e);
          }
        }
      }

    } catch (Exception e) {
      logger.error("GrpFileEmailTask--新增群组文件任务处理任务出错", e);
    }
  }



}
