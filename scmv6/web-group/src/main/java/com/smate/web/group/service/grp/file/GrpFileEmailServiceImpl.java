package com.smate.web.group.service.grp.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.psninfo.PsnInfoUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.group.dao.grp.grpbase.GrpBaseInfoDao;
import com.smate.web.group.dao.grp.grpbase.GrpIndexUrlDao;
import com.smate.web.group.dao.grp.member.GrpMemberDao;
import com.smate.web.group.model.grp.file.GrpFile;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.model.grp.grpbase.GrpIndexUrl;

@Service("grpFileEmailService")
@Transactional(rollbackOn = Exception.class)
public class GrpFileEmailServiceImpl implements GrpFileEmailService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private GrpIndexUrlDao grpIndexUrlDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;


  /**
   * 新上传的文件，课件，发送邮件
   */
  @Override
  public void sendUploadFileEamilNotify(GrpFile grpFile, Integer fileCount) throws Exception {
    Person sender = personDao.get(grpFile.getUploadPsnId());
    GrpBaseinfo baseinfo = grpBaseInfoDao.get(grpFile.getGrpId());
    List<Long> grpMembers = grpMemberDao.getGrpMembers(grpFile.getGrpId());
    if (grpMembers == null || grpMembers.size() == 0) {
      return;
    }
    for (Long psnId : grpMembers) {
      if (psnId.longValue() == grpFile.getUploadPsnId().longValue()) {
        continue;
      }
      Person receiver = personDao.get(psnId);
      try {
        sendNotifyEmail(sender, receiver, grpFile, baseinfo, fileCount);
      } catch (Exception e) {
        logger.info("人员为null，人员psnId=" + receiver.getPersonId(), e);
      }
    }
  }

  /**
   * 发送通知邮件
   * 
   * @param sender
   * @param receiver
   * @param grpFile
   * @param baseinfo
   * @throws Exception
   */
  public void sendNotifyEmail(Person sender, Person receiver, GrpFile grpFile, GrpBaseinfo baseinfo, Integer fileCount)
      throws Exception {
    Map<String, Object> paramData = new HashMap<String, Object>();
    if (grpFile.getFileModuleType() == 0) {
      // 文件
      buildGrpFileEmailParam(paramData, sender, receiver, grpFile, baseinfo, fileCount);
    } else if (grpFile.getFileModuleType() == 2) {
      // 课件
      buildGrpCoursewareEmailParam(paramData, sender, receiver, grpFile, baseinfo, fileCount);
    } else if (grpFile.getFileModuleType() == 1) {
      // 作业
      buildGrpAssignmentsEmailParam(paramData, sender, receiver, grpFile, baseinfo, fileCount);
    }
    // 保存邮件初始信息
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }


  /**
   * 构建群组文件信息
   * 
   * @param params
   * @param sender
   * @param receiver
   * @param grpFile
   * @param baseinfo
   */
  void buildGrpFileEmailParam(Map<String, Object> paramData, Person sender, Person receiver, GrpFile grpFile,
      GrpBaseinfo baseinfo, Integer fileCount) throws Exception {

    if (sender == null || receiver == null || grpFile == null || baseinfo == null) {
      throw new Exception("上传文件，通知邮件，缺少必要的参数");
    }
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    Integer templateCode = 10093;
    info.setReceiver(receiver.getEmail());// 接收邮箱
    info.setSenderPsnId(sender.getPersonId());// 发送人psnId，0=系统邮件
    info.setReceiverPsnId(receiver.getPersonId());// 接收人psnId，0=非科研之友用户
    info.setMailTemplateCode(templateCode);// 模版标识，参考V_MAIL_TEMPLATE
    info.setMsg("为群组上传文件");// 描述
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
    // 发件人主页
    String psnUrl = "";
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    if (psnProfileUrl != null) {
      psnUrl = domainscm + "/P/" + psnProfileUrl.getPsnIndexUrl();
    }
    String downUrl = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.GROUP, grpFile.getGrpFileId());
    GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(baseinfo.getGrpId());
    String fileUrl = "";
    if (grpIndexUrl != null) {
      fileUrl = domainscm + "/G/" + grpIndexUrl.getGrpIndexUrl();
      fileUrl = fileUrl + "?model=file ";
    } else {
      fileUrl =
          domainscm + "/groupweb/grpinfo/main?des3GrpId=" + Des3Utils.encodeToDes3(baseinfo.getGrpId().toString());
      fileUrl = fileUrl + "&model=file ";
    }
    String fileUrlMobile =
        domainMobile + "/grp/main/grpfilemain?des3GrpId=" + Des3Utils.encodeToDes3(baseinfo.getGrpId().toString());

    String fileListUrl = EmailCommonService.PC_OR_MB_TOKEN + "pc=" + fileUrl + "&&mobile=" + fileUrlMobile;
    // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友主页");
    MailLinkInfo l2 = new MailLinkInfo();
    l1.setKey("psnUrl");
    l1.setUrl(psnUrl);
    l1.setUrlDesc("发件人主页");
    MailLinkInfo l3 = new MailLinkInfo();
    l1.setKey("shareFileUrl");
    l1.setUrl(downUrl);
    l1.setUrlDesc("文件url");
    MailLinkInfo l4 = new MailLinkInfo();
    l1.setKey("fileUrl");
    l1.setUrl(fileListUrl);
    l1.setUrlDesc("文件列表url");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    mailData.put("receiver", PsnInfoUtils.getPersonName(receiver, language));
    mailData.put("sender", PsnInfoUtils.getPersonName(sender, language));
    mailData.put("fileUrl", fileListUrl); // 文件列表url
    mailData.put("fileCount", fileCount.toString());
    mailData.put("fileNames", grpFile.getFileName());
    mailData.put("fileDesc", StringUtils.isNotBlank(grpFile.getFileDesc()) ? grpFile.getFileDesc() : "");
    mailData.put("groupName", baseinfo.getGrpName());
    mailData.put("shareFileUrl", downUrl); // 文件url
    mailData.put("psnUrl", psnUrl);
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add(PsnInfoUtils.getPersonName(sender, language));
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
  }

  /**
   * 构建群组课件信息
   * 
   * @param params
   * @param sender
   * @param receiver
   * @param grpFile
   * @param baseinfo
   */
  void buildGrpCoursewareEmailParam(Map<String, Object> paramData, Person sender, Person receiver, GrpFile grpFile,
      GrpBaseinfo baseinfo, Integer fileCount) {
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    Integer templateCode = 10094;
    info.setReceiver(receiver.getEmail());// 接收邮箱
    info.setSenderPsnId(sender.getPersonId());// 发送人psnId，0=系统邮件
    info.setReceiverPsnId(receiver.getPersonId());// 接收人psnId，0=非科研之友用户
    info.setMailTemplateCode(templateCode);// 模版标识，参考V_MAIL_TEMPLATE
    info.setMsg("为群组上传课件");// 描述
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
    // 发件人主页
    String psnUrl = "";
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    if (psnProfileUrl != null) {
      psnUrl = domainscm + "/P/" + psnProfileUrl.getPsnIndexUrl();
    }
    String downUrl = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.GROUP, grpFile.getGrpFileId());
    GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(baseinfo.getGrpId());
    String fileUrl = "";
    if (grpIndexUrl != null) {
      fileUrl = domainscm + "/G/" + grpIndexUrl.getGrpIndexUrl();
      fileUrl = fileUrl + "?model=curware ";
    } else {
      fileUrl =
          domainscm + "/groupweb/grpinfo/main?des3GrpId=" + Des3Utils.encodeToDes3(baseinfo.getGrpId().toString());
      fileUrl = fileUrl + "&model=curware ";
    }
    String fileUrlMobile = domainMobile + "/grp/main/grpfilemain?des3GrpId="
        + Des3Utils.encodeToDes3(baseinfo.getGrpId().toString()) + "&courseFileType=2";
    String materialUrl = EmailCommonService.PC_OR_MB_TOKEN + "pc=" + fileUrl + "&&mobile=" + fileUrlMobile;
    // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友主页");
    MailLinkInfo l2 = new MailLinkInfo();
    l1.setKey("psnUrl");
    l1.setUrl(psnUrl);
    l1.setUrlDesc("发件人主页");
    MailLinkInfo l3 = new MailLinkInfo();
    l1.setKey("shareMaterialUrl");
    l1.setUrl(downUrl);
    l1.setUrlDesc("课件url");
    MailLinkInfo l4 = new MailLinkInfo();
    l1.setKey("materialUrl");
    l1.setUrl(materialUrl);
    l1.setUrlDesc("课件列表url");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    mailData.put("receiver", PsnInfoUtils.getPersonName(receiver, language));
    mailData.put("sender", PsnInfoUtils.getPersonName(sender, language));
    mailData.put("materialUrl", materialUrl); // 课件列表url
    mailData.put("materialCount", fileCount.toString());
    mailData.put("materialNames", grpFile.getFileName());
    mailData.put("materialDesc", StringUtils.isNotBlank(grpFile.getFileDesc()) ? grpFile.getFileDesc() : "");
    mailData.put("groupName", baseinfo.getGrpName());
    mailData.put("shareMaterialUrl", downUrl); // 课件url
    mailData.put("psnUrl", psnUrl);
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add(PsnInfoUtils.getPersonName(sender, language));
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
  }

  /**
   * 构建群组作业信息
   * 
   * @param params
   * @param sender
   * @param receiver
   * @param grpFile
   * @param baseinfo
   */
  void buildGrpAssignmentsEmailParam(Map<String, Object> paramData, Person sender, Person receiver, GrpFile grpFile,
      GrpBaseinfo baseinfo, Integer fileCount) { // 定义接口接收的参数
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    Integer templateCode = 10072;
    info.setReceiver(receiver.getEmail());// 接收邮箱
    info.setSenderPsnId(sender.getPersonId());// 发送人psnId，0=系统邮件
    info.setReceiverPsnId(receiver.getPersonId());// 接收人psnId，0=非科研之友用户
    info.setMailTemplateCode(templateCode);// 模版标识，参考V_MAIL_TEMPLATE
    info.setMsg("为群组上传作业");// 描述
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
    // 发件人主页
    String psnUrl = "";
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      psnUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl();
    }
    String downUrl = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.GROUP, grpFile.getGrpFileId());
    GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(baseinfo.getGrpId());
    String fileUrl = "";
    if (grpIndexUrl != null) {
      fileUrl = domainscm + "/G/" + grpIndexUrl.getGrpIndexUrl();
      fileUrl = fileUrl + "?model=work ";
    } else {
      fileUrl =
          domainscm + "/groupweb/grpinfo/main?des3GrpId=" + Des3Utils.encodeToDes3(baseinfo.getGrpId().toString());
      fileUrl = fileUrl + "&model=work ";
    }
    String fileUrlMobile = domainMobile + "/grp/main/grpfilemain?des3GrpId="
        + Des3Utils.encodeToDes3(baseinfo.getGrpId().toString()) + "&workFileType=1";
    String assignmentUrl = EmailCommonService.PC_OR_MB_TOKEN + "pc=" + fileUrl + "&&mobile=" + fileUrlMobile;
    // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友主页");
    MailLinkInfo l2 = new MailLinkInfo();
    l1.setKey("psnUrl");
    l1.setUrl(psnUrl);
    l1.setUrlDesc("发件人主页");
    MailLinkInfo l3 = new MailLinkInfo();
    l1.setKey("shareAssignmentUrl");
    l1.setUrl(downUrl);
    l1.setUrlDesc("作业url");
    MailLinkInfo l4 = new MailLinkInfo();
    l1.setKey("assignmentUrl");
    l1.setUrl(assignmentUrl);
    l1.setUrlDesc("作业列表url");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    mailData.put("receiver", PsnInfoUtils.getPersonName(receiver, language));
    mailData.put("sender", PsnInfoUtils.getPersonName(sender, language));
    mailData.put("assignmentUrl", assignmentUrl); // 作业列表url
    mailData.put("assignmentCount", fileCount.toString());
    mailData.put("assignmentNames", grpFile.getFileName());
    mailData.put("assignmentDesc", StringUtils.isNotBlank(grpFile.getFileDesc()) ? grpFile.getFileDesc() : "");
    mailData.put("groupName", baseinfo.getGrpName());
    mailData.put("shareAssignmentUrl", downUrl); // 作业url
    mailData.put("psnUrl", psnUrl);
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add(PsnInfoUtils.getPersonName(sender, language));
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
  }

  /**
   * 
   * @param person
   * @param language zh=中文
   * @return
   */
  String getPersonName(Person person, String language) {
    if ("zh".equals(language) || "zh_CN".equals(language)) {
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


}
