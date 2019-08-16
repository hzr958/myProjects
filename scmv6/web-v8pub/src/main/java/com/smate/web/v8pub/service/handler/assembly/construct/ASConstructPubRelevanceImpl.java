package com.smate.web.v8pub.service.handler.assembly.construct;

import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.group.GrpKwDiscService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 构造relevance（相关度）参数 <br/>
 * 相关度 相关度：成果关键词与群组关键词匹配数
 * 
 * @author YJ
 *
 *         2018年8月3日
 */
@Transactional(rollbackFor = Exception.class)
public class ASConstructPubRelevanceImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GrpKwDiscService grpKwDiscService;
  @Autowired
  private PubSnsDetailService pubSnsDetailService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubGenre != PubGenreConstants.GROUP_PUB) {
      return null;
    }
    if (NumberUtils.isNullOrZero(pub.grpId)) {
      return null;
    }
    try {
      // 1. 获取成果关键词
      String pubKeywords = getPubKeyWords(pub);
      // 2. 获取群组关键词
      List<String> grpKeywords = grpKwDiscService.listGrpKeyword(pub.grpId);
      // 计算相关度relevance
      pub.relevance = bulidRelevance(pubKeywords, grpKeywords);
      logger.debug("sns库构建relevance参数成功");
    } catch (Exception e) {
      logger.error("构建relevance参数出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "构建relevance参数出错！", e);
    }
    return null;
  }

  private String getPubKeyWords(PubDTO pub) {
    if (StringUtils.isNotEmpty(pub.keywords)) {
      return pub.keywords;
    }
    PubSnsDetailDOM pubSnsDetailDOM = pubSnsDetailService.get(pub.pubId);
    if (pubSnsDetailDOM != null) {
      return pubSnsDetailDOM.getKeywords();
    }
    return "";
  }

  /**
   * 计算相关度
   * 
   * @param pubKeywords
   * @param grpKeywords
   * @return
   */
  private Integer bulidRelevance(String pubKeywords, List<String> grpKeywords) {
    Integer count = 0;
    if (StringUtils.isBlank(pubKeywords)) {
      return 0;
    }
    if (CollectionUtils.isEmpty(grpKeywords)) {
      return 0;
    }
    // 过滤数据
    pubKeywords = filterKwString(pubKeywords);
    grpKeywords = filterKwStringList(grpKeywords);

    for (String groupKw : grpKeywords) {
      if (StringUtils.isNotEmpty(groupKw)) {
        if (pubKeywords.indexOf(groupKw) != -1) {
          count++;
        }
      }
    }
    return count;
  }

  public String filterKwString(String str) {
    if (StringUtils.isEmpty(str)) {
      return "";
    }
    str = XmlUtil.filterAuForCompare(str);
    // 过滤数字
    str = str.replaceAll("\\d+", "");
    return str;
  }

  public List<String> filterKwStringList(List<String> kwStrList) {
    List<String> strList = new ArrayList<String>();
    for (String str : kwStrList) {
      str = this.filterKwString(str);
      //如果包含之前的关键，就不要添加了--ajb SCM-22495
      if (StringUtils.isEmpty(str) || strList.contains(str)) {
        continue;
      }
      strList.add(str);
    }
    return strList;
  }

}
