package com.smate.web.v8pub.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smate.web.v8pub.service.journal.JournalService;


/**
 * 成果期刊控制器
 * 
 * @author aijiangbin
 * @date 2018年9月12日
 */
@Controller
public class JournalController {


  @Resource
  private JournalService journalService;


  /**
   * 添加期刊功能 只要保存到，sns库
   * 
   * @param jsonData
   * @return
   */
  @RequestMapping(value = "/pub/addjournal", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public String ajaxAddJournal(String jname, String jissn) {
    String result = "";
    if (StringUtils.isNotBlank(jname)) {
      result = journalService.ajaxAddJournalByPubEnter(jname, jissn);
    }
    return result;
  }



}
