package com.smate.web.psn.service.profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.psnname.ConstPsnLastNamePyDao;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.dao.profile.ConstSurNameDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.profile.ConstSurName;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 人员接口服务
 *
 * @author wsn
 * @createTime 2017年7月21日 上午11:20:50
 *
 */
@Service("psnHomepageService")
@Transactional(rollbackFor = Exception.class)
public class PsnHomepageServiceImpl implements PsnHomepageService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private ConstSurNameDao constSurNameDao;
  @Autowired
  private ConstPsnLastNamePyDao constPsnLastNamePyDao;
  @Autowired
  private UserDao userDao;

  @Override
  public PersonProfileForm getPsnNameInfo(PersonProfileForm form) throws PsnException {
    if (form.getPsnId() > 0) {
      Person psn = personProfileDao.findPsnAllName(form.getPsnId());
      if (psn != null) {
        form.setName(psn.getName());
        form.setFirstName(psn.getFirstName());
        form.setLastName(psn.getLastName());
        form.setZhFirstName(psn.getZhFirstName());
        form.setZhLastName(psn.getZhLastName());
        form.setOtherName(psn.getOtherName());
        // 中文的姓和名为空的话，用中文的name字段拆一下
        /*
         * if (StringUtils.isBlank(form.getZhLastName()) && StringUtils.isBlank(form.getZhFirstName()) &&
         * StringUtils.isNotBlank(form.getName())) { Map<String, String> splitName =
         * this.splitCName(form.getName()); if (splitName != null) {
         * form.setZhFirstName(splitName.get("zhFirstName"));
         * form.setZhLastName(splitName.get("zhLastName")); } }
         */
        if (StringUtils.isBlank(form.getName())) {
          form.setName(form.getLastName() + " " + form.getFirstName());
        }
      }
    }
    return form;
  }

  @Override
  public Map<String, String> splitCName(String cname) {
    try {
      List<ConstSurName> list = constSurNameDao.findAllSurName();// 查找所有复姓
      Map<String, String> map = new HashMap<String, String>();
      String lastName = "";
      try {
        if (StringUtils.isNotBlank(cname)) {
          char[] names = cname.trim().toCharArray();
          if (names.length >= 2) {// 姓名长度要大于2
            lastName = "" + names[0] + names[1];
            if (!isExistSurName(list, lastName)) {// 不存在复姓
              lastName = "" + names[0];
            }
            map.put("zhLastName", lastName);
            map.put("zhFirstName", cname.replace(lastName, ""));
          } else {
            map.put("zhLastName", "" + names[0]);
            map.put("zhFirstName", "");
          }

        }
      } catch (Exception e) {
        logger.warn("解析复姓的拼音失败:" + cname, e);
      }
      return map;
    } catch (Exception e) {
      return null;
    }
  }

  // 解析复姓
  public Map<String, String> parseSurName(char[] names, HanyuPinyinOutputFormat format) {

    Map<String, String> map = new HashMap<String, String>();
    String firstName = "";
    String lastName = "";
    for (int i = 0; i < names.length; i++) {
      String[] name;
      try {
        name = PinyinHelper.toHanyuPinyinStringArray(names[i], format);
      } catch (BadHanyuPinyinOutputFormatCombination e) {
        name = null;
        e.printStackTrace();
      }
      if (i == 0) {
        if (name != null && name.length > 0)
          lastName = name[0];
      } else if (i == 1) {
        if (name != null && name.length > 0)
          lastName += " " + name[0];
      } else {
        if (name != null && name.length > 0)
          firstName += " " + name[0];
      }
    }
    if (!"".equals(firstName))
      firstName = org.apache.commons.lang.StringUtils.substring(firstName, 1, 21);

    if (firstName != null && firstName.indexOf(" ") > -1) {
      String CollFirstName[] = firstName.split(" ");
      StringBuffer fnBuf = new StringBuffer();
      for (String fn : CollFirstName) {
        fnBuf.append(String.valueOf(fn.charAt(0)).toUpperCase()).append(fn.substring(1)).append(" ");
      }
      firstName = fnBuf.toString().trim();
    } else if (firstName != null && firstName.length() > 0) {
      firstName = String.valueOf(firstName.charAt(0)).toUpperCase() + firstName.substring(1);
    }
    if (lastName != null && lastName.indexOf(" ") > -1) {
      String CollLastName[] = lastName.split(" ");
      StringBuffer lnBuf = new StringBuffer();
      for (String ln : CollLastName) {
        lnBuf.append(String.valueOf(ln.charAt(0)).toUpperCase()).append(ln.substring(1)).append(" ");
      }
      lastName = lnBuf.toString().trim();
    } else if (lastName != null && lastName.length() > 0) {
      lastName = String.valueOf(lastName.charAt(0)).toUpperCase() + lastName.substring(1);
    }

    map.put("firstName", firstName);
    map.put("lastName", lastName);

    return map;
  }

  // 判断是否存在复姓
  public boolean isExistSurName(List<ConstSurName> surNameList, String lastName) {
    boolean flag = false;
    for (ConstSurName surName : surNameList) {
      if (lastName.equals(surName.getName().trim())) {
        flag = true;
        break;
      }
    }
    return flag;

  }

  @Override
  public String parseWordToPinYin(String word, String wordType, Integer length) throws PsnException {
    String result = "";
    if (length == null) {
      length = 21; // 原始默认长度
    }
    try {
      if (StringUtils.isNotBlank(word)) {
        if ("lastName".equals(wordType)) {// 姓氏要进行特殊处理，有些姓氏有多音字
          String str = constPsnLastNamePyDao.findFirstPinyinByZhWord(word);// 查询多音姓表
          if (StringUtils.isNotBlank(str)) {
            result = str;
          } else {
            result = ServiceUtil.parseWordPinyin(word, " ");
          }
        } else {
          result = ServiceUtil.parseWordPinyin(word, " ");
        }
        StringUtils.substring(result, 0, length);
      }
    } catch (Exception e) {
      logger.error("将字符转成拼音失败，word=" + word, e);
      throw new PsnException(e);
    }
    return result;
  }

  @Override
  public String registerParseWordToPinYin(String word, Integer length) throws PsnException {
    String result = "";
    if (length == null) {
      length = 21; // 原始默认长度
    }
    try {
      if (StringUtils.isNotBlank(word)) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        char[] words = word.trim().toCharArray();
        if (words.length > 0) {
          for (int i = 0; i < words.length; i++) {
            String[] pinyin = null;
            boolean isChinese = true;
            if (StringUtils.isNotBlank(words[i] + "") && IrisStringUtils.isChineseChar(words[i])) {
              pinyin = PinyinHelper.toHanyuPinyinStringArray(words[i], format);
            } else {
              pinyin = new String[] {words[i] + ""};
              isChinese = false;
            }
            if (pinyin == null) {
              logger.error("---------将字符转成拼音失败，word=" + word);
              break;
            }
            if (i == 0) {
              result += pinyin[0];
            } else {
              if (isChinese) {
                result += " " + pinyin[0];
              } else {
                result += pinyin[0];
              }
            }
          }
          if (!"".equals(result))
            result = org.apache.commons.lang.StringUtils.substring(result, 0, length);

          if (result != null && result.indexOf(" ") > -1) {
            String CollFirstName[] = result.split(" ");
            StringBuffer fnBuf = new StringBuffer();
            for (String fn : CollFirstName) {
              fn = fn.replace(" ", "");// 可能连续输入空格,导致split后有空串
              if (fn.length() <= 0) {
                continue;
              }
              fnBuf.append(String.valueOf(fn.charAt(0)).toUpperCase()).append(fn.substring(1)).append("-");
            }
            result = fnBuf.substring(0, fnBuf.length() - 1);
          } else if (result != null && result.length() > 0) {
            result = String.valueOf(result.charAt(0)).toUpperCase() + result.substring(1);
          }
        }
      }
    } catch (Exception e) {
      logger.error("将字符转成拼音失败，word=" + word, e);
      throw new PsnException(e);
    }
    return result;
  }

  @Override
  public String findPsnAccount(Long psnId) throws PsnException {
    return userDao.getLoginNameById(psnId);
  }
}
