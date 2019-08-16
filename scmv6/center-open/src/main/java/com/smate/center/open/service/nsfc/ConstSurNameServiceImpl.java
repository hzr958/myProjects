package com.smate.center.open.service.nsfc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.nsfc.ConstSurNameDao;
import com.smate.center.open.model.nsfc.ConstSurName;
import com.smate.core.base.utils.string.ServiceUtil;


/**
 * 
 * @zjh
 *
 */

@Service("constSurNameService")
@Transactional(rollbackFor = Exception.class)
public class ConstSurNameServiceImpl implements ConstSurNameService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ConstSurNameDao constSurNameDao;

  @Override
  public Map<String, String> parsePinYin(String cname) {
    try {

      List<ConstSurName> list = findAllSurName();// 查找所有复姓

      Map<String, String> map = new HashMap<String, String>();
      String lastName = "";
      try {
        if (StringUtils.isNotBlank(cname)) {
          HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
          format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
          char[] names = cname.trim().toCharArray();

          if (names.length > 2) {// 姓名长度要大于2
            lastName = "" + names[0] + names[1];
            if (isExistSurName(list, lastName)) {// 存在复姓
              map = parseSurName(names, format);
            } else {
              map = ServiceUtil.parsePinYin(cname);
            }

          } else {
            map = ServiceUtil.parsePinYin(cname);
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

  @Override
  public List<ConstSurName> findAllSurName() throws Exception {
    return constSurNameDao.findAllSurName();
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


}
