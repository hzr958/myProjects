package com.smate.center.job.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smate.center.job.framework.service.OfflineJobService;

/**
 * 任务管理控制器
 * 
 * @author houchuanjie
 * @date 2018年2月7日 上午10:27:33
 */
@Controller
@RequestMapping("/job")
public class JobManageController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OfflineJobService offlineJobInfoService;

    @RequestMapping("admin/index")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("admin/login")
    public String login(Model model){
        return "login";
    }
}
