package com.smate.web.psn.service.setting;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.dao.merge.AccountsMergeDao;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.MergeCountForm;
import com.smate.web.psn.model.merge.AccountsMerge;
import com.smate.web.psn.model.psninfo.SysMergeUserHis;
import com.smate.web.psn.model.psninfo.SysMergeUserInfo;
import com.smate.web.psn.service.merge.task.PersonMergeTaskService;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.user.UserService;

/**
 * 合并账户设置的业务逻辑实现类.
 * 
 * @author mjg
 * 
 */
@Service("mergeCountService")
@Transactional(rollbackFor = Exception.class)
public class MergeCountServiceImpl implements MergeCountService {

  private static Logger logger = LoggerFactory.getLogger(MergeCountServiceImpl.class);
  @Autowired
  private SysMergeUserHistoryService sysMergeUserHistoryService;

  @Autowired
  private PersonManager personManager;

  @Autowired
  private UserService userService;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private PersonMergeTaskService personMergeTaskService;
  @Value(value = "${initOpen.restful.url}")
  private String openRestfulUrl;
  @Autowired
  private AccountsMergeDao accountsMergeDao;

  /**
   * 获取当前登录帐号的信息.
   * 
   * @return
   * @throws ServiceException
   */
  @Override
  public MergeCountForm initMainCountInfo(MergeCountForm form) throws Exception {
    Long currPsnId = SecurityUtils.getCurrentUserId();
    // 获取个人基本信息，以便获取邮件语言版本
    Person psn = personManager.getPersonByRecommend(currPsnId);
    if (psn != null) {
      // 获取当前登录用户的用户名.
      String loginName = userService.findUserById(psn.getPersonId()).getLoginName();
      // 保存合并用户状态记录.
      SysMergeUserHis mergedUser = this.buildMergeUserHis(loginName, psn.getEmail(), currPsnId, MERGE_USER_STATUS_SAVED,
          SysMergeUserHistoryService.MERGE_STATUS_INIT, null);
      sysMergeUserHistoryService.saveMergeUserHis(mergedUser);
      String psnDes3Id = Des3Utils.encodeToDes3(psn.getPersonId().toString());
      form = this.buildForm(form, loginName, psn.getAvatars(), psnDes3Id, psn.getViewName(), psn.getFirstName(),
          psn.getLastName(), psn.getPersonId(), psn.getViewTitolo(), psn.getName());
    }
    return form;
  }

  /**
   * 构建form参数对象.
   * 
   * @param form
   * @param psn
   * @return
   */
  private MergeCountForm buildForm(MergeCountForm form, String loginName, String avatars, String des3Id,
      String viewName, String firstName, String lastName, Long psnId, String viewTitolo, String name) {
    if (form == null) {
      form = new MergeCountForm();
    }
    form.setLoginCount(loginName);
    form.setPsnAvatars(avatars);
    form.setPsnDes3Id(des3Id);
    form.setPsnViewName(viewName);
    form.setPsnFirstName(firstName);
    form.setPsnId(psnId);
    form.setPsnLastName(lastName);
    form.setPsnTitolo(viewTitolo);
    form.setPsnZhName(name);
    return form;
  }

  /**
   * 构建scholar库合并用户邮件通知记录表对象.
   * 
   * @param loginName
   * @param email
   * @param psnId
   * @param status
   * @param mergeStatus
   * @param delPsnId
   * @return
   */
  private SysMergeUserHis buildMergeUserHis(String loginName, String email, Long psnId, Integer status,
      Integer mergeStatus, Long delPsnId) {
    SysMergeUserHis mergeUserHis = new SysMergeUserHis();
    mergeUserHis.setDealTime(new Date());
    mergeUserHis.setLoginName(loginName);
    mergeUserHis.setEmail(email);
    mergeUserHis.setMailStatus(0);
    mergeUserHis.setMergeStatus(mergeStatus);
    mergeUserHis.setPsnId(psnId);
    mergeUserHis.setDelPsnId(delPsnId);
    mergeUserHis.setStatus(status);
    return mergeUserHis;
  }

  /**
   * 获取当前登录用户的正在合并用户记录列表.
   * 
   * @return
   */
  @Override
  public List<MergeCountForm> getMergingList() {
    List<MergeCountForm> formList = new ArrayList<MergeCountForm>();
    Long psnId = SecurityUtils.getCurrentUserId();
    List<SysMergeUserInfo> mergeUserList = sysMergeUserHistoryService.getMergingListByPsn(psnId, MERGE_USER_STATUS_DEL);
    if (CollectionUtils.isNotEmpty(mergeUserList)) {
      for (SysMergeUserInfo userInfo : mergeUserList) {
        MergeCountForm iform = new MergeCountForm();
        iform = this.buildForm(iform, userInfo.getLoginCount(), userInfo.getPsnAvatars(), userInfo.getDelDesPsnId(),
            userInfo.getPsnViewName(), userInfo.getPsnFirstName(), userInfo.getPsnLastName(), userInfo.getDelPsnId(),
            userInfo.getPsnViewTitolo(), userInfo.getPsnZhName());
        formList.add(iform);
      }
    }
    return formList;
  }

  /**
   * 验证用户名和密码是否正确，并返回用户ID.
   * 
   * @param userName
   * @param passWord
   * @return
   */
  @Override
  public Long checkLoginCount(String userName, String passWord) {
    try {
      User user = userService.findUserByLoginName(userName);
      // 如果用户不存在，则返回-1.
      if (user == null) {
        return CHECK_RESULT_LOGIN_ERROR;
      }
      // 如果密码不匹配，则返回-2.
      if (!user.getPassword().equals(passwordEncoder.encodePassword(passWord, null))) {
        return CHECK_RESULT_PWD_ERROR;
      }
      return user.getId();
    } catch (Exception e) {
      logger.info("验证用户名和密码是否正确，并返回用户ID", e);
      return CHECK_RESULT_PWD_ERROR;
    }
  }

  /**
   * 进行帐号合并操作，并返回被合并帐号的人员信息.
   * 
   * @param targetPsnId
   * @return
   * @throws ServiceException
   */
  @Override
  public String mergePsnCount(Long targetPsnId) throws Exception {
    Long currPsnId = SecurityUtils.getCurrentUserId();
    Person savePsn = personManager.getPersonByRecommend(currPsnId);
    // 获取被合并用户的用户名.
    String loginName = userService.findUserName(targetPsnId);
    Person delPsn = personManager.getPersonByRecommend(targetPsnId);
    this.saveMergeUserOperat(loginName, currPsnId, delPsn, MERGE_USER_STATUS_DEL);
    // 直接保存bpo合并记录
    String mergeResult = "error";
    try {
      // 不保存bpo合并记录
      // personMergeTaskService.saveMergePsn(currPsnId, targetPsnId);
      // 在sns库保存合并账号记录
      AccountsMerge accountsMerge = new AccountsMerge();
      accountsMerge.setCreateDate(new Date());
      accountsMerge.setSavePsnId(currPsnId);
      accountsMerge.setDelPsnId(targetPsnId);
      accountsMerge.setStatus(0L);
      accountsMerge.setStatusExt(1L);
      accountsMergeDao.save(accountsMerge);
      mergeResult = "succeed";
    } catch (Exception e) {
      mergeResult = "error";
    }
    // 如果接口响应成功，则获取被合并用户的邮件地址.
    if (StringUtils.equalsIgnoreCase("succeed", mergeResult)) {
      this.deleteSolrPsnInfo(targetPsnId);
      return savePsn.getEmail();
    } else if (StringUtils.equalsIgnoreCase("error", mergeResult)) {
      throw new ServiceException();
    }
    return null;
  }

  /**
   * 保存被合并用户记录.
   * 
   * @param loginName
   * @param savePsnId
   * @param delPerson
   * @param status
   */
  private void saveMergeUserOperat(String loginName, Long savePsnId, Person delPerson, Integer status) {
    // 保存被合并用户状态记录.
    SysMergeUserHis mergedUser = this.buildMergeUserHis(loginName, delPerson.getEmail(), savePsnId, status,
        SysMergeUserHistoryService.MERGE_STATUS_MERGING, delPerson.getPersonId());
    sysMergeUserHistoryService.saveMergeUserHis(mergedUser);
    // 保存被合并删除的人员信息记录.
    SysMergeUserInfo mergeUserInfo = this.buildMergeUserInfo(loginName, savePsnId, delPerson);
    sysMergeUserHistoryService.saveMergeUserInfo(mergeUserInfo);
  }

  /**
   * 构建scholar库被合并删除的人员信息表对象.
   * 
   * @param loginName
   * @param savePsnId
   * @param person
   * @return
   */
  private SysMergeUserInfo buildMergeUserInfo(String loginName, Long savePsnId, Person person) {
    SysMergeUserInfo mergeUserInfo = new SysMergeUserInfo();
    mergeUserInfo.setPsnId(savePsnId);
    mergeUserInfo.setDelPsnId(person.getPersonId());
    mergeUserInfo.setDelDesPsnId(person.getPersonDes3Id());
    mergeUserInfo.setInsName(person.getInsName());
    mergeUserInfo.setLoginCount(loginName);
    mergeUserInfo.setPsnAvatars(person.getAvatars());
    mergeUserInfo.setPsnEmail(person.getEmail());
    mergeUserInfo.setPsnEnName(person.getEname());
    mergeUserInfo.setPsnFirstName(person.getFirstName());
    mergeUserInfo.setPsnLastName(person.getLastName());
    mergeUserInfo.setPsnTitolo(person.getTitolo());
    mergeUserInfo.setPsnZhName(person.getName());
    mergeUserInfo.setPsnViewName(person.getViewName());
    mergeUserInfo.setPsnViewTitolo(person.getViewTitolo());
    return mergeUserInfo;
  }

  @SuppressWarnings("unchecked")
  private void deleteSolrPsnInfo(Long psnId) {
    if (psnId == null) {
      logger.info("solr删除人员信息psnId为空！");
      return;
    }
    // openServerUrl = "http://127.0.0.1:49080/center-open/scmopendata";
    RestTemplate restTemplate = new RestTemplate();
    Map restfulMap = new HashMap<String, Object>();
    Map dataMap = new HashMap<String, Object>();
    restfulMap.put("openid", "99999999");
    restfulMap.put("token", "00000000psndsolr");
    dataMap.put("psn_id", psnId);
    restfulMap.put("data", JacksonUtils.mapToJsonStr(dataMap));
    Object obj = restTemplate.postForObject(openRestfulUrl, restfulMap, Object.class);

    if (obj != null) {
      Map<String, Object> map = JacksonUtils.jsonToMap(obj.toString());
      // 返回结果为ArrayList,详见open返回值说明
      List<Object> status = (List<Object>) map.get("result");
      try {
        Integer result = Integer.parseInt(String.valueOf(((Map<String, Object>) status.get(0)).get("return_status")));
        if (result == 1) {
          logger.info("solr删除人员索引信息成功，psnId = " + psnId);
        } else {
          logger.info("solr删除人员索引信息失败，psnId = " + psnId);
        }
      } catch (Exception e) {
        logger.error("solr删除人员索引信息失败，psnId = " + psnId, e);
      }
    }
  }
}
