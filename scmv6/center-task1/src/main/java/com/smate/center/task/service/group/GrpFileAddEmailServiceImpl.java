package com.smate.center.task.service.group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.smate.center.task.dao.sns.grp.EmailGrpFilePsnDao;
import com.smate.center.task.dao.sns.grp.GrpMemberDao;
import com.smate.center.task.dao.sns.quartz.GrpBaseInfoDao;
import com.smate.center.task.dao.sns.quartz.GrpFileDao;
import com.smate.center.task.dao.sns.quartz.GrpIndexUrlDao;
import com.smate.center.task.model.grp.EmailGrpFilePsn;
import com.smate.center.task.model.grp.GrpBaseinfo;
import com.smate.center.task.model.sns.quartz.GrpFile;
import com.smate.center.task.model.sns.quartz.GrpIndexUrl;
import com.smate.center.task.service.email.MailInitDataService;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.sys.ScmSystemUtil;

/**
 * 群组文件 ，发送邮件服务
 * 
 * @author aijiangbin
 *
 */
@Service("groupFileAddEmailService")
@Transactional(rollbackOn = Exception.class)
public class GrpFileAddEmailServiceImpl implements GrpFileAddEmailService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ScmSystemUtil scmSystemUtil;
  @Autowired
  private GrpFileDao grpFileDao;
  @Autowired
  private GrpIndexUrlDao grpIndexUrlDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private EmailGrpFilePsnDao emailGrpFilePsnDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private MailInitDataService mailInitDataService;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;

  /**
   * 当天上传文件的群组id
   */
  @Override
  public List<Long> findTodayUploadFileGrpId() throws Exception {
    return grpFileDao.findTodayUploadFileGrpId();
  }

  /**
   * 当天上传的文件 或者 课件
   */
  @Override
  public GrpFile findTodayUplaodGrpPubsByGrpId(Long grpId) throws Exception {
    return grpFileDao.findTodayUplaodGrpPubsByGrpId(grpId);
  }

  /**
   * 新上传的文件，课件，发送邮件
   */
  @Override
  public void sendUploadFileEamilNotify(GrpFile grpFile) throws Exception {

    Person sender = personDao.get(grpFile.getUploadPsnId());
    GrpBaseinfo baseinfo = grpBaseInfoDao.get(grpFile.getGrpId());
    List<Long> grpMembers = grpMemberDao.getGrpMemberIdByGrpId(grpFile.getGrpId());
    if (grpMembers == null || grpMembers.size() == 0) {
      return;
    }
    try {
      for (Long psnId : grpMembers) {
        if (psnId.longValue() == grpFile.getUploadPsnId().longValue()) {
          continue;
        }
        Person receiver = personDao.get(psnId);
        sendNotifyEmail(sender, receiver, grpFile, baseinfo);
      }
    } catch (Exception e) {
      throw new Exception("上传群组文件，给组员发送邮件，异常", e);
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
  public void sendNotifyEmail(Person sender, Person receiver, GrpFile grpFile, GrpBaseinfo baseinfo) throws Exception {
    Map<String, Object> params = new HashMap<String, Object>();
    if (grpFile.getFileModuleType() == 0) {
      buildGrpFileEmailParam(params, sender, receiver, grpFile, baseinfo);
    } else if (grpFile.getFileModuleType() == 2) {
      buildGrpCoursewareEmailParam(params, sender, receiver, grpFile, baseinfo);
    }
    // 保存邮件初始信息
    mailInitDataService.saveMailInitData(params);
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
  void buildGrpFileEmailParam(Map<String, Object> params, Person sender, Person receiver, GrpFile grpFile,
      GrpBaseinfo baseinfo) throws Exception {

    if (sender == null || receiver == null || grpFile == null || baseinfo == null) {
      throw new Exception("上传文件，通知邮件，缺少必要的参数");
    }
    String subject = "";
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    String receiverName = getPersonName(receiver, language);
    params.put("receiver", receiverName);
    String senderName = getPersonName(sender, language);
    params.put("sender", senderName);
    // http://dev.scholarmate.com/P/Qf22qe
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    String psnUrl = "";
    if (psnProfileUrl != null) {
      psnUrl = scmSystemUtil.getSysDomain() + "/P/" + psnProfileUrl.getPsnIndexUrl();
    }
    params.put("psnUrl", psnUrl); // 上传者url
    String downUrl = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.GROUP, grpFile.getGrpFileId());
    params.put("shareFileUrl", downUrl); // 文件url
    // https://dev.scholarmate.com/G/YRn2ue?model=file
    GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(baseinfo.getGrpId());
    String fileUrl = "";
    if (grpIndexUrl != null) {
      fileUrl = scmSystemUtil.getSysDomain() + "/G/" + grpIndexUrl.getGrpIndexUrl();
      fileUrl = fileUrl + "?model=file ";
    } else {
      fileUrl = scmSystemUtil.getSysDomain() + "/groupweb/grpinfo/main?des3GrpId="
          + Des3Utils.encodeToDes3(baseinfo.getGrpId().toString());
      fileUrl = fileUrl + "&model=file ";
    }
    params.put("fileUrl", fileUrl); // 文件列表url
    params.put("fileCount", 1);
    params.put("fileNames", grpFile.getFileName());
    params.put("groupName", baseinfo.getGrpName());
    params.put("domainUrl", scmSystemUtil.getSysDomain());
    String template = "";
    if ("zh".equals(language) || "zh_CN".equals(language)) {
      template = "Group_Add_New_File_Template_zh_CN.ftl";
    } else {
      template = "Group_Add_New_File_Template_en_US.ftl";
    }
    subject = params.get("sender") + "在群组上传了文件";
    params.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
    params.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, receiver.getPersonId());

    params.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, receiver.getEmail());
    params.put(EmailConstants.EMAIL_TEMPLATE_KEY, template);

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
  void buildGrpCoursewareEmailParam(Map<String, Object> params, Person sender, Person receiver, GrpFile grpFile,
      GrpBaseinfo baseinfo) {

    String subject = "";
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    String receiverName = getPersonName(receiver, language);
    params.put("receiver", receiverName);
    String senderName = getPersonName(sender, language);
    params.put("sender", senderName);
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    String psnUrl = "";
    if (psnProfileUrl != null) {
      psnUrl = scmSystemUtil.getSysDomain() + "/P/" + psnProfileUrl.getPsnIndexUrl();
    }
    params.put("psnUrl", psnUrl); // 上传者url
    // https://dev.scholarmate.com/G/YRn2ue?model=file
    GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(baseinfo.getGrpId());
    String fileUrl = "";
    if (grpIndexUrl != null) {
      fileUrl = scmSystemUtil.getSysDomain() + "/G/" + grpIndexUrl.getGrpIndexUrl();
      fileUrl = fileUrl + "?model=curware ";
    } else {
      fileUrl = scmSystemUtil.getSysDomain() + "/groupweb/grpinfo/main?des3GrpId="
          + Des3Utils.encodeToDes3(baseinfo.getGrpId().toString());
      fileUrl = fileUrl + "&model=curware ";
    }
    params.put("fileUrl", fileUrl); // 文件列表url
    params.put("fileCount", 1);
    params.put("fileNames", grpFile.getFileName());
    params.put("fileDesc", grpFile.getFileDesc());
    params.put("groupName", baseinfo.getGrpName());
    params.put("domainUrl", scmSystemUtil.getSysDomain());
    String template = "";
    if ("zh".equals(language) || "zh_CN".equals(language)) {
      template = "Group_Add_New_Multimedia_Template_zh_CN.ftl";
    } else {
      template = "Group_Add_New_Multimedia_Template_en_US.ftl";
    }
    subject = params.get("sender") + "为群组新增了一个教学课件";
    params.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
    params.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, receiver.getPersonId());

    params.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, receiver.getEmail());
    params.put(EmailConstants.EMAIL_TEMPLATE_KEY, template);

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

  @Override
  public void saveEmailGrpFilePsn(EmailGrpFilePsn emailGrpFilePsn) throws Exception {
    emailGrpFilePsnDao.save(emailGrpFilePsn);

  }

}
