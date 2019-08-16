package com.smate.web.v8pub.controller.data;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.pub.util.DisposeDes3IdUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.enums.PubHandlerStatusEnum;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.restful.PublicImportAndConfirmPubService;
import com.smate.web.v8pub.service.restful.PublicPubDupCheckService;
import com.smate.web.v8pub.service.searchimport.ImportOtherPubService;
import com.smate.web.v8pub.service.searchimport.PubImportService;
import com.smate.web.v8pub.service.sns.homepage.PubConfirmService;
import com.smate.web.v8pub.vo.PendingImportPubVO;
import com.smate.web.v8pub.vo.searchimport.PubImportVO;

/**
 * 成果导入接口
 * 
 * @author wsn
 * @date 2018年9月13日
 */

@RestController
public class ImportPubController {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource
  private PubImportService pubImportService;
  @Resource
  private PubPdwhDetailService pubPdwhDetailService;
  @Resource
  private PubConfirmService PubConfirmService;
  @Resource
  private PublicPubDupCheckService publicPubDupCheckService;
  @Autowired
  private PublicImportAndConfirmPubService publicImportAndConfirmPubService;
  @Autowired
  private ImportOtherPubService importOtherPubService;

  /**
   * 初始化待导入成果
   * 
   * @param importVo
   * @return
   */
  @SuppressWarnings("rawtypes")
  @PostMapping("/data/pub/import/init")
  public Object initImportPubInfo(@RequestBody PubImportVO importVo) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      String inputXml = URLDecoder.decode(importVo.getInputXml(), "UTF-8");
      logger.debug("传入的xml内容：" + inputXml);
      if (StringUtils.isNotBlank(inputXml) && !NumberUtils.isNullOrZero(importVo.getPsnId())) {
        importVo.setInputXml(inputXml);
        // 先将传入的xml构建成PendingImportPubVO对象
        pubImportService.buildPendingImportPubByXml(importVo);
        // 处理一些其他信息
        pubImportService.initImportPubInfo(importVo);
        List<PendingImportPubVO> importPubs = importVo.getPendingImportPubs();
        if (CollectionUtils.isNotEmpty(importPubs)) {
          importPubs.forEach(p -> p.setJsonPub(""));
        }
        map.put("status", "success");
        map.put("list", importVo.getPendingImportPubs());
        map.put("cacheKey", importVo.getCacheKey());
      } else {
        map.put("status", "inputXml or psnId is null");
      }
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("初始化待导入成果数据出错", e);
    }
    return map;
  }

  /**
   * 保存待导入的成果
   * 
   * @param importVo
   * @return
   */
  @PostMapping(value = "/data/pub/import/save")
  public Object saveImportPubList(@RequestBody PubImportVO importVo) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(importVo.getPubJsonParams())) {
        JSONArray jsonArray = JSONArray.parseArray(importVo.getPubJsonParams());
        importVo.setPubJsonList(jsonArray);
        importVo.setPsnId(SecurityUtils.getCurrentUserId());
        // 保存待导入的成果
        pubImportService.saveImportPub(importVo);
        map.put("status", "success");
        map.put("importSuccessNum", importVo.getImportSuccessSize());
        map.put("recommendPsn", importVo.getRecommendPsn());
      } else {
        map.put("status", "error");
        map.put("errorMsg", "PubJsonParams is null");
      }
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("保存待导入成果出错", e);
    }
    return map;
  }

  /**
   * 
   * 导入基准库成果到我的成果
   * 
   * @param importVo
   * @return
   */
  @PostMapping(value = "/data/pub/import/pdwhpubtomypub", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String ajaxpdwhpubimportmypub(@RequestBody String jsonData) {
    Map<String, String> result = new HashMap<String, String>();
    Map<String, String> dataMap = JacksonUtils.jsonToMap(jsonData);
    if (StringUtils.isNotBlank(dataMap.get("des3PubId"))) {
      Long pdwhPubId = DisposeDes3IdUtils.disposeDes3Id(null, dataMap.get("des3PubId"));
      Long psnId = DisposeDes3IdUtils.disposeDes3Id(null, dataMap.get("des3PsnId"));
      // 1先调用查重接口
      String dupResult = publicPubDupCheckService.getPdwhPubDupcheckStatus(psnId, pdwhPubId);
      Map<String, Object> dupResultMap = JacksonUtils.jsonToMap(dupResult);
      if ("SUCCESS".equals(dupResultMap.get("status").toString())) {
        if (dupResultMap.get("msg") != null) {// 成果已经存在
          result.put("status", "SUCCESS");
          result.put("msg", "dup");// 有重复成果
        } else {
          // 2.导入成果到个人库
          String saveResult =
              publicImportAndConfirmPubService.importAndConfirmPdwhPub(pdwhPubId, psnId, null, null, null);
          Map<String, Object> saveResultMap = JacksonUtils.jsonToMap(saveResult);
          if ("SUCCESS".equals(saveResultMap.get("status").toString())) {
            result.put("status", PubHandlerStatusEnum.SUCCESS.getValue());
            result.put("msg", "成果已导入到个人库");
          } else {
            result.put("status", PubHandlerStatusEnum.ERROR.getValue());
            result.put("msg", "成果导入失败");
          }
        }
      }
    } else {
      result.put("status", "error");
      result.put("msg", "加密pubId为空");

    }
    return JacksonUtils.mapToJsonStr(result);
  }
  /**
   * 
   * 导入他人成果到我的成果，给移动端调用
   * 
   * @param importVo
   * @return
   */
  @RequestMapping("/data/pub/sns/importtomypub")
  public Object ajaxsnspubimportmypub(@RequestBody PubImportVO importVo) {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (importVo.getCurrentPsnId() != null && importVo.getCurrentPsnId() != 0L) {
        map = importOtherPubService.importPub(importVo);
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("导入他人成果异常" + importVo.getPsnId(), e);
    }
    return map;
  }

}
