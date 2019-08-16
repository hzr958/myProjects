package com.smate.center.batch.quartz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.library.DicLibrary;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.center.batch.service.pub.GenerateAddrPsnConstDicService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;

public class PubToPubsimpleTask27 {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 1000; // 每次刷新获取的个数

  @Autowired
  private NsfcKeywordsService nsfcKeywordsService;
  @Autowired
  private GenerateAddrPsnConstDicService generateAddrPsnConstDicService;

  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private CacheService cacheService;

  @Value("${solr.server.url}")
  private String serverUrl;

  public void run() throws BatchTaskException {
    logger.debug("====================================PubToPubsimpleTask27===开始运行");
    if (isRun() == false) {
      logger.debug("====================================PubToPubsimpleTask27===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("pubToPubsimpleTask__6,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    // SIZE
    List<String> pdwhPubIdList = nsfcKeywordsService.getNsfcCategoryToHandleKwList(10, SIZE);
    if (pdwhPubIdList == null || pdwhPubIdList.size() == 0) {
      logger.info("====================================CotfByNstfPrjTask===运行完毕");
      return;
    }
    for (String category : pdwhPubIdList) {
      Integer rs = 0;
      try {
        // 先算中文
        this.generateAddrPsnConstDicService.generateNsfcKwsDicByCategory(category);
        rs = this.nsfcKeywordsService.getKwSubsetsCotf(category, 1);
        this.nsfcKeywordsService.updateNsfcCategoryStatus(category, rs);
      } catch (Exception e) {
        logger.error("nsfc项目计算项目cotf错误,category:" + category, e);
        this.nsfcKeywordsService.updateNsfcCategoryStatus(category, rs);
      }
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return true;
    // return taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask27") == 1;
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
