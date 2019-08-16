package com.smate.center.open.service.interconnection;

import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.dao.profile.psnwork.PsnWorkHistoryInsInfoDao;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.interconnection.AccountInterconnectionForm;
import com.smate.center.open.model.interconnection.PsnInfo;
import com.smate.center.open.model.profile.psnwork.PsnWorkHistoryInsInfo;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 人员信息服务类实现
 * 
 * @author zll
 */
@Service("openPersonManager")
@Transactional(rollbackFor = Exception.class)
public class OpenPersonManagerImpl implements OpenPersonManager {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PsnWorkHistoryInsInfoDao psnWorkHistoryInsInfoDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private UserDao userDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public void findMatchPerson(AccountInterconnectionForm form) {
    try {
      String email = form.getEmail();
      String userName = form.getUserName();
      String insName = form.getInsName();
      String firstName = form.getFirstName();
      String lastName = form.getLastName();
      //2019-04-04 优先openId 查找人员信息
     if(findPersonByOpenId(form) != null){
       return;
     }
      // 通过email匹配登录邮箱找到人员ID
      // Long psnId = userDao.findIdByLoginName(email);
      // 通过email查找person对象。
      List<Person> matchPersonList = new ArrayList<>();
      List<Person> personList = personProfileDao.findPersonByEmail(email);
      if (personList == null || personList.size() == 0) {
        return;
      }

      for (Person person : personList) {
        // SCM-10735 新修改的逻辑，要匹配人员单位
        if (person != null) {
          // 匹配人员单位名称
          boolean insNameMatchable =
              person.getInsName() != null ? StringUtils.equalsIgnoreCase(person.getInsName(), insName) : false;
          if (!insNameMatchable)
            continue;
          // 匹配人员姓名
          boolean psnNameMatchable = false;
          if (person.getName() != null) {
            psnNameMatchable = StringUtils.equalsIgnoreCase(person.getName(), userName);
          } else if (person.getFirstName() != null && person.getLastName() != null) {
            psnNameMatchable = StringUtils.equalsIgnoreCase(person.getFirstName(), firstName)
                && StringUtils.equalsIgnoreCase(person.getLastName(), lastName);
          }
          if (psnNameMatchable && insNameMatchable) {
            matchPersonList.add(person);
          }
        }
      }

      if (matchPersonList == null || matchPersonList.size() == 0) {
        return;
      }

      // 页面只显示 两个人员
      int count = 0;
      for (Person person : matchPersonList) {
        if (count == 2) {
          break;
        }
        // 先检查该psnId对应的人员是否已关联过对应的第三方系统的人员
        /*if (StringUtils.isNotBlank(form.getFromSys())) {
          Long openId = openUserUnionDao.getOpenIdByPsnId(person.getPersonId());
          if (openId != null) {
            OpenUserUnion openUserUnion = openUserUnionDao.getOpenUserUnion(openId, form.getFromSys());
            if (openUserUnion != null) {
              continue;
            }
          }
        }*/
        count++;
        buildPsnInfo(form, email, person);
      }
      // 设置第一个人员
      if (form.getPsnInfoList().size() > 0) {
        form.setPsnInfo(form.getPsnInfoList().get(0));
      }
    } catch (Exception e) {
      logger.error("互联互通---帐号关联----匹配科研之友人员出错", e);
      throw new OpenException(e);
    }
  }

  private  OpenUserUnion findPersonByOpenId(AccountInterconnectionForm form){
    if( StringUtils.isNotBlank(form.getDemoOpenId()) && NumberUtils.isDigits(form.getDemoOpenId())){
      Long openId = NumberUtils.toLong(form.getDemoOpenId());
      OpenUserUnion openUserUnion = openUserUnionDao.getOpenUserUnionByOpenId(openId);
      if(openUserUnion !=null){
        Person person = personProfileDao.get(openUserUnion.getPsnId());
        if(person != null){
          buildPsnInfo(form, person.getEmail(), person);
          form.setPsnInfo(form.getPsnInfoList().get(0));
          return  openUserUnion;
        }
      }

    }
    return null ;
  }

  private void buildPsnInfo(AccountInterconnectionForm form, String email, Person psn) {
    form.setInsName(psn.getInsName());
    form.setDepartment(psn.getDepartment());
    form.setPosition(psn.getPosition());
    // 查询人员成果专利统计信息 PSN_STATISTICS表
    PsnStatistics psnStatistics = psnStatisticsDao.get(psn.getPersonId());
    PsnWorkHistoryInsInfo psnWorkHistoryInsInfo = psnWorkHistoryInsInfoDao.getPsnWorkHistoryInsInfo(psn.getPersonId());

    PsnInfo psnInfo = new PsnInfo();
    psnInfo.setPsnId(psn.getPersonId());
    psnInfo.setDes3PsnId(ServiceUtil.encodeToDes3(psn.getPersonId() + ""));
    psnInfo.setFirstName(psn.getFirstName());
    psnInfo.setLastName(psn.getLastName());
    if (StringUtils.isNotBlank(psn.getName())) {
      psnInfo.setPsnName(psn.getName());
    } else {
      psnInfo.setPsnName(psn.getFirstName() + " " + psn.getLastName());
    }

    psnInfo.setAvatorUrl(psn.getAvatars());
    psnInfo.setInsId(psn.getInsId());
    psnInfo.setInsName(psn.getInsName());
    psnInfo.setEmail(email);
    if (psnWorkHistoryInsInfo != null) {
      psnInfo.setPosition_en(psnWorkHistoryInsInfo.getPositionEn());
      psnInfo.setPosition_zh(psnWorkHistoryInsInfo.getPositionZh());
      psnInfo.setDepartment_zh(psnWorkHistoryInsInfo.getDepartmentZh());
      psnInfo.setDepartment_en(psnWorkHistoryInsInfo.getDepartmentEn());
    }
    if (psnStatistics != null) {
      psnInfo.setPatentSum(psnStatistics.getPatentSum());
      psnInfo.setPubSum(psnStatistics.getPubSum());
    }
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.find(psn.getPersonId());
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      psnInfo.setPsnShortUrl(domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl());
    }
    form.getPsnInfoList().add(psnInfo);
  }

}
