package com.smate.web.psn.service.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.email.service.MailInitDataService;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.privacy.service.PublicPrivacyService;
import com.smate.core.base.psn.dao.PsnFileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnFile;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.psn.dao.file.PsnFileShareBaseDao;
import com.smate.web.psn.dao.file.PsnFileShareRecordDao;
import com.smate.web.psn.dao.group.GrpFileShareBaseDao;
import com.smate.web.psn.dao.group.GrpFileShareRecordDao;
import com.smate.web.psn.dao.profile.PersonEmailDao;
import com.smate.web.psn.form.FileMainForm;
import com.smate.web.psn.form.PsnFileInfo;
import com.smate.web.psn.model.file.PsnFileShareBase;
import com.smate.web.psn.model.file.PsnFileShareRecord;
import com.smate.web.psn.model.grp.GrpFile;
import com.smate.web.psn.model.grp.GrpFileShareBase;
import com.smate.web.psn.model.grp.GrpFileShareRecord;
import com.smate.web.psn.model.psninfo.PersonEmailRegister;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 我的文件分享服务类
 * 
 * @author Administrator
 *
 */
@Service("myFileShareService")
@Transactional(rollbackOn = Exception.class)
public class MyFileShareServiceImpl implements MyFileShareService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static final String ENCODING = "utf-8";
  @Resource(name = "freemarkerConfiguration")
  private Configuration freemarkerConfiguration;
  @Autowired
  private PsnFileShareRecordDao psnFileShareRecordDao;
  @Autowired
  private PsnFileShareBaseDao psnFileShareBaseDao;
  @Autowired
  private MailInitDataService mailInitDataService;
  @Autowired
  private PsnFileDao psnFileDao;
  @Autowired
  private PersonEmailDao personEmailDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private UserDao userDao;
  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "psnCacheService")
  private CacheService cacheService;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private GrpFileShareBaseDao grpFileShareBaseDao;
  @Autowired
  private GrpFileShareRecordDao grpFileShareRecordDao;
  @Autowired
  private PublicPrivacyService publicPrivacyService;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  @Override
  public void shareMyFile(FileMainForm form) throws Exception {

    PsnFileShareRecord record = new PsnFileShareRecord();
    record.setSharerId(form.getPsnId());
    record.setReveiverId(form.getReceiverId());
    record.setFileId(form.getFileId());
    record.setStatus(0);
    record.setCreateDate(new Date());
    record.setUpdateDate(record.getCreateDate());
    record.setMsgRelationId(form.getMsgRelationId());
    record.setShareBaseId(form.getShareBaseId());
    psnFileShareRecordDao.save(record);

    PsnFile psnFile = psnFileDao.get(form.getFileId());
    Person sender = personDao.get(form.getPsnId());
    Person receiver = personDao.get(form.getReceiverId());

    // 发送邮件
    restSendShareMyFileEmail(sender, receiver, psnFile);
  }

  /**
   * 调用接口发送分享我的文件邮件
   * 
   * @param sender
   * @param receiver
   * @param psnFile
   * @throws Exception
   */
  private void restSendShareMyFileEmail(Person sender, Person receiver, PsnFile psnFile) throws Exception {
    // 全文请求使用新模板
    if (sender == null || receiver == null || psnFile == null) {
      throw new Exception("构建全文回复，邮件对象为空" + this.getClass().getName());
    }

    String language = StringUtils.isNotBlank(receiver.getEmailLanguageVersion()) ? receiver.getEmailLanguageVersion()
        : sender.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().getLanguage();
    }
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();

    // 1跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    String senderPersonUrl = "";
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      senderPersonUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl();
    }
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("senderPersonUrl");
    l2.setUrl(senderPersonUrl);
    l2.setUrlDesc("发件人主页链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    MailLinkInfo l4 = new MailLinkInfo();
    l4.setKey("viewUrl");
    l4.setUrl(this.domainscm + "/dynweb/showmsg/msgmain?model=chatMsg");
    l4.setUrlDesc("查看站内信地址");
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));

    // 2构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    Integer tempcode = 10102;
    info.setSenderPsnId(sender.getPersonId());
    info.setReceiverPsnId(receiver.getPersonId());
    info.setReceiver(receiver.getEmail());
    info.setMsg("分享我的文件邮件");
    info.setMailTemplateCode(tempcode);
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));

    // 3模版参数集
    // 发件人头衔
    String psnName = getPersonName(sender, language);
    mailData.put("psnName", psnName);
    mailData.put("total", "1");
    // 0：其他，1：成果，2：文献， 3：工作文档；4：项目
    mailData.put("type", "3");
    mailData.put("recvName", getPersonName(receiver, language));
    mailData.put("recommendReason", "您可能会对这些文件感兴趣。");
    mailData.put("minEnShareTitle", psnFile.getFileName());
    mailData.put("minZhShareTitle", psnFile.getFileName());
    mailData.put("emailTypeKey", 0);
    mailData.put("mailContext", "");

    // 4主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    String subjectType = "";
    String subjectCount = mailData.get("total").toString();
    if ("zh".equals(language) || "zh_CN".equals(language)) {
      subjectType = "文件";
    } else {
      subjectType = "files";
      if ("1".equals(subjectCount)) {
        subjectCount = "a";
        subjectType = "file";
      }
    }
    subjectParamLinkList.add(psnName);
    subjectParamLinkList.add(subjectCount);
    subjectParamLinkList.add(subjectType);
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));

    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }

  /**
   * 文件批量分享
   * 
   * @param form
   * @throws Exception
   */
  @Override
  public void shareAllMyFiles(FileMainForm form) throws Exception {
    // 获取分组 分享id
    Long id = psnFileShareBaseDao.getId();
    form.setShareBaseId(id);
    // 循环保存记录 循环发送消息
    String[] des3ReceiverIds = form.getDes3ReceiverIds().split(",");
    String[] des3FileIds = form.getDes3FileIds().split(",");
    String firstFileName = "";
    // 当前时间
    Date currentDat = new Date();
    Person sender = personDao.get(form.getPsnId());
    StringBuilder msgRelationIds = new StringBuilder();
    for (String des3ReceiverId : des3ReceiverIds) {
      Long receiverId = Long.parseLong(Des3Utils.decodeFromDes3(des3ReceiverId));
      boolean canSendMsg = publicPrivacyService.canSendMsg(form.getPsnId(), receiverId);
      if (receiverId != null && canSendMsg) {
        // 循环保存记录 与发送消息
        for (String des3FileId : des3FileIds) {
          Long fileId = null;
          if (NumberUtils.isNumber(des3FileId)) {
            fileId = Long.parseLong(des3FileId);
          } else {
            fileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3FileId));
          }
          if (fileId != null) {
            // 还需要判断一下文件是不是属于自己的
            PsnFile psnFile = psnFileDao.get(fileId);
            if (psnFile == null || !psnFile.getOwnerPsnId().equals(form.getPsnId())) {
              throw new Exception("分享的文件不属于自己的 。 分享人的psnId=" + form.getPsnId() + "  分享的文件id=" + fileId);
            }
            if (StringUtils.isBlank(firstFileName)) {
              firstFileName = psnFile.getFileName();
            }
            // 发送消息 保存记录
            // 先发消息
            // 调open接口发送消息
            Map<String, Object> map1 = buildSendMsgParam(form, receiverId, fileId);
            Object obj = restTemplate.postForObject(this.openResfulUrl, map1, Object.class);
            Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
            Long msgRelationId = 0L;
            if (resultMap != null && "success".equals(resultMap.get("status"))) {
              List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
              if (result != null && result.size() > 0) {
                msgRelationId = NumberUtils.toLong(Objects.toString(result.get(0).get("msgRelationId"), "0"));
              }
            }

            // 在发送保存记录

            PsnFileShareRecord record = new PsnFileShareRecord();
            record.setSharerId(form.getPsnId());
            record.setReveiverId(receiverId);
            record.setFileId(fileId);
            record.setStatus(0);
            record.setCreateDate(currentDat);
            record.setUpdateDate(currentDat);
            record.setMsgRelationId(msgRelationId);
            record.setShareBaseId(form.getShareBaseId());
            psnFileShareRecordDao.save(record);
          }
        }
        // 发送邮件给这个人
        Map<String, Object> map = new HashMap<String, Object>();
        Person receiver = personDao.get(receiverId);
        restSendShareAllMyFilesEmail(sender, receiver, firstFileName, form.getTextContent(), des3FileIds.length,
            form.getShareBaseId());
      }
      if (StringUtils.isNotBlank(form.getTextContent()) && canSendMsg) {
        // 发送消息
        Map<String, Object> map1 = buildSendMsgParam(form, receiverId);
        Object obj = restTemplate.postForObject(this.openResfulUrl, map1, Object.class);
        // 取得消息id 保存到分享基础表 给来 取消分享用
        Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
        if (resultMap != null && "success".equals(resultMap.get("status"))) {
          List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
          if (result != null && result.size() > 0) {
            msgRelationIds.append(Objects.toString(result.get(0).get("msgRelationId"), "0") + ",");
          }
        }
      }
    }
    // 更新 分享记录基础表
    PsnFileShareBase shareBase = new PsnFileShareBase();
    shareBase.setId(id);
    shareBase.setSharerId(form.getPsnId());
    shareBase.setStatus(0);
    shareBase.setCreateDate(new Date());
    shareBase.setUpdateDate(shareBase.getCreateDate());
    if (StringUtils.isNotBlank(msgRelationIds) && msgRelationIds.length() > 0) {
      shareBase.setShareContentRel(msgRelationIds.substring(0, msgRelationIds.lastIndexOf(",")));
    }
    psnFileShareBaseDao.save(shareBase);
    form.getResultMap().put("result", "success");
  }

  /**
   * 调用接口发送批量分享我的文件的邮件
   * 
   * @param sender
   * @param receiver
   * @param string
   * @param textContent
   * @param length
   * @param shareBaseId
   * @throws Exception
   */
  private void restSendShareAllMyFilesEmail(Person sender, Person receiver, String file, String content, int total,
      Long baseId) throws Exception {
    // 全文请求使用新模板
    if (sender == null || receiver == null || file == null) {
      throw new Exception("构建文件回复，邮件对象为空" + this.getClass().getName());
    }
    String language = StringUtils.isNotBlank(receiver.getEmailLanguageVersion()) ? receiver.getEmailLanguageVersion()
        : sender.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();

    // 1跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    // 发件人主页
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    String senderPersonUrl = "";
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      senderPersonUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl();
    }
    String viewUrl = this.domainscm + "/psnweb/fileshare/emailviewfiles?from=new&A=" + sender.getPersonDes3Id() + "&B="
        + receiver.getPersonDes3Id() + "&C=" + Des3Utils.encodeToDes3(baseId.toString());
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("senderPersonUrl");
    l2.setUrl(senderPersonUrl);
    l2.setUrlDesc("发件人主页链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    MailLinkInfo l4 = new MailLinkInfo();
    l4.setKey("viewUrl");
    l4.setUrl(viewUrl);
    l4.setUrlDesc("文件详情链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));

    // 2构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    Integer tempcode = 10102;
    info.setSenderPsnId(sender.getPersonId());
    info.setReceiverPsnId(receiver.getPersonId());
    info.setReceiver(receiver.getEmail());
    info.setMsg("批量分享我的文件邮件");
    info.setMailTemplateCode(tempcode);
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));

    // 3邮件模版参数集
    // 发件人头衔
    String psnName = getPersonName(sender, language);
    mailData.put("psnName", psnName);
    mailData.put("total", total);
    mailData.put("mailContext", "");
    mailData.put("recvName", getPersonName(receiver, language));
    mailData.put("recommendReason", content);
    mailData.put("minEnShareTitle", "\"" + file.trim() + "\"");
    mailData.put("minZhShareTitle", "“" + file.trim() + "”");
    mailData.put("emailTypeKey", 0);
    mailData.put("mailContext", "");
    // 0：其他，1：成果，2：文献， 3：工"\"" + file + "\""作文档；4：项目
    mailData.put("type", "3");

    // 4主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    String subjectType = "";
    String subjectCount = mailData.get("total") + "";
    if ("zh".equals(language) || "zh_CN".equals(language)) {
      subjectType = "文件";
    } else {
      subjectType = "files";
      if ("1".equals(subjectCount)) {
        subjectCount = "a";
        subjectType = "file";
      }
    }
    subjectParamLinkList.add(psnName);
    subjectParamLinkList.add(subjectCount);
    subjectParamLinkList.add(subjectType);
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));

    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }

  /**
   * 构建发消息需要的参数
   * 
   * @param form
   * @return
   */
  private Map<String, Object> buildSendMsgParam(FileMainForm form, Long receiverId, Long fileId) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    map.put("openid", "99999999");
    map.put("token", "00000000msg77msg");
    dataMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER);
    dataMap.put(MsgConstants.MSG_SENDER_ID, form.getPsnId());
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, receiverId);
    dataMap.put(MsgConstants.MSG_FILE_ID, fileId);
    dataMap.put(MsgConstants.MSG_CONTENT, form.getTextContent());

    dataMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, "file");
    dataMap.put(MsgConstants.MSG_BELONG_PERSON, "true");

    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }

  /**
   * 只发文本消息
   * 
   * @param form
   * @return
   */
  private Map<String, Object> buildSendMsgParam(FileMainForm form, Long receiverId) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    map.put("openid", "99999999");
    map.put("token", "00000000msg77msg");
    dataMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER);
    dataMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, "text");
    dataMap.put(MsgConstants.MSG_SENDER_ID, form.getPsnId());
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, receiverId);
    dataMap.put(MsgConstants.MSG_CONTENT, form.getTextContent());
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }



  /**
   * 
   * @param person
   * @param language zh=中文
   * @return
   */
  String getPersonName(Person person, String language) {
    if ("zh".equals(language) || "zh_CN".equalsIgnoreCase(language)) {
      if (StringUtils.isNotBlank(person.getName())) {
        return person.getName();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getEname();
      }
    } else {
      if (StringUtils.isNotBlank(person.getEname())) {
        return person.getEname();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getName();
      }

    }
  }

  /**
   * 得到分享主表的主键
   * 
   * @throws Exception
   */
  @Override
  public void getPsnFileShareBaseId(FileMainForm form) throws Exception {
    Long id = psnFileShareBaseDao.getId();
    form.setShareBaseId(id);
    PsnFileShareBase shareBase = new PsnFileShareBase();
    shareBase.setId(id);
    shareBase.setSharerId(form.getPsnId());
    shareBase.setStatus(0);
    shareBase.setCreateDate(new Date());
    shareBase.setUpdateDate(shareBase.getCreateDate());
    psnFileShareBaseDao.save(shareBase);
  }

  public static void main(String[] args) {
    System.out.println(Des3Utils.decodeFromDes3("%2FUBjIuqDehbNqF3gaH%2BwMw%3D%3D"));

    System.out.println("12345,".substring(0, "12345,".lastIndexOf(",")));

  }

  private String buildEmailTitle(Map<String, Object> map) throws Exception {
    String msg = "";
    try {
      msg = FreeMarkerTemplateUtils.processTemplateIntoString(
          freemarkerConfiguration.getTemplate(ObjectUtils.toString(map.get("tmpUrl")), ENCODING), map);
    } catch (IOException e) {
      logger.error("构建Email标题失败，没有找到对应的Email标题模板！", e);
    } catch (TemplateException e) {
      logger.error("构造Email标题失败,FreeMarker处理失败", e);
    }
    return msg;
  }

  @Override
  public void getFileShareDataInSendSide(Long resSendId, Long resReveiverId, Long baseId, FileMainForm form) {
    try {
      List<PsnFileShareRecord> resSendList =
          this.psnFileShareRecordDao.queryPsnResSendResByPage(resSendId, resReveiverId, baseId);
      if (CollectionUtils.isNotEmpty(resSendList)) {
        List<Long> fileId = new ArrayList<Long>();
        for (PsnFileShareRecord resSendRes : resSendList) {
          fileId.add(resSendRes.getFileId());
        }
        List<Map<Object, Object>> psnFilelist = psnFileDao.getFileListByFileId(fileId);
        List<PsnFileInfo> psnFileInfoList = new ArrayList<PsnFileInfo>();
        if (psnFilelist != null && psnFilelist.size() > 0) {
          for (Map<Object, Object> map : psnFilelist) {
            PsnFileInfo psnFileInfo = new PsnFileInfo();
            if (map.get("fileName") != null) {
              psnFileInfo.setFileName(map.get("fileName").toString());
            }
            if (map.get("fileDesc") != null) {
              psnFileInfo.setFileDesc(map.get("fileDesc").toString());
            }
            if (map.get("fileType") != null) {
              psnFileInfo.setFileType(map.get("fileType").toString());
            }
            if (map.get("uploadDate") != null) {
              psnFileInfo.setUploadDate((Date) map.get("uploadDate"));
            }
            if (map.get("fileSize") != null) {
              psnFileInfo.setFileSize(getPrintSize((Long) map.get("fileSize")));
            }
            if (map.get("id") != null) {
              // SCM-14409 hcj
              String downloadUrl = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.PSN, (Long) map.get("id"));
              psnFileInfo.setDownloadUrl(downloadUrl);
            }
            psnFileInfoList.add(psnFileInfo);
          }
          form.setPsnFileInfoList(psnFileInfoList);

        }
      }

    } catch (Exception e) {
      logger.error("查看分享文件失败" + resReveiverId, e);
    }

  }

  public static String getPrintSize(long size) {
    // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
    if (size < 1024) {
      return String.valueOf(size) + "B";
    } else {
      size = size / 1024;
    }
    // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
    // 因为还没有到达要使用另一个单位的时候
    // 接下去以此类推
    if (size < 1024) {
      return String.valueOf(size) + "KB";
    } else {
      size = size / 1024;
    }
    if (size < 1024) {
      // 因为如果以MB为单位的话，要保留最后1位小数，
      // 因此，把此数乘以100之后再取余
      size = size * 100;
      return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
    } else {
      // 否则如果要以GB为单位的，先除于1024再作同样的处理
      size = size * 100 / 1024;
      return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
    }
  }

  /*
   * public PsnFileInfo buildeFileShareQueryForm(PsnFile stationFile, Date shareDate, int status) {
   * PsnFileInfo form = new PsnFileInfo(); form.setFileId(stationFile.getId());
   * form.setDes3FileId(ServiceUtil.encodeToDes3(stationFile.getId().toString()));
   * 
   * form.setDes3ResRecId(stationFile.getResRecId() == null ? null :
   * ServiceUtil.encodeToDes3(stationFile .getResRecId().toString()));
   * 
   * form.setResNodeId(1); form.setFileName(stationFile.getFileName());
   * form.setFileType(stationFile.getFileType()); form.setFileViewType(
   * stationFile.getFileName().substring(stationFile.getFileName().lastIndexOf(".") +
   * 1).toLowerCase()); form.setShareDate(shareDate); // boolean isDel = //
   * newStationFdFlService.checkStationFileIsDel(stationFile.getFileId());
   * 
   * form.setIsImported(status);
   * form.setDes3ArchId(ServiceUtil.encodeToDes3(stationFile.getArchiveFileId().toString())); try {
   * ArchiveFile archiveFile = archiveFileDao.findArchiveFileById(stationFile.getArchiveFileId());
   * form.setFileStatus(stationFile.getStatus() == 0 ? 0 : 1);
   * form.setFileSize(archiveFile.getFileSize()); } catch (Exception e) { logger.error("分享构建文件参数失败",
   * e); } return form; }
   */

  @Override
  public int checkNewShareStatus(Long baseId) {
    PsnFileShareBase psnFileShareBase = psnFileShareBaseDao.get(baseId);
    if (psnFileShareBase == null || psnFileShareBase.getStatus() != 0) {
      return -1;
    }
    return 0;
  }

  @Override
  public int checkGrpFileShareStatus(Long baseId) {
    GrpFileShareBase grpFileShareBase = grpFileShareBaseDao.get(baseId);
    if (grpFileShareBase == null || grpFileShareBase.getStatus() != 0) {
      return -1;
    }
    return 0;
  }

  @Override
  public void getGrpFileShareDataInSendSide(Long resSendId, Long resReveiverId, Long baseId, FileMainForm form)
      throws Exception {
    try {
      List<GrpFileShareRecord> resSendList =
          grpFileShareRecordDao.queryGrpFileShareList(resSendId, resReveiverId, baseId);
      if (CollectionUtils.isNotEmpty(resSendList)) {
        List<PsnFileInfo> psnFileInfoList = new ArrayList<PsnFileInfo>();
        for (GrpFileShareRecord resSendRes : resSendList) {
          GrpFile grpFile = grpFileShareRecordDao.findGrpFileById(resSendRes.getGrpFileId());
          PsnFileInfo psnFileInfo = new PsnFileInfo();
          psnFileInfo.setFileName(grpFile.getFileName());
          psnFileInfo.setFileDesc(grpFile.getFileDesc());
          psnFileInfo.setFileType(grpFile.getFileType());
          psnFileInfo.setUploadDate(grpFile.getUploadDate());
          psnFileInfo.setFileSize(getPrintSize(grpFile.getFileSize()));
          String downloadUrl = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.GROUP, grpFile.getGrpFileId());
          psnFileInfo.setDownloadUrl(downloadUrl);
          psnFileInfoList.add(psnFileInfo);
        }
        form.setPsnFileInfoList(psnFileInfoList);
      }

    } catch (Exception e) {
      logger.error("查看分享文件失败" + resReveiverId, e);
    }
  }

  /**
   * 通过邮件，分享给好友
   * 
   * @param form
   */
  @Override
  public void shareAllMyFilesByEmails(FileMainForm form) throws Exception {
    // 获取分组 分享id
    Long id = psnFileShareBaseDao.getId();
    form.setShareBaseId(id);
    // 循环保存记录 循环发送消息
    String[] emails = form.getReceiverEmails().split(",");
    String[] des3FileIds = form.getDes3FileIds().split(",");
    String[] fileNames = form.getFileNames().split(",");
    // 当前时间
    Date currentDat = new Date();
    Person sender = personDao.get(form.getPsnId());
    StringBuilder msgRelationIds = new StringBuilder();
    boolean flag = false;
    for (String email : emails) {
      Long receiverId = 0L;// 默认匿名接收者
      flag = false;
      if (StringUtils.isBlank(email) || !isEmail(email)) {
        continue;
      }

      // 获取已经确认的邮件
      User user = userDao.findByLoginName(email);
      if (user != null) {
        String des3psnId = Des3Utils.encodeToDes3(user.getId().toString());
        if (StringUtils.isNotBlank(form.getDes3ReceiverIds()) && form.getDes3ReceiverIds().indexOf(des3psnId) != -1) {
          flag = true;
          continue;// 判断接受者邮箱名和psnId对应的人是否重复,不要重复发送
        }
        PersonEmailRegister psnEmail = personEmailDao.getPsnEmail(user.getId(), user.getLoginName());
        if (psnEmail.getIsVerify() == 1) {
          receiverId = user.getId();
        }
      }

      // 屏蔽文件分享给自己的情况
      if (receiverId.longValue() == form.getPsnId().longValue()) {
        // 说明分享给自己
        if (emails.length == 1) {
          form.getResultMap().put("result", "nss");// nss = not share
                                                   // self
          return;
        }
        continue;
      }

      // if (receiverId != null && receiverId.longValue() ==
      // form.getPsnId().longValue()) {
      // continue;
      // }
      if (receiverId != null) {
        // 循环保存记录 与发送消息
        for (String des3FileId : des3FileIds) {
          Long fileId = null;
          if (NumberUtils.isNumber(des3FileId)) {
            fileId = Long.parseLong(des3FileId);
          } else {
            fileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3FileId));
          }
          if (fileId != null) {
            // 还需要判断一下文件是不是属于自己的
            PsnFile psnFile = psnFileDao.get(fileId);
            if (psnFile == null || !psnFile.getOwnerPsnId().equals(form.getPsnId())) {
              throw new Exception("分享的文件不属于自己的 。 分享人的psnId=" + form.getPsnId() + "  分享的文件id=" + fileId);
            }
            Long msgRelationId = 0L;
            if (receiverId != null && receiverId != 0L) {
              // 发送消息 保存记录
              // 先发消息
              // 调open接口发送消息
              Map<String, Object> map1 = buildSendMsgParam(form, receiverId, fileId);
              Object obj = restTemplate.postForObject(this.openResfulUrl, map1, Object.class);
              Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
              if (resultMap != null && "success".equals(resultMap.get("status"))) {
                List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
                if (result != null && result.size() > 0) {
                  msgRelationId = NumberUtils.toLong(result.get(0).get("msgRelationId").toString());
                }
              }
            }

            // 在发送保存记录

            PsnFileShareRecord record = new PsnFileShareRecord();
            record.setSharerId(form.getPsnId());
            record.setReveiverId(receiverId);
            record.setFileId(fileId);
            record.setStatus(0);
            record.setCreateDate(currentDat);
            record.setUpdateDate(currentDat);
            record.setMsgRelationId(msgRelationId);
            record.setShareBaseId(form.getShareBaseId());
            if (receiverId == 0) {
              record.setOutsideEmail(email);
            }
            psnFileShareRecordDao.save(record);
          }
        }
        // 发送邮件给这个人
        Person receiver = new Person();
        if (receiverId != null && receiverId != 0L) {
          receiver = personDao.get(receiverId);
        } else {
          // 站外用户已邮件形式发送给对方 站外用户 personid为0
          receiver.setPersonId(0L);
          receiver.setEmail(email);
        }
        restSendShareAllMyFilesEmail(sender, receiver, fileNames[0], form.getTextContent(), des3FileIds.length,
            form.getShareBaseId());
      }
      if (StringUtils.isNotBlank(form.getTextContent()) && receiverId != null && receiverId != 0L) {
        // 发送消息
        Map<String, Object> map1 = buildSendMsgParam(form, receiverId);
        Object obj = restTemplate.postForObject(this.openResfulUrl, map1, Object.class);
        // 取得消息id 保存到分享基础表 给来 取消分享用
        Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
        if (resultMap != null && "success".equals(resultMap.get("status"))) {
          List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
          if (result != null && result.size() > 0) {
            msgRelationIds.append(result.get(0).get("msgRelationId").toString() + ",");
          }
        }

      }
    }
    if (!flag) {
      // 更新 分享记录基础表
      PsnFileShareBase shareBase = new PsnFileShareBase();
      shareBase.setId(id);
      shareBase.setSharerId(form.getPsnId());
      shareBase.setStatus(0);
      shareBase.setCreateDate(new Date());
      shareBase.setUpdateDate(shareBase.getCreateDate());
      if (StringUtils.isNotBlank(msgRelationIds) && msgRelationIds.length() > 0) {
        shareBase.setShareContentRel(msgRelationIds.substring(0, msgRelationIds.lastIndexOf(",")));
      }
      psnFileShareBaseDao.save(shareBase);
    }
    form.getResultMap().put("result", "success");
  }

  public static boolean isEmail(String str) {
    String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    return match(regex, str);
  }

  /**
   * @param regex 正则表达式字符串
   * @param str 要匹配的字符串
   * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
   */
  private static boolean match(String regex, String str) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(str);
    return matcher.matches();
  }

}
