package com.smate.sie.center.task.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.dao.security.role.Sie6InsRoleDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.model.consts.SieConstPosition;
import com.smate.core.base.utils.model.consts.SieConstRegion;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;
import com.smate.core.base.utils.model.rol.SieInsRole;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.service.consts.SieConstPositionService;
import com.smate.core.base.utils.service.consts.SieConstRegionService;
import com.smate.sie.center.task.dao.ImportInsDataFromDao;
import com.smate.sie.center.task.dao.ImportInsDataInfoDao;
import com.smate.sie.center.task.dao.ImportInsDataSourceDao;
import com.smate.sie.center.task.dao.InsGuidDao;
import com.smate.sie.center.task.model.ImportInsDataFrom;
import com.smate.sie.center.task.model.ImportInsDataFromId;
import com.smate.sie.center.task.model.ImportInsDataInfo;
import com.smate.sie.center.task.model.InsGuid;
import com.smate.sie.center.task.model.SieInsInfo;
import com.smate.sie.core.base.utils.dao.ins.SieInsRegionDao;
import com.smate.sie.core.base.utils.dao.ins.SieInstitutionEditHistoryDao;
import com.smate.sie.core.base.utils.dao.psn.SieInsPersonDao;
import com.smate.sie.core.base.utils.model.ins.SieInsRegion;
import com.smate.sie.core.base.utils.model.ins.SieInstitutionEditHistory;
import com.smate.sie.core.base.utils.model.psn.SieInsPerson;
import com.smate.sie.core.base.utils.model.psn.SieInsPersonPk;
import com.smate.sie.core.base.utils.service.ins.SieConstInsTypeService;
import com.smate.sie.core.base.utils.service.ins.SieInsIntegrityService;
import com.smate.sie.core.base.utils.service.psn.InsPersonAddorDelRelationService;
import com.smate.sie.core.base.utils.service.psn.PsnInfoSyncToSnsService;
import com.smate.sie.core.base.utils.service.psn.SieInsPsnLogService;
import com.smate.sie.core.base.utils.string.JPinyinUtil;
import com.smate.sie.core.base.utils.unit.avatars.UnitAvatarsUtils;

/**
 * 批量创建单位接口实现
 * 
 * @author hd xr
 * @description 查重逻辑参考wiki
 * @address http://wiki.oa.irissz.com/pages/viewpage.action?pageId=40928813
 */
@Service("importInsService")
@Transactional(rollbackFor = Exception.class)
public class ImportInsServiceImpl implements ImportInsService {

  Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 文件根路径
   */
  @Value("${sie.file.root}")
  private String rootPath;
  @Autowired
  private ImportInsDataInfoDao importInsDataInfoDao;
  @Autowired
  private ImportInsDataFromDao importInsDataFromDao;
  @Autowired
  private SieConstRegionService sieConstRegionService;
  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;
  @Autowired
  private SieInsRegionDao sieInsRegionDao;
  @Autowired
  private Sie6InsPortalDao sie6InsPortalDao;
  @Autowired
  private SieInstitutionEditHistoryDao sieInstitutionEditHistoryDao;
  @Autowired
  private SieInsPersonDao sieInsPersonDao;
  @Autowired
  private Sie6InsRoleDao sie6InsRoleDao;
  @Autowired
  private ImportInsDataSourceDao importInsDataSourceDao;
  @Autowired
  private InsGuidDao insGuidDao;
  @Autowired
  private SieConstPositionService sieConstPositionService;

  @Autowired
  private CacheService cacheService;
  @Autowired
  private PsnInfoSyncToSnsService psnInfoSyncToSnsService;
  @Autowired
  private SieInsPsnLogService sieInsPsnLogService;

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Autowired
  private InsPersonAddorDelRelationService insPersonAddorDelRelationService;

  @Autowired
  private SieInsIntegrityService sieInsIntegrityService;

  @Autowired
  private SieConstInsTypeService sieConstInsTypeService;



  @Override
  public List<ImportInsDataInfo> findSyncInsList(int batchSize) throws ServiceException {
    List<ImportInsDataInfo> insList = null;
    try {
      insList = importInsDataInfoDao.findSyncInsList(batchSize, 0L);
    } catch (Exception e) {
      logger.error("批量创建单位任务，查询单位信息失败", e);
      throw new ServiceException("IMPORT_INS_DATA_INFO查询单位信息失败", e);
    }
    return insList;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void doInsCreate(ImportInsDataInfo tmpInsInfo) throws ServiceException {
    SieInsInfo info = null;
    try {
      info = collectInsInfo(tmpInsInfo);
      info.setZhName(StringUtils.trim(info.getZhName()));
      Long insId = addIns(info);
      if (insId == null) {
        throw new ServiceException("批量创建单位任务，创建单位失败");
      }
      // 更新单位域名
      addInsPortal(info);
      // 新增更新guid
      addInsGuid(info.getInsId());

      // 判断contactName, contactEmail 是否都不为空，满足则true;
      boolean isCreInsPsn = true;
      if (StringUtils.isBlank(info.getContactEmail()) || StringUtils.isBlank(info.getContactName())) {
        isCreInsPsn = false;
      }
      if (isCreInsPsn) {// contact_email & contract_psn 字段均有值
        Long psnId = this.findPersonId(info.getContactEmail());
        boolean isNewPerson = psnId == null;
        if (isNewPerson) {// contact_email 不存在账号，调用人员注册接口，并将人员信息插入psn_ins表
          String pwd = UUID.randomUUID().toString();
          String avatars = null;
          // 如果contactName . contactEmail 都不为null，则创建人员
          if (isCreInsPsn) {
            // 调用center-open的人员注册服务,保存人员基本信息
            Map<String, Object> resultMap = JacksonUtils.jsonToMap(
                saveRegisterInfo(info.getContactName(), info.getFirstName(), info.getLastName(), info.getPsnEname(),
                    info.getEnName(), info.getContactEmail(), pwd, insId, info.getZhName(), "SIE").toString());
            if ("success".equals(resultMap.get("status"))) {
              List result = (List) resultMap.get("result");
              if (result != null && result.size() > 0) {
                Map temp = (Map) result.get(0);
                psnId = (Long) temp.get("psnId");
                avatars = (String) temp.get("avatars");
              }
            } else {
              String result = (String) resultMap.get("msg");
              throw new ServiceException("批量创建单位任务：调用人员注册接口失败-" + result);
            }
            info.setPsnId(psnId);
            info.setRolAvatar(avatars);
          }
        } else {// contact_email 存在账号，调用SNS接口获取人员信息，将人员信息插入psn_ins表
          info.setPsnId(psnId);
          Person personTmp = this.fillPsnInsBySns(psnId);
          if (personTmp != null) {
            info.setRolAvatar(personTmp.getAvatars());
            info.setTel(personTmp.getTel());
            info.setPosId(personTmp.getPosId());
            info.setPosGrades(personTmp.getPosGrades() == null ? null : personTmp.getPosGrades().toString());
            info.setPosition(personTmp.getPosition());
            info.setPsnRegionId(personTmp.getRegionId());
            info.setSex(personTmp.getSex());
            // 或取到的PsnTitolo字段保存至info对象
            info.setPsnTitolo(personTmp.getTitolo());
          }
        }
        if (isCreInsPsn) {
          addPsnIns(info, isNewPerson);
          // 单位管理员(RO)
          addSysUserRole(info.getPsnId(), 2L, insId);
          // 单位个人用户(M)
          addSysUserRole(info.getPsnId(), 3L, insId);
        }
      }

      // 更新单位信息完整度
      Sie6Institution ins = sie6InstitutionDao.get(insId);
      sieInsIntegrityService.saveIntegrity(ins);
    } catch (Exception e) {
      logger.error("批量创建单位任务失败", e);
      throw new ServiceException(e.getMessage(), e);
    }

  }

  @Override
  public Long addIns(SieInsInfo info) throws ServiceException {
    if (!NumberUtils.isNumber(info.getOrgCode().toString()) || info.getToken() == null) {
      throw new ServiceException("批量创建单位任务，orgcode=" + info.getOrgCode() + " token=" + info.getToken());
    }
    Long orgCode = info.getOrgCode();
    try {
      // 通过orgcode和token，查询现有单位id
      ImportInsDataFrom insByCode = importInsDataFromDao.get(new ImportInsDataFromId(orgCode, info.getToken()));
      //
      Sie6Institution insByName = sie6InstitutionDao.findInsByInsName(info.getZhName());
      if (insByCode == null && insByName == null) {// 新增
        // 查询新单位的ins_id
        // Long insId =sie6InstitutionDao.findNewInsId() ;

        Sie6Institution ins = covertToIns(info);
        ins.setCreateDate(new Date());

        // 添加
        sie6InstitutionDao.save(ins);
        Long insId = ins.getId();
        if (insId == null || insId == 0) {
          logger.error("批量创建单位任务，新增或获取ins_id失败");
          throw new ServiceException("批量创建单位任务，新增或获取ins_id失败");
        }
        // INS_REGION
        this.addOrChangeInsReg(insId, info.getCountryId(), info.getPrvId(), info.getCyId(), info.getDisId());
        info.setInsId(insId);
        // 向数据来源表插入数据
        if (orgCode != null && orgCode > 0) {
          importInsDataFromDao
              .save(new ImportInsDataFrom(info.getInsId(), info.getOrgCode(), info.getToken(), new Date()));
        }
        return insId;

      } else {// 更新
        Long insId = null;
        Sie6Institution temp = null;
        if (insByCode != null) {// 单位存在，根据insId做更新
          Sie6Institution ins = sie6InstitutionDao.get(insByCode.getInsId());
          if (ins == null) {
            throw new ServiceException("批量创建单位任务，根据insId获取单位，单位不存在。orgcode=" + info.getOrgCode() + " token="
                + info.getToken() + " insId=" + insByCode.getInsId());
          }
          if (insByName == null) {
            throw new ServiceException("批量创建单位任务，根据insName获取单位，单位不存在。orgcode=" + info.getOrgCode() + " token="
                + info.getToken() + " insName=" + info.getZhName());
          }
          temp = ins;
          insId = ins.getId();

          // 备份单位的数据
          SieInstitutionEditHistory his = new SieInstitutionEditHistory(temp);
          sieInstitutionEditHistoryDao.save(his);
        } else if (insByCode == null && insByName != null) {
          temp = insByName;
          insId = insByName.getId();

          // 备份单位的数据
          SieInstitutionEditHistory his = new SieInstitutionEditHistory(temp);
          sieInstitutionEditHistoryDao.save(his);
        }
        info.setInsId(insId);
        // 需要考虑更新的字段：联系电话、单位网址、单位地址（这些字段均为原先为空时更新，否则不更新）
        // String contactTel = StringUtils.isBlank(temp.getTel()) ? info.getContactTel() : temp.getTel();
        String url = StringUtils.isBlank(temp.getUrl()) ? info.getUrl() : temp.getUrl();
        String address = StringUtils.isBlank(temp.getZhAddress()) ? info.getAddress() : temp.getZhAddress();
        // 截取字段赋值到uniform_id2
        String uniformId2 = null;
        // 组织机构代码是社会信用代码的9至17位数字
        if (info.getUniformId1() != null && info.getUniformId1().length() == 18) {
          uniformId2 = info.getUniformId1().substring(8, 17);
          // 社会统一信用代码 uniform_id1
          if (temp.getUniformId1() == null) {
            temp.setUniformId1(info.getUniformId1());
          }
          if (temp.getUniformId2() == null) {
            temp.setUniformId2(uniformId2);
          }
        }

        // 字段不为空则更新institution表联系人相关信息
        if (temp.getTel() == null) {
          temp.setTel(info.getContactTel());
        }
        if (temp.getUrl() == null) {
          temp.setUrl(url);
        }
        if (temp.getZhAddress() == null) {
          temp.setZhAddress(address);
        }
        if (temp.getContactEmail() == null) {
          temp.setContactEmail(info.getContactEmail());
        }
        if (temp.getContactPerson() == null) {
          temp.setContactPerson(info.getContactName());
        }

        temp.setUpdateDate(new Date());

        // 批量创建任务，设置单位机构开通方式，1 表单注册 2 任务创建 3 接口创建 4 机构主页
        temp.setDataFrom(2);// 2 任务创建

        // INS_REGION
        this.addOrChangeInsReg(insId, info.getCountryId(), info.getPrvId(), info.getCyId(), info.getDisId());

        // 直接更新
        sie6InstitutionDao.save(temp);
        if (insByCode == null && insByName != null && orgCode != null && orgCode > 0) {
          // 向数据来源表插入数据
          importInsDataFromDao
              .save(new ImportInsDataFrom(insByName.getId(), info.getOrgCode(), info.getToken(), new Date()));
        }
        return insId;
      }
    } catch (Exception e) {
      logger.error("批量创建单位任务，更新单位信息失败", e);
      throw new ServiceException("批量创建单位任务，更新单位信息失败 -- " + e.getMessage(), e);
    }
  }

  public SieInsRegion addOrChangeInsReg(Long insId, Long countryId, Long prvId, Long cyId, Long disId)
      throws ServiceException {
    if (insId == null) {

      throw new ServiceException("批量创建单位任务，保存更新ins_region表出错，insId不能为空");
    }

    try {
      SieInsRegion ins = sieInsRegionDao.get(insId);
      if (ins == null) {
        ins = new SieInsRegion();
        ins.setInsId(insId);
        ins.setPrvId(prvId);
        ins.setCyId(cyId);
        ins.setDisId(disId);
        ins.setCountryId(countryId);
      } else {
        if (com.smate.core.base.utils.number.NumberUtils.isNullOrZero(ins.getDisId())) {
          ins.setDisId(disId);
        }
        if (com.smate.core.base.utils.number.NumberUtils.isNullOrZero(ins.getCyId())) {
          ins.setCyId(cyId);
        }
        if (com.smate.core.base.utils.number.NumberUtils.isNullOrZero(ins.getPrvId())) {
          ins.setPrvId(prvId);
        }
        if (com.smate.core.base.utils.number.NumberUtils.isNullOrZero(ins.getCountryId())) {
          ins.setCountryId(countryId == null ? 156 : countryId);// 国家编号默认值为156
        }

      }

      sieInsRegionDao.save(ins);
      return ins;
    } catch (Exception e) {
      logger.error("批量创建单位任务，机构所在地表记录失败", e);
      throw new ServiceException("INS_REGION增加或更新失败,insId=" + insId, e);
    }

  }

  public void addInsGuid(Long insId) throws ServiceException {
    if (insId == null) {
      throw new ServiceException("批量创建单位任务，保存更新ins_guid表出错，insId不能为空");
    }
    try {
      InsGuid insGuid = insGuidDao.get(insId);
      if (insGuid == null) {
        String guid = UUID.randomUUID().toString().replace("-", "");
        insGuid = new InsGuid(insId, guid);
        this.insGuidDao.save(insGuid);
      }
    } catch (Exception e) {
      logger.error("批量创建单位任务，保存更新ins_guid表失败", e);
      throw new ServiceException("保存更新ins_guid表失败,insId=" + insId, e);
    }

  }

  @Override
  public void updateTmpInsInfo(ImportInsDataInfo tmpInsInfo) throws ServiceException {
    try {
      importInsDataInfoDao.save(tmpInsInfo);
    } catch (Exception e) {
      logger.error("批量创建单位任务，保存单位更新状态失败,id=" + tmpInsInfo.getId() + ",synFlag=" + tmpInsInfo.getSynFlag(), e);
      throw new ServiceException("IMPORT_INS_DATA_INFO保存单位更新状态失败", e);
    }

  }

  /**
   * 将TmpInsInfo转为InsInfo
   * 
   * @param tmpInsInfo
   * @return
   */
  private SieInsInfo collectInsInfo(ImportInsDataInfo tmpInsInfo) throws Exception {
    SieInsInfo info = new SieInsInfo(tmpInsInfo);
    try {
      // 机构类型
      // Long nature = sieConstDicManage.getNatureByName(tmpInsInfo.getNature());
      Long nature = sieConstInsTypeService.getNatureByName(tmpInsInfo.getNature());// ROL-6348 xr
      // 国家
      Long countryId = sieConstRegionService.findRegionId(tmpInsInfo.getCountry());
      // 省
      Long prvId = sieConstRegionService.findRegionId(tmpInsInfo.getProv());
      // 城市
      Long cyId = sieConstRegionService.findRegionId(tmpInsInfo.getCity());
      // 县区
      Long disId = sieConstRegionService.findRegionId(tmpInsInfo.getDis());

      if (cyId == null && disId != null && disId > 0) {
        SieConstRegion re = sieConstRegionService.getRegionById(disId);
        if (re != null) {
          cyId = re.getSuperRegionId();
        }
      }

      if (prvId == null && cyId != null && cyId > 0) {
        SieConstRegion re = sieConstRegionService.getRegionById(cyId);
        if (re != null) {
          prvId = re.getSuperRegionId();
        }
      }
      if (countryId == null && prvId != null && prvId > 0) {
        SieConstRegion re = sieConstRegionService.getRegionById(prvId);
        if (re != null) {
          countryId = re.getSuperRegionId();
        }
      }
      // region_id
      if (disId != null && disId > 0) {
        info.setRegionId(disId);
      } else if (cyId != null && cyId > 0) {
        info.setRegionId(cyId);
      } else if (prvId != null && prvId > 0) {
        info.setRegionId(prvId);
      } else {
        info.setRegionId(countryId);
      }
      info.setCountryId(countryId);
      info.setPrvId(prvId);
      info.setCyId(cyId);
      info.setDisId(disId);
      info.setNature(nature);
      String url = info.getUrl();
      if (!StringUtils.isBlank(url)) {
        url = url.toString().toLowerCase().trim();
        if (url.startsWith("http://")) {
          url = url.replaceFirst("http://", "");

        } else if (url.startsWith("https://")) {
          url = url.replaceFirst("https://", "");
        }
      }
      info.setUrl(url);
      String token = importInsDataSourceDao.findTokenBySoureceName(tmpInsInfo.getId().getToken());
      if (token != null) {
        info.setToken(token);
      }
      Map<String, String> pinyin = JPinyinUtil.parseFullNamePinYin(info.getContactName());
      if (pinyin != null) {
        info.setFirstName(pinyin.get("firstName"));
        info.setLastName(pinyin.get("lastName"));
        info.setPsnEname(info.getFirstName() + " " + info.getLastName());
      }
      // 社会统一信用代码
      if (tmpInsInfo.getUniformId1() != null) {
        if (tmpInsInfo.getUniformId1().getBytes().length == 18) {
          info.setUniformId1(tmpInsInfo.getUniformId1());
        }
      }

      return info;

    } catch (Exception e) {
      logger.error("批量创建单位任务，将ImportInsDataInfo转为SieInsInfo对象失败", e);
      throw new Exception(e.getMessage(), e);
    }

  }

  /**
   * 将SieInsInfo转为Sie6Institution对象
   * 
   * @param info
   * @return
   * @throws ServiceException
   */
  private Sie6Institution covertToIns(SieInsInfo info) throws ServiceException {
    try {
      Sie6Institution ins = new Sie6Institution();
      // ins.setId(info.getInsId());
      ins.setZhName(StringUtils.trim(info.getZhName()));
      // ins.setEnName(StringUtils.trim(info.getEnName()));
      ins.setTel(info.getContactTel());
      ins.setUrl(info.getUrl());
      ins.setContactPerson(info.getContactName());
      ins.setStatus(info.getStatus());
      ins.setNature(info.getNature());
      ins.setZhAddress(info.getAddress());
      ins.setContactEmail(info.getContactEmail());
      ins.setDataFrom(2);// 设置单位开通方式（2 批量创建）
      ins.setUpdateDate(new Date());
      // 截取字段赋值到uniform_id2
      String uniformId2 = null;
      // 组织机构代码是社会信用代码的9至17位数字
      if (info.getUniformId1() != null && info.getUniformId1().length() == 18) {
        uniformId2 = info.getUniformId1().substring(8, 17);
        // 社会统一信用代码 uniform_id1
        ins.setUniformId1(info.getUniformId1());
        ins.setUniformId2(uniformId2);
      } else {
        ins.setUniformId1("");
        ins.setUniformId2("");
      }
      return ins;
    } catch (Exception e) {
      logger.error("批量创建单位任务，将SieInsInfo转为Sie6Institution对象失败", e);
      throw new ServiceException(e.getMessage(), e);
    }

  }

  @Override
  public void addInsPortal(SieInsInfo info) throws ServiceException {
    if (info.getInsId() == null) {
      logger.error("批量创建单位任务，ins_portal新增单位域名失败，insId不能为空，单位：" + info.getZhName());
      throw new ServiceException("批量创建单位任务，ins_portal新增单位域名失败，insId不能为空，单位：" + info.getZhName());
    }
    try {
      // 查询现有单位
      Sie6InsPortal insPortal = sie6InsPortalDao.get(info.getInsId());
      if (insPortal == null) {// 不存在单位需要增加域名配置

        // 查询新单位的ins_id
        String domain = info.getInsId() + ".scholarmate.com";
        insPortal = new Sie6InsPortal();
        insPortal.setInsId(info.getInsId());
        insPortal.setDomain(domain);
        insPortal.setDefaultLang(info.getDefaultLang());
        insPortal.setSwitchLang(info.getSwitchLang());
        insPortal.setZhTitle(info.getZhName());
        insPortal.setEnTitle(info.getEnName());

        // 生成默认图标（单位名称缩略字母图片）
        String LogoPath = insPortal.getLogo();
        if (StringUtils.isBlank(LogoPath)) {
          LogoPath = this.createInsProtalLogo(info);
        }
        insPortal.setLogo(LogoPath);
      }
      sie6InsPortalDao.saveEntity(insPortal);

    } catch (Exception e) {
      logger.error("批量创建单位任务，新增单位域名失败", e);
      throw new ServiceException("ins_portal新增单位域名失败", e);

    }

  }

  @SuppressWarnings("rawtypes")
  @Override
  public Long findPersonId(String email) throws ServiceException {
    Long psnId = null;
    try {
      Map<String, Object> resultMap = JacksonUtils.jsonToMap(checkEmail(email, "SIE").toString());
      if ("success".equals(resultMap.get("status"))) {
        List result = (List) resultMap.get("result");
        if (result != null && result.size() > 0) {
          Map temp = (Map) result.get(0);
          if (temp.get("psnid") != null && StringUtils.isNotBlank(temp.get("psnid").toString())) {
            psnId = Long.parseLong(temp.get("psnid").toString());
          }

        }
      }
    } catch (Exception e) {
      logger.error("通过email查找已注册用户的psnId出错", e);
      throw new ServiceException(e);
    }
    return psnId;
  }

  private String createInsProtalLogo(SieInsInfo info) {
    String zhName = info.getZhName();
    String enName = info.getEnName();

    String avatars = null;
    try {
      // 生成图标使用的字符串
      String iconStr = UnitAvatarsUtils.getIconStr(zhName, enName);
      if (iconStr.length() > 0) {
        String iconPath = UnitAvatarsUtils.unitAvatars(iconStr, info.getInsId(), rootPath + "/sielogo");
        // 生成图标路径
        avatars = "/sielogo" + iconPath;
        // 保存图标路径
      }
    } catch (Exception e) {
      logger.error("根据单位名称随机产生默认图标失败!");
    }
    return avatars;
  }

  /**
   * 调用center-open系统的接口验证email是否是科研之友账号
   * 
   * @return
   */
  private Object checkEmail(String email, String fromSys) {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    mapDate.put("openid", 99999999L);// 系统默认openId
    mapDate.put("token", "11111111oxui3en2");// 系统默认token
    mapDate.put("data", buildEmialCheckParameter(email, fromSys));
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  /**
   * 构造调用验证email是否是科研之友账号所需的参数
   * 
   * @param email
   * @param fromSys
   * @return
   */
  private String buildEmialCheckParameter(String email, String fromSys) {
    Map<String, Object> date = new HashMap<String, Object>();
    date.put("fromSys", fromSys);
    date.put("email", email);
    return JacksonUtils.mapToJsonStr(date);
  }

  @SuppressWarnings("rawtypes")
  public Person fillPsnInsBySns(Long psnId) throws SysServiceException {
    Person psn = null;
    try {
      Map<String, Object> resultMap = JacksonUtils.jsonToMap(fillPsnInsBySnsparam(psnId).toString());
      if ("success".equals(resultMap.get("status"))) {
        List result = (List) resultMap.get("result");
        if (result != null && result.size() > 0) {
          Map temp = (Map) result.get(0);
          if (temp.get("psnId") == null
              || (temp.get("psnid") != null && StringUtils.isNotBlank(temp.get("psnid").toString())
                  && Long.parseLong(temp.get("psnid").toString()) == psnId)) {
            psn = new Person();
            if (temp.get("lastName") != null) {
              psn.setLastName(temp.get("lastName").toString());
            }
            if (temp.get("sex") != null && StringUtils.isNotBlank(temp.get("sex").toString())
                && StringUtils.isNumeric(temp.get("sex").toString())) {
              psn.setSex(Integer.valueOf(temp.get("sex").toString()));
            }
            if (temp.get("mobile") != null) {
              psn.setMobile(temp.get("mobile").toString());
            }
            if (temp.get("firstName") != null) {
              psn.setFirstName(temp.get("firstName").toString());
            }
            if (temp.get("tel") != null) {
              psn.setTel(temp.get("tel").toString());
            }
            if (temp.get("position") != null) {
              psn.setPosition(temp.get("position").toString());
            }
            if (temp.get("email") != null) {
              psn.setEmail(temp.get("email").toString());
            }
            if (temp.get("avatars") != null) {
              psn.setAvatars(temp.get("avatars").toString());
            }
            if (temp.get("zhName") != null) {
              psn.setName(temp.get("zhName").toString());
            }
            if (temp.get("enName") != null) {
              psn.setEname(temp.get("enName").toString());
            }
            psn.setRegionId(com.smate.core.base.utils.number.NumberUtils.parseLong(temp.get("regionId").toString()));
            if (temp.get("titolo") != null) {
              psn.setTitolo(temp.get("titolo").toString());
            }

          }
          return psn;
        }
      }
      return psn;
    } catch (Exception e) {
      logger.error("远程调用个人段，读取个人数据", e);
      throw new SysServiceException("远程调用个人段，读取个人数据", e);
    }

  }

  private Object fillPsnInsBySnsparam(Long psnId) {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    mapDate.put("openid", 99999999L);// 系统默认openId
    mapDate.put("token", "11111111965eb72c");// 系统默认token
    mapDate.put("data", buildPsnIns(psnId));
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  private String buildPsnIns(Long psnId) {
    Map<String, Object> date = new HashMap<String, Object>();
    date.put("dataType", "SIE");
    date.put("psnId", psnId);
    return JacksonUtils.mapToJsonStr(date);
  }

  /**
   * 调用center-open系统的人员注册服务保存注册人员的基本信息
   * 
   * @return
   */

  private Object saveRegisterInfo(String name, String firstName, String lastName, String enName, String position,
      String email, String pwd, Long insId, String insName, String fromSys) {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    mapDate.put("openid", 99999999L);// 系统默认openId
    String token = (String) cacheService.get("ins_token", "ins_token" + insId);
    if (StringUtils.isBlank(token)) {
      token = this.getInsToken(insId);
      if (StringUtils.isNotBlank(token)) {
        cacheService.put("ins_token", CacheService.EXP_DAY_1, "ins_token" + insId, token);
      } else {
        token = "11111111";// 系统默认token
      }
    }
    mapDate.put("token", token + "3djd2x9s");
    mapDate.put("data", buildRegisterOpenDataParameter(name, firstName, lastName, enName, position, email, pwd, insId,
        insName, fromSys));
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  /**
   * 构造调用开放系统所需的参数 密码要经过MD5加密
   * 
   * @param person
   * @return
   */
  private String buildRegisterOpenDataParameter(String name, String firstName, String lastName, String enName,
      String position, String email, String pwd, Long insId, String insName, String fromSys) {
    Map<String, Object> date = new HashMap<String, Object>();

    String syncXml = "<root><person><name>" + (StringUtils.isNotBlank(name) ? name : "") + "</name><ename>"
        + (StringUtils.isNotBlank(enName) ? enName : "") + "</ename>" + "<firstName>"
        + (StringUtils.isNotBlank(firstName) ? firstName : "") + "</firstName>" + "<lastName>"
        + (StringUtils.isNotBlank(lastName) ? lastName : "") + "</lastName>" + "<email>"
        + (StringUtils.isNotBlank(email) ? email : "") + "</email><insId>" + (insId == null ? "" : insId)
        + "</insId><insName>" + (StringUtils.isNotBlank(insName) ? insName : "") + "</insName><newpassword>"
        + (StringUtils.isNotBlank(pwd) ? DigestUtils.md5Hex(pwd) : "") + "</newpassword><position>"
        + (StringUtils.isNotBlank(position) ? position : "") + "</position></person></root>";
    date.put("fromSys", fromSys);
    date.put("syncXml", syncXml);
    return JacksonUtils.mapToJsonStr(date);
  }

  @Override
  public void addPsnIns(SieInsInfo info, Boolean isNewPerson) throws ServiceException {
    if (info.getPsnId() == null || info.getInsId() == null) {

      throw new ServiceException("批量创建单位任务，psnId和insId不能为空，单位：" + info.getZhName());
    }
    try {
      Long psnId = info.getPsnId();
      // 首次加入单位
      Boolean isNewPsnIns = false;
      SieInsPerson psnIns = sieInsPersonDao.findPsnInsAllStatus(psnId, info.getInsId());
      if (psnIns == null) {
        isNewPsnIns = true;
        psnIns = new SieInsPerson();
        psnIns.setCreateDate(new Date());
      } else {
        psnIns.setUpdateDate(new Date());
      }
      SieInsPersonPk pk = new SieInsPersonPk(psnId, info.getInsId());
      psnIns.setPk(pk);
      psnIns.setZhName(info.getContactName());
      psnIns.setFirstName(info.getFirstName());
      psnIns.setLastName(info.getLastName());
      psnIns.setEnName(info.getPsnEname());
      psnIns.setEmail(info.getContactEmail());
      psnIns.setAvatars(info.getRolAvatar());
      psnIns.setTel(info.getTel());
      psnIns.setMobile(info.getMobile());
      psnIns.setPosId(info.getPosId());
      psnIns.setPosition(info.getPosition());
      psnIns.setPosGrades(info.getPosGrades());
      psnIns.setSex(psnIns.getSex());
      psnIns.setRegionId(info.getPsnRegionId());
      // 增加titolo字段信息
      psnIns.setTitle(info.getPsnTitolo());
      psnIns.setStatus(1);
      // 职称
      String grades = null;
      String position = psnIns.getPosition() == null ? null : psnIns.getPosition().trim();
      if (psnIns.getPosId() == null && StringUtils.isNotBlank(position)) {
        SieConstPosition constPosition = sieConstPositionService.getPosByName(position);
        if (constPosition != null) {
          psnIns.setPosId(constPosition.getId());
          grades = constPosition.getPosGrades();
        } else {
          psnIns.setPosId(null);
          grades = null;
        }
      } else if (psnIns.getPosId() != null) {
        String posGrades = sieConstPositionService.getPosGrades(psnIns.getPosId());
        if (posGrades != null) {
          grades = posGrades;
        } else {
          psnIns.setPosId(null);
          grades = null;
        }

      } else {
        psnIns.setPosId(null);
        grades = null;
      }
      psnIns.setPosGrades(grades);
      sieInsPersonDao.save(psnIns);
      String result = "";
      // 只更新首要邮件，不会重置密码
      if (!isNewPerson) {
        result = psnInfoSyncToSnsService.updatePersonByApprove(psnIns.getPk().getPsnId(), psnIns.getEmail());
        result = result + psnInfoSyncToSnsService.doSync(psnIns);
      }
      String msg = "";
      // 已有账号首次加入单位，添加单位与人员的关联
      if (isNewPsnIns && !isNewPerson) {
        String token = (String) cacheService.get("ins_token", "ins_token" + info.getInsId());
        if (StringUtils.isBlank(token)) {
          token = insPersonAddorDelRelationService.getInsToken(info.getInsId());
          if (StringUtils.isNotBlank(token)) {
            cacheService.put("ins_token", CacheService.EXP_DAY_1, "ins_token" + info.getInsId(), token);
          }
        }
        if (StringUtils.isNotBlank(token)) {
          Map<String, Object> resultMap = insPersonAddorDelRelationService.addRelation(psnId, token);
          if (resultMap != null && resultMap.get("msg") != null) {
            msg = resultMap.get("msg").toString();
          }
        }

      }
      sieInsPsnLogService.log(0L, psnIns.getPk().getInsId(), psnId, "add", "批量创建单位时，单位联系人，添加人员;" + result + ";" + msg);

    } catch (Exception e) {
      logger.error("批量创建单位任务， 增加人员与单位的关系失败，单位：" + info.getZhName(), e);
      throw new ServiceException("psn_ins增加人员与单位的关系失败", e);
    }

  }

  @Override
  public void addSysUserRole(Long psnId, Long roleId, Long insId) throws ServiceException {
    if (psnId == null || insId == null || roleId == null) {

      throw new ServiceException("批量创建单位任务，psnId、insId和roleId不能为空");
    }

    try {
      SieInsRole insRole = sie6InsRoleDao.getUserRole(psnId, insId, roleId);// 查询是否有人员与单位的角色
      if (insRole == null) {

        sie6InsRoleDao.save(new SieInsRole(psnId, insId, roleId));

      }
    } catch (Exception e) {
      logger.error("批量创建单位任务，增加人员与单位的角色失败", e);
      throw new ServiceException("sys_user_role增加人员与单位的角色失败,insId=" + insId, e);
    }

  }

  /**
   * 获取单位token.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("rawtypes")
  private String getInsToken(Long insId) throws ServiceException {
    try {
      String token = null;
      Map<String, Object> postData = new HashMap<String, Object>();
      postData.put("openid", 99999999L);// 系统默认openId
      postData.put("token", "11111111yung90kk");// 系统默认token
      Map<String, Object> data = new HashMap<String, Object>();
      data.put("insId", insId);
      postData.put("data", JacksonUtils.mapToJsonStr(data));
      Map<String, Object> resultMap =
          JacksonUtils.jsonToMap(restTemplate.postForObject(SERVER_URL, postData, Object.class).toString());
      if ("success".equals(resultMap.get("status"))) {
        List result = (List) resultMap.get("result");
        Map tokenMap = (Map) result.get(0);
        token = ObjectUtils.toString(tokenMap.get("token"));
      } else {
        logger.error("获取单位token接口（yung90kk）返回错误结果,insId:{}", new Object[] {insId});
        throw new ServiceException("获取单位token接口（yung90kk）返回错误结果：" + resultMap.toString());
      }
      return token;
    } catch (Exception e) {
      logger.error("获取token出现异常了喔,insId={}", insId, e);
      throw new ServiceException("获取token出现异常了喔", e);
    }
  }

}
