package com.smate.web.management.service.analysis;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.web.management.dao.analysis.sns.PsnWorkHistoryInsInfoDao;
import com.smate.web.management.model.analysis.sns.PsnWorkHistoryInsInfo;

/**
 * @author zt.
 * 
 */
@Service("personManager")
@Transactional(rollbackFor = Exception.class)
public class PersonManagerImpl implements PersonManager {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private String rootFolder;
  @Autowired
  private FileService fileService;

  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private SysDomainConst sysDomainConst;
  @Autowired
  private PsnWorkHistoryInsInfoDao psnWorkHistoryInsInfoDao;
  @Autowired
  private PersonProfileDao personProfileDao;


  @Override
  public Person getPerson(Long psnId) throws Exception {
    Person psn = personProfileDao.get(psnId);
    if (psn != null) {
      psn.setViewName(this.buildPsnShowName(psn));// 构建显示名称.
      psn.setViewTitolo(this.getPsnViewTitolo(psn));// 构建显示头衔.
    }
    return psn;
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
   * 获取用户信息(包含头衔的显示信息).
   * 
   * @throws Exception
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
   * 构建人员显示名称_MJG_SCM-5707.
   * 
   * @param zhName
   * @param firstName
   * @param lastName
   * @return
   */
  @Override
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

  @Override
  public List<Long> getCooperatorPsnId(Long insId, List<Long> psnIdList) throws Exception {
    try {
      return this.personProfileDao.queryCooperatorPsnId(insId, psnIdList);
    } catch (Exception e) {
      logger.error("过滤用户id取得合作者id出现异常：", e);
      throw new Exception(e);
    }
  }

  @Override
  public String getPsnViewTitoloApplication(Person psn) {
    String viewTitolo = "";
    try {
      PsnWorkHistoryInsInfo psnIns = psnWorkHistoryInsInfoDao.getPsnWorkHistoryInsInfo(psn.getPersonId());
      if (psnIns != null) {
        Locale locale = LocaleContextHolder.getLocale();
        if (Locale.US.equals(locale)) {
          String insName = (StringUtils.isBlank(psnIns.getInsNameEn())) ? psnIns.getInsNameZh() : psnIns.getInsNameEn();
          String deptName =
              (StringUtils.isBlank(psnIns.getDepartmentEn())) ? psnIns.getDepartmentZh() : psnIns.getDepartmentEn();
          String title =
              (StringUtils.isBlank(psnIns.getPositionEn())) ? psnIns.getPositionZh() : psnIns.getPositionEn();
          insName = StringUtils.isEmpty(insName) ? "" : insName + "; ";
          deptName = StringUtils.isEmpty(deptName) ? "" : deptName + "; ";
          title = StringUtils.isEmpty(title) ? "" : title + "; ";
          viewTitolo = insName + deptName + title;

          if (StringUtils.isNotEmpty(viewTitolo)) {
            viewTitolo = viewTitolo.trim();
            viewTitolo = viewTitolo.substring(0, viewTitolo.length() - 1);// 去掉“；”
          }
        } else {
          String insName = (StringUtils.isBlank(psnIns.getInsNameZh())) ? psnIns.getInsNameEn() : psnIns.getInsNameZh();
          String deptName =
              (StringUtils.isBlank(psnIns.getDepartmentZh())) ? psnIns.getDepartmentEn() : psnIns.getDepartmentZh();
          String title =
              (StringUtils.isBlank(psnIns.getPositionZh())) ? psnIns.getPositionEn() : psnIns.getPositionZh();
          insName = StringUtils.isEmpty(insName) ? "" : insName + "; ";
          deptName = StringUtils.isEmpty(deptName) ? "" : deptName + "; ";
          title = StringUtils.isEmpty(title) ? "" : title + "; ";
          viewTitolo = insName + deptName + title;

          if (StringUtils.isNotEmpty(viewTitolo)) {
            viewTitolo = viewTitolo.trim();
            viewTitolo = viewTitolo.substring(0, viewTitolo.length() - 1);// 去掉“；”
          }
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
}
