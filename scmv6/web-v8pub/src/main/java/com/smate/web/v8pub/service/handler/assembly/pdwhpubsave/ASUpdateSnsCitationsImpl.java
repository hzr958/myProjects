package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.pub.consts.RestTemplateUtils;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubPdwhSnsRelationService;
import com.smate.web.v8pub.service.sns.PubSnsService;

/**
 * 保存基准库成果同时更新跟个人成果有关的引用次数
 * 
 * @author zll
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ASUpdateSnsCitationsImpl implements PubHandlerAssemblyService {
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String scmDomain;
  @Autowired
  private PubPdwhSnsRelationService pubPdwhSnsRelationService;
  @Autowired
  private PubSnsService pubSnsService;

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
    // 同时要更新与当前成果有关联关系的个人成果
    List<Long> snsPubIds = pubPdwhSnsRelationService.getSnsIdByPdwhId(pub.pubId);
    for (Long pubId : snsPubIds) {
      Map<String, Object> params = new HashMap<String, Object>();
      PubSnsPO pubSns = pubSnsService.queryPubSns(pubId);
      if (pubSns == null) {
        return null;
      }
      params.put("pubHandlerName", "updateSnsCitationsHandler");
      params.put("des3PubId", Des3Utils.encodeToDes3(pubId.toString()));
      params.put("citations", pub.citations);
      params.put("des3PsnId", Des3Utils.encodeToDes3(pubSns.getCreatePsnId().toString()));
      params.put("srcDbId", null);
      params.put("citedType", 0);
      RestTemplateUtils.post(restTemplate, scmDomain + V8pubQueryPubConst.PUBHANDLER_URL,
          JacksonUtils.mapToJsonStr(params));

    }
    return null;
  }

}
