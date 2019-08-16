package com.smate.center.open.service.keyword;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.utils.xml.XmlUtils;
import com.smate.core.base.pub.dao.pdwh.PdwhPubKeywordDictionaryDao;
import com.smate.core.base.pub.model.pdwh.PdwhPubKeywordDictionary;

/**
 * 
 * @author aijiangbin
 * @date 2018年4月25日
 */

@Service("pdwhPubkeywordsService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubkeywordsServiceImpl implements PdwhPubkeywordsService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  public static final Integer MAX_SIZE = 1000;
  @Autowired
  private PdwhPubKeywordDictionaryDao pdwhPubKeywordDictionaryDao;

  /**
   * 得到需要翻译的关键词
   */
  @Override
  public List<Map<String, Object>> getNeedTranslateKeyword() {
    List<Map<String, Object>> listMap = new ArrayList<>();
    List<PdwhPubKeywordDictionary> list = pdwhPubKeywordDictionaryDao.getNeedTranslateKeywords(MAX_SIZE);
    if (list == null || list.size() == 0) {
      return listMap;
    }
    for (PdwhPubKeywordDictionary keyword : list) {
      Map<String, Object> map = new HashMap<>();
      map.put("id", keyword.getId());
      map.put("language", keyword.getLanguage());
      if (keyword.getLanguage() == 1) {
        map.put("keyword", StringEscapeUtils.escapeJson(keyword.getEnKeyword()));
      } else {
        map.put("keyword", StringEscapeUtils.escapeJson(keyword.getZhKeyword()));
      }
      if (map.get("keyword") == null || StringUtils.isBlank(map.get("keyword").toString())) {
        keyword.setStatus(9);// 错误数据
        keyword.setUpdateDate(new Date());
        pdwhPubKeywordDictionaryDao.save(keyword);
        continue;
      }
      listMap.add(map);
    }
    return listMap;
  }

  /**
   * 得到需要翻译的关键词
   */
  @Override
  public List<Map<String, Object>> getKeywordByPubId(Long pubId) {
    List<Map<String, Object>> listMap = new ArrayList<>();
    PdwhPubKeywordDictionary keyword = pdwhPubKeywordDictionaryDao.get(pubId);
    if (keyword == null) {
      return listMap;
    }
    Map<String, Object> map = new HashMap<>();
    map.put("id", keyword.getId());
    map.put("language", keyword.getLanguage());
    if (keyword.getLanguage() == 1) {
      map.put("keyword", StringEscapeUtils.escapeJson(keyword.getEnKeyword()));
    } else {
      map.put("keyword", StringEscapeUtils.escapeJson(keyword.getZhKeyword()));
    }
    if (map.get("keyword") == null || StringUtils.isBlank(map.get("keyword").toString())) {
      keyword.setStatus(9);// 错误数据
      keyword.setUpdateDate(new Date());
      pdwhPubKeywordDictionaryDao.save(keyword);
    }
    listMap.add(map);
    return listMap;
  }

  @Override
  public void updatePdwhPubkeywords(Map<String, Object> map) throws Exception {
    // Long id = NumberUtils.toLong(map.get("id").toString());
    // Integer language = NumberUtils.toInt(map.get("language").toString());
    if (map.get("keyword") == null) {
      logger.error("TranslatePdwhPubKeywordsServiceImpl需要翻译的关键词为空====");
    }
    String kwStr = (String) map.get("keyword");
    Integer language = XmlUtils.isChinese(kwStr) ? 0 : 1;
    String keywordGg = getStringByLen(map.get("keywordGg").toString(), 200);
    keywordGg = StringEscapeUtils.unescapeJson(keywordGg);
    keywordGg = StringEscapeUtils.unescapeHtml4(keywordGg);
    String keywordBd = getStringByLen(map.get("keywordBd").toString(), 200);
    keywordBd = StringEscapeUtils.unescapeJson(keywordBd);
    keywordBd = StringEscapeUtils.unescapeHtml4(keywordBd);
    String keywordTx = getStringByLen(map.get("keywordTx").toString(), 200);
    keywordTx = StringEscapeUtils.unescapeJson(keywordTx);
    keywordTx = StringEscapeUtils.unescapeHtml4(keywordTx);
    // PdwhPubKeywordDictionary pdwhPubKeywordDictionary = pdwhPubKeywordDictionaryDao.get(id);
    PdwhPubKeywordDictionary pdwhPubKeywordDictionary = new PdwhPubKeywordDictionary();
    if (language == 1) {// 英文的
      if (StringUtils.isNotBlank(kwStr)) {
        pdwhPubKeywordDictionary.setEnKeyword(kwStr);
      }
      if (StringUtils.isNotBlank(keywordGg)) {
        pdwhPubKeywordDictionary.setZhKeywordGg(keywordGg);
      }
      if (StringUtils.isNotBlank(keywordBd)) {
        pdwhPubKeywordDictionary.setZhKeywordBd(keywordBd);
      }
      if (StringUtils.isNotBlank(keywordTx)) {
        pdwhPubKeywordDictionary.setZhKeywordTx(keywordTx);
      }
    } else {
      if (StringUtils.isNotBlank(kwStr)) {
        pdwhPubKeywordDictionary.setZhKeyword(kwStr);
      }
      if (StringUtils.isNotBlank(keywordGg)) {
        pdwhPubKeywordDictionary.setEnKeywordGg(keywordGg);
      }
      if (StringUtils.isNotBlank(keywordBd)) {
        pdwhPubKeywordDictionary.setEnKeywordBd(keywordBd);
      }
      if (StringUtils.isNotBlank(keywordTx)) {
        pdwhPubKeywordDictionary.setEnKeywordTx(keywordTx);
      }
    }
    pdwhPubKeywordDictionary.setLanguage(language);
    pdwhPubKeywordDictionary.setStatus(1);
    pdwhPubKeywordDictionary.setUpdateDate(new Date());
    pdwhPubKeywordDictionaryDao.save(pdwhPubKeywordDictionary);

  }

  @Override
  public void updatePdwhPubkeywordsList(List<Map<String, Object>> list) throws Exception {
    for (Map<String, Object> map : list) {
      updatePdwhPubkeywords(map);
    }

  }

  public String getStringByLen(String str, int length) {
    str = str.trim();
    if (StringUtils.isBlank(str) || str.length() <= length) {
      return str;
    }
    return str.substring(0, length);
  }

  @Override
  public List<Map<String, Object>> getKeywordByPageNo(Integer pageNo) {
    List<Map<String, Object>> listMap = new ArrayList<>();
    List<PdwhPubKeywordDictionary> list = pdwhPubKeywordDictionaryDao.getKeywordsByPageNo(pageNo, MAX_SIZE);
    if (list == null || list.size() == 0) {
      return listMap;
    }
    for (PdwhPubKeywordDictionary keyword : list) {
      Map<String, Object> map = new HashMap<>();
      map.put("id", keyword.getId());
      map.put("language", keyword.getLanguage());
      if (keyword.getLanguage() == 1) {
        map.put("keyword", StringEscapeUtils.escapeJson(keyword.getEnKeyword()));
      } else {
        map.put("keyword", StringEscapeUtils.escapeJson(keyword.getZhKeyword()));
      }
      if (map.get("keyword") == null || StringUtils.isBlank(map.get("keyword").toString())) {
        keyword.setStatus(9);// 错误数据
        keyword.setUpdateDate(new Date());
        pdwhPubKeywordDictionaryDao.save(keyword);
        continue;
      }
      listMap.add(map);
    }
    return listMap;
  }


}
