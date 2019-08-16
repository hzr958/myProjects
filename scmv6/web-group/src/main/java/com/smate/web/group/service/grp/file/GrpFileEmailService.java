package com.smate.web.group.service.grp.file;

import com.smate.web.group.model.grp.file.GrpFile;


/**
 * 群组文文件，邮件
 * 
 * @author aijiangbin
 *
 */
public interface GrpFileEmailService {

  public void sendUploadFileEamilNotify(GrpFile grpFile, Integer fileCount) throws Exception;

}
