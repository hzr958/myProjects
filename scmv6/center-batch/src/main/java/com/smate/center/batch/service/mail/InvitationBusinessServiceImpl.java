package com.smate.center.batch.service.mail;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.MessageConstant;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.mq.InviteMessageSyncProducer;
import com.smate.center.batch.service.pub.mq.SnsInviteMailMessage;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.MapBuilder;


/**
 * 收件箱-站内邀请业务逻辑实现类.
 * 
 * @see 功能：邀请的业务流程控制.
 * @author maojianguo
 * 
 */
@SuppressWarnings("javadoc")
@Service("invitationBusinessService")
@Transactional(rollbackFor = Exception.class)
public class InvitationBusinessServiceImpl implements InvitationBusinessService {

  private static final long serialVersionUID = 94547297943780655L;
  @Autowired
  private InvitationBusinessMethod businessMethod;
  @Autowired
  private InviteMessageSyncProducer snsInviteMessageProducer;
  @Autowired
  private MailBoxService mailBoxService;
  @Autowired
  private InboxService inboxService;
  @Autowired
  private CacheService cacheService;

  /**
   * 处理站内邀请发送端的业务逻辑.
   * 
   * @param person 发送发件邀请的人.
   * @param inviteMailParam 发件箱记录所需参数.
   * @param mailParam 发送MQ所需参数(具体业务相关的动态值).
   * @param operaType 操作类型(对应DynamicConstant类中RES_TYPE类型的参数值).
   * @return
   * @throws ServiceException
   */
  @Override
  public void dealSendInviteMessage(Person person, Map<String, Object> inviteMailParam, Map<String, Object> mailParam,
      int messageType) throws ServiceException {
    // 保存发件表记录.
    // InviteMailBox mailBox = mailBoxService.saveInviteMailBox(person, inviteMailParam);
    // 封装请求参数.
    // mailParam = businessMethod.assemStaticSendParam(person, mailBox, mailParam);
    // 遍历收件人节点发送信息.
    List<Integer> nodeList = businessMethod.getReceivorNodeList(mailParam, messageType);
    if (CollectionUtils.isNotEmpty(nodeList)) {
      for (Integer nodeId : nodeList) {
        // 发送MQ信息.
        snsInviteMessageProducer.sendInviteMessageToSns(mailParam, nodeId, messageType);
      }
    }

  }

  /**
   * 处理MQ接收消息.
   */
  @SuppressWarnings("unchecked")
  @Override
  public void dealInviteMessage(SnsInviteMailMessage inviteMessage) throws ServiceException {
    Integer messageType = inviteMessage.getMessageType();// 邀请类型.
    Map<String, Object> param = inviteMessage.getParam();// 请求参数.
    Integer nodeId = 1000;// 发送消息的节点,新系统无节点.
    // 获取收件人清单
    List<Map<String, Object>> receivorList = businessMethod.getReceivorList(messageType, param, nodeId);
    if (receivorList != null && receivorList.size() > 0) {
      inboxService = businessMethod.getInboxService(nodeId);
      // 收件箱表相关参数.
      Map<String, Object> inviteInParam = businessMethod.assemInviteInParam(param);
      String invitePsnIdString = businessMethod.getInvitePsnId(param);
      String mailContent = businessMethod.getInviteMailContent(param);
      // 发送站内信和站内邮件.
      for (Map<String, Object> receivor : receivorList) {
        // 获取收件人记录.
        String psnidString = ObjectUtils.toString(receivor.get("psnId"));
        Long psnId = (StringUtils.isBlank(psnidString) ? 0L : Long.valueOf(psnidString));
        Person recvPerson = businessMethod.getPersonManager(psnId).getPerson(psnId);
        param.put("locale", recvPerson.getEmailLanguageVersion());
        // 重建收件人信息.
        Map<String, Object> receivorInfo = businessMethod.rebuildReceivorInfo(recvPerson, receivor);
        // 封装收件箱相关表的参数(key值必须对应InboxService的saveInviteRelationRecord方法中的key值).
        Map<String, Object> dataParam = MapBuilder.getInstance().put("invitePsnId", invitePsnIdString)
            .put("mailContent", mailContent).put("psnId", psnidString).getMap();
        // 验证是否已保存邀请的收件记录.
        boolean isSavedReceivMail = businessMethod.checkInviteReceivRecord(psnId, param, nodeId, inboxService);
        if (!isSavedReceivMail) {
          // inboxService.saveInviteRelationRecord(inviteInParam, recvPerson, dataParam);
          // 处理发送站内邀请的邮件.
          businessMethod.sendInviteMail(messageType, param, receivorInfo, nodeId);
        }
        cacheService.remove(MessageConstant.MSG_TIP_CACHE_NAME, MessageConstant.MSG_TIP_CACHE_KEY + psnId);

      }
    }

  }

  /**
   * 将参数集合拼装为参数字符串.
   * 
   * @param paramMap
   * @return
   */
  @Override
  @SuppressWarnings("rawtypes")
  public String assemParamStr(Map<String, String> paramMap) {
    StringBuilder paramStr = new StringBuilder();
    // 遍历参数集合.
    Iterator iterator = paramMap.keySet().iterator();
    while (iterator.hasNext()) {
      String key = ObjectUtils.toString(iterator.next());// 参数名.
      String value = paramMap.get(key);// 参数值.
      // 拼装参数字符串.
      paramStr.append(key).append("=").append(value).append("&");
    }
    // 删除最后一个多余字符.
    if (paramStr.length() > 0) {
      paramStr.deleteCharAt(paramStr.lastIndexOf("&"));
    }
    return paramStr.toString();
  }

  /**
   * 将邀请码解析为参数集合.
   * 
   * @param invitationCode 邀请码.
   * @return
   */
  @Override
  @SuppressWarnings("unchecked")
  public Map<String, String> splitInviteCode(String invitationCode, String key) {
    Map<String, String> paramMap = MapBuilder.getInstance().getMap();
    if (StringUtils.isNotBlank(invitationCode)) {
      String[] dataArr = invitationCode.split("-");
      String[] nameArr = this.getParamNameArr(key);
      if (nameArr.length > 0 && nameArr.length == dataArr.length) {
        for (int i = 0; i < nameArr.length; i++) {
          paramMap.put(nameArr[i], dataArr[i]);
        }
      }
    }
    return paramMap;
  }

  /**
   * 获取请求参数的名称数组.
   * 
   * @param key 邀请类型.
   * @return
   */
  private String[] getParamNameArr(String key) {
    // TODO
    return null;
  }
}
