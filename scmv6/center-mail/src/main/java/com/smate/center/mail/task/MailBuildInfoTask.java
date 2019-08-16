package com.smate.center.mail.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.center.mail.exception.FirstEmailSameException;
import com.smate.center.mail.exception.NotReceiveException;
import com.smate.center.mail.exception.TemplateTimeLimitException;
import com.smate.center.mail.model.MailDataInfo;
import com.smate.center.mail.service.MailBuildInfoService;
import com.smate.center.mail.service.build.MailBuildInfoExcuteService;

/**
 * 构建邮件中心服务
 * 
 * @author zzx
 *
 */
public class MailBuildInfoTask {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  private static final int MAIL_DISPATCH_SIZE = 100;
  @Autowired
  private MailBuildInfoService mailBuildInfoService;
  @Autowired
  private List<MailBuildInfoExcuteService> mailBuildInfoExcuteService;

  public void execute() {
    try {
      List<MailOriginalData> list = mailBuildInfoService.findMailOriginalDataList(MAIL_DISPATCH_SIZE);
      if (list != null && list.size() > 0) {
        for (MailOriginalData one : list) {
          try {
            MailDataInfo info = mailBuildInfoService.buildExcuteParam(one);
            for (MailBuildInfoExcuteService service : mailBuildInfoExcuteService) {
              service.excute(info);
            }
          } catch (TemplateTimeLimitException e) {
            one.setMsg(e.getMessage());
            mailBuildInfoService.buildFailedForTemplateTimeLimit(one);
            logger.error("构建邮件中心服务-模版发送频率限制!", e);
          } catch (FirstEmailSameException e) {
            one.setMsg(e.getMessage());
            mailBuildInfoService.buildFirstEmailSame(one);
            logger.error("构建邮件中心服务-收件人与发件人首要邮箱一致!", e);
          } catch (NotReceiveException e) {
            one.setMsg(e.getMessage());
            mailBuildInfoService.buildFailedForNotReceive(one);
            logger.error("构建邮件中心服务-mailId=" + one.getMailId() + "用户不接收此类邮件!", e);
          } catch (Exception e) {
            one.setMsg(e.getMessage());
            mailBuildInfoService.buildFailedForError(one);
            logger.error("构建邮件中心服务-mailId=" + one.getMailId() + "构建邮件信息出错!", e);
          }
        }
      }
      logger.info("构建邮件中心服务-构建邮件信息!");
    } catch (Exception e) {
      logger.error("构建邮件中心服务-构建邮件信息出错!", e);
    }
  }

}
