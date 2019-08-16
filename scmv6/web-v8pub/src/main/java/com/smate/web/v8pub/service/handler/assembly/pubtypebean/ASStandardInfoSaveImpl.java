package com.smate.web.v8pub.service.handler.assembly.pubtypebean;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.utils.PubLocaleUtils;

/**
 * 标准信息的保存
 * 
 * @author YJ
 *
 *         2019年5月21日
 */

@Transactional(rollbackFor = Exception.class)
public class ASStandardInfoSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // if (pub.pubType.intValue() == PublicationTypeEnum.STANDARD) {
    // StandardInfoBean a = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(),
    // StandardInfoBean.class);
    // if (a != null && StringUtils.isBlank(a.getStandardNo())) {
    // throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "标准成果缺失标准号，不进行成果保存业务");
    // }
    // }

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  private PubTypeInfoBean constructParams(PubDTO pub) {
    StandardInfoBean a = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), StandardInfoBean.class);
    if (a != null) {
      a.setStandardNo(StringUtils.substring(a.getStandardNo(), 0, 65));
      a.setTechnicalCommittees(StringUtils.substring(a.getTechnicalCommittees(), 0, 49));
      a.setPublishAgency(StringUtils.substring(a.getPublishAgency(), 0, 95));
      a.setImplementDate(PubParamUtils.formatDate(a.getImplementDate()));
      a.setObsoleteDate(PubParamUtils.formatDate(a.getObsoleteDate()));
    }
    return a;
  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubType.intValue() == PublicationTypeEnum.STANDARD) {
      try {
        pub.pubTypeInfoBean = constructParams(pub);
      } catch (Exception e) {
        logger.error("构建标准类别对象失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "构建标准成果类别对象失败！", e);
      }
      // 构建briefDesc参数
      try {
        Map<String, String> result = buildData(pub);
        BriefFormatter formatter = new BriefFormatter(PubLocaleUtils.getLocale(pub.title), result);
        String briefDesc = formatter.format(BriefFormatter.STANDARDBRIEF_PATTERN);
        pub.briefDesc = briefDesc;
      } catch (Exception e) {
        logger.error("标准成果构建briefDesc参数失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "标准成果构建briefDesc参数失败！", e);
      }
    }
    return null;
  }

  private Map<String, String> buildData(PubDTO pub) {
    StandardInfoBean a = (StandardInfoBean) pub.pubTypeInfoBean;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("standard_no".toUpperCase(), a.getStandardNo());
    }
    return map;
  }

}
