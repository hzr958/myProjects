package com.smate.web.v8pub.service.handler.assembly.pubtypebean;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.utils.DataFormatUtils;
import com.smate.web.v8pub.utils.PubLocaleUtils;

/**
 * 成果类别：专利 构建成果详情的类别对象
 * 
 * @author YJ
 *
 *         2018年7月31日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPatentInfoSaveImpl implements PubHandlerAssemblyService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {}

  private PubTypeInfoBean constructParams(PubDTO pub) {
    PatentInfoBean a = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), PatentInfoBean.class);
    if (a != null) {
      a.setApplicationNo(StringUtils.substring(a.getApplicationNo(), 0, 40));
      a.setPublicationOpenNo(StringUtils.substring(a.getPublicationOpenNo(), 0, 40));
      a.setIPC(StringUtils.substring(a.getIPC(), 0, 40));
      a.setCPC(StringUtils.substring(a.getCPC(), 0, 40));
      a.setPatentee(StringUtils.substring(a.getPatentee(), 0, 120));
      a.setApplier(StringUtils.substring(a.getApplier(), 0, 120));
      a.setIssuingAuthority(PubParamUtils.buildPatentIssuingAuthority(a.getArea()));
      a.setPrice(StringUtils.substring(a.getPrice(), 0, 120));
      Integer status = a.getStatus();
      if (status == null) {
        status = StringUtils.isNotBlank(a.getStartDate()) ? 1 : 0;
      }
      a.setStatus(status);
      a.setApplicationDate(PubParamUtils.formatDate(a.getApplicationDate()));
      a.setEndDate(PubParamUtils.formatDate(a.getEndDate()));
      a.setStartDate(PubParamUtils.formatDate(a.getStartDate()));
    }
    return a;
  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubType.intValue() == PublicationTypeEnum.PATENT) {
      // 构建pubTypeInfoBean
      try {
        pub.pubTypeInfoBean = constructParams(pub);
      } catch (Exception e) {
        logger.error("构建专利类别对象失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "构建专利类别对象失败！", e);
      }
      // 构建briedDesc参数
      try {
        Map<String, String> result = buildData(pub);
        BriefFormatter formatter = new BriefFormatter(PubLocaleUtils.getLocale(pub.title), result);
        String briefDesc = formatter.format(BriefFormatter.PATENTBRIEF_PATTERN);
        pub.briefDesc = briefDesc;
      } catch (Exception e) {
        // 不抛出异常
        logger.error("专利成果构建briefDesc参数失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "专利成果构建briefDesc参数失败！", e);
      }
    }
    return null;
  }

  private Map<String, String> buildData(PubDTO pub) {
    Map<String, String> map = new HashMap<String, String>();
    PatentInfoBean a = (PatentInfoBean) pub.pubTypeInfoBean;
    if (a != null) {
      if (a.getStatus() != null && a.getStatus() == 0) {
        // 申请状态
        map.put("start_date".toUpperCase(), DataFormatUtils.parseDate(a.getApplicationDate(), "-"));
      } else {
        map.put("start_date".toUpperCase(), DataFormatUtils.parseDate(a.getStartDate(), "-"));
      }
      // 签发机构/发证单位
      map.put("issuing_authority".toUpperCase(), PubParamUtils.buildPatentIssuingAuthority(a.getArea()));
      map.put("application_no".toUpperCase(), a.getApplicationNo());
      // SCM-20958 国家或地区id不加入至专利编目信息显示
      map.put("country_name".toUpperCase(), "");
    }
    return map;
  }

}
