package com.smate.web.management.service.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.keywords.dao.KeywordsDicDao;
import com.smate.core.base.keywords.model.KeywordsDic;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.management.dao.analysis.sns.KeywordsDicComWordDao;
import com.smate.web.management.model.analysis.KeywordSplit;
import com.smate.web.management.model.analysis.PubInfo;
import com.smate.web.management.model.analysis.sns.KeywordsDicComWord;

/**
 * 使用字典拆分字符串服务.
 * 
 * @author lqh
 * 
 */
@Service("keywordsDicService")
@Transactional(rollbackFor = Exception.class)
public class KeywordsDicServiceImpl implements KeywordsDicService {

  /**
   * 
   */
  private static final long serialVersionUID = 8254854673966927060L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 通用关键词，主要是用于英文通用关键词过滤.
   */
  private static List<String> COMMON_EN_WORDS = new ArrayList<String>();
  // 关键词缓存
  @Autowired
  private CacheService cacheService;
  @Autowired
  private KeywordsDicComWordDao keywordsDicComWordDao;
  @Autowired
  private KeywordsDicDao keywordsDicDao;
  @Resource(name = "pubKeywordsWeightSetProcess")
  private KeywordsWeightSetProcess keywordsWeightSetProcess;

  @Override
  public List<KeywordSplit> findKeywords(String str) throws Exception {

    if (StringUtils.isBlank(str)) {
      return null;
    }
    try {
      return this.findKeywords(str, null);
    } catch (Exception e) {
      logger.error("查找内容中的关键词.", e);
      throw new Exception("查找内容中的关键词.", e);
    }
  }

  @Override
  public List<KeywordSplit> findKeywords(String str, List<String> extKws) throws Exception {

    if (StringUtils.isBlank(str)) {
      return null;
    }
    try {
      List<KeywordSplit> extKwList = this.covertStrKwsToKwSplit(extKws);
      return findKeywords2(str, extKwList);
    } catch (Exception e) {
      logger.error("查找内容中的关键词，使用字符串补充关键词.", e);
      throw new Exception("查找内容中的关键词，使用字符串补充关键词.", e);
    }
  }

  @Override
  public List<KeywordSplit> findKeywords2(String str, List<KeywordSplit> extKws) throws Exception {

    if (StringUtils.isBlank(str)) {
      return null;
    }
    String lstr = str.toLowerCase();
    try {
      // 先使用默认词库拆分
      List<KeywordSplit> defaultDicSpList = this.getFturesHashSplitStr(lstr);
      if (CollectionUtils.isEmpty(defaultDicSpList) && CollectionUtils.isEmpty(extKws)) {
        return null;
      }

      // 合并关键词
      defaultDicSpList = this.mergeKeywords2(defaultDicSpList, extKws);

      // 开始精确差找是否拆分关键词
      List<KeywordSplit> filterSplitList = new ArrayList<KeywordSplit>();

      for (KeywordSplit extKw : defaultDicSpList) {
        String lextKw = extKw.getKwtxt();
        int start = 0;
        // 找到一个，继续找下一个
        loop_while: while (start < lstr.length()) {
          int idxOf = lstr.indexOf(lextKw, start);
          if (idxOf == -1) {
            break;
          }
          start = idxOf + 1;
          // 一个单词，并且全部是大写，需要完全匹配大小写，这种情况一般是缩写简写
          if (extKw.getType() == 1 && extKw.getWlen() == 1
              && extKw.getKeyword().equals(extKw.getKeyword().toUpperCase())) {
            if (str.indexOf(extKw.getKeyword()) < 0) {
              continue loop_while;
            }
          }

          // 查找是否已经添加到列表
          for (KeywordSplit result : filterSplitList) {
            if (result.getKwtxt().equals(lextKw)) {
              // 频次+1
              result.setFreq(result.getFreq() + 1);
              continue loop_while;
            }
          }
          // 设置频次
          extKw.setFreq(1);
          filterSplitList.add(extKw);
        }
      }

      return filterSplitList;
    } catch (Exception e) {
      logger.error("查找内容中的关键词，使用KeywordSplit补充关键词.", e);
      throw new Exception("查找内容中的关键词，使用KeywordSplit补充关键词.", e);
    }
  }

  /**
   * 将字符串合并到查找出来的关键词.
   * 
   * @param filterKws
   * @param kwsList
   * @return
   */
  public List<KeywordSplit> mergeKeywords(List<KeywordSplit> filterKws, List<String> kwsList) {

    if (CollectionUtils.isEmpty(kwsList)) {
      return filterKws;
    }
    if (filterKws == null) {
      filterKws = new ArrayList<KeywordSplit>();
    }
    outer_loop: for (String kw : kwsList) {

      for (KeywordSplit ku : filterKws) {
        if (kw.equalsIgnoreCase(ku.getKwtxt())) {
          continue outer_loop;
        }
      }
      // 未找到，添加到列表
      filterKws.add(covertStrKwsToKwSplit(kw));
    }
    return filterKws;
  }

  /**
   * 将两关键词组合合并.
   * 
   * @param filterKws
   * @param kwsList
   * @return
   */
  public List<KeywordSplit> mergeKeywords2(List<KeywordSplit> filterKws, List<KeywordSplit> kwsList) {

    if (CollectionUtils.isEmpty(kwsList)) {
      return filterKws;
    }
    if (filterKws == null) {
      filterKws = new ArrayList<KeywordSplit>();
    }
    outer_loop: for (KeywordSplit kw : kwsList) {

      for (KeywordSplit ku : filterKws) {
        if (kw.getKwtxt().equals(ku.getKwtxt())) {
          continue outer_loop;
        }
      }
      // 未找到，添加到列表
      filterKws.add(kw);
    }
    return filterKws;
  }

  @Override
  public List<KeywordSplit> findPubKeywords(PubInfo pubinfo) throws Exception {

    return this.findPubKeywords(pubinfo, keywordsWeightSetProcess);
  }


  /**
   * 合并成果拆分关键词结果，统计权重.
   * 
   * @param pubKws
   * @param pubTitleKws
   * @param pubAbsKws
   * @param wtSetting
   * @return
   */
  private List<KeywordSplit> mergePubSplitKws(List<KeywordSplit> pubKws, List<KeywordSplit> pubTitleKws,
      List<KeywordSplit> pubAbsKws, KeywordsWeightSetProcess keywordsWeightSetProcess) {
    // 先将关键词合并
    List<KeywordSplit> mergeList = new ArrayList<KeywordSplit>();
    // 成果关键词
    if (CollectionUtils.isNotEmpty(pubKws)) {
      outer_loop: for (KeywordSplit kw : pubKws) {
        int freq = kw.getFreq() == 0 ? 1 : kw.getFreq();
        for (KeywordSplit mkw : mergeList) {
          if (mkw.getKwtxt().equals(kw.getKwtxt())) {
            mkw.setKwNum(mkw.getKwNum() + freq);
            continue outer_loop;
          }
        }
        kw.setKwNum(freq);
        mergeList.add(kw);
      }
    }
    // 成果标题
    if (CollectionUtils.isNotEmpty(pubTitleKws)) {
      outer_loop: for (KeywordSplit kw : pubTitleKws) {
        int freq = kw.getFreq() == 0 ? 1 : kw.getFreq();
        for (KeywordSplit mkw : mergeList) {
          if (mkw.getKwtxt().equals(kw.getKwtxt())) {
            mkw.setTitleNum(mkw.getTitleNum() + freq);
            continue outer_loop;
          }
        }
        kw.setTitleNum(freq);
        mergeList.add(kw);
      }
    }
    // 成果摘要
    if (CollectionUtils.isNotEmpty(pubAbsKws)) {
      outer_loop: for (KeywordSplit kw : pubAbsKws) {
        int freq = kw.getFreq() == 0 ? 1 : kw.getFreq();
        for (KeywordSplit mkw : mergeList) {
          if (mkw.getKwtxt().equals(kw.getKwtxt())) {
            mkw.setAbsNum(mkw.getAbsNum() + freq);
            continue outer_loop;
          }
        }
        kw.setAbsNum(freq);
        mergeList.add(kw);
      }
    }
    // 排除掉一些不合适的关键词
    List<KeywordSplit> suitList = filterSuitPubSplitKws(mergeList);
    // 计算权重
    keywordsWeightSetProcess.setKwsWeight(suitList);
    // 排序
    Collections.sort(suitList);
    return suitList;
  }

  /**
   * <pre>
   * 对于英文关键词，如果长的关键词包含短的但个关键词，排除短的关键词;
   *  对于中文关键词，如果长的关键词包含4个以下的其他关键词，排除短的关键词.
   * </pre>
   * 
   * @param pubSplitKws
   * @return
   */
  private List<KeywordSplit> filterSuitPubSplitKws(List<KeywordSplit> pubSplitKws) {

    List<KeywordSplit> suitList = new ArrayList<KeywordSplit>();

    outer_loop: for (int i = 0; i < pubSplitKws.size(); i++) {

      KeywordSplit kwi = pubSplitKws.get(i);
      int length = KeywordsDicUtils.getKwWordLength(kwi.getKwtxt());

      // 如果是英文关键词，长度>1直接算合适
      if (StringUtils.isAsciiPrintable(kwi.getKwtxt())) {
        if (length > 1) {
          suitList.add(kwi);
          continue outer_loop;
        }
        // 如果是中文关键词，长度>=4直接算合适
      } else if (length > 3) {
        suitList.add(kwi);
        continue outer_loop;
      }

      inner_loop: for (int j = 0; j < pubSplitKws.size(); j++) {
        if (i == j) {
          continue inner_loop;
        }
        KeywordSplit kwj = pubSplitKws.get(j);
        // 找到包含的，排除掉
        if (kwj.getKwtxt().indexOf(kwi.getKwtxt()) > -1) {
          continue outer_loop;
        }
      }
      suitList.add(kwi);
    }
    return suitList;
  }

  /**
   * 合并成果拆分关键词.
   * 
   * @param mergeList
   * @param fromKws
   * @param fromWt
   * @return
   */
  private List<KeywordSplit> mergePubSplitKws(List<KeywordSplit> mergeList, List<KeywordSplit> fromKws, double fromWt) {

    if (CollectionUtils.isNotEmpty(fromKws)) {
      outer_loop: for (KeywordSplit kwsp : fromKws) {

        for (KeywordSplit mkw : mergeList) {
          if (kwsp.getKwtxt().equals(mkw.getKwtxt())) {
            // 频率，权重累加
            mkw.setFreq(mkw.getFreq() + kwsp.getFreq());
            mkw.setWeight(mkw.getWeight() + kwsp.getFreq() * fromWt);
            continue outer_loop;
          }
        }
        // 计算权重
        kwsp.setWeight(kwsp.getFreq() * fromWt);
        mergeList.add(kwsp);
      }
    }
    return mergeList;
  }

  /**
   * <pre>
   * 合并相同类型：比如中文标题与英文标题、中文摘要与英文摘要，
   * 注意，不同类型不能通过此方法合并，因为权重不一样.
   * </pre>
   * 
   * @param kwsList1
   * @param kwsList2
   * @return
   */
  private List<KeywordSplit> mergeSameTypeSplitKws(List<KeywordSplit> kwsList1, List<KeywordSplit> kwsList2) {

    // 都不为空，合并
    if (CollectionUtils.isNotEmpty(kwsList1) && CollectionUtils.isNotEmpty(kwsList2)) {
      List<KeywordSplit> mergeList = new ArrayList<KeywordSplit>();
      kwsList1.addAll(kwsList2);
      outer_loop: for (KeywordSplit kwsp : kwsList1) {

        for (KeywordSplit mkw : mergeList) {
          if (kwsp.getKwtxt().equals(mkw.getKwtxt())) {
            mkw.setFreq(mkw.getFreq() + kwsp.getFreq());
            continue outer_loop;
          }
        }
        mergeList.add(kwsp);
      }
      return mergeList;
    } else if (CollectionUtils.isNotEmpty(kwsList1)) {

      return kwsList1;
    } else if (CollectionUtils.isNotEmpty(kwsList2)) {

      return kwsList2;
    }
    return null;
  }

  /**
   * 将字符串关键词对象转换成KeywordSplit对象.
   * 
   * @param kwsList
   * @return
   */
  private List<KeywordSplit> covertStrKwsToKwSplit(List<String> kwsList) {
    if (CollectionUtils.isEmpty(kwsList)) {
      return null;
    }

    List<KeywordSplit> spList = new ArrayList<KeywordSplit>();
    for (String kw : kwsList) {
      spList.add(covertStrKwsToKwSplit(kw));
    }
    return spList;
  }

  /**
   * 将字符串关键词对象转换成KeywordSplit对象.
   * 
   * @param kw
   * @return
   */
  private KeywordSplit covertStrKwsToKwSplit(String kw) {
    // 构造新对象
    Integer type = StringUtils.isAsciiPrintable(kw) ? 1 : 2;
    Integer wlen = KeywordsDicUtils.getKwWordLength(kw);
    Long[] hashkw = getKeywordUnitHash(kw);
    KeywordSplit ks = new KeywordSplit(kw, kw.toLowerCase(), hashkw[0], hashkw[1], wlen, type);
    return ks;
  }

  /**
   * 获取字符串正序、反序 hashcode.
   * 
   * @param keyword
   * @return
   */
  private static Long[] getKeywordUnitHash(String keyword) {
    if (StringUtils.isBlank(keyword)) {
      return null;
    }
    // 中文去掉所有空格
    if (ServiceUtil.isChineseStr(keyword)) {
      return HashUtils.getSrUnitHashCode(XmlUtil.getTrimBlankLower(keyword));
    } else {
      // 英文保留单词之间的空格
      return HashUtils.getSrUnitHashCode(XmlUtil.getTrimDoubleBlankLower(keyword));
    }
  }

  /**
   * 获取具有特征词的关键词列表.
   * 
   * @param ftureHashs
   * @return
   * @throws ServiceException
   */
  private List<KeywordsDic> getFturesHashWord(Set<Long> ftureHashs) throws Exception {

    try {
      List<KeywordsDic> ftureKw = new ArrayList<KeywordsDic>();
      List<Long> findDbHash = new ArrayList<Long>();
      // 先找缓存
      for (Long whash : ftureHashs) {
        List<KeywordsDic> ckws = getFtureHashDicCache(whash);
        if (ckws != null) {
          ftureKw.addAll(ckws);
        } else {
          findDbHash.add(whash);
        }
      }
      if (CollectionUtils.isNotEmpty(findDbHash)) {
        // 20个一起找数据库
        Collection<Collection<Long>> container = ServiceUtil.splitList(findDbHash, 20);
        for (Collection<Long> splitC : container) {
          List<KeywordsDic> findKws = this.keywordsDicDao.loadKwListByFturesHash(splitC);
          this.putFtureHashWordCache(splitC, findKws);
          ftureKw.addAll(findKws);
        }
      }
      // 查重过滤
      return filterDupKeywordsDic(ftureKw);
    } catch (Exception e) {
      logger.error("获取具有特征词的关键词列表.", e);
      throw new Exception("获取具有特征词的关键词列表.", e);
    }
  }

  /**
   * 获取具有特征词的关键词列表.
   * 
   * @param ftureHashs
   * @return
   * @throws ServiceException
   */
  private List<KeywordSplit> getFturesHashSplitStr(String str) throws Exception {

    try {
      if (StringUtils.isBlank(str)) {
        return null;
      }
      // 拆分
      Set<Long> wordHashSet = new HashSet<Long>();
      wordHashSet.addAll(this.splitStr2WordHash(str.toLowerCase()));
      List<KeywordsDic> dicList = getFturesHashWord(wordHashSet);

      // 匹配特征关键词
      List<KeywordSplit> resultList = new ArrayList<KeywordSplit>();

      for (KeywordsDic kwObj : dicList) {
        // 构造新对象
        KeywordSplit kws = new KeywordSplit(kwObj.getId(), kwObj.getKeyword(), kwObj.getKwtxt(), kwObj.getKwhash(),
            kwObj.getKwRhash(), kwObj.getWlen(), kwObj.getType());
        resultList.add(kws);
      }

      return resultList;
    } catch (Exception e) {
      logger.error("获取具有特征词的关键词列表.", e);
      throw new Exception("获取具有特征词的关键词列表.", e);
    }
  }

  /**
   * 获取唯一的关键词字典列表.
   * 
   * @param ftureKwList
   * @return
   */
  private List<KeywordsDic> filterDupKeywordsDic(List<KeywordsDic> ftureKwList) {
    if (CollectionUtils.isEmpty(ftureKwList)) {
      return ftureKwList;
    }
    List<KeywordsDic> dupList = new ArrayList<KeywordsDic>();
    outer_loop: for (KeywordsDic ftureKw : ftureKwList) {

      for (KeywordsDic dupFtureKw : dupList) {
        if (dupFtureKw.getId().equals(ftureKw.getId())) {
          continue outer_loop;
        }
      }
      dupList.add(ftureKw);
    }
    return dupList;
  }

  /**
   * 通用关键词，主要是用于英文通用关键词过滤.
   * 
   * @return
   * @throws ServiceException
   */
  private List<String> getCommonWord() throws Exception {

    try {
      if (CollectionUtils.isEmpty(COMMON_EN_WORDS)) {
        List<KeywordsDicComWord> list = keywordsDicComWordDao.getAll();
        for (KeywordsDicComWord wd : list) {
          COMMON_EN_WORDS.add(wd.getWord());
        }
      }
      return COMMON_EN_WORDS;
    } catch (Exception e) {
      logger.error("通用关键词，主要是用于英文通用关键词过滤.", e);
      throw new Exception("通用关键词，主要是用于英文通用关键词过滤.", e);
    }
  }

  /**
   * 拆分内容，如果是英文单词，先拆分出来，其他按照两个两个连接的方式单独拆分出来，计算每个组合的hashcode.
   * 
   * @param content
   * @return
   * @throws ServiceException
   */
  private Set<Long> splitStr2WordHash(String content) throws Exception {

    try {
      content = content.toLowerCase();
      Set<Long> wordHashSet = new HashSet<Long>();
      // 英文，先单独拆分出来.
      if (content.matches(".*[0-9|a-z|\\-]{2,}.*")) {
        Pattern p = Pattern.compile("[0-9|a-z|\\-]{2,}");
        Matcher m = p.matcher(content);
        while (m.find()) {
          String word = m.group();
          // 通用字直接排除
          if (!getCommonWord().contains(word)) {
            wordHashSet.add(HashUtils.getStrHashCode(word));
          }
        }
      }
      // 所有词两两组合
      List<String> split2Words = KeywordsDicUtils.splitStrJoin2Words(content);
      for (String str : split2Words) {
        if (StringUtils.isNotBlank(str)) {
          wordHashSet.add(HashUtils.getStrHashCode(str));
        }
      }
      return wordHashSet;
    } catch (Exception e) {
      logger.error("拆分内容，如果是英文单词，先拆分出来，其他按照两个两个连接的方式单独拆分出来，计算每个组合的hashcode.", e);
      throw new Exception("拆分内容，如果是英文单词，先拆分出来，其他按照两个两个连接的方式单独拆分出来，计算每个组合的hashcode.", e);
    }
  }

  /**
   * 获取关联的特征关键词缓存.
   * 
   * @param ftureHash
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  private List<KeywordsDic> getFtureHashDicCache(Long ftureHash) {

    ArrayList<KeywordsDic> list = (ArrayList<KeywordsDic>) cacheService.get("whash", String.valueOf(ftureHash));
    if (CollectionUtils.isNotEmpty(list)) {
      return list;
    }
    return null;
  }

  /**
   * 批量放入缓存中.
   * 
   * @param whash
   * @param list
   * @throws ServiceException
   */
  private void putFtureHashWordCache(Collection<Long> ftrueHashs, List<KeywordsDic> list) {

    // 分类
    for (Long ftureHash : ftrueHashs) {
      ArrayList<KeywordsDic> dicList = new ArrayList<KeywordsDic>();
      for (KeywordsDic dicWord : list) {
        if (ftureHash.equals(dicWord.getFturesHash())) {
          dicList.add(dicWord);
        }
      }
      // 放入缓存
      putFtureHashWordCache(ftureHash, dicList);
    }
  }

  /**
   * 放入缓存中.
   * 
   * @param whash
   * @param list
   * @throws ServiceException
   */
  private void putFtureHashWordCache(Long whash, ArrayList<KeywordsDic> list) {
    // Element element = new Element(whash, list);
    // wordCache.put(element);
    cacheService.put("whash", String.valueOf(whash), list);
  }

  /*
   * public void setWordCache(Cache wordCache) { this.wordCache = wordCache; }
   */

  @Override
  public List<KeywordSplit> findPubKeywords(PubInfo pubinfo, KeywordsWeightSetProcess keywordsWeightSetProcess)
      throws Exception {

    Assert.notNull(pubinfo, "成果信息不能为空");
    Assert.notNull(keywordsWeightSetProcess, "权重设置不能为空");

    try {
      // 成果关键词
      // 注意：成果关键词也需要作为扩展词库拆分成果标题、摘要
      List<String> pubKwsList = new ArrayList<String>();
      // 中文关键词
      String zhKws = pubinfo.getZhKws();
      List<String> zhPubKws = KeywordsDicUtils.splitKeywordsList(zhKws);
      if (zhPubKws != null) {
        pubKwsList.addAll(zhPubKws);
      }

      // 英文关键词
      String enKws = pubinfo.getEnKws();
      List<String> enPubKws = KeywordsDicUtils.splitKeywordsList(enKws);
      if (enPubKws != null) {
        pubKwsList.addAll(enPubKws);
      }
      // 转换成KeywordSplit对象
      List<KeywordSplit> pubKws = covertStrKwsToKwSplit(pubKwsList);

      // 中文标题关键词
      String zhTitle = pubinfo.getZhTitle();
      List<KeywordSplit> zhTitleKws = this.findKeywords2(zhTitle, pubKws);
      // 英文标题关键词
      String enTitle = pubinfo.getEnTitle();
      List<KeywordSplit> enTitleKws = this.findKeywords2(enTitle, pubKws);
      List<KeywordSplit> pubTitleKws = mergeSameTypeSplitKws(zhTitleKws, enTitleKws);

      // 中文摘要关键词
      String zhAbs = pubinfo.getZhAbs();
      List<KeywordSplit> zhAbsKws = this.findKeywords2(zhAbs, pubKws);
      // 英文摘要关键词
      String enAbs = pubinfo.getEnAbs();
      List<KeywordSplit> enAbsKws = this.findKeywords2(enAbs, pubKws);
      List<KeywordSplit> pubAbsKws = mergeSameTypeSplitKws(zhAbsKws, enAbsKws);

      // 合并.
      List<KeywordSplit> mergeList = this.mergePubSplitKws(pubKws, pubTitleKws, pubAbsKws, keywordsWeightSetProcess);

      return mergeList;
    } catch (Exception e) {
      logger.error("查找成果内容中的关键词.", e);
      throw new Exception("查找成果内容中的关键词.", e);
    }
  }

}
