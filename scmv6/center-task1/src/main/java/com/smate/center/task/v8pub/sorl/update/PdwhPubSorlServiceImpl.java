package com.smate.center.task.v8pub.sorl.update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubIndexUrlDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.dao.pdwh.PdwhDataTaskDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;


@Service("pdwhPubSorlService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubSorlServiceImpl implements PdwhPubSorlService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String scmDomain;

  @Autowired
  private PdwhDataTaskDAO pdwhDataTaskDAO;
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;

  @Override
  public List<PdwhDataTaskPO> findPdwhId(Long startId, Long endId, Integer size) {
    return pdwhDataTaskDAO.findSorlPdwhPubId(startId, endId, size);
  }

  @Override
  public void save(PdwhDataTaskPO pubData) {
    pdwhDataTaskDAO.saveOrUpdate(pubData);
  }


  @Override
  public void updatePdwhPubSorl(PdwhDataTaskPO pubData) throws ServiceException {
    try {
      Long pdwhPubId = pubData.getPubId();
      if (NumberUtils.isNotNullOrZero(pdwhPubId)) {
        // 构建参数
        Map<String, Object> parameters = buildParameters(pdwhPubId);
        // 访问V8pub系统接口更新sorl索引
        String SERVER_URL = scmDomain + V8pubQueryPubConst.PDWHUPDATESORL_URL;
        String result = restTemplate.postForObject(SERVER_URL, parameters, String.class);
        // 接口返回数据处理
        Map<String, Object> resultMap = JacksonUtils.jsonToMap(result);
        // 获取处理结果
        if (resultMap.get("result") != null) {
          String res = (String) resultMap.get("result");
          if (res.equalsIgnoreCase("SUCCESS")) {
            pubData.setStatus(4);
          } else {
            pubData.setStatus(-2);
            pubData.setError("更新sorl失败");
          }
        }
      }
      save(pubData);
    } catch (Exception e) {
      logger.error("更新基准库sorl数据出错！", e);
      throw new ServiceException(e);
    }

  }

  private Map<String, Object> buildParameters(Long pdwhPubId) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("des3PubId", Des3Utils.encodeToDes3(pdwhPubId + ""));
    return params;
  }


}
