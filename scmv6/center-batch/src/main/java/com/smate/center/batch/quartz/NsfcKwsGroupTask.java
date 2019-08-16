package com.smate.center.batch.quartz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.center.batch.service.pub.PdwhPubKeywordsService;
import com.smate.center.batch.service.pub.PdwhPubKeywordsServiceImpl;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 按照计算好的知识库对关键词分组
 * 
 */
public class NsfcKwsGroupTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 2000; // 每次刷新获取的个数

  @Autowired
  private NsfcKeywordsService nsfcKeywordsService;
  @Autowired
  private PdwhPubKeywordsService pdwhPubKeywordsService;

  public void run() throws BatchTaskException {
    logger.debug("====================================KGPubKwsExtractTask===开始运行");
    if (isRun() == false) {
      logger.debug("====================================KGPubKwsExtractTask===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("KGPubKwsExtractTask,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    // 按从高到低tf排序关键词序列
    List<String> kwListTfDesc = nsfcKeywordsService.getGrpToHandleKwList();
    Map<Integer, List<String>> groupMp = new HashMap<Integer, List<String>>();
    // kwList是已经按
    Integer i = 1;
    Integer length = 0;
    Integer ListSize = kwListTfDesc.size();
    while (true) {
      List<String> groupedKws = new ArrayList<String>();
      // 每次从建立新list，用于排序后，计算子集；kwListTfDesc保留关键词tf排序顺序
      TreeSet<String> TwoSubsets = pdwhPubKeywordsService.getGroupKwsByCotfTwo(new ArrayList<String>(kwListTfDesc),
          kwListTfDesc.get(0), groupedKws, 0, null);
      /*
       * TreeSet<String> TwoSubsets = pdwhPubKeywordsService.getSubsetsLengthTwo(new
       * ArrayList<String>(kwListTfDesc), kwListTfDesc.get(0), groupedKws);
       */
      if (groupedKws.size() > 1) {
        groupMp.put(i, groupedKws);
        i++;
      } else {
        if (groupMp.get(99999) == null) {
          groupMp.put(99999, groupedKws);
          i++;
        } else {
          List<String> otherKws = groupMp.get(99999);
          otherKws.addAll(groupedKws);
          groupMp.put(99999, otherKws);
        }
      }
      length = length + groupedKws.size();
      if (length >= ListSize) {
        break;
      }
      kwListTfDesc.removeAll(groupedKws);
    }
    for (Entry<Integer, List<String>> et : groupMp.entrySet()) {
      System.out.println("===第" + et.getKey() + "组关键词===，关键词词数：" + et.getValue().size());
      System.out.println(et.getValue());
    }
  }


  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return true;
  }

  public static void main(String[] args) throws IOException {
    String path = "E:/讨论PPT/关键词分组demo.txt";
    String kwStr = "";
    String kwStrAll = "";
    File file = new File(path);
    FileReader fr = new FileReader(file);
    BufferedReader br = new BufferedReader(fr);
    while (StringUtils.isNotEmpty(kwStr = br.readLine())) {
      kwStrAll = kwStrAll + kwStr;
    }
    String[] kwCo = kwStrAll.split(";");
    Set<String> kwSet = new TreeSet<String>();
    List<String> kwList = new ArrayList<String>();
    for (String kw : kwCo) {
      if (StringUtils.isNotEmpty(StringUtils.trim(kw))) {
        kwSet.add(StringUtils.trim(kw));
      }
    }
    for (String str : kwSet) {
      kwList.add(str);
    }
    Collections.sort(kwList);
    PdwhPubKeywordsServiceImpl pps = new PdwhPubKeywordsServiceImpl();
    String startKw = "风险度量";
    List<String> groupedKws = new ArrayList<String>();
    TreeSet<String> TwoSubsets = pps.getSubsetsLengthTwo(kwList, startKw, groupedKws);
    System.out.println(groupedKws);
    TreeSet<String> ThreeSubsets = pps.getSubsets(groupedKws, 3);
    System.out.println(ThreeSubsets.size());
  }
}
