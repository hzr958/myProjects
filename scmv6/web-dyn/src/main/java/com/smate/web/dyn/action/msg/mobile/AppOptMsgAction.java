package com.smate.web.dyn.action.msg.mobile;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.client.RestTemplate;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.service.msg.OptMsgService;
import com.smate.web.dyn.service.msg.ShowMsgService;
import com.smate.web.dyn.service.share.ShareStatisticsService;

/**
 * app消息操作
 * 
 * @author LJ
 *
 *         2017年9月28日
 */
public class AppOptMsgAction extends ActionSupport implements Preparable, ModelDriven<MsgShowForm> {
  private static final long serialVersionUID = 8734956948650987766L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MsgShowForm form;
  @Resource
  private ShowMsgService showMsgService;
  @Resource
  private OptMsgService optMsgService;
  @Resource
  private ShareStatisticsService shareStatisticsService;
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private FileDownloadUrlService fileDownloadUrlService;
  private String fileType;

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  /**
   * 获取站内信文件下载地址
   * 
   * @return
   */
  @Action("/app/dynweb/getfileDownLoadUrl")
  @Deprecated
  public String getPubFulltextDownLoad() {
    FileTypeEnum fileEnum;
    Long searchId = 0L;
    Map<String, Object> map = new HashMap<String, Object>();
    if (form.getPubId() != null || form.getFileId() != null) {

      try {
        if (fileType.equals("file")) {
          fileEnum = FileTypeEnum.PSN;
          searchId = form.getFileId();
        } else if (fileType.equals("pub") || fileType.equals("fulltext")) {
          fileEnum = FileTypeEnum.SNS_FULLTEXT;
          searchId = form.getPubId();
        } else if (fileType.equals("pdwhpub")) {
          fileEnum = FileTypeEnum.PDWH_FULLTEXT;
          searchId = form.getPubId();
        } else if (fileType.equals("groupfile")) {
          fileEnum = FileTypeEnum.GROUP;
          searchId = form.getFileId();
        } else {
          throw new Exception("文件类型错误");
        }
        String downloadUrl = fileDownloadUrlService.getShortDownloadUrl(fileEnum, searchId);

        map.put("downloadUrl", downloadUrl);
        map.put("result", "success");
        status = IOSHttpStatus.OK;
      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        map.put("result", "error");
        logger.error("获取站内信文件下载地址出错", e);
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;
      map.put("result", "error");
    }
    AppActionUtils.renderAPPReturnJson(map, total, status);
    return null;
  }

  /**
   * 成果分享给好友
   * 
   * @return
   */
  @Action("/app/dynweb/showmsg/ajaxsendpubsharetofriend")
  public String ajaxSendPubShareToFriend() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      if (StringUtils.isNotBlank(form.getDes3ReceiverIds()) && form.getPubIds() != null) {
        optMsgService.sendPubShareToFriend(form);
        for (Long p : form.getPubIds()) {
          shareStatisticsService.addBatchShareRecord(form.getPsnId(), 1, p);
        }
        map.put("result", "success");
        map.put("msg", iszhCN ? "成果分享成功" : "Shared successfully");
        status = IOSHttpStatus.OK;
      } else {
        status = IOSHttpStatus.BAD_REQUEST;
        map.put("result", "error");
        map.put("msg", iszhCN ? "系统错误，请稍候再试" : "System error occured, please try again later");
      }

    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;

      map.put("result", "error");
      map.put("msg", iszhCN ? "成果分享失败" : "Shared failed");
      logger.error("成果分享异常,pubId=" + form.getPubId(), e.toString());
    }
    AppActionUtils.renderAPPReturnJson(map, total, status);
    return null;
  }

  /**
   * 发送消息
   * 
   * @return
   */
  @Action("/app/dynweb/ajaxsendmsg")
  public String ajaxSendMsg() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      showMsgService.sendMsg(form);
      map.put("msgRelationId", form.getMsgRelationId());
      /*
       * if (form.getMsgRelationId() > 0L) { map.put("result", "success"); } else { map.put("result",
       * "error"); }
       */
      if (form.getNotPermissionPsnIds() != null && form.getNotPermissionPsnIds().size() > 0) {
        map.put("status", "noPermit");
        map.put("msg", getText("dyn.msg.center.notSentMsg"));
      } else {
        map.put("msgRelationId", form.getMsgRelationId());
        map.put("status", "success");
        // 发送邮件通知
        showMsgService.sendInsideMsgEmail(form);
      }
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      map.put("result", "error");
      logger.error("发送消息出错,form=" + form, e);
    }
    AppActionUtils.renderAPPReturnJson(map, total, status);
    return null;
  }

  /**
   * 创建会话
   * 
   * @return
   */
  @Action("/app/dynweb/ajaxcreatemsgchat")
  public String createMsgChat() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      showMsgService.createMsgChat(form);
      map.put("result", "success");
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      map.put("result", "error");
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("创建会话出错,form=" + form, e);
    }
    AppActionUtils.renderAPPReturnJson(map.get("result"), total, status);
    return null;
  }

  /**
   * 删除站内信会话
   * 
   * @return
   */
  @Action("/app/dynweb/ajaxdelmsgchatrelation")
  public String delMsgChatRelation() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      showMsgService.delMsgChatRelation(form);
      map.put("result", "success");
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      map.put("result", "error");
      logger.error("删除站内信会话出错,form=" + form, e);
    }
    AppActionUtils.renderAPPReturnJson(map.get("result"), total, status);
    return null;
  }

  /**
   * 全文请求接受/忽略
   * 
   * @return
   */
  @Action("/app/dynweb/ajaxoptfulltextrequest")
  public String ajaxOptFulltextRequest() {
    /*
     * Map<String, String> map = new HashMap<String, String>(); try {
     * showMsgService.optFulltextRequest(form); status = IOSHttpStatus.OK; map.put("result", "success");
     * } catch (Exception e) { status = IOSHttpStatus.INTERNAL_SERVER_ERROR; map.put("result", "error");
     * logger.error("全文请求接受/忽略,form=" + form, e); }
     * AppActionUtils.renderAPPReturnJson(map.get("result"), total, status);
     */

    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = form.getPubId();
    Integer operate = form.getDealStatus();
    try {
      boolean flag = false; // 参数检测正确标志
      if (!NumberUtils.isNullOrZero(pubId) && !NumberUtils.isNullOrZero(form.getMsgRelationId()) && operate != null
          && NumberUtils.isNotZero(operate) && !NumberUtils.isNullOrZero(psnId)) {
        if (operate == 1 || operate == 2 || operate == 3) {
          flag = true;
        }
      }
      if (flag) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("currentPsnId", psnId);
        paramMap.put("pubId", pubId);
        paramMap.put("dealStatus", operate);
        paramMap.put("msgId", form.getMsgRelationId());

        map = restTemplate.postForObject(domainscm + "/data/pub/fulltext/ajaxrequpdate", paramMap, Map.class);
        status = IOSHttpStatus.OK;
      } else {
        map.put("errmsg", "参数校验不通过");
        map.put("status", "error");
        status = IOSHttpStatus.PARAM_ERROR;
      }

    } catch (Exception e) {
      logger.error("移动端处理成果全文请求出错， psnId = " + psnId + ", pubId = " + pubId + ", operate = " + operate, e);
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      map.put("status", "error");
    }
    AppActionUtils.renderAPPReturnJson(map.get("status"), total, status);
    return null;
  }

  @Override
  public MsgShowForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new MsgShowForm();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

}
