package com.smate.center.job.web.controller;

import com.smate.center.job.common.enums.JobWeightEnum;
import com.smate.center.job.common.exception.ServiceException;
import com.smate.center.job.framework.service.OnlineJobConfigService;
import com.smate.center.job.framework.service.OnlineJobService;
import com.smate.center.job.web.support.jqgrid.PageModel;
import com.smate.center.job.web.support.jqgrid.SearchOper;
import com.smate.center.job.web.vo.JsonResult;
import com.smate.center.job.web.vo.OnlineJobConfigVO;
import com.smate.center.job.web.vo.OnlineJobVO;
import com.smate.core.base.utils.string.StringUtils;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author houchuanjie
 * @date 2018/05/19 17:22
 */
@Controller
@RequestMapping("/job")
public class OnlineJobController {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OnlineJobConfigService onlineJobConfigService;
  @Autowired
  private OnlineJobService onlineJobService;

  @RequestMapping("online/index")
  public String onlineView(Model model) {
    StringBuilder sb = new StringBuilder();
    for (JobWeightEnum weight : JobWeightEnum.values()) {
      sb.append(weight.name() + ":" + weight.getValue() + ";");
    }
    sb.deleteCharAt(sb.lastIndexOf(";"));
    model.addAttribute("jobWeightEnums", sb.toString());
    return "online-index";
  }

  @ResponseBody
  @RequestMapping("online/config/search")
  public PageModel searchOnlineConfig(@ModelAttribute PageModel<OnlineJobConfigVO> pageModel) {
    PageModel page = onlineJobConfigService.search(pageModel);
    return page;
  }

  @ResponseBody
  @RequestMapping("online/config/add")
  public JsonResult addConfig(@ModelAttribute OnlineJobConfigVO onlineJobConfigVO) {
    try {
      if (Objects.isNull(onlineJobConfigVO.getPriority())) {
        onlineJobConfigVO.setPriority(99);
      }
      onlineJobConfigService.addJob(onlineJobConfigVO);
      return new JsonResult(true, "添加成功！", onlineJobConfigVO);
    } catch (Exception e) {
      logger.error("新增在线任务失败！{}", onlineJobConfigVO, e);
      return new JsonResult(false, "添加失败！", null);
    }
  }

  @ResponseBody
  @RequestMapping("online/config/edit")
  public JsonResult editConfig(@ModelAttribute OnlineJobConfigVO onlineJobConfigVO) {
    try {
      onlineJobConfigService.updateJobInfo(onlineJobConfigVO);
      return new JsonResult(true, "添加成功！", onlineJobConfigVO);
    } catch (ServiceException e) {
      logger.error("修改在线任务失败！{}", onlineJobConfigVO, e);
      return new JsonResult(false, "系统错误，修改失败！", null);
    }
  }

  @ResponseBody
  @RequestMapping("online/config/delete")
  public JsonResult deleteConfig(@RequestParam("id") String ids) {
    try {
      List<String> idList = StringUtils.split2List(ids, ",", s -> s);
      if (CollectionUtils.isNotEmpty(idList)) {
        onlineJobConfigService.batchDelete(idList);
        return new JsonResult(true, "删除成功！", idList);
      } else {
        return new JsonResult(false, "要删除的id参数不能为空！", null);
      }
    } catch (ServiceException e) {
      logger.error("批量删除在线任务失败！ids={}", ids, e);
      return new JsonResult(false, "删除失败！", null);
    }
  }

  @ResponseBody
  @RequestMapping("online/list/search/failed")
  public PageModel listFailed(@ModelAttribute PageModel<OnlineJobVO> pageModel,
      @RequestParam("name") String jobName) {
    checkSingleSearch(jobName, pageModel);
    PageModel page = onlineJobService.searchFailed(pageModel);
    return page;
  }

  @ResponseBody
  @RequestMapping("online/list/search/processed")
  public PageModel listDone(@ModelAttribute PageModel<OnlineJobVO> pageModel,
      @RequestParam("name") String jobName) {
    checkSingleSearch(jobName, pageModel);
    return onlineJobService.searchHistory(pageModel);
  }

  @ResponseBody
  @RequestMapping("online/list/search/unprocessed")
  public PageModel listUnprocessed(@ModelAttribute PageModel<OnlineJobVO> pageModel,
      @RequestParam("name") String jobName) {
    checkSingleSearch(jobName, pageModel);
    PageModel page = onlineJobService.searchUnprocessed(pageModel);
    return page;
  }

  private void checkSingleSearch(String jobName, PageModel pageModel) {
    if (StringUtils.isNotBlank(jobName)) {
      pageModel.setSearchField("name");
      pageModel.setSearchOper(SearchOper.EQ);
      pageModel.setSearchString(jobName.trim());
      pageModel.setSearch(true);
    }
  }

  @ResponseBody
  @RequestMapping("online/list/delete/failed")
  public JsonResult deleteFailed(@RequestParam("id") String ids) {
    return delete(ids);
  }

  @ResponseBody
  @RequestMapping("online/list/delete/unprocessed")
  public JsonResult deleteUnprocessed(@RequestParam("id") String ids) {
    return delete(ids);
  }

  @ResponseBody
  @RequestMapping("online/list/delete/processed")
  public JsonResult deleteProcessed(@RequestParam("id") String ids) {
    try {
      List<String> idList = StringUtils.split2List(ids, ",", s -> s);
      if (CollectionUtils.isNotEmpty(idList)) {
        onlineJobService.batchDeleteHistory(idList);
        return new JsonResult(true, "删除成功！", idList);
      } else {
        return new JsonResult(false, "要删除的id参数不能为空！", null);
      }
    } catch (ServiceException e) {
      logger.error("批量删除在线任务失败！ids={}", ids, e);
      return new JsonResult(false, "删除失败！", null);
    }
  }

  private JsonResult delete(String ids) {
    try {
      List<String> idList = StringUtils.split2List(ids, ",", s -> s);
      if (CollectionUtils.isNotEmpty(idList)) {
        onlineJobService.batchDelete(idList);
        return new JsonResult(true, "删除成功！", idList);
      } else {
        return new JsonResult(false, "要删除的id参数不能为空！", null);
      }
    } catch (ServiceException e) {
      logger.error("批量删除在线任务失败！ids={}", ids, e);
      return new JsonResult(false, "删除失败！", null);
    }
  }

  @ResponseBody
  @RequestMapping(value = "online/list/save", method = RequestMethod.POST)
  public JsonResult edit(@ModelAttribute OnlineJobVO onlineJobVO,
      @RequestParam("oper") String oper) {
    String operStr = "修改";
    if ("add".equalsIgnoreCase(oper)) {
      onlineJobVO.setId(null);
      operStr = "添加";
    }
    try {
      onlineJobVO = onlineJobService.saveOrUpdate(onlineJobVO);
      return new JsonResult(true, operStr + "成功！", onlineJobVO);
    } catch (ServiceException e) {
      return new JsonResult(false, operStr + "失败！", null);
    }
  }

}
