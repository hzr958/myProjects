package com.smate.web.v8pub.service.sns;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.service.psn.PsnSolrInfoModifyService;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.enums.PubHandlerEnum;

@Service("pubDeleteService")
@Transactional(rollbackFor = Exception.class)
public class PubDeleteServiceImpl implements PubDeleteService {
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String scmDomain;
  @Autowired
  private PsnSolrInfoModifyService psnSolrInfoModifyService;

  @Override
  public String deletePub(String des3PubId, Long psnId) {
    String url = scmDomain + V8pubQueryPubConst.PUBHANDLER_URL;
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("pubGenre", PubGenreConstants.PSN_PUB);
    map.put("des3PubId", des3PubId);
    map.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    map.put("pubHandlerName", PubHandlerEnum.DELETE_PSN_PUB.name);
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(map), headers);
    String result = restTemplate.postForObject(url, entity, String.class);
    return result;
  }
}
