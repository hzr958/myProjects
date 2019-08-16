package com.smate.web.v8pub.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.service.pdwh.CategoryHightechIndustryService;
import com.smate.web.v8pub.vo.pdwh.PdwhPubIndustryVO;

/**
 * 成果行业
 * 
 * @author YJ
 *
 *         2019年5月30日
 */
@Controller
public class PubIndustryController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CategoryHightechIndustryService categoryHightechIndustryService;

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/pub/industry/ajaxselect", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String findPubIndustry(@ModelAttribute("codes") String codes) {
    Map<String, Object> map = new HashMap<>();
    try {
      // 1.加载左级列表（行业主列表）
      String leftData = categoryHightechIndustryService.findAllIndustry();
      List<PdwhPubIndustryVO> industrys = JacksonUtils.jsonToCollection(leftData, List.class, PdwhPubIndustryVO.class);
      map.put("leftData", industrys);
      // 2.加载选中元素
      List<PdwhPubIndustryVO> rightData = categoryHightechIndustryService.buildChooseIndustry(codes);
      map.put("rightData", rightData);
    } catch (Exception e) {
      logger.error("根据行业code查询子级分类失败！codes={}", codes, e);
    }
    return JacksonUtils.mapToJsonStr(map);
  }

}
