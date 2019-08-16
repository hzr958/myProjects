package com.smate.web.v8pub.service.handler.assembly.open;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;

@Transactional(rollbackFor = Exception.class)
public class ASOpenDynamicProduceImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${realTimeDyn.restful.url}")
  private String initNewDynUrl;
  @Value("${groupDyn.restful.url}")
  private String initGrpDynUrl;

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // 产生动态必须要psnId，否则不产生动态，不影响成果保存的主逻辑
    if (NumberUtils.isNullOrZero(pub.psnId)) {
      logger.error("产生动态错误：psnId为空，psnId={}", pub.psnId);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "产生动态错误：psnId为空！只是不产生动态，不影响成果保存的主逻辑");
    }

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (!NumberUtils.isNullOrZero(pub.grpId)) {
      sendGroupDynamic(pub);
    } else {
      sendDynamic(pub);
    }
    return null;
  }

  private void sendGroupDynamic(PubDTO pub) {
    // 产生群组动态
    Map<String, Object> groupDynMap = new HashMap<String, Object>();
    groupDynMap.put("groupId", pub.grpId);
    groupDynMap.put("psnId", pub.psnId);
    groupDynMap.put("resType", "pub");
    groupDynMap.put("resId", pub.pubId);
    groupDynMap.put("tempType", "GRP_ADDPUB");
    try {
      restTemplate.postForObject(initGrpDynUrl, groupDynMap, Object.class);
    } catch (Exception e) {
      logger.error("生成成果群组动态出错,grpId{},pubId={},psnId={}", new Object[] {pub.grpId, pub.pubId, pub.psnId}, e);
    }
  }

  private void sendDynamic(PubDTO pub) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("pubId", pub.pubId);
    map.put("psnId", pub.psnId);
    map.put("dynType", "B3TEMP");
    map.put("resType", 1);
    // 生成动态_tsz
    // 得到restful的返回值;注意要返回Object类型，如果返回String类型，就会导致字符串被多次转译,转化为map的时候报错
    try {
      restTemplate.postForObject(initNewDynUrl, map, Object.class);
    } catch (Exception e) {
      // 动态生成动态异常，不往外抛
      logger.error("生成成果动态出错,pubId={},psnId={}", new Object[] {pub.pubId, pub.psnId}, e);
    }
  }

}
