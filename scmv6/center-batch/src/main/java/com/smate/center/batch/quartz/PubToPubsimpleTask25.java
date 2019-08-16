package com.smate.center.batch.quartz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ansj.library.DicLibrary;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.gdata.util.common.base.StringUtil;
import com.smate.center.batch.model.pdwh.pub.PubKeywordsSubsetsHntTmp;
import com.smate.center.batch.model.sns.pub.ProjectDataFiveYear;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pub.ProjectDataFiveYearService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;

public class PubToPubsimpleTask25 {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 1000; // 每次刷新获取的个数

  @Autowired
  private ProjectDataFiveYearService projectDataFiveYearService;

  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private CacheService cacheService;
  @Value("${solr.server.url}")
  private String serverUrl;

  public void run() throws BatchTaskException {
    logger.debug("====================================PubToPubsimpleTask25===开始运行");
    if (isRun() == false) {
      logger.debug("====================================PubToPubsimpleTask25===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("PubToPubsimpleTask25,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    if (taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask_removePubCache6") == 1) {
      cacheService.remove("PubToPubsimpleTask25_1", "last_pub_id");
      cacheService.remove("PubToPubsimpleTask25_1", "progress");
    }
    List<PubKeywordsSubsetsHntTmp> categoryList = this.projectDataFiveYearService.getCategory(5);

    if (categoryList == null || categoryList.size() == 0) {
      logger.info("高新产业关键词co-tf计算完成！");
      return;
    }

    for (PubKeywordsSubsetsHntTmp category : categoryList) {
      try {
        if (taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask_removePubCache6") == 1) {
          cacheService.remove("PubToPubsimpleTask25_1", "last_pub_id");
          cacheService.remove("PubToPubsimpleTask25_1", "progress");
        }
        Long lastPubId = (Long) cacheService.get("PubToPubsimpleTask25_1", "last_pub_id");
        Long total = this.projectDataFiveYearService.getTotalCounts();
        if (lastPubId == null) {
          lastPubId = 0L;
        }
        while (true) {
          List<ProjectDataFiveYear> dataList =
              projectDataFiveYearService.getProjectDataList(SIZE, lastPubId, category.getId());
          if (dataList == null || dataList.size() == 0) {
            logger.info("PubToPubsimpleTask6已经完成");
            break;
          }
          lastPubId = dataList.get(dataList.size() - 1).getId();
          this.cacheService.put("PubToPubsimpleTask25_1", 60 * 60 * 24, "last_pub_id", lastPubId);
          int j = 0;
          for (ProjectDataFiveYear projectData : dataList) {
            try {
              SolrServer server = initializeSolrServerForTfCotf();
              // String applicationCode =
              // projectData.getApplicationCode();
              String approveCode = projectData.getApproveCode();
              /*
               * if (approveCode.equals("51379039")) { logger.info("============================project_id: " +
               * approveCode); }
               */
              logger.info("============================project_id: " + approveCode);
              String content = StringUtils.trimToEmpty(projectData.getZhTitle())
                  + StringUtils.trimToEmpty(projectData.getZhAbstract())
                  + StringUtils.trimToEmpty(projectData.getZhKeywords());

              Map<Integer, Map<Integer, String>> zhContentMap = null;
              if (!StringUtil.isEmpty(content)) {
                // 全角转半角
                String zhContent = XmlUtil.changeSBCChar(content);
                /*
                 * zhContentMap = projectDataFiveYearService.getKwSubsetsFromPubContent(zhContent);
                 * this.projectDataFiveYearService.saveAllSubsetsHnt(zhContentMap, projectData.getApplicationCode(),
                 * approveCode);
                 */
                projectDataFiveYearService.saveKwStrToSolr(server, zhContent, projectData.getApplicationCode(),
                    approveCode);
                // server.commit();
              }
            } catch (Exception e) {
              logger.error("PubToPubsimpleTask25_1 -- 关键词组合出错  pubid ：" + projectData.getId(), e);
            }
            j++;
          }
          Integer progress = (Integer) this.cacheService.get("PubToPubsimpleTask25_1", "progress");
          if (progress == null) {
            progress = 0;
          }
          progress = progress + j;
          this.cacheService.put("PubToPubsimpleTask25_1", 60 * 60 * 24, "progress", progress);
          logger.info("=======================================================");
          logger.info("PubToPubsimpleTask25_1 已经完成== " + progress + "/" + total + " ==");
          logger.info("=======================================================");
        }
        category.setStatus(1);
        this.projectDataFiveYearService.saveRs(category);
      } catch (Exception e) {
        category.setStatus(3);
        this.projectDataFiveYearService.saveRs(category);
        logger.error("出错", e);
      }
    }
  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    // return true;
    return taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask27") == 1;
  }

  private SolrServer initializeSolrServerForTfCotf() {
    String newServerCollectionUrl = serverUrl + "collection_tf_cotf";
    SolrServer server = new HttpSolrServer(newServerCollectionUrl);
    return server;
  }

  private void loadDic(List<String> dicStrs) {
    try {
      // Forest forest = Library.makeForest(dicPath);
      // Result rs = NlpAnalysis.parse(testStr, forest);
      // Result rs = DicAnalysis.parse(testStr, forest);
      for (String kw : dicStrs) {
        DicLibrary.insert(DicLibrary.DEFAULT, kw);
      }
      /*
       * Result rs = NlpAnalysis.parse(testStr); Iterator<Term> it = rs.iterator(); while (it.hasNext()) {
       * Term t = it.next(); System.out.println(t.getName() + ": " + t.getNatureStr()); }
       */

    } catch (Exception e2) {
      // TODO Auto-generated catch block
      e2.printStackTrace();
    }
  }

  private List<String> getNsfcKws() throws Exception {
    File file = new File("C:\\Users\\Administrator\\Desktop\\ML\\新建文件夹\\nsfc_kw_discipline.txt");
    FileInputStream fis = new FileInputStream(file);
    InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
    BufferedReader br = new BufferedReader(isr);
    List<String> strList = new ArrayList<String>();
    while (StringUtils.isNotEmpty(br.readLine())) {
      strList.add(br.readLine());
    }
    fis.close();
    isr.close();
    br.close();
    return strList;
  }

  public static void main(String[] args) {
    String pattern = "\\([^)]*\\)";
    String kw = "国际天文学联合会（iau）";
    String kw1 = "恒星初始质量函数(imf)";
    String kw2 = "co2(ch4)-h2o-nacl";
    String kw4 = "l(2";
    kw = XmlUtil.changeSBCChar(kw4);
    kw = kw.replaceAll(pattern, "");
    System.out.println(kw);
  }
}
