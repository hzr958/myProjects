package com.smate.center.task.service.pdwh.quartz;

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
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.pdwh.quartz.PdwhInsAddrConstDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubAddrInsRecordDao;
import com.smate.center.task.dao.pdwh.quartz.PubFundingInfoDao;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.dao.sns.quartz.InstitutionDao;
import com.smate.center.task.model.pdwh.pub.PdwhInsAddrConst;
import com.smate.center.task.model.pdwh.pub.PdwhPubAddrInsRecord;
import com.smate.center.task.model.pdwh.quartz.PubFundingInfo;
import com.smate.center.task.v8pub.dao.pdwh.PdwhMemberEmailDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

@Service("PdwhPublicationService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPublicationServiceImpl implements PdwhPublicationService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static Integer jobType = TaskJobTypeConstants.pdwhPubAssignInsTask;
  @Autowired
  private PdwhInsAddrConstDao pdwhInsAddrConstDao;
  @Autowired
  private PdwhPubAddrInsRecordDao pdwhPubAddrInsRecordDao;

  @Autowired
  private PubFundingInfoDao pubFundingInfoDao;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Value("${extpdwh.custom.dicpath}")
  private String dicRootPath;
  @Autowired
  private PdwhMemberEmailDAO pdwhMemberEmailDAO;
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  @Autowired
  private InstitutionDao institutionDao;

  @Override
  public List<PubPdwhDetailDOM> getPdwhPubIds(Long lastPubId, int batchSize) {
    return pubPdwhDetailDAO.getPdwhPubIds(lastPubId, batchSize);
  }


  @Override
  public void savePubFundingInfo(PubFundingInfo pubFundingInfo) {
    if (pubFundingInfoDao.get(pubFundingInfo.getPubId()) == null) {
      pubFundingInfoDao.save(pubFundingInfo);
    }
  }

  /**
   * 对成果单位进行分词匹配,字符将会被替换后匹配
   * 
   * @author LIJUN
   * @throws IOException
   * @date 2018年3月26日
   */
  @Override
  public void segmentPubOrg(Long pubId, String addrs) throws Exception {

    pdwhPubAddrInsRecordDao.deleteUnconfirmedRecord(pubId);
    Map<String, Set<String>> extractInsName = this.getExtractInsName(addrs);
    Set<String> addrlist = extractInsName.get("scm_ins_name");
    if (CollectionUtils.isEmpty(addrlist)) {
      return;
    }

    this.saveMatchedAddr(pubId, addrlist);

  }

  /**
   * @Author LIJUN
   * @Description //TODO 从词典提取单位
   * @Date 10:49 2018/7/19
   * @Param [str]
   * @return java.util.Map<java.lang.String,java.util.Set<java.lang.String>>
   **/
  public Map<String, Set<String>> getExtractInsName(String str) throws Exception {
    if (org.apache.commons.lang3.StringUtils.isEmpty(str)) {
      return null;
    }
    str = replaceChars(str);
    // 直接使用，在服务器启动时加载
    Result kwRs = DicAnalysis.parse(str);
    Set<String> ins = new TreeSet<String>();
    Map<String, Set<String>> mp = new HashMap<String, Set<String>>();
    for (Term t : kwRs.getTerms()) {
      if (t == null) {
        continue;
      }

      if ("scm_ins_name".equals(t.getNatureStr())) {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(t.getName())) {
          ins.add(resetChars(t.getName()));
        }
      }
    }
    if (ins.size() > 0) {
      mp.put("scm_ins_name", ins);
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
  public static String resetChars(String string) {
    string = string.replace("空格", " ").replace("逗号", ",").replace("点符号", ".").replace("左括号", "(").replace("右括号", ")")
        .replace("和符号", "&").replace("撇号", "'").replace("杠符号", "-").replace("前书名号", "《").replace("后书名号", "》");
    return string;

  }


  /**
   * 更新对应关系
   * 
   * @param pubId
   * @param addrlist
   * @author LIJUN
   * @date 2018年3月20日
   */
  public void saveMatchedAddr(Long pubId, Set<String> addrlist) {
    Set<PdwhPubAddrInsRecord> matchedAddrlist = new HashSet<>();// 临时保存匹配上的单位名，用于去除重复数据后保存

    for (String addr : addrlist) {
      Long addrHash = PubHashUtils.cleanPubAddrHash(addr);
      List<PdwhInsAddrConst> insInfo = pdwhInsAddrConstDao.getInsInfoByNameHash(addrHash);
      if (CollectionUtils.isEmpty(insInfo)) {
        logger.info("PdwhInsAddrConst地址常量表查询不到对应单位，请检查分词器单位字典数据是否完整！pubId:" + pubId + " addr:" + addr);
        continue;
      }
      /**
       * 去除重复的单位，防止单位常量表有重复数据
       */
      Set<PdwhInsAddrConst> insMatchList = new HashSet<>();
      List<Long> ids = new ArrayList<>();// 用于排除ins_id相同的单位
      for (PdwhInsAddrConst pdwhInsAddrConst : insInfo) {
        /**
         * //重写了PdwhInsAddrConst equals 方法，ins_id 和ins_name_hash相同则是唯一记录
         */
        if (!ids.contains(pdwhInsAddrConst.getInsId())) {
          insMatchList.add(pdwhInsAddrConst);
        }

      }
      /**
       * 单位地址常量表存在一个单位名对应了几个ins_id的情况，都保存
       */
      for (PdwhInsAddrConst pdwhInsAddrConst : insMatchList) {
        Long constId = pdwhInsAddrConst.getConstId();
        Long insId = pdwhInsAddrConst.getInsId();
        List<PdwhPubAddrInsRecord> list = pdwhPubAddrInsRecordDao.findRecByPubIdAndInsId(pubId, insId);

        if (CollectionUtils.isEmpty(list)) {
          // 记录不存在执行保存流程
          /**
           * 单位匹配记录表，pub_id 和ins_id为唯一记录
           */
          Long regionId = institutionDao.findInsRegionId(insId);
          PdwhPubAddrInsRecord insRecord =
              new PdwhPubAddrInsRecord(constId, pubId, insId, addr, addrHash, 0, new Date(), regionId);
          matchedAddrlist.add(insRecord);

        } else {// 更新时间
          if (list.size() > 1) {
            logger.error("PdwhPubAddrInsRecord中有重复数据，请确认！pubid:" + pubId);
          }
          pdwhPubAddrInsRecordDao.updateTime(constId);// 更新时间
        }

      }

    }
    if (CollectionUtils.isNotEmpty(matchedAddrlist)) {
      for (PdwhPubAddrInsRecord pdwhPubAddrInsRecord : matchedAddrlist) {
        try {
          pdwhPubAddrInsRecordDao.saveWithNewTransaction(pdwhPubAddrInsRecord);
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
          logger.error("PdwhPubAddrInsRecord保存失败，可能已经存在该记录,pubId={},insId={},list={}", pdwhPubAddrInsRecord.getPubId(),
              pdwhPubAddrInsRecord.getInsId(), matchedAddrlist.toString());
        } catch (Exception e) {
          logger.error("PdwhPubAddrInsRecord保存出错,pubId={},insId={},list={}", pdwhPubAddrInsRecord.getPubId(),
              pdwhPubAddrInsRecord.getInsId(), matchedAddrlist.toString(), e);
        }
      }
    }
    // SCM-18085
    // pdwhPublicationDao.updatePubUpdateTime(pubId);

  }



  @Override
  public List<Long> getNeedAssignPubIds(Long pubId, Integer size) {
    return pdwhPubAddrInsRecordDao.getNeedAssignPubIds(pubId, size);
  }



  @Override
  public PubPdwhDetailDOM getNeedAssignPub(Long pdwhPubId) {
    return pubPdwhDetailDAO.findById(pdwhPubId);
  }

  @Override
  public List<String> getPubMemberEmailList(Long pdwhPubId) {

    return pdwhMemberEmailDAO.getPubMemberEmailList(pdwhPubId);
  }

  @Override
  public List<Long> getbatchhandleIdList(Integer size) throws Exception {
    return tmpTaskInfoRecordDao.getbatchhandleIdList(size, jobType);
  }

  @Override
  public void updateTaskStatus(Long pdwhPubId, int status, String errMsg) throws Exception {
    tmpTaskInfoRecordDao.updateTaskStatus(pdwhPubId, status, errMsg, jobType);
  }



  @Override
  public List<Long> getTmpBatchhandleIdList(Long startHandleId, Long endHandleId) {
    return tmpTaskInfoRecordDao.getTmpBatchhandleIdList(startHandleId, endHandleId, jobType);
  }

}
