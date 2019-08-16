package com.smate.core.base.utils.string;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.acegisecurity.util.EncryptionUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.security.Des3Utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 公共的业务层工具类.
 * 
 * @author zb
 * 
 */
@SuppressWarnings("deprecation")
public class ServiceUtil {

  public static final String ZH_LANG = "zh";
  public static final String EN_LANG = "en";
  private static final Log LOGGER = LogFactory.getLog(ServiceUtil.class);
  private static final String UNICODE_EMAIL_CODE =
      "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?$";
  private static final String COMMON_EMAIL_CODE =
      "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d)|(([a-z]|\\d)([a-z]|\\d|-|\\.|_|~)*([a-z]|\\d)))\\.)+(([a-z])|(([a-z])([a-z]|\\d|-|\\.|_|~)*([a-z])))\\.?$";
  private static final Pattern UNICODE_EMAIL_PATTENRN = Pattern.compile(UNICODE_EMAIL_CODE);
  private static final Pattern COMMON_EMAIL_PATTENRN = Pattern.compile(COMMON_EMAIL_CODE);
  /**
   * 复姓
   */
  public static final List<String> FU_XING = new ArrayList<String>();
  /**
   * 姓氏多音字(可增加)
   */
  public static final Map<String, String> DUO_YIN_XING = new HashMap<>();

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
    FU_XING.add("公输");
    /*-----------------------------*/
    DUO_YIN_XING.put("单", "Shan");
    DUO_YIN_XING.put("仇", "Qiu");
    DUO_YIN_XING.put("解", "Xie");
    DUO_YIN_XING.put("覃", "Qin");
    DUO_YIN_XING.put("乐", "Yue");
    DUO_YIN_XING.put("曾", "Zeng");
    DUO_YIN_XING.put("费", "Fei");
    DUO_YIN_XING.put("车", "Che");
    DUO_YIN_XING.put("洗", "Xian");
    DUO_YIN_XING.put("宿", "Su");
    DUO_YIN_XING.put("什", "Shi");
    DUO_YIN_XING.put("省", "Sheng");
    DUO_YIN_XING.put("缪", "Miao");
    DUO_YIN_XING.put("柏", "Bai");
    DUO_YIN_XING.put("朝", "Zhao");
  }

  /*
   * 针对URL要求的 des3加密.
   * 
   * @param str
   * 
   * @return
   */
  public static String encodeToDes3(String str) {

    return ServiceUtil.encodeToDes3(str, ServiceConstants.ENCRYPT_KEY);
  }

  /**
   * 针对URL要求的des3解密 .
   * 
   * @param str
   * @return
   */
  public static String decodeFromDes3(String str) {

    return ServiceUtil.decodeFromDes3(str, ServiceConstants.ENCRYPT_KEY);
  }

  /**
   * 针对URL要求的 des3加密，特殊使用，例如特殊的查看权限.
   * 
   * @param str
   * @return
   */
  public static String specEncodeToDes3(String str) {

    return ServiceUtil.encodeToDes3(str, ServiceConstants.SPE_ENCRYPT_KEY);
  }

  /**
   * 针对URL要求的des3解密，特殊使用，例如特殊的查看权限.
   * 
   * @param str
   * @return
   */
  public static String specDecodeFromDes3(String str) {

    return ServiceUtil.decodeFromDes3(str, ServiceConstants.SPE_ENCRYPT_KEY);
  }

  /**
   * 针对egrant成果url的des3解密.
   * 
   * @param str
   * @return
   */
  public static String egrantDecodeFromDes3(String str) {
    return ServiceUtil.decodeFromDes3(str, ServiceConstants.EGRANT_ENCRYPT_KEY);
  }

  /**
   * 针对ISIS关键词确认url的des3解密.
   * 
   * @param str
   * @return
   */
  public static String IsisDecodeFromDes3(String str) {
    return ServiceUtil.decodeFromDes3(str, ServiceConstants.ISIS_ENCRYPT_KEY);
  }

  /**
   * 针对URL要求的 des3加密，指定加密KEY.
   * 
   * @param str
   * @param encryptKey
   * @return
   */
  public static String encodeToDes3(String str, String encryptKey) {

    try {
      if (StringUtils.isBlank(str)) {
        return null;
      }
      return java.net.URLEncoder.encode(EncryptionUtils.encrypt(encryptKey, str), "utf-8");
    } catch (Exception e) {
      LOGGER.warn("des3加密失败:" + str);
      return null;
    }
  }

  /**
   * 针对URL要求的des3解密 ，指定加密KEY.
   * 
   * @param str
   * @param encryptKey
   * @return
   */
  public static String decodeFromDes3(String str, String encryptKey) {

    try {
      if (StringUtils.isBlank(str)) {
        return null;
      }
      String tmp = null;
      tmp = str.replace("+", "%2B");
      tmp = tmp.replace("=", "%3D");
      tmp = tmp.replace("%25", "%");
      return EncryptionUtils.decrypt(encryptKey, java.net.URLDecoder.decode(tmp, "utf-8"));

    } catch (Exception e) {
      LOGGER.warn("des3解密失败:" + str);
      return null;
    }
  }

  /**
   * 格式化指定日期的格式为中文格式字符.
   * 
   * @param date
   * @return
   */
  public static String formateZhDateFull(Date date) {
    SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return formate.format(date);
  }

  /**
   * 获取过期时间字符串，供共享使用，共享key=des3(keyId,outdate).
   * 
   * @param hours
   * @return
   */
  public static String getOutDateStr(int hours) {

    SimpleDateFormat form = new SimpleDateFormat("yyyyMMddHHmmss");
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    calendar.set(Calendar.HOUR_OF_DAY, hour + hours);
    Date cc = calendar.getTime();
    return form.format(cc);
  }

  /**
   * 解析共享key失败,共享key=des3(keyId,outdate).
   * 
   * @param key
   * @return
   */
  public static String[] parseShareKey(String key) {

    try {
      if (StringUtils.isNotBlank(key)) {
        // 解密
        String deKey = Des3Utils.decodeFromDes3(key);
        // 格式是否正确
        if (deKey != null && deKey.matches("\\d+,\\d+,\\d+")) {
          String[] keys = deKey.split(",");
          // 获取时间
          SimpleDateFormat form = new SimpleDateFormat("yyyyMMddHHmmss");
          Date outDate = form.parse(keys[2]);
          // 是否过期
          if (outDate.compareTo(new Date()) > 0) {
            return keys;
          }
        }
      }
      return null;
    } catch (Exception e) {
      LOGGER.warn("解析共享key失败:" + key, e);
      return null;
    }

  }

  /**
   * 获取List中的随机对象.
   * 
   * @param list
   * @return
   */
  public static <T> T randomArray(List<T> list) {

    if (list != null && list.size() > 0) {
      int size = list.size();
      if (size == 1) {
        return list.get(0);
      }
      int posit = RandomUtils.nextInt(size);
      return list.get(posit);
    }
    return null;
  }

  /**
   * 获取List中的随机对象.
   * 
   * @param <T>
   * @param list
   * @param exList 排除的对象，此处必须为list的子集.
   * @return
   */
  public static <T> T randomArray(List<T> list, List<T> exList) {

    if (list != null && list.size() > 0) {
      if (exList != null) {
        // 集合必须更大
        if (list.size() > exList.size()) {
          T data = randomArray(list);
          while (exList.contains(data)) {
            data = randomArray(list);
          }
          return data;
        }
      } else {
        return randomArray(list);
      }
    }
    return null;
  }

  /**
   * 获取当前年.
   * 
   * @return
   */
  public static int getCurrentYear() {

    Calendar now = Calendar.getInstance();
    return now.get(Calendar.YEAR);
  }

  /**
   * 产生姓氏
   * 
   * @param zhName
   * @return
   */
  public static Map<String, Object> generateFirstLastName(String zhName) {
    Map<String, Object> resMap = new HashMap<>();
    if (StringUtils.isBlank(zhName) || !isChinese(zhName)) {
      return resMap;
    }

    Map<String, Set<String>> psnNameList = new HashMap<>();
    Map<String, String> parsePinYin = parsePinYin(zhName);
    String lastName = parsePinYin.get("lastName").toLowerCase(); /* ma ;duan;ou yang */
    String firstName = parsePinYin.get("firstName").toLowerCase(); /* jian;wen jie;xiang yuan */
    resMap.put("lastName", lastName);
    resMap.put("firstName", firstName);
    return resMap;
  }

  /**
   * 将人员的中文姓氏转换为对应的拼音,首字母大写
   * 
   * @author SYL
   * @param lastName
   * @return
   */
  public static String generateLastPsnName(String lastName) {
    if (StringUtils.isNotBlank(lastName) && isChinese(lastName)) {
      lastName = lastName.trim();
      for (String key : DUO_YIN_XING.keySet()) {// 是否为多音姓氏
        if (key.equals(lastName)) {
          return DUO_YIN_XING.get(lastName);
        }
      }
      char[] charArray = lastName.toCharArray();
      StringBuffer lastNameStr = new StringBuffer();
      for (char c : charArray) {
        String lowCaseStr = parseWordPinyin(c);
        lastNameStr.append(Character.toUpperCase(lowCaseStr.charAt(0))).append(lowCaseStr.substring(1)).append(" ");
      }
      return lastNameStr.toString().trim();
    }
    return Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
  }

  /**
   * 根据人员中文姓名生成人员的全称\简称\前缀名(fullname,initname,prefixname) eg:马建,段文杰,欧阳向远
   * 
   * @param zhName
   * @return Map<String, Set<String>>
   * @author LIJUN
   * @date 2018年5月24日
   */
  public static Map<String, Set<String>> generatePsnName(String zhName) {

    if (StringUtils.isBlank(zhName) || !isChinese(zhName)) {
      return null;
    }
    Map<String, Set<String>> psnNameList = new HashMap<>();
    Map<String, String> parsePinYin = parsePinYin(zhName);
    String lastName = parsePinYin.get("lastName").toLowerCase(); /* ma ;duan;ou yang */
    String firstName = parsePinYin.get("firstName").toLowerCase(); /* jian;wen jie;xiang yuan */

    Set<String> fullname = new HashSet<>();
    Set<String> initname = new HashSet<>();
    Set<String> prefixname = new HashSet<>();
    Boolean isFx = false;
    for (String fx : FU_XING) {
      if (zhName.startsWith(fx)) {
        isFx = true;
        break;
      }
    }
    if (isFx) {
      if (zhName.length() == 3) {
        fullname.add(lastName + " " + firstName);/* ou yang qiong */
        fullname.add(lastName + firstName);/* ou yangqiong */
        fullname.add(firstName + " " + lastName); /* qiong ou yang */
        fullname.add(firstName + " " + lastName.replace(" ", ""));/* qiong ouyang */
        String finit = firstName.substring(0, 1);// q
        initname.add(lastName + " " + finit);/* ou yang q */
        initname.add(lastName.replace(" ", "") + " " + finit);/* ouyang q */
        initname.add(finit + " " + lastName);/* q ouyang */
        initname.add(finit + " " + lastName.replace(" ", ""));/* q ou yang */
        prefixname.add(lastName.replace(" ", "") + " " + finit); /* ou yang q */
      } else if (zhName.length() == 2) {
        fullname.add(lastName + " " + firstName);// dong fang
        fullname.add(firstName + " " + lastName);// fang dong
        initname.add(lastName + " " + firstName.substring(0, 1));// maj
        initname.add(firstName.substring(0, 1) + " " + lastName);// j
      } else {
        fullname.add(lastName.replace(" ", "") + " " + firstName.replace(" ", "")); /* xiangyuan ouyang */
        fullname.add(firstName.replace(" ", "") + " " + lastName.replace(" ", ""));/* ouyang xiangyuan */
        fullname.add(lastName + " " + firstName);// xiang yuan ou
                                                 // yang
        fullname.add(firstName + " " + lastName);// ou yang xiang
        // yuan
        fullname.add(lastName + " " + firstName.replace(" ", "")); /* ou yang xiangyuan */
        fullname.add(firstName.replace(" ", "") + " " + lastName); /* xiangyuan ou yang */
        fullname.add(lastName.replace(" ", "") + " " + firstName); /* ouyang xiang yuan */
        fullname.add(firstName + " " + lastName.replace(" ", ""));/* xiang yuan ouyang */
        String finitblack = firstName.substring(0, 1) + " " + firstName.split(" ")[1].substring(0, 1);// x
        String finit = firstName.substring(0, 1) + firstName.split(" ")[1].substring(0, 1);// xy
        initname.add(lastName + " " + finitblack);/* ou yang x y */
        initname.add(finitblack + " " + lastName);/* x y ou yang */

        initname.add(lastName.replace(" ", "") + " " + finitblack); /* ouyang x y */
        initname.add(finitblack + " " + lastName.replace(" ", "")); /* x y ouyang */

        initname.add(lastName + " " + finit); /* ou yang xy */
        initname.add(finit + " " + lastName); /* xy ou yang */

        initname.add(lastName.replace(" ", "") + " " + finit); /* ouyang xy */
        initname.add(finit + " " + lastName.replace(" ", "")); /* xy ouyang */

        initname.add(lastName + " " + finit.substring(0, 1)); /* ou yang x */
        initname.add(finit.substring(0, 1) + " " + lastName); /* x ou yang */

        initname.add(lastName.replace(" ", "") + " " + finit.substring(0, 1));/* ouyang x */
        initname.add(finit.substring(0, 1) + " " + lastName.replace(" ", "")); /* x ouyang */

        prefixname.add(lastName.replace(" ", "") + " " + finit.substring(0, 1)); /* ouyang x */
      }

    } else {

      if (zhName.length() == 2) {
        fullname.add(lastName + " " + firstName);// ma jian
        fullname.add(firstName + " " + lastName);// jian ma

        initname.add(lastName + " " + firstName.substring(0, 1));// ma
                                                                 // j
        initname.add(firstName.substring(0, 1) + " " + lastName);// j
                                                                 // ma
        prefixname.add(lastName + " " + firstName.substring(0, 1)); // ma
                                                                    // j
      }
      if (zhName.length() == 3) {
        fullname.add(lastName + " " + firstName);// duan wen jie
        fullname.add(lastName + " " + firstName.replace(" ", ""));// duan
                                                                  // wenjie
        fullname.add(firstName + " " + lastName);// wen jie duan
        fullname.add(firstName.replace(" ", "") + " " + lastName);// wenjie
                                                                  // duan
        String finitblack = firstName.substring(0, 1) + " " + firstName.split(" ")[1].substring(0, 1);// w
                                                                                                      // j
        String finit = firstName.substring(0, 1) + firstName.split(" ")[1].substring(0, 1);// wj

        initname.add(lastName + " " + finitblack);// duan w j
        initname.add(lastName + " " + finit);// duan wj
        initname.add(firstName.replace(" ", "") + " " + lastName.substring(0, 1));// wenjie
                                                                                  // d
        initname.add(firstName.substring(0, 1) + " " + lastName);// w
                                                                 // duan
        initname.add(finitblack + " " + lastName);// w j duan
        prefixname.add(lastName + " " + firstName.substring(0, 1)); // duan
      }

    }

    psnNameList.put("fullname", fullname);
    psnNameList.put("initname", initname);
    psnNameList.put("prefixname", prefixname);
    return psnNameList;
  }

  public static boolean isChinese(String str) {
    if (str == null) {
      return false;
    }
    Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
    // pattern.matcher(str.trim()).matches()包含中文
    return pattern.matcher(str.trim()).matches();
  }

  /**
   * 解析姓名的拼音.
   * 
   * @param cname
   * @return
   */
  public static Map<String, String> parsePinYin(String cname) {
    try {
      Map<String, String> map = new HashMap<String, String>();
      String firstName = "";
      String lastName = "";
      int fxLength = 0;// 复姓姓名的长度
      try {
        if (StringUtils.isNotBlank(cname)) {

          // 是否复姓
          if (cname.length() > 2) {
            for (String fx : FU_XING) {
              if (cname.startsWith(fx)) {
                fxLength = fx.length();
                break;
              }
            }
          }
          if (fxLength != 0) {// 为复姓
            lastName = generateLastPsnName(cname.substring(0, fxLength));
            firstName = parseWordPinyin(cname.substring(fxLength), " ");
          } else {
            lastName = generateLastPsnName(cname.substring(0, 1));
            firstName = parseWordPinyin(cname.substring(1), " ");
          }
          if (!"".equals(firstName)) {
            firstName = StringUtils.substring(firstName, 0, 21);
          }
        }
      } catch (Exception e) {
        LOGGER.warn("解析姓名的拼音失败:" + cname, e);
      }
      map.put("firstName", firstName);
      map.put("lastName", lastName);
      return map;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 拆分姓、名. zhFirstName最多50个字符
   * 
   * @param cname
   * @return
   */
  public static Map<String, String> parseZhfirstAndLastNew(String cname) {

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
            lastNameZh = org.apache.commons.lang.StringUtils.substring(cname, 0, 2);// 中文姓
            firstNameZh = org.apache.commons.lang.StringUtils.substring(cname, 2, 52);
          } else {
            lastNameZh = org.apache.commons.lang.StringUtils.substring(cname, 0, 1);
            firstNameZh = org.apache.commons.lang.StringUtils.substring(cname, 1, 51);
          }
        }

      } catch (Exception e) {
        LOGGER.warn("拆分姓名失败:" + cname, e);
      }
      map.put("firstNameZh", firstNameZh);
      map.put("lastNameZh", lastNameZh);
      return map;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 解析单个字的拼音.
   * 
   * @param word
   * @return
   */
  public static String parseWordPinyin(char word) {

    HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
    format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    String[] name = null;
    try {
      name = PinyinHelper.toHanyuPinyinStringArray(word, format);
    } catch (BadHanyuPinyinOutputFormatCombination e) {
      name = null;
      e.printStackTrace();
    }
    if (name != null) {
      return name[0];
    }
    return null;
  }

  /**
   * 将一个中文字符串转换为拼音，首字母大写
   * 
   * @author SYL
   * @param zhWord 要转换的中文字符
   * @param splitStr 每个拼音之间的分割符（eg:splitStr=" ";你好-> Ni Hao）
   * @return
   */
  public static String parseWordPinyin(String zhWord, String splitStr) {
    if (StringUtils.isNotBlank(zhWord)) {
      StringBuffer pinyinStr = new StringBuffer();
      char[] charArray = zhWord.trim().toCharArray();
      for (int i = 0; i < charArray.length; i++) {
        if (!Character.isSpaceChar(charArray[i])) {
          if (isChinese(Character.toString(charArray[i]))) {
            String lowCaseStr = parseWordPinyin(charArray[i]);
            pinyinStr.append(Character.toUpperCase(lowCaseStr.charAt(0))).append(lowCaseStr.substring(1));
            if (i < charArray.length - 1 && !Character.isSpaceChar(charArray[i + 1])) {// 如果中文后面不是空格，则增加一个分隔符
              pinyinStr.append(splitStr);
            }
          } else {
            pinyinStr.append(charArray[i]);
            if (i < charArray.length - 1 && isChinese(Character.toString(charArray[i + 1]))) {// 如果英文单词后面是中文，则增加一个分隔符
              pinyinStr.append(splitStr);
            }
          }
        } else {
          pinyinStr.append(charArray[i]);
        }
      }
      return pinyinStr.toString();
    }
    return zhWord;
  }

  /**
   * 将一个字符串中包含的所有中文转换为拼音，并将其所有多音字组合进行返回（eg:"仇人"->{"chou ren","qiu ren"}）
   * 
   * @return
   * @throws BadHanyuPinyinOutputFormatCombination
   */
  public static Set<String> parseWordPinyinAll(String zhStr) throws BadHanyuPinyinOutputFormatCombination {
    if (StringUtils.isNotBlank(zhStr)) {
      Set<String> allPinyinStr = new HashSet<>();
      char[] charArray = zhStr.toCharArray();
      HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
      format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
      for (int i = 0; i < charArray.length; i++) {
        if (isChinese(charArray[i] + "")) {
          String[] allPinyin = PinyinHelper.toHanyuPinyinStringArray(charArray[i], format);
          if (allPinyinStr.size() == 0) {// 第一个字符直接加进去
            if (charArray.length > 1 && i < charArray.length - 1 && isChinese(charArray[i + 1] + "")) {
              for (String string : allPinyin) {
                allPinyinStr.add(string + " ");
              }
            } else {
              allPinyinStr.addAll(Arrays.asList(allPinyin));
            }
          } else {
            Set<String> set = new HashSet<>();
            for (String py : allPinyinStr) {
              for (String string : allPinyin) {
                if (i < charArray.length - 1 && !Character.isSpaceChar(charArray[i + 1]))
                  set.add(py + string + " ");
                else
                  set.add(py + string);
              }
            }
            allPinyinStr = set;
          }
        } else {
          if (allPinyinStr.size() == 0) {
            if (i < charArray.length - 1 && isChinese(charArray[i + 1] + "") && !Character.isSpaceChar(charArray[i])) {
              allPinyinStr.add(charArray[i] + " ");
            } else {
              allPinyinStr.add(charArray[i] + "");
            }
          } else {
            Set<String> set = new HashSet<>();
            for (String py : allPinyinStr) {
              if (i < charArray.length - 1 && isChinese(charArray[i + 1] + "")) {
                set.add(py + charArray[i] + " ");
              } else {
                set.add(py + charArray[i]);
              }
            }
            allPinyinStr = set;
          }
        }
      }
      return allPinyinStr;
    }
    return null;
  }

  /**
   * 将逗号分隔的长整型字符串解析.
   * 
   * @param str
   * @return
   */
  public static List<Long> splitStrToLong(String str) {
    if (StringUtils.isNotBlank(str)) {
      String[] strs = str.split(",");
      List<Long> list = new ArrayList<Long>();
      for (String lng : strs) {
        // 数据库中的id 最大18位
        if (lng.length() > 18) {
          continue;
        }
        if (NumberUtils.isNumber(lng)) {
          list.add(Long.valueOf(lng));
        }
      }
      return list;
    }
    return null;
  }

  /**
   * 将逗号分隔的长整型字符串解析.(没有表达式验证)
   * 
   * @param str
   * @return
   */
  public static List<Long> splitStrToLongList(String str) {
    if (StringUtils.isNotBlank(str)) {
      String[] strs = str.split(",");
      List<Long> list = new ArrayList<Long>();
      for (String lng : strs) {
        list.add(Long.valueOf(lng));
      }
      return list;
    }
    return null;
  }

  /**
   * 将逗号分隔的整型字符串解析.
   * 
   * @param str
   * @return
   */
  public static List<Integer> splitStrToInteger(String str) {
    if (StringUtils.isNotBlank(str)) {
      String[] strs = str.split(",");
      List<Integer> list = new ArrayList<Integer>();
      for (String lng : strs) {
        list.add(Integer.valueOf(lng));
      }
      return list;
    }
    return null;
  }

  /**
   * 将逗号分隔的长整型加密字符串解析.
   * 
   * @param str
   * @return
   */
  public static List<Long> splitDesStrToLong(String str) {

    if (StringUtils.isBlank(str)) {
      return null;
    }
    String[] strArray = str.split(",");
    if (strArray.length > 0) {
      List<Long> list = null;
      for (String desid : strArray) {
        Long id = null;
        if (NumberUtils.isNumber(desid)) {
          id = Long.valueOf(desid);
        } else {
          String strId = Des3Utils.decodeFromDes3(desid);
          if (NumberUtils.isNumber(strId)) {
            id = Long.valueOf(strId);
          }
        }
        if (id != null) {
          list = list == null ? new ArrayList<Long>() : list;
          list.add(Long.valueOf(id));
        }
      }
      return list;
    }
    return null;
  }

  /**
   * 将逗号分隔的加密整型字符串解析.
   * 
   * @param str
   * @return
   */
  public static List<Integer> splitDesStrToInteger(String str) {

    if (StringUtils.isBlank(str)) {
      return null;
    }
    String[] strArray = str.split(",");
    if (strArray.length > 0) {
      List<Integer> list = null;
      for (String desid : strArray) {
        list = list == null ? new ArrayList<Integer>() : list;
        Integer id = null;
        if (NumberUtils.isNumber(desid)) {
          id = Integer.valueOf(desid);
        } else {
          String strId = Des3Utils.decodeFromDes3(desid);
          if (NumberUtils.isNumber(strId)) {
            id = Integer.valueOf(strId);
          }
        }
        if (id != null) {
          list.add(Integer.valueOf(id));
        }
      }
      return list;
    }
    return null;
  }

  /**
   * 将逗号分隔的长整型字符串解析.
   * 
   * @param str
   * @return
   */
  public static Set<Long> splitStrToLongSet(String str) {
    if (StringUtils.isNotBlank(str)) {
      String[] strs = str.split(",");
      Set<Long> set = new HashSet<Long>();
      for (String lng : strs) {
        set.add(Long.valueOf(lng));
      }
      return set;
    }
    return null;
  }

  public static String filterNull(Object obj) {
    if (obj == null) {
      return "";
    }
    return (String.valueOf(obj).trim());
  }

  /**
   * 未录入数据的，提示未填写.
   * 
   * @param content
   * @param locale
   * @return
   */
  public static String refreshEmptyContent(String content, Locale locale) {
    if (StringUtils.isNotBlank(content)) {
      return content;
    } else {
      return "";
    }
  }

  /**
   * 获取异常堆栈串.
   * 
   * @param e
   * @return
   */
  public static String getErrorTranceStr(Exception e) {
    // 获取错误信息
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }

  /**
   * 普通EMAIL校验.
   * 
   * @param email
   * @return
   */
  public static boolean isEmailValidate(String email) {

    if (StringUtils.isBlank(email)) {
      return false;
    }

    if (!COMMON_EMAIL_PATTENRN.matcher(email).find()) {
      return false;
    }
    return true;
  }

  /**
   * 带unicode的EMAIL校验，支持中文.
   * 
   * @param email
   * @return
   */
  public static boolean isUnicodeEmailValidate(String email) {

    if (StringUtils.isBlank(email)) {
      return false;
    }

    if (!UNICODE_EMAIL_PATTENRN.matcher(email).find()) {
      return false;
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  public static Collection<Collection<Long>> splitList(Collection<Long> collection, int size) {

    if (CollectionUtils.isEmpty(collection)) {
      return CollectionUtils.EMPTY_COLLECTION;
    }
    Collection<Collection<Long>> container = new ArrayList<Collection<Long>>();
    Iterator<Long> iter = collection.iterator();
    int i = 0;
    Collection<Long> item = null;
    while (iter.hasNext()) {
      if (i == size || i == 0) {
        item = new ArrayList<Long>();
        container.add(item);
        i = 0;
      }
      Long obj = iter.next();
      item.add(obj);
      i++;
    }
    return container;
  }

  @SuppressWarnings("unchecked")
  public static Collection<Collection<String>> splitStrList(Collection<String> collection, int size) {

    if (CollectionUtils.isEmpty(collection)) {
      return CollectionUtils.EMPTY_COLLECTION;
    }
    Collection<Collection<String>> container = new ArrayList<Collection<String>>();
    Iterator<String> iter = collection.iterator();
    int i = 0;
    Collection<String> item = null;
    while (iter.hasNext()) {
      if (i == size || i == 0) {
        item = new ArrayList<String>();
        container.add(item);
        i = 0;
      }
      String obj = iter.next();
      item.add(obj);
      i++;
    }
    return container;
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
   * 判断是否是NSFC单位.
   * 
   * @param insId
   * @return
   */
  public static boolean isNsfcIns(Long insId) {

    if (insId == null) {
      return false;
    }
    for (long nsfcIns : ServiceConstants.NSFC_INS_IDS) {
      if (nsfcIns == insId) {
        return true;
      }
    }
    return false;
  }

  /**
   * 
   * 将给定的字符串转换成全部是大写英文的字符串 如果字符串中含中文，则先将中文转换成汉语拼音
   * 
   * @param str
   * @return
   */
  public static String parseNameToUpperEn(String str) {
    String regexStr = "[\u4E00-\u9FA5]";
    Pattern p = Pattern.compile(regexStr);
    StringBuffer newStr = new StringBuffer();
    if (StringUtils.isNotBlank(str)) {
      char[] strToChar = str.toCharArray();
      for (int i = 0; i < strToChar.length; i++) {
        if (p.matches(regexStr, strToChar[i] + "")) {
          newStr.append(ServiceUtil.parseWordPinyin(strToChar[i]));
        } else {
          newStr.append(strToChar[i]);
        }
      }

    }
    return newStr.toString().toUpperCase();
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
            lastNameZh = org.apache.commons.lang.StringUtils.substring(cname, 0, 2);// 中文姓
            firstNameZh = org.apache.commons.lang.StringUtils.substring(cname, 2, 12);
          } else {
            lastNameZh = org.apache.commons.lang.StringUtils.substring(cname, 0, 1);
            firstNameZh = org.apache.commons.lang.StringUtils.substring(cname, 1, 11);
          }
        }

      } catch (Exception e) {
        LOGGER.warn("拆分姓名失败:" + cname, e);
      }
      map.put("firstNameZh", firstNameZh);
      map.put("lastNameZh", lastNameZh);
      return map;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 判断字符串是否是乱码
   *
   * @param strName 字符串
   * @return 是否是乱码
   */
  public static boolean isMessyCode(String strName) {
    Pattern p = Pattern.compile("\\s*|t*|r*|n*");
    Matcher m = p.matcher(strName);
    String after = m.replaceAll("");
    String temp = after.replaceAll("\\p{P}", "");
    char[] ch = temp.trim().toCharArray();
    float chLength = ch.length;
    float count = 0;
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      if (!Character.isLetterOrDigit(c)) {
        if (!isChinese(String.valueOf(c))) {
          count = count + 1;
        }
      }
    }
    float result = count / chLength;
    if (result > 0.4) {
      return true;
    } else {
      return false;
    }

  }

  public static String getRegularString(String s) {
    if (org.apache.commons.lang3.StringUtils.isBlank(s)) {
      return "";
    }
    char[] chars = s.toCharArray();
    char[] chars2 = new char[chars.length];
    int flag = 0;
    for (int i = 0; i < chars.length; i++) {
      if (!isMessyCode(String.valueOf(chars[i]))) {
        chars2[flag++] = chars[i];
      }
    }
    return String.valueOf(chars2);
  }

  public static void main(String[] args) throws Exception {
    System.out.println(getRegularString("in Cognitive Radio￼Networks.png"));
    // String parseWordPinyin = parseWordPinyin("TOm 测试数 YY ^*()", " ");
    // System.out.println(parseWordPinyin);
    Set<String> parseWordPinyinAll = parseWordPinyinAll("asd仇单快");
    parseWordPinyinAll.forEach(i -> System.out.println(i));
    System.out.println(parseWordPinyin("asd仇单快", ","));
  }
}
