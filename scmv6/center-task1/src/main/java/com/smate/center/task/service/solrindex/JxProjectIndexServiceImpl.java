package com.smate.center.task.service.solrindex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.gdata.util.common.base.Joiner;
import com.smate.center.task.dao.pdwh.quartz.JxkjtPrpInfoTempDao;
import com.smate.center.task.dao.pdwh.quartz.JxstcPrpInfoTempDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProjectDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProjectOrgDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProjectPersonDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProposalDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProposalOrgDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProposalPersonDao;
import com.smate.center.task.model.pdwh.pub.JxkjtPrpInfoTemp;
import com.smate.center.task.model.pdwh.pub.JxstcPrpInfoTemp;
import com.smate.center.task.model.snsbak.bdsp.BdspProject;
import com.smate.center.task.model.snsbak.bdsp.BdspProjectOrg;
import com.smate.center.task.model.snsbak.bdsp.BdspProposal;
import com.smate.center.task.model.snsbak.bdsp.BdspProposalOrg;
import com.smate.core.base.utils.cache.CacheService;

@Service("jxProjectIndexService")
@Transactional(rollbackFor = Exception.class)
public class JxProjectIndexServiceImpl implements JxProjectIndexService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 1000;
  public static String INDEX_PRP_CODE = "prp_code_index";
  @Value("${solr.server.url}")
  private String serverUrl;
  private String runEnv = System.getenv("RUN_ENV");
  @Autowired
  private CacheService cacheService;
  @Autowired
  private JxstcPrpInfoTempDao jxstcPrpInfoTempDao;
  @Autowired
  private JxkjtPrpInfoTempDao jxkjtPrpInfoTempDao;
  @Autowired
  private BdspProjectDao bdspProjectDao;
  @Autowired
  private BdspProposalDao bdspProposalDao;
  @Autowired
  private BdspProposalOrgDao bdspProposalOrgDao;

  @Autowired
  private BdspProjectOrgDao bdspProjectOrgDao;
  @Autowired
  private BdspProposalPersonDao bdspProposalPersonDao;
  @Autowired
  private BdspProjectPersonDao bdspProjectPersonDao;

  @Override
  public void indexJxProject() {
    SolrServer server = new HttpSolrServer(serverUrl);
    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
    Long lastJxstcPrpCode = (Long) cacheService.get(INDEX_PRP_CODE, "last_jxstc_prp_code");
    Long lastjxkjtPrpCode = (Long) cacheService.get(INDEX_PRP_CODE, "last_jxkjt_prp_code");
    if (lastJxstcPrpCode == null && lastjxkjtPrpCode == null) {
      lastJxstcPrpCode = 0L;
    }
    List<JxkjtPrpInfoTemp> kjtPrpInfoList = new ArrayList<JxkjtPrpInfoTemp>();
    List<JxstcPrpInfoTemp> stcPrpInfoList = jxstcPrpInfoTempDao.getJxstcPrpInfoList(lastJxstcPrpCode, SIZE);
    if (CollectionUtils.isEmpty(stcPrpInfoList)) {
      if (lastjxkjtPrpCode == null) {
        lastjxkjtPrpCode = 0L;
      }
      kjtPrpInfoList = jxkjtPrpInfoTempDao.getJxkjtPrpInfoList(lastjxkjtPrpCode, SIZE);
      if (CollectionUtils.isEmpty(kjtPrpInfoList)) {
        return;
      }
    }
    if (CollectionUtils.isNotEmpty(stcPrpInfoList)) {
      cacheService.put(INDEX_PRP_CODE, 60 * 60 * 24, "last_jxstc_prp_code",
          stcPrpInfoList.get(stcPrpInfoList.size() - 1).getPrpCode());
      for (JxstcPrpInfoTemp jxstcPrpInfoTemp : stcPrpInfoList) {
        BdspProposal dspPps = bdspProposalDao.getBdspPps(jxstcPrpInfoTemp.getPrpCode());
        if (dspPps == null) {
          continue;
        }
        SolrInputDocument doc = new SolrInputDocument();
        // 必须字段设定schema.xml配置,配置id时需将其与其他id区分开，以5开头
        doc.setField("businessType", INDEX_PRP_CODE);
        Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
        doc.setField("env", runEnv);
        doc.setField("id", Long.parseLong("2000000" + jxstcPrpInfoTemp.getPrpCode().toString()));
        doc.setField("jxPrpCode", jxstcPrpInfoTemp.getPrpCode());
        doc.setField("jxSummary", jxstcPrpInfoTemp.getSummary());
        doc.setField("jxZhTitle", jxstcPrpInfoTemp.getZhTitle());
        doc.setField("jxKeyWords", jxstcPrpInfoTemp.getKeyWords());
        doc.setField("jxPrpNo", dspPps.getPrpNo());// 项目编号
        doc.setField("jxReqAmt", dspPps.getReqAmt().toString());// 立项金额
        List<BdspProposalOrg> dspPpsOrgs = bdspProposalOrgDao.getDspPpsOrg(jxstcPrpInfoTemp.getPrpCode());
        List<String> orgZhNames = new ArrayList<String>();
        List<Long> provinceIds = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(dspPpsOrgs)) {
          for (BdspProposalOrg bdspProposalOrg : dspPpsOrgs) {
            orgZhNames.add(bdspProposalOrg.getOrgZhName());
            provinceIds.add(bdspProposalOrg.getProvinceId());
          }
        }
        doc.setField("jxOrgName", Joiner.on(",").join(orgZhNames));// 承担单位
        doc.setField("jxProvinceId", Joiner.on(",").join(provinceIds));// 省份
        List<String> dspPpsPsnNames = bdspProposalPersonDao.getDspPpsPsn(jxstcPrpInfoTemp.getPrpCode());
        doc.setField("jxPsnName", dspPpsPsnNames.toString());// 项目负责人
        doc.setField("jxStatYear", dspPps.getStatYear());// 申请年度
        doc.setField("grantName", dspPps.getZhGrantName());// 资助类别
        docList.add(doc);
      }

    } else {
      cacheService.put(INDEX_PRP_CODE, 60 * 60 * 24, "last_jxkjt_prp_code",
          kjtPrpInfoList.get(kjtPrpInfoList.size() - 1).getPrpCode());
      for (JxkjtPrpInfoTemp jxkjtPrpInfoTemp : kjtPrpInfoList) {
        BdspProject bdspPrj = bdspProjectDao.getBdspPrj(jxkjtPrpInfoTemp.getPrpCode());
        if (bdspPrj == null) {
          continue;
        }
        SolrInputDocument doc = new SolrInputDocument();
        // 必须字段设定schema.xml配置,配置id时需将其与其他id区分开，以5开头
        doc.setField("businessType", INDEX_PRP_CODE);
        Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
        doc.setField("env", runEnv);
        doc.setField("id", Long.parseLong("3000000" + jxkjtPrpInfoTemp.getPrpCode().toString()));
        doc.setField("jxPrpCode", jxkjtPrpInfoTemp.getPrpCode());
        doc.setField("jxSummary", jxkjtPrpInfoTemp.getSummary());
        doc.setField("jxZhTitle", jxkjtPrpInfoTemp.getZhTitle());
        doc.setField("jxKeyWords", jxkjtPrpInfoTemp.getKeyWords());

        doc.setField("jxPrpNo", bdspPrj.getPrpNo());// 项目编号
        doc.setField("jxReqAmt", bdspPrj.getReqAmt().toString());// 立项金额
        List<BdspProjectOrg> dspPrjOrgs = bdspProjectOrgDao.getDspPrjOrg(jxkjtPrpInfoTemp.getPrpCode());
        List<String> orgZhNames = new ArrayList<String>();
        List<Long> provinceIds = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(dspPrjOrgs)) {
          for (BdspProjectOrg bdspProjectOrg : dspPrjOrgs) {
            orgZhNames.add(bdspProjectOrg.getOrgZhName());
            provinceIds.add(bdspProjectOrg.getProvinceId());
          }
        }
        doc.setField("jxOrgName", Joiner.on(",").join(orgZhNames));// 承担单位
        doc.setField("jxProvinceId", Joiner.on(",").join(provinceIds));// 省份
        List<String> dspPrjPsnNames = bdspProjectPersonDao.getDspPrjPsn(jxkjtPrpInfoTemp.getPrpCode());
        doc.setField("jxPsnName", dspPrjPsnNames.toString());// 项目负责人
        doc.setField("jxStatYear", bdspPrj.getStatYear());// 申请年度
        doc.setField("grantName", bdspPrj.getZhGrantName());// 资助类别
        docList.add(doc);
      }

    }
    if (CollectionUtils.isNotEmpty(docList)) {
      try {
        Date start = new Date();
        server.add(docList);
        server.commit();
        Date end = new Date();
        logger.info("jxProject索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("jxProject索引创建出错，end = " + new Date());
      }
    }
  }

}
