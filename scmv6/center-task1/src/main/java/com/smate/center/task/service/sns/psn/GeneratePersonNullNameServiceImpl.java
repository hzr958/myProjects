package com.smate.center.task.service.sns.psn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.dao.sns.quartz.NameSplitDao;
import com.smate.center.task.dao.sns.quartz.PersonFLNameDao;
import com.smate.center.task.model.sns.pub.PersonFLName;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;

@Service("generatePersonNullNameService")
@Transactional(rollbackFor = Exception.class)
public class GeneratePersonNullNameServiceImpl implements GeneratePersonNullNameService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private NameSplitDao nameSplitDao;
  @Autowired
  private PersonFLNameDao personFLNameDao;
  private static Integer jobType = TaskJobTypeConstants.GeneratePersonNullNameTask;

  @Override
  public void updateTaskStatus(Long psnId, int status, String err) {
    try {
      tmpTaskInfoRecordDao.updateTaskStatus(psnId, status, err, jobType);
    } catch (Exception e) {
      logger.error("更新任务状态记录出错！handleId,status,jobtype=" + psnId + ",status" + ",jobtype", e);
    }
  }

  @Override
  public List<Long> getNeedTohandleList(int batchSize) throws Exception {
    return tmpTaskInfoRecordDao.getbatchhandleIdList(batchSize, jobType);
  }

  @Override
  public void startGeneratePsnNames(Long psnId) {
    Person person = personDao.get(psnId);
    // 处理person表部分数据为“null”情况
    String name = this.dealNull(person.getName());
    String firstName = this.dealNull(person.getFirstName());
    String lastName = this.dealNull(person.getLastName());
    String ename = this.dealNull(person.getEname());

    // 赋值到PersonFLName
    PersonFLName nameSplit = new PersonFLName();
    nameSplit.setPsnId(psnId);
    nameSplit.setNameZh(name);
    nameSplit.setNameEn(ename);
    nameSplit.setFirstNameEN(firstName);
    nameSplit.setLastNameEN(lastName);

    if (StringUtils.isEmpty(name)) {
      this.updateTaskStatus(psnId, 3, "name为空，不处理");
      return;
    }

    if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
      // person表 firstName&firstName有数据则不处理
      if (StringUtils.isNotBlank(ename)) {
        this.updateTaskStatus(psnId, 3, "person表 firstName&lastName有数据，且ename有数据不处理");
      } else {
        nameSplit.setNameEn(lastName + " " + firstName);
        personFLNameDao.save(nameSplit);
        this.updateTaskStatus(psnId, 1, " firstName&lastName有数据，生成了ename");
      }

      return;
    }
    // 目前暂时只处理了人名中含有 ，, .·符号的拆分处理
    String cleanName = name.replace(",", "").replace(".", "").replace(";", "").replace("，", "").replace("·", "");// 简易清理name信息

    if (this.HasDigit(cleanName)) {
      this.updateTaskStatus(psnId, 3, "name包含数字暂不处理，name:" + name);
      return;
    }

    // 纯中文（包含符号）处理,拆分firstname&lastname
    if (isFullChinese(cleanName)) {
      try {
        this.dealFullZhName(psnId, nameSplit);
      } catch (Exception e) {
        this.updateTaskStatus(psnId, 2, "纯中文（包含符号）处理出错，name:" + name);
      }
      return;
    }
    // 纯英文(包含符号)处理,拆分firstname&lastname
    if (isFullEnglish(cleanName)) {
      try {
        this.dealFullEnName(psnId, nameSplit);
      } catch (Exception e) {
        this.updateTaskStatus(psnId, 2, "纯英文（包含符号）处理出错，name:" + name);
      }

      return;
    }
    // 中英文符号混杂处理
    this.updateTaskStatus(psnId, 3, "name中、英、符号混杂暂不处理，name:" + name);

  }

  /**
   * 全英文人名处理
   * 
   * @param psnId
   * @param nameSplit
   */
  public void dealFullEnName(Long psnId, PersonFLName nameSplit) throws Exception {
    String fname = "";
    String lname = "";
    String firstName = nameSplit.getFirstNameEN();
    String lastName = nameSplit.getLastNameEN();
    String ename = nameSplit.getNameEn();
    String name = nameSplit.getNameZh();
    try {
      String sourceflag = "name";// 标记拆分属性，用于日志记录

      Map<String, String> splitName = this.splitEnName(psnId, name, sourceflag);
      if (splitName == null) {
        return;
      }
      lname = splitName.get("lname");
      fname = splitName.get("fname");

    } catch (Exception e) {
      logger.error("name纯英文处理,拆分firstname&lastname出错！，name:" + name);
      this.updateTaskStatus(psnId, 2, "name纯英文处理,拆分firstname&lastname出错！，name:" + name);
      return;
    }

    StringBuffer sbBuffer = new StringBuffer();
    sbBuffer.append("英文人名处理:生成了");
    if (StringUtils.isBlank(firstName)) {
      nameSplit.setFirstNameEN(fname);
      sbBuffer.append("firstName，");
    }
    if (StringUtils.isBlank(lastName)) {
      nameSplit.setLastNameEN(lname);
      sbBuffer.append("lastName，");
    }
    if (StringUtils.isBlank(ename)) {// ename为空则赋值为：name;
      nameSplit.setNameEn(name);
      sbBuffer.append("ename");
    }
    personFLNameDao.save(nameSplit);
    this.updateTaskStatus(psnId, 1, sbBuffer.toString());
  }

  /**
   * 全中文人名拆分处理
   * 
   * @param psnId
   * @param nameSplit
   */
  public void dealFullZhName(Long psnId, PersonFLName nameSplit) throws Exception {
    String fname = "";
    String lname = "";
    String firstName = nameSplit.getFirstNameEN();
    String lastName = nameSplit.getLastNameEN();
    String ename = nameSplit.getNameEn();
    String name = nameSplit.getNameZh();
    StringBuffer sbBuffer = new StringBuffer();
    sbBuffer.append("中文人名处理:生成了");
    // 纯中文名处理
    if (StringUtils.isNotBlank(ename)) {// ename不为空则用ename拆分
      if (isFullChinese(ename)) {
        // 如果ename为中文则改用name转换
        if (name.contains("·")) {
          this.updateTaskStatus(psnId, 3, "name为全中文，包含符号 暂不处理，name:" + name);
          return;
        }
        if (name.contains("，") || name.contains(",")) {
          this.updateTaskStatus(psnId, 3, "name为全中文，包含符号 暂不处理，name:" + name);
          return;
        }
        if (name.contains("公司") && name.length() > 4) {
          this.updateTaskStatus(psnId, 3, "该中文名不规范，暂不处理，name:" + name);
          return;
        }
        Map<String, String> parsePinYin = ServiceUtil.parsePinYin(name);
        fname = this.formatName(parsePinYin.get("firstName"));
        lname = this.formatName(parsePinYin.get("lastName"));

      } else {
        // 否则用ename拆分（ename全英文）
        try {
          String sourceflag = "ename";// 标记拆分属性，用于日志记录
          Map<String, String> splitEnName = this.splitEnName(psnId, ename, sourceflag);
          if (splitEnName == null) {
            return;
          }
          lname = splitEnName.get("lname");
          fname = splitEnName.get("fname");
        } catch (Exception e) {
          this.updateTaskStatus(psnId, 2, "ename拆分出错！");
          return;
        }
      }
      // 保存处理（有值则不处理）
      if (StringUtils.isBlank(firstName)) {
        sbBuffer.append("firstName,");
        nameSplit.setFirstNameEN(fname);
      }
      if (StringUtils.isBlank(lastName)) {
        sbBuffer.append("lastName,");
        nameSplit.setLastNameEN(lname);
      }

    } else {// ename为空则用name拆分
      if (name.contains("·")) {
        this.updateTaskStatus(psnId, 3, "name为全中文，包含符号 暂不处理，name:" + name);
        return;
      }
      if (name.contains("，") || name.contains(",")) {
        this.updateTaskStatus(psnId, 3, "name为全中文，包含符号 暂不处理，name:" + name);
        return;
      }

      if (name.contains("公司") && name.length() > 4) {
        this.updateTaskStatus(psnId, 3, "该中文名不规范，暂不处理，name:" + name);
        return;
      }
      Map<String, String> parsePinYin = ServiceUtil.parsePinYin(name);
      fname = this.formatName(parsePinYin.get("firstName"));
      lname = this.formatName(parsePinYin.get("lastName"));
      if (StringUtils.isBlank(firstName)) {
        sbBuffer.append("firstName,");
        nameSplit.setFirstNameEN(fname);// 获取中文名转换的英文firstName
      }
      if (StringUtils.isBlank(lastName)) {
        sbBuffer.append("lastName,");
        nameSplit.setLastNameEN(lname);// 获取中文名转换的英文lastName
      }
    }

    if (StringUtils.isBlank(ename)) {// ename为空则赋值为：firstname+" "+lastname;;
      if (containEnChar(fname + " " + lname)) {
        sbBuffer.append("ename");
        nameSplit.setNameEn(fname + " " + lname);
      }
    }
    personFLNameDao.save(nameSplit);
    this.updateTaskStatus(psnId, 1, sbBuffer.toString());

  }

  /**
   * 进一步格式处理中文转换英文lastname&firstname
   * 
   * @param lastname
   * @return
   */
  public String formatName(String firstname) {
    if (firstname.contains(" ")) {
      String tmp = firstname.replace(" ", "").toLowerCase();
      firstname = tmp.substring(0, 1).toUpperCase() + tmp.substring(1);
    }

    return firstname;
  }

  /**
   * 拆分英文firstname&lastname方法
   * 
   * @param psnId
   * @param name
   * @param sourceflag
   * @return
   */
  private Map<String, String> splitEnName(Long psnId, String name, String sourceflag) throws Exception {
    String lname = "";
    String fname = "";
    Map<String, String> map = new HashMap<String, String>();

    if ((name.contains(",") || name.contains("，")) && !name.contains(".")) {// 只有,或者，没有.的人名处理
      String splitIndex = ",";
      if (name.contains("，")) {
        splitIndex = "，";
      }
      // 处理Duan,Cunming类似只有一个","的命名
      String[] split = name.split(splitIndex);
      if (split.length <= 2) {
        lname = split[0];
        fname = name.replace(lname, "").replace(splitIndex, "");
      }
      // 处理Damien, Daniel, Roland HINSINGER 含有两个,的情况
      else {
        fname = name.substring(0, name.lastIndexOf(splitIndex) + 1);// 以最后一个“，”或者","拆分
        // ；“，”或者","前面为firstname；
        lname = name.replace(fname, "");// 获取lastname
      }
    }

    else if (!name.contains("，") && !name.contains(",") && name.contains(".")
        && name.lastIndexOf(".") != name.length() - 1) {// 只包含.不含，
      fname = name.substring(0, name.lastIndexOf(".") + 1);// 以最后一个“.”拆分，","前面为firstname；
      lname = name.replace(fname, "");// 获取lastname
    }

    else if ((name.contains(",") || name.contains("，")) && name.contains(".")) {// 有，和.的情况
      String splitIndex = ",";
      if (name.contains("，")) {
        splitIndex = "，";
      }
      if (name.indexOf(".") < name.indexOf(splitIndex)) {// CarlK.Edwards,III和B.F.Spencer,Jr这种.在，前的暂不处理
        this.updateTaskStatus(psnId, 3, sourceflag + "为全英文但不符合规范 暂不处理，name:" + name);
        return null;
      }
      // 处理 Sui, D. Z这样的情况
      String[] split = name.split(",");
      lname = split[0];
      fname = name.replace(lname, "").replace(",", "");// 获取firstname

    } // PEDRO S·C·F·ROCHA 处理只含有·符号的英文人名
    else if (name.contains("·") && !name.contains("，") && !name.contains(",") && !name.contains(".")) {
      fname = name.substring(0, name.lastIndexOf("·") + 1);// 以最后一个“·”拆分前面为firstname；
      lname = name.replace(fname, "");// 获取lastname
    } else {// 不含符号全英文处理
      if (!name.contains(" ")) {// 全英文不包含空格且不含. ,等符号暂不拆分
        this.updateTaskStatus(psnId, 3, sourceflag + "为全英文不包含空格且不含. ,等符号暂不处理，name:" + name);
        return null;
      }
      // 如果人名中间含有 de De, eg: Claudio De Felice ;Richard de
      // Dear,则de(De)以及后面都为lastname
      if (name.contains(" de ") || name.contains(" De ")) {
        if (name.contains(" de ")) {
          lname = name.substring(name.indexOf("de"), name.length());
          fname = name.replace(lname, "");
        } else if (name.contains(" De ")) {
          lname = name.substring(name.indexOf("De"), name.length());
          fname = name.replace(lname, "");
        }
      } else {
        String[] split2 = name.split(" ");
        int length = split2.length;
        fname = split2[0];
        lname = name.replace(fname, "");// 获取lastname
        if (length <= 1) {
          this.updateTaskStatus(psnId, 3, sourceflag + "为全英文但不符合规范 暂不处理，name:" + name);
          return null;
        }
      }
    }
    map.put("fname", fname == null ? fname : fname.trim());
    map.put("lname", lname == null ? lname : lname.trim());
    return map;

  }

  /**
   * 判断字符串是否有中文
   * 
   * @param name
   * @return
   */
  public static boolean containZhChar(String name) {
    if (StringUtils.isNotBlank(name)) {
      Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
      Matcher matcher = p.matcher(name);
      if (matcher.find()) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断字符串是否有英文
   * 
   * @param name
   * @return
   */
  public static boolean containEnChar(String name) {
    if (StringUtils.isNotBlank(name)) {
      Pattern p = Pattern.compile("[A-Za-z]");
      Matcher matcher = p.matcher(name);
      if (matcher.find()) {
        return true;
      }
    }
    return false;
  }

  /**
   * 是否全中文
   * 
   * @param str
   * @return
   */
  private boolean isFullChinese(String str) {
    if (str == null) {
      return false;
    }
    Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
    return pattern.matcher(str.trim().replace(" ", "")).matches();
  }

  /**
   * 是否全英文
   * 
   * @param str
   * @return
   */
  private boolean isFullEnglish(String str) {
    if (str == null) {
      return false;
    }
    Pattern pattern = Pattern.compile("[a-zA-Z]+");
    return pattern.matcher(str.trim().replace(" ", "")).matches();
  }

  /**
   * 判断是否包含数字
   * 
   * @param content
   * @return
   */
  public boolean HasDigit(String content) {
    boolean flag = false;
    Pattern p = Pattern.compile(".*\\d+.*");
    Matcher m = p.matcher(content);
    if (m.matches()) {
      flag = true;
    }
    return flag;
  }

  /**
   * 处理值中含有"null"的情况,置为空处理
   * 
   * @param str
   * @return
   */
  public String dealNull(String str) {
    if (StringUtils.isNotBlank(str)) {
      if (str.contains("null")) {
        str = "";
      } else if (" ".equals(str)) {
        str = "";
      }

    } else {
      return "";
    }

    return str;

  }

  // test
  public static void main(String[] args) {
    Pattern pattern = Pattern.compile("[a-zA-Z]+");
    String string = "dasfdz., ";
    System.out.println("全英文" + pattern.matcher(string.trim().replaceAll(" ", "")).matches());

    Pattern pattern1 = Pattern.compile("[\\u4E00-\\u9FBF]+");
    String str = "子.";
    System.out.println("全中文" + pattern1.matcher(str.trim().replaceAll(" ", "")).matches());

    System.err.println("Wan,Hong".split(",")[0]);

    System.err.println("win,fgh,dj".indexOf(","));
    String name = "Duan，Cunming";
    System.err.println((name.contains(",") || name.contains("，")) && !name.contains("."));

    System.err.println("Richard de Dear".substring("Richard de Dear".indexOf("de"), "Richard de Dear".length()));

    name = "PEDRO S·C·F·ROC，H,A.";
    if (name.contains("·") && !name.contains("，") && !name.contains(",") && !name.contains(".")) {
      System.err.println("66666");
    }

  }

}
