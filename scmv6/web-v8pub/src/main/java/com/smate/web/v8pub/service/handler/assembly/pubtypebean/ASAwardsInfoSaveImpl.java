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
import com.smate.web.v8pub.dom.AwardsInfoBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.utils.DataFormatUtils;
import com.smate.web.v8pub.utils.PubLocaleUtils;

/**
 * 成果类别：奖励 构建成果详情的类别对象
 * 
 * @author YJ
 *
 *         2018年7月31日
 */
@Transactional(rollbackFor = Exception.class)
public class ASAwardsInfoSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  /**
   * 参数处理 主要是长度的处理
   * 
   * @param pub
   * @return
   */
  private PubTypeInfoBean constructParams(PubDTO pub) {
    AwardsInfoBean a = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), AwardsInfoBean.class);
    if (a != null) {
      a.setCertificateNo(StringUtils.substring(a.getCertificateNo(), 0, 100));
      a.setCategory((StringUtils.substring(a.getCategory(), 0, 100)));
      a.setGrade(StringUtils.substring(a.getGrade(), 0, 100));
      a.setIssuingAuthority(StringUtils.substring(a.getIssuingAuthority(), 0, 100));
      a.setAwardDate(PubParamUtils.formatDate(a.getAwardDate()));
    }
    return a;
  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubType.intValue() == PublicationTypeEnum.AWARD) {
      try {
        pub.pubTypeInfoBean = constructParams(pub);
      } catch (Exception e) {
        logger.error("构建奖励类别对象失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "构建奖励类别对象失败!", e);
      }
      // 构建briefDesc参数
      try {
        Map<String, String> result = buildData(pub);
        BriefFormatter formatter = new BriefFormatter(PubLocaleUtils.getLocale(pub.title), result);
        String briefDesc = formatter.format(BriefFormatter.AWARDBRIEF_PATTERN);
        pub.briefDesc = briefDesc;
      } catch (Exception e) {
        logger.error("奖励成果构建briefDesc参数失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "奖励成果构建briefDesc参数失败！", e);
      }
    }
    return null;
  }

  private Map<String, String> buildData(PubDTO pub) {
    AwardsInfoBean a = (AwardsInfoBean) pub.pubTypeInfoBean;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("issuing_authority".toUpperCase(), a.getIssuingAuthority());
      map.put("category".toUpperCase(), a.getCategory());
      map.put("grade".toUpperCase(), a.getGrade());
      // 这里的日期格式必须是以 - 隔开的
      map.put("award_date".toUpperCase(), DataFormatUtils.parseDate(a.getAwardDate(), "-"));
    }
    return map;
  }

}
