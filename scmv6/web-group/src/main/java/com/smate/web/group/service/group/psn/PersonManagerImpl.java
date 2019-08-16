package com.smate.web.group.service.group.psn;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.group.dao.group.psn.PsnWorkHistoryInsInfoDao;
import com.smate.web.group.model.group.psn.PsnWorkHistoryInsInfo;

/**
 * @author zt.
 * 
 */
@Service("personManager")
@Transactional(rollbackFor = Exception.class)
public class PersonManagerImpl implements PersonManager {

  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PsnWorkHistoryInsInfoDao psnWorkHistoryInsInfoDao;

  /*
   * @Autowired private PsnWorkHistoryInsInfoDao psnWorkHistoryInsInfoDao;
   */
  /**
   * 获取用户信息(包含头衔的显示信息).
   */
  @Override
  public Person getPersonByRecommend(Long personId) throws Exception {
    Person psn = personProfileDao.getPersonByRecommend(personId);
    if (psn == null)
      return null;
    psn.setViewName(this.buildPsnShowName(psn));// 构建显示名称.
    psn.setViewTitolo(this.getPsnViewTitolo(psn));
    return psn;
  }

  /**
   * 获取人员头衔显示信息<显示人员工作单位并取消头衔>_MJG_SCM-5707.
   * 
   * @param psn
   * @return
   * @throws ServiceException
   */
  public String getPsnViewTitolo(Person psn) throws Exception {
    String viewTitolo = "";
    try {
      PsnWorkHistoryInsInfo psnIns = psnWorkHistoryInsInfoDao.getPsnWorkHistoryInsInfo(psn.getPersonId());
      if (psnIns != null) {
        Locale locale = LocaleContextHolder.getLocale();
        if (Locale.US.equals(locale)) {
          viewTitolo = (StringUtils.isBlank(psnIns.getInsNameEn())) ? psnIns.getInsNameZh() : psnIns.getInsNameEn();
        } else {
          viewTitolo = (StringUtils.isNotBlank(psnIns.getInsNameZh())) ? psnIns.getInsNameZh() : psnIns.getInsNameEn();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (StringUtils.isBlank(viewTitolo) && StringUtils.isNotBlank(psn.getDefaultAffiliation())) {
      viewTitolo = psn.getDefaultAffiliation();
    }
    return viewTitolo;
  }

  /**
   * 获取用户姓名，姓名可能为空，需要转换.
   * 
   * @param locale
   * @param person
   * @return
   */
  private String buildPsnShowName(Person person) {
    String zhName = person.getName();
    String firstName = person.getFirstName();
    String lastName = person.getLastName();
    return this.getPsnViewName(zhName, firstName, lastName);
  }

  /**
   * 获取人员头衔显示信息<显示人员工作单位并取消头衔>_MJG_SCM-5707.
   * 
   * @param psn
   * @return
   */
  /*
   * public String getPsnViewTitolo(Person psn) { String viewTitolo = ""; PsnWorkHistoryInsInfo psnIns
   * = psnWorkHistoryInsInfoDao.getPsnWorkHistoryInsInfo(psn.getPersonId()); if (psnIns != null) {
   * Locale locale = LocaleContextHolder.getLocale(); if (Locale.US.equals(locale)) { viewTitolo =
   * (StringUtils.isBlank(psnIns.getInsNameEn())) ? psnIns.getInsNameZh() : psnIns .getInsNameEn(); }
   * else { viewTitolo = (StringUtils.isNotBlank(psnIns.getInsNameZh())) ? psnIns.getInsNameZh() :
   * psnIns .getInsNameEn(); } } if (StringUtils.isBlank(viewTitolo) &&
   * StringUtils.isNotBlank(psn.getDefaultAffiliation())) { viewTitolo = psn.getDefaultAffiliation();
   * } return viewTitolo; }
   */

  /**
   * 构建人员显示名称_MJG_SCM-5707.
   * 
   * @param zhName
   * @param firstName
   * @param lastName
   * @return
   */
  public String getPsnViewName(String zhName, String firstName, String lastName) {
    String showName = null;
    Locale locale = LocaleContextHolder.getLocale();
    if (Locale.US.equals(locale)) {
      showName =
          (StringUtils.isBlank(firstName) && StringUtils.isBlank(lastName)) ? zhName : firstName + " " + lastName;
    } else {
      showName = (StringUtils.isNotBlank(zhName)) ? zhName : firstName + " " + lastName;
    }
    return showName;
  }

  @Override
  public String getPsnName(Long psnId, Locale locale) throws Exception {
    Person psn = personProfileDao.get(psnId);
    if (psn == null)
      return null;
    locale = locale == null ? LocaleContextHolder.getLocale() : locale;

    return this.getPsnName(psn, locale.toString());
  }

  @Override
  public String getPsnName(Person person, String locale) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getPsnName(Person psn) throws Exception {
    return this.getPsnName(psn, LocaleContextHolder.getLocale().toString());
  }

}
