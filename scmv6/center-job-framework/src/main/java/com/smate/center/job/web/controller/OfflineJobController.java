package com.smate.center.job.web.controller;

import com.smate.center.job.common.enums.JobWeightEnum;
import com.smate.center.job.common.exception.ServiceException;
import com.smate.center.job.framework.exception.CouldNotStopJobException;
import com.smate.center.job.framework.service.JobManageService;
import com.smate.center.job.framework.service.OfflineJobService;
import com.smate.center.job.web.support.jqgrid.PageModel;
import com.smate.center.job.web.vo.JsonResult;
import com.smate.center.job.web.vo.OfflineJobVO;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.string.StringUtils;
import java.util.List;
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
 * 任务相关视图控制器
 *
 * @author houchuanjie
 * @date 2018/05/16 10:01
 */
@Controller
@RequestMapping("/job")
public class OfflineJobController {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OfflineJobService offlineJobService;
  @Autowired
  private JobManageService jobManageService;

  @RequestMapping("offline/index")
  public String offlineView(Model model) {
    StringBuilder sb = new StringBuilder();
    for (DBSessionEnum db : DBSessionEnum.values()) {
      sb.append(db.name() + ":" + db.getValue() + ";");
    }
    sb.deleteCharAt(sb.lastIndexOf(";"));
    model.addAttribute("dbSessionEnums", sb.toString());
    sb = new StringBuilder();
    for (JobWeightEnum weight : JobWeightEnum.values()) {
      sb.append(weight.name() + ":" + weight.getValue() + ";");
    }
    sb.deleteCharAt(sb.lastIndexOf(";"));
    model.addAttribute("jobWeightEnums", sb.toString());
    return "offline-index";
  }

  @RequestMapping(value = "offline/search", method = {RequestMethod.POST})
  @ResponseBody
  public PageModel searchOffline(@ModelAttribute PageModel<OfflineJobVO> pageModel) {
    PageModel page = offlineJobService.search(pageModel);
    return page;
  }

  @RequestMapping("offline/add")
  @ResponseBody
  public JsonResult add(@ModelAttribute OfflineJobVO offlineJobVO) {
    try {
      offlineJobService.addJob(offlineJobVO);
      return new JsonResult(true, "添加成功！", offlineJobVO);
    } catch (Exception e) {
      logger.error("新增离线任务失败！{}", offlineJobVO, e);
      return new JsonResult(false, "添加失败！", null);
    }
  }

  @RequestMapping("offline/edit")
  @ResponseBody
  public JsonResult edit(@ModelAttribute OfflineJobVO offlineJobVO) {
    try {
      offlineJobService.updateJobInfo(offlineJobVO);
      return new JsonResult(true, "修改成功！", offlineJobVO);
    } catch (Exception e) {
      logger.error("更新离线任务失败！offlineJobVO={}", offlineJobVO, e);
      return new JsonResult(false, "修改失败！", offlineJobVO);
    }
  }

  @RequestMapping("offline/toggle-enable")
  @ResponseBody
  public JsonResult edit(String id, Boolean enable) {
    try {
      if (StringUtils.isBlank(id) || enable == null) {
        logger.error("参数信息不正确！id='{}', enable={}", id, enable);
        return new JsonResult(false, "参数不正确！", null);
      }
      OfflineJobVO offlineJobVO = offlineJobService.enableJob(id, enable);
      return new JsonResult(true, "修改成功！", offlineJobVO);
    } catch (ServiceException e) {
      logger.error("更新离线任务失败！id='{}'", id, e);
      return new JsonResult(false, "修改失败！", null);
    }
  }

  @RequestMapping("offline/op-run")
  @ResponseBody
  public JsonResult opRun(String id, String op) {
    if (StringUtils.isBlank(id) || StringUtils.isBlank(op)) {
      logger.error("参数信息不正确！id='{}', op='{}'", id, op);
      return new JsonResult(false, "参数不正确！", null);
    }
    String msg = "";
    try {
      OfflineJobVO offlineJobVO = new OfflineJobVO();
      switch (op) {
        case "repeat":
        case "run":
          msg = "操作成功，任务稍后将会执行...";
          offlineJobVO = jobManageService.runJob(id);
          break;
        case "stop":
          msg = "操作成功，任务已停止！";
          offlineJobVO = jobManageService.stopJob(id);
          break;
      }
      return new JsonResult(true, msg, offlineJobVO);
    } catch (ServiceException e) {
      logger.error("{}离线任务失败！jobId='{}', op='{}'", op.equals("stop") ? "停止" : "启动", id, op, e);
      return new JsonResult(false, "操作失败，系统出现意料之外的错误！", null);
    } catch (CouldNotStopJobException e){
      logger.warn("暂时无法停止任务！{}", e.toString());
      return new JsonResult(false, "任务停止超时，请稍后再试！", null);
    }
  }


  @RequestMapping("offline/delete")
  @ResponseBody
  public JsonResult delete(@RequestParam("id") String ids) {
    try {
      List<String> idList = StringUtils.split2List(ids, ",", s -> s);
      if (CollectionUtils.isNotEmpty(idList)) {
        offlineJobService.batchDelete(idList);
        return new JsonResult(true, "删除成功！", idList);
      } else {
        return new JsonResult(false, "要删除的id参数不能为空！", null);
      }
    } catch (ServiceException e) {
      logger.error("批量删除离线任务失败！ids={}", ids, e);
      return new JsonResult(false, "删除失败！", null);
    }
  }


  @RequestMapping("offline/refresh")
  @ResponseBody
  public JsonResult refreshJobInfos(@RequestParam("ids") String ids) {
    List<String> idList = StringUtils.split2List(ids, ",", s -> s);
    if (CollectionUtils.isNotEmpty(idList)) {
      List<OfflineJobVO> newestList = offlineJobService.getNewestList(idList);
      return new JsonResult(true, "获取成功！", newestList);
    } else {
      return new JsonResult(false, "ids参数为空，没有要获取的数据！", null);
    }
  }
}
