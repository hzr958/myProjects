package com.smate.sie.core.base.utils.string;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 中文姓名解析成 拼音串
 * 
 * @author ztg
 * @date 2019年5月30日
 */
public class JPinyinUtil {
  private static final Log LOGGER = LogFactory.getLog(JPinyinUtil.class);
  // private static String FX_PATH = "/pinyindata/fxpinyin.properties";
  // private static String DX_PATH = "/pinyindata/dxpinyin.properties";

  public static final String ZH_LANG = "zh";
  public static final String EN_LANG = "en";
  private static final List<String> FU_XING = new ArrayList<String>();
  public static String FIRST_NAME = "firstName";
  public static String LAST_NAME = "lastName";
  public static String FIRST_NAME_ZH = "firstNameZh";
  public static String LAST_NAME_ZH = "lastNameZh";

  static {
    FU_XING.add("欧阳");
    FU_XING.add("太史");
    FU_XING.add("端木");
    FU_XING.add("上官");
    FU_XING.add("司马");
    FU_XING.add("东方");
    FU_XING.add("独孤");
    FU_XING.add("南宫");
    FU_XING.add("万俟");
    FU_XING.add("闻人");
    FU_XING.add("夏侯");
    FU_XING.add("诸葛");
    FU_XING.add("尉迟");
    FU_XING.add("公羊");
    FU_XING.add("赫连");
    FU_XING.add("澹台");
    FU_XING.add("皇甫");
    FU_XING.add("宗政");
    FU_XING.add("濮阳");
    FU_XING.add("公冶");
    FU_XING.add("太叔");
    FU_XING.add("申屠");
    FU_XING.add("公孙");
    FU_XING.add("慕容");
    FU_XING.add("仲孙");
    FU_XING.add("钟离");
    FU_XING.add("长孙");
    FU_XING.add("宇文");
    FU_XING.add("司徒");
    FU_XING.add("鲜于");
    FU_XING.add("司空");
    FU_XING.add("闾丘");
    FU_XING.add("子车");
    FU_XING.add("亓官");
    FU_XING.add("司寇");
    FU_XING.add("巫马");
    FU_XING.add("公西");
    FU_XING.add("颛孙");
    FU_XING.add("壤驷");
    FU_XING.add("公良");
    FU_XING.add("漆雕");
    FU_XING.add("乐正");
    FU_XING.add("宰父");
    FU_XING.add("谷梁");
    FU_XING.add("拓跋");
    FU_XING.add("夹谷");
    FU_XING.add("轩辕");
    FU_XING.add("令狐");
    FU_XING.add("段干");
    FU_XING.add("百里");
    FU_XING.add("呼延");
    FU_XING.add("东郭");
    FU_XING.add("南门");
    FU_XING.add("羊舌");
    FU_XING.add("微生");
    FU_XING.add("公户");
    FU_XING.add("公玉");
    FU_XING.add("公仪");
    FU_XING.add("梁丘");
    FU_XING.add("公仲");
    FU_XING.add("公上");
    FU_XING.add("公门");
    FU_XING.add("公山");
    FU_XING.add("公坚");
    FU_XING.add("左丘");
    FU_XING.add("公伯");
    FU_XING.add("西门");
    FU_XING.add("公祖");
    FU_XING.add("第五");
    FU_XING.add("公乘");
    FU_XING.add("贯丘");
    FU_XING.add("公皙");
    FU_XING.add("南荣");
    FU_XING.add("东里");
    FU_XING.add("东宫");
    FU_XING.add("仲长");
    FU_XING.add("子书");
    FU_XING.add("子桑");
    FU_XING.add("即墨");
    FU_XING.add("达奚");
    FU_XING.add("褚师");
    FU_XING.add("东门");
    FU_XING.add("单于");
    FU_XING.add("淳于");
    FU_XING.add("完颜");
  }

  /**
   * 解析中文姓名的拼音， 用map存返回值，firstName名称, lastName姓氏
   * 
   * @param cname
   * @return
   */
  public static Map<String, String> parseFullNamePinYin(String cname) {
    try {
      Map<String, String> map = new HashMap<>();
      String lastName = "";
      String firstName = "";
      boolean fux = false;

      try {
        if (StringUtils.isNotBlank(cname)) {
          // 是否复姓
          cname = cname.trim();
          if (cname.length() > 2) {
            for (String fx : FU_XING) {
              if (cname.startsWith(fx)) {
                fux = true;
              }
            }
          }

          int lsEndIndex = 1;
          if (fux) {
            lsEndIndex = 2;
          }

          // String path = "";
          // // 根据是否复姓，截取得到姓氏字符串 和 确定properties文件路径
          // if (fux) {
          // path = FX_PATH;
          // } else {
          // path = DX_PATH;
          // }

          char[] lsNames = cname.substring(0, lsEndIndex).toCharArray();
          char[] fsNames = cname.substring(lsEndIndex, cname.length()).toCharArray();

          // 从自定义配置properties文件中获得姓氏拼音
          // String tempLsNames = gainLastNameOfProFile(cname.substring(0, lsEndIndex), path);
          String tempLsNames = gainLastNameOfMap(cname.substring(0, lsEndIndex), fux);

          // 获得姓氏拼音
          if (StringUtils.isNotBlank(tempLsNames)) {
            lastName = tempLsNames.trim();
          } else {
            for (char c : lsNames) {
              String npy = parseWordPinyin(c, PinyinFormat.WITHOUT_TONE);
              if (StringUtils.isNotBlank(npy)) {
                lastName = StringUtils.trim(lastName + " " + npy);
              }
            }
          }

          // 获得名字拼音
          for (char c : fsNames) {
            String npy = parseWordPinyin(c, PinyinFormat.WITHOUT_TONE);
            if (StringUtils.isNotBlank(npy)) {
              firstName = StringUtils.trim(firstName + " " + npy);
            }
          }
          if (!"".equals(firstName))
            firstName = StringUtils.substring(firstName, 0, 20);
        }
      } catch (Exception e) {
        LOGGER.warn("解析中文姓名异常..传入参数cname=" + cname, e);
      }

      firstName = CapitalizeTheFirstLetterAll(firstName);
      lastName = CapitalizeTheFirstLetterAll(lastName);
      map.put(FIRST_NAME, firstName);
      map.put(LAST_NAME, lastName);
      return map;
    } catch (Exception e) {
      return null;
    }
  }



  /**
   * 将姓氏或者名字转成的拼音字符串namestr，每个拼音首字母大写格式化
   * 
   * @param namestr
   * @return
   */
  private static String CapitalizeTheFirstLetterAll(String namestr) {
    if (namestr != null && namestr.indexOf(" ") > -1) {
      String CollName[] = namestr.split(" ");
      StringBuffer fnBuf = new StringBuffer();
      for (String fn : CollName) {
        fnBuf.append(String.valueOf(fn.charAt(0)).toUpperCase()).append(fn.substring(1)).append(" ");
      }
      namestr = fnBuf.toString().trim();
    } else if (namestr != null && namestr.length() > 0) {
      namestr = String.valueOf(namestr.charAt(0)).toUpperCase() + namestr.substring(1);
    }
    return namestr;
  }

  /**
   * 根据是否复姓分别从JPinyinConst中获取姓氏拼音
   * 
   * @param substring
   * @param fux
   * @return
   */
  public static String gainLastNameOfMap(String fromName, boolean isfux) {
    if (fromName != null && fromName.length() > 0) {
      String lastName = "";
      Map<String, String> xingMap;
      if (isfux) {
        xingMap = JPinyinConsts.getFxMap();
      } else {
        xingMap = JPinyinConsts.getDxMap();
      }

      if (xingMap != null && !xingMap.isEmpty()) {
        for (Entry<String, String> entry : xingMap.entrySet()) {
          String key = (String) entry.getKey();
          if (fromName.equals(key)) {
            lastName = entry.getValue();
            break;
          }
        }
      }
      return lastName;
    }
    return null;
  }

  /**
   * 根据是否复姓分别从配置文件中获取姓氏拼音
   * 
   * @param fromName
   * @param path
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static String gainLastNameOfProFile(String fromName, String path) {
    if (fromName != null && fromName.length() > 0) {
      String lastName = "";
      Properties pros = new Properties();
      try {
        InputStreamReader reader = new InputStreamReader(JPinyinUtil.class.getResourceAsStream(path), "utf-8");
        pros.load(reader);
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      Set keyValue = pros.keySet();
      for (Iterator it = keyValue.iterator(); it.hasNext();) {
        String key = (String) it.next();
        if (fromName.equals(key)) {
          lastName = pros.getProperty(key);
          break;
        }
      }
      return lastName;
    }
    return null;
  }

  /**
   * 拆分姓、名.
   * 
   * @param cname
   * @return
   */
  public static Map<String, String> parseZhfirstAndLast(String cname) {
    try {
      Map<String, String> map = new HashMap<String, String>();
      String firstNameZh = "";// 中文的名字
      String lastNameZh = "";// 中文的姓
      boolean fux = false;
      try {
        if (StringUtils.isNotBlank(cname)) {
          // 是否复姓
          if (cname.length() > 2) {
            for (String fx : FU_XING) {
              if (cname.startsWith(fx)) {
                fux = true;
              }
            }
          }
          if (fux == true) {
            lastNameZh = StringUtils.substring(cname, 0, 2);// 中文姓
            firstNameZh = StringUtils.substring(cname, 2, 12);
          } else {
            lastNameZh = StringUtils.substring(cname, 0, 1);
            firstNameZh = StringUtils.substring(cname, 1, 11);
          }
        }
      } catch (Exception e) {
        LOGGER.warn("拆分中文姓名失败:" + cname, e);
      }
      map.put(FIRST_NAME_ZH, firstNameZh);
      map.put(LAST_NAME_ZH, lastNameZh);
      return map;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 根据格式解析单个字的拼音
   * 
   * @param word
   * @param pinyinFormat
   * @return
   */
  public static String parseWordPinyin(char word, PinyinFormat pinyinFormat) {
    String[] names = null;
    try {
      names = PinyinHelper.convertToPinyinArray(word, pinyinFormat);
    } catch (Exception e) {
      names = null;
      e.printStackTrace();
    }
    if (names != null && names.length > 0) {
      return names[0];
    }
    return null;
  }

  /**
   * 将给定的字符串转换成全部是大写英文的字符串 如果字符串中含中文，则先将中文转换成汉语拼音
   * 
   * @param str
   * @return
   */
  @SuppressWarnings("static-access")
  public static String parseNameToUpperEn(String str) {
    String regexStr = "[\u4E00-\u9FA5]";
    Pattern p = Pattern.compile(regexStr);
    StringBuffer newStr = new StringBuffer();
    if (StringUtils.isNotBlank(str)) {
      char[] strToChar = str.toCharArray();
      for (int i = 0; i < strToChar.length; i++) {
        if (p.matches(regexStr, strToChar[i] + "")) {
          newStr.append(JPinyinUtil.parseWordPinyin(strToChar[i], PinyinFormat.WITHOUT_TONE));
        } else {
          newStr.append(strToChar[i]);
        }
      }

    }
    return newStr.toString().toUpperCase();
  }

  /**
   * 获取字符串的语言类型.
   * 
   * @param str
   * @return
   */
  public static String getStrLang(String str) {
    if (StringUtils.isBlank(str) || isChineseStr(str)) {
      return ZH_LANG;
    }
    return EN_LANG;
  }

  /**
   * 判断是否是中文关键词.
   * 
   * @param str
   */
  public static boolean isChineseStr(String str) {
    if (StringUtils.isBlank(str)) {
      return false;
    }
    return !StringUtils.isAsciiPrintable(str);
  }
}
