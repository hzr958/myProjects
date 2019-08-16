package com.smate.center.merge.service.task.email;

/**
 * 合并 发送邮件服务类
 * 
 * @author yhx
 *
 */
public interface PsnMergeEmailNoticeService {
  /**
   * 合并失败 发送邮件给管理员
   * 
   * @throws Exception
   */
  public void sendEmailToAdmin(Long psnId);

  /**
   * 合并成功 发送邮件给被合并人
   * 
   * @param psnId
   * @throws Exception
   */
  public void sendEmailToPsn(Long psnId) throws Exception;
}
