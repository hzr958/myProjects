package com.smate.center.task.service.sns.quartz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.dao.sns.quartz.CategoryMapScmNsfcDao;
import com.smate.center.task.dao.sns.quartz.KeywordNsfc201711Dao;
import com.smate.center.task.dao.sns.quartz.VKeywordsDicDao;
import com.smate.center.task.dao.sns.quartz.VKeywordsSynonymDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.exception.TaskDupRecordException;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.model.sns.quartz.KeywordNsfc201711;
import com.smate.center.task.model.sns.quartz.VKeywordsDic;
import com.smate.center.task.model.sns.quartz.VKeywordsSynonym;
import com.smate.center.task.utils.TmpTaskUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;

@Service("generateIKAnalyerDictService")
@Transactional(rollbackFor = Exception.class)
public class GenerateIKAnalyerDictServiceImpl implements GenerateIKAnalyerDictService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${self.dicpath}")
  private String dicRootPath;
  private static String dicEnName = "mydicten";
  private static String dicZhName = "mydictzh";
  private static String dicMixName = "mydictmix";
  @Autowired
  private KeywordNsfc201711Dao keywordNsfc201711Dao;
  @Autowired
  private VKeywordsDicDao vKeywordsDicDao;
  @Autowired
  private CategoryMapScmNsfcDao categoryMapScmNsfcDao;
  @Autowired
  private VKeywordsSynonymDao vKeywordsSynonymDao;
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  private static Integer jobType = TaskJobTypeConstants.GenerateIKAnalyerDictTask;

  @Override
  public List<TmpTaskInfoRecord> batchGetJobList(Integer size) throws Exception {
    return tmpTaskInfoRecordDao.getTaskInfoRecordList(size, jobType);
  }

  @Override
  public void startJob(TmpTaskInfoRecord job) throws Exception {
    Long jobId = job.getJobId();
    KeywordNsfc201711 keywordNsfc = keywordNsfc201711Dao.get(job.getHandleId());
    String keywordsEn = keywordNsfc.getKeywordsEn();
    String keywordsZh = keywordNsfc.getKeywordsZh();
    Long applicationId = keywordNsfc.getApplicationId();
    String applicationCode1 = keywordNsfc.getApplicationCode1();
    String applicationCode2 = keywordNsfc.getApplicationCode2();
    Integer year = keywordNsfc.getYear();
    int zhCount = 0;
    List<String> splitZh = null;

    // 如果有，或,和、没有；或;则不处理
    if (containIllegalChars(keywordsZh) || containIllegalChars(keywordsEn)) {
      this.updateTaskStatus(jobId, 3, "英文或中文关键词中只含有，,和、字符暂不拆分");// 更新任务状态
      return;
    }

    /**
     * 1.处理只有；或者;为分隔符的关键词，按；;拆分
     * <p>
     * 2.处理含有；或者;但是还含有其他、，,符号的关键词 ，按；;拆分
     * <p>
     * 3.处理只包含，,符号的关键词，按，,拆分
     * <p>
     * 4.处理只包含、符号的关键词，按、拆分
     * <p>
     * 
     * 5.不含拆分符号的关键词暂时不会被处理
     */

    // 中文关键词不为空时
    if (StringUtils.isNotBlank(keywordsZh)) {
      if (!checkNeedSplit(keywordsZh)) {
        this.updateTaskStatus(jobId, 3, "关键词中不含分隔字符暂不拆分");// 更新任务状态
        return;
      }
      splitZh = cleankw(keywordsZh);
      zhCount = splitZh.size();
    }
    int enCount = 0;
    List<String> splitEn = null;
    // 英文文关键词不为空时
    if (StringUtils.isNotBlank(keywordsEn)) {
      if (!checkNeedSplit(keywordsEn)) {
        this.updateTaskStatus(jobId, 3, "关键词中不含分隔字符暂不拆分");// 更新任务状态
        return;
      }
      splitEn = cleankw(keywordsEn);
      enCount = splitEn.size();
    }
    try {
      if (zhCount == 0 && enCount == 0) {
        this.updateTaskStatus(jobId, 3, "中英文关键词数据错误！数据分别为：" + keywordsZh + " ， " + keywordsEn);// 更新任务状态
        return;
      }
      this.saveKeyWords(jobId, applicationId, applicationCode1, applicationCode2, year, zhCount, enCount, splitZh,
          splitEn);
    } catch (Exception e) {
      this.updateTaskStatus(jobId, 2, "拆分处理过程发生错误！" + e.getMessage());// 更新任务状态
    }

  }

  /**
   * 替换字符统一拆分
   * 
   * @param str
   * @return
   */
  public String replaceChars(String str) {
    String reString = str;
    // 只有，或者,
    if (!str.contains(";") && !str.contains("；") && !str.contains("、")) {
      if (str.contains(",") || str.contains("，")) {
        reString = str.replace(",", ";").replace("，", ";");
      }

    }

    // 只有、
    if (!str.contains(";") && !str.contains("；") && !str.contains("，") && !str.contains(",")) {
      if (str.contains("、")) {
        reString = str.replace("、", ";");
      }

    }

    return reString;
  }

  /**
   * 是否要拆分 什么拆分符号都没有
   * 
   * @param str
   */
  public boolean checkNeedSplit(String str) {
    if (!str.contains(";") && !str.contains("；") && !str.contains(",") && !str.contains("，") && !str.contains("、")) {
      return false;
    }
    return true;

  }

  /**
   * 清理关键词并去重复
   * 
   * @param keywords
   * @return
   */
  public List<String> cleankw(String keywords) {
    // 替换，、拆分符
    keywords = replaceChars(keywords);

    String cleankeywords = keywords.replace("；", ";");
    String[] split = cleankeywords.split(";");

    List<String> list = new ArrayList<>();
    List<String> newList = new ArrayList<String>();
    for (String str : split) {
      if (StringUtils.isNoneBlank(str)) {
        // 查找是否有重复
        if (newList.contains(str.toLowerCase().trim().replace(" ", ""))) {
          // logger.info("含有重复关键词：" + keywords);
        } else {
          list.add(str);
        }
        newList.add(str.toLowerCase().trim().replace(" ", ""));
      }
    }
    return list;

  }

  /**
   * 判断是否包含不处理的字符
   * 
   * @param str
   * @return
   */
  public boolean containIllegalChars(String str) {
    if (StringUtils.isNotBlank(str)) {
      if ((str.contains(",") || str.contains("，")) && str.contains("、")) {
        if (str.contains(";") || str.contains("；")) {
          return false;
        }
        return true;
      }
    }
    return false;
  }

  /**
   * 保存处理
   * 
   * @param jobId
   * @param applicationId
   * @param applicationCode1
   * @param applicationCode2
   * @param year
   * @param zhCount
   * @param enCount
   * @param splitZh
   * @param splitEn
   */
  public void saveKeyWords(Long jobId, Long applicationId, String applicationCode1, String applicationCode2,
      Integer year, int zhCount, int enCount, List<String> splitZh, List<String> splitEn)
      throws Exception, ServiceException {
    StringBuffer sBuffer = new StringBuffer();

    // 中文关键词有值，英文没有值
    if (zhCount > 0 && enCount == 0) {
      for (int i = 0; i < splitZh.size(); i++) {
        // 保存数据到keywordsdic
        VKeywordsDic vkwd = null;
        boolean isDup = false;
        try {
          vkwd = saveToVKeywordsDic(applicationId, applicationCode1, applicationCode2, year, splitZh.get(i));
        } catch (TaskDupRecordException e) {
          sBuffer.append(splitZh.get(i) + ",");
          isDup = true;
        }
        // 是中文就保存到中文，是英文就保存到英文
        if (!isDup) {
          if (TmpTaskUtils.containZhChar(vkwd.getKeyword())) {
            this.saveToVKeywordsSynonym(vkwd.getId(), vkwd.getKeyword(), vkwd.getKwtxt(), null, null, null);
          } else {
            this.saveToVKeywordsSynonym(null, null, null, vkwd.getId(), vkwd.getKeyword(), vkwd.getKwtxt());
          }

        }
      }
      this.updateTaskStatus(jobId, 1, "只有中文，其中重复关键词有：" + sBuffer);// 更新任务状态

    }

    // 英文关键词有值，中文关键词没有
    else if (enCount > 0 && zhCount == 0) {
      for (int i = 0; i < splitEn.size(); i++) {
        // 保存数据到keywordsdic
        VKeywordsDic vkwd = null;
        boolean isDup = false;
        try {
          vkwd = saveToVKeywordsDic(applicationId, applicationCode1, applicationCode2, year, splitEn.get(i));
        } catch (TaskDupRecordException e) {
          sBuffer.append(splitEn.get(i) + ",");
          isDup = true;
        }
        // 是英文就保存到英文，中文保存到中文
        if (!isDup) {
          if (TmpTaskUtils.containZhChar(vkwd.getKeyword())) {
            this.saveToVKeywordsSynonym(vkwd.getId(), vkwd.getKeyword(), vkwd.getKwtxt(), null, null, null);
          } else {
            this.saveToVKeywordsSynonym(null, null, null, vkwd.getId(), vkwd.getKeyword(), vkwd.getKwtxt());
          }

        }

      }
      this.updateTaskStatus(jobId, 1, "只有英文，其中重复关键词有：" + sBuffer);// 更新任务状态

    }

    // 中英文关键词都有值
    else if (zhCount > 0 && enCount > 0) {
      // 中英文关键词数目相同时
      if (zhCount == enCount) {
        StringBuffer handleSameCount = this.handleSameCount(applicationId, applicationCode1, applicationCode2, year,
            zhCount, enCount, splitZh, splitEn);
        sBuffer.append(handleSameCount);
      } else {
        StringBuffer handleDiffCount = this.handleDiffCount(applicationId, applicationCode1, applicationCode2, year,
            zhCount, enCount, splitZh, splitEn);
        sBuffer.append(handleDiffCount);
      }
      String msg = null;
      if (zhCount == enCount) {
        msg = "数目相同";
      }

      else {
        msg = "数目不相同";
      }
      this.updateTaskStatus(jobId, 1, "中英文," + msg + "其中重复关键词有：" + sBuffer);// 更新任务状态
    }

  }

  /**
   * 处理数目相同的
   * 
   * @param applicationId
   * @param applicationCode1
   * @param applicationCode2
   * @param year
   * @param zhCount
   * @param enCount
   * @param splitZh
   * @param splitEn
   * @return
   * @throws Exception
   */
  public StringBuffer handleSameCount(Long applicationId, String applicationCode1, String applicationCode2,
      Integer year, int zhCount, int enCount, List<String> splitZh, List<String> splitEn) throws Exception {
    StringBuffer sBuffer = new StringBuffer();
    for (int i = 0; i < splitZh.size(); i++) {
      // 保存中文关键词到DIC
      boolean isDup = false;
      VKeywordsDic zhkwd = null;
      VKeywordsDic enkwd = null;
      boolean isSame = false;
      // 中文关键词中拆分出来只含有英文和其他字符的关键词，他对应的英文关键词可能一样的会导致重复，这种情况，则只保存英文到dic不记录对应中英关系

      // 判断zhkeywords中是否有和enkeywords中相同的

      for (String enStr : splitEn) {
        boolean checkSame = checkSame(splitZh.get(i), enStr);
        if (checkSame) {
          isSame = true;
        }
      }

      // zhkeywords中含有英文关键词和enkeywords相同则只保存enkeywords中拆分的，
      // enkeywords中含有中文关键词和zhkeywords相同则只保存zhkeywords中拆分的，
      if (isSame) {
        // 相同且这个splitZh中的关键词是中文
        if (TmpTaskUtils.containZhChar(splitZh.get(i))) {
          // 保存关键词到DIC
          try {
            zhkwd = saveToVKeywordsDic(applicationId, applicationCode1, applicationCode2, year, splitZh.get(i));
          } catch (TaskDupRecordException e) {
            sBuffer.append(splitZh.get(i) + ",");
            isDup = true;
          }
          // TODO splitEn对应位置的关键词将不会保存
          // 保存到中文
          if (!isDup) {
            this.saveToVKeywordsSynonym(zhkwd.getId(), zhkwd.getKeyword(), zhkwd.getKwtxt(), null, null, null);
          }
        } else {
          // 相同且这个splitZh中的关键词是英文（丢掉splitZh中的这个关键词）
          // 保存对应位置英文关键词到DIC
          try {
            enkwd = saveToVKeywordsDic(applicationId, applicationCode1, applicationCode2, year, splitEn.get(i));
          } catch (TaskDupRecordException e) {
            sBuffer.append(splitEn.get(i) + ",");
            isDup = true;
          }
          // 只保存英文
          if (!isDup) {
            this.saveToVKeywordsSynonym(null, null, null, enkwd.getId(), enkwd.getKeyword(), enkwd.getKwtxt());
          }

        }

      }
      // 没有相同的正常保存
      else {
        boolean zhdup = false;
        boolean endup = false;
        // 保存关键词到DIC
        try {
          zhkwd = saveToVKeywordsDic(applicationId, applicationCode1, applicationCode2, year, splitZh.get(i));
        } catch (TaskDupRecordException e) {
          sBuffer.append(splitZh.get(i) + ",");
          zhdup = true;
        }

        // 保存关键词到DIC
        try {
          enkwd = saveToVKeywordsDic(applicationId, applicationCode1, applicationCode2, year, splitEn.get(i));
        } catch (TaskDupRecordException e) {
          sBuffer.append(splitEn.get(i) + ",");
          endup = true;
        }

        // 保存处理
        if (!endup && !zhdup) {
          // 都没重复则一起保存
          this.saveToVKeywordsSynonym(zhkwd.getId(), zhkwd.getKeyword(), zhkwd.getKwtxt(), enkwd.getId(),
              enkwd.getKeyword(), enkwd.getKwtxt());

        }
        // 英文重复，只保存中文(保存过程有对关键词放到VKeywordsSynonym中英字段的处理)
        else if (endup && !zhdup) {
          this.saveToVKeywordsSynonym(zhkwd.getId(), zhkwd.getKeyword(), zhkwd.getKwtxt(), null, null, null);
        } else if (zhdup && !endup) {
          this.saveToVKeywordsSynonym(null, null, null, enkwd.getId(), enkwd.getKeyword(), enkwd.getKwtxt());
        }

      }

    }
    return sBuffer;

  }

  /**
   * 处理中英文数目不相同的
   * 
   * @param applicationId
   * @param applicationCode1
   * @param applicationCode2
   * @param year
   * @param zhCount
   * @param enCount
   * @param splitZh
   * @param splitEn
   * @return
   * @throws Exception
   */

  public StringBuffer handleDiffCount(Long applicationId, String applicationCode1, String applicationCode2,
      Integer year, int zhCount, int enCount, List<String> splitZh, List<String> splitEn) throws Exception {
    StringBuffer sBuffer = new StringBuffer();
    // 中英文关键词字数不同时,分开保存，同时需要判断中文中是否含英文字符的关键词和英文中的相同，如果相同则只取英文关键词中的拆分的保存，反之
    for (int i = 0; i < splitZh.size(); i++) {
      VKeywordsDic zhkwd = null;
      boolean isDup = false;
      boolean hasSame = false;

      // 判断zhkeywords中是否有和enkeywords中相同的
      for (String enStr : splitEn) {
        boolean checkSame = checkSame(splitZh.get(i), enStr);
        if (checkSame) {
          hasSame = true;
        }
      }

      // 没有相同的直接保存
      // 有相同的但是是中文的也保存，（enkeywords中对应的中文的则不保存）
      if (!hasSame || (TmpTaskUtils.containZhChar(splitZh.get(i)) && hasSame)) {
        // 保存中文关键词到DIC
        try {
          zhkwd = saveToVKeywordsDic(applicationId, applicationCode1, applicationCode2, year, splitZh.get(i));
        } catch (TaskDupRecordException e) {
          sBuffer.append(splitZh.get(i) + ",");
          isDup = true;
        }
        // 是英文就保存到英文，中文保存到中文
        if (!isDup) {
          if (TmpTaskUtils.containZhChar(zhkwd.getKeyword())) {
            this.saveToVKeywordsSynonym(zhkwd.getId(), zhkwd.getKeyword(), zhkwd.getKwtxt(), null, null, null);
          } else {
            this.saveToVKeywordsSynonym(null, null, null, zhkwd.getId(), zhkwd.getKeyword(), zhkwd.getKwtxt());
          }

        }

      }

    }

    for (int i = 0; i < splitEn.size(); i++) {
      // 保存英文关键词到DIC
      VKeywordsDic enkwd = null;
      boolean isDup = false;
      boolean hasSame = false;

      // 判断enkeywords中是否有和zhkeywords中相同的
      for (String zhStr : splitZh) {
        boolean checkSame = checkSame(splitEn.get(i), zhStr);
        if (checkSame) {
          hasSame = true;
        }
      }

      // 没有相同的直接保存
      // 有相同的但是英文的也保存，（zhkeywords中对应的英文的则不保存）
      if (!hasSame || (!TmpTaskUtils.containZhChar(splitEn.get(i)) && hasSame)) {
        try {
          enkwd = saveToVKeywordsDic(applicationId, applicationCode1, applicationCode2, year, splitEn.get(i));
        } catch (TaskDupRecordException e) {
          sBuffer.append(splitEn.get(i) + ",");
          isDup = true;
        }
        // 是英文就保存到英文，中文保存到中文
        if (!isDup) {
          if (TmpTaskUtils.containZhChar(enkwd.getKeyword())) {
            this.saveToVKeywordsSynonym(enkwd.getId(), enkwd.getKeyword(), enkwd.getKwtxt(), null, null, null);
          } else {
            this.saveToVKeywordsSynonym(null, null, null, enkwd.getId(), enkwd.getKeyword(), enkwd.getKwtxt());
          }

        }
      }
    }

    return sBuffer;

  }

  /**
   * 检查中文关键词中是否和英文中的相同
   * 
   * @param zhStr
   * @param enStr
   * @return
   */

  public boolean checkSame(String zhStr, String enStr) {

    String replacezh = zhStr.trim().toLowerCase().replace(" ", "");
    String replaceen = enStr.trim().toLowerCase().replace(" ", "");
    if (replacezh.equals(replaceen)) {
      return true;
    }

    return false;
  }

  /**
   * 保存关键词
   * 
   * @param applicationId
   * @param applicationCode1
   * @param applicationCode2
   * @param year
   * @param keyword
   * @return
   * @throws Exception,TaskDupRecordException
   */
  public VKeywordsDic saveToVKeywordsDic(Long applicationId, String applicationCode1, String applicationCode2,
      Integer year, String keyword) throws TaskDupRecordException, Exception {
    VKeywordsDic kwd = new VKeywordsDic();
    if (StringUtils.isBlank(keyword)) {
      throw new ServiceException("keyword为空！");
    }
    String kws = keyword.trim();
    String fturesWd = kws.toLowerCase().replace(" ", "");
    Long keywordHash = PubHashUtils.getKeywordHash(fturesWd);
    Long dupKwFturesHash = vKeywordsDicDao.getDupKwFturesHash(keywordHash);
    if (dupKwFturesHash != null) {
      throw new TaskDupRecordException("查询到重复关键词，不保存：" + kws);
    }

    kwd.setApplicationId(applicationId);
    kwd.setApplicationCode1(applicationCode1);
    kwd.setApplicationCode2(applicationCode2);

    if (StringUtils.isNotBlank(applicationCode1)) {
      StringBuffer sb = new StringBuffer();
      List<Long> scmCategoryByNsfcCategory = categoryMapScmNsfcDao.getScmCategoryByNsfcCategory(applicationCode1);
      if (!CollectionUtils.isEmpty(scmCategoryByNsfcCategory)) {
        for (Long long1 : scmCategoryByNsfcCategory) {
          sb.append(long1 + ",");
        }
        kwd.setScmCategoryId(sb.toString());
      }
    } else if (StringUtils.isNotBlank(applicationCode2)) {
      StringBuffer sb = new StringBuffer();
      List<Long> scmCategoryByNsfcCategory = categoryMapScmNsfcDao.getScmCategoryByNsfcCategory(applicationCode1);
      if (!CollectionUtils.isEmpty(scmCategoryByNsfcCategory)) {
        for (Long long1 : scmCategoryByNsfcCategory) {
          sb.append(long1);
        }
        kwd.setScmCategoryId(sb.toString());
      }
    }

    kwd.setYear(year);
    kwd.setKeyword(kws);
    kwd.setKwtxt(kws.toLowerCase());
    kwd.setFturesWd(fturesWd);
    kwd.setType(getKwType(kws));
    kwd.setKwhash(PubHashUtils.getKeywordHash(kws));
    kwd.setFturesHash(keywordHash);
    // kwd.setKwRhash(kwRhash);
    try {
      if (TmpTaskUtils.isFullChinese(keyword)) {
        kwd.setWlen(keyword.length());
      } else if (TmpTaskUtils.isFullEnglish(keyword)) {
        if (keyword.contains(" ")) {
          kwd.setWlen(keyword.split(" ").length);

        }
        if (keyword.contains(",") || keyword.contains("，")) {
          keyword = keyword.replace("，", ",");
          kwd.setWlen(keyword.split(",").length);
        }
        if (keyword.contains("、")) {
          kwd.setWlen(keyword.split("、").length);
        } else {
          kwd.setWlen(keyword.length());
        }
      } else {
        kwd.setWlen(keyword.length());
      }

    } catch (Exception e) {
      logger.error("获取关键词长度出错！");
      kwd.setWlen(keyword.length());
    }
    vKeywordsDicDao.save(kwd);

    return kwd;
  }

  public void saveScmId() {

  }

  /**
   * 保存到VKeywordsSynonym
   * 
   * @param id
   * @param zhKeywordId
   * @param zhKeyword
   * @param zhKeywordtxt
   * @param enKeywordId
   * @param enKeyword
   * @param enKeywordtxt
   */
  public void saveToVKeywordsSynonym(Long zhKeywordId, String zhKeyword, String zhKeywordtxt, Long enKeywordId,
      String enKeyword, String enKeywordtxt) {
    VKeywordsSynonym vkws = new VKeywordsSynonym();

    // zhKeyword为英文，enKeyword含中文，交换位置
    if (!TmpTaskUtils.containZhChar(zhKeyword) && TmpTaskUtils.containZhChar(enKeyword)) {
      vkws.setZhKeywordId(enKeywordId);
      vkws.setZhKeyword(enKeyword == null ? null : enKeyword.trim());
      vkws.setZhKeywordtxt(enKeywordtxt == null ? null : enKeywordtxt);
      vkws.setEnKeywordId(zhKeywordId);
      vkws.setEnKeyword(zhKeyword == null ? null : zhKeyword.trim());
      vkws.setEnKeywordtxt(zhKeywordtxt == null ? null : zhKeywordtxt.trim());
    }
    // zhKeyword为空，enKeyword含中文，交换位置
    else if (StringUtils.isBlank(zhKeyword) && TmpTaskUtils.containZhChar(enKeyword)) {
      vkws.setZhKeywordId(enKeywordId);
      vkws.setZhKeyword(enKeyword == null ? null : enKeyword.trim());
      vkws.setZhKeywordtxt(enKeywordtxt == null ? null : enKeywordtxt);
      vkws.setEnKeywordId(zhKeywordId);
      vkws.setEnKeyword(zhKeyword == null ? null : zhKeyword.trim());
      vkws.setEnKeywordtxt(zhKeywordtxt == null ? null : zhKeywordtxt.trim());
    }

    // zhKeyword为英文，enKeyword为空，交换位置
    else if (StringUtils.isBlank(enKeyword) && !TmpTaskUtils.containZhChar(zhKeyword)) {
      vkws.setZhKeywordId(enKeywordId);
      vkws.setZhKeyword(enKeyword == null ? null : enKeyword.trim());
      vkws.setZhKeywordtxt(enKeywordtxt == null ? null : enKeywordtxt);
      vkws.setEnKeywordId(zhKeywordId);
      vkws.setEnKeyword(zhKeyword == null ? null : zhKeyword.trim());
      vkws.setEnKeywordtxt(zhKeywordtxt == null ? null : zhKeywordtxt.trim());
    }
    // 其他情况不用交换位置。
    else {
      vkws.setZhKeywordId(zhKeywordId);
      vkws.setZhKeyword(zhKeyword == null ? null : zhKeyword.trim());
      vkws.setZhKeywordtxt(zhKeywordtxt == null ? null : zhKeywordtxt);
      vkws.setEnKeywordId(enKeywordId);
      vkws.setEnKeyword(enKeyword == null ? null : enKeyword.trim());
      vkws.setEnKeywordtxt(enKeywordtxt == null ? null : enKeywordtxt.trim());
    }

    vKeywordsSynonymDao.save(vkws);

  }

  /**
   * 获取关键词类型
   * 
   * @param str
   * @return
   */

  private int getKwType(String str) {
    if (TmpTaskUtils.containZhChar(str)) {
      return 2;// 中文
    } else {
      return 1;// 英文
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

  @Override
  public List<VKeywordsSynonym> GetVKeywordsSynonymList(int index, int batchsize) {
    return vKeywordsSynonymDao.GetVKeywordsSynonymList(index, 1000);
  }

  @Override
  public void writeToDicFile(String datesuffix) throws Exception {
    String newdicEnName = dicEnName + datesuffix;
    String newdicZhName = dicZhName + datesuffix;
    String newdicMixName = dicMixName + datesuffix;
    // 删除上次任务生成的文件
    this.removeOldDic(new String[] {newdicEnName, newdicZhName, newdicMixName});
    List<VKeywordsSynonym> getVKeywordsSynonymList = null;
    int index = 0;
    int mixCount = 0;
    int enCount = 0;
    int zhCount = 0;
    while (true) {
      index++;// 分页取ID
      try {
        getVKeywordsSynonymList = GetVKeywordsSynonymList(index, 1000);
        if (CollectionUtils.isEmpty(getVKeywordsSynonymList)) {
          break;
        }
      } catch (Exception e) {
        logger.error("获取需要写入的关键词数据出错！", e);
      }

      List<String> mixlist = new ArrayList<>();
      List<String> enlist = new ArrayList<>();
      List<String> zhlist = new ArrayList<>();

      for (VKeywordsSynonym vksm : getVKeywordsSynonymList) {
        if (vksm.getZhKeyword() != null) {
          // 将内容区域的回车换行去除
          mixlist.add(vksm.getZhKeyword().replaceAll("[\\t\\n\\r]", ""));
          zhlist.add(vksm.getZhKeyword().replaceAll("[\\t\\n\\r]", ""));

        }
        if (vksm.getEnKeyword() != null) {
          enlist.add(vksm.getEnKeyword().replaceAll("[\\t\\n\\r]", ""));
          mixlist.add(vksm.getEnKeyword().replaceAll("[\\t\\n\\r]", ""));
        }
      }
      try {
        File root = new File(dicRootPath);
        if (!root.exists()) {
          if (root.mkdirs()) {
            root.setWritable(true);
          }
        }
        // 写入完整字典
        mixCount += mixlist.size();
        for (String str1 : mixlist) {
          this.writeToDicFile(newdicMixName, str1);
        }
        // 写入全英文
        enCount += enlist.size();
        for (String str1 : enlist) {
          this.writeToDicFile(newdicEnName, str1);
        }
        // 写入全中文
        zhCount += zhlist.size();
        for (String str1 : zhlist) {
          this.writeToDicFile(newdicZhName, str1);
        }

      } catch (Exception e) {
        logger.error("写入到字典文件出错！", e);
      }

    }
    try {
      this.writeLog(newdicMixName, mixCount);
      this.writeLog(newdicEnName, enCount);
      this.writeLog(newdicZhName, zhCount);
    } catch (IOException e) {
      logger.error("写入字典记录文件出错！", e);
    }

  }

  public void writeLog(String filename, int count) throws IOException {
    BufferedWriter bufferedWriter = null;
    try {
      String dic = dicRootPath + filename + ".txt";
      File txt = new File(dic);
      bufferedWriter = new BufferedWriter(new FileWriter(txt, true));
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
      String diclog = "写入关键词总数为：" + count + ",生成时间：" + sdf.format(new Date());
      bufferedWriter.write(diclog + "\r\n");
      bufferedWriter.flush();

    } catch (IOException e) {
      logger.error("字典文件记录信息写入错误");
    } finally {
      bufferedWriter.close();
    }

  }

  /**
   * 先删除上次生成的文件
   */
  public void removeOldDic(String[] strings) {
    for (String filename : strings) {
      File file = new File(filename + ".dic");
      if (file.exists()) {
        file.delete();
      }
    }
  }

  /**
   * 写入到文件
   * 
   * @param path
   * @param content
   * @throws IOException
   */
  public void writeToDicFile(String path, String content) throws IOException {
    ByteArrayInputStream InputStringStream = new ByteArrayInputStream(content.getBytes());
    // 可检测多种类型，并剔除bom
    BOMInputStream bomIn = new BOMInputStream(InputStringStream, false, ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE,
        ByteOrderMark.UTF_16BE);
    String charset = "utf-8";
    // 若检测到bom，则使用bom对应的编码
    if (bomIn.hasBOM()) {
      charset = bomIn.getBOMCharsetName();
    }
    InputStreamReader reader = new InputStreamReader(bomIn, charset);
    BufferedReader bufferedReader = null;
    BufferedWriter writer = null;
    try {

      bufferedReader = new BufferedReader(reader);
      String str = null;
      File dic = new File(dicRootPath + path + ".dic");
      if (!dic.exists()) {
        if (dic.createNewFile()) {
          dic.setWritable(true);
        }
      }
      writer = new BufferedWriter(new FileWriter(dic, true));
      /*
       * window/dos 下文件换行符为 0x0D ,0x0A unix/linux 下文件换行符为 0x0A 0x0D == \r 0x0A == \n
       */
      while ((str = bufferedReader.readLine()) != null) {
        writer.write(str + System.lineSeparator());
      }
      writer.flush();
    } catch (Exception e) {
      logger.error("写入字典文件错误！");

    } finally {
      bufferedReader.close();
      writer.close();
    }

  }

}
