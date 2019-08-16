package com.smate.core.base.file.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.common.MD5Util;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 获取文件下载地址服务类接口，用于获取各种文件类型的下载地址
 * 
 * @author houchuanjie
 * @date 2017-11-27
 * 
 */
@Service
public class FileDownloadUrlServiceImpl implements FileDownloadUrlService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${domainscm}")
  private String domain;
  @Autowired
  private RestTemplate restTemplate;

  @Resource(name = "snsCacheService")
  private CacheService cacheService;

  /**
   * open系统token
   */
  private static final String OPEN_TOKEN = "00000000sht22url";
  /**
   * openid
   */
  private static final Long OPEN_ID = 99999999L;

  private static final char[] CONST_CHARS =
      new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
          'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
          'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

  /**
   * @param pubId 成果id
   */
  @Override
  public String getDownloadUrl(FileTypeEnum fileType, Long pubId) {
    // 调整成隐藏参数 的方式
    String key = RandomStringUtils.random(4, CONST_CHARS);
    String a = MD5Util.string2MD5(pubId + key + fileType.getValue());
    String v = ";" + pubId + ";" + fileType.getValue();
    cacheService.put(DOWNLOAD_CACHE_MARK, 1 * 60 * 60, a, v);
    String url = domain + "/fileweb/filedownload?" + DOWNLOAD_KEY + "=" + a;
    return url;
  }

  /**
   * @param fileId 文件id
   * @param pubId 成果id
   */
  @Override
  public String getDownloadUrl(FileTypeEnum fileType, Long fileId, Long pubId) {
    // 调整成隐藏参数 的方式
    String key = RandomStringUtils.random(4, CONST_CHARS);
    String a = MD5Util.string2MD5(fileId + key + fileType.getValue());
    String v = fileId + ";" + ObjectUtils.defaultIfNull(pubId, "") + ";" + fileType.getValue();
    cacheService.put(DOWNLOAD_CACHE_MARK, 1 * 60 * 60, a, v);
    String url = domain + "/fileweb/filedownload?" + DOWNLOAD_KEY + "=" + a;
    return url;
  }

  @Override
  public String getZipDownloadUrl(FileTypeEnum type, String des3Ids) {
    String key = RandomStringUtils.random(4, CONST_CHARS);
    cacheService.put(DOWNLOAD_CACHE_MARK, 1 * 60 * 60, MD5Util.string2MD5(des3Ids + key), key);
    String url = domain + "/fileweb/batchDownload?des3fids=" + URLEncoder.encode(des3Ids) + "&fileType="
        + type.getValue() + "&key=" + key;
    return url;
  }

  /**
   * 附件地址
   */
  @Override
  public String getDownloadAttachmentUrl(FileTypeEnum fileType, Long id, Long pubId) {
    String key = RandomStringUtils.random(4, CONST_CHARS);
    String a = MD5Util.string2MD5(id + pubId + key + fileType.getValue());
    String v = id + ";" + pubId + ";" + fileType.getValue();
    cacheService.put(DOWNLOAD_CACHE_MARK, 1 * 60 * 60, a, v);
    String url = domain + "/fileweb/filedownload?" + DOWNLOAD_KEY + "=" + a;
    return url;
  }

  @SuppressWarnings("unchecked")
  @Override
  public String getShortDownloadUrl(FileTypeEnum fileType, Long id) {
    String url = "";
    String reqUrl = domain + "/scmopendata";
    try {
      HashMap<String, Object> reqParam = buildShortUrlRequestParam(fileType, id, null);
      Map<?, ?> resultMap = sendMsgToOpenData(reqUrl, reqParam);
      if (resultMap != null && "success".equals(resultMap.get("status"))) {
        List<Map<String, Object>> map = (List<Map<String, Object>>) resultMap.get("result");
        if (map != null && map.size() > 0) {
          String shortUrl = map.get(0).get("shortUrl").toString();
          url = domain + "/" + ShortUrlConst.F_TYPE + "/" + shortUrl;
        } else {
          logger.error("生成文件短地址短地址出错，远程调用接口返回数据: {}", resultMap);
        }
      } else {
        logger.error("生成文件短地址出错，远程调用接口返回数据：{}", resultMap);
      }
    } catch (Exception e) {
      logger.error("生成文件短地址出错，fileId={}。\n异常信息: {}", id, e);
    }
    return url;
  }

  private HashMap<String, Object> buildShortUrlRequestParam(FileTypeEnum fileType, Long fileId, Long pubId) {
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> data = new HashMap<String, Object>();
    HashMap<String, Object> shortUrlParamet = new HashMap<String, Object>();
    HashMap<String, Object> paramMap = new HashMap<String, Object>();
    /**
     * {openid: '', token:"", data:{createPsnId: '', type:"F", shortUrlParamet: {des3FileId:
     * '',typeFile:''}}}
     */
    try {
      paramMap.put("openid", OPEN_ID);
      paramMap.put("token", OPEN_TOKEN);
      data.put("createPsnId", "0");
      data.put("type", ShortUrlConst.F_TYPE);
      shortUrlParamet.put("des3FileId", Des3Utils.encodeToDes3(fileId.toString()));
      shortUrlParamet.put("fileType", fileType.getValue());
      if (pubId != null) {
        shortUrlParamet.put("pubId", pubId.toString());
      }
      data.put("shortUrlParamet", mapper.writeValueAsString(shortUrlParamet));
      paramMap.put("data", mapper.writeValueAsString(data));
      return paramMap;
    } catch (JsonProcessingException e) {
      logger.error("生成文件下载短地址出现错误，原因：构建/scmopendata接口请求信息出错，fileId={}, fileType={}", fileId, fileType, e);
      return null;
    }
  }

  /**
   * 向open接口发送请求数据包，获取文件下载的短地址信息
   * 
   * @param reqUrl open接口的地址
   * @param paramMap 请求参数
   * @return 响应结果json数据
   */
  private Map<?, ?> sendMsgToOpenData(String reqUrl, Map<String, Object> paramMap) {
    Object resultData = restTemplate.postForObject(reqUrl, paramMap, Object.class);
    Map<?, ?> resultMap = JacksonUtils.jsonToMap(resultData.toString());
    return resultMap;
  }

  @Override
  public String getShortDownloadUrl(FileTypeEnum fileType, Long id, Long pubId) {
    String url = "";
    String reqUrl = domain + "/scmopendata";
    try {
      HashMap<String, Object> reqParam = buildShortUrlRequestParam(fileType, id, pubId);
      Map<?, ?> resultMap = sendMsgToOpenData(reqUrl, reqParam);
      if (resultMap != null && "success".equals(resultMap.get("status"))) {
        List<Map<String, Object>> map = (List<Map<String, Object>>) resultMap.get("result");
        if (map != null && map.size() > 0) {
          String shortUrl = map.get(0).get("shortUrl").toString();
          url = domain + "/" + ShortUrlConst.F_TYPE + "/" + shortUrl;
        } else {
          logger.error("生成文件短地址短地址出错，远程调用接口返回数据: {}", resultMap);
        }
      } else {
        logger.error("生成文件短地址出错，远程调用接口返回数据：{}", resultMap);
      }
    } catch (Exception e) {
      logger.error("生成文件短地址出错，fileId={}。\n异常信息: {}", id, e);
    }
    return url;
  }

}
