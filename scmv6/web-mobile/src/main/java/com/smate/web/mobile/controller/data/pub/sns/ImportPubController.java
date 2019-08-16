package com.smate.web.mobile.controller.data.pub.sns;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.vo.pdwh.PubImportVO;

/**
 * 成果导入接口
 * 
 * @author wsn
 * @date 2018年9月13日
 */

@RestController
public class ImportPubController {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/import/init", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object initImportPubInfo(@RequestBody PubImportVO importVo) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      importVo.setPsnId(SecurityUtils.getCurrentUserId());
      // importVo.setPsnId(1000000727634L);
      String inputXml = importVo.getInputXml();
      logger.debug("传入的xml内容：" + inputXml);
      if (StringUtils.isNotBlank(inputXml) && !NumberUtils.isNullOrZero(importVo.getPsnId())) {
        map = (Map<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.INIT_IMPORT_PUB, importVo,
            Object.class);
      } else {
        map.put("status", "error");
        map.put("errorMsg", "inputXml or psnId is null");
      }
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("初始化待导入成果数据出错", e);
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("list", map.get("list"));
    data.put("cacheKey", map.get("cacheKey"));
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("status"), "error")),
        Objects.toString(map.get("errorMsg"), ""));
  }

  /**
   * 保存待导入的成果
   * 
   * @param importVo
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/import/save", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object saveImportPubList(@RequestBody PubImportVO importVo) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      importVo.setPsnId(SecurityUtils.getCurrentUserId());
      // importVo.setPsnId(1000000727634L);
      if (StringUtils.isNotBlank(importVo.getPubJsonParams()) && !NumberUtils.isNullOrZero(importVo.getPsnId())
          && StringUtils.isNotBlank(importVo.getCacheKey())) {
        // 保存待导入的成果
        map = (Map<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.SAVE_IMPORT_PUB, importVo,
            Object.class);
      } else {
        map.put("status", "error");
        map.put("errorMsg", "PubJsonParams or psnId or cacheKey is null");
      }
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("保存待导入成果出错", e);
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("importSuccessNum", map.get("importSuccessNum"));
    data.put("recommendPsn", map.get("recommendPsn"));
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("status"), "error")),
        Objects.toString(map.get("errorMsg"), ""));
  }

}
