package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dto.IndustryDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubIndustryPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubIndustryService;

@Transactional(rollbackFor = Exception.class)
public class ASPubIndustrySaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubIndustryService pubIndustryService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      pubIndustryService.deleteById(pub.pubId);
      if (pub.industrys == null || pub.industrys.size() == 0) {
        return null;
      }
      List<IndustryDTO> industryList =
          JacksonUtils.jsonToCollection(pub.industrys.toJSONString(), List.class, IndustryDTO.class);
      PubIndustryPO pubIndustryPO = null;
      // 删除之前所有的科技领域
      if (CollectionUtils.isNotEmpty(industryList)) {
        for (IndustryDTO industry : industryList) {
          if (StringUtils.isBlank(industry.getIndustryCode())) {
            continue;
          }
          pubIndustryPO = new PubIndustryPO();
          if (NumberUtils.isNullOrZero(industry.getPubId())) {
            pubIndustryPO.setPubId(pub.pubId);
          }
          pubIndustryPO.setIndustryCode(industry.getIndustryCode());
          pubIndustryService.save(pubIndustryPO);
        }
      }
    } catch (Exception e) {
      logger.error("保存sns库行业信息出错！行业信息={}", pub.industrys, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库成果行业信息出错!", e);
    }
    return null;
  }

}
