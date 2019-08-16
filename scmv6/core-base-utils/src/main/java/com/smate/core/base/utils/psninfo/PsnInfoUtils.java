package com.smate.core.base.utils.psninfo;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.utils.model.security.Person;

/**
 * 构建人员信息公共工具类
 * 
 * @author lhd
 *
 */
public class PsnInfoUtils {

  /**
   * 构建人员名称_统一取name或ename,为空取另一个
   * 
   * @param psn
   * @param locale
   * @return
   */
  public static String buildPsnName(Person psn, Locale locale) {
    String psnName = "";
    if (psn != null) {
      if (locale == null) {
        locale = LocaleContextHolder.getLocale();
      }
      String name = psn.getName();
      String eName = psn.getEname();
      if (Locale.US.equals(locale)) {
        if (StringUtils.isBlank(eName)) {
          psnName = psn.getName();
        } else {
          psnName = psn.getEname();
        }
      } else {
        if (StringUtils.isBlank(name)) {
          psnName = psn.getEname();
        } else {
          psnName = psn.getName();
        }
      }
    }
    return psnName;
  }

  /**
   * 构建人员信息:机构+部门+职称
   * 
   * @param psn
   * @param locale
   * @return
   */
  public static String buildPsnTitoloInfo(Person psn, Locale locale) {
    String psnTitle = "";
    if (psn != null) {
      if (locale == null) {
        locale = LocaleContextHolder.getLocale();
      }
      String insName = psn.getInsName();
      String department = psn.getDepartment();
      String position = psn.getPosition();
      // 机构+部门
      if (StringUtils.isBlank(insName) || StringUtils.isBlank(department)) {
        psnTitle = (StringUtils.isBlank(insName) ? "" : insName) + (StringUtils.isBlank(department) ? "" : department);
      } else {
        psnTitle = insName + ", " + department;
      }
      // 职称
      if (StringUtils.isNotBlank(position)) {
        if (StringUtils.isNotBlank(psnTitle)) {
          psnTitle += ", " + position;
        } else {
          psnTitle = position;
        }
      }
    }
    return psnTitle;
  }

  /**
   * 
   * @param person
   * @param language zh=中文
   * @return
   */
  public static String getPersonName(Person person, String language) {
    if ("en".equalsIgnoreCase(language) || "en_US".equalsIgnoreCase(language)) {
      if (StringUtils.isNotBlank(person.getEname())) {
        return person.getEname();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getName();
      }
    } else {
      if (StringUtils.isNotBlank(person.getName())) {
        return person.getName();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getEname();
      }

    }
  }

}
