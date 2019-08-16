package com.smate.web.psn.service.file;

import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.FileMainForm;
import com.smate.web.psn.model.share.FileShareForm;

/**
 * 我的文件分享服务类
 * 
 * @author Administrator
 *
 */
public interface MyFileShareService {

  /**
   * 分享我的文件
   * 
   * @throws Exception
   */
  void shareMyFile(FileMainForm form) throws Exception;

  /**
   * 文件批量分享
   * 
   * @param form
   * @throws Exception
   */

  public void shareAllMyFiles(FileMainForm form) throws Exception;

  /**
   * 得到分享主表的主键
   * 
   * @throws Exception
   */
  void getPsnFileShareBaseId(FileMainForm form) throws Exception;

  /**
   * 判断分享状态
   * 
   * @param baseId
   * @return
   */
  int checkNewShareStatus(Long baseId);

  /**
   * 在接收端分页查找文件分享记录.
   * 
   * @param resSendId
   * @param page
   * @return
   * @throws ServiceException
   */
  void getFileShareDataInSendSide(Long resSendId, Long resReveiverId, Long baseId, FileMainForm form);

  /**
   * 群组分享文件的状态
   * 
   * @param baseId
   * @return
   */
  public int checkGrpFileShareStatus(Long baseId);

  /**
   * 群组分享文件的列表
   * 
   * @param resSendId
   * @param resReveiverId
   * @param baseId
   * @param form
   * @return
   * @throws ServiceException
   */
  public void getGrpFileShareDataInSendSide(Long resSendId, Long resReveiverId, Long baseId, FileMainForm form)
      throws Exception;

  /**
   * 通过邮件，分享给好友
   * 
   * @param form
   */
  public void shareAllMyFilesByEmails(FileMainForm form) throws Exception;
}
