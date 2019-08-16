package com.smate.web.management.service.institution;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gdata.util.ServiceException;
import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.email.dao.MailInitDataDao;
import com.smate.core.base.email.model.MailInitData;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.bpo.BpoInsPortal;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.InsRole;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.ConstDictionaryManage;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.management.dao.institution.bpo.ConstDictionaryBpoDao;
import com.smate.web.management.dao.institution.bpo.InsDomainDao;
import com.smate.web.management.dao.institution.bpo.InsEditRemarkDao;
import com.smate.web.management.dao.institution.bpo.InsSourceDao;
import com.smate.web.management.dao.institution.bpo.InstitutionAttachmentDao;
import com.smate.web.management.dao.institution.bpo.InstitutionBpoDao;
import com.smate.web.management.dao.institution.rol.ConstInsCoordinateDao;
import com.smate.web.management.dao.institution.rol.KpiInsSettingDao;
import com.smate.web.management.dao.institution.rol.RolRoleDao;
import com.smate.web.management.dao.institution.sns.InsSnRegionDao;
import com.smate.web.management.dao.institution.sns.PsnInsDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.institution.bpo.FileUploadSimple;
import com.smate.web.management.model.institution.bpo.InsEditRemark;
import com.smate.web.management.model.institution.bpo.InsSource;
import com.smate.web.management.model.institution.bpo.InstitutionAttachment;
import com.smate.web.management.model.institution.bpo.InstitutionBpo;
import com.smate.web.management.model.institution.bpo.InstitutionRolForm;
import com.smate.web.management.model.institution.rol.ConstInsCoordinate;
import com.smate.web.management.model.institution.rol.KpiInsSetting;
import com.smate.web.management.model.institution.sns.InsRegion;

@Service("institutionBpoService")
@Transactional(rollbackFor = Exception.class)
public class InstitutionBpoServiceImpl implements InstitutionBpoService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InstitutionBpoDao institutionBpoDao;
  @Autowired
  private InstitutionAttachmentDao institutionAttachmentDao;
  @Autowired
  private InsSourceDao insSourceDao;
  @Autowired
  private ConstCnRegionService constCnRegionService;
  @Autowired
  private ConstDictionaryManage constDictionaryManage;
  @Autowired
  private InsDomainDao insDomainDao;
  @Autowired
  private InsPortalDao insPortalDao;
  @Autowired
  private InsEditRemarkDao insEditRemarkDao;
  @Autowired
  private KpiInsSettingDao kpiInsSettingDao;
  @Autowired
  private ConstInsCoordinateDao constInsCoordinateDao;
  @Autowired
  private InsSnRegionDao insSnRegionDao;
  @Autowired
  private UserDao userDao;
  @Autowired
  private PersonDao personDao;
  // 链接有效期限
  private final static int URLLIMITDAY = 2;
  @Autowired
  private MailInitDataDao mailInitDataDao;
  @Autowired
  private InsAliasStatusService insAliasStatusService;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private PsnInsDao psnInsDao;
  @Autowired
  private InstitutionSnsService institutionSnsService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private InstitutionBakService institutionBakService;
  @Autowired
  private RolRoleDao rolRoleDao;
  @Resource(name = "archiveFileService")
  private ArchiveFilesService archiveFilesService;
  @Resource(name = "rolArchiveFilesService")
  private ArchiveFilesService rolArchiveFilesService;
  @Autowired
  private ConstDictionaryBpoDao constDictionaryBpoDao;

  @Override
  public Page<InstitutionBpo> getInstitutionByPage(InstitutionRolForm form, Page page) throws Exception {
    try {
      page = institutionBpoDao.findInstitutionByPage(form.getInsName(), form.getInsSource(), page);
      List<InstitutionBpo> list = page.getResult();
      if (CollectionUtils.isNotEmpty(list)) {
        for (InstitutionBpo ins : list) {
          InstitutionAttachment institutionAttachment = institutionAttachmentDao.get(ins.getId());
          ins.setFaxAttachmentPath(institutionAttachment == null ? null : institutionAttachment.getFaxPath());
          if (form.getInsSource() == null || form.getInsSource().equals(-1)) {
            ins.setInsSource(insSourceDao.get(ins.getId()));
          } else {
            InsSource insSource = new InsSource();
            switch (form.getInsSource()) {
              case 0:
                insSource.setOnlineReg(1);
                break;
              case 1:
                insSource.setInviteReg(1);
                break;
              case 2:
                insSource.setIsisLink(1);
                break;
              case 3:
                insSource.setIsisSync(1);
                break;
            }
            ins.setInsSource(insSource);
          }
        }
      }
      return page;
    } catch (DaoException e) {
      logger.error("分页查找单位数据出现异常：", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public InstitutionRolForm getEditInstitutionDetail(InstitutionRolForm form) throws Exception {
    try {
      InstitutionBpo ins = institutionBpoDao.get(form.getInsId());
      if (ins != null) {
        form.setInsName(ins.getZhName());
        form.setContactPerson(ins.getContactPerson());
        form.setZhAddress(ins.getZhAddress());
        form.setServerEmail(ins.getServerEmail());
        form.setTel(ins.getTel());
        form.setProvinceList(constCnRegionService.getAllProvince());
        List<ConstDictionary> natureList = constDictionaryBpoDao.findConstByCategory("ins_type");
        form.setNatureList(natureList);
        form.setNature(ins.getNature());
        // 单位域名信息
        BpoInsPortal insPortal = this.findInsPortal(ins.getId());
        if (insPortal != null) {
          form.setLogoAddr(insPortal.getLogo());
          String domainTxt = insPortal.getDomain().substring(0, insPortal.getDomain().lastIndexOf(".scholarmate.com"));
          if (domainTxt.lastIndexOf(".") == -1) {
            form.setInsDomain(domainTxt);
          } else {
            form.setInsDomain(domainTxt.substring(0, domainTxt.lastIndexOf(".")));
            form.setInsDomainSuffix(domainTxt.substring(domainTxt.lastIndexOf(".") + 1, domainTxt.length()));
          }
        } else {
          String runEnv = System.getenv("RUN_ENV");
          if ("run".equalsIgnoreCase(runEnv)) {
            form.setInsDomainSuffix("cn");
          } else {
            form.setInsDomainSuffix("bj");
          }
        }

        // 修改单位备注信息总数
        Long insEditRemarkCount = insEditRemarkDao.queryInsEditRemarkCountByInsId(ins.getId());
        form.setInsEditRemarkCount(insEditRemarkCount);

        // 报表统计信息
        KpiInsSetting kpiInsSetting = kpiInsSettingDao.get(ins.getId());
        if (kpiInsSetting != null) {
          form.setStat(kpiInsSetting.getStatOpen());
          form.setCons(kpiInsSetting.getContOpen());
        }

        // 单位经纬度信息
        ConstInsCoordinate constInsCoordinate = constInsCoordinateDao.get(ins.getId());
        if (constInsCoordinate != null) {
          form.setLongitude(constInsCoordinate.getLongitude());
          form.setLatitude(constInsCoordinate.getLatitude());
        }

        // 单位地区信息
        InsRegion insRegion = insSnRegionDao.get(ins.getId());
        if (insRegion != null) {
          if (insRegion.getPrvId() != null || insRegion.getPrvId() != 0) {
            form.setProviceId(insRegion.getPrvId());
            form.setCityList(constCnRegionService.getAllCity(insRegion.getPrvId()));
          }
          if (insRegion.getCyId() != null || insRegion.getCyId() != 0) {
            form.setCityId(insRegion.getCyId());
          }
        }
      }
      return form;
    } catch (Exception e) {
      logger.error("获取单位编辑页面所需信息时系统出现异常：", e);
      throw new Exception(e);
    }
  }

  @Override
  public BpoInsPortal findInsPortal(Long insId) throws Exception {
    try {
      return insId == null ? null : insDomainDao.findInsPortalByInsId(insId);
    } catch (DaoException e) {
      logger.error("获取单位域名信息出错", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public int sendResetPwdEmail(String jsonParam, String scmUrl) throws Exception {
    try {
      int failedTotal = 0;
      List<Map<String, String>> objects = new ArrayList<Map<String, String>>();
      objects = JacksonUtils.jsonToList(jsonParam);
      if (CollectionUtils.isEmpty(objects)) {
        return failedTotal;
      }
      for (Map<String, String> object : objects) {
        Long psnId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(object.get("des3PsnId")));
        if (psnId == null || psnId == 0L) {
          logger.error("发送重置密码邮件错误：psnId为空，所以忽略向该人发邮件！");
          continue;
        }
        User user = userDao.get(psnId);
        Person person = personDao.get(psnId);
        if (person == null || user == null) {
          logger.error("发送重置密码邮件错误", "psnId=" + psnId + "的人不存在，所以忽略向该人发邮件！");
          continue;
        }
        try {
          sendMail(user, person, scmUrl);
        } catch (Exception e) {
          logger.error("整理邮件数据时出错：psnId为空，所以忽略向该人发邮件！：psnId=" + psnId);
        }

      }

    } catch (Exception e) {

    }
    return 0;
  }

  private void sendMail(User user, Person person, String scmUrl) throws Exception {
    try {
      String subject = null;
      String userName = null;
      Map<String, Object> mailMap = new HashMap<String, Object>();
      String languageVersion =
          StringUtils.isBlank(person.getEmailLanguageVersion()) ? "zh_CN" : person.getEmailLanguageVersion();
      if ("zh_CN".equals(languageVersion)) {
        subject = "科研之友–密码重设";
        userName = person.getName();
        if (StringUtils.isBlank(userName)) {
          userName = person.getFirstName() + " " + person.getLastName();
        }
        mailMap.put("from_zh_CN", "科研之友");
      } else {
        subject = "ScholarMate - Reset password";
        userName = person.getFirstName() + " " + person.getLastName();
        if (StringUtils.isBlank(person.getFirstName()) || StringUtils.isBlank(person.getLastName())) {
          userName = person.getName();
        }
        mailMap.put("from_en_US", "ScholarMate");
      }
      user.setTokenChanged((short) 0);// 设置是否确认过重设密码邮件
      userDao.save(user);
      String gen = ServiceUtil.encodeToDes3(Long.toString(System.currentTimeMillis()));
      // 修改了忘记密码的URL路径_MJG_SCM-4073.
      String url = scmUrl + "/scmwebsns/forgetpwd/resetPassword?key="
          + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(user.getId().toString()), "UTF-8") + "&gen="
          + java.net.URLEncoder.encode(gen, "UTF-8") + "&locale=" + languageVersion;
      mailMap.put("userName", userName);
      mailMap.put("forgetEmail", user.getEmail());
      mailMap.put("resetUrl", url);
      mailMap.put("limit", URLLIMITDAY);
      mailMap.put("loginName", user.getLoginName());
      mailMap.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
      mailMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, "RetrievePwd_SimpleTemplate_bpo_" + languageVersion + ".ftl");
      mailMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, user.getId());
      mailMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, user.getEmail());
      MailInitData mid = new MailInitData();
      mid.setCreateDate(new Date());
      mid.setFromNodeId(1);
      mid.setMailData(JacksonUtils.jsonObjectSerializer(mailMap));
      mid.setStatus(1);
      mid.setToAddress(user.getEmail());
      mailInitDataDao.saveMailData(mid);
    } catch (Exception e) {
      logger.error("发送邮件失败", e);
      throw new Exception(e);
    }

  }

  @Override
  public boolean checkInsNameIsUsing(Long insId, String insName) throws Exception {
    InstitutionBpo institution = null;
    try {
      institution = institutionBpoDao.findInstitutionByIdAndName(insId, insName);
    } catch (DaoException e) {
      logger.error("检查单位名称是否被其他单位使用时出现异常", e);
      throw new ServiceException(e);
    }
    return institution != null;
  }

  @Override
  public boolean checkDomainIsUsing(Long insId, String insDomain) throws Exception {
    BpoInsPortal insPortal = null;
    try {
      insPortal = insDomainDao.findInsPortalByCheck(insDomain, insId);
    } catch (Exception e) {
      logger.error("检查单位域名是否被其他单位使用时出现异常", e);
      throw new ServiceException(e);
    }

    return insPortal != null;
  }

  @Override
  public void editInstitution(InstitutionRolForm form) throws Exception {
    try {
      InstitutionBpo institution = institutionBpoDao.get(form.getInsId());
      if (!institution.getInsName().equalsIgnoreCase(form.getInsName())) {
        insAliasStatusService.markNameCg(institution.getId());
      }
      institution.setZhName(form.getInsName());
      institution.setUrl(form.getInsDomain());
      institution.setContactPerson(form.getContactPerson());
      institution.setZhAddress(form.getZhAddress());
      institution.setServerEmail(form.getServerEmail());
      institution.setTel(form.getTel());
      institution.setStat(form.getStat());
      institution.setCons(form.getCons());
      institution.setProvinceId(form.getProviceId());
      institution.setCityId(form.getCityId());
      institution.setLongitude(form.getLongitude());
      institution.setLatitude(form.getLatitude());
      institution.setNature(form.getNature());
      institutionBpoDao.save(institution);
      // 更新单位域名
      BpoInsPortal insPortal = insDomainDao.findInsPortalByInsId(institution.getId());
      if (insPortal == null || !insPortal.getDomain().equalsIgnoreCase(form.getInsDomain())) {
        if (insPortal == null) {
          insPortal = new BpoInsPortal();
          insPortal.setZhTitle(institution.getInsName());
          insPortal.setInsId(institution.getId());
          insPortal.setDefaultLang("zh_CH");
          insPortal.setSwitchLang("1");
          insPortal.setRolNodeId(10000);
          insPortal.setSnsNodeId(1);
        }
        insPortal.setDomain(StringUtils.lowerCase(form.getInsDomain()));
      }
      if (form.getNature() == 6L) {
        insPortal.setVersion(1);
      } else {
        insPortal.setVersion(0);
      }
      insDomainDao.save(insPortal);
      User user = userDao.getUserByUsername(form.getServerEmail());
      Person person = new Person();
      boolean isSysUser = false;
      if (user != null) {
        person = personDao.get(user.getId());
        person = person != null ? person : new Person();
        isSysUser = true;
      } else {
        person.setNewpassword("111111");
      }
      person.setName(form.getContactPerson());
      person.setEmail(form.getServerEmail());
      person.setTel(form.getTel());
      person.setCityId(institution.getRegionId());
      person.setInsCname(institution.getInsName());
      person.setInsName(institution.getInsName());
      person.setInsId(institution.getId());
      if (!isSysUser) {
        personDao.save(person);
      }
      Institution ins = new Institution();
      buildParam(institution, ins);
      InsPortal portal = buildPortal(new InsPortal(), insPortal);
      // 同步信息到sns
      institutionSnsService.save(portal, ins, person);
      // 新注册用户保存登录信息
      if (!isSysUser) {
        user.setPassword(passwordEncoder.encodePassword("111111", null));
        user.setEmail(institution.getServerEmail());
        userDao.save(user);
      }
      // 备份单位信息修改记录
      institutionBakService.backUpInstitution(institution, SecurityUtils.getCurrentUserId());
      // 保存修改单位信息时的备注
      insEditRemarkDao
          .save(new InsEditRemark(institution.getId(), SecurityUtils.getCurrentUserId(), new Date(), form.getRemark()));
    } catch (Exception e) {

    }
  }

  public InsPortal buildPortal(InsPortal portal, BpoInsPortal bpoPortal) {

    portal.setDefaultLang(bpoPortal.getDefaultLang());
    portal.setDomain(bpoPortal.getDomain());
    portal.setEnTitle(bpoPortal.getEnTitle());
    // portal.setIndexPage(bpoPortal.getIndexPage());
    portal.setInitTitle(bpoPortal.getInitTitle());
    portal.setInsId(bpoPortal.getInsId());
    portal.setLogo(bpoPortal.getLogo());
    portal.setOtherRolNodes(bpoPortal.getOtherRolNodes());
    // portal.setRolNodeId(bpoPortal.getRolNodeId());
    // portal.setSnsNodeId(bpoPortal.getSnsNodeId());
    portal.setSwitchLang(bpoPortal.getSwitchLang());
    // portal.setVersion(bpoPortal.getVersion());
    portal.setWebCtx(bpoPortal.getWebCtx());
    portal.setZhTitle(bpoPortal.getZhTitle());
    return portal;

  }

  public void buildParam(InstitutionBpo institution, Institution ins) {
    ins.setCityId(institution.getCityId());
    ins.setId(institution.getId());
    ins.setAbbreviation(institution.getAbbreviation());
    ins.setCons(institution.getCons());
    ins.setContactPerson(institution.getContactPerson());
    ins.setEnabled(institution.getEnabled());
    ins.setEnAddress(institution.getEnAddress());
    ins.setEnName(institution.getEnName());
    // ins.setIsisOrgCode(institution.getIsisOrgCode());
    ins.setLatitude(institution.getLatitude());
    ins.setLongitude(institution.getLongitude());
    ins.setNature(institution.getNature());
    ins.setPostcode(institution.getPostcode());
    ins.setProvinceId(institution.getProvinceId());
    ins.setRegionId(institution.getRegionId());
    ins.setServerEmail(institution.getServerEmail());
    ins.setServerTel(institution.getServerTel());
    ins.setStat(institution.getStat());
    ins.setStatus(institution.getStatus());
    ins.setTel(institution.getTel());
    ins.setUrl(institution.getUrl());
    ins.setZhAddress(institution.getZhAddress());
    ins.setZhName(institution.getZhName());
  }

  @Override
  public Page getInsEditRemarkByPage(Long insId, Page page) throws Exception {
    page = insEditRemarkDao.queryInsEditRemarkByPage(insId, page);
    List<InsEditRemark> insEditRemarkList = page.getResult();
    if (CollectionUtils.isNotEmpty(insEditRemarkList)) {
      for (InsEditRemark insEditRemark : insEditRemarkList) {
        Person person = personDao.get(insEditRemark.getPsnId());
        insEditRemark.setPsnName(person != null ? person.getName() : null);
      }
    }
    return page;
  }

  @Override
  public List<User> getInsAdminByInsId(Long insId) throws Exception {
    List<User> userList = null;
    try {
      List<InsRole> insRoleList = rolRoleDao.getInsRoleByInsIdAndRoleId(insId, 2L);
      if (CollectionUtils.isNotEmpty(insRoleList)) {
        List<Long> userIdList = new ArrayList<Long>();
        for (InsRole insRole : insRoleList) {
          userIdList.add(insRole.getId().getUserId());
        }
        userList = userDao.findUserByUserIdList(userIdList);
      }
    } catch (Exception e) {
      logger.error("取得指定单位的单位管理员信息时出现异常：", e);
      throw new ServiceException(e);
    }

    return userList;
  }

  @Override
  public FileUploadSimple uploadAndSaveInsFax(FileUploadSimple fileUploadSimple) throws Exception {
    try {
      fileUploadSimple = archiveFilesService.uploadInsFax(fileUploadSimple);
      Long insId = NumberUtils.toLong(fileUploadSimple.getDes3Id());
      InstitutionAttachment institutionAttachment = this.institutionAttachmentDao.get(insId);
      if (institutionAttachment != null) {
        institutionAttachment.setFaxPath(fileUploadSimple.getArchiveFile().getFilePath());
        this.institutionAttachmentDao.save(institutionAttachment);
      } else {
        institutionAttachment = new InstitutionAttachment(insId, fileUploadSimple.getArchiveFile().getFilePath());
        this.institutionAttachmentDao.save(institutionAttachment);
      }
    } catch (Exception e) {
      logger.error("上传和保存传真附件失败: ", e);
    }
    return fileUploadSimple;
  }

  @Override
  public FileUploadSimple uploadAndSaveInsLogo(FileUploadSimple fileUploadSimple) throws Exception {
    try {
      Long insId = NumberUtils.createLong(fileUploadSimple.getDes3Id());
      BpoInsPortal insPortal = this.findInsPortal(insId);
      if (insPortal != null) {
        fileUploadSimple = rolArchiveFilesService.uploadInsLogo(fileUploadSimple);
        if (StringUtils.isBlank(fileUploadSimple.getSaveResult())) {
          insPortal.setLogo(fileUploadSimple.getArchiveFile().getFilePath());
          insDomainDao.save(insPortal);
          // 同步信息到sns
          InsPortal portal = buildPortal(new InsPortal(), insPortal);
          insPortalSync(portal);
        }
      } else {
        String domain = fileUploadSimple.getDomain();
        if (StringUtils.isNotBlank(domain)) {
          boolean isUsing = this.checkDomainIsUsingByDomainAndInsId(insId, domain);
          if (!isUsing) {
            fileUploadSimple = this.rolArchiveFilesService.uploadInsLogo(fileUploadSimple);
            if (StringUtils.isBlank(fileUploadSimple.getSaveResult())) {
              insPortal = new BpoInsPortal();
              insPortal.setDomain(domain.toLowerCase());
              insPortal.setInsId(insId);
              insPortal.setDefaultLang("zh_CN");
              insPortal.setSwitchLang("1");
              insPortal.setRolNodeId(10000);
              insPortal.setSnsNodeId(1);
              insPortal.setLogo(fileUploadSimple.getArchiveFile().getFilePath());
              insDomainDao.save(insPortal);
              // 同步信息到sns
              InsPortal portal = buildPortal(new InsPortal(), insPortal);
              insPortalSync(portal);
            }
          } else {
            fileUploadSimple
                .setSaveResult(MapBuilder.getInstance().put("result", "error").put("msg", "此单位域名已被使用").getJson());
          }
        } else {
          fileUploadSimple
              .setSaveResult(MapBuilder.getInstance().put("result", "error").put("msg", "请先填写单位域名").getJson());
        }

      }
      if (StringUtils.isBlank(fileUploadSimple.getSaveResult())) {
        fileUploadSimple.setSaveResult(MapBuilder.getInstance().put("result", "success").put("msg", "上传和保存单位Logo成功")
            .put("path0", "/scmmanagement").put("path", fileUploadSimple.getArchiveFile().getFilePath()).getJson());
      }
      return fileUploadSimple;
    } catch (Exception e) {
      logger.error("上传和保存单位Logo失败: ", e);
      if (StringUtils.isBlank(fileUploadSimple.getSaveResult())) {
        fileUploadSimple.setSaveResult(MapBuilder.getInstance().put("result", "error").put("msg", "上传和保存单位Logo失败")
            .put("path", fileUploadSimple.getArchiveFile().getFilePath()).getJson());
      }
    }

    return fileUploadSimple;
  }

  public boolean checkDomainIsUsingByDomainAndInsId(Long insId, String domain) throws Exception {
    try {
      // 返回空表示域名未被使用或是此单位拥有的域名
      BpoInsPortal insPortal = insDomainDao.findInsPortalByCheck(domain, insId);
      return insPortal != null;
    } catch (Exception e) {
      logger.error("检查单位域名是否被使用出错", e);
      throw new Exception(e);
    }

  }

  public void insPortalSync(InsPortal insPortal) throws Exception {
    try {
      insPortalDao.delete(insPortal);
      insPortalDao.save(insPortal);
    } catch (Exception e) {
      logger.equals("同步机构信息到sns失败，insId=" + insPortal.getInsId());
      throw new Exception(e);
    }

  }

}
