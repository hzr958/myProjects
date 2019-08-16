package com.smate.web.v8pub.service.handler.assembly.construct;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.group.GrpBaseInfoService;

/**
 * 构建labeled（群组成果是否标注）参数 <br/>
 * labeled 是否标注 0成果未标注；1成果已标注；标注即成果资助基金信息与群组基金信息匹配
 * 
 * @author YJ
 *
 *         2018年8月3日
 */
@Transactional(rollbackFor = Exception.class)
public class ASConstructPubLabeledImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GrpBaseInfoService grpBaseInfoService;

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
      // 1. 获取成果基金信息fundInfo
      String pubFundInfo = pub.fundInfo;
      // 2. 获取群组项目批准号/编号的projectNo
      String projectNo = grpBaseInfoService.getProjectNo(pub.grpId);
      pub.labeled = buildLabeled(pubFundInfo, projectNo);
    } catch (Exception e) {
      logger.error("构建labeled参数失败", e);
      throw new PubHandlerAssemblyException("ASConstructPubLabeledImpl构建labeled参数出错！", e);
    }
    return null;
  }

  private Integer buildLabeled(String pubFundInfo, String projectNo) {
    if (StringUtils.isBlank(pubFundInfo)) {
      return 0;
    }
    if (StringUtils.isBlank(projectNo)) {
      return 0;
    }
    // 字段过滤
    pubFundInfo = XmlUtil.filterAuForCompare(pubFundInfo);
    projectNo = XmlUtil.filterAuForCompare(projectNo);
    /**
     * 2.将基金信息与项目批准号进行匹配 一个项目只对应一个资助基金号，而一个成果对应多个不同的资助基金。 所以用项目基金号fundInfo去匹配成果基金号projectNo
     */
    if (pubFundInfo.indexOf(projectNo) != -1) {
      return 1;
    }
    return 0;
  }

}
