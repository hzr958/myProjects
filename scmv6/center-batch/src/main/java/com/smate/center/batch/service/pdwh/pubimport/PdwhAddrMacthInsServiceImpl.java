package com.smate.center.batch.service.pdwh.pubimport;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.ansj.library.DicLibrary;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.PdwhInsAddrConstDao;
import com.smate.center.batch.dao.pdwh.pub.PdwhPubAddrInsRecordDao;
import com.smate.center.batch.dao.sns.institution.InstitutionDao;
import com.smate.center.batch.model.pdwh.pub.PdwhInsAddrConst;
import com.smate.center.batch.model.pdwh.pub.PdwhPubAddrInsRecord;
import com.smate.center.batch.util.pub.AuthorNameUtils;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;

/**
 * 基准库成果地址与地址常量表匹配任务业务实现
 * 
 * @author LIJUN
 * @date 2018年3月15日
 */
@Service("pdwhAddrMacthInsService")
@Transactional(rollbackFor = Exception.class)
public class PdwhAddrMacthInsServiceImpl implements PdwhAddrMacthInsService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhInsAddrConstDao pdwhInsAddrConstDao;
  @Autowired
  private PdwhPubAddrInsRecordDao pdwhPubAddrInsRecordDao;
  @Autowired
  private InstitutionDao institutionDao;


  @Override
  public void startMatchInsName(Set<String> orgstrs, Long pubId, String context) throws Exception {
    // 删除没有被确认的地址
    pdwhPubAddrInsRecordDao.deleteUnconfirmedRecord(pubId);
    String addrs = HtmlUtils.Html2Text(String.join(" ", orgstrs).trim());
    // TODO 英文地址处理 对于分词工具暂没有合适方法 暂时采用替换英文中" " 为空格中文字符解决
    logger.info("-----------------------------处理后的单位地址" + addrs);
    this.segmentStr(pubId, AuthorNameUtils.replaceChars(addrs));

  }


  /**
   * 分词匹配
   * 
   * @author LIJUN
   * @throws IOException
   * @date 2018年3月26日
   */
  public void segmentStr(Long pubId, String addrs) throws Exception {
    long t1 = System.currentTimeMillis();
    Map<String, Set<String>> extractInsName = this.getExtractInsName(addrs);
    Set<String> addrlist = extractInsName.get("scm_ins_name");
    long t2 = System.currentTimeMillis();
    logger.info(pubId + "单位长度：" + (StringUtils.isBlank(addrs) ? 0 : addrs.length()) + "分词用时:" + (t2 - t1));
    if (CollectionUtils.isEmpty(addrlist)) {
      return;
    }
    this.saveMatchedAddr(pubId, addrlist);
    long t3 = System.currentTimeMillis();
    logger.info(pubId + "单位长度：" + (StringUtils.isBlank(addrs) ? 0 : addrs.length()) + "匹配保存单位用时:" + (t3 - t2));
  }

  /**
   * @Author LIJUN
   * @Description //TODO 从词典提取单位
   * @Date 10:49 2018/7/19
   * @Param [str]
   * @return java.util.Map<java.lang.String,java.util.Set<java.lang.String>>
   **/
  @Override
  public Map<String, Set<String>> getExtractInsName(String str) throws Exception {
    if (StringUtils.isEmpty(str)) {
      return null;
    }
    str = AuthorNameUtils.replaceChars(str);
    // 直接使用，在服务器启动时加载
    Result kwRs = DicAnalysis.parse(str);
    logger.error("------------------------------词典包换的信息" + kwRs);
    Set<String> ins = new TreeSet<String>();
    Map<String, Set<String>> mp = new HashMap<String, Set<String>>();
    for (Term t : kwRs.getTerms()) {
      if (t == null) {
        continue;
      }

      if ("scm_ins_name".equals(t.getNatureStr())) {
        logger.info("-----------------------------scm_ins_name:" + t.getNatureStr());
        if (StringUtils.isNotEmpty(t.getName())) {
          /* logger.error("----------------------------匹配到的单个地址:" + t.getName()); */
          ins.add(AuthorNameUtils.resetChars(t.getName()));
        }
      }
    }
    if (ins.size() > 0) {
      logger.info("---------------------------匹配到的单位地址" + ins);
      mp.put("scm_ins_name", ins);
    }
    return mp;
  }

  /**
   * 保存单位匹配对应关系
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
        logger.error("PdwhInsAddrConst地址常量表查询不到对应单位，请检查分词器单位字典数据是否完整！pubId:" + pubId + " addr:" + addr);
        continue;
      }

      Set<PdwhInsAddrConst> insMatchList = new HashSet<>();
      /**
       * 去除重复的单位，防止单位常量表有重复数据
       */
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
          Long regionId = institutionDao.getRegionIdByInsId(insId);
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
          pdwhPubAddrInsRecordDao.save(pdwhPubAddrInsRecord);
        } catch (Throwable e) {
          logger.error("PdwhPubAddrInsRecord保存出错,pubId={},insId={},list={}", pdwhPubAddrInsRecord.getPubId(),
              pdwhPubAddrInsRecord.getInsId(), matchedAddrlist.toString(), e);
        }
      }
    }
    // SCM-18085
    // pdwhPublicationDao.updatePubUpdateTime(pubId);

  }

  public static void main(String[] args) {
    System.out.println("=========Nsfc人员与机构关键词词典开始加载=========");
    String addrdic = "D:/home/smate/files/pdwhdic/addr/1118.dic";
    String psndic = "D:/home/smate/files/pdwhdic/psn/1548000300004.dic";


    File file = new File(addrdic);
    FileInputStream ris = null;
    InputStreamReader isr = null;
    BufferedReader br = null;
    if (file.exists()) {
      try {
        DicLibrary.clear(DicLibrary.DEFAULT);
        ris = new FileInputStream(file);
        isr = new InputStreamReader(ris, "UTF-8");
        br = new BufferedReader(isr);
        String fileStr = null;
        while (StringUtils.isNotBlank(fileStr = br.readLine())) {
          DicLibrary.insert(DicLibrary.DEFAULT, fileStr.toLowerCase().trim(), "scm_ins_name", DicLibrary.DEFAULT_FREQ);
        }
      } catch (Exception e) {
        System.out.println("人员检索提取人名，加载个人词典出错");
      } finally {
        try {
          ris.close();
          isr.close();
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      System.out.println("人员检索提取人名，加载个人词典完成");
    } else {
      System.out.println("人员检索提取人名，个人词典文件未发现");
    }

    File fileIns = new File(psndic);
    if (fileIns.exists()) {
      try {
        ris = new FileInputStream(fileIns);
        isr = new InputStreamReader(ris, "UTF-8");
        br = new BufferedReader(isr);
        String fileStr1 = null;
        while (StringUtils.isNotBlank(fileStr1 = br.readLine())) {
          DicLibrary.insert(DicLibrary.DEFAULT, fileStr1.toLowerCase().trim(), "scm_user_name",
              DicLibrary.DEFAULT_FREQ);
        }
      } catch (Exception e) {
        System.out.println("人员检索提取人名，加载单位名词典出错: " + e.getMessage());
      } finally {
        try {
          ris.close();
          isr.close();
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      System.out.println("人员检索提取人名，加载单位名词典完成");
    } else {
      System.out.println("人员检索提取人名，单位名词典文件未发现");
    }
    Long addrHash = PubHashUtils.cleanPubAddrHash("Hefei Univ Technol");
    PdwhAuthorMatchPersonServiceImpl pa = new PdwhAuthorMatchPersonServiceImpl();
    PdwhAddrMacthInsServiceImpl pi = new PdwhAddrMacthInsServiceImpl();
    String org =
        "Zhongkai Univ Agr &amp; Engn, Sch Environm Sci &amp; Engn, Guangzhou 510225, Guangdong, Peoples R China ";
    String name = null;
    name = HtmlUtils.Html2Text(org.trim());
    try {

      Map<String, Set<String>> addr = pi.getExtractInsName(AuthorNameUtils.replaceChars(name));
      Map<String, Set<String>> user = pa.getExtractUserName(name);
      System.out.println(addr);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
