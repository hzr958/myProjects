package com.smate.web.v8pub.service.handler.assembly.dispose;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dom.AwardsInfoBean;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.autocomplete.AcAwardIssueIns;
import com.smate.web.v8pub.service.autocomplete.AcAwardIssueInsService;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;

/**
 * 奖励成果授奖机构匹配
 * 
 * @author YJ
 *
 *         2018年9月29日
 */
@Transactional(rollbackFor = Exception.class)
public class ASDisposePubAwardIAMatchImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private AcAwardIssueInsService acAwardIssueInsService;

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
    try {
      if (pub.pubType.intValue() == PublicationTypeEnum.AWARD) {
        AwardsInfoBean awardsInfoBean = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), AwardsInfoBean.class);
        // 奖励类型的成果，进行颁奖机构的匹配
        if (awardsInfoBean != null) {
          String name = awardsInfoBean.getIssuingAuthority();
          name = StringUtils.substring(name, 0, 100);
          if (StringUtils.isNotBlank(name)) {
            AcAwardIssueIns acAwardIssueIns = acAwardIssueInsService.getByName(name);
            if (acAwardIssueIns == null) {
              acAwardIssueIns = new AcAwardIssueIns();
              acAwardIssueIns.setName(name);
              acAwardIssueIns.setCreateAt(new Date());
              // 将需要处理的智能提示字符串去掉空格并转换小写
              String query = name.replaceAll("\\s+", " ").trim().toLowerCase();
              acAwardIssueIns.setQuery(query);
              acAwardIssueInsService.save(acAwardIssueIns);
            }
            awardsInfoBean.setIssueInsId(acAwardIssueIns.getCode());
            awardsInfoBean.setIssuingAuthority(acAwardIssueIns.getName());
            pub.pubTypeInfo = JSONObject.parseObject(JacksonUtils.jsonObjectSerializerNoNull(awardsInfoBean));
          }
        }
      }
    } catch (Exception e) {
      logger.error("奖励成果授奖机构匹配出错！", e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }

}
