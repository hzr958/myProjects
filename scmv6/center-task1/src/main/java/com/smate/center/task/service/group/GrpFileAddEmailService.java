package com.smate.center.task.service.group;

import java.util.List;

import com.smate.center.task.model.grp.EmailGrpFilePsn;
import com.smate.center.task.model.sns.quartz.GrpFile;

/**
 * 群组文件 ，发送邮件服务
 * 
 * @author aijiangbin
 *
 */
public interface GrpFileAddEmailService {

  public List<Long> findTodayUploadFileGrpId() throws Exception;

  public GrpFile findTodayUplaodGrpPubsByGrpId(Long grpId) throws Exception;

  public void sendUploadFileEamilNotify(GrpFile grpFile) throws Exception;

  public void saveEmailGrpFilePsn(EmailGrpFilePsn emailGrpFilePsn) throws Exception;
}
