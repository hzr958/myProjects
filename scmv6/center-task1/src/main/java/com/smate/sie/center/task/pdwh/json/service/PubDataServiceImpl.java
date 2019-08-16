package com.smate.sie.center.task.pdwh.json.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.center.task.pdwh.json.pubtype.factory.PubTypeServiceFactory;
import com.smate.sie.center.task.pdwh.json.service.process.PubHandlerProcessService;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.pub.exception.PubHandlerCheckParameterException;
import com.smate.sie.core.base.utils.pub.exception.PubHandlerException;
import com.smate.sie.core.base.utils.pub.exception.ServiceException;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 成果数据服务实现
 * 
 * @author ZSJ
 *
 * @date 2019年2月11日
 */
@Service("pubDataService")
@Transactional(rollbackFor = Exception.class)
public class PubDataServiceImpl implements PubDataService {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private PubTypeServiceFactory pubTypeServiceFactory;

  /**
   * 调用具体的服务
   */
  @Override
  public Map<String, Object> pubHandleByType(PubJsonDTO pub) throws ServiceException {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      // TODO 前期参数校验 数据格式转换 (统一数据格式转换)
      checkCommonParameter(pub);
      PubHandlerProcessService pubhandlerService = pubTypeServiceFactory.getPubTypeServices(pub.pubTypeCode);
      result = pubhandlerService.excute(pub);
      buildCommonInfo(pub);
      result.put("status", "SUCCESS");
    } catch (PubHandlerCheckParameterException e) {
      logger.error("参数检验出错" + e.getMessage(), e);
      result.put("status", "ERROR");
      result.put("msg", e.getMessage());
    } catch (PubHandlerException e) {
      logger.error("成果处理出错" + e.getMessage(), e);
      result.put("status", "ERROR");
      result.put("msg", e.getMessage());
    } catch (Exception e) {
      logger.error("调用成果处理器出错" + e.getMessage(), e);
      result.put("status", "ERROR");
      result.put("msg", e.getMessage());
    }
    return result;
  }

  /**
   * 公用参数 校验
   * 
   * @param pub
   * @throws PubHandlerCheckParameterException
   */
  void checkCommonParameter(PubJsonDTO pub) throws PubHandlerCheckParameterException {}

  /**
   * 处理公共字段
   * 
   * @param pub
   */
  private void buildCommonInfo(PubJsonDTO pub) {
    if (StringUtils.isNotBlank(pub.publishDate)) {
      String[] dates = DateUtils.splitToYearMothDayByStr(pub.publishDate);
      pub.publishYear = dates[0];
      pub.publishMonth = dates[1];
      pub.publishDay = dates[2];
    }
    if ("0".equals(pub.isPublicCode.toString())) {
      pub.isPublicName = "非公开";
    } else {
      pub.isPublicName = "公开";
    }
    String keyWords = pub.keywords;
    if (StringUtils.isNotBlank(keyWords)) {
      keyWords = KeepFiveKeywords(keyWords);
      pub.keywords = keyWords;
    }
  }

  private String KeepFiveKeywords(String ckeywords) {
    String[] splitArr = ckeywords.split(";");
    List<String> lkwList = new ArrayList<String>();
    for (String kw : splitArr) {
      String lkw = kw.trim().toLowerCase();
      lkw = StringUtils.substring(lkw, 0, 20);
      if (StringUtils.isBlank(lkw) || lkwList.contains(lkw)) {
        continue;
      }
      lkwList.add(lkw);
    }
    int length = lkwList.size() > 5 ? 5 : lkwList.size();
    StringBuilder resultStr = new StringBuilder();
    for (int i = 0; i < length; i++) {
      if (!StringUtils.isBlank(lkwList.get(i))) {
        if (i == 0) {
          resultStr.append(lkwList.get(i).trim());
        } else {
          resultStr.append(";");
          resultStr.append(lkwList.get(i).trim());
        }
      }
    }
    return resultStr.toString();
  }
}
