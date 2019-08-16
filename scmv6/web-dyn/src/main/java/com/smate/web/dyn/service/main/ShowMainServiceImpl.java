package com.smate.web.dyn.service.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.dyn.form.main.MainForm;

/**
 * 主页显示服务类
 * 
 * @author zzx
 *
 */
@Service("showMainService")
@Transactional(rollbackOn = Exception.class)
public class ShowMainServiceImpl implements ShowMainService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonDao personDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;

  @Override
  public void main(MainForm form) throws Exception {
    form.setDes3PsnId(Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
    form.setAvatars(personDao.getPsnImgByObjectId(SecurityUtils.getCurrentUserId()));
  }

  @Override
  public void mainShortcuts(MainForm form) throws Exception {
    Integer status = 0;
    Map<String, Object> map = buildParams(form);
    // 调open接口发送消息
    Object resultData = restTemplate.postForObject(this.openResfulUrl, map, Object.class);
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(resultData.toString());
    // "{\"result\":[{\"endTime\":\"\",\"status\":\"0\"}],\"openid\":\"99999999\",\"time\":10520,\"status\":\"success\"}"
    if (resultMap != null && "success".equals(resultMap.get("status"))) {
      List<Map<String, Object>> mapList = (List<Map<String, Object>>) resultMap.get("result");
      Object obj = mapList.get(0).get("status");
      status = Integer.parseInt(obj == null ? "0" : String.valueOf(obj));
    } else {
      logger.error("科研验证受理接口调用失败！原因：{}", resultMap.get("msg"));
    }
    form.setStatus(status);
  }

  private Map<String, Object> buildParams(MainForm form) {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put("psnId", form.getPsnId());
    dataMap.put("appType", "2");// appType=1，开题分析；appType=2科研验证
    dataMap.put("ip", form.getIp());// 用户端ip
    map.put("openid", "99999999");
    map.put("token", "00000000kpipayv1");
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }
}
