package com.smate.center.task.service.pdwh.quartz;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubAssignDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubAuthorInfoDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.model.pdwh.pub.PdwhPubAuthorInfo;
import com.smate.center.task.model.pdwh.quartz.PdwhPubXml;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.single.constants.ImportPubXmlConstants;
import com.smate.center.task.single.oldXml.pub.ImportPubXmlDocument;
import com.smate.core.base.utils.data.XmlUtil;

@Service("splitPubAuthorInfoService")
@Transactional(rollbackFor = {Exception.class})
public class SplitPubAuthorInfoServiceImpl implements SplitPubAuthorInfoService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private PdwhPubAuthorInfoDao pdwhPubAuthorInfoDao;
  @Autowired
  private PdwhPubAssignDao pdwhPubAssignDao;

  private static Integer jobType = TaskJobTypeConstants.SplitPubAuthorInfoTask;

  @Override
  public List<TmpTaskInfoRecord> batchGetJobList(Integer size) throws Exception {
    return tmpTaskInfoRecordDao.getTaskInfoRecordList(size, jobType);
  }

  @Override
  public void startSplitInfo(TmpTaskInfoRecord job) throws Exception {
    Long pubId = job.getHandleId();
    Long jobId = job.getJobId();
    PdwhPubXml pdwhPubXml = (PdwhPubXml) this.pdwhPubXmlDao.get(pubId);
    if (pdwhPubXml == null) {
      this.tmpTaskInfoRecordDao.updateTaskStatusById(job.getJobId(), 2, "获取到的xml为空！");
      return;
    }
    String XmlString = pdwhPubXml.getXml();
    if (StringUtils.isEmpty(XmlString)) {
      return;
    }
    try {
      ImportPubXmlDocument document = new ImportPubXmlDocument(XmlString);
      // 任务需求更改，直接取pub下的作者信息
      this.dealPubAuthor(document, pubId, jobId);

      /*
       * @SuppressWarnings("unchecked") List<Node> node =
       * document.getNodes(ImportPubXmlConstants.AUTHOR_XPATH); if (CollectionUtils.isNotEmpty(node)) { //
       * 优先获取author节点中的作者信息 dealXMLHasPubAuthors(document, pubId, jobId); } else { //
       * 处理不含author节点普通xml作者信息 dealNormalPubAuthor(document, pubId, jobId); }
       */
    } catch (Exception e) {
      logger.error("拆分保存成果xml节点出错！pubId:" + pubId, e);
      this.tmpTaskInfoRecordDao.updateTaskStatusById(job.getJobId(), 2,
          "拆分保存成果xml节点出错！" + getSubString(e.toString(), 450));
    }
  }

  /**
   * 直接从pub节点下面取
   * 
   * @param document
   * @param pubId
   * @param jobId
   * @throws Exception
   */
  public void dealPubAuthor(ImportPubXmlDocument document, Long pubId, Long jobId) throws Exception {
    StringBuffer sBuffer = new StringBuffer();
    String dbCode = document.getSourceDbCode();
    // 从publication中获取作者信息
    String authorNames = document.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names");
    String[] splitNames = null;
    if (StringUtils.isNotEmpty(authorNames)) {
      authorNames = authorNames.replace("；", ";");
      if (!authorNames.contains(";")) {
        sBuffer.append("author_names不含;分隔符不拆分");
        authorNames = null;
      } else {
        splitNames = authorNames.split(";");
      }

    }

    String authorNamesSpecStr =
        document.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec");
    String[] splitNamesSpec = null;
    if (StringUtils.isNotEmpty(authorNamesSpecStr)) {
      authorNamesSpecStr = authorNamesSpecStr.replace("；", ";").replace("，", ",");
      // cnki 的authors_names_spec 以，为分隔符
      if (dbCode.equalsIgnoreCase("ChinaJournal")) {
        if (!authorNamesSpecStr.contains(",")) {
          sBuffer.append("authors_names_spec不含,分隔符不拆分");
          authorNamesSpecStr = null;
        } else {
          splitNamesSpec = authorNamesSpecStr.split(",");
        }

      } else {
        if (!authorNamesSpecStr.contains(";")) {
          sBuffer.append("authors_names_spec不含;分隔符不拆分");
          authorNamesSpecStr = null;
        } else {
          splitNamesSpec = authorNamesSpecStr.split(";");
        }

      }

    }
    // 如果作者信息为空，不处理
    if ((StringUtils.isEmpty(authorNames)) && (StringUtils.isEmpty(authorNamesSpecStr))) {
      this.tmpTaskInfoRecordDao.updateTaskStatusById(jobId, 3, "XML中author_names和authors_names_spec节点值都为空或者不含分隔符");
      return;
    }

    String organization = "";
    // 单位信息
    organization = document.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "organization");
    List<String> splitOrg = null;
    if (StringUtils.isNotEmpty(organization)) {
      splitOrg = this.splitOrg(organization, dbCode);

    } else {
      this.tmpTaskInfoRecordDao.updateTaskStatusById(jobId, 3, "XML中单位信息为空！");
      return;
    }

    // 依据authorName
    if ((StringUtils.isNotBlank(authorNames))) {
      for (int i = 0; i < splitNames.length; i++) {
        for (int j = 0; j < splitOrg.size(); j++) {
          String org = getSubString(splitOrg.get(j), 200);
          this.saveAuthorInfo(pubId, splitNames[i], org);
        }

      }
    }
    // 依据authorNameSpec
    if ((StringUtils.isNotBlank(authorNamesSpecStr))) {
      for (int i = 0; i < splitNamesSpec.length; i++) {
        for (int j = 0; j < splitOrg.size(); j++) {
          String org = getSubString(splitOrg.get(j), 200);
          this.saveAuthorInfo(pubId, splitNamesSpec[i], org);
        }

      }
    }

    this.tmpTaskInfoRecordDao.updateTaskStatusById(jobId, 1, sBuffer == null ? "" : sBuffer.toString());
  }

  public void saveAuthorInfo(Long pubId, String name, String org) {
    // 查询该成果已经指派的机构的Id
    List<Long> pubAssignId = pdwhPubAssignDao.getPubAssignId(pubId);
    if (CollectionUtils.isNotEmpty(pubAssignId)) {
      for (Long insId : pubAssignId) {
        this.pdwhPubAuthorInfoDao.saveAuthorInfo(pubId, insId, name, cleanName(name), org, cleanOrg(org));
      }
    } else {
      this.pdwhPubAuthorInfoDao.saveAuthorInfo(pubId, null, name, cleanName(name), org, cleanOrg(org));
    }

  }

  /*
   * public static void main(String[] args) { String string =
   * "惹人n,./?;:'\"\\|%& $#@jdf edfkdjfd  AFG()（）4448，、·· ； "; System.out.println(cleanOrg(string)); }
   */

  /**
   * 简单去除人名常见的符号空格
   * 
   * @param name
   * @return
   */
  public static String cleanName(String name) {
    name = name.replace("（", "").replace("）", "").replace("，", "").replace("；", "").replace("、", "").replace("·", "");
    name = name.replace("-", "").replace("(", "").replace(")", "").replace(" ", "").trim().toLowerCase();
    return name;

  }

  /**
   * 去除单位信息常见的中英文标点符号空格
   * 
   * @param name
   * @return
   */
  public static String cleanOrg(String org) {
    org = org.replace("（", "").replace("）", "").replace("，", "").replace("；", "").replace("、", "").replace("·", "");
    org = org.replaceAll("[\\p{Punct}\\p{Space}]+", "").toLowerCase();
    return org;

  }

  /**
   * 清理单位
   * 
   * @param keywords
   * @return
   */
  public List<String> splitOrg(String organization, String dbCode) {
    organization = organization.replace("；", ";");
    String[] splitOrg;
    if (dbCode.equalsIgnoreCase("sci") || dbCode.equalsIgnoreCase("istp") || dbCode.equalsIgnoreCase("ssci")) {
      splitOrg = organization.split(".");
    } else {
      splitOrg = organization.split(";");
    }

    List<String> list = new ArrayList<>();
    List<String> newList = new ArrayList<String>();
    for (String str : splitOrg) {
      if (dbCode.equalsIgnoreCase("sci") || dbCode.equalsIgnoreCase("istp") || dbCode.equalsIgnoreCase("ssci")) {
        if (str.contains("[") && str.contains("]")) {
          str = str.replace(str.substring(str.indexOf("["), str.indexOf("]") + 1), "");
        }
      }
      if (StringUtils.isNotBlank(str)) {
        // 查找是否有重复
        if (newList.contains(str.toLowerCase().trim().replace(" ", ""))) {
        } else {
          list.add(str);
        }
        newList.add(str.toLowerCase().trim().replace(" ", ""));
      }
    }
    return list;

  }

  /**
   * 
   * 
   * @param document
   * @param pubId
   * @throws Exception
   */
  public void dealNormalPubAuthor(ImportPubXmlDocument document, Long pubId, Long jobId) throws Exception {

    // 从publication中获取作者信息
    String authorNames = document.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names");
    String[] splitNames = null;
    if (StringUtils.isNotEmpty(authorNames)) {
      splitNames = authorNames.split(";");
    }

    // 作者中文名信息 主要是isi的有
    String authorNamesSpecStr =
        document.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec");
    String[] splitNamesSpec = null;
    if (StringUtils.isNotEmpty(authorNamesSpecStr)) {
      splitNamesSpec = authorNamesSpecStr.split(";");
    }
    // 如果作者信息为空，不处理
    if ((StringUtils.isEmpty(authorNames)) && (StringUtils.isEmpty(authorNamesSpecStr))) {
      this.tmpTaskInfoRecordDao.updateTaskStatusById(jobId, 2, "XML中author_names和authors_names_spec节点值都为空！");
      return;
    }
    if (StringUtils.isNotEmpty(authorNamesSpecStr)) {
      splitNamesSpec = authorNamesSpecStr.split(";");
    }

    String organization = "";
    // 单位信息（由于数据格式问题，publication下的单位暂时没有与个人相匹配）
    organization = document.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "organization");
    String[] splitIndex = splitNames != null ? splitNames : splitNamesSpec;
    for (int i = 0; i < splitIndex.length; i++) {
      String email = "";
      String authorName = "";
      String authorNamespec = "";

      // 如果authorNamespec为空 ，authorName为中文则移动过去
      authorName = splitNames == null ? null : splitNames[i];
      if (splitNamesSpec == null) {
        if (XmlUtil.containZhChar(splitNames[i])) {
          authorNamespec = authorName;
          authorName = "";// 是中文则移动到中文，英文置空
        }
      }
      this.pdwhPubAuthorInfoDao.saveAuthorsInfo(pubId, getSubString(authorName, 100), getSubString(authorNamespec, 100),
          getSubString(organization, 200), getSubString(email, 50));
    }
    this.tmpTaskInfoRecordDao.updateTaskStatusById(jobId, 1, "");

  }

  /**
   * 
   * 
   * @param node
   * @param document
   * @param pubId
   * @throws Exception
   */
  public void dealXMLHasPubAuthors(ImportPubXmlDocument document, Long pubId, Long jobId) throws Exception {

    @SuppressWarnings("unchecked")
    List<Node> node = document.getPubAuthorList();
    // 用于含中文名的信息匹配
    String authorNamesSpec =
        document.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec");
    String[] splitNamesSpec = null;
    if (StringUtils.isNotEmpty(authorNamesSpec)) {
      splitNamesSpec = authorNamesSpec.split(";");
    }

    for (int i = 0; i < node.size(); i++) {
      String email = "";
      String authorName = "";
      String authorNamespec = "";
      String organization = "";
      if (StringUtils.isNotEmpty(document.getXmlNodeAttributeValue(node.get(i), "au"))) {
        email = document.getXmlNodeAttributeValue(node.get(i), "email");
        organization = document.getXmlNodeAttributeValue(node.get(i), "dept");
        // 如果获取不到单位信息则从publication路径下获取（由于数据格式问题，暂时没有与个人相匹配）
        if (StringUtils.isEmpty(organization)) {
          organization = document.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "organization");
        }
        authorName = document.getXmlNodeAttributeValue(node.get(i), "au");
        if (StringUtils.isNotEmpty(authorNamesSpec)) {
          if (i < splitNamesSpec.length) {
            authorNamespec = splitNamesSpec[i];
            // 根据顺序获取对应的中文名字，需要进行近似匹配（待处理）
            /*
             * String tmpZhName = splitNamesSpec[i]; if (isFullChinese(tmpZhName.replace("·", "").replace("-",
             * "").trim())) {
             * 
             * Map<String, String> pinYin = ServiceUtil.parsePinYin(tmpZhName); String tname =
             * pinYin.get("lastName") + pinYin.get("firstName");
             * 
             * }
             */

          } else {
            authorNamespec = "";
          }
        }
        // 如果authorNamespec为空 ，authorName为中文则移动过去
        if (splitNamesSpec == null) {
          if (XmlUtil.containZhChar(authorName)) {
            authorNamespec = authorName;
            authorName = "";// 是中文则移动到中文，英文置空
          }
        }
        this.pdwhPubAuthorInfoDao.saveAuthorsInfo(pubId, getSubString(authorName.trim(), 100),
            getSubString(authorNamespec, 100), getSubString(organization, 200), getSubString(email.trim(), 50));

      } else {
        try {
          matchEmailWithName(document.getXmlNodeAttributeValue(node.get(i), "email"), pubId);
        } catch (Exception e) {
          logger.error("匹配作者邮件地址出错！", e);
          tmpTaskInfoRecordDao.updateTaskStatusById(jobId, 2, "匹配作者邮件地址出错！");
        }
      }
    }

    this.tmpTaskInfoRecordDao.updateTaskStatusById(pubId, 1, "");

  }

  /**
   * 通过author中email和成果作者匹配
   * 
   * @param email
   * @param pubId
   */
  public void matchEmailWithName(String email, Long pubId) throws Exception {
    int count = 0;
    String matchedName = "";
    String matchedeamil = "";

    String[] str = email.split(" ");
    // 获取成果的作者信息
    List<PdwhPubAuthorInfo> pubAuthorNames = this.pdwhPubAuthorInfoDao.getPubAuthorNames(pubId);
    if (CollectionUtils.isEmpty(pubAuthorNames)) {
      return;
    }
    for (String emstring : str) {
      if (!StringUtils.isEmpty(emstring)) {
        // 取email地址@前字符并去除无关字符
        String emailstr =
            emstring.split("@")[0].toLowerCase().trim().replace(".", "").replace("-", "").replaceAll("\\d+", "");
        // email清理后不含英文字符跳过处理
        if (StringUtils.isEmpty(emailstr)) {
          continue;
        }
        for (PdwhPubAuthorInfo pdwhPubAuthorInfo : pubAuthorNames) {
          // 暂时只和作者英文名做匹配处理，英文为空跳过
          if (StringUtils.isEmpty(pdwhPubAuthorInfo.getAuthorName())) {
            continue;
          }
          int subcount = cacuSubStrCount(emailstr, getSimpleCleanAuthorName(pdwhPubAuthorInfo.getAuthorName()));
          if (subcount > count) {
            count = subcount;
            matchedeamil = emstring;
            matchedName = pdwhPubAuthorInfo.getAuthorName();
          }
        }
        // 去除email地址后 ；符号
        if (";".equals(matchedeamil.substring(matchedeamil.length() - 1, matchedeamil.length()))) {
          matchedeamil = matchedeamil.substring(0, matchedeamil.length() - 1);
        }
        this.pdwhPubAuthorInfoDao.updateAuthorEmail(pubId, matchedName, matchedeamil.trim());
        count = 0;
      }
    }
  }

  @Override
  public void updateTaskStatus(Long jobId, int status, String err) {
    try {
      tmpTaskInfoRecordDao.updateTaskStatusById(jobId, status, err);
    } catch (Exception e) {
      logger.error("更新任务状态记录出错！jobId " + jobId, e);
    }
  }

  /**
   * 清理作者名，去除特殊字符空格并转为小写
   * 
   * @param name
   * @return
   */
  public String getSimpleCleanAuthorName(String name) {
    name = name.replace(",", " ").replace(";", " ").replaceAll("\\s*\\.\\s*", ".").replaceAll("\\s*\\-\\s*", "-")
        .replaceAll("\\s+", " ").replace(" ", "").trim().toLowerCase();
    return name;
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
   * 统计email@前字符串的子字符串在namestr中出现的次数，按两个字符拆分
   * 
   * @param emailstr
   * @param namestr
   * @return
   */
  public int cacuSubStrCount(String emailstr, String namestr) {
    int count = 0;
    for (int i = 0; i < emailstr.length(); i++) {
      int j = i + 2;
      String substring = emailstr.substring(i, j > emailstr.length() ? (j = emailstr.length()) : j);
      if (namestr.contains(substring)) {
        count++;
      }
    }
    return count;
  }

  public String getSubString(String str, int endIndex) throws UnsupportedEncodingException {
    if ((StringUtils.isNotEmpty(str)) && (str.length() > endIndex)) {
      str = str.substring(0, endIndex);
    }
    return str;
  }

}
