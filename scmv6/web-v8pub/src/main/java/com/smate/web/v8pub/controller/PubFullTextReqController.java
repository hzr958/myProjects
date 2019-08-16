package com.smate.web.v8pub.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smate.core.base.pub.enums.PubFullTextReqStatusEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.service.fulltext.PubFullTextReqService;
import com.smate.web.v8pub.vo.PubFulltextReqVO;

/**
 * 成果全文请求操作 控制类
 * 
 * @author yhx
 *
 */
@Controller
public class PubFullTextReqController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubFullTextReqService pubFullTextReqService;

  /**
   * 全文请求处理
   */
  @RequestMapping(value = "/pub/fulltext/ajaxreqadd", method = RequestMethod.POST)
  @ResponseBody
  public String addPubFullTextReq(PubFulltextReqVO req) {
    Map<String, Object> map = new HashMap<>();
    boolean iszhCn = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      boolean flag = true; // 参数检测正确标志
      if ("pub".equalsIgnoreCase(req.getPubType())) {
        // 群组个人成果的资源类型
        req.setPubType("sns");
      }
      if ("pdwh".equalsIgnoreCase(req.getPubType())) {
        // 历史数据资源类型
        req.setPubType("pdwhpub");
      }
      if (StringUtils.isNotBlank(req.getDes3PubId())
          && ("sns".equals(req.getPubType()) || "pdwhpub".equals(req.getPubType()))) {

        if ("sns".equals(req.getPubType())) { // 请求个人库必须要求有接收人的id,请求基准库不需要接收人id
          if (req.getRecvPsnId() == null || req.getRecvPsnId() == 0L) {
            flag = false;
          }
        }
      } else {
        flag = false;
      }
      if (req.getRecvPsnId() != null && req.getRecvPsnId() != 0L) {
        // 只要有接受者id 不管 请求个人的还是 基准库的 SCM-24031
        flag = true;
      }
      if (flag) {
        map = pubFullTextReqService.dealRequest(req);
        map.put("reqId", req.getReqId());
        map.put("msgId", req.getMsgId());
        map.put("msg", iszhCn ? "保存全文请求成功" : "Full-text has been saved successfully");
      } else {
        map.put("status", "error");
        map.put("msg", iszhCn ? "系统错误，请稍后再试" : "System error occured, please try again later");
      }

    } catch (Exception e) {
      map.put("status", "error");
      map.put("msg", iszhCn ? "系统错误，请稍后再试" : "System error occured, please try again later");
      logger.error("新增全文请求记录出错", e);
    }
    return JacksonUtils.jsonObjectSerializer(map);
  }

  /**
   * 处理全文请求，只接收POST请求
   * 
   * dealStatus : 1==同意 2==忽略/拒绝 3==上传全文 msgId MsgRelation表的ID pubId 成果id
   */
  @RequestMapping(value = "/pub/fulltext/ajaxrequpdate", method = RequestMethod.POST,
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String pubFullTextReqUpdate(PubFulltextReqVO req) {
    boolean iszhCn = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    Map<String, Object> map = new HashMap<>();
    try {
      boolean flag = false; // 参数检测正确标志
      if (req.getPubId() != 0L && req.getMsgId() != 0L && StringUtils.isNotBlank("" + req.getDealStatus())) {
        if (req.getDealStatus() == 1 || req.getDealStatus() == 2 || req.getDealStatus() == 3) {
          flag = true;
        }
      }
      if (flag) {
        // 处理用户请求
        pubFullTextReqService.updateStatus(req.getMsgId(), req.getPubId(),
            PubFullTextReqStatusEnum.valueOf(req.getDealStatus()), SecurityUtils.getCurrentUserId());
        map.put("status", "success");
        map.put("msg", iszhCn ? "处理全文请求成功" : "Full-text has been updated successfully");
      } else {
        map.put("status", "error");
        map.put("msg", iszhCn ? "系统错误，请稍后再试" : "System error occured, please try again later");
        logger.error("处理全文请求记录出错！请求参数错误");
      }
    } catch (Exception e) {
      map.put("status", "error");
      map.put("msg", iszhCn ? "系统错误，请稍后再试" : "System error occured, please try again later");
      logger.error("处理全文请求记录出错", e);
    }
    return JacksonUtils.jsonObjectSerializer(map);
  }
}
