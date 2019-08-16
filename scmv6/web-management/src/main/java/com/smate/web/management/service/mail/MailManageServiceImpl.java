package com.smate.web.management.service.mail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.dao.MailContentDao;
import com.smate.center.mail.connector.dao.MailEverydayStatisticDao;
import com.smate.center.mail.connector.dao.MailOriginalDataDao;
import com.smate.center.mail.connector.dao.MailTemplateDao;
import com.smate.center.mail.connector.model.MailEverydayStatistic;
import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.center.mail.connector.model.MailTemplate;
import com.smate.center.mail.connector.model.SearchMailInfo;
import com.smate.core.base.mail.dao.MailLinkDao;
import com.smate.core.base.mail.model.mongodb.MailLink;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.management.dao.mail.MailBlacklistDao;
import com.smate.web.management.dao.mail.MailClientDao;
import com.smate.web.management.dao.mail.MailConnectErrorDao;
import com.smate.web.management.dao.mail.MailRecordDao;
import com.smate.web.management.dao.mail.MailReturnedRecordDao;
import com.smate.web.management.dao.mail.MailSenderDao;
import com.smate.web.management.dao.mail.MailWhitelistDao;
import com.smate.web.management.model.mail.MailBlackListForm;
import com.smate.web.management.model.mail.MailBlacklist;
import com.smate.web.management.model.mail.MailClient;
import com.smate.web.management.model.mail.MailClientForm;
import com.smate.web.management.model.mail.MailConnectError;
import com.smate.web.management.model.mail.MailEverydayStatisticFrom;
import com.smate.web.management.model.mail.MailForShowInfo;
import com.smate.web.management.model.mail.MailLinkForm;
import com.smate.web.management.model.mail.MailManageConstant;
import com.smate.web.management.model.mail.MailManageForm;
import com.smate.web.management.model.mail.MailRecord;
import com.smate.web.management.model.mail.MailSender;
import com.smate.web.management.model.mail.MailSenderForm;
import com.smate.web.management.model.mail.MailTemplateForm;
import com.smate.web.management.model.mail.MailWhiteListForm;
import com.smate.web.management.model.mail.MailWhitelist;
import com.smate.web.management.model.mongodb.mail.MailReturnedRecord;

/**
 * 
 * @author zzx
 *
 */
@Service("mailManageService")
@Transactional(rollbackFor = Exception.class)
public class MailManageServiceImpl implements MailManageService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailLinkDao mailLinkDao;
  @Autowired
  private MailOriginalDataDao mailOriginalDataDao;
  @Autowired
  private MailTemplateDao mailTemplateDao;
  @Autowired
  private MailRecordDao mailRecordDao;
  @Autowired
  private MailContentDao mailContentDao;
  @Autowired
  private MailReturnedRecordDao mailReturnedRecordDao;
  @Autowired
  private MailConnectErrorDao mailConnectErrorDao;
  @Autowired
  private MailSenderDao mailSenderDao;
  @Autowired
  private MailClientDao mailClientDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private MailBlacklistDao mailBlacklistDao;
  @Autowired
  private MailWhitelistDao mailWhitelistDao;
  @Autowired
  private MailEverydayStatisticDao mailEverydayStatisticDao;

  @Override
  public void showMain(MailManageForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void findMailManageList(MailManageForm form) throws Exception {
    SearchMailInfo info = form.getSearchMailInfo();
    // 整理查询参数
    buildQueryParam(form, info);
    // 查询
    mailOriginalDataDao.searchMailOriginalDataList(info, form.getPage());
    // 构建显示信息
    buildMailShowInfo(form);
    // 构建统计信息
    buildMailShowCount(info, form);
  }

  private void buildQueryParam(MailManageForm form, SearchMailInfo info) throws Exception {
    form.setFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
    if (StringUtils.isNotBlank(info.getMailTemplateName())) {
      List<Integer> templateCodeList = mailTemplateDao.findCodeByTempName(info.getMailTemplateName());
      info.setTemplateCodeList(templateCodeList);
    }
    if (StringUtils.isNotBlank(info.getSenderDateStartStr())) {
      info.setSenderDateStart(form.getFormat().parse(info.getSenderDateStartStr()));
    }
    if (StringUtils.isNotBlank(info.getSenderDateEndStr())) {
      info.setSenderDateEnd(form.getFormat().parse(info.getSenderDateEndStr()));
    }
  }

  /**
   * 构建统计信息
   * 
   * @param info
   * @param form
   */
  private void buildMailShowCount(SearchMailInfo info, MailManageForm form) {
    form.setResultMap(new HashMap<String, String>());
    List<Map<String, Object>> statusList = info.getStatusList();
    if (statusList != null && statusList.size() > 0) {
      for (Map<String, Object> map : statusList) {
        // 0=待构造邮件 1=构造成功 2=构造失败 3=用户不接收此类邮件 4=模版频率限制
        String key = "皮这一下你很开心吗";
        switch (map.get("status").toString()) {
          case "0":
            key = MailManageConstant.to_be_construct;
            form.getResultMap().put(key, map.get("count").toString());
            break;
          case "2":
            key = MailManageConstant.construct_error;
            form.getResultMap().put(key, map.get("count").toString());
            break;
          case "3":
            key = MailManageConstant.receive_refuse;
            form.getResultMap().put(key, map.get("count").toString());
            break;
          case "4":
            key = MailManageConstant.frequency_limit;
            form.getResultMap().put(key, map.get("count").toString());
            break;
          default:
            break;
        }
      }
    }
    List<Map<String, Object>> sendStatusList = info.getSendStatusList();
    if (sendStatusList != null && sendStatusList.size() > 0) {
      for (Map<String, Object> map : sendStatusList) {
        // 发送状态 0=待分配 1=待发送 9=调度出错 2=发送成功 3=黑名单 4=receiver不存在
        String key = "LOVE";
        switch (map.get("sendStatus").toString()) {
          case "0":
            key = MailManageConstant.to_be_distributed;
            form.getResultMap().put(key, map.get("count").toString());
            break;
          case "1":
            key = MailManageConstant.to_be_sent;
            form.getResultMap().put(key, map.get("count").toString());
            break;
          case "2":
            key = MailManageConstant.send_successfully;
            form.getResultMap().put(key, map.get("count").toString());
            break;
          case "3":
            key = MailManageConstant.blacklist;
            form.getResultMap().put(key, map.get("count").toString());
            break;
          case "4":
            key = MailManageConstant.receiver_inexistence;
            form.getResultMap().put(key, map.get("count").toString());
            break;
          case "5":
            key = MailManageConstant.no_whitelist;
            form.getResultMap().put(key, map.get("count").toString());
            break;
          case "9":
            key = MailManageConstant.scheduling_error;
            form.getResultMap().put(key, map.get("count").toString());
            break;
          case "10":
            key = MailManageConstant.sending;
            form.getResultMap().put(key, map.get("count").toString());
            break;
          case "11":
            key = MailManageConstant.build_send_error;
            form.getResultMap().put(key, map.get("count").toString());
            break;
          default:
            break;
        }

      }
    }

  }

  private void buildMailShowInfo(MailManageForm form) {
    List<MailForShowInfo> list = new ArrayList<MailForShowInfo>();
    List<MailOriginalData> result = form.getPage().getResult();
    if (result != null && result.size() > 0) {
      for (MailOriginalData one : result) {
        MailForShowInfo info = new MailForShowInfo();
        info.setMailId(one.getMailId());
        info.setReceiver(one.getReceiver());
        info.setReceiverPsnId(one.getReceiverPsnId());
        info.setSenderPsnId(one.getSenderPsnId());
        info.setUpdateDate(one.getUpdateDate());
        info.setShowDateStr(form.getFormat().format(one.getUpdateDate()));
        // 模版名
        MailTemplate template = mailTemplateDao.get(one.getMailTemplateCode());
        if (template != null) {
          info.setMailTemplateName(template.getTemplateName());
        }
        info.setTemplateCode(one.getMailTemplateCode());
        // 主题
        MailRecord mailRecord = mailRecordDao.get(one.getMailId());
        if (mailRecord != null) {
          info.setSubject(mailRecord.getSubject());
          info.setSender(mailRecord.getSender());
        }
        // 邮件状态
        buildMailStatus(info, one);
        list.add(info);
      }
      form.setMailForShowInfoList(list);
    }
  }

  private void buildMailStatus(MailForShowInfo info, MailOriginalData one) {
    if (one.getStatus() == 1 && one.getSendStatus() != null) {
      // 发送状态 0=待分配 1=待发送 9=调度出错 2=发送成功 3=黑名单 4=receiver不存在
      getSendStatus(info, one);
    } else {
      // 0=待构造邮件 2=构造失败 3=用户不接收此类邮件4=模版频率限制
      getBuildStatus(info, one);
    }

  }

  /**
   * 列表显示对应状态提示
   * 
   * @param info
   * @param one
   */
  private void getBuildStatus(MailForShowInfo info, MailOriginalData one) {
    switch (one.getStatus()) {
      case 0:
        info.setStatusMsg(MailManageConstant.to_be_construct);
        break;
      case 2:
        info.setStatusMsg(MailManageConstant.construct_error);
        break;
      case 3:
        info.setStatusMsg(MailManageConstant.receive_refuse);
        break;
      case 4:
        info.setStatusMsg(MailManageConstant.frequency_limit);
        break;
      default:
        break;
    }

  }

  /**
   * 列表显示对应状态提示
   * 
   * @param info
   * @param one
   */
  private void getSendStatus(MailForShowInfo info, MailOriginalData one) {
    switch (one.getSendStatus()) {
      case 0:
        info.setStatusMsg(MailManageConstant.to_be_distributed);
        break;
      case 1:
        info.setStatusMsg(MailManageConstant.to_be_sent);
        break;
      case 2:
        info.setStatusMsg(MailManageConstant.send_successfully);
        break;
      case 3:
        info.setStatusMsg(MailManageConstant.blacklist);
        break;
      case 4:
        info.setStatusMsg(MailManageConstant.receiver_inexistence);
        break;
      case 5:
        info.setStatusMsg(MailManageConstant.no_whitelist);
        break;
      case 7:
        info.setStatusMsg(MailManageConstant.information_lock);
        break;
      case 8:
        info.setStatusMsg(MailManageConstant.send_error);
        break;
      case 9:
        info.setStatusMsg(MailManageConstant.scheduling_error);
        break;
      case 10:
        info.setStatusMsg(MailManageConstant.sending);
        break;
      case 11:
        info.setStatusMsg(MailManageConstant.build_send_error);
        break;
      default:
        break;
    }

  }

  @Override
  public void findMailLinkList(MailManageForm form) throws Exception {
    if (form.getMailId() == null) {
      throw new Exception("mailId不存在");
    }
    List<MailLink> list = mailLinkDao.findListByMailId(form.getMailId(), form.getPage());
    if (list != null && list.size() > 0) {
      form.setMailLinkList(list);
    }
  }

  @Override
  public void returnList(MailManageForm form) throws Exception {
    form.setFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
    if (StringUtils.isNotBlank(form.getStartSendDateStr())) {
      Date startSendDate = form.getFormat().parse(form.getStartSendDateStr());
      form.setStartSendDate(startSendDate);
    }
    if (StringUtils.isNotBlank(form.getEndSendDateStr())) {
      Date endSendDate = form.getFormat().parse(form.getEndSendDateStr());
      form.setEndSendDate(endSendDate);
    }
    List<MailReturnedRecord> list = mailReturnedRecordDao.findList(form.getPage(), form.getAccount(), form.getAddress(),
        form.getStartSendDate(), form.getEndSendDate());
    if (list != null && list.size() > 0) {
      form.setMailReturnedRecordList(list);
    }
    List<MailConnectError> mailConnectErrorList = mailConnectErrorDao.findList();
    if (mailConnectErrorList != null && mailConnectErrorList.size() > 0) {
      form.setShowMailConnError("有" + mailConnectErrorList.get(0).getAccount() + "等" + mailConnectErrorList.size()
          + "个帐号连接到收件箱失败，请及时到V_MAIL_CONNECT_ERROR处理！");
    }
  }

  @Override
  public void findTemplateList(MailTemplateForm form) throws Exception {
    MailTemplate mailTemplate = form.getMailTemplate();
    List<MailTemplate> templateList = mailTemplateDao.findTemplateList(mailTemplate, form.getPage(), form.getSubject());
    if (templateList != null && templateList.size() > 0) {
      form.setMailTemplateList(templateList);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void findLinkSumList(MailLinkForm form) throws Exception {
    buildParams(form);
    List<MailLinkForm> linklist = new ArrayList<MailLinkForm>();
    List<Map<String, Integer>> map = mailLinkDao.countLinks(form.getTemplateCode(), form.getStartCreateDate(),
        form.getEndCreateDate(), form.getPage());
    int count = 0;
    if (map.size() > 0 && map != null) {
      for (Map<String, Integer> o : map) {
        count = o.get("linksum");
        String _id = String.valueOf(o.get("_id"));
        String code = String.valueOf(o.get("templateCode"));
        String urlDesc = String.valueOf(o.get("urlDesc"));
        MailLinkForm info = setMailLinkFrom(count, _id, code, urlDesc);
        linklist.add(info);
      }
      form.setTemplateCode(linklist.get(0).getTemplateCode());
      form.setMailLinkList(linklist);
    }
  }

  private MailLinkForm setMailLinkFrom(int count, String _id, String code, String urlDesc) {
    Map<String, Object> str = new HashMap<String, Object>();
    MailLinkForm info = new MailLinkForm();
    str = JacksonUtils.jsonToMap(_id);
    String linkKey = String.valueOf(str.get("linkKey"));
    info.setLinkKey(linkKey);
    info.setCount(count);
    info.setUrlDesc(urlDesc);
    info.setTemplateCode(Integer.parseInt(code));
    MailTemplate template = mailTemplateDao.get(Integer.parseInt(code));
    if (template != null) {
      info.setTemplateName(template.getTemplateName());
    }
    return info;
  }

  private void buildParams(MailLinkForm form) throws Exception {
    form.setFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
    if (StringUtils.isNotBlank(form.getStartCreateDateStr())) {
      form.setStartCreateDate(form.getFormat().parse(form.getStartCreateDateStr()));
    }
    if (StringUtils.isNotBlank(form.getEndCreateDateStr())) {
      form.setEndCreateDate(form.getFormat().parse(form.getEndCreateDateStr()));
    }
  }

  /**
   * 发送账号列表
   */
  @Override
  public void findSenderList(MailSenderForm form) throws Exception {
    MailSender mailSender = form.getMailSender();
    List<MailSender> senderList = mailSenderDao.findSenderList(mailSender, form.getPage());
    if (senderList != null && senderList.size() > 0) {
      form.setMailSenderList(senderList);
    }
    MailSender mailCount = mailSenderDao.getSendCount();
    if (mailCount != null) {
      form.setResultMap(new HashMap<String, String>());
      form.getResultMap().put("总共可用次数", mailCount.getTodayMaxCount().toString());
      form.getResultMap().put("今天发送次数", mailCount.getTodaySendCount().toString());
      form.getResultMap().put("剩余发送次数", mailCount.getTodayRemainedCount().toString());
    }
  }

  /**
   * 发送客户端列表
   */
  @Override
  public void findClientList(MailClientForm form) throws Exception {
    MailClient mailClient = form.getMailClient();
    List<MailClient> clientList = mailClientDao.findClientList(mailClient, form.getPage());
    if (clientList != null && clientList.size() > 0) {
      form.setMailClientList(clientList);
    }

  }

  @Override
  public void findBlackList(MailBlackListForm form) throws Exception {
    MailBlacklist black = form.getMailBlack();
    List<MailBlacklist> blackList = mailBlacklistDao.findBlackList(black, form.getPage());
    if (blackList != null && blackList.size() > 0) {
      form.setMailBlacklist(blackList);
    }
  }

  @Override
  public void findWhiteList(MailWhiteListForm form) throws Exception {
    MailWhitelist white = form.getMailWhite();
    List<MailWhitelist> whiteList = mailWhitelistDao.findWhiteList(white, form.getPage());
    if (whiteList != null && whiteList.size() > 0) {
      form.setMailWhitelist(whiteList);
    }

  }

  @Override
  public void findEveryDayStatisticsList(MailEverydayStatisticFrom form) throws Exception {
    MailEverydayStatistic statistic = form.getMailEverydayStatistic();
    if (StringUtils.isNotBlank(form.getStartStatisticsDateStr())) {
      statistic.setStatisticsDateStartStr(form.getStartStatisticsDateStr());
    }
    if (StringUtils.isNotBlank(form.getEndStatisticsDateStr())) {
      statistic.setStatisticsDateEndStr(form.getEndStatisticsDateStr());
    }
    List<MailEverydayStatistic> statisticList = mailEverydayStatisticDao.findList(statistic, form.getPage());
    if (statisticList != null && statisticList.size() > 0) {
      form.setMailStatisticList(statisticList);
    }

  }
}
