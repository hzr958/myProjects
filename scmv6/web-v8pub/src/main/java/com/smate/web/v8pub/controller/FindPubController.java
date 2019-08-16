package com.smate.web.v8pub.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.utils.filter.FilterAllSpecialCharacter;
import com.smate.core.base.utils.solr.SolrUtil;
import com.smate.web.v8pub.service.findpub.FindPubService;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.query.PubQueryhandlerService;
import com.smate.web.v8pub.vo.FindPubVO;
import com.smate.web.v8pub.vo.PubListResult;

@Controller
public class FindPubController {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubQueryhandlerService pubQueryhandlerService;
  @Autowired
  private FindPubService findPubService;

  @RequestMapping(value = "/pub/ajaxfindpubmain")
  public Object showFindPubMain() {
    return "/pub/findpub/findPubMain";
  }

  @RequestMapping(value = "/pub/ajaxfindpubconditions")
  public Object leftConditionShow() {
    FindPubVO pubVo = new FindPubVO();
    List<Map<String, Object>> allArea = findPubService.getAllScienceArea();// 获取所有的科技领域
    pubVo.setScienceAreas(allArea);
    Calendar cal = Calendar.getInstance();
    pubVo.setCurrentYear(cal.get(Calendar.YEAR));// 获取当前的年份

    ModelAndView model = new ModelAndView();
    model.addObject("pubVo", pubVo);
    model.setViewName("/pub/findpub/findPubCondition");
    return model;
  }

  @RequestMapping(value = "/pub/ajaxfindpublist")
  public Object findPubList(FindPubVO pubVo) {
    ModelAndView view = new ModelAndView();
    PubListResult pubListResult = new PubListResult();
    try {
      String searchString = StringEscapeUtils.unescapeHtml4(pubVo.getSearchKey());
      // 只有这五种特殊字符 , exit
      if (FilterAllSpecialCharacter.isExcludedChars(searchString)) {
        pubVo.setSearchKey("");
      } else {
        pubVo.setSearchKey(SolrUtil.escapeQueryChars(searchString));
      }

      PubQueryDTO pubQueryDTO = new PubQueryDTO();
      copyConditionToPubQueryDTO(pubVo, pubQueryDTO);// 查询条件复制
      pubQueryDTO.setServiceType("findListInSolr");
      pubListResult = pubQueryhandlerService.queryPub(pubQueryDTO);
      if ((pubListResult.getTotalCount() / pubQueryDTO.getPageSize()) > 1000) {// 最多显示一千页
        pubListResult.setTotalCount(1000 * pubQueryDTO.getPageSize());
      }
    } catch (Exception e) {
      logger.error("PC---》我的论文---》发现，获取论文列表失败", e);
    }
    view.addObject("pubListVO", pubListResult);
    view.setViewName("/pub/findpub/findPubList");
    return view;
  }

  /**
   * 查询条件赋值
   * 
   * @param pubVo
   * @param pubQueryDTO
   */
  private void copyConditionToPubQueryDTO(FindPubVO pubVo, PubQueryDTO pubQueryDTO) {
    pubQueryDTO.setSearchArea(pubVo.getSearchArea());// 科技领域多个用逗号分隔
    pubQueryDTO.setSearchString(pubVo.getSearchKey());
    pubQueryDTO.setPublishYear(pubVo.getPublishYear());// 出版年份
    pubQueryDTO.setIncludeType(pubVo.getIncludeType());// 收录情况
    pubQueryDTO.setSearchPubType(pubVo.getSearchPubType());// 成果类型
    // pubQueryDTO.setSearchLanguage(pubVo.getSearchLanguage());// 语言
    pubQueryDTO.setOrderBy(pubVo.getOrderBy());// 排序
    pubQueryDTO.setPageNo(pubVo.getPage().getParamPageNo());
    pubQueryDTO.setPageSize(pubVo.getPage().getPageSize());

  }
}
