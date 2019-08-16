package com.smate.core.base.utils.string;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * 拼音工具类
 * 该工具类，用于成果验证 名字转换成果拼音。
 *
 * @author aijiangbin
 * @create 2019-03-01 16:50
 **/
public class PinYinUtils {

  private static final Log LOGGER = LogFactory.getLog(ServiceUtil.class);


  /**
   * 获取一段文字的所有拼音组合情况,以list<String>形式返回
   */
  public static List<String> getHardPinyins(String s){
    if(s==null){
      s="";//null时处理，后边处理时报错
    }
    String[][] allPinyins=new String[s.length()][];//存放整个字符串的各个字符所有可能的拼音情况，如果非汉字则是它本身
    char[] words=s.toCharArray();//把这段文字转成字符数组
    for(int i=0;i<words.length;i++){
      allPinyins[i]=getAllPinyins(words[i]);//每个字符的所有拼音情况
      if(allPinyins[i] !=null && allPinyins[i].length>0){
        List<String> name = new ArrayList<>();
        for (int k = 0 ; k<allPinyins[i].length ; k++){
          name.add(allPinyins[i][k]);
          if(allPinyins[i][k].contains("v")){
            // lu: lv lu lü
            name.add(allPinyins[i][k].replace("v","u:"));
            name.add(allPinyins[i][k].replace("v","u"));
          }
        }
        allPinyins[i] = name.toArray(new String[name.size()]);
      }
    }
    String[] resultArray=recursionArrays(allPinyins,allPinyins.length,0);
    return Arrays.asList(resultArray);
  }

  /**
   * 获取包含一个字符的拼音（多音字则以数组形式返回多个）,非汉字则返回字符本身
   */
  public static String[] getAllPinyins(char word){
    HanyuPinyinOutputFormat pinyinFormat = new HanyuPinyinOutputFormat();	//创建拼音输入格式类
    pinyinFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);//指定格式中的大小写属性为小写
    pinyinFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//指定音标格式无音标
    pinyinFormat.setVCharType(HanyuPinyinVCharType.WITH_V);//指定用v表示ü
    String[] formatPinyin=null;
    try {
      formatPinyin = PinyinHelper.toHanyuPinyinStringArray(word, pinyinFormat);//获取对应的汉字拼音，不是汉字返回null
    } catch (BadHanyuPinyinOutputFormatCombination e) {//会抛出异常，捕获异常
      //logger.error(e.getMessage());
      e.printStackTrace();
    }
    if(formatPinyin==null){
      formatPinyin=new String[1];
      formatPinyin[0]=String.valueOf(word);//返回读音,如果多音字自返回一个
    }
    return formatPinyin;
  }

  /**
   * 用递归方法，求出这个二维数组每行取一个数据组合起来的所有情况，返回一个字符串数组
   * @param s 求组合数的2维数组
   * @param len 此二维数组的长度，省去每一次递归长度计算的时间和空间消耗，提高效率
   * @param cursor 类似JDBC、数据库、迭代器里的游标，指明当前从第几行开始计算求组合数，此处从0开始（数组第一行）
   * 				   避免在递归中不断复制剩余数组作为新参数，提高时间和空间的效率
   * @return String[] 以数组形式返回所有的组合情况
   */
  public static String[] recursionArrays(String[][] s,int len,int cursor){
    if(cursor<=len-2){
      int len1 = s[cursor].length;
      int len2 = s[cursor+1].length;
      int newLen = len1*len2;
      String[] temp = new String[newLen];
      int index = 0;
      for(int i=0;i<len1;i++){
        for(int j=0;j<len2;j++){
          temp[index] = s[cursor][i] +";"+ s[cursor+1][j];
          index ++;
        }
      }
      s[cursor+1]=temp;
      cursor++;
      return recursionArrays(s,len,cursor);
    }else{
      return s[len-1];
    }
  }

  /**
   * 产生多音名字的组合
   * @param zhName
   * @return
   */
  public static Map<String, Set<String>> generatePsnName(String zhName) {

    if (StringUtils.isBlank(zhName) || !ServiceUtil.isChinese(zhName)) {
      return null;
    }
    Set<String> fullname = new HashSet<>();
    Set<String> initname = new HashSet<>();
    Set<String> prefixname = new HashSet<>();
    Map<String, Set<String>> psnNameList = new HashMap<>();
    List<Map<String, String>> maps = parsePinYin(zhName);
    if(CollectionUtils.isNotEmpty(maps)){
      maps.stream().forEach(map ->{
        Map<String, Set<String>> combination = generateNameCombination(zhName, map);
        fullname.addAll(combination.get("fullname"));
        initname.addAll(combination.get("initname"));
        prefixname.addAll(combination.get("prefixname"));
      });
    }
    psnNameList.put("fullname", fullname);
    psnNameList.put("initname", initname);
    psnNameList.put("prefixname", prefixname);
    return psnNameList;
  }

  /**
   * 产生名字组合
   * @param zhName
   * @param parsePinYin
   */
  private static Map<String, Set<String>>  generateNameCombination(String zhName,
      Map<String, String> parsePinYin) {
    Map<String, Set<String>> psnNameList =  new HashMap<>();
    String lastName = parsePinYin.get("lastName").toLowerCase(); /* ma ;duan;ou yang */
    String firstName = parsePinYin.get("firstName").toLowerCase(); /* jian;wen jie;xiang yuan */

    Set<String> fullname = new HashSet<>();
    Set<String> initname = new HashSet<>();
    Set<String> prefixname = new HashSet<>();
    Boolean isFx = false;
    for (String fx : ServiceUtil.FU_XING) {
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
      } else if(zhName.length() == 2){
        fullname.add(lastName + " " + firstName);// dong fang
        fullname.add(firstName + " " + lastName);//fang dong
        initname.add(lastName + " " + firstName.substring(0, 1));// maj
        initname.add(firstName.substring(0, 1) + " " + lastName);// j
      }else {
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
        initname.add(finit + " " + lastName );//wj duan
        //姓氏不要缩写
        //initname.add(firstName.replace(" ", "") + " " + lastName.substring(0, 1));// wenjie d
        initname.add(firstName.substring(0, 1) + " " + lastName);// w
        // duan
        initname.add(finitblack + " " + lastName);// w j duan
        prefixname.add(lastName + " " + firstName.substring(0, 1)); // duan
      }

    }

    psnNameList.put("fullname", fullname);
    psnNameList.put("initname", initname);
    psnNameList.put("prefixname", prefixname);
    return  psnNameList;
  }


  /**
   * 解析多音姓名的拼音.
   *
   * @param cname
   * @return
   */
  public static List<Map<String, String>> parsePinYin(String cname) {

    try {
      List<Map<String, String>>  resultList = new ArrayList<>();
      boolean fux = false;
      try {
        if (StringUtils.isNotBlank(cname)) {
          // 是否复姓
          if (cname.length() > 2) {
            for (String fx : ServiceUtil.FU_XING) {
              if (cname.startsWith(fx)) {
                fux = true;
              }
            }
          }
          HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
          format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
          char[] names = cname.trim().toCharArray();
          List<String> list=getHardPinyins(cname);

          for(String s : list){
           LOGGER.debug(cname+" = "+s.replaceAll(";" ," "));
            String firstName = "";
            String lastName = "";
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < names.length; i++) {
                String[] split = s.split(";");
                String npy = split[i];
                // 复姓
                if (fux && npy != null) {
                  if (i == 0 || i == 1) {
                    lastName = StringUtils.trim(lastName + " " + npy);
                  } else {
                    if (npy != null) {
                      firstName += " " + npy;
                    }
                  }
                } else if (npy != null) {
                  if (i == 0) {
                    lastName = npy;
                  } else {
                    firstName += " " + npy;
                  }
                }
              }
             //一层循环遍历完
            if (!"".equals(firstName)) {
              firstName = StringUtils.substring(firstName, 1, 21);
            }
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
            resultList.add(map);
          }

        }
      } catch (Exception e) {
        LOGGER.warn("解析姓名的拼音失败:" + cname, e);
      }
      return resultList;
    } catch (Exception e) {
      return null;
    }
  }

  public static void main(String[] args) throws Exception {
    String statence="重生，长大张";//定义一个长字符串
    List<String> hardPinyins = getHardPinyins(statence);
    for(String s: hardPinyins){
      System.out.println(s.replaceAll(";" ," "));
    }

  }
}
