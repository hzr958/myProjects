package com.smate.web.v8pub.service.pubduplicate;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.util.PubDetailVoUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.vo.sns.PubDupResultVO;


/**
 * 成果查重服务实现：无论是基准库，个人库查重参数的构建，都在此类中添加
 * 
 * @author YJ
 *
 *         2019年7月18日
 */
@Service("pubCheckDuplicateService")
@Transactional(rollbackFor = Exception.class)
public class PubCheckDuplicateServiceImpl implements PubCheckDuplicateService {

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainScm;


  @Override
  public PubDupResultVO checkSnsPubDuplicate(PubSnsDetailDOM pubSnsDOM, Long psnId) throws ServiceException {
    Assert.notNull(pubSnsDOM, "个人库成果DOM对象为空");
    Assert.notNull(psnId, "个人查重psnId为空");
    Map<String, Object> paramMap = new HashMap<>();
    String publishDate = pubSnsDOM.getPublishDate();
    paramMap.put("pubGener", 1);
    paramMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId + ""));
    paramMap.put("title", pubSnsDOM.getTitle());
    paramMap.put("pubType", pubSnsDOM.getPubType());
    paramMap.put("doi", pubSnsDOM.getDoi());
    paramMap.put("sourceId", pubSnsDOM.getSourceId());
    paramMap.put("srcDbId", pubSnsDOM.getSrcDbId());
    switch (pubSnsDOM.getPubType()) {
      case 5:
        PatentInfoBean patentInfo = (PatentInfoBean) pubSnsDOM.getTypeInfo();
        if (patentInfo != null) {
          paramMap.put("applicationNo", patentInfo.getApplicationNo());
          paramMap.put("publicationOpenNo", patentInfo.getPublicationOpenNo());
          Integer status = patentInfo.getStatus();
          // 授权状态 StartDate,申请状态ApplicationDate
          publishDate = new Integer(1).equals(status) ? patentInfo.getStartDate() : patentInfo.getApplicationDate();
        }
        break;
      case 12:
        StandardInfoBean standardInfo = (StandardInfoBean) pubSnsDOM.getTypeInfo();
        paramMap.put("standardNo", standardInfo == null ? "" : standardInfo.getStandardNo());
        break;
      case 13:
        SoftwareCopyrightBean softwareInfo = (SoftwareCopyrightBean) pubSnsDOM.getTypeInfo();
        paramMap.put("registerNo", softwareInfo == null ? "" : softwareInfo.getRegisterNo());
        break;
    }
    paramMap.put("pubYear", PubDetailVoUtil.parseDateForYear(publishDate));
    String result =
        restTemplate.postForObject(this.domainScm + V8pubQueryPubConst.PUB_DUPCHECK_URL, paramMap, String.class);
    return buildDupResultVO(result);
  }


  private PubDupResultVO buildDupResultVO(String result) {
    if (StringUtils.isBlank(result)) {
      return null;
    }
    return JacksonUtils.jsonObject(result, PubDupResultVO.class);
  }

}
