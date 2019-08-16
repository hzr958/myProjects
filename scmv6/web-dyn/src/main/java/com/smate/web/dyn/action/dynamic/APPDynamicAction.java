package com.smate.web.dyn.action.dynamic;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.app.model.AppVersionRecord;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.dyn.form.dynamic.DynReplayInfo;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.dynamic.IOSDynamicInfo;
import com.smate.web.dyn.service.dynamic.DynamicRealtimeService;
import com.smate.web.dyn.service.dynamic.DynamicShareService;
import com.smate.web.dyn.service.dynamic.IOSDynamicService;
import com.smate.web.dyn.service.share.ShareStatisticsService;
import com.smate.web.dyn.service.statistics.DynStatisticsService;

/**
 * 科研之友IOS客户端动态数据获取处理接口
 * 
 * @author LJ
 * @since 2017-04-05
 */
public class APPDynamicAction extends ActionSupport implements ModelDriven<DynamicForm>, Preparable {
  private static final long serialVersionUID = -1213392457711783664L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private IOSDynamicService iosDynamicService;
  @Autowired
  private DynamicRealtimeService dynamicRealtimeService;
  @Autowired
  private DynamicShareService dynamicShareService;
  @Autowired
  private ShareStatisticsService shareStatisticsService;
  @Autowired
  private DynStatisticsService dynStatisticsService;
  private DynamicForm form;
  private IOSDynamicInfo dynInfo;// 用于封装返回数据转换为JSON给IOS客户端
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态
  private int flag;// 页面操作标志，0：下拉 1：上滑

  /**
   * 首页获取动态列表
   * 
   * @return String
   */
  @Action("/app/dynweb/show")
  public String getIOSDynList() {
    List<IOSDynamicInfo> list = new ArrayList<IOSDynamicInfo>();
    if (form.getPsnId() > 0L) {
      try {
        // 首页初始化动态数据，10条
        list = iosDynamicService.dynamicShow(form.getPsnId(), 10, 1);
        status = IOSHttpStatus.OK;
        if (list != null) {
          total = list.size();
        }
      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("获取动态列表错误,psnid:" + form.getPsnId(), e);
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
    }
    AppActionUtils.renderAPPReturnJson(CollectionUtils.isEmpty(list) ? CollectionUtils.EMPTY_COLLECTION : list, total,
        status);
    return null;
  }


  /**
   * 上滑下拉操作，根据DYNID获取动态信息
   * 
   * @return
   */

  @Action("/app/dynweb/getDynById")
  public String getDynInfoById() {
    // flag=1:获取小于等于DynID的10条，2：获取大的
    List<IOSDynamicInfo> list = new ArrayList<IOSDynamicInfo>();
    if (form.getDynId() != null && form.getDynId() > 0L && (flag == 1 || flag == 2)) {
      try {
        list = iosDynamicService.getDynById(form.getPsnId(), form.getDynId(), flag);
        if (list != null) {
          status = IOSHttpStatus.OK;
          total = list.size();
        }
      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("获取动态错误,dynId:" + form.getDynId(), e);
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
    }
    AppActionUtils.renderAPPReturnJson(list, total, status);
    return null;
  }

  /**
   * 赞动态（成果）动态页使用，
   * 
   * @return
   * @throws Exception
   */
  @Action("/app/dynweb/awarddyn")
  public String ajaxAward() {
    // resType&des3ResId&des3PubId&dynType（成果）
    // dynId,dynType,des3ResId,resType（动态）
    Map<String, Object> awardPsnContent = null;
    try {
      // 赞操作类型为2
      form.setOperatorType(2);
      /*
       * // 不是成果资源 ，0：普通动态， 1：成果 if (form.getResType() == 0) { form.setResId(form.getDynId()); } //
       * 资源节点为1，成果 if (form.getResNode() == null) { form.setResNode(1); }
       */
      awardPsnContent = iosDynamicService.addAward(form);
      if (awardPsnContent != null) {
        status = IOSHttpStatus.OK;
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("点赞操作错误,userId=" + form.getPsnId(), e);
    }
    AppActionUtils.renderAPPReturnJson(awardPsnContent, total, status);
    return null;
  }

  /**
   * 获取动态统计数
   * 
   * @return
   */
  @Action("/app/dynweb/getdynstatistics")
  public String getDynstatistics() {
    // psnid,dynId,resId,resType
    Map<String, Map<String, Object>> statistics = null;
    Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
    if (form.getDes3DynId() != null && form.getResId() != null && form.getResType() != null) {
      try {
        statistics = iosDynamicService.getStatistics(form);
        if (statistics != null) {
          map.put("dyn", statistics.get(form.getDynId().toString()));
          status = IOSHttpStatus.OK;
          total = statistics.size();
        }
      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("app获取动态统计数出错！");
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
    }
    AppActionUtils.renderAPPReturnJson(map, total, status);
    return null;

  }

  /**
   * 获取动态详情
   * 
   * @return
   */
  @Action("/app/dynweb/detail")
  public String dynamicDetail() {
    // psnId,dynId
    if (form.getDynId() != null && form.getDynId() > 0L) {
      try {
        dynInfo = iosDynamicService.buildDynamicDetail(form.getPsnId(), form.getDynId());
        if (dynInfo != null) {
          status = IOSHttpStatus.OK;
          total = 1;
        }
      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("获取动态详情出错,dynId=" + form.getDynId(), e);
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
    }
    AppActionUtils.renderAPPReturnJson(dynInfo, total, status);
    return null;

  }

  /**
   * 获取动态评论信息
   * 
   * @return
   */
  @Action("/app/dynweb/dynreply")
  public String loadDynReply() {
    // dynId,resType,resId
    List<DynReplayInfo> replyList = null;
    try {
      replyList = iosDynamicService.loadDynRely(form);
      if (replyList != null) {
        total = replyList.size();
      }
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("获取动态评论出错,dynId=" + form.getDynId(), e);
    }
    AppActionUtils.renderAPPReturnJson(replyList, total, status);
    return null;
  }

  /** 发布实时动态入口 */
  @Action("/app/dynweb/ajaxrealtime")
  public String dynamicRealtime() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long resId = form.getResId();
      Integer resType = form.getResType();
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getPsnId() == 0 && !"".equals(form.getDes3psnId())) {
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3psnId())));
      }
      dynamicRealtimeService.dynamicRealtime(form);
      // 纯资源动态要更新资源分享统计数，有些会单独另发启url去更新统计数
      if ("B2TEMP".equals(form.getDynType()) && 3 == form.getOperatorType()) {
        dynamicShareService.shareDynamic(form);
      }
      // A、B1动态要更新动态分享统计数（-1目前是什么逻辑不清楚了）
      if (-1 == form.getOperatorType() || ("B1TEMP".equals(form.getDynType()) && 3 == form.getOperatorType())) {// 分享更新统计表（有分享内容是-1）
        form.setPlatform("1");
        shareStatisticsService.addNewShareRecord(form);
      }
      /*
       * dynamicRealtimeService.dynamicRealtime(form); if ("B2TEMP".equals(form.getDynType()) && 3 ==
       * form.getOperatorType()) { dynamicShareService.shareDynamic(form); } if (-1 ==
       * form.getOperatorType()) {// 分享更新统计表（有分享内容是-1）
       * shareStatisticsService.addBatchShareRecord(form.getPsnId(), resType, resId); } if
       * ("ATEMP".equals(form.getDynType()) || "B1TEMP".equals(form.getDynType())) {// 带文字的
       * shareStatisticsService.updateDynStatistics(form.getDynId());
       * 
       * } if (-1 == form.getOperatorType() || ("B1TEMP".equals(form.getDynType()) && 3 ==
       * form.getOperatorType())) {// 分享更新资源 shareStatisticsService.addNewShareRecord(form); }
       */
      map.put("result", "success");
      status = IOSHttpStatus.OK;
      total = 1;

    } catch (Exception e) {
      logger.error("生成动态数据出错,dynId=" + form.getDynId(), e);
      map.put("result", "failed");
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
    }
    AppActionUtils.renderAPPReturnJson(map, total, status);
    return null;
  }

  /**
   * 分享动作，
   * 
   * @return
   * @throws Exception
   */
  @Action("/app/dynweb/quicksharedyn")
  public String ajaxShare() {
    // dynType&resType&des3ResId（成果）
    // resType, resId ,dynType ,dynId,parentDynId,psnId（动态）
    Map<String, Object> shareInfo = null;
    if (form.getPsnId() > 0) {
      try {
        shareInfo = iosDynamicService.quickshare(form);
        if (shareInfo != null) {
          status = IOSHttpStatus.OK;
        }
      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("分享操作错误,userId=" + form.getPsnId(), e);
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
    }
    AppActionUtils.renderAPPReturnJson(shareInfo, total, status);
    return null;
  }

  /**
   * 
   * <p>
   * Description:用于app获取服务信息以及最新版本号
   * </p>
   * 
   * @author
   * @date
   */
  @Action("/app/dynweb/sync")
  public String syncData() {
    Map<String, Object> map = new HashMap<>();
    AppVersionRecord iosVersionInfo = null;
    status = IOSHttpStatus.OK;
    try {
      iosVersionInfo = iosDynamicService.getIosVersionInfo();
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("app获取版本信息出错", e);
      AppActionUtils.renderAPPReturnJson(map, total, status);
      return null;
    }

    if (iosVersionInfo == null) {
      status = IOSHttpStatus.NOT_FOUND;
      AppActionUtils.renderAPPReturnJson(map, total, status);
      return null;
    }
    map.put("adImageUrl", "");
    map.put("version", iosVersionInfo.getVersionCode());
    map.put("versioncode", iosVersionInfo.getVersionNumber());
    map.put("sysdate", System.currentTimeMillis());
    map.put("udmsg", iosVersionInfo.getVersionDescription());
    AppActionUtils.renderAPPReturnJson(map, total, status);
    return null;

  }

  /**
   * post取参数,getInputStream()只能读取一次，会导致后面读取不到
   * 
   * @param httpRequest
   * @return
   * @throws Exception
   */
  public String getPostPara(HttpServletRequest httpRequest) throws Exception {
    ServletInputStream inputStream = httpRequest.getInputStream();
    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
    StringBuffer buffer = new StringBuffer();
    char[] buf = new char[64];
    int count = 0;
    try {
      while ((count = reader.read(buf)) != -1)
        buffer.append(buf, 0, count);
    } finally {
      reader.close();
    }
    String result = buffer.toString();
    return null;

  }

  /**
   * 获取下一个模板类型
   * 
   * @param dynType
   * @return
   */
  public String getNextDynType(String dynType) {
    String nextDynType = null;
    switch (dynType) {
      case "ATEMP":
        nextDynType = "B1TEMP";
        break;
      case "B1TEMP":
        nextDynType = "B1TEMP";
        break;
      case "B2TEMP":
        nextDynType = "B2TEMP";
        break;
      case "B3TEMP":
        nextDynType = "B2TEMP";
        break;
      default:
        break;
    }
    return nextDynType;

  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new DynamicForm();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  public DynamicForm getForm() {
    return form;
  }

  public void setForm(DynamicForm form) {
    this.form = form;
  }

  @Override
  public DynamicForm getModel() {
    return form;
  }

  public int getFlag() {
    return flag;
  }

  public void setFlag(int flag) {
    this.flag = flag;
  }

}
