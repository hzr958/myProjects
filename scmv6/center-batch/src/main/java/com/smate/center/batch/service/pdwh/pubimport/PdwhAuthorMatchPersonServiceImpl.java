package com.smate.center.batch.service.pdwh.pubimport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.PdwhPubAddrInsRecordDao;
import com.smate.center.batch.dao.pdwh.pub.PdwhPubAuthorSnsPsnRecordDao;
import com.smate.center.batch.dao.pdwh.pub.PubPdwhDAO;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubXmlDao;
import com.smate.center.batch.dao.sns.pub.PersonPmNameDao;
import com.smate.center.batch.model.pdwh.pub.PdwhPubAddrInsRecord;
import com.smate.center.batch.model.pdwh.pub.PdwhPubAuthorSnsPsnRecord;
import com.smate.center.batch.model.pdwh.pub.PubPdwhPO;
import com.smate.center.batch.model.sns.psn.PersonPmName;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.web.v8pub.dom.MemberInsBean;
import com.smate.web.v8pub.dom.PubMemberBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

/**
 * 基准库成果作者信息和sns人员匹配任务业务实现类
 *
 * @author LIJUN
 * @date 2018年3月15日
 */
@Service("pdwhAuthorMatchPersonService")
@Transactional(rollbackFor = Exception.class)
public class PdwhAuthorMatchPersonServiceImpl implements PdwhAuthorMatchPersonService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPubAddrInsRecordDao pdwhPubAddrInsRecordDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private PersonPmNameDao personPmNameDao;
  @Autowired
  private PdwhPubAuthorSnsPsnRecordDao pdwhPubAuthorSnsPsnRecordDao;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  boolean updateFlag;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;

  @Override
  public List<PdwhPubAddrInsRecord> getPubAddrMatchedRecord(Long pubId) {
    return pdwhPubAddrInsRecordDao.getPubAddrInsRecordByPubId(pubId);

  }

  @Override
  public void startMatchSnsPsn(List<PdwhPubAddrInsRecord> records, String context, PubPdwhDetailDOM pdwhPub)
      throws Exception {
    /**
     * 更新和新增逻辑相同
     */
    StringBuffer stringBuffer = new StringBuffer();
    List<PubMemberBean> members = pdwhPub.getMembers();

    if (CollectionUtils.isEmpty(members)) {
      logger.error("该成果没有获取到作者信息，pub_id:" + pdwhPub.getPubId());
      return;
    }
    for (PubMemberBean pubMemberBean : members) {
      /*
       * logger.error( "++++++++++++++++++++++++当前处理的成果id---------" + pdwhPub.getPubId() + "; 处理人名：" +
       * pubMemberBean.getName());
       */
      Set<String> matchedNameList = this.segmentStr(XmlUtil.replaceXMLAuthorChars(pubMemberBean.getName()));
      if (matchedNameList != null) {
        /*
         * logger.error("++++++++++++++++++++++++当前处理的成果id---------" + pdwhPub.getPubId() + "; 处理人名：" +
         * pubMemberBean.getName() + "词典返回的数据：————————" + matchedNameList.toString());
         */
        for (String matchedName : matchedNameList) {
          // 更新被确认的人员记录时间
          List<PdwhPubAuthorSnsPsnRecord> list =
              pdwhPubAuthorSnsPsnRecordDao.findConfirmRecByPubIdAndName(pdwhPub.getPubId(), matchedName);// 查询该成果人员姓名是否被用户确认
          if (CollectionUtils.isEmpty(list)) {// 记录不存在执行保存流程
            List<MemberInsBean> memberIns = pubMemberBean.getInsNames();
            if (CollectionUtils.isNotEmpty(memberIns)) {
              for (MemberInsBean memberInsBean : memberIns) {
                // 如果当前成果人员 已经匹配上了单位，只用当前匹配上的单位，成果其他单位不做匹配
                if (memberInsBean.getInsId() != null && StringUtils.isNotBlank(memberInsBean.getInsName())) {
                  List<PdwhPubAddrInsRecord> recored =
                      pdwhPubAddrInsRecordDao.findRecByPubIdAndInsId(pdwhPub.getPubId(), memberInsBean.getInsId());
                  if (recored != null && recored.size() > 0) {
                    this.saveMatchedRecord(recored.get(0), matchedName, pubMemberBean);
                  } else {
                    PdwhPubAddrInsRecord newRecord = new PdwhPubAddrInsRecord(pdwhPub.getPubId(),
                        memberInsBean.getInsId(), memberInsBean.getInsName(), 0);
                    this.saveMatchedRecord(newRecord, matchedName, pubMemberBean);
                  }
                } else {
                  for (PdwhPubAddrInsRecord record : records) {
                    this.saveMatchedRecord(record, matchedName, pubMemberBean);
                  }
                }
              }
            } else {
              for (PdwhPubAddrInsRecord record : records) {
                this.saveMatchedRecord(record, matchedName, pubMemberBean);
              }
            }

          } else {
            for (PdwhPubAuthorSnsPsnRecord psnRecord : list) {
              pdwhPubAuthorSnsPsnRecordDao.updateTime(psnRecord.getId());// 更新时间
            }
          }
        }
      } /*
         * else { logger.error("++++++++++++++++++++++++当前处理的成果id---------" + pdwhPub.getPubId() + "; 处理人名："
         * + pubMemberBean.getName() + "词典返回的数据为空：————————"); }
         */

    }
  }

  /**
   * // 删除没有被确认的成果地址记录
   *
   * @author LIJUN
   * @date 2018年3月30日
   */
  @Override
  public void deleteUnconfirmedRec(Long pubId) {
    // 删除没有被确认的成果地址记录
    pdwhPubAuthorSnsPsnRecordDao.deleteUnconfirmedRecord(pubId);

  }

  /**
   * 分词匹配
   *
   * @throws IOException
   * @author LIJUN
   * @date 2018年3月26日
   */
  public Set<String> segmentStr(String names) throws Exception {// 中文人名
    // long t1 = System.currentTimeMillis();
    Map<String, Set<String>> extractUserName = this.getExtractUserName(names);
    Set<String> namelist = extractUserName.get("scm_user_name");
    // long t2 = System.currentTimeMillis();
    // logger.info(pubId + "人名长度：" + (StringUtils.isBlank(string) ? 0 : string.length()) + "分词用时:" + (t2
    // - t1));
    if (CollectionUtils.isNotEmpty(namelist)) {
      return namelist;
    }
    return null;
    /*
     * this.updateMatchedRecord(pubId, records, namelist);
     */ /*
         * long t3 = System.currentTimeMillis(); logger.info(pubId + "人名长度：" + (StringUtils.isBlank(string)
         * ? 0 : string.length()) + "匹配用时:" + (t3 - t2));
         */

  }

  /**
   * @return java.util.Map<java.lang.String , java.util.Set < java.lang.String>>
   * @Author LIJUN
   * @Description //TODO 从词典提取人名
   * @Date 10:50 2018/7/19
   * @Param [str]
   **/
  public Map<String, Set<String>> getExtractUserName(String str) throws Exception {
    if (StringUtils.isEmpty(str)) {
      return null;
    }
    // 直接使用，在服务器启动时加载
    Result kwRs = DicAnalysis.parse(str);
    Set<String> name = new TreeSet<String>();
    Map<String, Set<String>> mp = new HashMap<String, Set<String>>();
    for (Term t : kwRs.getTerms()) {
      if (t == null) {
        continue;
      }
      if ("scm_user_name".equals(t.getNatureStr()) || "nr".equals(t.getNatureStr())) {
        if (StringUtils.isNotEmpty(t.getName())) {
          logger.error("----------------------------匹配到的单个人名:" + t.getName());
          name.add(t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim());
        }
      }
    }
    if (name.size() > 0) {
      mp.put("scm_user_name", name);
    }
    return mp;
  }

  /**
   * 替换常见字符
   *
   * @param string
   * @return
   * @author LIJUN
   * @date 2018年3月26日
   */
  public String replaceChars(String string) {

    string = string.replace("，", ",").replace("（", "(").replace("）", ")").replace("。", ".");
    string = string.replace(" ", "空格").replace(",", "逗号").replace(".", "点符号").replace("(", "左括号").replace(")", "右括号")
        .replace("&", "和符号").replace("'", "撇号").replace("-", "杠符号").replace("《", "前书名号").replace("》", "后书名号");
    return string;

  }

  /**
   * 还原字符
   *
   * @param string
   * @return
   * @author LIJUN
   * @date 2018年3月26日
   */
  public String resetChars(String string) {
    string = string.replace("空格", " ").replace("逗号", ",").replace("点符号", ".").replace("左括号", "(").replace("右括号", ")")
        .replace("和符号", "&").replace("撇号", "'").replace("杠符号", "-").replace("前书名号", "《").replace("后书名号", "》");
    return string;

  }

  /**
   * 更新记录
   *
   * @param record
   * @param namelist
   * @author LIJUN
   * @date 2018年3月20日
   */
  @Deprecated
  private void updateMatchedRecord(Long pubId, List<PdwhPubAddrInsRecord> records, Set<String> namelist) {
    for (String name : namelist) {
      // 更新被确认的人员记录时间
      List<PdwhPubAuthorSnsPsnRecord> list = pdwhPubAuthorSnsPsnRecordDao.findConfirmRecByPubIdAndName(pubId, name);// 查询该成果人员姓名是否被用户确认
      if (CollectionUtils.isEmpty(list)) {// 记录不存在执行保存流程
        for (PdwhPubAddrInsRecord record : records) {
          // this.saveMatchedRecord(record, name);
        }
      } else {
        for (PdwhPubAuthorSnsPsnRecord psnRecord : list) {
          pdwhPubAuthorSnsPsnRecordDao.updateTime(psnRecord.getId());// 更新时间
        }

      }
    }
  }

  /**
   * @param pubId
   * @param pbinsId
   * @param insName
   * @param matchName
   * @author LIJUN
   * @param pubMemberBean
   * @date 2018年3月20日
   */
  public void saveMatchedRecord(PdwhPubAddrInsRecord record, String matchName, PubMemberBean pubMemberBean) {
    Long pubId = record.getPubId();
    Long insId = record.getInsId();
    String insName = record.getInsName();
    Integer status = record.getStatus();// 成果地址状态，0默认，1被用户确认
    PubPdwhPO pubPdwh = pubPdwhDAO.get(pubId);
    List<PersonPmName> psnlist = personPmNameDao.getPsnByNameAndInsId(matchName, insId);// 获取匹配上的记录
    if (CollectionUtils.isEmpty(psnlist)) {
      logger.debug("根据人名获取人名常量表数据为空，请确认人名常量字典信息是否完整,pubId:" + pubId + "  name:" + matchName);
      return;
    }
    /**
     * pdwhPubAuthorSnsPsnRecord 状态 0 成果匹配不到sns人员，暂不记录<br>
     * 状态1.表示可信度低(仅人名匹配上)<br>
     * 2.表示可信度中（人名和未确认的地址），<br>
     * 3表示可信度最高（由用户成果认领确认或人名和确认的地址）。<br>
     * （地址状态信息PDWH_PUB_ADDR_INS_REcord表状态描述）
     **/
    Set<PdwhPubAuthorSnsPsnRecord> psnRecords = new HashSet<>();
    for (PersonPmName psn : psnlist) {
      // 如果匹配上的是全称，就需要PersonPmName表的name与成果作者一致
      if (1 == psn.getType()) {
        if (!psn.getName().equals(pubMemberBean.getName().toLowerCase())) {
          continue;
        }
      }
      List<Long> psnInsIdList = this.getPsnEduWorkInsIds(psn.getPsnId(), pubPdwh.getPublishYear());
      if (CollectionUtils.isNotEmpty(psnInsIdList) && psnInsIdList.contains(insId)) {
        // insId和人名匹配上
        PdwhPubAuthorSnsPsnRecord psnRecord =
            pdwhPubAuthorSnsPsnRecordDao.getPsnRecord(pubId, psn.getPsnId(), insId, matchName, psn.getType());
        if (psnRecord == null) {// 不存在匹配记录直接保存
          if (status == 1) {// 成果地址被确认
            PdwhPubAuthorSnsPsnRecord newRecord = new PdwhPubAuthorSnsPsnRecord(pubId, psn.getPsnId(), matchName, insId,
                insName, 3, new Date(), psn.getType());
            newRecord.setPubMemberName(pubMemberBean.getName());
            newRecord.setPubMemberId(pubMemberBean.getMemberId());
            psnRecords.add(newRecord);
          } else {
            PdwhPubAuthorSnsPsnRecord newRecord = new PdwhPubAuthorSnsPsnRecord(pubId, psn.getPsnId(), matchName, insId,
                insName, 2, new Date(), psn.getType());
            newRecord.setPubMemberName(pubMemberBean.getName());
            newRecord.setPubMemberId(pubMemberBean.getMemberId());
            psnRecords.add(newRecord);
          }
        }
      }
    }
    for (PdwhPubAuthorSnsPsnRecord pdwhPubAuthorSnsPsnRecord : psnRecords) {
      try {
        pdwhPubAuthorSnsPsnRecordDao.saveWithNewTransaction(pdwhPubAuthorSnsPsnRecord);
      } catch (Throwable e) {
        logger.error("PdwhPubAuthorSnsPsnRecord保存出现错误 ，listinfo={}", psnRecords.toString());

      }
    }

  }

  /**
   * @param pubYear
   * @return java.util.Set<java.lang.Long>
   * @Author LIJUN
   * @Description //TODO 查询人员的教育工作经历单位insids，包含person表insid
   * @Date 14:49 2018/7/31
   * @Param [psnId]
   **/
  public List<Long> getPsnEduWorkInsIds(Long psnId, Integer pubYear) {
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
}
