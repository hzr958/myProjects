package com.smate.center.task.service.pdwh.quartz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubKeywords20180511Dao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.pub.PdwhPubKeywords20180511;
import com.smate.core.base.pub.dao.pdwh.PdwhPubKeywordDictionaryDao;
import com.smate.core.base.pub.model.pdwh.PdwhPubKeywordDictionary;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.string.IrisStringUtils;

/**
 * @author aijiangbin
 * @date 2018年4月24日
 */
@Service("pdwhPublicationKeywordService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPublicationKeywordServiceImpl implements PdwhPublicationKeywordService {
  public final static Integer KEYWORD_MAX_LENGTH = 200; // 关键词最大长度
  public final static Integer NOT_DEAL_STATUS = 0; // 未处理状态
  public final static Integer DEAL_STATUS = 1; // 已处理状态
  public final static Integer LANGUAGE_ZH = 2; // 中文
  public final static Integer LANGUAGE_EN = 1; // 英文
  @Autowired
  private PdwhPubKeywordDictionaryDao pdwhPubKeywordDictionaryDao;
  @Autowired
  private PdwhPubKeywords20180511Dao pdwhPubKeywordsDao;

  /**
   * 得到未处理的关键词
   */
  @Override
  public List<PdwhPubKeywords20180511> getNoDealPdwhPubKeywordsList(int size) throws ServiceException {
    List<PdwhPubKeywords20180511> list = pdwhPubKeywordsDao.getNoDealPdwhPubKeywordsList(size);
    return list;
  }

  /**
   * 处理关键词
   */
  @Override
  public void dealPdwhPubKeywords(PdwhPubKeywords20180511 pdwhPubKeywords) throws ServiceException {
    String keywords = pdwhPubKeywords.getKeywords();
    pdwhPubKeywords.setStatus(DEAL_STATUS);// 更新状态，表示已经处理
    pdwhPubKeywordsDao.save(pdwhPubKeywords);
    if (StringUtils.isBlank(keywords)) {
      return;
    }
    String[] split = keywords.split(";");
    HashSet<String> keyWordSet = new HashSet<>();
    ArrayList<String> hashList = new ArrayList<>();
    // 先对关键词列表去重 ,先获取hashcode
    for (String keyword : split) {
      keyword = getStringByLen(keyword, KEYWORD_MAX_LENGTH);
      if (StringUtils.isBlank(keyword)) {
        continue;
      }
      String hashCode = getKeywordHash(keyword);
      if (hashList.contains(hashCode)) {
        continue;
      }
      hashList.add(hashCode);
      keyWordSet.add(keyword);
    }
    // 保存关键词
    for (String keyword : keyWordSet) {
      // 还要查重,通过hashCode 查重
      String hashCode = getKeywordHash(keyword);
      Boolean existKeyword = pdwhPubKeywordDictionaryDao.isExistByKeywordHashCode(hashCode);
      if (existKeyword) {
        continue;
      }
      PdwhPubKeywordDictionary dic = new PdwhPubKeywordDictionary();
      boolean flag = IrisStringUtils.hasChineseWord(keyword);
      if (flag) {
        dic.setZhKeyword(keyword);
        dic.setLanguage(LANGUAGE_ZH);
      } else {
        dic.setEnKeyword(keyword);
        dic.setLanguage(LANGUAGE_EN);
      }
      dic.setStatus(NOT_DEAL_STATUS);// 默认值，未处理
      dic.setKeywordHashCode(hashCode);
      pdwhPubKeywordDictionaryDao.save(dic);
    }
  }

  public String getStringByLen(String str, int length) {
    str = str.trim();
    if (StringUtils.isBlank(str) || str.length() <= length) {
      return str;
    }
    return str.substring(0, length);
  }

  /**
   * 获取由 标题+类型 生成的哈希值
   * 
   * @param title
   * @param pubType
   * @return
   */
  public static String getKeywordHash(String keyword) {
    if (keyword == null) {
      keyword = "";
    }
    keyword = dealTitle(keyword);
    return HashUtils.getStrHashCode(keyword) + "";
  }

  /**
   * 对标题进行处理:去掉 1.空格+and+空格 2.&amp; 3.统一小写 4.html标签过滤 5.去掉标点符号以及空格
   * 
   * @param title
   * @return
   */
  private static String dealTitle(String title) {
    if (org.apache.commons.lang3.StringUtils.isNotBlank(title)) {
      title = title.replaceAll(" and ", "");
      title = title.replaceAll("&amp;", "");
      title = title.toLowerCase();
      title = com.smate.core.base.utils.common.HtmlUtils.Html2Text(title);
      title = title.replaceAll("[\\pP\\p{Punct}\\pZ]", "");
    }
    return title;
  }
}
