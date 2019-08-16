package com.smate.web.management.service.psn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.service.security.SysUserLoginService;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.management.dao.psn.MailLogDao;
import com.smate.web.management.model.psn.MailLog;
import com.smate.web.management.model.psn.PsnInfo;
import com.smate.web.management.model.psn.PsnInfoForm;

/**
 * 人员信息显示服务实现
 * 
 * @author zll
 * 
 */
@Service("psnInfoService")
@Transactional(rollbackFor = Exception.class)
public class PsnInfoServiceImpl implements PsnInfoService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  // 帐号合并结果-帐号不存在.
  public static final Long CHECK_RESULT_MERGE_COUNT_ERROR = -1L;
  // 帐号合并结果-密码错误.
  public static final Long CHECK_RESULT_DELETE_COUNT_ERROR = -2L;
  @Autowired
  private PersonService personService;
  @Autowired
  private SysUserLoginService sysUserLoginService;
  @Autowired
  private UserService userService;
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private MergeCountService mergeCountService;
  @Autowired
  private MailLogDao mailLogDao;

  @Override
  public void getPsnInfo(PsnInfoForm form) {
    String NEW_PSN_VIEW_URL = "/psnweb/homepage/show?des3PsnId=";
    // 基本信息
    List<Person> psnList = personService.getAllPsn(form);
    List<PsnInfo> psnInfoList = new ArrayList<PsnInfo>();
    SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    for (Person person : psnList) {
      if (person != null) {
        PsnInfo psnInfo = new PsnInfo();
        form.setPsnId(person.getPersonId());
        psnInfo.setDes3PsnId(ServiceUtil.encodeToDes3(person.getPersonId().toString()));
        psnInfo.setAvatarsUrl(person.getAvatars());
        psnInfo.setPsnName(person.getName());
        psnInfo.setLastName(person.getLastName());
        psnInfo.setFirstName(person.getFirstName());
        psnInfo.setRegData(fdate.format(person.getRegData()));
        psnInfo.setEmail(person.getEmail());
        psnInfo = this.buildPsnTitoloInfo(psnInfo, person);
        psnInfo = this.buildPsnStatisticsInfo(psnInfo, person.getPersonId());
        // 登录账号
        psnInfo.setPsnId(person.getPersonId());
        Date lastLoginTime = sysUserLoginService.getLastLoginTimeById(person.getPersonId());
        if (lastLoginTime != null) {
          psnInfo.setLastLoginTime(fdate.format(lastLoginTime));
        }
        psnInfo.setLoginName(userService.getLoginNameById(person.getPersonId()));
        psnInfo.setPsnUrl(NEW_PSN_VIEW_URL + psnInfo.getDes3PsnId());
        Long emailCount = mailLogDao.getPsnEmailList(psnInfo.getEmail(), person.getPersonId(), 30);// 查询前一个月的邮件
        psnInfo.setIsCheckEmail(emailCount > 0 ? 1 : 0);// 查询;
        psnInfoList.add(psnInfo);
      }
    }
    form.setPsnInfoList(psnInfoList);
    form.getPage().setResult(psnInfoList);
  }

  private PsnInfo buildPsnStatisticsInfo(PsnInfo psnInfo, Long personId) {
    PsnStatistics psnStatistics = psnStatisticsService.getPsnStatistics(personId);
    if (psnStatistics != null) {
      psnInfo.setPatentSum(psnStatistics.getPatentSum());
      psnInfo.setPrjSum(psnStatistics.getPrjSum());
      psnInfo.setPubSum(psnStatistics.getPubSum());
    }
    return psnInfo;
  }

  private PsnInfo buildPsnTitoloInfo(PsnInfo psnInfo, Person person) {
    String insName = person.getInsName();
    String department = person.getDepartment();
    String position = person.getPosition();
    String titolo = person.getTitolo();
    // 构建单位+部门信息
    if (StringUtils.isBlank(insName) || StringUtils.isBlank(department)) {
      psnInfo.setInsAndDep(
          (StringUtils.isBlank(insName) ? "" : insName) + (StringUtils.isBlank(department) ? "" : department));
    } else {
      psnInfo.setInsAndDep(insName + ", " + department);
    }
    // 构建职称+头衔信息
    if (StringUtils.isBlank(position) || StringUtils.isBlank(titolo)) {
      psnInfo.setPosAndTitolo(
          (StringUtils.isBlank(position) ? "" : position) + (StringUtils.isBlank(titolo) ? "" : titolo));
    } else {
      psnInfo.setPosAndTitolo(position + ", " + titolo);
    }
    return psnInfo;
  }

  @Override
  public void getPsnEmailListInfo(PsnInfoForm form) {
    Integer typeValue = 30;
    if (form.getTypeId() != null && form.getTypeId() == 2) {
      typeValue = 60;
    } else if (form.getTypeId() != null && form.getTypeId() == 3) {
      typeValue = 90;
    }
    List<MailLog> mailInfoList = mailLogDao.getPsnEmailListInfo(form.getPsnEmail(), form.getPsnId(), typeValue);
    for (MailLog mailLog : mailInfoList) {
      String paramValue = mailLog.getId() + "|" + ServiceConstants.SCHOLAR_NODE_ID_1 + "|" + new Date().getTime();
      String mailViewParams = ServiceUtil.encodeToDes3(paramValue);
      mailLog.setDes3Id(mailViewParams);
    }
    form.setMailInfoList(mailInfoList);
  }

  @Override
  public String dealMergeCount(PsnInfoForm form) {
    String resultJson = "";
    String msgInfo = "";
    try {
      User mergeUser = userService.getpsnIdByEmail(form.getMergeCount());
      if (mergeUser == null) {
        // 保留帐号不存在.
        msgInfo = "保留账号不存在";
        return resultJson = "{\"result\":\"errMergeCount\",\"msg\":\"" + msgInfo + "\"}";
      } else {
        List<Long> UserIds = new ArrayList<Long>();
        String[] deleteCounts = form.getDeleteCount().split(";");
        for (String deleteCount : deleteCounts) {
          User deleteUser = userService.getpsnIdByEmail(deleteCount);
          if (deleteUser == null) {// 删除 帐号不存在.
            msgInfo = "删除账号" + deleteCount + "不存在";
            return resultJson = "{\"result\":\"errMergeCount\",\"msg\":\"" + msgInfo + "\"}";
          } else {
            // 判断是否重复添加 tsz
            Integer status = mergeCountService.getMergeStatus(mergeUser.getId(), deleteUser.getId());
            // 如果是 判断是否失败。
            if (status != null) {
              if (status == 2) {
                // 重复添加 合并失败账号
                msgInfo = deleteCount + "账户已经合并失败，请联系客服。";
              } else {
                // 重复添加正在合并账号
                msgInfo = deleteCount + "该账号正在被合并中，请耐心等待。";
              }
              return resultJson = "{\"result\":\"success\",\"msg\":\"" + msgInfo + "\"}";
            }
          }
          UserIds.add(deleteUser.getId());
        }
        try {
          // 对目标用户的帐号进行合并，并返回其首要邮件.
          String targetEmail = mergeCountService.mergePsnCount(mergeUser.getId(), UserIds, form.getMergeCount());
          msgInfo = "合并请求已成功提交，合并成功后将发通知邮件至“" + targetEmail + "（首要邮件）”中，请查收";

        } catch (Exception e) {
          msgInfo = "操作失败";
          resultJson = "{\"result\":\"success\",\"msg\":\"" + msgInfo + "\"}";
        }
        return resultJson = "{\"result\":\"success\",\"msg\":\"" + msgInfo + "\"}";
      }
    } catch (Exception e) {
      logger.info("验证用户名和密码是否正确，并返回用户ID", e);
      return resultJson = "{\"result\":\"errMergeCount\",\"msg\":\"保留或删除帐号有问题\"}";
    }
  }

}
