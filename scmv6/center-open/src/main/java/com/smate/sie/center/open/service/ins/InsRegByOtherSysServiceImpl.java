package com.smate.sie.center.open.service.ins;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.common.EditValidateUtils;
import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.model.consts.SieConstRegion;
import com.smate.core.base.utils.xss.XssUtils;
import com.smate.sie.core.base.utils.dao.ins.Sie6InsRegDao;
import com.smate.sie.core.base.utils.dao.ins.SieInsRegExtendDao;
import com.smate.sie.core.base.utils.dao.insguid.SieInsGuidDao;
import com.smate.sie.core.base.utils.model.ins.Sie6InsReg;
import com.smate.sie.core.base.utils.model.ins.SieInsRegExtend;
import com.smate.sie.core.base.utils.model.insguid.SieInsGuid;
import com.smate.sie.core.base.utils.service.constant.SieConstCnRegionService;

/**
 * 单位注册接口 （提供给创新城）
 * 
 * @author hd
 *
 */
@Transactional(rollbackFor = Exception.class)
public class InsRegByOtherSysServiceImpl extends ThirdDataTypeBase {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 文件根路径
   */
  @Value("${file.root}")
  private String rootPath;
  @Autowired
  private Sie6InsRegDao sieInsRegDao;
  @Autowired
  private SieInsRegExtendDao sieInsRegExtendDao;
  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;
  @Autowired
  private SieConstCnRegionService sieConstCnRegionService;

  @Autowired
  private SieInsGuidDao sieInsGuidDao;

  @SuppressWarnings("null")
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    // 正则匹配 手机号和电话号码, 跟表单匹配一致
    String REGEX_MOBILE = "^[1][3,4,5,6,7,8,9][0-9]{9}$";
    String REGEX_TEL = "^([0-9]{3,4}-)?[0-9]{7,8}$";

    // 没有 参数校验 直接返回成功
    Map<String, Object> temp = new HashMap<String, Object>();

    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }

    // ins_name 必填
    Object insName = serviceData.get("ins_name");
    if (insName == null || StringUtils.isBlank(insName.toString())) {
      logger.error("单位基本数据 ins_name不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_9011, paramet, "单位基本数据 ins_name不能为空");
      return temp;
    }
    paramet.put("insName", insName);

    if (insName.toString().length() > 20) {
      logger.error("单位基本数据 ins_name长度不能超过20");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_9016, paramet, "单位基本数据 ins_name长度不能超过20");
      return temp;
    }

    Object url = serviceData.get("url");
    // url单位域名
    if (url != null || StringUtils.isNotBlank(url.toString())) {
      String contacturl = url.toString().trim();
      if (contacturl.length() > 20) {
        logger.error("单位域名 url长度不能超过20");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_9017, paramet, "单位基本数据 url长度不能超过20");
        return temp;
      }
      Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
      Matcher m = p.matcher(contacturl);
      if (m.find()) {
        logger.error("单位域名 url不能包含中文或特殊字符");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_9018, paramet, "单位基本数据 url不能包含中文或特殊字符");
        return temp;
      }
    }
    paramet.put("url", url);

    // contact_person 必
    Object psnName = serviceData.get("contact_person");
    if (psnName == null || StringUtils.isBlank(psnName.toString())) {
      logger.error("单位基本数据 contact_person不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_9012, paramet, "单位基本数据 contact_person不能为空");
      return temp;
    }
    paramet.put("psnName", psnName);

    // contact_email 必填，合法性
    Object psnEmail = serviceData.get("contact_email");
    if (psnEmail == null || !psnEmail.toString().toLowerCase().trim().matches(EditValidateUtils.MAIL_COAD)) {
      logger.error("获取通过contact_email不能为空，格式必须正确，contact_email=" + serviceData.get("contact_email"));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_9013, paramet, "单位基本数据 contact_email不能为空，格式必须正确");
      return temp;
    }
    paramet.put("psnEmail", psnEmail);

    // pwd 必填
    Object pwd = serviceData.get("pwd");
    if (pwd == null || StringUtils.isBlank(pwd.toString())) {
      logger.error("单位基本数据 pwd不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_9014, paramet, "单位基本数据 pwd不能为空");
      return temp;
    }
    paramet.put("pwd", pwd);

    // 非必填，有则校验合法性
    Object psnTel = serviceData.get("contact_tel");
    if (psnTel != null && StringUtils.isNotBlank(psnTel.toString())) {
      String contactTel = psnTel.toString().trim();
      if (Pattern.matches(REGEX_TEL, contactTel)) {
        psnTel = contactTel;
      } else {
        psnTel = null;
      }
    }
    paramet.put("psnTel", psnTel);

    Object psnMobile = serviceData.get("contact_mobile");
    if (psnMobile != null && StringUtils.isNotBlank(psnMobile.toString())) {
      String contactMobile = psnMobile.toString().trim();
      if (Pattern.matches(REGEX_MOBILE, contactMobile)) {
        psnMobile = contactMobile;
      } else {
        psnMobile = null;
      }
    }
    paramet.put("psnMobile", psnMobile);

    paramet.put("insAddress", serviceData.get("ins_address"));
    paramet.put("postCode", serviceData.get("ins_post"));
    paramet.put("fox", serviceData.get("ins_fox"));
    paramet.put("regionName", serviceData.get("ins_region"));
    paramet.put("checkEmails", serviceData.get("check_emails"));
    paramet.put("uniformId1", serviceData.get("uniform_id1"));
    paramet.put("uniformId2", serviceData.get("uniform_id2"));
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      Map<String, Object> temp = new HashMap<String, Object>();
      Map<String, Object> infoMap = new HashMap<String, Object>();
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
      String SCM_MSG = OpenMsgCodeConsts.SCM_000;

      // 获取单位名称
      String insName = XssUtils.filterByXssStr(paramet.get("insName").toString());
      insName = StringUtils.trim(insName);
      Sie6Institution institution = sie6InstitutionDao.findInstitutionByInsName(insName);

      // 如果institution中不存在，则查ins_reg表
      Boolean existInsReg = false;// 不存在

      if (institution == null) {
        List<Sie6InsReg> insRegs = sieInsRegDao.findInsRegByInsName(insName);
        if (insRegs != null && !insRegs.isEmpty()) {
          existInsReg = true;// 存在待审核
        }
      }


      if (institution != null) {
        // 存在institution中
        Long insId = institution.getId();
        String orgGuid = null;
        SieInsGuid insGuid = sieInsGuidDao.get(insId);
        if (insGuid != null) {
          orgGuid = insGuid.getGuid();
        }
        insName = institution.getInsName();

        // {ins_id:xxxx,org_guid:xxxx,org_name:xxxxx}
        infoMap.put("ins_id", insId);
        infoMap.put("org_guid", orgGuid);
        infoMap.put("org_name", insName);

        SCM_MSG = OpenMsgCodeConsts.SCM_9020;
      } else if (existInsReg) {
        // 存在ins_reg 中 SCM_9021
        SCM_MSG = OpenMsgCodeConsts.SCM_9021;

      } else {
        // 不存在
        Sie6InsReg insReg = new Sie6InsReg();
        insReg.setInsName(insName);
        insReg.setInsAddress(paramet.get("insAddress").toString());
        insReg.setContactEmail(paramet.get("psnEmail").toString().toLowerCase().trim());
        insReg.setContactPsName(paramet.get("psnName").toString());

        // 手机号和电话号会做校验，有可能null 需判断
        Object psnTelObj = paramet.get("psnTel");
        String psnTel = null;
        if (psnTelObj != null) {
          psnTel = psnTelObj.toString().trim();
        }
        insReg.setContactTel(psnTel);


        Object psnMobileObj = paramet.get("psnMobile");
        String psnMobile = null;
        if (psnMobileObj != null) {
          psnMobile = psnMobileObj.toString().trim();
        }
        insReg.setMobile(psnMobile);


        Object urlObj = paramet.get("url");
        String url = null;
        if (urlObj != null) {
          url = urlObj.toString().toLowerCase().trim();
          if (url.startsWith("http://")) {
            url = url.replaceFirst("http://", "");

          } else if (url.startsWith("https://")) {
            url = url.replaceFirst("https://", "");
          }
        }
        insReg.setInsUrl(url);
        insReg.setStatus("R");
        insReg.setRegDate(new Date());
        insReg.setPassword(DigestUtils.md5Hex(paramet.get("pwd").toString()));

        Object city = paramet.get("regionName");
        if (city != null && !StringUtils.isBlank(city.toString())) {
          SieConstRegion c = sieConstCnRegionService.getCityByName(city.toString());
          if (c != null) {
            insReg.setRegionId(c.getId());
          }
        }
        insReg.setRegWay(3);// ROL-5904 ztg ROL-6218 xr
        sieInsRegDao.save(insReg);

        // 拓展表
        SieInsRegExtend regExd = new SieInsRegExtend();
        regExd.setInsregId(insReg.getInsregId());
        regExd.setCheckEmails(paramet.get("checkEmails") == null ? null : paramet.get("checkEmails").toString());
        regExd.setPostcode(paramet.get("postCode") == null ? null : paramet.get("postCode").toString());
        regExd.setFox(paramet.get("fox") == null ? null : paramet.get("fox").toString());
        regExd.setRegionName(paramet.get("regionName") == null ? null : paramet.get("regionName").toString());
        regExd.setUniformId1(paramet.get("uniformId1") == null ? null : paramet.get("uniformId1").toString());
        regExd.setUniformId2(paramet.get("uniformId2") == null ? null : paramet.get("uniformId2").toString());
        sieInsRegExtendDao.save(regExd);

        // 响应成功
        SCM_MSG = OpenMsgCodeConsts.SCM_000;
      }

      dataList.add(infoMap);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, SCM_MSG);// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;
    } catch (Exception e) {
      logger.error("单位注册接口 异常  map=" + paramet.toString());
      logger.error("单位注册接口 异常 " + e.getMessage());
      throw new OpenException(e);
    }
  }

  public static void main(String args[]) {
    String REGEX_MOBILE = "^[1][3,4,5,6,7,8,9][0-9]{9}$";
    Boolean result = Pattern.matches(REGEX_MOBILE, "13212161518");


    String REGEX_TEL = "^([0-9]{3,4}-)?[0-9]{7,8}$";
    Boolean result1 = Pattern.matches(REGEX_TEL, "066-8374082");
    System.out.println(result);
    System.out.println(result1);
  }
}
