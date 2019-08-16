package com.smate.web.v8pub.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.homepage.PubConfirmService;
import com.smate.web.v8pub.vo.PubListVO;
import com.smate.web.v8pub.vo.PubQueryModel;

/**
 * 成果认领控制器
 * 
 * @author aijiangbin
 * @date 2018年8月9日
 */
@Controller
public class PubConfirmController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Resource
  private PubConfirmService pubConfirmService;


  @RequestMapping("/pub/ajaxgetpubconfirmlist")
  @ResponseBody
  public ModelAndView ajaxgetpubconfirm(PubQueryModel pubQueryModel) {

    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
      pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
    } catch (Exception e) {
      logger.error("复制属性异常", e);
    }
    PubListVO pubListVO = new PubListVO();
    pubListVO.setPubQueryDTO(pubQueryDTO);
    ModelAndView view = new ModelAndView();
    pubListVO.getPubQueryDTO().setServiceType("pubConfirmList");
    pubListVO.getPubQueryDTO().setSearchPsnId(pubListVO.getPsnId());
    pubListVO.getPubQueryDTO().setOrderBy("pubId");
    pubConfirmService.queryPubComfirm(pubListVO);
    view.addObject(pubListVO);
    if (pubQueryModel.getIsAll() == 1) {
      // 判断是否有全文认领
      pubListVO.setHasPubfulltextConfirm(pubConfirmService.checkPubfulltextConfirm(pubListVO));
      view.setViewName("/pub/confirm/pub_confirm_list");
    } else {
      view.setViewName("/pub/confirm/pub_confirm");
    }

    return view;
  }

  @RequestMapping("/pub/ajaxpubconfirmlist")
  @ResponseBody
  public ModelAndView getPubList(PubQueryModel pubQueryModel) {

    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
      pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
    } catch (Exception e) {
      logger.error("复制属性异常", e);
    }
    PubListVO pubListVO = new PubListVO();
    pubListVO.setPubQueryDTO(pubQueryDTO);
    ModelAndView view = new ModelAndView();
    pubListVO.getPubQueryDTO().setServiceType("pubConfirmList");
    pubListVO.getPubQueryDTO().setSearchPsnId(pubListVO.getPsnId());
    pubListVO.getPubQueryDTO().setOrderBy("pubId");
    pubConfirmService.queryPubComfirm(pubListVO);
    view.addObject(pubListVO);
    view.setViewName("/pub/confirm/pub_add_list");
    return view;
  }

  /**
   * 判断成果认领是否有数据
   * 
   * @param pubQueryModel
   * @return
   */
  @RequestMapping("/pub/ajaxgetpubconfirmcount")
  @ResponseBody
  public void ajaxGetPubConfirmCount(PubQueryModel pubQueryModel, HttpServletResponse response) {
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
      pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
    } catch (Exception e) {
      logger.error("复制属性异常", e);
    }
    Map<String, Integer> result = new HashMap<String, Integer>();
    PubListVO pubListVO = new PubListVO();
    pubListVO.setPubQueryDTO(pubQueryDTO);
    pubListVO.getPubQueryDTO().setServiceType("pubConfirmList");
    pubListVO.getPubQueryDTO().setSearchPsnId(pubListVO.getPsnId());
    pubListVO.getPubQueryDTO().setOrderBy("pubId");
    pubConfirmService.queryPubComfirm(pubListVO);
    result.put("count", pubListVO.getTotalCount());
    try {
      response.getWriter().print(JacksonUtils.mapToJsonStr(result));
    } catch (IOException e) {
      logger.error("查询成果认领条数出错", e);
    }
  }

  /**
   * 首页动态获取成果认领列表
   * 
   * @param pubQueryModel
   * @return
   */
  @RequestMapping("/pub/mainmodule/ajaxgetdynpubconfirm")
  @ResponseBody
  public ModelAndView ajaxgetdynpubconfirm(PubQueryModel pubQueryModel) {
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
      pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
    } catch (Exception e) {
      logger.error("复制属性异常", e);
    }
    PubListVO pubListVO = new PubListVO();
    pubListVO.setPubQueryDTO(pubQueryDTO);
    ModelAndView view = new ModelAndView();
    pubListVO.getPubQueryDTO().setServiceType("pubConfirmList");
    pubListVO.getPubQueryDTO().setSearchPsnId(pubListVO.getPsnId());
    pubListVO.getPubQueryDTO().setOrderBy("pubId");
    pubConfirmService.queryPubComfirm(pubListVO);
    view.addObject(pubListVO);
    if (pubQueryModel.getIsAll() == 1) {
      view.setViewName("/pub/confirm/dyn_pub_confirm_list");
      // 判断是否有全文认领
      pubListVO.setHasPubfulltextConfirm(pubConfirmService.checkPubfulltextConfirm(pubListVO));
    } else {
      view.setViewName("/pub/confirm/dyn_pub_confirm");
    }
    return view;
  }

  /**
   * 成果认领接口-确认认领 接收的参数：{"des3PubId":"基准库的pubId","des3PsnId":""} 返回的格式：成功：{"status": "SUCCESS"}
   * 失败：{"status": "ERROR","msg":"错误信息"}
   * 
   * @param jsonData
   * @return
   */
  @RequestMapping(value = "/data/pub/affirmconfirm", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public String pubConfirm(@RequestBody String jsonData) {
    Map<String, String> map = JacksonUtils.jsonMapUnSerializer(jsonData);
    String des3PdwhPubId = map.get("des3PubId");
    String des3PsnId = map.get("des3PsnId");
    String result = pubConfirmService.affirmPubComfirm(des3PdwhPubId, des3PsnId);
    return result;
  }

  /**
   * 忽略成果认领
   * 
   * @param jsonData
   * @return
   */
  @RequestMapping(value = "/data/pub/ignoreconfirm", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public String ignorePubConfirm(@RequestBody String jsonData) {
    Map<String, String> map = JacksonUtils.jsonMapUnSerializer(jsonData);
    String des3PdwhPubId = map.get("des3PubId");
    String des3PsnId = map.get("des3PsnId");
    String result = pubConfirmService.ignorePubComfirm(des3PdwhPubId, des3PsnId);
    return result;
  }

}
