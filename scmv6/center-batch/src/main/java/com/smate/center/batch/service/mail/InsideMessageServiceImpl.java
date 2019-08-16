package com.smate.center.batch.service.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.InsideMessageConstants;
import com.smate.center.batch.constant.MessageConstant;
import com.smate.center.batch.constant.PrivacyType;
import com.smate.center.batch.dao.sns.prj.FriendDao;
import com.smate.center.batch.dao.sns.pub.PrivacySettingsDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.InsideMailBox;
import com.smate.center.batch.model.psn.ConstMailTypeDictionary;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.center.batch.model.sns.pub.PrivacySettings;
import com.smate.center.batch.service.dynamic.DynamicShareService;
import com.smate.center.batch.service.psn.PsnMailSetService;
import com.smate.center.batch.service.pub.mq.SnsSyncMessageProducer;
import com.smate.center.batch.util.pub.BasicRmtSrvModuleConstants;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;

import net.sf.json.JSONObject;



/**
 * 短信Service实现，Context(应用场景)．
 * 
 * @author pwl
 * 
 */
@Service("insideMessageService")
@Transactional(rollbackFor = Exception.class)
public class InsideMessageServiceImpl implements InsideMessageService {


  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailBoxService mailBoxService;
  @Autowired
  private InboxService inboxService;
  @Autowired
  private PsnMailSetService psnMailSetService;
  @Autowired
  private DynamicShareService dynamicShareService;
  @Autowired
  private ScmEmailService scmEmailService;
  @Autowired
  private SnsSyncMessageProducer snsSyncMessageProducer;
  @Autowired
  private PrivacySettingsDao privacySettingsDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private InsideMailParamBuilder sharePubForGroupParamBuilder;
  @Autowired
  private InsideMailParamBuilder shareFileForGroupParamBuilder;
  @Autowired
  private CongratutionJobParamBuilder congratutionJobParamBuilder;
  @Autowired
  private InsideMailParamBuilder evaFriendParamBuilder;
  @Autowired
  private InsideMailParamBuilder cmdFriendParamBuilder;
  @Autowired
  private InsideMailParamBuilder shareCVParamBuilder;
  @Autowired
  private InsideMailParamBuilder shareFundParamBuilder;
  @Autowired
  private InsideMailParamBuilder shareJournalParamBuilder;
  @Autowired
  private InsideMailParamBuilder shareAreaParamBuilder;
  @Autowired
  private InsideMailParamBuilder shareHotWordsParamBuilder;
  @Autowired
  private InsideMailParamBuilder ordinaryInsideMsgParamBuilder;
  @Autowired
  private InsideMailParamBuilder shareGroupParamBuilder;
  @Autowired
  private FulltextReqInsideMsgParamBuilder fulltextReqInsideMsgParamBuilder;
  @Autowired
  private ReceiverService receiverService;
  @Autowired
  private CacheService cacheService;

  @Override
  public void sendMessage(Message message) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void sendMessageAndMail(Message message) throws ServiceException {
    try {
      this.setInsideMailParamBuilder(message.getMsgType());

      if (message.getSupportEmail() != null && message.getSupportEmail() == 1) {
        this.sendMessageAndMailSupportEmail(message);
      } else {
        List<Long> psnIdList = ServiceUtil.splitStrToLong(message.getReceivers().trim());
        this.sendMessageAndMail(psnIdList, null, message);
      }
    } catch (Exception e) {
      logger.error("发送短消息和邮件(不支持邮件输入)出现异常：", e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public void sendMessageAndMailSupportEmail(Message message) throws ServiceException {
    try {
      Map<String, List> receiverMap = receiverService.getReceivePsnIdAndEmail(message.getReceivers());
      // 匹配到系统用户保存psnId
      List<Long> psnIdList = receiverMap.get("psnIdList");
      // 没有匹配到系统用户则保存email
      List<String> emailList = receiverMap.get("emailList");

      this.sendMessageAndMail(psnIdList, emailList, message);
    } catch (Exception e) {
      logger.error("发送短消息和邮件(支持邮件输入)，判断接收人是否为系统用户时出现异常：", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 发送消息和邮件.
   * 
   * @param recieverIdList
   * @param recEmailList
   * @throws ServiceException
   */
  private void sendMessageAndMail(List<Long> recIdList, List<String> recEmailList, Message message)
      throws ServiceException {
    try {
      Map<String, Object> map =
          ThreadLocalInsideMailParamBuilder.getInsideMailParamBuilder().builderParam(message, recIdList); // 通过不同类型构造本节点邮箱所需参数
      if (CollectionUtils.isNotEmpty(recIdList)) {
        InsideMailBox insideMail = mailBoxService.saveInsideMailBox(map);// 当前节点保存发件箱信息
        for (Long recId : recIdList) {
          // 是否拒绝接收站内信
          boolean isRefuseInsideMsg = this.isUserRefuseMsg(SecurityUtils.getCurrentUserId(), recId);
          if (!isRefuseInsideMsg) {
            snsSyncMessageProducer.syncInsideMsgToSns(recId, insideMail, map,
                BasicRmtSrvModuleConstants.SNS_MODULE_ID.intValue());
          }
        }
      }

      // 如果是InsideMessageConstants.MSG_TYPE_REPLAY_FULLTEXT_REQUEST类型，则需要另外添加成果详情自动登录链接

      if (CollectionUtils.isNotEmpty(recEmailList)) {
        for (String email : recEmailList) {
          Map<String, Object> mapClone = new HashMap<String, Object>(map);
          scmEmailService.sendAppTypeMail(email, mapClone);
        }
      }

      // 添加分享记录
      if (NumberUtils.toInt(ObjectUtils.toString(map.get("isProduceDynamic"))) == 1) {
        this.produceDynamic(message.getReceivers(), message.getJsonParam());
      }
    } catch (Exception e) {
      logger.error("发送消息和邮件出现异常：", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveSyncInsideMessageAndSendMail(Long receiverId, InsideMailBox insideMailBox,
      Map<String, Object> mailParam) throws ServiceException {
    try {
      inboxService.saveInsideInbox(receiverId, insideMailBox); // 保存收件箱信息
      int isSendMsgMail = NumberUtils
          .createInteger(mailParam.get("isSendMsgMail") == null ? null : mailParam.get("isSendMsgMail").toString());
      boolean isRecInsideMsgMail = false;
      if (isSendMsgMail == 1) {
        isRecInsideMsgMail =
            this.getPsnMailSetService(receiverId).isClosed(receiverId, ConstMailTypeDictionary.EXIST_NEW_MESSAGE);
      }
      if (!isRecInsideMsgMail) { // 获取用户邮件设置，判断用户是否关闭了短信邮件通知发送功能
        scmEmailService.sendInsideMail(receiverId, mailParam);
      }
      cacheService.remove(MessageConstant.MSG_TIP_CACHE_NAME, MessageConstant.MSG_TIP_CACHE_KEY + receiverId);
    } catch (Exception e) {
      logger.error("处理同步过来的站内短消息出现异常：", e);
      throw new ServiceException();
    }
  }

  /**
   * 产生动态.
   * 
   * @param receivers
   * @param jsonParam
   * @throws ServiceException
   */
  private void produceDynamic(String receivers, String jsonParam) throws ServiceException {
    try {
      JSONObject jsonObject = JSONObject.fromObject(jsonParam);
      if (jsonObject.containsKey("resType")) {
        int resType = jsonObject.getInt("resType");
        if (resType == 8) {// 群组
          dynamicShareService.shareGroup(jsonParam, 0, receivers);
        } else if (resType == 9) {// 简历
          dynamicShareService.shareResume(jsonParam, 0, receivers);
        } else {// 应用
          dynamicShareService.shareApp(jsonParam, 0, receivers);
        }
      }
    } catch (Exception e) {
      logger.error("发送站内信产生动态出现异常：", e);
    }
  }

  /**
   * 获取指定用户邮件设置Service.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  private PsnMailSetService getPsnMailSetService(Long psnId) throws ServiceException {
    return this.psnMailSetService;
  }

  /**
   * 判断用户是否拒绝接收站内信.
   * 
   * @param sender
   * @param psnId
   * @return
   * @throws ServiceException
   */
  private boolean isUserRefuseMsg(Long sender, Long psnId) throws ServiceException {
    boolean refuse = true;
    try {
      PrivacySettings ps = this.privacySettingsDao.loadPsByPsnId(psnId, PrivacyType.SEND_MSG);
      if (ps == null || ps.getPermission().equals(0)) {
        refuse = false;
      } else if (ps.getPermission().equals(1)) {
        refuse = this.friendDao.getFriendId(psnId, sender) != null ? false : true;
      } else if (ps.getPermission().equals(2)) {
        refuse = true;
      }
    } catch (Exception e) {
      logger.error("查询用户短消息配置失败：", e);
      throw new ServiceException(e);
    }
    return refuse;
  }

  private void setInsideMailParamBuilder(Integer msgType) {
    switch (msgType) {
      case InsideMessageConstants.MSG_TYPE_INVITE_ADDPUBFOR_GROUP:
        ThreadLocalInsideMailParamBuilder.setInsideMailParamBuilder(sharePubForGroupParamBuilder);
        break;
      case InsideMessageConstants.MSG_TYPE_INVITE_ADDFILEFOR_GROUP:
        ThreadLocalInsideMailParamBuilder.setInsideMailParamBuilder(shareFileForGroupParamBuilder);
        break;
      case InsideMessageConstants.MSG_TYPE_EVALUATION_OF:
        ThreadLocalInsideMailParamBuilder.setInsideMailParamBuilder(evaFriendParamBuilder);
        break;
      case InsideMessageConstants.MSG_TYPE_FRIEND_RECOMMEND:
        ThreadLocalInsideMailParamBuilder.setInsideMailParamBuilder(cmdFriendParamBuilder);
        break;
      case InsideMessageConstants.MSG_TYPE_CONGRATULATION_JOB:
        ThreadLocalInsideMailParamBuilder.setInsideMailParamBuilder(congratutionJobParamBuilder);
        break;
      case InsideMessageConstants.MSG_TYPE_SHARE_CV:
        ThreadLocalInsideMailParamBuilder.setInsideMailParamBuilder(shareCVParamBuilder);
        break;
      case InsideMessageConstants.MSG_TYPE_SHARE_FUND:
        ThreadLocalInsideMailParamBuilder.setInsideMailParamBuilder(shareFundParamBuilder);
        break;
      case InsideMessageConstants.MSG_TYPE_SHARE_JOURNAL:
        ThreadLocalInsideMailParamBuilder.setInsideMailParamBuilder(shareJournalParamBuilder);
        break;
      case InsideMessageConstants.MSG_TYPE_SHARE_AREA:
        ThreadLocalInsideMailParamBuilder.setInsideMailParamBuilder(shareAreaParamBuilder);
        break;
      case InsideMessageConstants.MSG_TYPE_SHARE_HOTWORDS:
        ThreadLocalInsideMailParamBuilder.setInsideMailParamBuilder(shareHotWordsParamBuilder);
        break;
      case InsideMessageConstants.MSG_TYPE_SHARE_GROUP:
        ThreadLocalInsideMailParamBuilder.setInsideMailParamBuilder(shareGroupParamBuilder);
        break;
      case InsideMessageConstants.MSG_TYPE_REPLAY_FULLTEXT_REQUEST:
        ThreadLocalInsideMailParamBuilder.setInsideMailParamBuilder(fulltextReqInsideMsgParamBuilder);
        break;
      default:
        ThreadLocalInsideMailParamBuilder.setInsideMailParamBuilder(ordinaryInsideMsgParamBuilder);
        break;
    }
  }
}
