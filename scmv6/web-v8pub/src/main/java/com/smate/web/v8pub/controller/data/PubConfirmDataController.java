package com.smate.web.v8pub.controller.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smate.core.base.utils.exception.PubException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.v8pub.po.sns.PubFulltextPsnRcmd;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.service.sns.fulltextpsnrcmd.PubFulltextPsnRcmdService;
import com.smate.web.v8pub.service.sns.homepage.PubConfirmService;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 成果 全文 认领 相关服务
 * 
 * @author aijiangbin
 * @date 2018年9月4日
 */
@Controller
public class PubConfirmDataController {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Resource
  private PubFulltextPsnRcmdService pubFulltextPsnRcmdService;
  @Resource
  private PubConfirmService pubConfirmService;
  @Autowired
  private PubPdwhService pubPdwhService;
  @Autowired
  private PubSnsService pubSnsService;



  /**
   * 得到成果，全文认领统计数
   * 
   * @param jsonData
   * @return
   */
  @RequestMapping(value = "/data/pub/getpubfulltextconfirmcount", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public String getPubFullTextConfirmCount(@RequestBody String jsonData) {
    Map<String, Object> map = new HashMap<>();
    try {
      Map<Object, Object> paramMap = JacksonUtils.json2Map(jsonData);
      Object des3PsnId = paramMap.get("des3PsnId");
      Long psnId = 0L;
      if (des3PsnId != null) {
        psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PsnId.toString()));
      }
      if (psnId != 0L) {
        Map<Object, Object> json2Map = JacksonUtils.json2Map(jsonData);
        Long pubFulltextCount = pubFulltextPsnRcmdService.getFulltextCount(psnId);
        map.put("pubFulltextCount", pubFulltextCount);
      } else {
        map.put("pubFulltextCount", 0);
      }
    } catch (Exception e) {
      logger.error("获取成果认领相关统计数，异常", e);
      map.put("pubFulltextCount", 0);
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 得到成果，全文认领统计数
   * 
   * @param jsonData
   * @return
   */
  @RequestMapping(value = "/data/pub/getpubconfirmcount", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public String getPubConfirmCount(@RequestBody String jsonData) {
    Map<String, Object> map = new HashMap<>();
    try {
      Map<Object, Object> paramMap = JacksonUtils.json2Map(jsonData);
      Object des3PsnId = paramMap.get("des3PsnId");
      Long psnId = 0L;
      if (des3PsnId != null) {
        psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PsnId.toString()));
      }
      if (psnId != 0L) {
        Map<Object, Object> json2Map = JacksonUtils.json2Map(jsonData);
        Long pubConfirmCount = pubConfirmService.getPubConfirmCount(psnId);
        map.put("pubConfirmCount", pubConfirmCount);
      } else {
        map.put("pubConfirmCount", 0);
      }
    } catch (Exception e) {
      logger.error("获取成果认领相关统计数，异常", e);
      map.put("pubFulltextCount", 0);
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 得到成果，全文认领统列表
   * 
   * @param jsonData
   * @return
   */
  @RequestMapping(value = "/data/pub/pubfulltextlist", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public String pubFulltextList(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<>();
    try {
      if (pubOperateVO.getPsnId() != null && pubOperateVO.getPsnId() != 0L) {
        pubFulltextPsnRcmdService.getPubFulltextList(pubOperateVO);
        map.put("totalCount", pubOperateVO.getTotalCount());
        map.put("list", pubOperateVO.getPubRcmdftList());
        map.put("status", "SUCCESS");
      } else {
        map.put("status", "ERROR");
      }
    } catch (Exception e) {
      logger.error("获取成果认领列表，异常", e);
      map.put("status", "ERROR");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 确认全文认领 是这篇成果的全文
   * 
   * @param ids PUB_FULLTEXT_PSN_RCMD表id，多个逗号隔开
   * @param des3PsnId 当前操作者ID
   * @return
   */
  @RequestMapping(value = "/data/pub/opt/confirmpubft", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public String confirmPub(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    String ids = pubOperateVO.getIds();
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(pubOperateVO.getDes3PsnId()), 0L);
    try {
      if (StringUtils.isNotBlank(ids) && psnId > 0L) {
        List<Long> idList = new ArrayList<Long>();
        idList = ServiceUtil.splitStrToLong(ids);
        if (!CollectionUtils.isEmpty(idList)) {
          int errorCount = 0;
          for (Long id : idList) {
            // TODO 判断操作者和全文推荐的成果拥有者是否是同一个人
            if (checkConfirmPub(id)) {
              this.pubFulltextPsnRcmdService.confirmPubft(id, psnId);// 认领全文
              map.put("status", "success");
            } else {
              errorCount++;
            }
          }
          if (errorCount == idList.size()) {
            map.put("status", "not_exist");
          }
        }
      } else {
        map.put("errMsg", "ids or psnId is null");
      }
    } catch (Exception e) {
      logger.error("确认全文认领 是这篇成果的全文出错，ids=" + ids + ", psnId = " + psnId, e);
      map.put("status", "error");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 检查认领的成果
   * 
   * @param id
   * @return
   */
  /*
   * private boolean checkConfirmPub(Long id) { if (NumberUtils.isNotNullOrZero(id)) {
   * PubFulltextPsnRcmd psnRcmd = pubPdwhService.getPubFulltextPsnRcmdById(id); if (psnRcmd != null &&
   * NumberUtils.isNotNullOrZero(psnRcmd.getSrcPubId())) { if (psnRcmd.getDbId().equals(1)) { //
   * 说明是基准库的全文，那么srcPubId是基准库的id PubPdwhPO pubPdwhPO = pubPdwhService.get(psnRcmd.getSrcPubId()); if
   * (pubPdwhPO != null && pubPdwhPO.getStatus().equals(PubPdwhStatusEnum.DEFAULT)) { //
   * 基准库成果状态正常才进行操作 return true; } } else { // 说明是个人库的全文，那么srcPubId是个人库的id PubSnsPO pubSnsPO =
   * pubSnsService.get(psnRcmd.getSrcPubId()); if (pubSnsPO != null &&
   * pubSnsPO.getStatus().equals(PubSnsStatusEnum.DEFAULT)) { // 基准库成果状态正常才进行操作 return true; } } } }
   * return false; }
   */

  /**
   * 确认全文认领 不是这篇成果的全文
   * 
   * @param ids PUB_FULLTEXT_PSN_RCMD表id，多个逗号隔开
   * @param des3PsnId 当前操作者ID
   * @return
   */
  @RequestMapping(value = "/data/pub/opt/rejectpubft", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public String rejectPubFulltext(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      String ids = pubOperateVO.getIds();
      if (StringUtils.isNotBlank(ids) && pubOperateVO.getPsnId() != null && pubOperateVO.getPsnId() > 0L) {
        List<Long> idList = new ArrayList<Long>();
        idList = ServiceUtil.splitStrToLong(ids);
        if (!CollectionUtils.isEmpty(idList)) {
          for (Long id : idList) {
            this.pubFulltextPsnRcmdService.rejectPubft(id);// 不是这篇成果的全文
            map.put("status", "success");
          }
        }
      }
    } catch (Exception e) {
      logger.error("确认全文认领 不是这篇成果的全文出错，ids={},psnId={}", pubOperateVO.getIds(), pubOperateVO.getPsnId(), e);
      map.put("status", "error");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 全文认领，单个全文认领信息
   * 
   * @return
   */
  @RequestMapping(value = "/data/pub/pubfulltext/details", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public Object queryRcmdPubFulltextInfo(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Page page = new Page();
    page.setIgnoreMin(true);
    page.setPageSize(1);
    Long operatePsnId = pubOperateVO.getPsnId();
    try {
      if (operatePsnId != null && operatePsnId > 0L) {
        page = pubFulltextPsnRcmdService.getRcmdFulltextDetails(pubOperateVO, page);
        map.put("status", "success");
        map.put("list", page.getResult());
      } else {
        map.put("status", "error");
        map.put("errMsg", "operatePsnId is null");
      }
    } catch (Exception e) {
      logger.error("个人主页-成果模块-全文认领-出错,psnId= " + operatePsnId, e);
      map.put("status", "error");
    }
    return map;
  }

  /**
   * 检查认领的成果 <br/>
   * 1.基准库成果删除，会同步删除推荐出去的记录 <br/>
   * 2.个人库成果删除，会同步删除推荐出去的记录<br/>
   * 所以校验认领的成果是否存在，只需要保证认领记录存在，存在就可以进行认领，不存在就不能认领<br/>
   * 
   * @param id
   * @return
   * @throws PubException
   */
  private boolean checkConfirmPub(Long id) throws PubException {
    if (NumberUtils.isNotNullOrZero(id)) {
      PubFulltextPsnRcmd psnRcmd = pubFulltextPsnRcmdService.getPubFulltextPsnRcmd(id);
      if (psnRcmd != null) {
        return true;
      }
    }
    return false;
  }


}
