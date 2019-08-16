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
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.utils.PubLocaleUtils;

/**
 * 软件著作权的信息保存
 * 
 * @author YJ
 *
 *         2019年5月21日
 */

@Transactional(rollbackFor = Exception.class)
public class ASSoftwareCopyrightSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // if (pub.pubType.intValue() == PublicationTypeEnum.SOFTWARE_COPYRIGHT) {
    // SoftwareCopyrightBean a = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(),
    // SoftwareCopyrightBean.class);
    // if (a != null && StringUtils.isBlank(a.getRegisterNo())) {
    // throw new PubHandlerAssemblyException(this.getClass().getSimpleName() +
    // "软件著作权成果缺失登记号，不进行成果保存业务");
    // }
    // }
  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  private PubTypeInfoBean constructParams(PubDTO pub) {
    SoftwareCopyrightBean a = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), SoftwareCopyrightBean.class);
    if (a != null) {
      a.setRegisterNo(StringUtils.substring(a.getRegisterNo(), 0, 200));
      a.setFirstPublishDate(PubParamUtils.formatDate(a.getFirstPublishDate()));
      a.setPublicityDate(PubParamUtils.formatDate(a.getPublicityDate()));
      a.setRegisterDate(PubParamUtils.formatDate(a.getRegisterDate()));
    }
    return a;
  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubType.intValue() == PublicationTypeEnum.SOFTWARE_COPYRIGHT) {
      try {
        pub.pubTypeInfoBean = constructParams(pub);
      } catch (Exception e) {
        logger.error("构建软件著作权类别对象失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "构建软件著作权成果类别对象失败！", e);
      }
      // 构建briefDesc参数
      try {
        Map<String, String> result = buildData(pub);
        BriefFormatter formatter = new BriefFormatter(PubLocaleUtils.getLocale(pub.title), result);
        String briefDesc = formatter.format(BriefFormatter.SOFTWARECOPYRIGHTBRIEF_PATTERN);
        pub.briefDesc = briefDesc;
      } catch (Exception e) {
        logger.error("软件著作权成果构建briefDesc参数失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "软件著作权成果构建briefDesc参数失败！", e);
      }
    }
    return null;
  }

  private Map<String, String> buildData(PubDTO pub) {
    SoftwareCopyrightBean a = (SoftwareCopyrightBean) pub.pubTypeInfoBean;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("register_no".toUpperCase(), a.getRegisterNo());
    }
    return map;
  }

}
