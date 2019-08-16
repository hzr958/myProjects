package com.smate.web.v8pub.controller;


import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.service.sns.resume.PersonalResumePubService;
import com.smate.web.v8pub.vo.PersonalResumePubVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PersonalResumePubController {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonalResumePubService personalResumePubService;

  /**
   * 弹出人员简历成果选择框
   * 
   * @return
   */
  @RequestMapping("/pub/psncv/ajaxselectpub")
  public ModelAndView editCVPubInfo(PersonalResumePubVO form) {
    ModelAndView model = new ModelAndView("/pub/resume/psn_cvpub_edit");
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    try {
      // 先判断是否是简历拥有者
      boolean isOwner = personalResumePubService.isOwnerOfCV(form.getCvId(), currentPsnId);
      if (isOwner) {
        // 查找已选中成果
        form.setPubInfoList(
            personalResumePubService.findPsnCVSelectedPub(form.getCvId(), currentPsnId, form.getModuleId()));
      }
      model.addObject("pubVO", form);
    } catch (Exception e) {
      logger.error(
          "弹出人员简历成果编辑框出错， psnId = " + currentPsnId + ", CVID = " + form.getCvId() + ", moduleId=" + form.getModuleId(),
          e);
    }
    return model;
  }

  /**
   * 简历成果信息
   * 
   * @return
   */
  @RequestMapping("/pub/resume/ajaxpubinfo")
  public ModelAndView buildPsnResumePubInfo(PersonalResumePubVO form) {
    ModelAndView model = new ModelAndView("/pub/resume/resume_pub_info");
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      personalResumePubService.buildPsnResumePubInfo(form);
      model.addObject("pubVO", form);
    } catch (Exception e) {
      logger.error("获取简历代表性成果出错， psnId = " + SecurityUtils.getCurrentUserId() + ", resumeId=" + form.getResumeId(), e);
    }
    return model;
  }

  /**
   * 保存代表性成果信息
   * 
   * @return
   */
  @RequestMapping("/pub/resume/ajaxsaverepresentpub")
  @ResponseBody
  public String savePsnCVRepresentPubInfo(PersonalResumePubVO form) {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      boolean result = personalResumePubService.checkPubInfo(form);
      if (result) {
        personalResumePubService.savePsnResumeModule(form);
      } else {
        resultMap.put("pubCheck", "false");
      }
      resultMap.put("result", "success");
    } catch (Exception e) {
      resultMap.put("result", "error");
      logger.error("保存个人简历成果信息出错， psnId = " + form.getPsnId() + ", resumeId = " + form.getResumeId(), e);
    }
    return JacksonUtils.mapToJsonStr(resultMap);
  }
}
