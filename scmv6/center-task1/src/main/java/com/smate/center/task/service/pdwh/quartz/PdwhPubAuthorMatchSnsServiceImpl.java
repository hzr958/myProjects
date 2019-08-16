package com.smate.center.task.service.pdwh.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.email.PersonEmailDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhInsNameDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubAuthorInfoDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhSnsPubAuthorRelationDao;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.model.pdwh.pub.IsiInsNameDao;
import com.smate.center.task.model.pdwh.pub.PdwhPubAuthorInfo;
import com.smate.center.task.model.pdwh.quartz.PdwhSnsPubAuthorRelation;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.single.util.pub.PsnPmIsiNameUtils;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;

@Service("pdwhPubAuthorMatchSnsService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubAuthorMatchSnsServiceImpl implements PdwhPubAuthorMatchSnsService {
  @Autowired
  private UserDao userDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  PersonEmailDao personEmailDao;
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  @Autowired
  private PdwhPubAuthorInfoDao pdwhPubAuthorInfoDao;
  @Autowired
  private PdwhInsNameDao pdwhInsNameDao;
  @Autowired
  private IsiInsNameDao isiInsNameDao;
  @Autowired
  PdwhSnsPubAuthorRelationDao pdwhSnsPubAuthorRelationDao;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static Integer jobType = TaskJobTypeConstants.PdwhPubAuthorMatchSnsTask;

  @Override
  public void startMatching(TmpTaskInfoRecord info) throws Exception {
    Long jobId = info.getJobId();
    Long psnId = info.getHandleId();
    Person person = personDao.findPersonInfoIncludeIns(info.getHandleId());
    String otherName = person.getOtherName();
    String firstName = person.getFirstName();
    String lastName = person.getLastName();
    String name = person.getName();
    String ename = person.getEname();
    String insName = person.getInsName();
    Long insId = person.getInsId();
    // 用人员的姓名和单位和单位别名匹配
    if (StringUtils.isBlank(name) && StringUtils.isBlank(ename)) {
      this.updateTaskStatus(jobId, 2, "人员姓名name，ename都为空");
      return;
    }
    if (StringUtils.isBlank(insName) && insId == null) {
      this.updateTaskStatus(jobId, 2, "人员单位名，insId都为空");
      return;
    }
    List<String> namelist = new ArrayList<>();
    namelist.add(name);
    namelist.add(ename);
    // initNames
    Set<String> initNames = PsnPmIsiNameUtils.buildInitName(firstName, lastName, otherName);
    if (CollectionUtils.isNotEmpty(initNames)) {
      namelist.addAll(initNames);
    }

    // fullNames
    Set<String> fullNames = PsnPmIsiNameUtils.buildFullName(firstName, lastName, otherName);
    if (CollectionUtils.isNotEmpty(fullNames)) {
      namelist.addAll(fullNames);
    }

    // name +insId匹配
    boolean matchWithNameAndInsId = false;
    if (insId != null) {
      for (String namestr : namelist) {
        if (StringUtils.isEmpty(namestr)) {
          break;
        }
        matchWithNameAndInsId = this.matchWithNameAndInsId(namestr, insId, psnId);
        // 匹配上则退出
        if (matchWithNameAndInsId) {
          break;
        }
      }
    }

    List<String> insNameList = new ArrayList<>();
    List<String> similarInsName = pdwhInsNameDao.getSimilarInsName(insName);
    List<String> similarInsName2 = isiInsNameDao.getSimilarInsName(insName);
    if (CollectionUtils.isNotEmpty(similarInsName)) {
      insNameList.addAll(similarInsName);
    }
    if (CollectionUtils.isNotEmpty(similarInsName2)) {
      insNameList.addAll(similarInsName2);
    }

    // 查询不到别名则直接用原始记录的单位名
    if (CollectionUtils.isEmpty(insNameList)) {
      insNameList.add(insName);
    } else {
      insNameList = cleanListDup(insNameList);
    }
    boolean matchWithNameAndOrg = false;

    if (CollectionUtils.isNotEmpty(insNameList)) {
      for (String namestr : namelist) {
        if (StringUtils.isEmpty(namestr)) {
          break;
        }
        matchWithNameAndOrg = this.matchWithNameAndOrg(namestr, insNameList, psnId);
        // 匹配上则退出
        if (matchWithNameAndOrg) {
          break;
        }
      }
    }

    if (matchWithNameAndOrg && matchWithNameAndInsId) {
      // name and insId with name（ename）and org matched
      this.updateTaskStatus(jobId, 1, "both");
    } else if (!matchWithNameAndOrg && matchWithNameAndInsId) {
      // insId and name(ename) matched
      this.updateTaskStatus(jobId, 1, "insId matched");
    } else if (matchWithNameAndOrg && !matchWithNameAndInsId) {
      // name（ename）and org matched
      this.updateTaskStatus(jobId, 1, "org matched");
    } else {
      this.updateTaskStatus(jobId, 3, "没有匹配上");
    }

  }

  /**
   * 用人名和单位id匹配
   * 
   * @param name
   * @param insId
   * @param psnId
   * @return
   */
  public boolean matchWithNameAndInsId(String name, Long insId, Long psnId) {

    boolean flag = false;
    List<PdwhPubAuthorInfo> rec = this.pdwhPubAuthorInfoDao.findRecordBynameAndInsId(cleanName(name), insId);
    if (CollectionUtils.isNotEmpty(rec)) {
      for (PdwhPubAuthorInfo pdwhPubAuthorInfo : rec) {
        pdwhSnsPubAuthorRelationDao.saveUniqueNameOrg(new PdwhSnsPubAuthorRelation(pdwhPubAuthorInfo.getPubId(), psnId,
            pdwhPubAuthorInfo.getAuthorName(), pdwhPubAuthorInfo.getOrganization()));
        flag = true;
      }
    }

    return flag;

  }

  /**
   * 只用人名匹配
   * 
   * @param name
   * @param psnId
   * @return
   */
  @SuppressWarnings("unused")
  private boolean matchWithName(String name, Long psnId) {
    boolean flag = false;
    List<PdwhPubAuthorInfo> rec = this.pdwhPubAuthorInfoDao.findRecordByName(cleanName(name));
    if (CollectionUtils.isNotEmpty(rec)) {
      for (PdwhPubAuthorInfo pdwhPubAuthorInfo : rec) {
        pdwhSnsPubAuthorRelationDao.saveUniqueNameOrg(new PdwhSnsPubAuthorRelation(pdwhPubAuthorInfo.getPubId(), psnId,
            pdwhPubAuthorInfo.getAuthorName(), pdwhPubAuthorInfo.getOrganization()));
        flag = true;
      }
    }

    return flag;

  }

  /**
   * 用人名和单位匹配
   * 
   * @param name
   * @param insNameList
   * @param psnId
   */
  private boolean matchWithNameAndOrg(String name, List<String> insNameList, Long psnId) {
    boolean flag = false;
    // 用name 和单位
    if (StringUtils.isNoneBlank(name)) {
      for (String org : insNameList) {
        if (StringUtils.isEmpty(org)) {
          break;
        }
        List<PdwhPubAuthorInfo> rec = this.pdwhPubAuthorInfoDao.findRecord(cleanName(name), cleanOrg(org));
        if (CollectionUtils.isNotEmpty(rec)) {
          for (PdwhPubAuthorInfo pdwhPubAuthorInfo : rec) {
            pdwhSnsPubAuthorRelationDao.saveUniqueNameOrg(new PdwhSnsPubAuthorRelation(pdwhPubAuthorInfo.getPubId(),
                psnId, pdwhPubAuthorInfo.getAuthorName(), pdwhPubAuthorInfo.getOrganization()));
            flag = true;
          }
        }
      }
    }
    return flag;
  }

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
   * list去除重复
   * 
   * @param insNameList
   * @return
   */
  public List<String> cleanListDup(List<String> insNameList) {
    List<String> list = new ArrayList<>();
    List<String> newList = new ArrayList<String>();
    for (String str : insNameList) {
      if (StringUtils.isNoneBlank(str)) {
        // 查找是否有重复
        if (newList.contains(str.toLowerCase().trim().replace(" ", ""))) {
          logger.info("含有重复：" + str);
        } else {
          list.add(str);
        }
        newList.add(str.toLowerCase().trim().replace(" ", ""));
      }
    }
    return list;

  }

  @Override
  public List<TmpTaskInfoRecord> batchGetJobList(Integer size) throws Exception {

    return tmpTaskInfoRecordDao.getTaskInfoRecordList(size, jobType);
  }

  public void startMatchWithEmail(Long Id) throws Exception {
    try {
      PdwhPubAuthorInfo pdwhPubAuthorInfo = pdwhPubAuthorInfoDao.get(Id);
      String email = pdwhPubAuthorInfo.getEmail();
      Long pubId = pdwhPubAuthorInfo.getPubId();
      // 和person表email信息匹配
      List<Person> personByEmail = personDao.getPersonByEmail(email);
      if (CollectionUtils.isNotEmpty(personByEmail)) {
        for (Person person : personByEmail) {
          pdwhSnsPubAuthorRelationDao.saveUnique(new PdwhSnsPubAuthorRelation(pubId, person.getPersonId(), email));
        }
      }
      // 和sys_user表email信息匹配
      List<User> findUserByLoginNameOrEmail = userDao.findUserByLoginNameOrEmail(email);
      if (CollectionUtils.isNotEmpty(findUserByLoginNameOrEmail)) {
        for (User user : findUserByLoginNameOrEmail) {
          pdwhSnsPubAuthorRelationDao.saveUnique(new PdwhSnsPubAuthorRelation(pubId, user.getId(), email));
        }
      }
      // 和personeamil表email信息匹配
      List<Long> findPsnIdByEmail = personEmailDao.findPsnIdByEmail(email);
      if (CollectionUtils.isNotEmpty(findPsnIdByEmail)) {
        for (Long psnId : findPsnIdByEmail) {
          pdwhSnsPubAuthorRelationDao.saveUnique(new PdwhSnsPubAuthorRelation(pubId, psnId, email));
        }
      }
      tmpTaskInfoRecordDao.updateTaskStatus(Id, 1, "", jobType);

    } catch (Exception e) {
      logger.error("email匹配出错！", e);
      tmpTaskInfoRecordDao.updateTaskStatus(Id, 2, "email匹配出错！", jobType);
    }
  }

  @Override
  public void updateTaskStatus(Long JobId, int status, String err) {
    try {
      tmpTaskInfoRecordDao.updateTaskStatusById(JobId, status, err);
    } catch (Exception e) {
      logger.error("更新任务状态记录出错！JobId:" + JobId, e);
    }
  }

}
