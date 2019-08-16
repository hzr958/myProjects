package com.smate.web.v8pub.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.cache.SnsCacheService;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.consts.PubImportConstants;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.service.restful.PublicImportAndConfirmPubService;
import com.smate.web.v8pub.service.restful.PublicPubDupCheckService;
import com.smate.web.v8pub.service.searchimport.ImportOtherPubService;
import com.smate.web.v8pub.service.searchimport.PubImportService;
import com.smate.web.v8pub.vo.PubListVO;
import com.smate.web.v8pub.vo.importfile.ChangePub;
import com.smate.web.v8pub.vo.importfile.ImportSaveVo;
import com.smate.web.v8pub.vo.searchimport.PubImportVO;

/**
 * 成果导入控制器
 * 
 * @author wsn
 * @date 2018年8月9日
 */
@Controller
public class PubImportController {

  private static Logger logger = LoggerFactory.getLogger(PubImportController.class);
  @Value("${domainscm}")
  private String snsDomain; // 科研之友域名
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private HttpServletRequest request;
  @Autowired
  private PubImportService pubImportService;
  @Autowired
  private ImportOtherPubService importOtherPubService;
  @Autowired
  private PublicPubDupCheckService publicPubDupCheckService;
  @Autowired
  private PublicImportAndConfirmPubService publicImportAndConfirmPubService;
  @Autowired
  private SnsCacheService cacheService;

  /**
   * 成果联邦检索导入第一步，进入联邦检索页面
   * 
   * @param importVo
   * @return
   */
  @RequestMapping("/pub/import/search")
  public ModelAndView pubImportFirstStep(@ModelAttribute PubImportVO importVo) {
    ModelAndView view = new ModelAndView();
    try {
      importVo.setSnsDomain(snsDomain);
      // 设置当前登录人员ID
      importVo.setPsnId(SecurityUtils.getCurrentUserId());
      // 设置是本人检索
      importVo.setIsInitRadio(PubImportConstants.SEARCH_ME);
      // 设置要查询的文献库类型为成果
      importVo.setDbType(String.valueOf(PubImportConstants.OUTPUT));
      // 初始化检索页面所需信息
      pubImportService.initSearchInfo(importVo);
      // 获取人员所有的单位信息
      List<WorkHistory> workList = pubImportService.findPsnAllInsInfo(importVo.getPsnId());
      importVo.setWorkList(workList);
      // 设置首要单位信息（workList中第一个单位信息）
      for (WorkHistory work : workList) {
        if (CommonUtils.compareLongValue(work.getIsPrimary(), 1L)) {
          importVo.setPrimaryWork(work);
          break;
        }
      }
      // 当前年份
      Calendar cal = Calendar.getInstance();
      importVo.setCurrentYear(cal.get(Calendar.YEAR));
      // 获取用户使用的操作系统
      this.getUserOperationSys(importVo);
    } catch (Exception e) {
      logger.error("进入联邦检索第一步页面出错", e);
    }
    view.addObject("importVo", importVo);
    view.setViewName("pub/import/search/search_import_first_step");
    return view;
  }

  /**
   * 显示待导入的成果列表
   * 
   * @param importVo
   * @return
   */
  @RequestMapping(value = "/pub/import/ajaxinit", method = RequestMethod.POST)
  @ResponseBody
  public ModelAndView initImportPubList(@ModelAttribute("importVo") PubImportVO importVo) {
    ModelAndView view = new ModelAndView();
    try {
      importVo.setPsnId(SecurityUtils.getCurrentUserId());
      String inputXml = URLDecoder.decode(importVo.getInputXml(), "UTF-8");
      logger.debug("传入的xml内容：" + inputXml);
      if (StringUtils.isNotBlank(inputXml)) {
        importVo.setInputXml(inputXml);
        // 先将传入的xml构建成PendingImportPubVO对象
        pubImportService.buildPendingImportPubByXml(importVo);
        // 处理一些其他信息
        pubImportService.initImportPubInfo(importVo);
      }
    } catch (Exception e) {
      importVo.setValidPub(false);
      logger.error("初始化待导入成果数据出错", e);
    }
    view.addObject("importVo", importVo);
    view.setViewName("pub/import/search/import_pub_list");
    return view;
  }

  /**
   * 保存待导入的成果
   * 
   * @param importVo
   * @return
   */
  @RequestMapping(value = "/pub/import/ajaxsave", method = RequestMethod.POST)
  @ResponseBody
  public ModelAndView showImportPubList(@ModelAttribute PubImportVO importVo) {
    ModelAndView view = new ModelAndView();
    try {
      if (StringUtils.isNotBlank(importVo.getPubJsonParams())) {
        // 参数被xss转义掉了，这边转义回来
        String pubJsonParams = StringEscapeUtils.unescapeHtml4(importVo.getPubJsonParams());
        JSONArray jsonArray = JSONArray.parseArray(pubJsonParams);
        importVo.setPubJsonList(jsonArray);
        importVo.setPsnId(SecurityUtils.getCurrentUserId());
        // 保存待导入的成果
        pubImportService.saveImportPub(importVo);
      }
    } catch (Exception e) {
      logger.error("保存待导入成果出错", e);
    }
    view.addObject("importVo", importVo);
    view.setViewName("pub/import/search/import_pub_result");
    return view;
  }

  /**
   * 进入插件更新或安装提示页面
   * 
   * @param importVo
   * @return
   */
  @RequestMapping("/pub/import/plugin")
  public ModelAndView showPluginInstallOrUpdate(@ModelAttribute PubImportVO importVo) {
    ModelAndView view = new ModelAndView();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      importVo.setPsnId(psnId);
      this.getUserOperationSys(importVo);
    } catch (Exception e) {
      logger.error("进入插件更新或安装提示页面出错,psnId = " + importVo.getPsnId(), e);
    }
    view.addObject("importVo", importVo);
    view.setViewName("pub/import/search/install_or_update_plugin");
    return view;

  }

  /**
   * 
   * 导入他人成果到我的成果
   * 
   * @param importVo
   * @return
   */
  @RequestMapping("/pub/snspub/importotherpubtomypub")
  @ResponseBody
  public Object ajaxsnspubimportmypub(@ModelAttribute PubImportVO importVo) {
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

  /**
   * 
   * 导入基准库成果到我的成果
   * 
   * @param importVo
   * @return
   */
  @RequestMapping("/pub/snspub/importpdwhpubtomypub")
  @ResponseBody
  public String ajaximportpdwhpub(@ModelAttribute("des3PubId") String pubId) {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (!NumberUtils.isNullOrZero(psnId)) {
        // 直接调用接口保存
        Map<String, String> map = new HashMap<String, String>();
        map.put("des3PubId", pubId);
        map.put("des3PsnId", Des3Utils.encodeToDes3(psnId + ""));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(map), headers);
        String dupUrl = snsDomain + "/data/pub/import/pdwhpubtomypub";
        String result = restTemplate.postForObject(dupUrl, entity, String.class);
        if ("SUCCESS".equals(JacksonUtils.jsonToMap(result).get("status").toString())) {
          if ("dup".equals(JacksonUtils.jsonToMap(result).get("msg").toString())) {
            resultMap.put("result", "dup");
          } else {
            resultMap.put("result", "success");
          }
        } else {
          resultMap.put("result", "error");
        }
      } else {
        resultMap.put("result", "error");
      }
    } catch (Exception e) {
      resultMap.put("result", "error");
      logger.error("导入基准库成果异常,des3PubId=" + pubId, e);
    }
    return JacksonUtils.mapToJsonStr(resultMap);
  }

  /**
   * 添加成果，检索、批量导入基准库成果到我的成果
   */
  @RequestMapping("/pub/snspub/ajaximportlist")
  @ResponseBody
  public ModelAndView ajaximportpdwhpubs(@ModelAttribute("des3PubIds") String des3PubIds) {
    ModelAndView view = new ModelAndView();
    PubListVO pubListVO = new PubListVO();
    List<PubInfo> pubInfoList = new ArrayList<PubInfo>();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId == null || psnId == 0L) {// 没有登录
      return null;
    }
    Integer index = 0;
    for (String des3PubId : des3PubIds.split(",")) {
      try {
        Long pdwhPubId = Long.valueOf(Des3Utils.decodeFromDes3(des3PubId));
        // 直接调用接口保存
        Map<String, String> map = new HashMap<String, String>();
        map.put("des3PubId", des3PubId);
        map.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        PubInfo pubInfo = new PubInfo();
        // 调用查重接口
        String dupResult = publicPubDupCheckService.getPdwhPubDupcheckStatus(psnId, pdwhPubId);
        if ("SUCCESS".equals(JacksonUtils.jsonToMap(dupResult).get("status").toString())) {
          if (JacksonUtils.jsonToMap(dupResult).get("msg") != null) {
            pubInfo.setDupPubId(Long.valueOf(JacksonUtils.jsonToMap(dupResult).get("msg").toString()));
            pubInfo.setIsInsert(1);
          }
        }
        // 基准信息
        pubInfo.setPubId(pdwhPubId);
        pubImportService.buildPdwhPubInfo(pubInfo, psnId);
        pubInfo.setIndex(++index);
        pubInfoList.add(pubInfo);
      } catch (Exception e) {
        logger.error("导入基准库成果异常,psnId:" + psnId, e);
      }
    }
    pubListVO.setResultList(pubInfoList);
    pubListVO.setPubTypes(pubImportService.getAllPubType());
    view.addObject(pubListVO);
    view.setViewName("/pub/import/search/collect_imp_pubList");
    return view;
  }

  /**
   * 导入科研之友成果-切换成果类型进行查重
   * 
   * @param pubType
   * @param pubId
   * @return
   */
  @RequestMapping(value = "/pub/dup/checkpubtype")
  @ResponseBody
  public String checkPubTypeDup(@ModelAttribute("pubType") Integer pubType, @ModelAttribute("pubId") Long pubId) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (NumberUtils.isNullOrZero(psnId)) {
        // 没有登录
        resultMap.put("result", "error");
        return JacksonUtils.mapToJsonStr(resultMap);
      }
      if (NumberUtils.isNullOrZero(pubId) || !NumberUtils.isNotNullOrZero(pubType)) {
        resultMap.put("result", "error");
        return JacksonUtils.mapToJsonStr(resultMap);
      }
      // 调用查重接口
      String dupResult = publicPubDupCheckService.dupByPubType(psnId, pubId, pubType);
      Map<Object, Object> dupMap = JacksonUtils.jsonToMap(dupResult);
      if ("SUCCESS".equals(dupMap.get("status").toString())) {
        if (dupMap.get("msg") != null) {
          Long dupPubId = Long.valueOf(dupMap.get("msg").toString());
          resultMap.put("result", dupPubId);
        } else {
          resultMap.put("result", "no_dup");
        }
      } else {
        resultMap.put("result", "no_dup");
      }
    } catch (Exception e) {
      logger.error("切换类型查重失败！pubId={}", pubId, e);
      resultMap.put("result", "error");
    }
    return JacksonUtils.mapToJsonStr(resultMap);
  }

  /**
   * 文件导入，联邦检索-切换成果类型进行查重
   * 
   * @param pubType
   * @param index
   * @param cacheKey
   * @return
   */
  @RequestMapping(value = "/pub/dup/checkpubtype2")
  @ResponseBody
  public String checkPubTypeDup(@ModelAttribute("pubType") Integer pubType, @ModelAttribute("index") Integer index,
      @ModelAttribute("cacheKey") String cacheKey) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (NumberUtils.isNullOrZero(psnId)) {
        // 没有登录
        resultMap.put("result", "error");
        return JacksonUtils.mapToJsonStr(resultMap);
      }
      resultMap = publicPubDupCheckService.dupByPubType(psnId, cacheKey, pubType, index);
    } catch (Exception e) {
      logger.error("文件导入或者联邦检索切换类型查重失败！", e);
      resultMap.put("result", "error");
    }
    return JacksonUtils.mapToJsonStr(resultMap);
  }

  /**
   * 添加成果，检索、批量导入基准库成果到我的成果
   */
  @SuppressWarnings("rawtypes")
  @RequestMapping("/pub/snspub/ajaxsaveimportlist")
  @ResponseBody
  public ModelAndView saveimportpdwhpubs(@ModelAttribute("saveJson") String saveJson) {
    ModelAndView view = new ModelAndView();
    ImportSaveVo importSaveVo = JacksonUtils.jsonObject(saveJson, ImportSaveVo.class);
    Long psnId = SecurityUtils.getCurrentUserId();
    Integer successCount = 0;
    Integer totalCount = 0;
    for (ChangePub pub : importSaveVo.getChangPubList()) {
      if (pub.getRepeatSelect() != 0) {// 如果不是跳过，判断是更新还是新增
        Set<PubSituationBean> situationList = pubImportService.buildSitationList(pub.getSitations(), pub.getPubId());
        Integer pubType = pub.getPubType();
        if (pub.getRepeatSelect() == 1) {// 更新个人查重到的成果
          Long dupPubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(pub.getDupPubId()));
          String saveResult = pubImportService.updateSnsPub(dupPubId, psnId, pub.getPubId(), pubType, situationList);
          if (StringUtils.isNotBlank(saveResult)) {
            Map saveResultMap = JacksonUtils.jsonToMap(saveResult);
            if ("SUCCESS".equals(saveResultMap.get("status").toString())) {
              successCount++;
              totalCount++;
            }
          }
        }
        if (pub.getRepeatSelect() == 2) {// 新增基准库成果到个人
          // 2.导入成果到个人库
          String saveResult = publicImportAndConfirmPubService.importAndConfirmPdwhPub(pub.getPubId(), psnId, pubType,
              situationList, null);
          Map saveResultMap = JacksonUtils.jsonToMap(saveResult);
          if ("SUCCESS".equals(saveResultMap.get("status").toString())) {
            successCount++;
            totalCount++;
          }
        }

      }
    }
    view.addObject("totalCount", totalCount);
    view.setViewName("/pub/import/search/collect_imp_result");
    return view;

  }

  /**
   * 获取单位别名
   * 
   * @param importVo
   * @return
   */
  @RequestMapping(value = "/pub/orgname/ajaxalias", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String findOrgNameAlias(@ModelAttribute PubImportVO importVo) {
    try {
      return pubImportService.getOrgNameAlias(importVo.getOrgName(), importVo.getDbCode());
    } catch (Exception e) {
      logger.error("获取单位别名出错,psnId = " + importVo.getPsnId() + ", orgName = " + importVo.getOrgName() + ", dbCode = "
          + importVo.getDbCode(), e);
    }
    return "";
  }

  @RequestMapping(value = "/pub/cache/ajaxremove")
  @ResponseBody
  public Object removeCahcePendingImportPub(@ModelAttribute PubImportVO importVo) {
    Map<String, String> map = new HashMap<String, String>();
    try {
      cacheService.remove("searchImportPubCache", importVo.getCacheKey());
      map.put("result", "success");
    } catch (Exception e) {
      logger.error(
          "删除掉缓存的待导入成果出错, psnId = " + SecurityUtils.getCurrentUserId() + ", cacheKey = " + importVo.getCacheKey(), e);
      map.put("result", "error");
    }
    return map;
  }

  // 获取用户使用的操作系统
  protected void getUserOperationSys(PubImportVO importVo) {
    String agent = request.getHeader("User-Agent").toLowerCase();
    String reqSystem = "Windows";
    if (agent.indexOf("linux") != -1) {
      reqSystem = "Linux";
    } else if (agent.indexOf("macintosh") != -1) {
      reqSystem = "Macintosh";
    }
    importVo.setSystemType(reqSystem);
  }

}
