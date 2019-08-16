package com.smate.web.psn.service.share;



import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.share.FileShareForm;



/**
 * 查询分享文件服务接口.
 * 
 * @author pwl
 * 
 */
public interface FileShareQueryService extends Serializable {


  Page<FileShareForm> getFileShareDataInSendSide1(Long resSendId, Long resReveiverId, Long baseId,
      Page<FileShareForm> page) throws ServiceException;

  /**
   * 判断分享状态
   * 
   * @param baseId
   * @return
   */
  int checkNewShareStatus(Long baseId);

  /**
   * 判断群组文件分享状态
   * 
   * @param baseId
   * @return
   */
  public int checkGrpFileShareStatus(Long baseId);

  /**
   * 群组文件分享数据
   * 
   * @param resSendId
   * @param resReveiverId
   * @param baseId
   * @param page
   * @return
   */
  public Page<FileShareForm> getGrpFileShareDataInSendSide(Long resSendId, Long resReveiverId, Long baseId,
      Page<FileShareForm> page) throws ServiceException;
}
