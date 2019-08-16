package com.smate.web.group.service.grp.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.email.service.MailInitDataService;
import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.psn.dao.PsnFileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnFile;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.string.StrUtils;
import com.smate.web.group.action.grp.form.GrpFileForm;
import com.smate.web.group.action.grp.form.GrpFileLabelShowInfo;
import com.smate.web.group.action.grp.form.GrpFileMember;
import com.smate.web.group.action.grp.form.GrpFileShowInfo;
import com.smate.web.group.dao.group.psn.PersonEmailDao;
import com.smate.web.group.dao.grp.file.GrpFileDao;
import com.smate.web.group.dao.grp.file.GrpFileShareBaseDao;
import com.smate.web.group.dao.grp.file.GrpFileShareRecordDao;
import com.smate.web.group.dao.grp.grpbase.GrpBaseInfoDao;
import com.smate.web.group.dao.grp.grpbase.GrpStatisticsDao;
import com.smate.web.group.dao.grp.label.GrpFileLabelDao;
import com.smate.web.group.dao.grp.label.GrpLabelDao;
import com.smate.web.group.dao.grp.member.GrpMemberDao;
import com.smate.web.group.model.group.psn.PersonEmailRegister;
import com.smate.web.group.model.group.psn.PsnInfo;
import com.smate.web.group.model.grp.file.GrpFile;
import com.smate.web.group.model.grp.file.GrpFileShareBase;
import com.smate.web.group.model.grp.file.GrpFileShareRecord;
import com.smate.web.group.model.grp.grpbase.GrpStatistics;
import com.smate.web.group.model.grp.label.GrpFileLabel;
import com.smate.web.group.model.grp.label.GrpLabel;
import com.smate.web.group.service.grp.label.GrpFileLabelService;
import com.smate.web.group.service.grp.member.BuildPsnInfoService;
import com.smate.web.group.service.grp.member.GrpRoleService;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 群组文件service
 * 
 * @author AiJiangBin
 */
@Service("grpFileService")
@Transactional(rollbackFor = Exception.class)
public class GrpFileServiceImpl implements GrpFileService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private BuildPsnInfoService buildPsnInfoService;
  @Autowired
  private GrpFileDao grpFileDao;
  @Resource
  private GrpRoleService grpRoleService;
  @Autowired
  private GrpStatisticsDao grpStatisticsDao;

  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Autowired
  private ArchiveFileService archiveFileService;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private UserDao userDao;
  @Autowired
  private PersonEmailDao personEmailDao;
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private MailInitDataService mailInitDataService;
  @Autowired
  private PsnFileDao psnFileDao;
  @Autowired
  private BatchJobsService batchJobsService;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;
  @Value("${domainscm}")
  private String domainscm;
  private static final String ENCODING = "utf-8";
  @Resource(name = "freemarkerConfiguration")
  private Configuration freemarkerConfiguration;

  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private GrpFileShareBaseDao grpFileShareBaseDao;
  @Autowired
  private GrpFileShareRecordDao grpFileShareRecordDao;
  @Autowired
  private GrpFileEmailService grpFileEmailService;

  @Autowired
  private GrpFileLabelDao grpFileLabelDao;

  @Autowired
  private GrpLabelDao grpLabelDao;
  @Autowired
  private GrpFileLabelService grpFileLabelService;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  @Override
  public void findGrpFileList(GrpFileForm grpFileForm) throws Exception {
    grpFileDao.findGrpFile(grpFileForm);
    List<GrpFile> grpFileList = grpFileForm.getPage().getResult();
    List<GrpFileShowInfo> grpFileShowInfos = new ArrayList<GrpFileShowInfo>();
    grpFileForm.setGrpFileShowInfos(grpFileShowInfos);
    if (grpFileList != null && grpFileList.size() > 0) {
      Map<Long, String> map = new HashMap<Long, String>();
      grpFileForm.setPsnIdNameMap(map);
      for (int i = 0; i < grpFileList.size(); i++) {
        GrpFileShowInfo grpFileShowInfo = buildGrpFileShowInfo(grpFileList, i, grpFileForm);
        if (grpFileForm.getGrpRole() == 1 || grpFileForm.getGrpRole() == 2
            || grpFileForm.getPsnId().longValue() == grpFileShowInfo.getUploadPsnId().longValue()) {
          grpFileShowInfo.setShowDel(true);
          grpFileShowInfo.setShowEdit(true);
        }
        grpFileShowInfos.add(grpFileShowInfo);
        PsnInfo psnInfo = buildPsnInfoService.buildPersonInfo(grpFileList.get(i).getUploadPsnId(), 1);
        map.put(grpFileList.get(i).getUploadPsnId(), psnInfo.getName());
      }
    }

  }

  /**
   * 构建文件显示信息
   * 
   * @param grpFileList
   * @param i
   * @param role 1=群组拥有者,2=管理员,3=组员
   * @return
   */
  private GrpFileShowInfo buildGrpFileShowInfo(List<GrpFile> grpFileList, int i, GrpFileForm grpFileForm) {
    GrpFileShowInfo grpFileShowInfo = new GrpFileShowInfo();
    grpFileShowInfo.setArchiveFileId(grpFileList.get(i).getArchiveFileId());
    grpFileShowInfo.setFileDesc(grpFileList.get(i).getFileDesc());
    grpFileShowInfo.setFileModuleType(grpFileList.get(i).getFileModuleType());
    grpFileShowInfo.setFileName(grpFileList.get(i).getFileName());
    grpFileShowInfo.setFilePath(grpFileList.get(i).getFilePath());
    grpFileShowInfo.setFileSize(grpFileList.get(i).getFileSize());
    grpFileShowInfo.setFileStatus(grpFileList.get(i).getFileStatus());
    grpFileShowInfo.setFileType(grpFileList.get(i).getFileType());
    grpFileShowInfo.setGrpFileId(grpFileList.get(i).getGrpFileId());
    grpFileShowInfo.setGrpId(grpFileList.get(i).getGrpId());
    grpFileShowInfo.setUpdateDate(grpFileList.get(i).getUpdateDate());
    grpFileShowInfo.setUploadDate(grpFileList.get(i).getUploadDate());
    grpFileShowInfo.setUploadPsnId(grpFileList.get(i).getUploadPsnId());
    // SCM-14409 hcj
    String downUrl = "";
    if (grpFileForm.getShortFileUrl()) {
      downUrl = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.GROUP, grpFileList.get(i).getGrpFileId(), 0L);
    } else {
      downUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.GROUP, grpFileList.get(i).getGrpFileId(), 0L);
    }
    grpFileShowInfo.setDownloadUrl(downUrl);
    // 图片类型文件设置缩略图url，没有缩略图或者不是图片类型文件返回空字符串
    grpFileShowInfo.setImgThumbUrl(archiveFileService.getImgFileThumbUrl(grpFileShowInfo.getArchiveFileId()));
    // 构建文件标签start
    List<GrpFileLabel> fileLabelList = grpFileLabelDao.findLabelByFileId(grpFileList.get(i).getGrpFileId());
    List<GrpFileLabelShowInfo> labelShowInfoList = new ArrayList<>();
    int role = grpFileForm.getGrpRole();
    Long psnId = grpFileForm.getPsnId();
    if (fileLabelList != null && fileLabelList.size() > 0) {
      for (GrpFileLabel grpFileLabel : fileLabelList) {
        GrpLabel grpLabel = grpLabelDao.get(grpFileLabel.getGrpLabelId());
        GrpFileLabelShowInfo showInfo = new GrpFileLabelShowInfo();
        showInfo.setLabelName(grpLabel.getLabelName());
        showInfo.setDes3FileLabelId(Des3Utils.encodeToDes3(grpFileLabel.getId().toString()));
        showInfo.setDes3GrpFileId(Des3Utils.encodeToDes3(grpFileList.get(i).getGrpFileId().toString()));
        showInfo.setDes3GrpLabelId(Des3Utils.encodeToDes3(grpFileLabel.getGrpLabelId().toString()));
        if (role == 1 || role == 2 || grpFileLabel.getCreatePsnId().longValue() == psnId.longValue()) {
          showInfo.setShowDel(true);
        }
        labelShowInfoList.add(showInfo);

      }
      grpFileShowInfo.setGrpFileLabelShowInfoList(labelShowInfoList);
    }
    // 构建文件标签end
    return grpFileShowInfo;
  }

  /**
   * 得到psnName
   * 
   * @param person
   * @return
   */
  private String getPsnName(Person person) {
    if (person == null) {
      return "";
    }
    if (StringUtils.isNotBlank(person.getName())) {
      return person.getName();
    }
    return person.getEname();

  }

  @Override
  public Boolean checkFilePermit(GrpFileForm grpFileForm) throws Exception {
    int role = grpRoleService.getGrpRole(grpFileForm.getPsnId(), grpFileForm.getGrpId());
    if (role == 1 || role == 2) {
      return true;
    } else {
      if (checkIsMyGrpFile(grpFileForm.getGrpId(), grpFileForm.getPsnId(), grpFileForm.getGrpFileId())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Boolean checkIsMyGrpFile(Long grpId, Long psnId, Long grpFileId) throws Exception {
    return grpFileDao.checkIsMyGrpFile(grpId, psnId, grpFileId);
  }

  @Override
  public void addMyFileForGrp(GrpFileForm grpFileForm) throws Exception {
    List<Long> grpFileIdList = new ArrayList<Long>();
    grpFileForm.setGrpFileIdList(grpFileIdList);
    GrpFile firstGrpfile = null; // 第一个群组文件

    for (int i = 0; i < grpFileForm.getStationFileIdList().size(); i++) {
      // 我的文件存在
      GrpFile grpFile = new GrpFile();
      // StationFile stationFile =
      // stationFileDao.get(grpFileForm.getStationFileIdList().get(i));
      PsnFile psnFile = psnFileDao.get(grpFileForm.getStationFileIdList().get(i));
      ArchiveFile archiveFile = archiveFileDao.get(psnFile.getArchiveFileId());
      if (psnFile != null) {
        grpFile.setArchiveFileId(psnFile.getArchiveFileId());
        grpFile.setFileDesc(psnFile.getFileDesc());
        // grpFile.setFileId(stationFile.getFileId());
        if (grpFileForm.getGrpCategory() != null && 10 == grpFileForm.getGrpCategory()) { // 课程群组
          if (2 == grpFileForm.getGrpFileType() && grpFileForm.getGrpRole() < 3) {
            grpFile.setFileModuleType(2); // 文件类型课件
          } else {
            grpFile.setFileModuleType(1); // 文件类型
          }
        } else { // 其他群组
          grpFile.setFileModuleType(0); // 文件类型
        }
        grpFile.setFileName(psnFile.getFileName());
        // 暂时保存空
        grpFile.setFilePath(archiveFile == null ? 0 + "" : archiveFile.getFileUrl());
        grpFile.setFileSize(archiveFile == null ? 0 : archiveFile.getFileSize());
        grpFile.setFileStatus(psnFile.getStatus());
        grpFile.setFileType(psnFile.getFileType());
        grpFile.setGrpId(grpFileForm.getGrpId());
        grpFile.setUpdateDate(new Date());
        grpFile.setUploadDate(new Date());
        grpFile.setUploadPsnId(grpFileForm.getPsnId());
        grpFileDao.save(grpFile);

        GrpStatistics grpStatistics = grpStatisticsDao.get(grpFileForm.getGrpId());
        grpStatistics.setSumFile(grpStatistics.getSumFile() + 1);
        grpStatisticsDao.save(grpStatistics);
        grpFileIdList.add(grpFile.getGrpFileId());
      }
      if (i == 0) {
        firstGrpfile = grpFile;
      }
    }
    // 产生邮件
    if (grpFileForm.getIsNeedSendGroupEmail() == 1 && grpFileForm.getGrpFileIdList().size() > 0) {
      grpFileEmailService.sendUploadFileEamilNotify(firstGrpfile, grpFileForm.getGrpFileIdList().size());
      logger.info("************************发送群上传个人文件组邮件通知**************************************");
    }

  }

  public void sendEmailToGroupMember(GrpFileForm grpFileForm, GrpFile grpFile) {
    if (grpFileForm.getStationFileIdList() == null || grpFileForm.getStationFileIdList().size() < 1) {
      return;
    }

    Map<String, Object> emailMap = new HashMap<String, Object>();
    Person senderPsn = personDao.getPersonName(SecurityUtils.getCurrentUserId());// 上传文件到群组的人员
    String senderPsnName = "";// 发出邀请的人名
    String receiverPsnName = "";// 被邀请加入群组的人
    String subject = "";// 邮件主题
    String mailTemplate = "";// 邮件模板
    String sendFileType = "";// 文件类型
    String subjectType = "";
    String count = "";
    count = grpFileForm.getStationFileIdList().size() == 1 ? "a"
        : Integer.toString(grpFileForm.getStationFileIdList().size());
    String grpName = grpBaseInfoDao.getGrpNameByGrpId(grpFileForm.getGrpId());
    List<Long> grpMembers = grpMemberDao.getGrpMembers(grpFileForm.getGrpId());
    for (Long psnId : grpMembers) {
      if (psnId.equals(SecurityUtils.getCurrentUserId())) {
        continue;
      }
      Person receiverPsn = personDao.get(psnId);// 被邀请加入群组的人
      if ("zh_CN".equals(receiverPsn.getEmailLanguageVersion())) {
        if (2 == grpFileForm.getGrpFileType()) {
          sendFileType = "Multimedia"; // 文件类型教学课件
          subjectType = "教学课件";

        } else {
          sendFileType = "File"; // 文件类型
          subjectType = "文件";

        }
        senderPsnName = StringUtils.isEmpty(senderPsn.getName()) ? senderPsn.getFirstName() + senderPsn.getLastName()
            : senderPsn.getName();
        receiverPsnName =
            StringUtils.isEmpty(receiverPsn.getName()) ? receiverPsn.getFirstName() + receiverPsn.getLastName()
                : receiverPsn.getName();
        subject = senderPsnName + "在群组“" + grpName + "”添加了"
            + Integer.toString(grpFileForm.getStationFileIdList().size()) + "个" + subjectType;
        mailTemplate = "Group_Add_New_" + sendFileType + "_Template_zh_CN.ftl";

      } else {
        if (2 == grpFileForm.getGrpFileType()) {
          sendFileType = "Multimedia"; // 文件类型教学课件
          subjectType = "course material";

        } else {
          sendFileType = "File"; // 普通文件
          subjectType = "file";

        }
        senderPsnName = senderPsn.getFirstName() + " " + senderPsn.getLastName();
        if (StringUtils.isBlank(senderPsn.getFirstName()) || StringUtils.isBlank(senderPsn.getLastName())) {
          senderPsnName = senderPsn.getName();
        }
        receiverPsnName = receiverPsn.getFirstName() + " " + receiverPsn.getLastName();
        if (StringUtils.isBlank(receiverPsn.getFirstName()) || StringUtils.isBlank(receiverPsn.getLastName())) {
          receiverPsnName = receiverPsn.getName();
        }
        subject = senderPsnName + " added " + count + " " + sendFileType + " to group" + '"' + grpName + '"';
        mailTemplate = "Group_Add_New_" + sendFileType + "_Template_en_US.ftl";
      }

      String fileUrl = domainscm + "/groupweb/grpinfo/main?grpId=" + grpFileForm.getGrpId() + "&model=file";
      String shareFileUrl = domainscm + "/groupweb/grpinfo/main?grpId=" + grpFileForm.getGrpId()
          + "&model=file&searchKey=" + grpFile.getFileName();
      String operateUrl = domainscm + "/scmwebsns/resume/psnView?des3PsnId="
          + ServiceUtil.encodeToDes3(senderPsn.getPersonId().toString()) + "&menuId=1100";
      emailMap.put("psnUrl", operateUrl);
      emailMap.put("shareFileUrl", shareFileUrl);
      // 设置模板参数
      emailMap.put("sender", senderPsnName);
      emailMap.put("receiver", receiverPsnName);
      emailMap.put("domainUrl", domainscm);
      emailMap.put("fileUrl", fileUrl);
      emailMap.put("groupName", grpName);
      emailMap.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
      emailMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, receiverPsn.getEmail());
      emailMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, mailTemplate);
      emailMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, receiverPsn.getPersonId());
      emailMap.put(EmailConstants.EMAIL_SENDER_PSNID_KEY, senderPsn.getPersonId());
      emailMap.put("fileCount", grpFileForm.getStationFileIdList().size());
      emailMap.put("fileNames", grpFile.getFileName());
      try {
        mailInitDataService.saveMailInitData(emailMap);
      } catch (Exception e) {
        logger.info("保存邀请加入群组邮件初始化信息出错,群组id是" + grpFileForm.getGrpId() + "被邀请加入群组的人员id是：" + psnId, e);
      }
    }
  }

  @Override
  public Boolean checkExitGrpFile(Long grpFileId) throws Exception {
    return grpFileDao.checkGrpFileExit(grpFileId);
  }

  @Override
  public void editGrpFile(GrpFileForm grpFileForm) throws Exception {
    grpFileDao.editGrpFileDesc(grpFileForm.getGrpFileId(), grpFileForm.getGrpId(),
        IrisStringUtils.subMaxLengthString(grpFileForm.getGrpFileDesc(), 500));

  }

  @Override
  public void deleteGrpFile(GrpFileForm grpFileForm) throws Exception {
    List<String> des3FileIds = new ArrayList<String>();
    for (int i = 0; i < grpFileForm.getGrpFileIdList().size(); i++) {
      grpFileForm.setGrpFileId(grpFileForm.getGrpFileIdList().get(i));
      if (checkFilePermit(grpFileForm)) {
        grpFileDao.deleteGrpFile(grpFileForm.getGrpFileId(), grpFileForm.getGrpId());
        GrpStatistics grpStatistics = grpStatisticsDao.get(grpFileForm.getGrpId());
        grpStatistics.setSumFile(grpStatistics.getSumFile() - 1);
        grpStatisticsDao.save(grpStatistics);
        grpFileForm.setBatchCount(grpFileForm.getBatchCount() + 1);
        des3FileIds.add(Des3Utils.encodeToDes3(grpFileForm.getGrpFileId().toString()));
      }

    }
    grpFileForm.setDelDes3FileIds(des3FileIds);
  }

  @Override
  public void collectGrpFile(GrpFileForm grpFileForm) throws Exception {
    for (int i = 0; i < grpFileForm.getGrpFileIdList().size(); i++) {
      grpFileForm.setGrpFileId(grpFileForm.getGrpFileIdList().get(i));
      // 先拿到群组文件
      GrpFile grpFile = grpFileDao.get(grpFileForm.getGrpFileId());
      if (grpFile != null) {
        // 判断，之前有没有收藏，
        PsnFile psnFile = psnFileDao.findPsnFileByPsnIdArchiveId(grpFileForm.getPsnId(), grpFile.getArchiveFileId());
        if (psnFile == null) {
          psnFile = new PsnFile();
          psnFile.setOwnerPsnId(grpFileForm.getPsnId());
          psnFile.setFileName(grpFile.getFileName());
          psnFile.setFileType(grpFile.getFileType());
          psnFile.setArchiveFileId(grpFile.getArchiveFileId());
          psnFile.setPermission(0);
        }
        psnFile.setUploadDate(new Date());
        psnFile.setUpdateDate(psnFile.getUploadDate());
        psnFile.setStatus(0);
        psnFile.setFileDesc(grpFile.getFileDesc());
        psnFileDao.save(psnFile);
        grpFileForm.setBatchCount(grpFileForm.getBatchCount() + 1);
      }
    }
  }

  @Override
  public int flagGrpFileType(GrpFileForm grpFileForm) throws Exception {
    return grpFileDao.updateGrpFileType(grpFileForm.getGrpFileId(), grpFileForm.getGrpId(),
        grpFileForm.getGrpFileType());
  }

  @Override
  public void findGrpFileMember(GrpFileForm grpFileForm) throws Exception {
    List<GrpFileMember> grpFileMemberList = new ArrayList<GrpFileMember>();
    grpFileForm.setGrpFileMemberList(grpFileMemberList);
    List<Object[]> list = grpFileDao.findGrpFileMember(grpFileForm.getGrpId(), grpFileForm.getGrpFileType());
    if (list != null && list.size() > 0) {
      for (int i = 0; i < list.size(); i++) {
        boolean flag = false;
        GrpFileMember grpFileMember = new GrpFileMember();
        grpFileMember.setMemberFileNum(NumberUtils.toInt(list.get(i)[1].toString()));
        Long psnId = NumberUtils.toLong(list.get(i)[0].toString());
        PsnInfo psnInfo = buildPsnInfoService.buildPersonInfo(psnId, 1);
        grpFileMember.setMemberName(psnInfo.getName());
        grpFileMember.setMemberAvator(psnInfo.getPerson().getAvatars());
        grpFileMember.setDes3MemberId(Des3Utils.encodeToDes3(psnId.toString()));
        // 在这里过滤查询的人员关键词
        if (StringUtils.isNotBlank(grpFileForm.getSearchGrpFileMemberName())) {
          String searchKey = grpFileForm.getSearchGrpFileMemberName().toLowerCase();
          String name = psnInfo.getPerson().getName();
          String eName = psnInfo.getPerson().getEname();
          String firstName = psnInfo.getPerson().getFirstName();
          String lastName = psnInfo.getPerson().getLastName();
          if (StringUtils.isNotBlank(name) && name.toLowerCase().contains(searchKey)) {
            flag = true;
          }
          if (StringUtils.isNotBlank(eName) && eName.toLowerCase().contains(searchKey)) {
            flag = true;
          }
          if (StringUtils.isNotBlank(firstName) && firstName.toLowerCase().contains(searchKey)) {
            flag = true;
          }
          if (StringUtils.isNotBlank(lastName) && lastName.toLowerCase().contains(searchKey)) {
            flag = true;
          }
          if (flag) {
            grpFileMemberList.add(grpFileMember);
          }
          continue;
        }
        grpFileMemberList.add(grpFileMember);
      }

    }

  }

  @SuppressWarnings("unchecked")
  @Override
  public void saveUploadGrpFile(GrpFileForm grpFileForm) throws Exception {

    ArchiveFile archiveFile = archiveFileDao.get(grpFileForm.getArchiveFileId());
    GrpFile grpFile = new GrpFile();
    if (archiveFile != null && archiveFile.getCreatePsnId().equals(grpFileForm.getPsnId())) {
      // grpFileForm.setStationFileId(groupFile.getFileId());
      // grpFileForm.setGrpFileType(archiveFile.getFileModuleType());
      grpFile.setArchiveFileId(archiveFile.getFileId());
      grpFile.setFileDesc(grpFileForm.getGrpFileDesc());
      // grpFile.setFileId(groupFile.getFileId());
      grpFile.setFileModuleType(grpFileForm.getGrpFileType());
      grpFile.setFileName(archiveFile.getFileName());
      String fileUrl = archiveFile.getFileUrl();
      grpFile.setFilePath(fileUrl);
      grpFile.setFileSize(archiveFile.getFileSize());
      grpFile.setFileStatus(0);
      grpFile.setFileType(archiveFile.getFileType());
      grpFile.setGrpId(grpFileForm.getGrpId());
      grpFile.setUpdateDate(archiveFile.getCreateTime());
      grpFile.setUploadDate(grpFile.getUpdateDate());
      grpFile.setUploadPsnId(grpFileForm.getPsnId());
      grpFileDao.save(grpFile);
      GrpStatistics grpStatistics = grpStatisticsDao.get(grpFileForm.getGrpId());
      grpStatistics.setSumFile(grpStatistics.getSumFile() + 1);
      grpStatisticsDao.save(grpStatistics);
      grpFileForm.setGrpFileId(grpFile.getGrpFileId());

      // 图片类型的文件创建缩略图生成任务
      if (archiveFileService.isImageFile(archiveFile)) {
        batchJobsService.createAndSaveThumbnailBatchJob(archiveFile, FileTypeEnum.GROUP);
      }

      // 群组新增文件邮件
      if (grpFileForm.getIsNeedSendGroupEmail() == 1) {
        grpFileEmailService.sendUploadFileEamilNotify(grpFile, 1);
        logger.info("************************发送群上传文件组邮件通知**************************************");
      }
      // 如果存在文件标签，则保存。
      if (grpFileForm.getGrpLabelIdList() != null && grpFileForm.getGrpLabelIdList().size() > 0) {
        grpFileLabelService.saveUploadFileLabel(grpFileForm);
      }
    }
    // 群组新增文件邮件
    /*
     * if ("1".endsWith(grpFile.getFileType())) { sendEmailToGroupMember(grpFileForm, grpFile); }
     */

  }

  @Override
  public GrpFile findGrpFile(GrpFileForm grpFileForm) throws Exception {
    GrpFile grpFile = grpFileDao.get(grpFileForm.getGrpFileId());
    return grpFile;
  }

  @Override
  public GrpFile shareGrpFiles(GrpFileForm form) throws Exception {
    // 循环保存记录 循环发送消息
    Long baseId = grpFileShareBaseDao.getId();
    StringBuilder msgRelationIds = new StringBuilder();
    Date currentDat = new Date();
    String[] des3ReceiverIds = form.getDes3ReceiverIds().split(",");
    List<Long> grpFileIdList = form.getGrpFileIdList();
    // String[] fileNames = form.getFileNames().split(",");
    String firstFileName = "";
    Person sender = personDao.get(form.getPsnId());
    for (String des3ReceiverId : des3ReceiverIds) {
      Long receiverId = Long.parseLong(Des3Utils.decodeFromDes3(des3ReceiverId));
      if (receiverId != null) {
        // 循环保存记录 与发送消息,目前只有单条记录分享
        Long archiveFileId = 0L;
        for (Long grpFileId : grpFileIdList) {
          if (grpFileId != null) {
            // 还需要判断一下文件是不是属于自己的
            GrpFile grpFile = grpFileDao.get(grpFileId);
            if (grpFile == null) {
              throw new Exception("分享的文件不属于该群组的 。 分享人的psnId=" + form.getPsnId() + "  分享的文件id=" + grpFileId + " 群组id="
                  + form.getGrpId());
            }
            if (StringUtils.isBlank(firstFileName)) {
              firstFileName = grpFile.getFileName();
            }
            archiveFileId = grpFile.getArchiveFileId();
            // 发送消息 保存记录
            // 先发消息
            // 调open接口发送消息
            Map<String, Object> map1 = buildSendMsgParam(form, receiverId, grpFileId);
            Object obj = restTemplate.postForObject(this.openResfulUrl, map1, Object.class);
            logger.info(obj.toString());
            Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
            Long msgRelationId = 0L;
            if (resultMap != null && "success".equals(resultMap.get("status"))) {
              List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
              if (result != null && result.size() > 0) {
                if (result.get(0).get("notPermissionPsnIds") != null) {
                  break;
                }
                msgRelationId = NumberUtils.toLong(result.get(0).get("msgRelationId").toString());
              }
            }
            // 在发送保存记录

            GrpFileShareRecord record = new GrpFileShareRecord();
            record.setSharerId(form.getPsnId());
            record.setReveiverId(receiverId);
            record.setGrpFileId(grpFileId);;
            record.setStatus(0);
            record.setCreateDate(currentDat);
            record.setUpdateDate(currentDat);
            record.setMsgRelationId(msgRelationId);
            record.setShareBaseId(baseId);
            grpFileShareRecordDao.save(record);
          }
        }
        // 发送邮件给这个人
        Person receiver = personDao.get(receiverId);
        // TODO baseId 群组文件中目前还没有
        restSendShareGrpFileEmail(sender, receiver, firstFileName, form.getTextContent(), grpFileIdList.size(),
            form.getGrpId(), baseId);
      }
      if (StringUtils.isNotBlank(form.getTextContent())) {
        // 发送消息
        Map<String, Object> map1 = buildSendMsgParam(form, receiverId);
        Object obj = restTemplate.postForObject(this.openResfulUrl, map1, Object.class);
        Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
        if (resultMap != null && "success".equals(resultMap.get("status"))) {
          List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
          if (result != null && result.size() > 0) {
            if (result.get(0).get("notPermissionPsnIds") != null) {
              break;
            }
            msgRelationIds.append(result.get(0).get("msgRelationId").toString() + ",");
          }
        }
      }
    }

    // 更新 分享记录基础表
    GrpFileShareBase shareBase = new GrpFileShareBase();
    shareBase.setId(baseId);
    shareBase.setSharerId(form.getPsnId());
    shareBase.setGrpId(form.getGrpId());
    shareBase.setStatus(0);
    shareBase.setCreateDate(new Date());
    shareBase.setUpdateDate(shareBase.getCreateDate());
    if (StringUtils.isNotBlank(msgRelationIds) && msgRelationIds.length() > 0) {
      shareBase.setShareContentRel(msgRelationIds.substring(0, msgRelationIds.lastIndexOf(",")));
    }
    grpFileShareBaseDao.save(shareBase);
    return null;
  }

  /**
   * 调用接口发送群组分享文件邮件
   * 
   * @param sender
   * @param receiver
   * @param file
   * @param content
   * @param total
   * @param grpId
   * @param baseId
   * @throws Exception
   */
  private void restSendShareGrpFileEmail(Person sender, Person receiver, String file, String content, int total,
      Long grpId, Long baseId) throws Exception {
    // 全文请求使用新模板
    if (sender == null || receiver == null || file == null) {
      throw new Exception("构建文件回复，邮件对象为空" + this.getClass().getName());
    }
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    String language = StringUtils.isNotBlank(receiver.getEmailLanguageVersion()) ? receiver.getEmailLanguageVersion()
        : sender.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }

    // 跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    String senderPersonUrl = "";
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      senderPersonUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl();
    }
    String viewUrl = this.domainscm + "/psnweb/fileshare/emailviewfiles?des3GrpId="
        + Des3Utils.encodeToDes3(grpId.toString()) + "&A=" + sender.getPersonDes3Id() + "&B="
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
    MailLinkInfo l3 = new MailLinkInfo();
    l3.setKey("viewUrl");
    l3.setUrl(viewUrl);
    l3.setUrlDesc("群组文件列表");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));

    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    Integer tempcode = 10102;
    info.setSenderPsnId(sender.getPersonId());
    info.setReceiverPsnId(receiver.getPersonId());
    info.setReceiver(receiver.getEmail());
    info.setMsg("群组分享文件邮件");
    info.setMailTemplateCode(tempcode);
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));

    mailData.put("recvName", getPersonName(receiver, language));
    mailData.put("recommendReason", content);
    mailData.put("minEnShareTitle", "\"" + file.trim() + "\"");
    mailData.put("minZhShareTitle", "“" + file.trim() + "”");
    mailData.put("emailTypeKey", 0);
    // 发件人头衔
    mailData.put("psnName", getPersonName(sender, language));
    mailData.put("total", total);
    // 0：其他，1：成果，2：文献， 3：工"\"" + file + "\""作文档；4：项目
    mailData.put("type", "3");
    mailData.put("mailContext", "");

    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    String subjectType = "";
    String subjectCount = mailData.get("total").toString();
    if ("zh".equals(language) || "zh_CN".equals(language)) {
      subjectType = "文件";
    } else {
      subjectType = "files ";
      if ("1".equals(subjectCount)) {
        subjectType = "file";
        subjectCount = "a";
      }
    }
    subjectParamLinkList.add(getPersonName(sender, language));
    subjectParamLinkList.add(subjectCount);
    subjectParamLinkList.add(subjectType);
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));

    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }

  @Override
  public GrpFile shareGrpFilesByEmail(GrpFileForm form) throws Exception {
    // 循环保存记录 循环发送消息
    Long baseId = grpFileShareBaseDao.getId();
    StringBuilder msgRelationIds = new StringBuilder();
    Date currentDat = new Date();
    String[] emails = form.getReceiverEmails().split(",");
    List<Long> grpFileIdList = form.getGrpFileIdList();
    String[] fileNames = form.getFileNames().split(",");
    Person sender = personDao.get(form.getPsnId());
    for (String email : emails) {

      Long receiverId = 0L;
      if (StringUtils.isBlank(email) || !StrUtils.isEmail(email)) {
        continue;
      }
      // 获取已经确认的邮件
      User user = userDao.findByLoginName(email);
      if (user != null) {
        PersonEmailRegister psnEmail = personEmailDao.getPsnEmail(user.getId(), user.getLoginName());
        if (psnEmail.getIsVerify() == 1) {
          receiverId = user.getId();
        }
      }
      if (receiverId != null && receiverId.longValue() == form.getPsnId().longValue()) {
        continue;
      }

      if (receiverId != null) {
        // 循环保存记录 与发送消息,目前只有单条记录分享
        Long archiveFileId = 0L;
        for (Long grpFileId : grpFileIdList) {
          if (grpFileId != null) {
            // 还需要判断一下文件是不是属于自己的
            GrpFile grpFile = grpFileDao.get(grpFileId);
            if (grpFile == null) {
              throw new Exception("分享的文件不属于该群组的 。 分享人的psnId=" + form.getPsnId() + "  分享的文件id=" + grpFileId + " 群组id="
                  + form.getGrpId());
            }
            archiveFileId = grpFile.getArchiveFileId();
            Long msgRelationId = 0L;
            if (receiverId != null && receiverId != 0L) {
              // 发送消息 保存记录
              // 先发消息
              // 调open接口发送消息
              Map<String, Object> map1 = buildSendMsgParam(form, receiverId, grpFileId);
              Object obj = restTemplate.postForObject(this.openResfulUrl, map1, Object.class);
              logger.info(obj.toString());
              Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());

              if (resultMap != null && "success".equals(resultMap.get("status"))) {
                List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
                if (result != null && result.size() > 0) {
                  if (result.get(0).get("notPermissionPsnIds") != null) {
                    break;
                  }
                  msgRelationId = NumberUtils.toLong(result.get(0).get("msgRelationId").toString());
                }
              }
            }

            // 在发送保存记录

            GrpFileShareRecord record = new GrpFileShareRecord();
            record.setSharerId(form.getPsnId());
            record.setReveiverId(receiverId);
            record.setGrpFileId(grpFileId);
            record.setStatus(0);
            record.setCreateDate(currentDat);
            record.setUpdateDate(currentDat);
            record.setMsgRelationId(msgRelationId);
            record.setShareBaseId(baseId);
            grpFileShareRecordDao.save(record);
          }
        }
        if (receiverId != null && receiverId != 0L) {
          Person receiver = personDao.get(receiverId);
          // TODO baseId 群组文件中目前还没有
          restSendShareGrpFileEmail(sender, receiver, fileNames[0], form.getTextContent(), grpFileIdList.size(),
              form.getGrpId(), baseId);
        } else { // 发送邮件给 站外人员
          Person receiver = new Person();
          receiver.setPersonId(0L);
          receiver.setEmail(email);
          restSendShareGrpFileEmail(sender, receiver, fileNames[0], form.getTextContent(), grpFileIdList.size(),
              form.getGrpId(), baseId);
        }

      }
      if (StringUtils.isNotBlank(form.getTextContent()) && receiverId != null && receiverId != 0L) {
        // 发送消息
        Map<String, Object> map1 = buildSendMsgParam(form, receiverId);
        Object obj = restTemplate.postForObject(this.openResfulUrl, map1, Object.class);
        Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
        if (resultMap != null && "success".equals(resultMap.get("status"))) {
          List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
          if (result != null && result.size() > 0) {
            if (result.get(0).get("notPermissionPsnIds") != null) {
              break;
            }
            msgRelationIds.append(result.get(0).get("msgRelationId").toString() + ",");
          }
        }
      }
    }

    // 更新 分享记录基础表
    GrpFileShareBase shareBase = new GrpFileShareBase();
    shareBase.setId(baseId);
    shareBase.setSharerId(form.getPsnId());
    shareBase.setGrpId(form.getGrpId());
    shareBase.setStatus(0);
    shareBase.setCreateDate(new Date());
    shareBase.setUpdateDate(shareBase.getCreateDate());
    if (StringUtils.isNotBlank(msgRelationIds) && msgRelationIds.length() > 0) {
      shareBase.setShareContentRel(msgRelationIds.substring(0, msgRelationIds.lastIndexOf(",")));
    }
    grpFileShareBaseDao.save(shareBase);
    return null;
  }

  /**
   * 构建发消息需要的参数
   * 
   * @param form
   * @return
   */
  private Map<String, Object> buildSendMsgParam(GrpFileForm form, Long receiverId, Long fileId) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    map.put("openid", "99999999");
    map.put("token", "00000000msg77msg");
    dataMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER);
    dataMap.put(MsgConstants.MSG_SENDER_ID, form.getPsnId());
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, receiverId);
    dataMap.put(MsgConstants.MSG_GRP_FILE_ID, fileId);
    dataMap.put(MsgConstants.MSG_GRP_ID, form.getGrpId());
    dataMap.put(MsgConstants.MSG_CONTENT, form.getTextContent());
    dataMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, "file");
    dataMap.put(MsgConstants.MSG_BELONG_PERSON, "false");

    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }

  /**
   * 只发文本消息
   * 
   * @param form
   * @return
   */
  private Map<String, Object> buildSendMsgParam(GrpFileForm form, Long receiverId) throws Exception {
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
   * @param person
   * @param language zh=中文
   * @return
   */
  String getPersonName(Person person, String language) {
    if ("zh".equalsIgnoreCase(language) || "zh_CN".equalsIgnoreCase(language)) {
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
}
