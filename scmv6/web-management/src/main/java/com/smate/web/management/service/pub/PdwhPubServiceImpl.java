package com.smate.web.management.service.pub;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.management.dao.pub.PubOperateLogDao;
import com.smate.web.management.dao.pub.PubPdwhDAO;
import com.smate.web.management.model.pub.PubInfoForm;
import com.smate.web.management.model.pub.PubOperateLog;
import com.smate.web.management.model.pub.PubPdwhPO;

@Service("pdwhPubService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubServiceImpl implements PdwhPubService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PubOperateLogDao pubOperateLogDao;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String scmDomain;

  @Override
  public void getPubList(PubInfoForm form) {
    PubPdwhPO pubPdwh = form.getPubPdwhPO();
    List<PubPdwhPO> pubList = pubPdwhDAO.findPubList(pubPdwh, form.getPage());
    if (pubList != null && pubList.size() > 0) {
      form.setPubInfoList(pubList);
    }
  }

  @Override
  public PubPdwhPO get(Long pubId) {
    try {
      return pubPdwhDAO.get(pubId);
    } catch (Exception e) {
      logger.error("查询成果出错！pubId={}", pubId);
    }
    return null;
  }

  @Override
  public String deletePub(String des3PubId) throws ServiceException {
    String url = scmDomain + V8pubQueryPubConst.PUBHANDLER_URL;
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("des3PubId", des3PubId);
    map.put("pubHandlerName", "deletePdwhPubHandler");
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(map), headers);
    String result = restTemplate.postForObject(url, entity, String.class);
    return result;
  }

  @Override
  public void savePubOperateLog(Long pubId, Long psnId, Long opType, String descMsg) throws Exception {
    try {
      PubOperateLog pubOperateLog = new PubOperateLog(psnId, pubId, opType, descMsg, new Date());
      pubOperateLogDao.save(pubOperateLog);
    } catch (Exception e) {
      logger.error("管理系统记录成果操作日志出错！pubId={}，psnId={}", pubId, psnId);
      throw new Exception();
    }
  }
}
