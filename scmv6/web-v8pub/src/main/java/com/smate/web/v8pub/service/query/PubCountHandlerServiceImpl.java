package com.smate.web.v8pub.service.query;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.v8pub.service.sns.PubSnsService;

/**
 * 成果统计数处理service
 * 
 * @author aijiangbin
 * @date 2018年7月24日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PubCountHandlerServiceImpl implements PubCountHandlerService {

  @Resource
  private PubSnsService pubSnsService;

  @Override
  public Map<String, Object> querySnsPubCount(PubQueryDTO pubQueryDTO) throws ServiceException {
    // 设置默认参数
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, String> publishYearMap = new HashMap<String, String>();
    Map<String, String> pubTypeMap = new HashMap<String, String>();
    Map<String, String> includeTypeMap = new HashMap<String, String>();
    // 初始化参数
    initParams(publishYearMap, pubTypeMap, includeTypeMap);
    List<Map<String, Object>> pubTypecount = pubSnsService.querySnsPubCount(pubQueryDTO, 1);
    // 构建成果类型
    buildPubType(pubTypeMap, pubTypecount);
    List<Map<String, Object>> publishYearcount = pubSnsService.querySnsPubCount(pubQueryDTO, 2);
    // 构建年份
    buildPublishYear(publishYearMap, publishYearcount);
    Long noLimit = pubSnsService.findPubCount(pubQueryDTO);
    publishYearMap.put("noLimit", noLimit != null ? noLimit.toString() : "0");
    // 收录类别统计数
    List<Map<String, Object>> includeTypecount = pubSnsService.querySnsPubCount(pubQueryDTO, 3);
    // 构建收录类别
    buildIncludeType(includeTypeMap, includeTypecount);
    map.put("publishYear", publishYearMap);
    map.put("pubType", pubTypeMap);
    map.put("includeType", includeTypeMap);
    return map;
  }

  private void buildIncludeType(Map<String, String> includeTypeMap, List<Map<String, Object>> includeTypecount) {
    if (includeTypecount != null && includeTypecount.size() > 0) {
      Integer scieCount = 0;
      for (Map<String, Object> m : includeTypecount) {
        String libraryName = String.valueOf(m.get("libraryName"));
        boolean isSCIE = libraryName.equalsIgnoreCase("sci") || libraryName.equalsIgnoreCase("scie");
        if (isSCIE) {
          scieCount += NumberUtils.toInt(m.get("count").toString(), 0);
          includeTypeMap.put("scie", scieCount + "");
        }
        if (libraryName.equalsIgnoreCase("ssci")) {
          includeTypeMap.put("ssci", m.get("count").toString());
        }
        if (libraryName.equalsIgnoreCase("ei")) {
          includeTypeMap.put("ei", m.get("count").toString());
        }
        if (libraryName.equalsIgnoreCase("istp")) {
          includeTypeMap.put("istp", m.get("count").toString());
        }
        if (libraryName.equalsIgnoreCase("cssci")) {
          includeTypeMap.put("cssci", m.get("count").toString());
        }
        if (libraryName.equalsIgnoreCase("pku")) {
          includeTypeMap.put("pku", m.get("count").toString());
        }
        if (libraryName.equalsIgnoreCase("other")) {
          includeTypeMap.put("other", m.get("count").toString());
        }
      }
    }
  }

  private void buildPublishYear(Map<String, String> publishYearMap, List<Map<String, Object>> publishYearcount) {
    if (publishYearcount != null && publishYearcount.size() > 0) {
      for (Map<String, Object> m : publishYearcount) {
        if (m.get("publishDate") != null) {
          publishYearMap.put(m.get("publishDate").toString(), m.get("count").toString());
        }
      }
    }
  }

  private void buildPubType(Map<String, String> pubTypeMap, List<Map<String, Object>> pubTypecount) {
    if (pubTypecount != null && pubTypecount.size() > 0) {
      for (Map<String, Object> m : pubTypecount) {
        pubTypeMap.put(m.get("pubType").toString(), m.get("count").toString());
      }
    }
  }

  private void initParams(Map<String, String> publishYearMap, Map<String, String> pubTypeMap,
      Map<String, String> includeTypeMap) {
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    for (int i = currentYear; i >= currentYear - 10; i--) {// 年份默认统计数
      publishYearMap.put(i + "", "0");
    }
    for (int i = 1; i <= 10; i++) {// 成果类型默认统计数
      pubTypeMap.put(i + "", "0");
    }
    includeTypeMap.put("sci", "0");
    includeTypeMap.put("scie", "0");
    includeTypeMap.put("ssci", "0");
    includeTypeMap.put("ei", "0");
    includeTypeMap.put("istp", "0");
    includeTypeMap.put("cssci", "0");
    includeTypeMap.put("pku", "0");
    includeTypeMap.put("other", "0");
  }

  @Override
  public Map<String, Object> queryGrpPubCount(PubQueryDTO pubQueryDTO) throws ServiceException {
    // 设置默认参数
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, String> publishYearMap = new HashMap<String, String>();
    Map<String, String> pubTypeMap = new HashMap<String, String>();
    Map<String, String> includeTypeMap = new HashMap<String, String>();
    // 初始化参数
    initParams(publishYearMap, pubTypeMap, includeTypeMap);
    List<Map<String, Object>> pubTypecount = pubSnsService.queryGrpPubCount(pubQueryDTO, 1);
    // 构建成果类型
    buildPubType(pubTypeMap, pubTypecount);
    List<Map<String, Object>> publishYearcount = pubSnsService.queryGrpPubCount(pubQueryDTO, 2);
    // 构建年份
    buildPublishYear(publishYearMap, publishYearcount);

    List<Map<String, Object>> includeTypecount = pubSnsService.queryGrpPubCount(pubQueryDTO, 3);
    // 构建收录类别
    buildIncludeType(includeTypeMap, includeTypecount);
    map.put("publishYear", publishYearMap);
    map.put("pubType", pubTypeMap);
    map.put("includeType", includeTypeMap);
    return map;
  }

}
