package com.smate.web.v8pub.controller.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smate.core.base.pub.enums.PubFullTextReqStatusEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.service.fulltext.PubFullTextReqService;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.vo.PubFulltextReqVO;

/**
 * 成果全文请求操作 控制类
 * 
 * @author yhx
 *
 */
@RestController
public class PubFullTextReqDataController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubFullTextReqService pubFullTextReqService;
  @Autowired
  private PubFullTextService pubFullTextService;

  /**
   * 全文请求处理
   */
  @RequestMapping(value = "/data/pub/fulltext/ajaxreqadd", method = RequestMethod.POST,
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object addPubFullTextReq(@RequestBody PubFulltextReqVO req) {
    Map<String, Object> map = new HashMap<>();
    try {
      if (checkFulltextRequestParam(req)) {
        map = pubFullTextReqService.dealRequest(req);
        map.put("reqId", req.getReqId());
        map.put("msgId", req.getMsgId());
        map.put("msg", "保存全文请求成功");
      } else {
        map.put("status", "error");
        map.put("msg", "系统错误，请稍后再试");
      }

    } catch (Exception e) {
      map.put("status", "error");
      map.put("msg", "参数校验不通过");
      logger.error("新增全文请求记录出错", e);
    }
    return map;
  }

  private boolean checkFulltextRequestParam(PubFulltextReqVO reqVO) {
    boolean pubIdNull = StringUtils.isNotBlank(reqVO.getDes3PubId());
    boolean isPdwh = "pdwh".equalsIgnoreCase(reqVO.getPubType());
    boolean isSns = "sns".equalsIgnoreCase(reqVO.getPubType()) && !NumberUtils.isNullOrZero(reqVO.getRecvPsnId());
    return pubIdNull && (isPdwh || isSns);
  }

  /**
   * 处理全文请求，只接收POST请求
   * 
   * @param dealStatus : 1==同意 2==忽略/拒绝 3==上传全文
   * @param msgId MsgRelation表的ID
   * @param pubId 成果id
   */
  @RequestMapping(value = "/data/pub/fulltext/ajaxrequpdate", method = RequestMethod.POST,
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object pubFullTextReqUpdate(@RequestBody PubFulltextReqVO req) {
    Map<String, Object> map = new HashMap<>();
    try {
      Long pubId = req.getPubId();
      Integer operate = req.getDealStatus();
      boolean flag = false; // 参数检测正确标志
      if (!NumberUtils.isNullOrZero(pubId) && !NumberUtils.isNullOrZero(req.getMsgId()) && operate != null
          && NumberUtils.isNotZero(operate)) {
        if (operate == 1 || operate == 2 || operate == 3) {
          flag = true;
        }
      }
      if (flag) {
        // 处理用户请求
        pubFullTextReqService.updateStatus(req.getMsgId(), pubId, PubFullTextReqStatusEnum.valueOf(operate),
            req.getCurrentPsnId());
        map.put("status", "success");
        map.put("msg", "处理全文请求成功");
      } else {
        map.put("status", "error");
        map.put("msg", "系统错误，请稍后再试");
        logger.error("处理全文请求记录出错！请求参数错误!");
      }
    } catch (Exception e) {
      map.put("status", "error");
      map.put("msg", "系统错误，请稍后再试");
      logger.error("处理全文请求记录出错！", e);
    }
    return map;
  }

  /**
   * 获取个人库成果全文权限
   * 
   * @return
   */
  @RequestMapping(value = "/data/pub/fulltext/getPermission", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public String getPubPermission(@RequestBody PubFulltextReqVO req) {
    HashMap<String, Object> map = new HashMap<>();
    Long pubId = com.smate.core.base.utils.number.NumberUtils.parseLong(Des3Utils.decodeFromDes3(req.getDes3PubId()));
    Long fileId = com.smate.core.base.utils.number.NumberUtils.parseLong(Des3Utils.decodeFromDes3(req.getDes3FileId()));
    try {
      if (pubId != null && fileId != null) {
        Integer fullTextPermission = pubFullTextService.getFullTextPermission(pubId, fileId, req.getReqPsnId());
        map.put("fullTextPermission", fullTextPermission);
        Long pubOwnerPsnId = pubFullTextReqService.getPubOwnerPsnId(req);
        if (NumberUtils.isNotNullOrZero(pubOwnerPsnId)) {
          map.put("des3OwnerPsnId", Des3Utils.encodeToDes3(pubOwnerPsnId.toString()));
        }
        map.put("result", "success");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("获取个人库成果全文权限信息出错,pubId=" + pubId, e);
    }
    return JacksonUtils.jsonObjectSerializer(map);
  }

  /**
   * 获取成果全文图片
   * 
   * @return
   */
  @RequestMapping(value = "/data/pub/getfulltextimageurl", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public Object getFulltextImageUrl(@RequestBody Map<String, Object> map) {
    String fullTextImageUrl = "";
    String pubIdStr = Objects.toString(map.get("des3PubId"));
    try {
      if (pubIdStr != null) {
        Long pubId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(pubIdStr));
        fullTextImageUrl = pubFullTextService.getFulltextImageUrl(pubId);
      }
    } catch (Exception e) {
      logger.error("获取个人库成果全文权限信息出错,pubIdStr=" + pubIdStr, e);
    }
    return fullTextImageUrl;
  }
}
