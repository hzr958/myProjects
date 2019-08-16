package com.smate.center.task.service.pdwh.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.email.PersonEmailDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhAddrPsnUpdateRecordDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhInsAddrConstDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubAddrInsRecordDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubAuthorSnsPsnRecordDao;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.dao.sns.psn.PersonPmNameDao;
import com.smate.center.task.dao.sns.quartz.InstitutionDao;
import com.smate.center.task.model.pdwh.pub.PdwhInsAddrConst;
import com.smate.center.task.model.pdwh.pub.PdwhPubAddrInsRecord;
import com.smate.center.task.model.pdwh.pub.PdwhPubAuthorSnsPsnRecord;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.psn.model.PersonPmName;
import com.smate.center.task.v8pub.dao.pdwh.PdwhMemberEmailDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhMemberInsNameDAO;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.service.psnname.PsnNameToPinyinService;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;

@Service("updatePdwhPubMatchInfoService")
@Transactional(rollbackFor = Exception.class)
public class UpdatePdwhPubMatchInfoServiceImpl implements UpdatePdwhPubMatchInfoService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhAddrPsnUpdateRecordDao pdwhAddrPsnUpdateRecordDao;
  @Autowired
  private PdwhInsAddrConstDao pdwhInsAddrConstDao;
  @Autowired
  private PdwhPublicationService pdwhPublicationService;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private PdwhPubAddrInsRecordDao pdwhPubAddrInsRecordDao;
  @Autowired
  private PdwhPubAuthorSnsPsnRecordDao pdwhPubAuthorSnsPsnRecordDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PersonPmNameDao personPmNameDao;
  @Autowired
  private PdwhMemberInsNameDAO pdwhMemberInsNameDAO;
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  @Autowired
  private PdwhMemberEmailDAO pdwhMemberEmailDAO;
  @Autowired
  private PersonEmailDao personEmailDao;

  @Autowired
  private PsnNameToPinyinService psnNameToPinyinService;
  @Autowired
  private InstitutionDao institutionDao;

  /*
   * static SolrServer server; static { server = new
   * HttpSolrServer("http://120.132.126.90:28080/solr/"); }
   */
  @Override
  public List<Long> getNeedUpdateAddrConstId(Integer size) {
    return pdwhAddrPsnUpdateRecordDao.getNeedUpdateConstIds(size);
  }

  @Override
  public void startUpdateAddrConst(Long constId) throws Exception {
    PdwhInsAddrConst addrConst = pdwhInsAddrConstDao.get(constId);
    Long insId = addrConst.getInsId();
    /*
     * String insName = addrConst.getInsName(); Long insNameHash = addrConst.getInsNameHash();
     */
    // 地址更新记录
    List<PdwhPubAddrInsRecord> insRecordList = pdwhPubAddrInsRecordDao.getPubAddrInsRecordByConst(constId);
    /**
     * 对地址常量的更新只限于修改了单位的ins_ID
     */
    if (CollectionUtils.isEmpty(insRecordList)) {
      logger.error("PdwhPubAddrInsRecord表查询不到对应记录，constId:" + constId);
      pdwhAddrPsnUpdateRecordDao.updateStatus(constId, 1, 1);
      return;
    }
    Long regionId = institutionDao.findInsRegionId(insId);
    for (PdwhPubAddrInsRecord insRecord : insRecordList) {
      Long pubId = insRecord.getPubId();
      insRecord.setInsId(insId);
      /*
       * insRecord.setInsName(insName); insRecord.setInsNameHash(insNameHash);
       */
      insRecord.setRegionId(regionId);
      insRecord.setUpdateTime(new Date());
      pdwhPubAddrInsRecordDao.save(insRecord);// 更新地址匹配记录表
      pdwhPubAuthorSnsPsnRecordDao.UpdateInsIdRecByPubId(pubId, insId);
    }
    pdwhAddrPsnUpdateRecordDao.updateStatus(constId, 1, 1);
  }

  @Override
  public List<Long> getNeedUpdatePsnConstId(Integer size) {
    return pdwhAddrPsnUpdateRecordDao.getNeedUpdatePsnIds(size);
  }

  /**
   * 更新人名匹配记录
   * 
   * @param psnId
   * @author LIJUN
   * @date 2018年3月22日
   */
  @Override
  public void updatePubAuthorSnsPsnRecord(Person person) throws Exception {

    List<String> namelist = personPmNameDao.getAllPsnName(person.getPersonId());

    /*
     * // 修改了人名 原有的不符合的匹配记录需要删除。被确认的人名存在则更新时间 List<PdwhPubAuthorSnsPsnRecord> list =
     * pdwhPubAuthorSnsPsnRecordDao .getUnconfirmedRecByPsnId(person.getPersonId()); //
     * 删除已经不存在的未确认的人名记录,对于还存在的记录更新时间 if (CollectionUtils.isEmpty(namelist)) { for
     * (PdwhPubAuthorSnsPsnRecord psnRecord : list) {
     * pdwhPubAuthorSnsPsnRecordDao.delete(psnRecord.getId()); } } if
     * (CollectionUtils.isNotEmpty(namelist) && CollectionUtils.isNotEmpty(list)) { for
     * (PdwhPubAuthorSnsPsnRecord psnRecord : list) { if (!namelist.contains(psnRecord.getPsnName())) {
     * pdwhPubAuthorSnsPsnRecordDao.delete(psnRecord.getId()); } else {
     * pdwhPubAuthorSnsPsnRecordDao.updateTime(psnRecord.getId()); } } }
     */
    // 对于确认的人名记录，存在则更新时间，不存在则不更新
    List<PdwhPubAuthorSnsPsnRecord> confirmedList =
        pdwhPubAuthorSnsPsnRecordDao.getConfirmedRecByPsnId(person.getPersonId());
    if (CollectionUtils.isNotEmpty(namelist) && CollectionUtils.isNotEmpty(confirmedList)) {
      for (PdwhPubAuthorSnsPsnRecord pdwhPubAuthorSnsPsnRecord : confirmedList) {
        if (namelist.contains(pdwhPubAuthorSnsPsnRecord.getPsnName())) {
          pdwhPubAuthorSnsPsnRecordDao.updateTime(pdwhPubAuthorSnsPsnRecord.getId());
        }
      }
    }

  }

  @Override
  public void deleteUnconfirmedRecordByPsn(Long psnId) throws Exception {
    pdwhPubAuthorSnsPsnRecordDao.deleteUnconfirmedRecordByPsn(psnId);// 先删除没有被确认的记录

  }

  @Override
  public List<PersonPmName> getPsnPmName(Long personId) {
    return personPmNameDao.getPsnNameRecByPsnId(personId);

  }

  /**
   * 检索人员成果并进行匹配
   * 
   * @param personPmName
   * @param page
   * @param server
   * @return false 检索不到成果，true检索到，需要继续分页检索
   * @throws SolrServerException
   * @author LIJUN
   * @date 2018年6月29日
   */
  @Override
  public boolean matchPsnPubs(PersonPmName personPmName, List<Map<String, Object>> psnPubList)
      throws SolrServerException {
    if (CollectionUtils.isNotEmpty(psnPubList)) {
      for (Map<String, Object> pub : psnPubList) {// 人员检索到的成果
        Long pubId = null;
        Long pubYear = null;
        if (pub.get("pubId") != null) {
          pubId = NumberUtils.toLong(pub.get("pubId").toString());
        }
        if (pub.get("pubYear") != null) {
          pubYear = NumberUtils.toLong(pub.get("pubYear").toString());
        }
        List<PdwhPubAddrInsRecord> records = pdwhPubAddrInsRecordDao.getPubAddrInsRecordByPubId(pubId);
        if (CollectionUtils.isEmpty(records)) {
          logger.debug("PdwhPubAddrInsRecord表：该成果没有记录，将自动重新分词匹配一次。pub_id:" + pubId);
          try {
            if (pub.get("organization") != null) {
              String organization = pub.get("organization").toString();
              if (StringUtils.isNotBlank(organization)) {
                pdwhPublicationService.segmentPubOrg(pubId, organization);
              }
            }
          } catch (Exception e) {
            logger.error("更新人员信息，注册人员，匹配成果单位，重新分词匹配该成果单位时发生错误，pubId：" + pubId);
          }
        }
        records = pdwhPubAddrInsRecordDao.getPubAddrInsRecordByPubId(pubId);
        if (CollectionUtils.isEmpty(records)) {
          logger.info("该成果匹配不到单位信息，pub_id:" + pubId);
          continue;
        }
        this.savePubPsnRecord(records, pubId, pubYear, personPmName);
      }
    } else {
      return false;// 检索不到成果
    }
    return true;
  }

  /**
   * 保存成果人员匹配记录
   * 
   * @param records
   * @param pubId
   * @param psnId
   * @param psnInsId
   * @param psnName
   * @param nameType
   * @author LIJUN
   * @param pubYear
   * @date 2018年5月23日
   */
  public void savePubPsnRecord(List<PdwhPubAddrInsRecord> records, Long pubId, Long pubYear,
      PersonPmName personPmName) {
    Long psnId = personPmName.getPsnId();
    String psnName = personPmName.getName();
    Integer nameType = personPmName.getType();
    List<Map<String, Object>> insMapList = pdwhMemberInsNameDAO.getPubMemberInsIdList(pubId, psnName);
    if (CollectionUtils.isNotEmpty(insMapList)) {
      for (Map<String, Object> insMap : insMapList) {
        String insId = insMap.get("insId").toString();
        String insName = insMap.get("insName").toString();
        Long memberId = Long.valueOf(insMap.get("memberId").toString());
        if (StringUtils.isNotBlank(insId) && StringUtils.isNotBlank(insName)) {
          // 如果当前成果人员,已经匹配上了单位，只用当前匹配上的单位，成果其他单位不做匹配
          List<PdwhPubAddrInsRecord> recored =
              pdwhPubAddrInsRecordDao.findRecByPubIdAndInsId(pubId, Long.valueOf(insId));
          if (recored != null && recored.size() > 0) {
            this.saveMatchedRecord(recored.get(0), psnId, psnName, memberId, nameType, pubYear);
          } else {
            PdwhPubAddrInsRecord newRecord = new PdwhPubAddrInsRecord(pubId, Long.valueOf(insId), insName, 0);
            this.saveMatchedRecord(newRecord, psnId, psnName, memberId, nameType, pubYear);
          }
        } else {
          for (PdwhPubAddrInsRecord record : records) {
            this.saveMatchedRecord(record, psnId, psnName, memberId, nameType, pubYear);
          }
        }
      }
    }
  }


  private void saveMatchedRecord(PdwhPubAddrInsRecord record, Long psnId, String psnName, Long memberId,
      Integer nameType, Long pubYear) {
    List<Long> psnInsIdList = this.getPsnEduWorkInsIds(psnId, pubYear);
    if (CollectionUtils.isNotEmpty(psnInsIdList) && psnInsIdList.contains(record.getInsId())) {
      // insId和人名匹配上
      PdwhPubAuthorSnsPsnRecord psnRecord =
          pdwhPubAuthorSnsPsnRecordDao.getPsnRecord(record.getPubId(), psnId, record.getInsId(), psnName, nameType);
      if (psnRecord == null) {
        if (record.getStatus() == 1) {// 成果地址被确认
          psnRecord = new PdwhPubAuthorSnsPsnRecord(record.getPubId(), psnId, psnName, record.getInsId(),
              record.getInsName(), 3, new Date(), nameType);
          psnRecord.setPubMemberId(memberId);
          psnRecord.setPubMemberName(psnName);
        } else {
          psnRecord = new PdwhPubAuthorSnsPsnRecord(record.getPubId(), psnId, psnName, record.getInsId(),
              record.getInsName(), 2, new Date(), nameType);
          psnRecord.setPubMemberId(memberId);
          psnRecord.setPubMemberName(psnName);
        }
        try {
          pdwhPubAuthorSnsPsnRecordDao.saveWithNewTransaction(psnRecord);
          // 同时要实时指派到人，即pub_assign_log表要有记录
          this.saveTmpTaskInfoRecord(psnRecord.getPubId());
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
          logger.error("PdwhPubAuthorSnsPsnRecord保存出现错误，已经存在相同记录 ，listinfo={}", psnRecord.toString());
        } catch (Exception e) {
          logger.error("PdwhPubAuthorSnsPsnRecord保存出现错,recordinfo={}", psnRecord.toString(), e);
        }
      }
    }

  }


  private void saveTmpTaskInfoRecord(Long pubId) {
    TmpTaskInfoRecord tmpTaskInfoRecord =
        tmpTaskInfoRecordDao.getJobByhandleId(pubId, TaskJobTypeConstants.pdwhPubAssignInsTask);
    if (tmpTaskInfoRecord != null) {
      tmpTaskInfoRecord.setStatus(0);
      tmpTaskInfoRecord.setHandletime(new Date());
    } else {
      tmpTaskInfoRecord = new TmpTaskInfoRecord(pubId, TaskJobTypeConstants.pdwhPubAssignInsTask, new Date());
    }
    tmpTaskInfoRecordDao.saveOrUpdate(tmpTaskInfoRecord);

  }

  private List<Long> getPsnEduWorkInsIds(Long psnId, Long pubYear) {
    List<Long> psnEduInsId = new ArrayList<Long>();
    List<Long> psnWorkInsId = new ArrayList<Long>();
    if (pubYear == null) {
      psnEduInsId = educationHistoryDao.getPsnEduInsId(psnId);
      psnEduInsId = workHistoryDao.getPsnWorkInsId(psnId);
    } else {
      psnEduInsId = educationHistoryDao.getPsnEduByIdAndYear(psnId, Long.valueOf(pubYear));
      psnWorkInsId = workHistoryDao.getPsnWorkByIdAndYear(psnId, Long.valueOf(pubYear));// 查询工作经历insid
    }
    Set<Long> psnInsIds = new HashSet<>();
    List<Long> psnInsIdList = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(psnEduInsId)) {
      psnInsIds.addAll(psnEduInsId);
    }
    if (CollectionUtils.isNotEmpty(psnWorkInsId)) {
      psnInsIds.addAll(psnWorkInsId);
    }
    psnInsIdList.addAll(psnInsIds);
    return psnInsIdList;
  }

  @Override
  public void generalPsnPmName(Person person) {
    String zhname = person.getName();
    String ename = person.getEname();
    personPmNameDao.saveUserInputName(cleanAuthorChars(zhname), cleanAuthorChars(ename), person.getPersonId(),
        person.getInsId());
    Map<String, Set<String>> map = psnNameToPinyinService.generalPsnPmName(person);
    if (map == null || map.isEmpty()) {
      return;
    }
    Set<String> fullnames = map.get(PsnNameToPinyinService.FULLNAME);
    personPmNameDao.saveFullName(fullnames, person.getPersonId(), person.getInsId());

    Set<String> initNames = map.get(PsnNameToPinyinService.INITNAME);
    personPmNameDao.saveInitName(initNames, person.getPersonId(), person.getInsId());
    Set<String> prefixNames = map.get(PsnNameToPinyinService.PREFIXNAME);
    personPmNameDao.savePrefixName(prefixNames, person.getPersonId(), person.getInsId());
  }

  @Override
  public Person getPersonIns(Long psnId) {
    return personDao.getPersonNameIns(psnId);
  }

  @Override
  public List<Long> tmpGetNeedUpdatePsnConstId(Integer size) {
    return pdwhAddrPsnUpdateRecordDao.getTmpNeedUpdatePsnIds(size);
  }

  @Override
  public void tmpGeneralPsnPmName(Long psnId) {
    Person person = this.getPersonIns(psnId);
    generalPsnPmName(person);

  }

  /**
   * 将姓名常见字符替换为空格 如果有连续空格则只保留一个
   * 
   * @param string
   * @return
   * @author LIJUN
   * @date 2018年3月26日
   */
  public static String cleanAuthorChars(String string) {
    if (StringUtils.isBlank(string)) {
      return null;
    }
    string = HtmlUtils.htmlUnescape(string);
    String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].\\-<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。·《》，、·？\" ]";
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(string);
    string = m.replaceAll(" ").trim();
    while (string.contains("  ")) {
      string = string.replace("  ", " ");
    }
    return string.trim().toLowerCase();

  }

  /**
   * 去除空格 符号转为小写
   * 
   * @param string
   * @return
   * @author LIJUN
   * @date 2018年3月26日
   */
  public static String replaceBlankChars(String string) {
    if (StringUtils.isBlank(string)) {
      return null;
    }
    string = HtmlUtils.htmlUnescape(string);
    String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].\\-<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。·《》，、·？\" ]";
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(string);
    string = m.replaceAll("").trim();
    return string.trim().toLowerCase();

  }

  /**
   * 更新处理状态
   * 
   * @param taskId
   * @param type 地址 1 ，人员2
   * @param status 0 默认，1成功，2失败
   * @author LIJUN
   * @date 2018年3月31日
   */
  @Override
  public void updateStatus(Long taskId, int type, int status) {
    pdwhAddrPsnUpdateRecordDao.updateStatus(taskId, type, status);

  }

  @Override
  public void saveOtherTaskRecord(Long psnId) {
    List<String> emailList = personEmailDao.getPsnEmails(psnId);
    Set<Long> pubIds = new HashSet<Long>();
    for (String email : emailList) {
      pubIds.addAll(pdwhMemberEmailDAO.getPubIdByEmail(email));
    }
    for (Long pubId : pubIds) {
      this.saveTmpTaskInfoRecord(pubId);
    }
  }
}
