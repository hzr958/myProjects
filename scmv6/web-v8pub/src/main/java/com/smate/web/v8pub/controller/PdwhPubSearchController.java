package com.smate.web.v8pub.controller;

import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.utils.filter.FilterAllSpecialCharacter;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.spring.SpringUtils;
import com.smate.core.base.utils.string.JnlFormateUtils;
import com.smate.web.v8pub.service.pdwh.PdwhSearchService;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.vo.PubListVO;
import com.smate.web.v8pub.vo.PubQueryInSolrVO;

@Controller
public class PdwhPubSearchController {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource
  private PdwhSearchService pdwhSearchService;
  @Value("${domainscm}")
  private String domain;

  /**
   * PC-人员检索.
   * 
   * @return
   */
  @RequestMapping("/pub/search/psnsearch")
  public ModelAndView psnsearch(String searchString) {
    ModelAndView view = new ModelAndView();
    view.addObject("searchString", searchString);
    view.setViewName("/pub/search/main");
    return view;
  }

  /**
   * pc-论文检索
   * 
   * @param searchString
   * @return
   */
  @RequestMapping("/pub/search/pdwhpaper")
  public ModelAndView getPaperMain(String searchString) {
    ModelAndView view = new ModelAndView();
    view.addObject("searchString", searchString);
    view.setViewName("/pub/search/paper_main");
    return view;
  }

  @RequestMapping("/pub/search/pdwhmain")
  public ModelAndView getMain(String searchString) {
    ModelAndView view = new ModelAndView();
    view.addObject("searchString", searchString);
    try {
      HttpServletResponse response = SpringUtils.getResponse();
      searchString = URLEncoder.encode(searchString, "utf-8");
      if (StringUtils.isNotBlank(searchString) && NumberUtils.isDigits(searchString) && searchString.length() == 8) {
        response.sendRedirect(domain + "/pub/search/psnsearch?searchString=" + searchString);
      } else {
        response.sendRedirect(domain + "/pub/search/pdwhpaper?searchString=" + searchString);
      }
      return null;
    } catch (Exception e) {
      view.setViewName("/pub/search/search_main");
      logger.error("首页检索出错", e);
    }

    return view;
  }

  /**
   * pc-论文检索--通过用户下拉框选中检索
   * 
   * @param searchString
   * @return
   */
  @RequestMapping("/pub/search/pdwhpaperbyac")
  public ModelAndView getPaperMainNew(String suggestStrType, String suggestStrPsn, String suggestStrPid,
      String suggestStrIns, String suggestStrKw, String searchString, String suggestStrInsId) {
    ModelAndView view = new ModelAndView();
    view.addObject("suggestStrType", suggestStrType);
    view.addObject("suggestStrPsn", suggestStrPsn);
    view.addObject("suggestStrPid", suggestStrPid);
    view.addObject("suggestStrIn", suggestStrIns);
    view.addObject("suggestStrKw", suggestStrKw);
    view.addObject("suggestStrInsId", suggestStrInsId);
    view.addObject("searchString", searchString);
    view.setViewName("/pub/search/paper_main_new");

    return view;
  }

  /**
   * pc-专利检索
   * 
   * @param searchString
   * @return
   */
  @RequestMapping("/pub/search/pdwhpatent")
  public ModelAndView getPatentMain(String searchString) {
    ModelAndView view = new ModelAndView();
    view.addObject("searchString", searchString);
    view.setViewName("/pub/search/patent_main");

    return view;
  }

  /**
   * PC-论文检索Action 获取论文列表.
   * 
   * @return
   */
  @RequestMapping("/pub/search/ajaxpdwhpaperlist")
  public ModelAndView getPapers(PubQueryInSolrVO pubQueryInSolrVO) {

    ModelAndView view = new ModelAndView();
    PubListVO pubListVO = new PubListVO();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      String searchString = StringEscapeUtils.unescapeHtml4(pubQueryInSolrVO.getSearchString());
      // 截取字符串
      searchString = interceptSearchKey(searchString);
      Boolean isDoi = JnlFormateUtils.isDoi(searchString);
      // 特殊字符处理
      if (isDoi) {
        pubQueryInSolrVO.setIsDoi(true);
        String searchKey = FilterAllSpecialCharacter.StringFilter(searchString);
        pubQueryDTO.setSearchString(searchKey);
      } else {
        pubQueryDTO.setIsDoi(false);
        // 只有这五种特殊字符 , exit
        if (FilterAllSpecialCharacter.isExcludedChars(searchString)) {
          pubQueryDTO.setSearchString("");
        } else {
          pubQueryDTO.setSearchString(FilterAllSpecialCharacter.stringFilterExcludeFiveChar(searchString));
        }
      }
      if (StringUtils.isNotBlank(pubQueryDTO.getSearchString())) {
        pubQueryDTO.setSeqQuery(pubQueryDTO.getSeqQuery());
        // 检索的字符串
        pubQueryDTO.setSearchString(pubQueryDTO.getSearchString());
        // 发表年份（单个）
        pubQueryDTO.setSearchPubYear(pubQueryInSolrVO.getSearchPubYear());
        // 成果类型（单个）
        pubQueryDTO.setSearchPubTypeId(pubQueryInSolrVO.getSearchPubTypeId());
        pubQueryDTO.setServiceType("paperListInSolr");
        if (pubQueryInSolrVO.getSuggestType() != null && !("0".equals(pubQueryInSolrVO.getSuggestType()))) {
          pubQueryDTO.setSuggestType(pubQueryInSolrVO.getSuggestType());
          pubQueryDTO.setSuggestInsName(pubQueryInSolrVO.getSuggestInsName());
          pubQueryDTO.setSuggestPsnId(pubQueryInSolrVO.getSuggestPsnId());
          pubQueryDTO.setSuggestPsnName(pubQueryInSolrVO.getSuggestPsnName());
          pubQueryDTO.setSuggestInsId(pubQueryInSolrVO.getSuggestInsId());
          pubQueryDTO.setServiceType("paperListBySuggestInSolr");
        }
        // 排序
        pubQueryDTO.setOrderBy(pubQueryInSolrVO.getOrderBy());
        pubQueryDTO.setPageNo(pubQueryInSolrVO.getPage().getPageNo());
        pubQueryDTO.setPageSize(pubQueryInSolrVO.getPage().getPageSize());
        if (pubQueryInSolrVO.getPage().getPageNo() == 1) {// 第一页时要将左侧的状态分类栏显示出来
          pubQueryDTO.setSeqQuery(PubQueryDTO.SEQ_2);
        }
        pubListVO.setPubQueryDTO(pubQueryDTO);
        pdwhSearchService.getPapers(pubListVO);
        view.addObject(pubListVO);
        view.setViewName("/pub/search/paper_menu_list");
      } else {
        view.setViewName("/pub/search/none_list");
      }
    } catch (Exception e) {
      logger.error("获取论文列表失败", e);
    }
    return view;
  }

  @RequestMapping("/pub/search/ajaxrecmpaperlist")
  public ModelAndView getrecmPapers(PubQueryInSolrVO pubQueryInSolrVO) {
    ModelAndView view = new ModelAndView();
    PubListVO pubListVO = new PubListVO();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSeqQuery(PubQueryDTO.SEQ_1);
    try {
      String searchString = StringEscapeUtils.unescapeHtml4(pubQueryInSolrVO.getSearchString());
      // 截取字符串
      searchString = interceptSearchKey(searchString);
      Boolean isDoi = JnlFormateUtils.isDoi(searchString);
      // 特殊字符处理
      if (isDoi) {
        pubQueryInSolrVO.setIsDoi(true);
        String searchKey = FilterAllSpecialCharacter.StringFilter(searchString);
        pubQueryDTO.setSearchString(searchKey);
      } else {
        pubQueryDTO.setIsDoi(false);
        // 只有这五种特殊字符 , exit
        if (FilterAllSpecialCharacter.isExcludedChars(searchString)) {
          pubQueryDTO.setSearchString("");
        } else {
          pubQueryDTO.setSearchString(FilterAllSpecialCharacter.stringFilterExcludeFiveChar(searchString));
        }
      }
      if (StringUtils.isNotBlank(pubQueryDTO.getSearchString())) {
        pubQueryDTO.setPageNo(pubQueryInSolrVO.getPage().getPageNo());
        pubQueryDTO.setPageSize(pubQueryInSolrVO.getPage().getPageSize());
        pubListVO.setPubQueryDTO(pubQueryDTO);
        pubListVO.getPubQueryDTO().setServiceType("paperListInSolr");
        pdwhSearchService.getPapers(pubListVO);
        view.addObject(pubListVO);
        view.setViewName("/pub/search/pub_add_list");
      } else {
        return null;
      }
    } catch (Exception e) {
      logger.error("获取论文列表失败", e);
    }
    return view;
  }

  /**
   * PC-专利检索Action 获取论文列表.
   * 
   * @return
   */
  @RequestMapping("/pub/search/ajaxpdwhpatentlist")
  public ModelAndView getPatents(PubQueryInSolrVO pubQueryInSolrVO) {
    ModelAndView view = new ModelAndView();
    PubListVO pubListVO = new PubListVO();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      String searchString = StringEscapeUtils.unescapeHtml4(pubQueryInSolrVO.getSearchString());
      // 截取字符串
      searchString = interceptSearchKey(searchString);
      Boolean isDoi = JnlFormateUtils.isDoi(searchString);
      // 特殊字符处理
      if (isDoi) {
        pubQueryDTO.setIsDoi(true);
        String searchKey = FilterAllSpecialCharacter.StringFilter(searchString);
        pubQueryDTO.setSearchString(searchKey);
      } else {
        pubQueryDTO.setIsDoi(false);
        // 只有这五种特殊字符 , exit
        if (FilterAllSpecialCharacter.isExcludedChars(searchString)) {
          pubQueryDTO.setSearchString("");
        } else {
          pubQueryDTO.setSearchString(FilterAllSpecialCharacter.stringFilterExcludeFiveChar(searchString));
        }
      }
      if (StringUtils.isNotBlank(pubQueryDTO.getSearchString())) {
        // 发表年份（多个，逗号隔开或空格隔开）
        pubQueryDTO.setPublishYear(pubQueryInSolrVO.getPublishYear());
        pubQueryDTO.setSearchPatYear(pubQueryInSolrVO.getSearchPatYear());
        pubQueryDTO.setSearchPatTypeId(pubQueryInSolrVO.getSearchPatTypeId());
        // 专利类型（多个，逗号隔开或空格隔开）
        pubQueryDTO.setSearchPubType(pubQueryInSolrVO.getSearchPubType());
        // 排序
        pubQueryDTO.setOrderBy(pubQueryInSolrVO.getOrderBy());
        // 收录情况
        pubQueryDTO.setIncludeType(pubQueryInSolrVO.getIncludeType());
        pubQueryDTO.setPageNo(pubQueryInSolrVO.getPage().getPageNo());
        pubQueryDTO.setPageSize(pubQueryInSolrVO.getPage().getPageSize());
        pubListVO.setPubQueryDTO(pubQueryDTO);
        pubListVO.getPubQueryDTO().setServiceType("patentListInSolr");
        if (pubQueryInSolrVO.getPage().getPageNo() == 1) {// 第一页时要将左侧的状态分类栏显示出来
          pubQueryDTO.setSeqQuery(PubQueryDTO.SEQ_2);
        }
        pdwhSearchService.getPatents(pubListVO);
        view.addObject(pubListVO);
        view.setViewName("/pub/search/patent_menu_list");
      } else {
        view.setViewName("/pub/search/none_list");
      }
    } catch (Exception e) {
      logger.error("获取论文列表失败", e);
    }
    return view;
  }

  /**
   * PC- 根据个人中英文名，科技领域 获取专利列表.
   * 
   * @return
   */
  @RequestMapping("/pub/search/ajaxrecmpatentlist")
  public ModelAndView getrcmdPatents(PubQueryInSolrVO pubQueryInSolrVO) {
    ModelAndView view = new ModelAndView();
    PubListVO pubListVO = new PubListVO();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSeqQuery(PubQueryDTO.SEQ_1);
    try {
      String searchString = StringEscapeUtils.unescapeHtml4(pubQueryInSolrVO.getSearchString());
      // 截取字符串
      searchString = interceptSearchKey(searchString);
      Boolean isDoi = JnlFormateUtils.isDoi(searchString);
      // 特殊字符处理
      if (isDoi) {
        pubQueryDTO.setIsDoi(true);
        String searchKey = FilterAllSpecialCharacter.StringFilter(searchString);
        pubQueryDTO.setSearchString(searchKey);
      } else {
        pubQueryDTO.setIsDoi(false);
        // 只有这五种特殊字符 , exit
        if (FilterAllSpecialCharacter.isExcludedChars(searchString)) {
          pubQueryDTO.setSearchString("");
        } else {
          pubQueryDTO.setSearchString(FilterAllSpecialCharacter.stringFilterExcludeFiveChar(searchString));
        }
      }
      if (StringUtils.isNotBlank(pubQueryDTO.getSearchString())) {
        pubQueryDTO.setPageNo(pubQueryInSolrVO.getPage().getPageNo());
        pubQueryDTO.setPageSize(pubQueryInSolrVO.getPage().getPageSize());
        pubListVO.setPubQueryDTO(pubQueryDTO);
        pubListVO.getPubQueryDTO().setServiceType("patentListInSolr");

        pdwhSearchService.getPatents(pubListVO);
        view.addObject(pubListVO);
        view.setViewName("/pub/search/patent_add_list");
      } else {
        return null;
      }
    } catch (Exception e) {
      logger.error("获取论文列表失败", e);
    }
    return view;
  }

  /**
   * 获取 论文左侧菜单
   * 
   * @return
   */
  @RequestMapping("/pub/search/ajaxpaperleftmenu")
  public ModelAndView getPaperLeftMenu(PubQueryDTO pubQueryDTO) {
    ModelAndView view = new ModelAndView();
    PubListVO pubListVO = new PubListVO();
    // 第二次查询
    pubQueryDTO.setSeqQuery(PubQueryDTO.SEQ_2);
    String searchString = StringEscapeUtils.unescapeHtml4(pubQueryDTO.getSearchString());
    // 截取字符串
    searchString = interceptSearchKey(searchString);
    Boolean isDoi = JnlFormateUtils.isDoi(searchString);
    // 特殊字符处理
    if (isDoi) {
      pubQueryDTO.setIsDoi(true);
      pubQueryDTO.setSearchString(FilterAllSpecialCharacter.StringFilter(searchString));
    } else {
      pubQueryDTO.setIsDoi(false);
      // 只有这五种特殊字符 , exit
      if (FilterAllSpecialCharacter.isExcludedChars(searchString)) {
        pubQueryDTO.setSearchString("");
      } else {
        pubQueryDTO.setSearchString(FilterAllSpecialCharacter.stringFilterExcludeFiveChar(searchString));
      }
    }
    try {
      if (StringUtils.isNotBlank(pubQueryDTO.getSearchString())) {
        pubQueryDTO.setServiceType("paperListInSolr");
        if (pubQueryDTO.getSuggestType() != null && !("0".equals(pubQueryDTO.getSuggestType()))) {
          pubQueryDTO.setServiceType("paperListBySuggestInSolr");
        }
        pubListVO.setPubQueryDTO(pubQueryDTO);
        Map<String, Object> menuMapTemp = pdwhSearchService.getPaperLeftMenu(pubListVO);
        pubQueryDTO.setYearMap((Map<Long, Long>) menuMapTemp.get("pubYear"));
        pubQueryDTO.setPubTypeMap((Map<Long, Long>) menuMapTemp.get("pubType"));
        pubQueryDTO.setLanguageMap((Map<String, Long>) menuMapTemp.get("languageType"));
      } else {
        Map<Long, Long> pubType = new LinkedHashMap<Long, Long>();
        pubType.put(4L, 0L);
        pubType.put(3L, 0L);
        pubType.put(8L, 0L);
        pubType.put(7L, 0L);
        pubQueryDTO.setPubTypeMap(pubType);

      }

    } catch (Exception e) {
      logger.error("获取论文菜单失败");
    }
    view.addObject(pubQueryDTO);
    view.setViewName("/pub/search/paper_leftmenu");
    return view;
  }

  /**
   * 获取专利左侧菜单
   * 
   * @return
   */
  @RequestMapping("/pub/search/ajaxpatentleftmenu")
  public ModelAndView getPatentLeftMenu(PubQueryDTO pubQueryDTO) {
    ModelAndView view = new ModelAndView();
    PubListVO pubListVO = new PubListVO();
    // 第二次查询
    pubQueryDTO.setSeqQuery(PubQueryDTO.SEQ_2);
    String searchString = StringEscapeUtils.unescapeHtml4(pubQueryDTO.getSearchString());
    // 截取字符串
    searchString = interceptSearchKey(searchString);
    Boolean isDoi = JnlFormateUtils.isDoi(searchString);
    // 特殊字符处理
    if (isDoi) {
      pubQueryDTO.setIsDoi(true);
      pubQueryDTO.setSearchString(FilterAllSpecialCharacter.StringFilter(searchString));
    } else {
      pubQueryDTO.setIsDoi(false);
      // 只有这五种特殊字符 , exit
      if (FilterAllSpecialCharacter.isExcludedChars(searchString)) {
        pubQueryDTO.setSearchString("");
      } else {
        pubQueryDTO.setSearchString(FilterAllSpecialCharacter.stringFilterExcludeFiveChar(searchString));
      }
    }
    try {
      if (StringUtils.isNotBlank(pubQueryDTO.getSearchString())) {
        pubListVO.setPubQueryDTO(pubQueryDTO);
        pubListVO.getPubQueryDTO().setServiceType("patentListInSolr");
        Map<String, Object> menuMapTemp = pdwhSearchService.getPatentLeftMenu(pubListVO);
        pubQueryDTO.setYearMap((Map<Long, Long>) menuMapTemp.get("pubYear"));
        pubQueryDTO.setPubTypeMap((Map<Long, Long>) menuMapTemp.get("pubType"));
        pubQueryDTO.setLanguageMap((Map<String, Long>) menuMapTemp.get("languageType"));
      } else {
        Map<Long, Long> pubType = new LinkedHashMap<Long, Long>();
        pubType.put(51L, 0L);
        pubType.put(52L, 0L);
        pubType.put(53L, 0L);
        pubType.put(7L, 0L);
        pubQueryDTO.setPubTypeMap(pubType);

      }

    } catch (Exception e) {
      logger.error("获取论文菜单失败");
    }
    view.addObject(pubQueryDTO);
    view.setViewName("/pub/search/patent_leftmenu");
    return view;
  }

  /**
   * 截取100个字符，且截取到空格前的，避免单词背截断 SCM-18209
   * 
   * @param searchKey
   * @return
   */
  private String interceptSearchKey(String searchKey) {
    String result = searchKey;
    if (StringUtils.isNotBlank(searchKey) && searchKey.length() > 100) {
      int lastLength = searchKey.length() > 150 ? 150 : searchKey.length();
      result = searchKey.substring(0, 99);
      String subStr2 = searchKey.substring(99, lastLength);
      if (subStr2.indexOf(" ") != -1) {
        String completeWord = subStr2.substring(0, subStr2.indexOf(" "));
        result += completeWord;
      }
    }
    return result;
  }

}
