package com.smate.web.mobile.controller.data.group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.psn.model.info.PsnInfo;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.group.vo.GrpLabel;
import com.smate.web.mobile.group.vo.MobileGroupFileVO;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.GrpApiConsts;
import com.smate.web.mobile.v8pub.consts.MobileGrpFileApiConsts;

/**
 * @description 群组文件数据APP接口controller
 * @author xiexing
 * @date 2019年5月14日
 */
@Controller
public class MobileGrpFileDataController {
  private static final Logger logger = LoggerFactory.getLogger(MobileGrpFileDataController.class);

  @Value("${domainMobile}")
  private String domainMobile;

  @Autowired
  private RestTemplate restTemplate;

  /**
   * 查询群组文件数据
   * 
   * @return
   */
  @RequestMapping(value = "/data/grp/filelist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object queryGrpFileList(MobileGroupFileVO groupFile) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Long grpId = groupFile.getGrpId();
    Map<String, Object> result = new HashMap<String, Object>();
    if (checkParam(grpId, psnId)) {
      try {
        MultiValueMap<String, String> paramMap = getGrpPubListParam(groupFile);// 获取参数
        result = restTemplate.postForObject(domainMobile + MobileGrpFileApiConsts.QUERY_GRP_FILE_LIST,
            RestUtils.buildPostRequestEntity(paramMap), Map.class);
      } catch (Exception e) {
        logger.error("获取群组文件出错,groupFile={}", groupFile, e);
      }
    }
    if (result != null && result.size() > 0 && "200".equals(result.get("status"))) {
      Map<String, Object> resultMap =
          result.get("results") != null ? (Map<String, Object>) result.get("results") : new HashMap<String, Object>();
      return AppActionUtils.buildReturnInfo(resultMap.get("commentlist"),
          NumberUtils.toInt(Objects.toString(resultMap.get("total"), "0")),
          AppActionUtils.changeResultStatus("success"));
    }
    return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"));
  }

  /**
   * 构造群组文件参数
   * 
   * @param pubVO
   * @return
   */
  private MultiValueMap<String, String> getGrpPubListParam(MobileGroupFileVO groupFile) {
    Long psnId = SecurityUtils.getCurrentUserId();
    MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
    paramMap.add("psnId", psnId.toString());
    paramMap.add("grpId", groupFile.getGrpId().toString());
    paramMap.add("searchKey", groupFile.getSearchKey());
    paramMap.add("searchGrpFileMemberId", Objects.toString(groupFile.getMemberId(), ""));
    paramMap.add("des3GrpLabelIds", groupFile.getDes3GrpLabelId());
    paramMap.add("page.pageNo", Objects.toString(groupFile.getPageNo(), ""));
    paramMap.add("shortFileUrl", "true");
    if (groupFile.getWorkFileType() != null) {
      paramMap.add("workFileType", groupFile.getWorkFileType().toString());
    }
    if (groupFile.getCourseFileType() != null) {
      paramMap.add("courseFileType", groupFile.getCourseFileType().toString());
    }
    return paramMap;
  }

  /**
   * 获取群组文件筛选条件
   * 
   * @param groupFile
   * @return
   */
  @RequestMapping(value = "/data/grp/showconditions", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object showConditions(MobileGroupFileVO groupFile) {
    Long grpId = groupFile.getGrpId();
    Map<String, Object> result = new HashMap<String, Object>();
    Map<String, Object> resultMap = new HashMap<String, Object>();
    if (NumberUtils.isNotNullOrZero(grpId)) {
      try {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("grpId", grpId.toString());
        params.add("grpFileType", Objects.toString(groupFile.getGrpFileType()));
        result = restTemplate.postForObject(domainMobile + MobileGrpFileApiConsts.QUERY_CONDITIONS,
            RestUtils.buildPostRequestEntity(params), Map.class);
      } catch (Exception e) {
        logger.error("获取群组文件查询条件出错,grpId={}", grpId, e);
      }
    }
    if (result != null && result.size() > 0 && "200".equals(result.get("status"))) {
      resultMap.put("psnInfos", result.get("psnInfos") != null ? (List<PsnInfo>) result.get("psnInfos") : null);
      resultMap.put("grpLabels", result.get("grpLabels") != null ? (List<GrpLabel>) result.get("grpLabels") : null);
      return AppActionUtils.buildReturnInfo(resultMap, 0, AppActionUtils.changeResultStatus("success"));
    }
    return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"));
  }

  /**
   * 发送请求并返回结果
   * 
   * @param URI
   * @param type
   * @param params
   * @return
   */
  public Map<String, Object> sendReq(String URI, String type, String... params) {
    if (StringUtils.isEmpty(URI) || StringUtils.isEmpty(type)) {
      return null;
    }
    MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
    if ("fileList".equalsIgnoreCase(type) && params.length > 5) {
      paramMap.add("psnId", params[0]);
      paramMap.add("grpId", params[1]);
      paramMap.add("searchKey", params[2]);
      paramMap.add("searchGrpFileMemberId", params[3]);
      paramMap.add("des3GrpLabelIds", Des3Utils.encodeToDes3(params[4]));
      paramMap.add("page.pageNo", params[5]);
    } else if ("conditions".equalsIgnoreCase(type)) {
      paramMap.add("grpId", params[0]);
    }
    Map<String, Object> result =
        restTemplate.postForObject(domainMobile + URI, RestUtils.buildPostRequestEntity(paramMap), Map.class);
    return result;
  }



  /**
   * 收藏群组文件
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/collect/file", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object collectGrpDynFile(MobileGroupFileVO groupFile) {
    Map<String, Object> result = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (StringUtils.isNotBlank(groupFile.getDes3GrpFileId()) && NumberUtils.isNotNullOrZero(psnId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GrpFileId", groupFile.getDes3GrpFileId());
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        result = restTemplate.postForObject(domainMobile + GrpApiConsts.GRP_COLLECT_FILE,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("收藏群组文件异常，fileId={}, psnId={}", groupFile.getDes3GrpFileId(), psnId, e);
      result = result == null ? new HashMap<String, Object>() : result;
      result.put("status", "error");
    }
    return AppActionUtils.buildReturnInfo(result, 0,
        AppActionUtils.changeResultStatus(Objects.toString(result.get("status"), "error")));
  }

  /**
   * 参数校验
   * 
   * @param grpId
   * @param psnId
   * @return
   */
  public boolean checkParam(Long grpId, Long psnId) {
    if (NumberUtils.isNotNullOrZero(grpId) && NumberUtils.isNotNullOrZero(psnId)) {
      return true;
    }
    return false;
  }
}
