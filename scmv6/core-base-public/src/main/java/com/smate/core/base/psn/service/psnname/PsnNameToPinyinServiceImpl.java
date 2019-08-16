package com.smate.core.base.psn.service.psnname;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.consts.dao.psnname.ConstPsnLastNamePyDao;
import com.smate.core.base.consts.dao.psnname.ConstSurNameDAO;
import com.smate.core.base.consts.model.psnname.ConstSurNameDTO;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@Service("PsnNameToPinyinService")
public class PsnNameToPinyinServiceImpl implements PsnNameToPinyinService {

  @Autowired
  private ConstPsnLastNamePyDao constPsnLastNamePyDao;

  @Autowired
  private ConstSurNameDAO constSurNameDao;

  @Override
  public Map<String, String> parseNameToPinyin(String psnName) {
    Map<String, String> nameMap = new HashMap<>();
    if (StringUtils.isNotBlank(psnName)) {
      String firstName = "";
      String lastName = "";
      psnName = psnName.trim();
      int fxLength = lastNameLength(psnName);// 复姓姓名的长度
      lastName = parseLastNameToPinyin(psnName.substring(0, fxLength));
      firstName = ServiceUtil.parseWordPinyin(psnName.substring(fxLength), " ");
      if (!"".equals(firstName)) {
        firstName = StringUtils.substring(firstName, 0, 21);
      }
      nameMap.put(FIRSTNAME, firstName);
      nameMap.put(LASTNAME, lastName);
    }
    return nameMap;
  }

  @Override
  public Map<String, String> parseNameToPinyin(String psnName, int fxLength) {
    Map<String, String> nameMap = new HashMap<>();
    if (StringUtils.isNotBlank(psnName)) {
      String firstName = "";
      String lastName = "";
      psnName = psnName.trim();
      if (fxLength > 1) {// 为复姓
        lastName = parseLastNameToPinyin(psnName.substring(0, fxLength));
        firstName = ServiceUtil.parseWordPinyin(psnName.substring(fxLength), " ");
      } else {
        lastName = parseLastNameToPinyin(psnName.substring(0, 1));
        firstName = ServiceUtil.parseWordPinyin(psnName.substring(1), " ");
      }
      if (!"".equals(firstName)) {
        firstName = StringUtils.substring(firstName, 0, 21);
      }
      nameMap.put(FIRSTNAME, firstName);
      nameMap.put(LASTNAME, lastName);
    }
    return nameMap;
  }


  @Override
  public String parseLastNameToPinyin(String lastName) {
    if (StringUtils.isNotBlank(lastName)) {
      lastName = lastName.trim();
      String py = constPsnLastNamePyDao.findFirstPinyinByZhWord(lastName);
      if (py != null) {// 如果为系统常量表中的多音姓，直接返回多音姓
        return py;
      }
      return ServiceUtil.parseWordPinyin(lastName, " ");
    }
    return lastName;
  }

  /**
   * 结合Person表中的eName和使用拼音进行转换结合获取名称拼音，优先选择与PsnName匹配的拼音
   * 
   * @param psnId
   * @return
   */
  private Map<String, String> chooseRightPsnName(String zhname, String ename, int fxLength) {
    try {
      Set<String> allPinyin =
          ServiceUtil.parseWordPinyinAll(zhname.substring(fxLength) + zhname.substring(0, fxLength));// 将人名中的
      // 姓放在后面，名放在前面。进行输入
      if (StringUtils.isNotBlank(ename)) {
        for (String py : allPinyin) {
          if (py.replaceAll(" ", "").equalsIgnoreCase(ename.replaceAll(" ", ""))) {
            String[] split = py.split(" ");
            StringBuffer lastName = new StringBuffer();
            StringBuffer firstName = new StringBuffer();
            for (int i = split.length - fxLength; i < split.length; i++) {
              lastName.append(split[i]).append(" ");
            }
            for (int i = 0; i < split.length - fxLength; i++) {
              firstName.append(split[i]).append(" ");
            }
            Map<String, String> nameMap = new HashMap<>();
            nameMap.put(FIRSTNAME, firstName.toString().trim());
            nameMap.put(LASTNAME, lastName.toString().trim());
            return nameMap;
          }
        }
      }
      return parseNameToPinyin(zhname, fxLength);
    } catch (BadHanyuPinyinOutputFormatCombination e) {
      throw new ServiceException("中文名称转拼音出现错误,zhName=" + zhname);
    }

  }

  @Override
  public Map<String, Set<String>> generalPsnPmName(Person person) {
    if (person == null) {
      return null;
    }
    String zhname = XmlUtil.getCleanAuthorName(person.getName()).replaceAll("\\d+", "");
    if (StringUtils.isBlank(zhname) || !ServiceUtil.isChinese(zhname)) {
      return null;
    }
    String ename = person.getEname();
    Map<String, Set<String>> psnNameList = new HashMap<>();
    int fxLength = lastNameLength(zhname);
    Map<String, String> parsePinYin = chooseRightPsnName(zhname, ename, fxLength);
    String lastName = parsePinYin.get(LASTNAME).toLowerCase(); /* ma ;duan;ou yang */
    String firstName = parsePinYin.get(FIRSTNAME).toLowerCase(); /* jian;wen jie;xiang yuan */
    Set<String> fullname = new HashSet<>();
    Set<String> initname = new HashSet<>();
    Set<String> prefixname = new HashSet<>();
    Boolean isFx = fxLength > 1;
    if (isFx) {
      if (zhname.length() == 3) {
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
      } else if (zhname.length() == 2) {
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

      if (zhname.length() == 2) {
        fullname.add(lastName + " " + firstName);// ma jian
        fullname.add(firstName + " " + lastName);// jian ma

        initname.add(lastName + " " + firstName.substring(0, 1));// ma
                                                                 // j
        initname.add(firstName.substring(0, 1) + " " + lastName);// j
                                                                 // ma
        prefixname.add(lastName + " " + firstName.substring(0, 1)); // ma
                                                                    // j
      }
      if (zhname.length() == 3) {
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

    psnNameList.put(FULLNAME, fullname);
    psnNameList.put(INITNAME, initname);
    psnNameList.put(PREFIXNAME, prefixname);
    return psnNameList;
  }

  /**
   * 获取当前姓名姓氏的长度
   * 
   * @param psnName
   * @return
   */
  private int lastNameLength(String psnName) {
    if (psnName.length() > 2) {// 判断复姓
      List<ConstSurNameDTO> constSurNames = constSurNameDao.findAllSurName();
      for (ConstSurNameDTO constSurName : constSurNames) {
        if (psnName.startsWith(constSurName.getName())) {
          return constSurName.getName().length();
        }
      }
    }
    return 1;
  }

  @Override
  public Map<String, String> parseZhfirstAndLast(String psnName) {
    return null;
  }
}
