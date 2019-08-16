package com.smate.center.open.service.register;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.consts.ConstPositionDao;
import com.smate.center.open.dao.institution.InstitutionDao;
import com.smate.center.open.dao.psn.EducationHistoryRegisterDao;
import com.smate.center.open.dao.psn.WorkHistoryRegisterDao;
import com.smate.center.open.dao.psnrefreshinfo.PsnRefreshUserInfoDao;
import com.smate.center.open.dao.register.PersonRegisterDao;
import com.smate.center.open.model.consts.ConstPosition;
import com.smate.center.open.model.psn.EducationHistoryRegister;
import com.smate.center.open.model.psn.WorkHistoryRegister;
import com.smate.center.open.model.psnrefresh.PsnRefreshUserInfo;
import com.smate.center.open.model.register.PersonRegister;
import com.smate.center.open.service.reschproject.InstitutionManager;
import com.smate.center.open.service.user.search.UserSearchService;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.utils.person.avatars.PersonAvatarsUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 保存人员基本信息 服务实现
 * 
 * @author tsz
 *
 */
@Service("registerPersonBaseInfoService")
@Transactional(rollbackFor = Exception.class)
public class RegisterPersonBaseInfoServiceImpl implements RegisterPersonBaseInfoService {

  private static Logger logger = LoggerFactory.getLogger(RegisterPersonBaseInfoServiceImpl.class);

  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private PersonRegisterDao personRegisterDao;
  @Autowired
  private WorkHistoryRegisterDao workHistoryRegisterDao;
  @Autowired
  private InstitutionManager institutionManager;
  @Autowired
  private EducationHistoryRegisterDao educationHistoryRegisterDao;
  @Autowired
  private UserSearchService userSearchService;
  @Autowired
  private PsnRefreshUserInfoDao psnRefreshUserInfoDao;
  @Autowired
  private ConstPositionDao constPositionDao;
  /**
   * 文件根路径
   */
  @Value("${file.root}")
  private String rootPath;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 保存人员基本信息
   * 
   * @throws Exception
   */
  @Override
  public void savePersonBaseInfo(PersonRegister person) throws Exception {
    try {
      // 构造对象里面的信息
      // 其他系统同步用户
      if (StringUtils.isBlank(person.getNewpassword())) {
        getPassword(person);
      }
      if (person.getInsId() == null && StringUtils.isNotBlank(person.getInsName())) {
        Long insId = institutionDao.getInsIdByName(person.getInsName(), person.getInsName());
        if (insId != null) {
          person.setInsId(insId);
        }
      }
      person.setRegData(new Date());
      // 先保存人员信息，获取psnid
      personRegisterDao.save(person);
      baseInfo(person);
    } catch (Exception e) {
      logger.error("人员注册，保存人员基础信息出错!", e);
      throw new Exception("人员注册，保存人员基础信息出错!", e);
    }
  }

  private void getPassword(PersonRegister person) {
    // 随机密码
    String uuid = UUID.randomUUID().toString();
    String pass = uuid.substring(uuid.length() - 8);
    person.setNewpassword(DigestUtils.md5Hex(pass));
  }

  /**
   * 处理基础数据
   * 
   * @param person
   * @throws Exception
   */
  private void baseInfo(PersonRegister person) throws Exception {

    try {
      // 重新构建名字相关字段
      if (StringUtils.isNotBlank(person.getLastName()) && StringUtils.isNotBlank(person.getFirstName())) {
        person.setEname(person.getFirstName() + " " + person.getLastName());
      } else {
        Map<String, String> pinyin = ServiceUtil.parsePinYin(person.getName());
        if (pinyin != null) {
          person.setFirstName(pinyin.get("firstName"));
          person.setLastName(pinyin.get("lastName"));
          person.setEname(person.getFirstName() + " " + person.getLastName());
        }
      }
      if ((StringUtils.isBlank(person.getZhFirstName()) || StringUtils.isBlank(person.getZhLastName()))
          && StringUtils.isNotBlank(person.getName())) {
        if (!ServiceUtil.isChineseStr(person.getName())) {
          String[] splited = person.getName().split("\\s+", 2);
          if (splited.length == 2) {
            if (person.getName().indexOf("-") > -1) {
              person.setZhLastName(splited[1]);
              person.setZhFirstName(splited[0]);
            } else {
              person.setZhLastName(splited[0]);
              person.setZhFirstName(splited[1]);
            }
          } else {
            person.setZhLastName(splited[0]);
          }
        } else {
          Map<String, String> splitName = ServiceUtil.parseZhfirstAndLastNew(person.getName());
          person.setZhFirstName(splitName.get("firstNameZh"));
          person.setZhLastName(splitName.get("lastNameZh"));
        }
      }

      if ((StringUtils.isBlank(person.getFirstName()) || StringUtils.isBlank(person.getLastName()))
          && !ServiceUtil.isChineseStr(person.getName())) {
        String[] splited = person.getName().split("\\s+", 2);
        if (splited.length == 2) {
          if (person.getName().indexOf("-") > -1) {
            person.setFirstName(splited[0]);
            person.setLastName(splited[1]);
          } else {
            person.setFirstName(splited[1]);
            person.setLastName(splited[0]);
          }
        } else {
          person.setLastName(splited[0]);
        }
        person.setEname(person.getFirstName() + " " + person.getLastName());
      }

      // 随机生成头像
      if (StringUtils.isNotBlank(person.getFirstName()) || StringUtils.isNotBlank(person.getLastName())) {
        String a =
            StringUtils.isNotBlank(person.getFirstName()) ? person.getFirstName().substring(0, 1).toUpperCase() : "";
        String b =
            StringUtils.isNotBlank(person.getLastName()) ? person.getLastName().substring(0, 1).toUpperCase() : "";
        try {
          String avatars = PersonAvatarsUtils.personAvatars(b + a, person.getPersonId(), rootPath + "/avatars");
          person.setAvatars(domainscm + "/avatars" + avatars);
        } catch (Exception e) {
          logger.error("根据英文名随机产生默认头像失败!");
        }
      }

      if (StringUtils.isBlank(person.getAvatars())) {
        person.setAvatars(domainscm + "/resmod/smate-pc/img/logo_psndefault.png");
      }
      // 补充手机号
      if (StringUtils.isNotBlank(person.getMobile())) {
        person.setTel(person.getMobile());
      }
    } catch (Exception e) {
      logger.error("人员注册，构建基础信息失败!", e);
      throw new Exception("人员注册，构建基础信息失败!", e);
    }
  }

  @Override
  public void saveMobileRegisgerInfo(PersonRegister person) {
    dealParameterNullString(person);
    // 判断是在校 还是 工作
    if ("0".equals(person.getPositionType())) { // 0 在职 psn_work_history
      workSave(person);
    } else if ("1".equals(person.getPositionType())) { // 1 就读
                                                       // psn_edu_history
      eduSave(person);
    }
    // 设置单位、部门、职称
    initPersonPosition(person);
    person.setDepartment(person.getUnit());
    if (person.getInsId() == null && StringUtils.isNotBlank(person.getInsName())) {
      Long insId = institutionDao.getInsIdByName(person.getInsName(), person.getInsName());
      if (insId != null) {
        person.setInsId(insId);
      }
    }
    personRegisterDao.savePerson(person);

  }


  /**
   * 根据用户的职业类型将用户填写的工作单位保存工作经历表
   * 
   * @param person
   */
  private void workSave(PersonRegister person) {

    // 工作经历
    WorkHistoryRegister work = new WorkHistoryRegister();
    // ljj 注册时填写的单位名称默认为首要单位
    work.setPsnId(person.getPersonId());
    work.setIsPrimary(1L);
    // 如果用户注册的单位在单位表有相应单位则将其主键插入到工作经历的关联单位id中
    if (person.getInsId() != null) {
      work.setInsId(person.getInsId());
    }
    if (StringUtils.isNotBlank(person.getInsName())) {
      work.setInsName(person.getInsName());
    } else if (StringUtils.isNotBlank(person.getInsCname())) {
      work.setInsName(person.getInsCname());
    } else if (StringUtils.isNotBlank(person.getInsEname())) {
      work.setInsName(person.getInsEname());
    }
    // 新增用户时，默认以注册时填写的单位为首要工作单位
    if (StringUtils.isNotBlank(person.getInsName())) {
      work.setIsPrimary(1L);
    }
    if (person.getUnit() != null) {
      work.setDepartment(person.getUnit());
    }
    if (StringUtils.isNotEmpty(person.getFromYear())) {
      work.setFromYear(Long.parseLong(person.getFromYear()));
    }
    if (StringUtils.isNotEmpty(person.getFromMonth())) {
      work.setFromMonth(Long.parseLong(person.getFromMonth()));
    }
    // 职务
    String position = person.getPosition() == null ? null : person.getPosition().trim();

    if (StringUtils.isNotBlank(position)) { // 职务不为空
      if (person.getPosId() == null) { // 职务Id为空
        ConstPosition constPosition = constPositionDao.getPosByName(position);
        if (constPosition != null) { // 查询
          work.setPosId(constPosition.getId());
          work.setPosGrades(constPosition.getGrades());
          work.setPosition(position);
        } else {
          work.setPosition(position);
        }
      } else {// 职务Id不为空
        Integer posGrades = constPositionDao.getPosGrades(person.getPosId());
        if (posGrades != null) {
          work.setPosGrades(posGrades);
          work.setPosition(position);
          work.setPosId(person.getPosId());
        } else {
          work.setPosition(position);
          work.setPosId(person.getPosId());
        }
      }
    }
    work.setIsActive(1L);
    workHistoryRegisterDao.save(work);

    // 获取机构信息
    Institution ins = institutionManager.getInstitution(work.getInsName(), work.getInsId());
    userSearchService.updateUserIns(work.getPsnId(), ins);
    // 冗余同步用户信息
    this.savaPsnInfoSyncAndRefresh(work.getPsnId());
  }

  private void savaPsnInfoSyncAndRefresh(Long psnId) {

    PsnRefreshUserInfo psnRefInfo = psnRefreshUserInfoDao.get(psnId);
    if (psnRefInfo == null) {
      psnRefInfo = new PsnRefreshUserInfo(psnId);
    }
    psnRefInfo.setIns(1);
    psnRefInfo.setDegree(1);
    psnRefInfo.setPosition(1);
    psnRefreshUserInfoDao.save(psnRefInfo);
  }

  /**
   * 设置职称
   * 
   * @param person
   */
  private void initPersonPosition(PersonRegister person) {
    if (person.getPosGrades() == null || person.getPosId() == null) {
      // 设置职位
      ConstPosition constPosition = this.constPositionDao.getPosByName(person.getPosition());
      if (constPosition != null) {
        if (person.getPosGrades() == null) {
          person.setPosGrades(constPosition.getGrades());
        }
        if (person.getPosId() == null) {
          person.setPosId(constPosition.getId());
        }
      }
    }
  }

  /**
   * 根据用户的职业类型将用户填写的工作单位教育经历表.
   * 
   * @param person
   */
  private void eduSave(PersonRegister person) {
    EducationHistoryRegister edu = new EducationHistoryRegister();
    edu.setPsnId(person.getPersonId());
    // ljj 注册时填写的学校默认为首要单位
    edu.setIsPrimary(1L);
    // 查找单位ID
    if (person.getInsId() == null) {
      // 修改获取单位结果记录的代码逻辑(修改为根据单位性质和单位名称查询单位记录)_MJG_SCM-1764.
      Long insId = null;
      // institutionManager.getInsIdByName(person.getColleageName(),person.getColleageName());
      Long natureType = 1L;// 单位性质为 1-college .
      List<Institution> insList =
          institutionManager.getInsListByName(person.getColleageName(), person.getColleageName(), natureType);
      if (CollectionUtils.isNotEmpty(insList)) {
        insId = insList.get(0).getId();
        edu.setInsId(insId);
      }
    } else {
      edu.setInsId(person.getInsId());
    }
    edu.setInsName(person.getColleageName());
    edu.setStudy(person.getStudy());
    edu.setDegree(person.getDegree());
    edu.setDegreeName(person.getDegreeName());
    if (StringUtils.isNotEmpty(person.getStudyFromYear())) {
      edu.setFromYear(Long.parseLong(person.getStudyFromYear()));
    }
    if (StringUtils.isNotEmpty(person.getStudyFromMonth())) {
      edu.setFromMonth(Long.parseLong(person.getStudyFromMonth()));
    }
    if (StringUtils.isNotEmpty(person.getStudyToYear())) {
      edu.setToYear(Long.valueOf(person.getStudyToYear()));
    }
    if (StringUtils.isNotEmpty(person.getStudyToMonth())) {
      edu.setToMonth(Long.valueOf(person.getStudyToMonth()));
    }

    educationHistoryRegisterDao.save(edu);
    // ljj
    person.setInsName(person.getColleageName());
  }

  // 处理工作经历中null字符串
  private void dealParameterNullString(PersonRegister person) {
    if (StringUtils.isNotBlank(person.getInsName()) && "null".equals(person.getInsName())) {
      person.setInsName("");
    }
    if (StringUtils.isNotBlank(person.getUnit()) && "null".equals(person.getUnit())) {
      person.setUnit("");
    }
    if (StringUtils.isNotBlank(person.getPosition()) && "null".equals(person.getPosition())) {
      person.setPosition("");
    }
  }

}
