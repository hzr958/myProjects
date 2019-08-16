package com.smate.center.data.service.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.data.dao.pub.HadoopPrjKeywordDao;
import com.smate.center.data.dao.pub.HadoopPrjKeywordsDao;
import com.smate.center.data.dao.pub.ProjectDataFiveYearDao;
import com.smate.center.data.model.hadoop.pub.HKeywordsItem;
import com.smate.center.data.model.pub.HadoopPrjKeyword;
import com.smate.center.data.model.pub.HadoopPrjKeywords;
import com.smate.center.data.model.pub.ProjectDataFiveYear;
import com.smate.center.data.utils.MessageDigestUtils;

@Service("projectDataFiveYearService")
@Transactional(rollbackFor = Exception.class)
public class ProjectDataFiveYearServiceImpl implements ProjectDataFiveYearService {
  @Autowired
  private ProjectDataFiveYearDao projectDataFiveYearDao;
  @Autowired
  private HadoopPrjKeywordDao hadoopPrjKeywordDao;
  @Autowired
  private HadoopPrjKeywordsDao hadoopPrjKeywordsDao;

  @Override
  public List<ProjectDataFiveYear> getProjectDataList(Integer size, Long startId, Long endId) {
    return projectDataFiveYearDao.getProjectDataList(size, startId, endId);
  }

  @Override
  public Set<String> handlePubKeywords(String keywords) {
    TreeSet<String> treeSet = new TreeSet<String>();
    String[] keywordsString = keywords.replace("；", ";").toLowerCase().split(";");
    for (String keyword : keywordsString) {
      String kw = keyword.replaceAll("\\s+", " ").trim();
      treeSet.add(MessageDigestUtils.messageDigest(kw));
    }
    return treeSet;
  }

  @Override
  public StringBuilder conbinePubKeywords(String applicationCode, Set<String> keywordsSet, StringBuilder strBuilder) {
    if (keywordsSet != null && keywordsSet.size() > 0) {
      List<String> zhkeywordsList = new ArrayList<>(keywordsSet);
      for (int i = 0; i < zhkeywordsList.size() - 1; i++) {
        strBuilder.append(MessageDigestUtils.messageDigest(applicationCode + zhkeywordsList.get(i)));
        strBuilder.append(" ");
        for (int j = i + 1; j < zhkeywordsList.size(); j++) {
          strBuilder.append(
              MessageDigestUtils.messageDigest(applicationCode + zhkeywordsList.get(i) + zhkeywordsList.get(j)));
          strBuilder.append(" ");
        }
      }
      strBuilder.append(
          MessageDigestUtils.messageDigest(applicationCode + zhkeywordsList.get(zhkeywordsList.size() - 1).toString()));
      strBuilder.append(" ");
    }
    return strBuilder;
  }

  @Override
  public void saveData(List<HKeywordsItem> allList) throws Exception {
    for (HKeywordsItem item : allList) {
      HadoopPrjKeyword hk = new HadoopPrjKeyword();
      hk.setKeyword(item.getKey1());
      hk.setCounts((new Double(item.getKey1Val())).intValue());
      hk.setApproveCode(item.getcId());
      hadoopPrjKeywordDao.save(hk);
      HadoopPrjKeyword hk2 = new HadoopPrjKeyword();
      hk2.setKeyword(item.getKey2());
      hk2.setCounts((new Double(item.getKey2Val())).intValue());
      hk2.setApproveCode(item.getcId());
      hadoopPrjKeywordDao.save(hk2);
      HadoopPrjKeywords hks = new HadoopPrjKeywords();
      hks.setKeywords(item.getKey1() + ";" + item.getKey2());
      hks.setCounts((new Double(item.getKey12Val())).intValue());
      hks.setApproveCode(item.getcId());
      hadoopPrjKeywordsDao.save(hks);
    }

  }

}
