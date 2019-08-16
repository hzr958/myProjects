package com.smate.web.mobile.controller.web.pub;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.service.MobilePubQueryService;
import com.smate.web.mobile.v8pub.vo.PubQueryModel;

@Controller
public class FindAndCollectedPubController extends WeChatBaseController {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MobilePubQueryService mobilePubQueryService;

  /**
   * 移动端成果收藏页面
   * 
   * @return
   */
  @RequestMapping("/pub/collect/main")
  public ModelAndView psnCollectedPubMain(PubQueryModel pubQueryModel) {
    ModelAndView view = new ModelAndView();
    Long currentUserId = SecurityUtils.getCurrentUserId();
    pubQueryModel.setSearchPsnId(currentUserId);
    view.addObject("queryModel", pubQueryModel);
    view.setViewName("/pub/collectpub/mobile_collected_pub_main");
    return view;
  }

  /**
   * 发现成果，选择科技领域条件页面
   * 
   * @return
   */
  @RequestMapping("/pub/find/area")
  public ModelAndView findPubAreas() {
    ModelAndView view = new ModelAndView();
    List<Map<String, Object>> scienceAreas = new ArrayList<Map<String, Object>>();
    try {
      scienceAreas = mobilePubQueryService.getAllScienceArea();
    } catch (Exception e) {
      logger.error("移动端获取所有科技领域出错，psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    view.addObject("allAreas", scienceAreas);
    view.setViewName("/pub/collectpub/mobile_find_pub_areas");
    return view;
  }

  /**
   * 论文--》发现页面
   * 
   * @param model
   * @return
   */
  @RequestMapping("/pub/find/main")
  public ModelAndView findPubMain(PubQueryModel model) {
    if ("DEFAULT".equals(model.getOrderBy())) {// 默认阅读数
      model.setOrderBy("readCount");
    }
    ModelAndView view = new ModelAndView();
    view.addObject("model", model);
    view.setViewName("/pub/collectpub/mobile_find_pub_main");
    return view;
  }

  /**
   * 论文--》发现--》检索条件
   * 
   * @return
   */
  @RequestMapping("/pub/find/conditions")
  public ModelAndView showFindPubConditions(PubQueryModel model) {
    ModelAndView view = new ModelAndView();
    try {
      // 获取当前年份
      Calendar cal = Calendar.getInstance();
      model.setCurrentYear(cal.get(Calendar.YEAR));
    } catch (Exception e) {
      logger.error("移动端进入论文--》发现--》检索条件页面出错, psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    view.addObject("model", model);
    view.setViewName("/pub/collectpub/mobile_find_pub_conditions");
    return view;
  }

  /**
   * 检索主页面
   * 
   * @param model
   * @return
   */
  @RequestMapping("/pub/search/main")
  public ModelAndView findInterested(PubQueryModel model) {
    ModelAndView view = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    model.setDes3SearchPsnId(Des3Utils.encodeToDes3(Objects.toString(psnId)));
    view.addObject("model", model);
    view.addObject("desPsnId", Des3Utils.encodeToDes3(Objects.toString(psnId)));
    view.setViewName("/pub/search/mobile_search_main");
    return view;
  }

  /**
   * 全站检索--》检索论文页面
   * 
   * @param model
   * @return
   */
  @RequestMapping(value = {"/pub/paper/search", "/pub/outside/paper/search"})
  public ModelAndView searchPaper(PubQueryModel model) {
    ModelAndView view = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    model.setDes3SearchPsnId(Des3Utils.encodeToDes3(Objects.toString(psnId)));
    try {
      if (model.getSearchString() != null) {
        model.setSearchString(URLDecoder.decode(HtmlUtils.htmlUnescape(model.getSearchString()), "UTF-8"));
      }
    } catch (UnsupportedEncodingException e) {
      logger.error("移动端全站检索--》检索论文--》检索关键词转化异常, psnId = " + SecurityUtils.getCurrentUserId() + "searchKey="
          + model.getSearchString(), e);
    }
    view.addObject("model", model);
    view.addObject("desPsnId", Des3Utils.encodeToDes3(Objects.toString(psnId)));
    view.setViewName("/pub/search/mobile_search_paper_main");
    return view;
  }

  /**
   * 全站检索--》检索论文--》检索条件
   * 
   * @return
   */
  @RequestMapping("/pub/paper/conditions")
  public ModelAndView showSearchPaperConditions(PubQueryModel model) {
    ModelAndView view = new ModelAndView();
    try {
      // 获取当前年份
      Calendar cal = Calendar.getInstance();
      model.setCurrentYear(cal.get(Calendar.YEAR));
    } catch (Exception e) {
      logger.error("移动端全站检索--》检索论文--》检索条件页面出错, psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    view.addObject("model", model);
    view.setViewName("/pub/search/mobile_search_paper_conditions");
    return view;
  }

  /**
   * 全站检索--》检索论文页面
   * 
   * @param model
   * @return
   */
  @RequestMapping("/pub/patent/search")
  public ModelAndView searchPatent(PubQueryModel model) {
    ModelAndView view = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    model.setDes3SearchPsnId(Des3Utils.encodeToDes3(Objects.toString(psnId)));
    try {
      model.setSearchString(URLDecoder.decode(HtmlUtils.htmlUnescape(model.getSearchString()), "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      logger.error("移动端全站检索--》检索专利--》检索关键词转化异常, psnId = " + SecurityUtils.getCurrentUserId() + "searchKey="
          + model.getSearchString(), e);
    }
    view.addObject("model", model);
    view.addObject("desPsnId", Des3Utils.encodeToDes3(Objects.toString(psnId)));
    view.setViewName("/pub/search/mobile_search_patent_main");
    return view;
  }

  /**
   * 全站检索--》检索专利--》检索条件
   * 
   * @return
   */
  @RequestMapping("/pub/patent/conditions")
  public ModelAndView showSearchPatentConditions(PubQueryModel model) {
    ModelAndView view = new ModelAndView();
    try {
      // 获取当前年份
      Calendar cal = Calendar.getInstance();
      model.setCurrentYear(cal.get(Calendar.YEAR));
    } catch (Exception e) {
      logger.error("移动端全站检索--》检索专利--》检索条件页面出错, psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    view.addObject("model", model);
    view.setViewName("/pub/search/mobile_search_patent_conditions");
    return view;
  }

}
