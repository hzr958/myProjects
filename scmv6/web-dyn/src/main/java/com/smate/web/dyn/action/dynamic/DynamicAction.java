package com.smate.web.dyn.action.dynamic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.client.RestTemplate;

import com.opensymphony.xwork2.ModelDriven;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.service.dynamic.DynamicAwardService;
import com.smate.web.dyn.service.dynamic.DynamicReplyService;
import com.smate.web.dyn.service.dynamic.DynamicService;
import com.smate.web.dyn.service.statistics.DynStatisticsService;

/**
 * 动态获取及相关操作Action
 * 
 * @author zk
 *
 */
@Results({@Result(name = "show", location = "/WEB-INF/jsp/dyn/show/dyn_main.jsp"),
    @Result(name = "show_sub", location = "/WEB-INF/jsp/dyn/show/dyn_main_sub.jsp"),
    @Result(name = "mobile_show_sub", location = "/WEB-INF/jsp/dyn/show/mobile_dyn_main_sub.jsp"),
    @Result(name = "dynamicDetail", location = "/WEB-INF/jsp/dyn/detail/dyn_detail.jsp"),
    @Result(name = "dyn_reply_list", location = "/WEB-INF/jsp/dyn/detail/dyn_detail_comment.jsp"),
    @Result(name = "pc_dyn_reply_list", location = "/WEB-INF/jsp/dyn/detail/pc_dyn_detail_comment.jsp"),
    @Result(name = "pc_sample_dyn_reply", location = "/WEB-INF/jsp/dyn/detail/pc_sample_dyn_reply.jsp"),
    @Result(name = "pc_dynamic_detail", location = "/WEB-INF/jsp/dyn/detail/pc_dyn_detail.jsp"),
    @Result(name = "dyn_res_recommend", location = "/WEB-INF/jsp/dyn/show/dyn_res_recommend.jsp"),
    @Result(name = "mobile_dyn_res_recommend", location = "/WEB-INF/jsp/dyn/show/mobile_dyn_res_recommend.jsp")})
public class DynamicAction extends WechatBaseAction implements ModelDriven<DynamicForm> {

  private static final long serialVersionUID = -9090557623490845340L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private DynamicForm form;
  @Autowired
  private DynamicService dynamicService;
  @Autowired
  private DynamicAwardService dynamicAwardService;
  @Autowired
  private DynamicReplyService dynamicReplyService;
  @Autowired
  private DynStatisticsService dynStatisticsService;
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 获取动态列表
   * 
   * @return
   */
  @Actions({@Action("/dynweb/dynamic/show"), @Action("/dynweb/mobile/dynshow")})
  public String dynamicList() {
    try {
      if (domainMobile.equals(this.getDomain())) {
        form.setPlatform("mobile");
      }
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        // jsapi签名只要与浏览器地址内容一致即可
        this.handleWxJsApiTicket(this.getDomain() + "/dynweb/mobile/dynshow");
        this.setDes3Wid(Des3Utils.encodeToDes3(Struts2Utils.getSession().getAttribute("wxOpenId").toString()));
      }
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setDes3psnId(Des3Utils.encodeToDes3(ObjectUtils.toString(SecurityUtils.getCurrentUserId().toString())));
      /*
       * if (form.getPsnId() > 0) { dynamicService.dynamicShow(form); } else { throw new
       * DynException("psnId不能为空！"); }
       */
    } catch (Exception e) {
      logger.error("获取动态列表出错,userId=" + form.getPsnId(), e);
    }
    return "show";
  }

  /**
   * 获取动态列表
   * 
   * @return
   */
  @Action("/dynweb/dynamic/ajaxshow")
  public String ajaxDynamicList() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setLocale(this.getLocale().toString());
      if (form.getPsnId() > 0) {
        dynamicService.dynamicShow(form);
      } else {
        throw new DynException("psnId不能为空！");
      }
    } catch (Exception e) {
      logger.error("获取动态列表出错,userId=" + form.getPsnId(), e);
    }
    if ("mobile".equals(form.getPlatform())) {
      return "mobile_show_sub";
    }
    return "show_sub";
  }

  /**
   * zzx 获取动态详情评论列表 resType=1且dynType=B1TEMP的纯成果动态，已拦截跳转成果详情 这里的resType=1的动态是当普通动态处理
   * 
   * @return 评论列表数据：dynReplyList、 评论时间：dateMap
   */
  @Action("/dynweb/dynamic/ajaxdynreply")
  public String loadDynReply() {
    try {
      dynamicReplyService.loadDynReply(form);
    } catch (Exception e) {
      logger.error("获取动态详情评论列表出错,dynId=" + form.getDynId(), e);
    }
    Struts2Utils.getRequest().setAttribute("nowDate", new Date().getTime());
    if ("pc".equals(form.getPlatform())) {
      return "pc_dyn_reply_list";
    }
    return "dyn_reply_list";

  }

  /**
   * pc端只加载第一评论
   * 
   * @return
   */
  @Action("/dynweb/dynamic/ajaxsampledynreply")
  public String loadSampleDynReply() {
    try {
      form.setPageNumber(1);
      form.setPageSize(1);
      dynamicReplyService.loadDynReply(form);
    } catch (Exception e) {
      logger.error("获取动态详情评论列表出错,userId=" + form.getPsnId() + ",paramJson:" + form.getParamJson(), e);
    }
    Struts2Utils.getRequest().setAttribute("nowDate", new Date().getTime());
    return "pc_sample_dyn_reply";
  }

  /**
   * 赞 ，动作，
   * 
   * @return
   * @throws Exception
   */
  @Action("/dynweb/dynamic/ajaxawarddyn")
  public String ajaxAward() {
    String awardPsnContent = "";
    try {
      awardPsnContent = dynamicAwardService.addAward(form);
    } catch (Exception e) {
      logger.error("点赞操作错误,userId=" + form.getPsnId() + ",paramJson:" + form.getParamJson(), e);
    }
    Struts2Utils.renderJson(awardPsnContent, "encoding:utf-8");
    return null;
  }

  /**
   * 初始化，时候点赞
   * 
   * @return
   */
  @Action("/dynweb/dynamic/ajaxinithasaward")
  public String ajaxinithasaward() {
    Map<String, Object> map = new HashMap<String, Object>();
    form.setPsnId(SecurityUtils.getCurrentUserId());
    try {
      Map<String, Boolean> hasAwardMap = dynamicAwardService.getAwardPsnHasAward(form);
      if (hasAwardMap.size() > 0) {
        map.put("hasAwardMap", hasAwardMap);
      }
      map.put("result", "success");
    } catch (DynException e) {
      logger.error("初始化 赞操作错误,userId=" + form.getPsnId(), e);
    }

    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 获取动态详情
   * 
   * @return
   */
  @Actions({@Action("/dynweb/dynamic/ajaxdetail"), @Action("/dynweb/dynamic/detail"),
      @Action("/dynweb/mobile/dyndetail")})
  public String dynamicDetail() {
    try {
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        // jsapi签名只要与浏览器地址内容一致即可
        this.handleWxJsApiTicket(this.getDomain() + "/dynweb/dynamic/detail");
      }
      form.setLocale(LocaleContextHolder.getLocale().toString());
      dynamicService.buildDynamicDetail(form);
    } catch (Exception e) {
      logger.error("获取动态详情,dynId=" + form.getDynId(), e);
    }
    if ("pc".equals(form.getPlatform())) {
      return "pc_dynamic_detail";
    }
    return "dynamicDetail";
  }

  /**
   * lhd 获取初始化动态统计数 参数：dynStatisticsIds(加载动态列表id)
   * 
   * @return 初始化动态统计数:dynStatistics、发布时间：publishDate
   */
  @Action("/dynweb/dynamic/ajaxgetdynstatistics")
  public String ajaxGetDynStatistics() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Map<String, Map<String, Object>> dynStatisMap =
          dynStatisticsService.getDynStatistics(form.getDynStatisticsIds(), form.getPsnId());
      if (dynStatisMap.size() > 0) {
        map.put("dynStatisMap", dynStatisMap);
      }
      map.put("result", "success");
      Struts2Utils.renderJson(map, "encoding:UTF-8");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("初始化动态统计失败", e);
    }
    return null;
  }

  /**
   * 获取动态详情页面收藏情况
   * 
   * @return
   */
  @Action("/dynweb/dynamic/ajaxgetdynCollect")
  public String ajaxgetdynCollect() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      map = dynStatisticsService.getDynCollect(form.getDes3DynId(), form.getPsnId());
      if (map.size() > 0) {
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
      Struts2Utils.renderJson(map, "encoding:UTF-8");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("初始化动态统计失败", e);
    }
    return null;
  }

  @Action("/dynweb/dynamic/ajaxsharecount")
  public String ajaxsharecount() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      dynStatisticsService.updateShareCount(form);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("初始化动态统计失败", e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  @SuppressWarnings("unchecked")
  @Action("/dynweb/dynamic/ajaxgetdynpubrecommend")
  public String dynPubRecommend() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      map = restTemplate.postForObject(domainscm + "/data/pub/recommend/getpubrecommendshowindyn", form, Map.class);
      dynamicService.buildDynPubRecommend(form, map);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("获取首页推荐论文出错", e);
    }
    if ("mobile".equals(form.getPlatform())) {
      return "mobile_dyn_res_recommend";
    }
    return "dyn_res_recommend";
  }

  @Action("/dynweb/dynamic/ajaxgetpubowner")
  public String ajaxgetpubowner() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      String ownerId = dynStatisticsService.getPubOwner(form.getResId());
      map.put("des3ownerId", ownerId);
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("获取个人库成果拥有者失败", e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 判断系统是否超时
   * 
   * @author ChuanjieHou
   * @date 2017年9月14日
   * @return
   */
  @Action("/dynweb/ajaxtimeout")
  public String ajaxTimeout() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", "success");
    Struts2Utils.renderJson(JacksonUtils.mapToJsonStr(map), "encoding:UTF-8");
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new DynamicForm();
    }
    form.setPsnId(SecurityUtils.getCurrentUserId());
  }

  @Override
  public DynamicForm getModel() {
    return form;
  }

}
