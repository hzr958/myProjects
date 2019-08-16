package com.smate.core.base.utils.service.psn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.constant.SolrConstants;
import com.smate.core.base.utils.json.JacksonUtils;

@Service("psnSolrInfoModifyService")
@Transactional(rollbackFor = Exception.class)
public class PsnSolrInfoModifyServiceImpl implements PsnSolrInfoModifyService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${initOpen.restful.url}")
  private String openServerUrl;

  /**
   * 若不需要很及时的更新，请使用personalManager.refreshPsnSolrInfoByTask(form.getPsnId());
   */
  @Override
  public void updateSolrPsnInfo(Long psnId) {
    if (psnId == null) {
      logger.info("solr更新人员信息psnId为空！");
      return;
    }
    // openServerUrl = "http://127.0.0.1:49080/center-open/scmopendata";
    RestTemplate restTemplate = new RestTemplate();
    Map restfulMap = new HashMap<String, Object>();
    Map dataMap = new HashMap<String, Object>();
    restfulMap.put(SolrConstants.SOLR_OPEN_ID, "99999999");
    restfulMap.put(SolrConstants.SOLR_TOKEN, "00000000psn2solr");
    dataMap.put("psn_id", psnId);
    restfulMap.put(SolrConstants.SOLR_RESTFULL_DATA, JacksonUtils.mapToJsonStr(dataMap));
    Object obj = restTemplate.postForObject(openServerUrl, restfulMap, Object.class);

    if (obj != null) {
      Map<String, Object> map = JacksonUtils.jsonToMap(obj.toString());
      // 返回结果为ArrayList,详见open返回值说明
      List<Object> status = (List<Object>) map.get(SolrConstants.SOLR_RESTFULL_RESULT);
      try {
        Integer result = Integer
            .parseInt(String.valueOf(((Map<String, Object>) status.get(0)).get(SolrConstants.SOLR_RETURN_STATUS)));
        if (result == 1) {
          logger.info("solr更新人员索引信息成功，psnId = " + psnId);
        } else {
          logger.info("solr更新人员索引信息失败，psnId = " + psnId);
        }
      } catch (Exception e) {
        logger.error("solr更新人员索引信息失败，psnId = " + psnId, e);
      }
    }
  }

  @Override
  public void deleteSolrPsnInfo(Long psnId) {
    if (psnId == null) {
      logger.info("solr删除人员信息psnId为空！");
      return;
    }
    // openServerUrl = "http://127.0.0.1:49080/center-open/scmopendata";
    RestTemplate restTemplate = new RestTemplate();
    Map restfulMap = new HashMap<String, Object>();
    Map dataMap = new HashMap<String, Object>();
    restfulMap.put(SolrConstants.SOLR_OPEN_ID, "99999999");
    restfulMap.put(SolrConstants.SOLR_TOKEN, "00000000psndsolr");
    dataMap.put("psn_id", psnId);
    restfulMap.put(SolrConstants.SOLR_RESTFULL_DATA, JacksonUtils.mapToJsonStr(dataMap));
    Object obj = restTemplate.postForObject(openServerUrl, restfulMap, Object.class);

    if (obj != null) {
      Map<String, Object> map = JacksonUtils.jsonToMap(obj.toString());
      // 返回结果为ArrayList,详见open返回值说明
      List<Object> status = (List<Object>) map.get(SolrConstants.SOLR_RESTFULL_RESULT);
      try {
        Integer result = Integer
            .parseInt(String.valueOf(((Map<String, Object>) status.get(0)).get(SolrConstants.SOLR_RETURN_STATUS)));
        if (result == 1) {
          logger.info("solr删除人员索引信息成功，psnId = " + psnId);
        } else {
          logger.info("solr删除人员索引信息失败，psnId = " + psnId);
        }
      } catch (Exception e) {
        logger.error("solr删除人员索引信息失败，psnId = " + psnId, e);
      }
    }
  }

  public static void main(String[] args) {

    PsnSolrInfoModifyServiceImpl psi = new PsnSolrInfoModifyServiceImpl();
    // psi.deleteSolrPsnInfo(15629L);
    psi.updateSolrPsnInfo(15629L);

  }
}
