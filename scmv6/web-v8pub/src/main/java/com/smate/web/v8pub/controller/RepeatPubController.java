package com.smate.web.v8pub.controller;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.service.repeatpub.RepeatPubOptService;
import com.smate.web.v8pub.service.repeatpub.RepeatPubShowService;
import com.smate.web.v8pub.vo.RepeatPubVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

/**
 * 重复成果， 控制器
 * 
 * @author aijiangbin
 * @date 2018年9月4日
 */
@Controller
public class RepeatPubController {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RepeatPubShowService repeatPubShowService;
  @Autowired
  private RepeatPubOptService repeatPubOptService;

  /**
   * 重复成果
   * 
   * @param pubCitedVo
   * @return
   */
  @RequestMapping(value = "/pub/repeatpub/maintitle", method = RequestMethod.POST,
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String repeatPubMain(@ModelAttribute RepeatPubVO repeatPubVO) {
    try {
      repeatPubShowService.repeatPubMainTitle(repeatPubVO);
      if ("success".equals(repeatPubVO.getResultMap().get("result"))) {
        Object count = repeatPubVO.getResultMap().get("count");
        repeatPubVO.getResultMap().put("msg", getText("你有以下" + count.toString() + "组可能重复的成果",
            "You may have " + count.toString() + " group(s) of duplicated publications."));
        // 显示立即处理
        repeatPubVO.getResultMap().put("dealwith", getText("立即处理", "Immediate Processing"));
        repeatPubVO.getResultMap().put("box_title", getText("你有以下" + count.toString() + "组可能重复的成果",
            "You have the following " + count.toString() + " group(s) of duplicated publications."));
      }
    } catch (Exception e) {
      logger.error("重复成果显示title出错，psnId=" + repeatPubVO.getPsnId(), e);
      repeatPubVO.getResultMap().put("result", "error");
    }
    return JacksonUtils.mapToJsonStr(repeatPubVO.getResultMap());
  }

  /**
   * 删除重复成果
   * 
   * @param pubCitedVo
   * @return
   */
  @RequestMapping(value = "/pub/repeatpub/del", method = RequestMethod.POST,
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String repeatPubDel(@ModelAttribute RepeatPubVO repeatPubVO) {
    try {
      repeatPubOptService.repeatPubDel(repeatPubVO);

    } catch (Exception e) {
      logger.error("删除重复成果出错，pubId=" + Des3Utils.decodeFromDes3(repeatPubVO.getDes3PubId()), e);
      repeatPubVO.getResultMap().put("result", "error");
    }
    return JacksonUtils.mapToJsonStr(repeatPubVO.getResultMap());
  }

  /**
   * 重复成果保留
   * 
   * @param pubCitedVo
   * @return
   */
  @RequestMapping(value = "/pub/repeatpub/keep", method = RequestMethod.POST,
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String repeatPubKeep(@ModelAttribute RepeatPubVO repeatPubVO) {
    try {
      repeatPubOptService.repeatPubKeep(repeatPubVO);
    } catch (Exception e) {
      logger.error("重复成果保留，pubId=" + Des3Utils.decodeFromDes3(repeatPubVO.getDes3PubId()), e);
      repeatPubVO.getResultMap().put("result", "error");
    }
    return JacksonUtils.mapToJsonStr(repeatPubVO.getResultMap());
  }


  /**
   * 显示重复记录页面
   */
  @RequestMapping(value = "/pub/ajaxrepeatpublist", method = RequestMethod.POST)
  public ModelAndView showRepeatPubPage(@ModelAttribute RepeatPubVO repeatPubVO) {
    ModelAndView view = new ModelAndView();
    try {
      repeatPubShowService.showRepeatPubPage(repeatPubVO);
      view.addObject("repeatPubVO", repeatPubVO);
    } catch (Exception e) {
      logger.error("显示重复记录出错，recordId=" + Des3Utils.decodeFromDes3(repeatPubVO.getDes3RecordId()), e);
    }
    view.setViewName("/pub/repeatpub/repeat_pub_list");
    return view;
  }


  public String getText(String zhText, String enText) {
    Locale locale = LocaleContextHolder.getLocale();
    if (locale.equals(locale.CHINA) || locale.equals(locale.CHINESE)) {
      return zhText;
    } else {
      return enText;
    }
  }
}
