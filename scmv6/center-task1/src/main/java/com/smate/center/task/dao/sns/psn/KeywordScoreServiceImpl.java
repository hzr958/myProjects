package com.smate.center.task.dao.sns.psn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.fund.rcmd.ConstFundCategoryKeywordsDao;
import com.smate.center.task.dao.fund.sns.ConstFundTopicDao;
import com.smate.center.task.dao.sns.quartz.PsnKwRmcDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.fund.rcmd.ConstFundCategoryKeywords;
import com.smate.center.task.model.sns.pub.KeywordSplit;


@Service("keywordScoreService")
@Transactional(rollbackFor = Exception.class)
public class KeywordScoreServiceImpl implements KeywordScoreService {

  /**
   * 
   */
  private static final long serialVersionUID = 4725976206886629872L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  private PsnKwRmcDao psnKwRmcDao;
  @Autowired
  private ConstFundTopicDao constFundTopicDao;
  @Autowired
  private ConstFundCategoryKeywordsDao constFundCategoryKeywordsDao;


  @Override
  public List<String> getPsnKeyWordsByPsnId(Long psnId) throws ServiceException {
    return psnKwRmcDao.getKwByPsn(psnId);
  }

  /**
   * 计算个人特征关键词和基金关键词的匹配.
   * 
   * @param psnKey
   * @param fundKeyList
   * @return
   */
  @Override
  public int matchPsnFundKey(List<String> psnKey, List<ConstFundCategoryKeywords> fundKeyList) {
    int count = 0;
    List<Long> keyIds = new ArrayList<Long>();
    loop: for (String psn : psnKey) {
      String key = psn.toLowerCase().trim();
      for (ConstFundCategoryKeywords fund : fundKeyList) {
        String fundKey = fund.getKeywordText();
        if (!keyIds.contains(fund.getId()) && StringUtils.isNotEmpty(fundKey)
            && fuzzy(key, fundKey.toLowerCase().trim())) {
          keyIds.add(fund.getId());
          count++;
          continue loop;
        }
      }
    }
    return count;
  }

  /**
   * 计算成果拆分关键词和基金关键词的匹配.
   * 
   * @param keywordsList
   * @param fundKeyList
   * @return
   */
  @Override
  public List<KeywordSplit> matchPubFundKey(List<KeywordSplit> keywordsList,
      List<ConstFundCategoryKeywords> fundKeyList) {
    List<KeywordSplit> resultList = new ArrayList<KeywordSplit>();
    List<Long> keyIds = new ArrayList<Long>();
    loop: for (KeywordSplit keyword : keywordsList) {
      for (ConstFundCategoryKeywords fundKey : fundKeyList) {
        if (keyword.getKwHash() != null && fundKey != null) {
          // 关键词相同.
          boolean equalKeyFlag = (keyword.getKwHash() != null && fundKey.getKeywordHash() != null
              && keyword.getKwHash().longValue() == fundKey.getKeywordHash().longValue());
          // 拆分关键词包含基金关键词或者基金关键词包含拆分关键词(即模糊匹配).
          boolean conKeyFlag = fuzzy(keyword.getKwtxt(), fundKey.getKeywordText());
          if (!keyIds.contains(fundKey.getId()) && (equalKeyFlag || conKeyFlag)) {
            resultList.add(keyword);
            keyIds.add(fundKey.getId());
            continue loop;
          }
        }
      }
    }
    return resultList;
  }

  /**
   * 根据基金ID匹配基金专题关键词和参数关键词.
   * 
   * @param paramKeyList
   * @param fundId
   * @return
   */
  @Override
  public List<String> matchFundTopicKey(List<String> paramKeyList, Long fundId) {
    try {
      if (CollectionUtils.isNotEmpty(paramKeyList)) {
        // 获取基金专题关键词并切割为单个关键词列表.
        List<String> fundTopicKeyList = constFundTopicDao.getFundTopicKeyList(fundId);
        if (CollectionUtils.isNotEmpty(fundTopicKeyList)) {
          // 单个关键词列表.
          List<String> fundKeyList = new ArrayList<String>();
          for (String fundKey : fundTopicKeyList) {
            if (StringUtils.isNotBlank(fundKey)) {
              String[] fundKeyArr = fundKey.split(",");
              for (int i = 0; i < fundKeyArr.length; i++) {
                fundKeyList.add(fundKeyArr[i]);
              }
            }
          }
          return this.matchFundTopic(paramKeyList, fundKeyList);
        }
      }
    } catch (Exception e) {
      logger.error("", e);
    }
    return null;
  }

  /**
   * 匹配基金专题关键词和参数关键词.
   * 
   * @param paramKeyList
   * @param fundKeyList
   * @return
   */
  @Override
  public List<String> matchFundTopic(List<String> paramKeyList, List<String> fundKeyList) {
    List<String> resultList = new ArrayList<String>();
    for (String paramKey : paramKeyList) {
      if (fundKeyList.contains(paramKey)) {
        resultList.add(paramKey);
      }
    }
    return resultList;
  }

  /**
   * 获取和基金关键词匹配的关键词列表.
   * 
   * @param keywordsList
   * @param fundId
   * @return
   */
  @Override
  public List<KeywordSplit> countPubAndFundKeyword(List<KeywordSplit> keywordsList, Long fundId) {
    List<ConstFundCategoryKeywords> fundKeyList;
    try {
      fundKeyList = constFundCategoryKeywordsDao.findFundKeywordByCategoryId(fundId);
      if (CollectionUtils.isNotEmpty(fundKeyList)) {
        return this.matchPubFundKey(keywordsList, fundKeyList);
      }
    } catch (Exception e) {
      logger.error("计算成果关键词与基金关键词出错", e);
    }
    return null;
  }

  @Override
  public int countPsnAndFundKeyword(List<String> psnKey, Long categoryId) {
    List<ConstFundCategoryKeywords> fundKeyList;
    try {
      if (CollectionUtils.isNotEmpty(psnKey)) {
        fundKeyList = constFundCategoryKeywordsDao.findFundKeywordByCategoryId(categoryId);
        if (CollectionUtils.isNotEmpty(fundKeyList)) {
          return this.matchPsnFundKey(psnKey, fundKeyList);
        }
      }
    } catch (Exception e) {
      logger.error("计算个人关键词与基金关键词出错", e);
    }
    return 0;
  }

  private boolean fuzzy(String keyTxt, String fundKey) {
    return ((StringUtils.isNotBlank(keyTxt)) && (StringUtils.isNotBlank(fundKey))
        && ((keyTxt.contains(fundKey)) || (fundKey.contains(keyTxt))));
  }
}
